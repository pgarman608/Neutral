package com.example.proyectofinaldam1.controlers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.proyectofinaldam1.R;
import com.example.proyectofinaldam1.models.AnimatedCharapter;
import com.example.proyectofinaldam1.models.DataBaseJSON;
import com.example.proyectofinaldam1.models.Set;
import com.example.proyectofinaldam1.models.Torneo;
import com.example.proyectofinaldam1.models.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

public class SetActivity extends AppCompatActivity {
    private ImageView imgJP1;
    private ImageView imgJP2;
    private ImageView imgBg;
    private ImageView imgChrJP1;
    private ImageView imgChrJP2;
    private Spinner spIcon;
    private Button btnSelectScene;
    private Button btnSelectChr;
    private Button btnWJP1;
    private Button btnWJP2;
    private ProgressBar progressSet;
    private ProgressBar pbJP1;
    private ProgressBar pbJP2;
    private TextView tvJP1;
    private TextView tvJP2;
    private TextView tvPointJP1;
    private TextView tvPointJP2;
    private TextView tvActualizar;
    private TextView tvRnd;
    private View vJP1;
    private View vJP2;
    private List<Integer> imgIcons;
    private int imgTempChrJP1;
    private ArrayAdapter<Integer> adapter;
    private Handler handlerJP1;
    private Handler handlerJP2;
    private AnimatedCharapter animeChrJP1;
    private AnimatedCharapter animeChrJP2;
    private static final int REQUEST_CODE = 1;
    private Set setGame;
    private ValueEventListener valueEventListenerInteger;
    private int pos;
    private int playing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        // Crear instancias de AnimatedCharapter para los personajes JP1 y JP2
        animeChrJP1 = new AnimatedCharapter(new int[]{R.drawable.hidle1,R.drawable.hidle2,R.drawable.hidle3,R.drawable.hidle4,R.drawable.hidle5},
                new int[]{R.drawable.hrun1,R.drawable.hrun2,R.drawable.hrun3,R.drawable.hrun4,R.drawable.hrun5},
                new int[]{R.drawable.hattack1,R.drawable.hattack2,R.drawable.hattack3,R.drawable.hattack4,R.drawable.hattack5},
                new int[]{R.drawable.hdeath1,R.drawable.hdeath2,R.drawable.hdeath3,R.drawable.hdeath4,R.drawable.hdeath5},
                R.drawable.hshield);
        animeChrJP2 = new AnimatedCharapter(new int[]{R.drawable.widle1,R.drawable.widle2,R.drawable.widle3,R.drawable.widle4,R.drawable.widle5},
                new int[]{R.drawable.wrun1,R.drawable.wrun2,R.drawable.wrun3,R.drawable.wrun4,R.drawable.wrun5},
                new int[]{R.drawable.wattack1,R.drawable.wattack2,R.drawable.wattack3,R.drawable.wattack4,R.drawable.wattack5},
                new int[]{R.drawable.wdeath1,R.drawable.wdeath2,R.drawable.wdeath3,R.drawable.wdeath4,R.drawable.wdeath5},
                R.drawable.wshield);
        // Asignar vistas a variables
        this.imgJP1 = (ImageView) findViewById(R.id.imgsetPlayer1);
        this.imgJP2 = (ImageView) findViewById(R.id.imgsetPlayer2);

        this.imgChrJP1 = (ImageView) findViewById(R.id.imgsetCharJP1);
        this.imgChrJP2 = (ImageView) findViewById(R.id.imgsetCharJP2);

        this.imgBg = (ImageView) findViewById(R.id.setStage);

        this.spIcon = (Spinner) findViewById(R.id.spIcon);

        this.btnSelectScene = (Button)  findViewById(R.id.btnSelectScene);
        this.btnSelectChr = (Button)  findViewById(R.id.btnSelectChr);
        this.btnWJP1 = (Button) findViewById(R.id.btnPlayer1);
        this.btnWJP2 = (Button) findViewById(R.id.btnPlayer2);

