package com.example.proyectofinaldam1.controlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinaldam1.R;
import com.example.proyectofinaldam1.models.DataBaseJSON;
import com.example.proyectofinaldam1.models.Set;
import com.example.proyectofinaldam1.models.Torneo;
import com.example.proyectofinaldam1.models.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TournamentActivity extends AppCompatActivity implements DataBaseJSON.UsuarioCallback{

    private TextView tvTitle;
    private TextView tvInfo;
    private TextView tvPoints;
    private TextView tvPlayers;
    private TextView tvMPlayers;
    private Button btnJPS;
    private Button btnSets;
    private Button btnStar;
    private Button btnJoins;
    private List<Usuario> usuarios;
    private Torneo torneo;
    private static Usuario usuario;

    /**
     * Se encarga de inicializar y configurar los elementos de la interfaz de usuario,
     * así como de establecer los eventos de click y cargar la información del torneo desde la base de datos.
     * Si el usuario actual no está registrado, no puede unirse al torneo ni iniciar el torneo.
     * Tampoco lo va a poder iniciar el torneo
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);
        getWindow().setStatusBarColor(Color.parseColor("#000000"));
        // Inicializar elementos de la interfaz de usuario
        tvTitle = (TextView) findViewById(R.id.tvTitleTrns);
        tvInfo = (TextView) findViewById(R.id.tvInfoTrns);
        tvPoints = (TextView) findViewById(R.id.tvPointTrn);
        tvPlayers = (TextView) findViewById(R.id.tvNJP);
        tvMPlayers = (TextView) findViewById(R.id.tvMJP);

        btnJPS = (Button) findViewById(R.id.btnSeePlayers);
        btnSets = (Button) findViewById(R.id.btnSeeSets);
        btnStar = (Button) findViewById(R.id.btnStartTrns);
        btnJoins = (Button) findViewById(R.id.btnUnite);

        Gson gson = new Gson();
        // Obtener la información del torneo desde el intent
        String strIntent = getIntent().getStringExtra("activity_anterior");
        torneo = gson.fromJson(strIntent,Torneo.class);
        // Mostrar la información del torneo en los elementos de la interfaz de usuario
        tvTitle.setText(torneo.getName());
        tvInfo.setText(torneo.getInfo());
        if (torneo.getUsersList() == null){
            tvPlayers.setText(""+0);
        }else{
            tvPlayers.setText(""+torneo.getUsersList().size());
        }
        tvMPlayers.setText(""+torneo.getNumMaxUsers());
        tvPoints.setText("" + torneo.getAllPoints());
        /**
         * El boton de unirse solo será visible cuando:
         * la cuenta esté iniciada
         * cuando no esté ya dentro del torneo
         * cuando el torneo no tenga sets
         * cuando el numero de usuario no sea superior al numero de usuarios
         */
        if (DataBaseJSON.userFirebase == null || existUsrTrn() != 0 || torneo.getSets() != null || torneo.getEnd() != 0) {
            btnJoins.setVisibility(View.INVISIBLE);
        }
        //Si no existe la lista de los sets no mostraremos el boton para los sets
        if (torneo.getSets() == null){
            btnSets.setVisibility(View.INVISIBLE);
        }
        //Si los sets no están creados y somos el creador el torneo mostraremos el btn de start
        if (DataBaseJSON.userFirebase == null ||!DataBaseJSON.userFirebase.getUid().equals(""+torneo.getUidCreator()) || torneo.getSets() != null){
            btnStar.setVisibility(View.INVISIBLE);
        }
        comprSet();
        // Obtener la lista de usuarios del torneo desde la base de datos
        usuarios = new ArrayList<>();
        cargarDatos(null);
        // Obtener el usuario actual desde la base de datos
        if (DataBaseJSON.userFirebase != null){
            DataBaseJSON.getUsuario(DataBaseJSON.userFirebase.getUid(),this);
        }
        //Cuando pulsemos el btn de start
        btnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDatos(new Runnable() {
                    @Override
                    public void run() {
                        if (torneo.getUsersList() != null){
                            Set setTmp = null;
                            //Ordenadaremos la lista de usuarios
                            Collections.sort(usuarios, new Comparator<Usuario>() {
                                @Override
                                public int compare(Usuario u1, Usuario u2) {
                                    return Integer.compare(u1.getPoints(), u2.getPoints());
                                }
                            });
                            Log.e("lolo", "onUsersObtenido: " + usuarios.size());
                            //Crearemos los sets con el numero de usuarios
                            for (int i = 0; i < usuarios.size(); i+=2) {
                                //necesotamos comprobar que hay dos jugadores o mas
                                if (usuarios.size() != i+1 && usuarios.get(i+1) != null){
                                    //Creamos un set con los dos jugadores
                                    Toast.makeText(TournamentActivity.this, "Torneo empezado", Toast.LENGTH_SHORT).show();
                                    setTmp = new Set(torneo.getUid(),new Random().nextInt(500000),usuarios.get(i),usuarios.get(i+1),createCaracter()+""+1);
                                    //Si un jugador no tiene la lista de lod ids de los torneos la crearemos y le añadiremos la id
                                    if (usuarios.get(i).getGames() == null || usuarios.get(i).getGames().isEmpty()){
                                        usuarios.get(i).setGames(new ArrayList<>());
                                        usuarios.get(i).setGames(0,"" + setTmp.getUid());
                                        DataBaseJSON.setUsuario(usuarios.get(i));
                                    }else{
                                        usuarios.get(i).setGames(usuarios.get(i).getGames().size()-1,"" + setTmp.getUid());
                                    }
                                }else{
                                }
                                //Guardaremos los sets en la lista de sets del torneo
                                if (i == 0){
                                    torneo.setSets(0,""+setTmp.getUid());
                                }else {
                                    torneo.setSets(i / 2, "" + setTmp.getUid());
                                }
                                //Crearemos los sets
                                DataBaseJSON.createSet(setTmp);
                            }
                            //Modificaremos el set
                            DataBaseJSON.setTrn(torneo);
                            btnStar.setVisibility(View.INVISIBLE);
                            btnSets.setVisibility(View.VISIBLE);
                        }else{
                            Toast.makeText(TournamentActivity.this, "WHAT", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        //Cuando pulsemos en el btn de sets veremos los sets disponibles en otra activity
        btnSets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TournamentActivity.this, SetListActivity.class);
                if (torneo.getUsersList() != null && torneo.getUsersList().size() >0){
                    intent.putExtra("activity_anterior",""+torneo.getUid());
                    startActivity(intent);
                }else{
                    Toast.makeText(TournamentActivity.this, "La lista de usuarios esta vacia", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Cuando pulsemos el btn de los jps veremos los usuarios dentro del torneo
        btnJPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentJPS = new Intent(TournamentActivity.this,RankingListActivity.class);
                intentJPS.putExtra("activity_anterior",gson.toJson(torneo));
                startActivity(intentJPS);
            }
        });
        //Nos uniremos a la lista de usuarios cuando pulsemos el btn de join
        btnJoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si no hay una lista de usuarios, la creamos
                if (torneo.getUsersList() == null){
                    torneo.setUsersList(new ArrayList<>());
                }
                cargarDatos(new Runnable() {
                    @Override
                    public void run() {
                        DataBaseJSON.getUsuario(DataBaseJSON.userFirebase.getUid(), new DataBaseJSON.UsuarioCallback() {
                            @Override
                            public void onUsuarioObtenido(Usuario usuario) {
                                //Si el usuario no tiene una lista de ids de torenos la crearemos y se la añadiremos
                                if (usuario.getUidTournament() == null ||  usuario.getUidTournament().isEmpty()){
                                    usuario.setUidTournament(new ArrayList<>());
                                    usuario.setUidTournament(0,"" + torneo.getUid());
                                }else{
                                    usuario.setUidTournament(usuario.getUidTournament().size()-1,"" + torneo.getUid());
                                }
                                usuarios.add(usuario);
                                Toast.makeText(TournamentActivity.this, "Usuario añadido al torneo", Toast.LENGTH_SHORT).show();
                                DataBaseJSON.setUsuario(usuario);
                            }
                            @Override
                            public void onUsersObtenido(List<Usuario> users) {}
                            @Override
                            public void onTrnsObtenido(List<Torneo> torneos) {}
                        });

                        if (torneo.getUsersList() != null ){
                            torneo.setUsersList(torneo.getUsersList().size(),DataBaseJSON.userFirebase.getUid());
                            tvPlayers.setText(""+torneo.getUsersList().size());
                        }else{
                            torneo.setUsersList(new ArrayList<>());
                            torneo.setUsersList(0,DataBaseJSON.userFirebase.getUid());
                            tvPlayers.setText("1");
                        }
                        //Mostraremos la información que hemos modificado en la vista
                        torneo.setAllPoints(torneo.getAllPoints() + usuario.getPoints());
                        tvPoints.setText("" + torneo.getAllPoints());
                        Toast.makeText(TournamentActivity.this, "Te has unido a :" + torneo.getName(), Toast.LENGTH_SHORT).show();
                        DataBaseJSON.setTrn(torneo);
                        btnJoins.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }
    /**
     * Realiza una consulta a la base de datos para actualizar el torneo y comprueba si se han establecido los sets.
     * Si los sets están establecidos, oculta el botón "btnJoins" y muestra el botón "btnSets".
     */
    private void comprSet() {
        DatabaseReference updateTrn = DataBaseJSON.dbFirebase.getReference("Torneos").child(""+torneo.getUid());
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, Object> childData = (HashMap<String, Object>) snapshot.getValue();
                    Gson gson = new Gson();
                    String json = gson.toJson(childData);
                    Torneo torneo1 = gson.fromJson(json, Torneo.class);
                    torneo = torneo1;
                    if (torneo.getSets()!=null){
                        btnJoins.setVisibility(View.INVISIBLE);
                        btnSets.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        updateTrn.addValueEventListener(listener);
    }

    /**
     * Comprobaremos si el usuario esta dentro de la lista de usuarios
     * @return
     */
    private int existUsrTrn(){
        int exist = 0;
        if (torneo.getUsersList() != null){
            for (String usr: torneo.getUsersList()){
                if (usr.equals(DataBaseJSON.userFirebase.getUid())){
                    exist++;
                }
            }
        }
        return exist;
    }

    /**
     * En este metodo cargo los datos del toreno para hacer modificaciones enlocal
     * @param runnable
     */
    private void cargarDatos(Runnable runnable){
        DataBaseJSON.GetTrnsTask trnsTask = new DataBaseJSON.GetTrnsTask(new DataBaseJSON.UsuarioCallback() {
            @Override
            public void onUsuarioObtenido(Usuario usuario) {}

            @Override
            public void onUsersObtenido(List<Usuario> users) {}
            @Override
            public void onTrnsObtenido(List<Torneo> trns) {
                for (int i = 0; i < trns.size(); i++) {
                    if (trns.get(i).getUid() == torneo.getUid()) {
                        //Compruebo que el torneo es el que estoy modifificando
                        torneo = trns.get(i);
                    }
                }
                //Recogo todos los usuarios del torneo
                DataBaseJSON.GetUsersTask UsersTask = new DataBaseJSON.GetUsersTask(torneo.getUsersList(), new DataBaseJSON.UsuarioCallback() {
                    @Override
                    public void onUsuarioObtenido(Usuario usuario) {
                    }

                    @Override
                    public void onUsersObtenido(List<Usuario> users) {
                        usuarios = users;
                        if (runnable != null)
                            runnable.run();
                    }
                    @Override
                    public void onTrnsObtenido(List<Torneo> torneos) {}
                });
                UsersTask.execute();
            }
        });
        trnsTask.execute();
    }
    private void pruebados(){
    }
    /**
     * En este metodo recogo un usuario de la base de datos
     * @param usuario
     */
    @Override
    public void onUsuarioObtenido(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * En este metodo recogo una lista de usuarios
     * @param users
     */
    @Override
    public void onUsersObtenido(List<Usuario> users) {
        this.usuarios = users;
    }

    /**
     * Un metod sin uso en esta vista
     * @param torneos
     */
    @Override
    public void onTrnsObtenido(List<Torneo> torneos) {}

    /**
     * Crearemos una letra por cada set creado
     */
    private int caracter = 64;
    private char createCaracter(){
        caracter++;
        return (char) caracter;
    }
}