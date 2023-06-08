package com.example.proyectofinaldam1.controlers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectofinaldam1.R;
import com.example.proyectofinaldam1.models.DataBaseJSON;
import com.example.proyectofinaldam1.models.Torneo;

import java.util.Random;

public class TournamentCreateActivity extends AppCompatActivity {
    private Button btnCreate;
    private EditText etTitle;
    private EditText etInfo;
    private EditText etMaxPlayer;
    private EditText etPrecio;

    /**
     * Método que se ejecuta al crear la actividad TournamentCreateActivity.
     *
     * @param savedInstanceState Objeto Bundle que contiene el estado anterior de la actividad, o null si no hay estado previo.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_create);

        getWindow().setStatusBarColor(Color.parseColor("#000000"));

        etTitle = (EditText) findViewById(R.id.edtTitleCreate);
        etInfo = (EditText) findViewById(R.id.edtCreateInfo);
        etMaxPlayer = (EditText) findViewById(R.id.edtMxJP);
        etPrecio = (EditText) findViewById(R.id.edtPrecioEntr);
        btnCreate = (Button) findViewById(R.id.btnCreateTrn);
        // Obtención de los valores ingresados por el usuario
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtención de los valores ingresados por el usuario
                String strTitle = etTitle.getText().toString();
                String strInfo = etInfo.getText().toString();
                String strMaxPlayer = etMaxPlayer.getText().toString();
                String strPrecio = etPrecio.getText().toString();

                if (!TextUtils.isEmpty(strTitle)){
                    if (!TextUtils.isEmpty(strInfo)){
                        try {
                            if (!TextUtils.isEmpty(strMaxPlayer)){
                                if (!TextUtils.isEmpty(strPrecio)){
                                    // Creación del objeto Torneo con los valores ingresados
                                    Torneo torneo = new Torneo(new Random().nextInt(500000),DataBaseJSON.fbAuth.getUid(),strTitle,strInfo,Integer.parseInt(strMaxPlayer),Integer.parseInt(strPrecio));
                                    // Llamada al método createTournament para crear el torneo en la base de datos
                                    int funciona = DataBaseJSON.createTournament(torneo);
                                    createToast("Torneo creado");
                                    onBackPressed();
                                }else{
                                    createToast("El torneo necesita un precio por entrada");
                                }
                            }else{
                                createToast("El torneo debe tener un limite de Jugadores");
                            }
                        }catch (Exception ex){
                            Toast.makeText(TournamentCreateActivity.this, "Introduce numeros en los campos del precio y jugadores", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        createToast("El torneo debe tiener informacion" );
                    }
                }else{
                    createToast("El titulo está vacio");
                }
            }
        });
    }

    /**
     * Método utilizado para mostrar un mensaje Toast con la información proporcionada
     * @param info
     */
    private void createToast(String info){
        Toast.makeText(this,info,Toast.LENGTH_SHORT).show();
    }
}