package com.example.proyectofinaldam1.controlers;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinaldam1.R;
import com.example.proyectofinaldam1.models.AnimatedCharapter;
import com.example.proyectofinaldam1.models.DataBaseJSON;
import com.example.proyectofinaldam1.models.Set;
import com.example.proyectofinaldam1.models.Torneo;
import com.example.proyectofinaldam1.models.Usuario;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    private TextView tvUser;
    private TextView tvPoints;
    private ImageView ivTier;
    private TextView tvGames;
    private TextView tvRonda;
    private TextView tvOP;
    private ImageView ivJP2;
    private ImageView ivStageJP2;
    private Button btnNext;
    private Button btnpost;
    private List<Torneo> torneos;
    private List<Set> sets;
    private int pos;
    private DataBaseJSON.GetTrnsTask getTrnsTask;
    private DataBaseJSON.GetSetsTask getSetsTask;
    private Handler opHandler;
    private AnimatedCharapter animeOP;

    /**
     * Si la actividad anterior es PrimerActivity, obtiene los datos del usuario desde la base de datos y muestra los puntos
     * y según la puntos como imagen principal saldra será una o otra
     * Si la actividad anterior no es PrimerActivity, muestra el nombre de usuario recibido como intent.
     * @param savedInstanceState El estado anterior de la actividad, si existe.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getWindow().setStatusBarColor(Color.parseColor("#000000"));

        tvUser = (TextView) findViewById(R.id.txtUserName);
        tvPoints = (TextView) findViewById(R.id.txtPointsUser);
        ivTier = (ImageView) findViewById(R.id.ivLvlUser);
        Gson gson = new Gson();
        String strIntent = getIntent().getStringExtra("activity_anterior");
        // Si la actividad anterior es PrimerActivity
        if (strIntent.equals("PrimerActivity")){
            // Mostraremos el nombre del usuario obtenido desde la base de datos
            tvUser.setText(DataBaseJSON.userFirebase.getDisplayName());
            tvPoints.setText("Cargando");
            DataBaseJSON.getUsuario(DataBaseJSON.userFirebase.getUid(), new DataBaseJSON.UsuarioCallback() {
                @Override
                public void onUsuarioObtenido(Usuario usuario) {
                    // Verificar si se obtuvo un usuario válido
                    if (usuario != null){
                        tvPoints.setText("Puntos: " + usuario.getPoints());
                        // Estableceremos la imagen correspondiente en función de los puntos del usuario
                        if (usuario.getPoints() <= 350){
                            ivTier.setImageResource(R.drawable.icono_dificultad_normal_ssbb);
                        }else{
                            if (usuario.getPoints() <= 700){
                                ivTier.setImageResource(R.drawable.icono_dificultad_dificil_ssbb);
                            }else{
                                if (usuario.getPoints() <= 1200){
                                    ivTier.setImageResource(R.drawable.icono_dificultad_muy_dificil_ssbb);
                                }else{
                                    ivTier.setImageResource(R.drawable.icono_dificultad_maximo_ssbb);
                                }
                            }
                        }
                    }
                }
                @Override
                public void onUsersObtenido(List<Usuario> users) {}

                @Override
                public void onTrnsObtenido(List<Torneo> torneos) {}
            });
        }else{
            //Si no es La primera actividad cargaremos la informacion de la otra actividad
            Usuario usr = gson.fromJson(strIntent,Usuario.class);
            tvUser.setText(usr.getNick());
            tvPoints.setText("Puntos: " + usr.getPoints());
            if (usr.getPoints() <= 350){
                ivTier.setImageResource(R.drawable.icono_dificultad_normal_ssbb);
            }else{
                if (usr.getPoints() <= 700){
                    ivTier.setImageResource(R.drawable.icono_dificultad_dificil_ssbb);
                }else{
                    if (usr.getPoints() <= 1200){
                        ivTier.setImageResource(R.drawable.icono_dificultad_muy_dificil_ssbb);
                    }else{
                        ivTier.setImageResource(R.drawable.icono_dificultad_maximo_ssbb);
                    }
                }
            }
        }
        //Crearemos las views para la vista de los usuarios
        tvRonda = (TextView) findViewById(R.id.tvRndUser);
        tvGames = (TextView) findViewById(R.id.tvResultUser);
        tvOP = (TextView) findViewById(R.id.tvNickOp);
        ivStageJP2 = (ImageView) findViewById(R.id.setStage2);
        ivJP2 = (ImageView) findViewById(R.id.ivJP2User);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnpost = (Button) findViewById(R.id.btnPost);
        //Crearemos la vista de los usuarios
        torneos = new ArrayList<>();
        sets = new ArrayList<>();
        //Crearemos la animación del oponente en las estadisticas
        animeOP = new AnimatedCharapter(new int[]{R.drawable.hidle1,R.drawable.hidle2,R.drawable.hidle3,R.drawable.hidle4,R.drawable.hidle5},
                new int[]{R.drawable.hrun1,R.drawable.hrun2,R.drawable.hrun3,R.drawable.hrun4,R.drawable.hrun5},
                new int[]{R.drawable.hattack1,R.drawable.hattack2,R.drawable.hattack3,R.drawable.hattack4,R.drawable.hattack5},
                new int[]{R.drawable.hdeath1,R.drawable.hdeath2,R.drawable.hdeath3,R.drawable.hdeath4,R.drawable.hdeath5},
                R.drawable.hshield);
        //Guardaremos los torneos de la base de datos
        getTrnsTask = new DataBaseJSON.GetTrnsTask(DataBaseJSON.userFirebase.getUid(), new DataBaseJSON.UsuarioCallback() {
            @Override
            public void onUsuarioObtenido(Usuario usuario) {}
            @Override
            public void onUsersObtenido(List<Usuario> users) {}
            @Override
            public void onTrnsObtenido(List<Torneo> trns) {
                torneos = trns;
                //Si el usuario a participado en torneos recogeremos los sets de ese torneo
                if (torneos != null && torneos.size()>= 1){
                    getSetsTask = new DataBaseJSON.GetSetsTask(torneos, new DataBaseJSON.SetCallback() {
                        @Override
                        public void onGetSets(List<Set> ss) {
                            sets = ss;
                            //Si hay sets mostraremos los botones y la informacion de los sets
                            if (sets != null && sets.size() > 0){
                                setvisibleJP2();
                                pos = 0;
                                if (pos == sets.size()-1){
                                    btnNext.setVisibility(View.INVISIBLE);
                                }else{
                                    btnNext.setVisibility(View.VISIBLE);
                                }
                                if (pos == 0){
                                    btnpost.setVisibility(View.INVISIBLE);
                                }else{
                                    btnpost.setVisibility(View.VISIBLE);
                                }
                                //Moveremos el sprite
                                moveOP();
                                setStadisticView();
                            }
                        }
                    });
                    getSetsTask.execute();
                }
            }
        });
        getTrnsTask.execute();
        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos--;
                setStadisticView();
                if (pos == 0){
                    btnpost.setVisibility(View.INVISIBLE);
                }
                if (pos < sets.size()){
                    btnNext.setVisibility(View.VISIBLE);
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos++;
                setStadisticView();
                if (pos == sets.size()-1){
                    btnNext.setVisibility(View.INVISIBLE);
                }
                if (pos > 0){
                    btnpost.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    /**
     * Con este método haremos visibles la parte de las estadisticas
     */
    private void setvisibleJP2(){
        tvGames.setVisibility(View.VISIBLE);
        tvOP.setVisibility(View.VISIBLE);
        tvRonda.setVisibility(View.VISIBLE);
        ivStageJP2.setVisibility(View.VISIBLE);
        ivJP2.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.VISIBLE);
        btnpost.setVisibility(View.VISIBLE);
    }
    /**
     * Este método escribiremos en las estadisticas el nombre del torneo
     *
     * @param uid
     * @return
     */
    private String setTvRonda(int uid){
        String aux = "No se encuentra la app";
        for (int i = 0; i < torneos.size(); i++) {
            if (torneos.get(i) != null &&  torneos.get(i).getSets() != null && torneos.get(i).getSets().contains("" + uid)){
                aux = torneos.get(i).getName();
            }
        }
        return aux;
    }

    /**
     * En este metodo mostraremos los datos de los sets en el las estadisticas
     */
    private void setStadisticView(){
        Set setAux = sets.get(pos);
        tvRonda.setText(setTvRonda(setAux.getUid()) + " | " + setAux.getRound());
        if (setAux.getUid_j1().getUid().equals(DataBaseJSON.userFirebase.getUid())){
            if (setAux.getUid_j2() != null){
                tvOP.setText(setAux.getUid_j2().getNick());
            }else{
                tvOP.setText("?");
            }
        }else{
            if (setAux.getUid_j1() != null){
                tvOP.setText(setAux.getUid_j1().getNick());
            }else{
                tvOP.setText("?");
            }
        }
        int point1 = 0;
        int point2 = 0;
        if (setAux.getGames() != null){
            for (int i = 0; i < setAux.getGames().size(); i++) {
                if (setAux.getGames().get(i) == 1){
                    point2++;
                }else{
                    if (setAux.getGames().get(i) == 2){
                        point1++;
                    }
                }
            }
            tvGames.setText("JP1 " + point2 + " - " + point1 + " JP2");
        }else{
            tvGames.setText("JP1 0 - 0 JP2");
        }
    }

    /**
     * Este metodo se encarga de la movilidad del sprite de las estadisticas
     */
    private void moveOP(){
        opHandler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                animeOP.changeImageSet(new AnimatedCharapter.SetListImg() {
                    @Override
                    public void setListChr(int[] listImg, int index) {
                        if(listImg.length != 5){
                            ivJP2.setImageResource(listImg[0]);
                        }else{
                            ivJP2.setImageResource(listImg[index]);
                        }
                    }
                });
                opHandler.postDelayed(this,150);
            }
        };
        opHandler.postDelayed(run,150);
    }
}