package com.example.proyectofinaldam1.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinaldam1.R;
import com.example.proyectofinaldam1.models.Usuario;

import java.util.List;

public class RaRanking extends RecyclerView.Adapter<RaRanking.RecyclerHolder> {

    private List<Usuario> users;
    /**
     * Constructor de la clase RaRanking.
     *
     * @param users La lista de usuarios para mostrar en el ranking.
     */
    public RaRanking(List<Usuario> users) {
        this.users = users;
    }

    /**
     * Crea y devuelve una nueva instancia de RecyclerHolder que contiene la vista del elemento de lista.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_ranking_list_row,parent, false);
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

    public void setUsers(List<Usuario> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    /**
     * Vincula los datos del usuario a las vistas del elemento de lista.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        Usuario usuario = users.get(position);
        holder.tvNick.setText(usuario.getNick());
        holder.tvPoints.setText("Puntos: " + usuario.getPoints());
        int points = usuario.getPoints();
        if (points <= 350){
            holder.imgUser.setImageResource(R.drawable.icono_dificultad_normal_ssbb);
        }else{
            if (points <= 700){
                holder.imgUser.setImageResource(R.drawable.icono_dificultad_dificil_ssbb);
            }else{
                if (points <= 1200){
                    holder.imgUser.setImageResource(R.drawable.icono_dificultad_muy_dificil_ssbb);
                }else{
                    holder.imgUser.setImageResource(R.drawable.icono_dificultad_maximo_ssbb);
                }
            }
        }
        holder.btnSeeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
    }

    /**
     * Devuelve la cantidad de elementos en la lista de usuarios.
     *
     * @return La cantidad de elementos en la lista.
     */
    @Override
    public int getItemCount() {
        return users.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        ImageView imgUser;
        TextView tvNick;
        TextView tvPoints;
        Button btnSeeUser;
        /**
         * Constructor de la clase RecyclerHolder.
         *
         * @param itemView La vista del elemento de lista.
         */
        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);

            imgUser = (ImageView) itemView.findViewById(R.id.ivSeeRnk);
            tvNick = (TextView) itemView.findViewById(R.id.txtSeeNameRnk);
            tvPoints = (TextView) itemView.findViewById(R.id.txtSeePointsRnk);
            btnSeeUser = (Button) itemView.findViewById(R.id.btnSeeUser);
        }
    }
}
