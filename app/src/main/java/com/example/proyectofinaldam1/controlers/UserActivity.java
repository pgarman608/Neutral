package com.example.proyectofinaldam1.controlers;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectofinaldam1.R;
import com.example.proyectofinaldam1.models.DataBaseJSON;
import com.example.proyectofinaldam1.models.Torneo;
import com.example.proyectofinaldam1.models.Usuario;
import com.google.gson.Gson;

import java.util.List;

public class UserActivity extends AppCompatActivity {
    private TextView tvUser;
    private TextView tvPoints;
    private ImageView ivTier;

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
    }
}