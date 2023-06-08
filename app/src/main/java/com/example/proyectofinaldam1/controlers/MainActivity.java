package com.example.proyectofinaldam1.controlers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.proyectofinaldam1.R;
import com.example.proyectofinaldam1.models.DataBaseJSON;
import com.example.proyectofinaldam1.models.Torneo;
import com.example.proyectofinaldam1.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button logBtn;
    private Button tnrBtn;
    private Button rnkBtn;
    private androidx.appcompat.view.ActionMode mLog;
    /**
     * Método que se ejecuta al crear la actividad principal.
     * Se inicializan los botones de la interfaz de usuario y se les asigna un listener.
     *También se asigna un listener de larga duración al botón de registro de actividad.
     * Se inicializa la instancia de autenticación de Firebase y se obtiene el usuario actual si existe.
     */
    public static Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(Color.parseColor("#000000"));
        logBtn = (Button) findViewById(R.id.btnLog);
        tnrBtn = (Button) findViewById(R.id.btnTnr);
        rnkBtn = (Button) findViewById(R.id.btnRnk);
        logBtn.setOnClickListener(this);
        tnrBtn.setOnClickListener(this);
        rnkBtn.setOnClickListener(this);
        logBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean active = false;
                if (mLog == null && DataBaseJSON.userFirebase != null){
                    mLog = startSupportActionMode(mLogCallback);
                    active = true;
                }
                return active;
            }
        });
        DataBaseJSON.fbAuth = FirebaseAuth.getInstance();
        if (DataBaseJSON.userFirebase != null){
            getUsuario();
        }
    }

    /**
     * Método que se ejecuta cuando la actividad se inicia.
     * Si hay un usuario logueado en Firebase, se llama al método getUsuario() para obtener los datos del usuario
     * y se establece el texto del botón de inicio de sesión con las primeras tres letras del nombre de usuario.
     * Si no hay un usuario logueado en Firebase, se registra un menú contextual para el botón de inicio de sesión
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (DataBaseJSON.userFirebase != null){
            getUsuario();
            logBtn.setText(DataBaseJSON.userFirebase.getDisplayName().substring(0,3));
        }else{
            registerForContextMenu(logBtn);
        }
    }

    /**
     * El botón con ID "btnLog" verifica si el usuario está registrado y, en función de eso, inicia
     * una actividad de inicio de sesión o una actividad de usuario. Los botones con ID "btnTnr" y
     * "btnRnk" inician las actividades correspondientes sin ninguna condición adicional.
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        Intent intentHome = null;
        switch (view.getId()){
            case R.id.btnLog:
                if (DataBaseJSON.userFirebase == null){
                    intentHome = new Intent(MainActivity.this, LoggingActivity.class);
                }else{
                    intentHome = new Intent(MainActivity.this, UserActivity.class);
                    intentHome.putExtra("activity_anterior", "PrimerActivity");
                }
                startActivity(intentHome);
                break;

            case R.id.btnTnr:
                intentHome = new Intent(MainActivity.this, TournamentListActivity.class);
                startActivity(intentHome);
                break;

            case R.id.btnRnk:
                intentHome = new Intent(MainActivity.this, RankingActivity.class);
                startActivity(intentHome);
                break;

        }
    }

    /**
     * Método que se llama cuando la actividad recibe una respuesta desde otra actividad, en este
     * caso se utiliza para actualizar la información del usuario logueado en la aplicación.ç
     * @param requestCode Código de solicitud que se especificó al iniciar la actividad que se espera que envíe una respuesta.
     * @param resultCode Código de resultado devuelto por la actividad que se espera que envíe una respuesta.
     * @param data Contiene cualquier dato adicional que la actividad que se espera que envíe una respuesta quiera devolver.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(DataBaseJSON.userFirebase != null){
            getUsuario();
            logBtn.setText(usuario.getNick().substring(0,3));
        }
    }

    /**
     * Callback para el modo de acción de registro de actividad.
     * Este Callback es utilizado para crear un menú de opciones en el modo de acción y manejar
     * la selección del usuario en el menú.
     */
    private ActionMode.Callback mLogCallback = new ActionMode.Callback() {
        /**
         * Crea el menú de opciones en el modo de acción.
         *
         * @param mode El ActionMode actual.
         * @param menu El menú de opciones a inflar.
         * @return Devuelve verdadero si el menú de opciones se ha creado correctamente, falso en caso contrario.
         */
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_btn_main,menu);
            return true;
        }
        /**
         * Prepara el menú de opciones en el modo de acción.
         *
         * @param mode El ActionMode actual.
         * @param menu El menú de opciones a preparar.
         * @return Devuelve falso.
         */
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
        /**
         * Maneja la selección del usuario en el menú de opciones del modo de acción.
         * Cuando el usuario pulse en el close user
         * @param mode El ActionMode actual.
         * @param item El ítem del menú que ha sido seleccionado.
         * @return Devuelve verdadero si el ítem del menú ha sido manejado correctamente, falso en caso contrario.
         */
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemId = item.getItemId();
            switch(itemId){
                case R.id.close_user:
                    DataBaseJSON.userFirebase = null;
                    DataBaseJSON.fbAuth.signOut();
                    logBtn.setText("LOG");
                    break;
            }
            return true;
        }
        /**
         * Destruye el modo de acción de registro de actividad.
         *
         * @param mode El ActionMode actual.
         */
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mLog = null;
        }
    };

    /**
     * La función "getUsuario()" llama a un método llamado "getUsuario()" de la clase "DataBaseJSON".
     * Este método recibe un parámetro que es el identificador único del usuario actualmente registrado en Firebase.
     */
    private void getUsuario(){
        DataBaseJSON.getUsuario(DataBaseJSON.userFirebase.getUid(), new DataBaseJSON.UsuarioCallback() {
            @Override
            public void onUsuarioObtenido(Usuario usr) {
                usuario = usr;
            }

            @Override
            public void onUsersObtenido(List<Usuario> users) {}

            @Override
            public void onTrnsObtenido(List<Torneo> torneos) {}
        });
    }
}