        this.tvJP1 = (TextView) findViewById(R.id.tvSetJP1);
        this.tvJP2 = (TextView) findViewById(R.id.tvSetJP2);
        this.tvPointJP1 = (TextView) findViewById(R.id.tvPointsJP1);
        this.tvPointJP2 = (TextView) findViewById(R.id.tvPointJP2);
        this.tvRnd = (TextView) findViewById(R.id.tvRnd);

        this.tvActualizar = (TextView)  findViewById(R.id.tvActualizar);
        this.vJP1 = (View) findViewById(R.id.vJP1);
        this.vJP2 = (View) findViewById(R.id.vJP2);

        tvActualizar.setText("Espera de conexión");

        progressSet = (ProgressBar) findViewById(R.id.progressSet);

        pbJP1 = (ProgressBar) findViewById(R.id.pbPlayer1);
        pbJP2 = (ProgressBar) findViewById(R.id.pbPlayer2);

        imgIcons = new ArrayList<>();
        // Crear la lista de iconos en el spinner
        createList();
        imgTempChrJP1 = 0;
        pos = calcPos();
        playing = 0;
        // Obtener datos de la actividad anterior
        Gson gson = new Gson();
        String strIntent = getIntent().getStringExtra("activity_anterior");
        setGame = gson.fromJson(strIntent,Set.class);
        // Establecer los textos de los TextView para JP1 y JP2
        tvJP1.setText(setGame.getUid_j1().getNick());
        tvJP2.setText(setGame.getUid_j2().getNick());
        // Determinar si el usuario actual es JP1 o JP2 y establecer los indicadores
        if (DataBaseJSON.userFirebase.getUid().equals(setGame.getUid_j1().getUid())){
            setGame.setJp1join(1);
        }else{
            setGame.setJp2join(1);
        }
        DataBaseJSON.setSet(setGame);
        if (setGame.getGames() != null){
            setPoints();
        }
        DatabaseReference newR = DataBaseJSON.dbFirebase.getReference("Sets").child(""+setGame.getUid());
        tvRnd.setText("Ronda :" + setGame.getRound());
        if (setGame.getStages() == null){
            setGame.setStages(new ArrayList<>());
            setGame.setStages(0,0);
            DataBaseJSON.setSet(setGame);
        }else{
            imgBg.setImageResource(setGame.getStages(pos));
        }
        DataBaseJSON.setSet(setGame);
        if (setGame.getChar_j1() == null){
            setGame.setListChar_j1(new ArrayList<>());
            setGame.setChar_j1(0,0);
            setGame.setListChar_j2(new ArrayList<>());
            setGame.setChar_j2(0,0);
            DataBaseJSON.setSet(setGame);
        }
        // Configurar el listener de Firebase para escuchar cambios en la base de datos
        valueEventListenerInteger = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (setGame.getEnd() == 0){
                    if (snapshot.exists()) {
                        HashMap<String, Object> childData = (HashMap<String, Object>) snapshot.getValue();
                        Gson gson = new Gson();
                        String json = gson.toJson(childData);
                        Set set = gson.fromJson(json, Set.class);
                        //Comprobaremos que los dos usuarios este dentro de la partida
                        if (set.getJp1join() != 0 && set.getJp2join() != 0){
                            //Hacemos que la informacion de esperando jugador se oculte
                            progressSet.setVisibility(View.INVISIBLE);
                            tvActualizar.setVisibility(View.INVISIBLE);
                            spIcon.setVisibility(View.VISIBLE);
                            findViewById(R.id.ttlChr).setVisibility(View.VISIBLE);
                            //Si las imagenes de los usuarios de la base de datos no estan vacios guardaremos la info
                            try {
                                if (set.getListChar_j1()!= null){
                                    if (set.getChar_j1(pos) != 0)
                                        imgChrJP1.setImageResource(set.getChar_j1(pos));
                                    if (set.getChar_j2(pos) != 0)
                                        imgChrJP2.setImageResource(set.getChar_j2(pos));
                                }
                            }catch (Exception ex){}
                            if (set.getChar_j2().size() == pos){
                                pos--;
                            }
                            //Comprobar que los dos usuarios han seleccionado el personaje
                            if (set.getChar_j2(pos)!= 0 && set.getChar_j1(pos)!= 0){
                                progressSet.setVisibility(View.INVISIBLE);
                                spIcon.setVisibility(View.INVISIBLE);
                                findViewById(R.id.ttlChr).setVisibility(View.INVISIBLE);
                                //Elegir quien va a ser el que eliga el escenario
                                if (DataBaseJSON.userFirebase.getUid().equals(setGame.getUid_j1().getUid()) && set.getStartSS() == 0){
                                    findViewById(R.id.ttlScene).setVisibility(View.VISIBLE);
                                    btnSelectScene.setVisibility(View.VISIBLE);
                                }else{
                                    if (DataBaseJSON.userFirebase.getUid().equals(setGame.getUid_j2().getUid()) && set.getStartSS() == 1){
                                        findViewById(R.id.ttlScene).setVisibility(View.VISIBLE);
                                        btnSelectScene.setVisibility(View.VISIBLE);
                                    }else{
                                        //Al usuario que no eliga le pondremos que espere la seleccion
                                        tvActualizar.setText("Esperando la eleccion");
                                        tvActualizar.setVisibility(View.VISIBLE);
                                        progressSet.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                            //Comprobaremos que el escenario a sido elegido
                            if (set.getStages() != null && set.getStages(pos) != 0){
                                if (set.getGames() != null){
                                    //Si el jugador 1 uno gana actualizaremos la informacion del set
                                    if (set.getGames(pos) == 2){
                                        set.setStartSS(0);
                                        setGame = set;
                                        btnWJP1.setEnabled(false);
                                        btnWJP2.setEnabled(false);
                                        setPoints();
                                        pos++;
                                        if (animeChrJP2.getSeccion() != 0){
                                            playing = 3;
                                        }
                                        spIcon.setVisibility(View.VISIBLE);
                                        findViewById(R.id.ttlChr).setVisibility(View.VISIBLE);
                                        spIcon.setEnabled(true);
                                        pelearse(set);
                                    }else{
                                        //Si el jugador dos uno gana actualizaremos la informacion del set
                                        if (set.getGames(pos) == 1){
                                            set.setStartSS(1);
                                            setGame = set;
                                            btnWJP1.setEnabled(false);
                                            btnWJP2.setEnabled(false);
                                            setPoints();
                                            pos++;
                                            if (animeChrJP2.getSeccion() != 0){
                                                playing = 2;
                                            }
                                            spIcon.setEnabled(true);
                                            spIcon.setVisibility(View.VISIBLE);
                                            findViewById(R.id.ttlChr).setVisibility(View.VISIBLE);
                                        }else{
                                            //Empezaran a pelearse las imagenes
                                            pelearse(set);
                                        }
                                    }
                                }else{
                                    //Empezaran a pelearse las imagenes
                                    pelearse(set);
                                }
                            }
                        }
                        setGame = set;
                    }
                }else{
                    //Sale la informacion de ganar
                    wins();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        newR.addValueEventListener(valueEventListenerInteger);
        //Cuando pulsemos esto nos mandára a la pagina de los escenarios
        btnSelectScene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStage = new Intent(SetActivity.this, SelectStageActivity.class);
                intentStage.putExtra("pos",pos);
                intentStage.putExtra("set",gson.toJson(setGame,Set.class));
                startActivityForResult(intentStage,REQUEST_CODE);
                findViewById(R.id.ttlScene).setVisibility(View.INVISIBLE);

            }
        });
        //Nos guardara el personaje elegido en la base de datos
        btnSelectChr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataBaseJSON.userFirebase.getUid().equals(setGame.getUid_j1().getUid())){
                    setGame.setChar_j1(pos,imgTempChrJP1);
                }else{
                    setGame.setChar_j2(pos,imgTempChrJP1);
                }
                DataBaseJSON.setSet(setGame);
                btnSelectChr.setEnabled(false);
                spIcon.setEnabled(false);
                findViewById(R.id.ttlChr).setVisibility(View.INVISIBLE);
                progressSet.setVisibility(View.VISIBLE);
            }
        });
        //Diremos que el jugador 1 a ganado el game
        btnWJP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setGame.getGames() == null){
                    setGame.setGames(new ArrayList<>());
                    setGame.setGames(pos,2);
                }else{
                    setGame.setGames(pos,2);
                }
                pos++;
                setGame.setGames(pos,0);
                setGame.setStartSS(1);
                DataBaseJSON.setSet(setGame);
                btnWJP1.setEnabled(false);
                btnWJP2.setEnabled(false);
                setPoints();
                playing = 3;
                spIcon.setEnabled(true);
                spIcon.setVisibility(View.VISIBLE);
                findViewById(R.id.ttlChr).setVisibility(View.VISIBLE);
            }
        });
        //Diremos que el jugador 2 a ganado el game
        btnWJP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setGame.getGames() == null){
                    setGame.setGames(new ArrayList<>());
                    setGame.setGames(pos,1);
                }else{
                    setGame.setGames(pos,1);
                }
                pos++;
                setGame.setGames(pos,0);
                setGame.setStartSS(0);
                DataBaseJSON.setSet(setGame);
                btnWJP1.setEnabled(false);
                btnWJP2.setEnabled(false);
                setPoints();
                playing = 2;
                spIcon.setVisibility(View.VISIBLE);
                findViewById(R.id.ttlChr).setVisibility(View.VISIBLE);
                spIcon.setEnabled(true);
            }
        });
        //Cargar los iconos en el spinner
        adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, imgIcons) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(getItem(position));
                imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
                return imageView;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return getView(position, convertView, parent);
            }
        };
        spIcon.setAdapter(adapter);
        //Cuando pulsemos un elemento del spinner nos guardemos en local el id de la imagen
        spIcon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int img = Integer.parseInt(parent.getItemAtPosition(position).toString());
                if (img != R.drawable.chrrandom){
                    imgTempChrJP1 = img;
                    btnSelectChr.setEnabled(true);
                }else{
                    btnSelectChr.setEnabled(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        animation();
    }

    /**
     * Empezará la animacion de la pelea
     * @param set
     */
    private void pelearse(Set set){
        findViewById(R.id.ttlScene).setVisibility(View.INVISIBLE);
        imgBg.setImageResource(set.getStages(pos));
        progressSet.setVisibility(View.VISIBLE);
        playing = 1;
        tvActualizar.setText("Peleando...");
        btnSelectScene.setVisibility(View.INVISIBLE);
        animeChrJP1.moveLeftToCenter(SetActivity.this,imgJP1,imgChrJP1,tvJP1);
        animeChrJP2.moveRightToCenter(SetActivity.this,imgJP2,imgChrJP2,tvJP2);
        btnWJP1.setEnabled(true);
        btnWJP2.setEnabled(true);
    }

    /**
     * Crearemos la lista de las imagenes del spinner
     */
    private void createList() {
        Resources res = getResources();
        Field[] drawableFields = R.drawable.class.getFields();
        imgIcons.add(R.drawable.chrrandom);
        for (Field field : drawableFields) {
            try {
                if (field.getName().startsWith("chr") && !(field.getName().startsWith("chrrandom"))) {
                    int resId = field.getInt(null);
                    imgIcons.add(resId);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    private int timeGameJP1;
    private int timedeadJP1;
    private int timedeadJP2;
    private int timeGameJP2;
    /**
     * Realiza la animación del personaje del jugador 1 (JP1) en el juego.
     * La animación varía según el estado de juego y el tiempo transcurrido.
     */
    private void animation(){
        handlerJP1 = new Handler();
        handlerJP2 = new Handler();
        timeGameJP1 = 0;
        timeGameJP2 = 0;
        timedeadJP1 = 0;
        timedeadJP2 = 0;
        //Los handler nos permite ejecutar de forma secuencial codigo en un tiempo de forma sencilla
        Runnable runjp1 = new Runnable() {
            @Override
            public void run() {
                if (playing == 0 || animeChrJP1.getPostStatus() == 1 || playing == 3){
                    // Animación idle de personaje del JP1
                    animeChrJP1.changeImageSet(new AnimatedCharapter.SetListImg() {
                        @Override
                        public void setListChr(int[] listImg, int index) {
                            if(listImg.length != 5){
                                imgJP1.setImageResource(listImg[0]);
                            }else{
                                imgJP1.setImageResource(listImg[index]);
                            }
                        }
                    });
                    handlerJP1.postDelayed(this, 150);
                }else{
                    // Animación en el juego del JP1
                    if (playing == 1){
                        timeGameJP1 += 10;
                        animeChrJP1.changeImageSetJP1(timeGameJP1,new AnimatedCharapter.SetListImg(){
                            @Override
                            public void setListChr(int[] listImg, int index) {
                                if(listImg.length != 5){
                                    imgJP1.setImageResource(listImg[0]);
                                }else{
                                    imgJP1.setImageResource(listImg[index]);
                                }
                            }
                        });
                        if (timeGameJP1 >= 200){
                            timeGameJP1 = 0;
                        }
                        if (setGame.getEnd() != 1){
                            handlerJP1.postDelayed(this, 150);
                        }
                    }else{
                        if(playing==2){
                            // Animación en el juego del JP1
                            timedeadJP1 += 10;
                            if (timedeadJP1 <= 170){
                                animeChrJP1.onDeath(timedeadJP1,new AnimatedCharapter.SetListImg(){
                                    @Override
                                    public void setListChr(int[] listImg, int index) {
                                        imgJP1.setImageResource(listImg[index]);
                                    }
                                });
                            }
                            if (timedeadJP1 >= 50){
                                animeChrJP2.setPostStatus(2);
                            }
                            if (timedeadJP1 >= 200){
                                timedeadJP1 = 0;
                                playing = 0;
                                animeChrJP2.setPostStatus(0);
                                animeChrJP1.moveCenterToLeft(SetActivity.this,imgJP1,imgChrJP1,tvJP1);
                                animeChrJP2.moveCenterToRight(SetActivity.this,imgJP2,imgChrJP2,tvJP2);
                            }
                            if (setGame.getEnd() != 1){
                                handlerJP1.postDelayed(this, 150);
                            }
                        }
                    }
                }
            }
        };
        handlerJP1.postDelayed(runjp1, 150);
        Runnable runjp2 = new Runnable() {
            @Override
            public void run() {
                if (playing == 0 || animeChrJP1.getPostStatus() == 1 || playing == 2){
                    // Animación de idle del JP1
                    animeChrJP2.changeImageSet(new AnimatedCharapter.SetListImg() {
                        @Override
                        public void setListChr(int[] listImg, int index) {
                            if(listImg.length != 5){
                                imgJP2.setImageResource(listImg[0]);
                            }else{
                                imgJP2.setImageResource(listImg[index]);
                            }
                        }
                    });
                    handlerJP2.postDelayed(this, 150);
                }else{
                    if (playing == 1){
                        // Animación de juego del JP2 (Solo tiene 4s)
                        timeGameJP2 += 10;
                        animeChrJP2.changeImageSetJP2(timeGameJP2,new AnimatedCharapter.SetListImg(){
                            @Override
                            public void setListChr(int[] listImg, int index) {
                                if(listImg.length != 5){
                                    imgJP2.setImageResource(listImg[0]);
                                }else{
                                    imgJP2.setImageResource(listImg[index]);
                                }
                            }
                        });
                        if (timeGameJP2 >= 200){
                            timeGameJP2 = 0;
                        }
                        if (setGame.getEnd() != 1){
                            handlerJP2.postDelayed(this, 150);
                        }
                    }else{
                        if (playing == 3){
                            // Animación de muerte del JP2 (Solo tiene 3s)
                            timedeadJP2 += 10;
                            if (timedeadJP2 < 140){
                                animeChrJP2.onDeath(timedeadJP2,new AnimatedCharapter.SetListImg(){
                                    @Override
                                    public void setListChr(int[] listImg, int index) {
                                        imgJP2.setImageResource(listImg[index]);
                                    }
                                });
                            }
                            if (timedeadJP2 >= 50){
                                animeChrJP1.setPostStatus(2);
                            }
                            if (timedeadJP2 >= 200){
                                timedeadJP2 = 0;
                                playing = 0;
                                animeChrJP1.setPostStatus(0);
                                animeChrJP1.moveCenterToLeft(SetActivity.this,imgJP1,imgChrJP1,tvJP1);
                                animeChrJP2.moveCenterToRight(SetActivity.this,imgJP2,imgChrJP2,tvJP2);
                            }
                            if (setGame.getEnd() != 1){
                                handlerJP2.postDelayed(this, 150);
                            }
                        }
                    }
                }
            }
        };
        handlerJP2.postDelayed(runjp2, 150);
    }

    /**
     * Cuando vengamos de elegir el escenario actualizaremos el set local y el de la base de datos
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
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String strset = data.getStringExtra("set");
            Gson gson = new Gson();
            Set set = gson.fromJson(strset,Set.class);
            imgBg.setImageResource(set.getStages(pos));
            DataBaseJSON.setSet(set);
            findViewById(R.id.ttlScene).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Actualilizaremos los puntos visibles con los de la base de datos
     */
    private void setPoints(){
        int point1 = 0;
        int point2 = 0;
        for (int i = 0; i < setGame.getGames().size(); i++) {
            if (setGame.getGames().get(i) == 1){
                point2++;
                setPrb(1,33*point2,100);
            }else{
                if (setGame.getGames().get(i) == 2){
                    point1++;
                    setPrb(2,33*point1,100);
                }
            }
        }
        //Si el jugador 1 o 2 ha llegado a 3 wins terminará el set
        if (point1 == 3){
            setGame.setEnd(1);
            animeChrJP1.setPostStatus(4);
        }else{
            if (point2 == 3){
                setGame.setEnd(2);
                animeChrJP1.setPostStatus(4);
            }
        }
        if (setGame.getEnd() != 0){
            wins();
        }
        DataBaseJSON.setSet(setGame);
        tvPointJP1.setText(""+point1);
        tvPointJP2.setText(""+point2);
    }
    /**
     * Muestra el resultado de la partida y realiza las acciones correspondientes al finalizar el juego.
     */
    private void wins(){
        String aux = "";
        // Verificar el estado de la partida
        if (setGame.getEnd() == 1){
            tvActualizar.setVisibility(View.VISIBLE);
            tvActualizar.setBackgroundColor(Color.rgb(0, 102, 255));
            aux = setGame.getUid_j1().getNick();
            //Actualizamos el punto del ganador JP1
            DataBaseJSON.getUsuario(setGame.getUid_j1().getUid(), new DataBaseJSON.UsuarioCallback() {
                @Override
                public void onUsuarioObtenido(Usuario usuario) {
                    usuario.setPoints(usuario.getPoints() + 10);
                    DataBaseJSON.setUsuario(usuario);
                }
                @Override
                public void onUsersObtenido(List<Usuario> users) {}
                @Override
                public void onTrnsObtenido(List<Torneo> torneos) {}
            });
            //Actualizamos el punto del perdedor JP2
            DataBaseJSON.getUsuario(setGame.getUid_j2().getUid(), new DataBaseJSON.UsuarioCallback() {
                @Override
                public void onUsuarioObtenido(Usuario usuario) {
                    usuario.setPoints(usuario.getPoints() - 10);
                    DataBaseJSON.setUsuario(usuario);
                }
                @Override
                public void onUsersObtenido(List<Usuario> users) {}
                @Override
                public void onTrnsObtenido(List<Torneo> torneos) {}
            });
        }else{
            tvActualizar.setVisibility(View.VISIBLE);
            tvActualizar.setBackgroundColor(Color.rgb(255, 0, 0));
            aux = setGame.getUid_j2().getNick();
            //Actualizamos el punto del ganador JP2
            DataBaseJSON.getUsuario(setGame.getUid_j1().getUid(), new DataBaseJSON.UsuarioCallback() {
                @Override
                public void onUsuarioObtenido(Usuario usuario) {
                    usuario.setPoints(usuario.getPoints() - 10);
                    DataBaseJSON.setUsuario(usuario);
                }
                @Override
                public void onUsersObtenido(List<Usuario> users) {}
                @Override
                public void onTrnsObtenido(List<Torneo> torneos) {}
            });
            //Actualizamos el punto del perdedor JP1
            DataBaseJSON.getUsuario(setGame.getUid_j2().getUid(), new DataBaseJSON.UsuarioCallback() {
                @Override
                public void onUsuarioObtenido(Usuario usuario) {
                    usuario.setPoints(usuario.getPoints() + 10);
                    DataBaseJSON.setUsuario(usuario);
                }
                @Override
                public void onUsersObtenido(List<Usuario> users) {}
                @Override
                public void onTrnsObtenido(List<Torneo> torneos) {}
            });
        }
        // Mostrar el mensaje de resultado de la partida
        tvActualizar.setText("Partida Terminada: Ganador " + aux);
        tvActualizar.setTextSize(18);
        tvActualizar.setTextColor(Color.rgb(212, 175, 55));
        // Ocultar elementos visuales
        progressSet.setVisibility(View.INVISIBLE);
        btnWJP1.setVisibility(View.INVISIBLE);
        btnWJP2.setVisibility(View.INVISIBLE);
        btnSelectScene.setVisibility(View.INVISIBLE);
        btnSelectChr.setVisibility(View.INVISIBLE);
        findViewById(R.id.ttlScene).setVisibility(View.INVISIBLE);
        findViewById(R.id.ttlChr).setVisibility(View.INVISIBLE);
        spIcon.setVisibility(View.INVISIBLE);
    }

    /**
     * Calcularemos el set donde estan los jugadores
     * @return
     */
    private int calcPos(){
        int aux = 0;
        if (setGame != null){
            for (int i = 0; i < setGame.getGames().size() ; i++) {
                if (setGame.getGames().get(i) == 0){
                    setGame.getGames().remove(i);
                    i--;
                }
            }
            aux = setGame.getGames().size()-1;
        }
        return aux;
    }
    private Handler handlerProgress;
    /**
     * En este metodo disminuiremos el progreso de la progress bar de la vista
     * @param pb
     * @param delpnt
     * @param start
     */
    private void setPrb(int pb,int delpnt, int start){
        int aux = 0;
        handlerProgress = new Handler();
        Runnable runPogress = new Runnable() {
            @Override
            public void run() {
                if (pb == 1){
                    pbJP1.setProgress(pbJP1.getProgress()-1);
                    if (pbJP1.getProgress() > start - delpnt){
                        handlerProgress.postDelayed(this,100);
                    }
                }else{
                    pbJP2.setProgress(pbJP2.getProgress()-1);
                    if (pbJP2.getProgress() > start - delpnt){
                        handlerProgress.postDelayed(this,100);
                    }
                }
            }
        };
        handlerProgress.postDelayed(runPogress,150);
    }
}