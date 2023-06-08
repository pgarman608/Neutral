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
import com.example.proyectofinaldam1.models.DataBaseJSON;
import com.example.proyectofinaldam1.models.Set;
import com.example.proyectofinaldam1.models.Usuario;
import com.google.gson.Gson;

import java.util.List;
public class RaSets extends RecyclerView.Adapter<RaSets.RecyclerHolder> {

    private List<Set> sets;
    /**
     * Constructor de la clase RaSets.
     *
     * @param sets La lista de sets para mostrar en el RecyclerView.
     */
    public RaSets(List<Set> sets) {
        this.sets = sets;
    }

    /**
     * Crea y devuelve una nueva instancia de RecyclerHolder que contiene la vista del elemento de lista.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     */
    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_set_list_row,parent, false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);
        return recyclerHolder;
    }

    /**
     * Vincula los datos del set a las vistas del elemento de lista.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder,int position) {
        Set set = sets.get(position);
        holder.tvRonda.setText("Ronda: " + set.getRound());
        // Datos del jugador 1
        if (set.getUid_j1() != null){
            holder.imgUserJP1.setImageResource(getImageJP(set.getUid_j1().getPoints()));
            holder.tvNickJP1.setText(set.getUid_j1().getNick());
            holder.tvPointsJP1.setText("" + set.getUid_j1().getPoints());
            holder.tvGamesJP1.setText("" + getPoints(1,set));
        }else{
            holder.imgUserJP1.setImageResource(R.drawable.chrrandom);
            holder.tvNickJP1.setText("?");
            holder.tvPointsJP1.setText("?");
            holder.btnSeeSet.setEnabled(false);
        }
        // Datos del jugador 2
        if (set.getUid_j2() != null){
            holder.imgUserJP2.setImageResource(getImageJP(set.getUid_j2().getPoints()));
            holder.tvNickJP2.setText(set.getUid_j2().getNick());
            holder.tvPointsJP2.setText("" + set.getUid_j2().getPoints());
            holder.tvGamesJP2.setText("" + getPoints(2,set));
        }else{
            holder.imgUserJP2.setImageResource(R.drawable.chrrandom);
            holder.tvNickJP2.setText("?");
            holder.tvPointsJP2.setText("?");
            holder.btnSeeSet.setEnabled(false);
        }
        // Configuración del botón ver set
        if (set.getEnd() == 0){
            if (DataBaseJSON.userFirebase == null){
                holder.btnSeeSet.setEnabled(false);
            }else{
                if (set.getUid_j1().getUid().equals(DataBaseJSON.userFirebase.getUid()) || set.getUid_j2().getUid().equals(DataBaseJSON.userFirebase.getUid())){
                    holder.btnSeeSet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null){
                                listener.onItemClick(position);
                            }
                        }
                    });
                }else{
                    holder.btnSeeSet.setEnabled(false);
                }
            }
        }else{
            holder.btnSeeSet.setEnabled(false);
        }
    }
    private int getImageJP(int points){
        int img = 0;
        if (points <= 350){
            img = R.drawable.icono_dificultad_normal_ssbb;
        }else{
            if (points <= 700){
                img = R.drawable.icono_dificultad_dificil_ssbb;
            }else{
                if (points <= 1200){
                    img = R.drawable.icono_dificultad_muy_dificil_ssbb;
                }else{
                    img = R.drawable.icono_dificultad_maximo_ssbb;
                }
            }
        }
        return img;
    }
    private int getPoints(int jp, Set setGame){
        int point = 0;
        if (setGame.getGames()!= null){
            for (int i = 0; i < setGame.getGames().size(); i++) {
                if (setGame.getGames().get(i) == 1 && jp == 1){
                    point++;
                }else{
                    if (setGame.getGames().get(i) == 2 && jp == 2){
                        point++;
                    }
                }
            }
        }
        return point;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener listener;

    /**
     * Establece el Listener para manejar eventos de clic en los elementos de la lista.
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Devuelve la cantidad de sets en la lista.
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return sets.size();
    }

    /**
     * Devuelve la cantidad de sets en la lista.
     *
     * @param sets
     */
    public void setSets(List<Set> sets) {
        this.sets = sets;
        notifyDataSetChanged();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        ImageView imgUserJP2;
        TextView tvNickJP1;
        TextView tvPointsJP1;
        ImageView imgUserJP1;
        TextView tvNickJP2;
        TextView tvPointsJP2;
        TextView tvGamesJP2;
        TextView tvGamesJP1;
        TextView tvRonda;
        Button btnSeeSet;
        /**
         * Constructor de la clase RecyclerHolder.
         *
         * @param itemView La vista del elemento de lista.
         */
        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            imgUserJP1 = (ImageView) itemView.findViewById(R.id.ivSetsJP1);
            tvNickJP1 = (TextView) itemView.findViewById(R.id.tvSetRowJP1Nick);
            tvPointsJP1 = (TextView) itemView.findViewById(R.id.tvSetRowJP1Points);
            tvGamesJP1 = (TextView) itemView.findViewById(R.id.tvSetRowJP1Games);
            imgUserJP2 = (ImageView) itemView.findViewById(R.id.ivSetsJP2);
            tvNickJP2 = (TextView) itemView.findViewById(R.id.tvSetRowJP2Nick);
            tvPointsJP2 = (TextView) itemView.findViewById(R.id.tvSetRowJP2Points);
            tvGamesJP2 = (TextView) itemView.findViewById(R.id.tvSetRowJP2Games);
            btnSeeSet = (Button) itemView.findViewById(R.id.btnSeeSet);
            tvRonda = (TextView) itemView.findViewById(R.id.tvSetsRound);
        }
    }
}
