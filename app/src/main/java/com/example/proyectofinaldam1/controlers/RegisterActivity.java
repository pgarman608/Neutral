package com.example.proyectofinaldam1.controlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinaldam1.R;
import com.example.proyectofinaldam1.models.DataBaseJSON;
import com.example.proyectofinaldam1.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edGMAIL;
    private EditText edPW;
    private Button btnRegister;
    private EditText edNick;
    private TextView tvTolog;

    public static interface UserFBCallback {
        void onUser(FirebaseUser user);
    }

    /**
     * Aquí se configura la interfaz de usuario y se realizan las inicializaciones necesarias.
     * Se establece el color de la barra de estado. Se obtiene una instancia de FirebaseAuth.
     * Se obtienen las referencias a los elementos de la interfaz de usuario, como los EditText y los botones.
     * Se establecen listeners de clic en el botón de registro y en el texto para ir al inicio de sesión.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setStatusBarColor(Color.parseColor("#000000"));

        edGMAIL = (EditText) findViewById(R.id.edtgmailR);
        edPW = (EditText) findViewById(R.id.edtPWR);
        edNick = (EditText) findViewById(R.id.edtNickR);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvTolog = (TextView) findViewById(R.id.tvToLogin);

        tvTolog.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    /**
     * Se verifica el ID de la vista que generó el evento y se realiza una acción correspondiente.
     * Si se hace clic en el botón de registro, se llama al método "register" para realizar el registro.
     * Luego se crea un intent para abrir la actividad MainActivity y se añade la bandera FLAG_ACTIVITY_CLEAR_TOP
     * para limpiar la pila de actividades y evitar que se pueda volver atrás.
     * Si se hace clic en el texto para ir al inicio de sesión, se crea un intent para abrir la actividad LoggingActivity.
     * Finalmente, se inicia la actividad correspondiente y se finaliza la actividad actual.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Intent intentRegister = null;
        switch (v.getId()) {
            case R.id.btnRegister:
                String email = edGMAIL.getText().toString().trim();
                String password = edPW.getText().toString().trim();
                String nick = edNick.getText().toString().trim();

                // Validar las credenciales de inicio de sesión y el nick

                if (TextUtils.isEmpty(nick)) {
                    edNick.setError("Ingrese un nick para su cuenta");
                } else {
                    if (TextUtils.isEmpty(email)) {
                        edGMAIL.setError("Ingrese su dirección de correo electrónico");
                    } else {
                        if (TextUtils.isEmpty(password)) {
                            edPW.setError("Ingrese su contraseña");
                        } else {
                            if (password.length() < 6) {
                                edPW.setError("La contraseña debe tener al menos 6 caracteres");
                            } else {
                                DataBaseJSON.createUser(nick, password, email);
                                intentRegister = new Intent(RegisterActivity.this, LoggingActivity.class);
                                intentRegister.putExtra("password", password);
                                intentRegister.putExtra("email", email);
                                startActivity(intentRegister);
                            }
                        }
                    }
                }
                break;
            case R.id.tvToLogin:
                intentRegister = new Intent(RegisterActivity.this, LoggingActivity.class);
                startActivity(intentRegister);
                finish();
                break;
        }
    }
}