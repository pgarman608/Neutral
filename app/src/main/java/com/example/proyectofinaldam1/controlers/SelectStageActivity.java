package com.example.proyectofinaldam1.controlers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.proyectofinaldam1.R;
import com.example.proyectofinaldam1.models.Set;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SelectStageActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivbf;
    private ImageView ivsbf;
    private ImageView ivfd;
    private ImageView ivps2;
    private ImageView ivhb;
    private ImageView ivsv;
    private ImageView ivtc;
    private ImageView ivpk;
    private Button btnSelect;
    private int imgSelect;

    /**
     * Método protegido utilizado para crear y configurar la interfaz de usuario de la actividad de selección de las etapas.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stage);
        // Inicializar y asignar vistas de imágenes
        this.ivbf = (ImageView) findViewById(R.id.ivbf);
        this.ivsbf = (ImageView) findViewById(R.id.ivsbf);

        this.ivfd = (ImageView) findViewById(R.id.ivfd);

        this.ivps2 = (ImageView) findViewById(R.id.ivps2);
        this.ivhb = (ImageView) findViewById(R.id.ivhb);

        this.ivsv = (ImageView) findViewById(R.id.ivsv);
        this.ivtc = (ImageView) findViewById(R.id.ivtc);
        this.ivpk = (ImageView) findViewById(R.id.ivpk);

        btnSelect = (Button) findViewById(R.id.btnSStage);
        // Asignar listener de clic a las imágenes
        this.ivbf.setOnClickListener(this);
        this.ivsbf.setOnClickListener(this);

        this.ivfd.setOnClickListener(this);

        this.ivps2.setOnClickListener(this);
        this.ivhb.setOnClickListener(this);

        this.ivsv.setOnClickListener(this);
        this.ivtc.setOnClickListener(this);
        this.ivpk.setOnClickListener(this);
        // Inicializar y asignar vista del botón de selección
        btnSelect.setOnClickListener(this);
        // Obtener posición desde el intent
        int pos = getIntent().getIntExtra("pos",-1);
        // Ocultar imágenes y texto según la posición
        if (pos == 0){
            this.ivsv.setVisibility(View.INVISIBLE);
            this.ivtc.setVisibility(View.INVISIBLE);
            this.ivpk.setVisibility(View.INVISIBLE);
            findViewById(R.id.tvConter).setVisibility(View.INVISIBLE);
        }
        // Ocultar imágenes y texto según la posición
        imgSelect = 0;
    }

    /**
     * Método llamado cuando se hace clic en las imagenes y btns
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        setInvisibleSelects();
        switch (v.getId()){
            case R.id.ivbf:
                imgSelect = R.drawable.stagebf;
                findViewById(R.id.ivbfselect).setVisibility(View.VISIBLE);
                break;
            case R.id.ivsbf:
                imgSelect = R.drawable.stagesb;
                findViewById(R.id.ivsbfselect).setVisibility(View.VISIBLE);
                break;
            case R.id.ivfd:
                imgSelect = R.drawable.stageff;
                findViewById(R.id.ivfdselect).setVisibility(View.VISIBLE);
                break;
            case R.id.ivps2:
                imgSelect = R.drawable.stageps2;
                findViewById(R.id.ivps2select).setVisibility(View.VISIBLE);
                break;
            case R.id.ivhb:
                imgSelect = R.drawable.stagehb;
                findViewById(R.id.ivhbselect).setVisibility(View.VISIBLE);
                break;
            case R.id.ivsv:
                imgSelect = R.drawable.stagesv;
                findViewById(R.id.ivsvselect).setVisibility(View.VISIBLE);
                break;
            case R.id.ivtc:
                imgSelect = R.drawable.stagetc;
                findViewById(R.id.ivtcselect).setVisibility(View.VISIBLE);
                break;
            case R.id.ivpk:
                imgSelect = R.drawable.stagekalos;
                findViewById(R.id.ivpkselect).setVisibility(View.VISIBLE);
                break;
            case R.id.btnSStage:

                // Crea un intent de retorno y agrega la imagen seleccionada
                Intent returnSet = new Intent();
                returnSet.putExtra("stage",imgSelect);

                // Obtiene la posición y el objeto Set de los extras del intent
                int pos = getIntent().getIntExtra("pos",-1);
                String strSet = getIntent().getStringExtra("set");
                Set set = new Gson().fromJson(strSet,Set.class);

                // Si la lista de stages en el objeto Set es nula, la inicializa
                if (set.getStages() == null){
                    set.setStages(new ArrayList<>());
                }

                // Establece la imagen seleccionada en la posición correspondiente en la lista de stages
                set.setStages(pos, imgSelect);
                returnSet.putExtra("set",new Gson().toJson(set));
                // Establece el resultado de la actividad como RESULT_OK y finaliza la actividad
                setResult(RESULT_OK, returnSet);
                finish();
                break;
        }
        // Habilita el botón de selección si está deshabilitado
        if(!btnSelect.isEnabled()){
            btnSelect.setEnabled(true);
        }
    }

    /**
     * Hacemos visibles todas las imagenes que sean manos
     */
    private void setInvisibleSelects(){
        findViewById(R.id.ivbfselect).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivsbfselect).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivfdselect).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivps2select).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivhbselect).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivsvselect).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivtcselect).setVisibility(View.INVISIBLE);
        findViewById(R.id.ivpkselect).setVisibility(View.INVISIBLE);
    }
}