package com.example.proyectofinaldam1.controlers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinaldam1.R;
import com.example.proyectofinaldam1.models.DataBaseJSON;
import com.example.proyectofinaldam1.models.Torneo;
import com.example.proyectofinaldam1.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class LoggingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvToRegister;
    private EditText etGMAIL;
    private EditText etPW;
    private Button btnLog;
    public static interface UserFBCallback {
        void onUser(FirebaseUser user);
    }

    /**
     * Este metodo inicia la vista y carga los componentes
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);

        getWindow().setStatusBarColor(Color.parseColor("#000000"));

        tvToRegister = (TextView) findViewById(R.id.txtToRegister);
        etGMAIL = (EditText) findViewById(R.id.edtgmailLI);
        etPW = (EditText) findViewById(R.id.edtContrasenaLI);
        btnLog = (Button) findViewById(R.id.btnLogIn);

        tvToRegister.setOnClickListener(this);
        btnLog.setOnClickListener(this);
        if (getIntent().getStringExtra("email" )!= null){
            etPW.setText(getIntent().getStringExtra("password"));
            etGMAIL.setText(getIntent().getStringExtra("email"));
        }
    }

    /**
     * Cuando el usuario hace clic en el botón de inicio de sesión, se verifica si los campos de
     * correo electrónico y contraseña no están vacíos. Si ambos campos no están vacíos, se llama a
     * un método de inicio de sesión con los valores del correo electrónico y la contraseña.
     * Si el inicio de sesión es exitoso, la actividad finaliza y se devuelve el usuario actual a
     * la actividad anterior.
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Intent intentLog = null;
        switch (v.getId()) {
            case R.id.txtToRegister:
                intentLog = new Intent(LoggingActivity.this, RegisterActivity.class);
                startActivity(intentLog);
                break;
            case R.id.btnLogIn:
                if (TextUtils.isEmpty(etGMAIL.getText().toString())){
                    etGMAIL.setError("Introduce un EMAIL");
                }else{
                    if (TextUtils.isEmpty(etPW.getText().toString())){
                        etPW.setError("Introduce una contraseña");
                    }else{
                        if (etPW.getText().toString().length() < 6){
                            etPW.setError("La contraseña debe tener al menos 6 caracteres");
                        }else{
                            initSesion(etGMAIL.getText().toString(), etPW.getText().toString(), new UserFBCallback() {
                                @Override
                                public void onUser(FirebaseUser user) {
                                    DataBaseJSON.userFirebase = user;
                                    Intent intent = new Intent(LoggingActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(LoggingActivity.this, "Sesion iniciada", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
        }
    }

    /**
     * al método initSesion con los valores del correo electrónico y la contraseña. Este método
     * intenta iniciar sesión en Firebase con los valores del correo electrónico y la contraseña
     * proporcionados. Si el inicio de sesión es exitoso, se llama al método onUser de la interfaz
     * UserFBCallback para devolver el usuario actual.
     * @param email
     * @param password
     * @param callback
     */
    private void initSesion(String email,String password,UserFBCallback callback){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                        callback.onUser(user);

                    } else {
                        Toast.makeText(LoggingActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    /**
     * Si el usuario a vuelto del logging se autocompletaran los datos introducidos anteriormente
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
        Log.e("asds ", "onActivityResult: " + getIntent().getStringExtra("password"));
        if (resultCode == RESULT_OK){
            etPW.setText(getIntent().getStringExtra("password"));
            etGMAIL.setText(getIntent().getStringExtra("email"));
        }
    }
}