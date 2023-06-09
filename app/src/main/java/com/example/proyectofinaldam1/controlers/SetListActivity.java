package com.example.proyectofinaldam1.controlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.proyectofinaldam1.R;
import com.example.proyectofinaldam1.adapters.RaSets;
import com.example.proyectofinaldam1.adapters.RaTournaments;
import com.example.proyectofinaldam1.models.DataBaseJSON;
import com.example.proyectofinaldam1.models.Set;
import com.example.proyectofinaldam1.models.Usuario;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SetListActivity extends AppCompatActivity implements DataBaseJSON.SetCallback{
    private RecyclerView rvSets;
    private RaSets raSets;
    private List<Set> sets;
    /**
     * Método protegido utilizado para crear y configurar la interfaz de usuario de la actividad.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_list);

        getWindow().setStatusBarColor(Color.parseColor("#000000"));

        rvSets = (RecyclerView) findViewById(R.id.reSets);

        String uidTrn = getIntent().getStringExtra("activity_anterior");
        sets = new ArrayList<>();
        // Ejecutar una tarea para obtener los conjuntos de datos de la base de datos de manera asíncrona
        DataBaseJSON.GetSetsTask getSetsTask = new DataBaseJSON.GetSetsTask(uidTrn,this);
        getSetsTask.execute();

        LinearLayoutManager layout = new LinearLayoutManager(this);
        raSets = new RaSets(sets);

        rvSets.setAdapter(raSets);
        rvSets.setLayoutManager(layout);

        Gson gson = new Gson();

        raSets.setOnItemClickListener(new RaSets.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Set set = sets.get(position);
                String strIntent = gson.toJson(set);
                Intent intentToUser = new Intent(SetListActivity.this,SetActivity.class);
                intentToUser.putExtra("activity_anterior",strIntent);
                startActivity(intentToUser);
            }
        });
        //Si hay cambios en los sets mientras el usuario esta mirando esta vista se actualizaran los datos
        DatabaseReference setsUpdate = DataBaseJSON.dbFirebase.getReference("Sets");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> childData = (HashMap<String, Object>) snapshot.getValue();
                Gson gson = new Gson();
                String json = gson.toJson(childData);
                Set[] tmpSet = gson.fromJson(json, Set[].class);
                List<Set> valueSet = Arrays.asList(tmpSet);
                for (int i = 0; i <sets.size(); i++) {
                    for (int j = 0; j < valueSet.size(); j++) {
                        if (sets.get(i).getUidTrns() == valueSet.get(j).getUidTrns()){
                            sets.add(i,valueSet.get(j));
                        }
                    }
                }
                raSets.setSets(sets);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    /**
     * Recogeremos los sets de la base de datos
     * @param sets
     */
    @Override
    public void onGetSets(List<Set> sets) {
        this.sets = sets;
        raSets.setSets(this.sets);
    }
}