package com.example.proyectofinaldam1.controlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.proyectofinaldam1.R;
import com.example.proyectofinaldam1.adapters.RaRanking;
import com.example.proyectofinaldam1.models.DataBaseJSON;
import com.example.proyectofinaldam1.models.Torneo;
import com.example.proyectofinaldam1.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RankingListActivity extends AppCompatActivity implements DataBaseJSON.UsuarioCallback {
    public RecyclerView rvRnk;
    private static List<Usuario> users;
    private RaRanking raRanking;

    /**
     * Aquí se configura la interfaz de usuario y se realizan las inicializaciones necesarias.
     * Se obtienen datos del intent y se realiza la tarea de obtener usuarios de la base de datos.
     * Se establece el adaptador y el administrador de diseño del RecyclerView.
     * Además, se añade un listener al adaptador para manejar eventos de clic en los elementos del RecyclerView.
     * Cuando se hace clic en un elemento, se abre la actividad UserActivity con los detalles del usuario seleccionado.
     * @param savedInstanceState El estado previamente guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list);

        getWindow().setStatusBarColor(Color.parseColor("#000000"));
        Gson gson = new Gson();
        rvRnk = (RecyclerView) findViewById(R.id.rvRnk);
        String strtrn = getIntent().getStringExtra("activity_anterior");

        Torneo trn = null;
        int tierSearch = getIntent().getIntExtra("Tier",-1);
        if (strtrn == null){
            DataBaseJSON.GetUsersTask getUsersTask = new DataBaseJSON.GetUsersTask(tierSearch,this);
            getUsersTask.execute();
            users = new ArrayList<>();
        }else{
            trn = gson.fromJson(strtrn,Torneo.class);
            users = new ArrayList<>();
            DataBaseJSON.GetUsersTask getUsersTask = new DataBaseJSON.GetUsersTask(trn.getUsersList(),this);
            getUsersTask.execute();
        }

        LinearLayoutManager layout = new LinearLayoutManager(this);
        raRanking = new RaRanking(users);
        rvRnk.setAdapter(raRanking);
        rvRnk.setLayoutManager(layout);
        raRanking.setOnItemClickListener(new RaRanking.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Usuario posUser = users.get(position);
                String strIntent = gson.toJson(posUser);
                Intent intentToUser = new Intent(RankingListActivity.this,UserActivity.class);
                intentToUser.putExtra("activity_anterior",strIntent);
                startActivity(intentToUser);
            }
        });
    }

    /**
     * Este netido no se usa
     * @param usuario
     */
    @Override
    public void onUsuarioObtenido(Usuario usuario) {

    }

    /**
     * En este metodo devuelvo una lista de usuarios de la base de datos
     * @param usuarios
     */
    @Override
    public void onUsersObtenido(List<Usuario> usuarios) {
        users = usuarios;
        raRanking.setUsers(users);
    }

    /**
     * Este metodo no se utiliza
     * @param torneos
     */
    @Override
    public void onTrnsObtenido(List<Torneo> torneos) {

    }
}