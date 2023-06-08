package com.example.proyectofinaldam1.controlers;

import androidx.annotation.Nullable;
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
import com.example.proyectofinaldam1.adapters.RaTournaments;
import com.example.proyectofinaldam1.models.DataBaseJSON;
import com.example.proyectofinaldam1.models.Torneo;
import com.example.proyectofinaldam1.models.Usuario;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class TournamentListActivity extends AppCompatActivity implements DataBaseJSON.UsuarioCallback{

    private Button btnCreateTrn;

    private RecyclerView rvTournaments;

    private List<Torneo> tournaments;

    private RaTournaments raTournaments;

    /**
     * Se encarga de inicializar y configurar los elementos de la interfaz de usuario,
     * así como de establecer los eventos de click y cargar la lista de torneos desde la base de datos.
     * Si el usuario actual no está registrado, deshabilita el botón de creación de torneos.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_list);
        // Configurar el color de la barra de estado
        getWindow().setStatusBarColor(Color.parseColor("#000000"));

        btnCreateTrn = (Button) findViewById(R.id.btnAddTrn);
        rvTournaments = (RecyclerView) findViewById(R.id.rvTrns);

        tournaments = new ArrayList<>();

        LinearLayoutManager layout = new LinearLayoutManager(this);
        raTournaments = new RaTournaments(tournaments);

        rvTournaments.setAdapter(raTournaments);
        rvTournaments.setLayoutManager(layout);
        // Verificar si el usuario actual está registrado
        if (DataBaseJSON.userFirebase == null){
            // El usuario no está registrado, deshabilitar el botón de creación de torneos
            btnCreateTrn.setEnabled(false);
        }
        // Establecer el evento de click del botón de creación de torneos
        btnCreateTrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad de creación de torneos
                Intent intentTrnCreate = new Intent(TournamentListActivity.this, TournamentCreateActivity.class);
                startActivity(intentTrnCreate);
            }
        });
        Gson gson = new Gson();
        raTournaments.setOnItemClickListener(new RaTournaments.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    // Obtener el torneo seleccionado
                    Torneo posTrn = tournaments.get(position);
                    // Convertir el torneo a JSON
                    String strIntent = gson.toJson(posTrn);
                    // Abrir la actividad de detalles del torneo seleccionado
                    Intent intentToTrn = new Intent(TournamentListActivity.this, TournamentActivity.class);
                    intentToTrn.putExtra("activity_anterior",strIntent);
                    startActivity(intentToTrn);
                }catch (Exception ex) {
                    Log.e("to trn", "onItemClick: " + ex.getMessage());
                }
            }
        });
        // Cargar la lista de torneos desde la base de datos
        DataBaseJSON.GetTrnsTask getTournaments = new DataBaseJSON.GetTrnsTask(this, this);
        getTournaments.execute();
    }

    /**
     * Cargaremos otra vez la lista de torneos cuando creemos el torneo
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DataBaseJSON.GetTrnsTask getTournaments = new DataBaseJSON.GetTrnsTask(this, this);
        getTournaments.execute();
    }

    /**
     * Este metodo no se usa
     * @param usuario
     */
    @Override
    public void onUsuarioObtenido(Usuario usuario) {}

    /**
     * Este metodo no se usa
     * @param users
     */
    @Override
    public void onUsersObtenido(List<Usuario> users) {}

    /**
     * Guardaremos los torneos en local y actualizaremos el recicler adapter
     * @param torneos
     */
    @Override
    public void onTrnsObtenido(List<Torneo> torneos) {
        tournaments = torneos;
        raTournaments.setTournaments(tournaments);
    }
}