package com.example.proyectofinaldam1.controlers;

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
import com.google.gson.Gson;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
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
            btnJoins.setEnabled(false);
        }
        //Si no existe la lista de los sets no mostraremos el boton para los sets
        if (torneo.getSets() == null){
            btnSets.setEnabled(false);
        }
        //Si los sets no están creados y somos el creador el torneo mostraremos el btn de start
        if (DataBaseJSON.userFirebase == null ||!DataBaseJSON.userFirebase.getUid().equals(""+torneo.getUidCreator())){
            btnStar.setVisibility(View.INVISIBLE);
        }
        // Obtener la lista de usuarios del torneo desde la base de datos
        DataBaseJSON.GetUsersTask getUsersTask = new DataBaseJSON.GetUsersTask(torneo.getUsersList(),this);
        getUsersTask.execute();
        usuarios = null;
        // Obtener el usuario actual desde la base de datos
        if (DataBaseJSON.userFirebase != null){
            DataBaseJSON.getUsuario(DataBaseJSON.userFirebase.getUid(),this);
        }
        //Cuando pulsemos el btn de start
        btnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usuarios != null){
                    Set setTmp = null;
                    //Ordenadaremos la lista de usuarios
                    Collections.sort(usuarios, new Comparator<Usuario>() {
                        @Override
                        public int compare(Usuario u1, Usuario u2) {
                            return Integer.compare(u1.getPoints(), u2.getPoints());
                        }
                    });
                    //Crearemos los sets con el numero de usuarios
                    for (int i = 0; i < usuarios.size(); i+=2) {
                        if (usuarios.size() != i+1 && usuarios.get(i+1) != null){
                            setTmp = new Set(torneo.getUid(),new Random().nextInt(500000),usuarios.get(i),usuarios.get(i+1),createCaracter()+""+1);
                        }else{
                            setTmp = new Set(torneo.getUid(),new Random().nextInt(500000),usuarios.get(i),null,createCaracter()+""+1);
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
                    DataBaseJSON.setTrn(torneo);
                    Toast.makeText(TournamentActivity.this, "Torneo empezado", Toast.LENGTH_SHORT).show();
                    btnStar.setEnabled(false);
                    btnSets.setEnabled(true);
                }
            }
        });
        //Cuando pulsemos en el btn de sets veremos los sets disponibles en otra activity
        btnSets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TournamentActivity.this, SetListActivity.class);
                if (torneo.getUsersList() != null || torneo.getUsersList().size() >0){
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
                if (torneo.getUsersList() == null){
                    torneo.setUsersList(new ArrayList<>());
                }
                torneo.setUsersList(torneo.getUsersList().size(),DataBaseJSON.userFirebase.getUid());
                torneo.setAllPoints(torneo.getAllPoints() + usuario.getPoints());
                tvPoints.setText("" + torneo.getAllPoints());
                tvPlayers.setText(""+torneo.getUsersList().size());
                Toast.makeText(TournamentActivity.this, "Te has unido a :" + torneo.getName(), Toast.LENGTH_SHORT).show();
                DataBaseJSON.setTrn(torneo);
                btnJoins.setEnabled(false);
            }
        });
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
    @Override
    public void onUsuarioObtenido(Usuario usuario) {
        this.usuario = usuario;
    }
    @Override
    public void onUsersObtenido(List<Usuario> users) {
        this.usuarios = users;
    }
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