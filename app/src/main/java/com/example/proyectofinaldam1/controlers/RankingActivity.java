package com.example.proyectofinaldam1.controlers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.proyectofinaldam1.R;
import com.example.proyectofinaldam1.adapters.RaRanking;

public class RankingActivity extends AppCompatActivity implements View.OnClickListener{
    private View vTierC;
    private View vTierA;
    private View vTierB;
    private View vTierS;

    /**
     * Este método es parte de la clase RankingActivity que hereda de la clase AppCompatActivity y se ejecuta cuando se crea la actividad.
     * @param savedInstanceState Objeto Bundle que contiene los datos previamente guardados del estado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        getWindow().setStatusBarColor(Color.parseColor("#000000"));


        vTierC = (View) findViewById(R.id.vTierC);
        vTierB = (View) findViewById(R.id.vTierB);
        vTierA = (View) findViewById(R.id.vTierA);
        vTierS = (View) findViewById(R.id.vTierS);

        vTierC.setOnClickListener(this);
        vTierB.setOnClickListener(this);
        vTierA.setOnClickListener(this);
        vTierS.setOnClickListener(this);
    }

    /**
     * Este método se llama cuando se hace clic en un elemento de la vista.
     * Se utiliza para pasar información de un Activity a otro usando un Intent.
     * @param v la vista que ha sido clickeada
     */
    @Override
    public void onClick(View v) {
        Intent intentRnk = new Intent(RankingActivity.this,RankingListActivity.class);
        switch (v.getId()){
            case R.id.vTierC:
                intentRnk.putExtra("Tier",300);
                break;
            case R.id.vTierB:
                intentRnk.putExtra("Tier",800);
                break;
            case R.id.vTierA:
                intentRnk.putExtra("Tier",1300);
                break;
            case R.id.vTierS:
                intentRnk.putExtra("Tier",1800);
                break;
        }
        startActivity(intentRnk);
    }
}