package com.example.proyectofinaldam1.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinaldam1.R;
import com.example.proyectofinaldam1.models.Torneo;
import com.example.proyectofinaldam1.models.Usuario;

import java.util.List;

public class RaTournaments extends RecyclerView.Adapter<RaTournaments.RecyclerHolder> {
    private List<Torneo> tournaments;
    /**
     * Constructor de la clase RaTournaments.
     *
     * @param tournaments La lista de torneos a mostrar.
     */
    public RaTournaments(List<Torneo> tournaments) {
        this.tournaments = tournaments;
    }

    /**
     * Crea y devuelve una nueva instancia de RecyclerHolder que contiene la vista del elemento de lista.
     *
     * @param parent   El ViewGroup al que se añadirá la vista del elemento de lista.
     * @param viewType El tipo de vista del elemento de lista.
     * @return Una instancia de RecyclerHolder que contiene la vista del elemento de lista.
     */
    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_tournament_list_row,parent, false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);
        return recyclerHolder;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener listener;
    /**
     * Establece el listener para gestionar los eventos de clic en los elementos de la lista.
     *
     * @param listener El listener que se va a establecer.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public int getItemCount() {
        return tournaments.size();
    }
    /**
     * Actualiza la lista de torneos y notifica al adaptador para refrescar la vista.
     *
     * @param tournaments La nueva lista de torneos.
     */
    public void setTournaments(List<Torneo> tournaments) {
        this.tournaments = tournaments;
        notifyDataSetChanged();
    }
    /**
     * Vincula los datos del torneo a las vistas del elemento de lista.
     *
     * @param holder   El RecyclerHolder que contiene la vista del elemento de lista.
     * @param position La posición del elemento en la lista.
     */
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        Torneo torneo = tournaments.get(position);
        int points = torneo.getAllPoints();
        Log.e("error torneo 5", "onComplete: "+ tournaments.size());
        if (points <= 1500){
            holder.imgTrnsRow.setImageResource(R.drawable.icono_dificultad_normal_ssbb);
        }else{
            if (points <= 5000){
                holder.imgTrnsRow.setImageResource(R.drawable.icono_dificultad_dificil_ssbb);
            }else{
                if (points <= 12000){
                    holder.imgTrnsRow.setImageResource(R.drawable.icono_dificultad_muy_dificil_ssbb);
                }else{
                    holder.imgTrnsRow.setImageResource(R.drawable.icono_dificultad_maximo_ssbb);
                }
            }
        }
        holder.tvNameTrnsRow.setText(torneo.getName());
        holder.tvPointsTrnsRow.setText("Puntos: " + points);
        // Establecer el listener para el botón de entrada al torneo
        holder.btnEnterTrns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
    }
    public class RecyclerHolder extends RecyclerView.ViewHolder {
        ImageView imgTrnsRow;
        TextView tvNameTrnsRow;
        TextView tvPointsTrnsRow;
        Button btnEnterTrns;
        /**
         * Constructor de la clase RecyclerHolder.
         *
         * @param itemView La vista del elemento de lista.
         */
        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            imgTrnsRow = (ImageView) itemView.findViewById(R.id.ivTrnsRow);
            tvNameTrnsRow = (TextView) itemView.findViewById(R.id.tvNameTrnsRow);
            tvPointsTrnsRow = (TextView) itemView.findViewById(R.id.tvPointsTrnsRow);
            btnEnterTrns = (Button) itemView.findViewById(R.id.btnEnterTrns);
        }
    }
}
