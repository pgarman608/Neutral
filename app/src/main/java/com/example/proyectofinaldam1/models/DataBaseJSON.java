package com.example.proyectofinaldam1.models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBaseJSON {
    public static FirebaseDatabase dbFirebase = FirebaseDatabase.getInstance();
    public static FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    public static FirebaseUser userFirebase;
    private static DatabaseReference refDB = dbFirebase.getReference("usuarios");
    public static interface UsuarioCallback {
        void onUsuarioObtenido(Usuario usuario);
        void onUsersObtenido(List<Usuario> users);
        void onTrnsObtenido(List<Torneo> torneos);
    }
    public static interface SetCallback{
        void onGetSets(List<Set> sets);
    }
    private static int solucion;
    /**
     * Crea un nuevo usuario en Firebase Authentication y guarda la información en la base de datos Firebase Realtime Database.
     *
     * @param nick     El nombre de usuario (nick) del nuevo usuario.
     * @param password La contraseña del nuevo usuario.
     * @param email    El correo electrónico del nuevo usuario.
     * @return El resultado de la creación del usuario:
     *         - 1 si el usuario se creó exitosamente y se guardó en la base de datos.
     *         - 0 si el usuario se creó exitosamente pero no se pudo guardar en la base de datos.
     *         - (-1) si ocurrió un error durante el proceso de creación del usuario.
     */
    public static int createUser(String nick, String password, String email){
        solucion =0;
        // Crea el usuario en Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Obtiene el usuario actualmente autenticado
                            DataBaseJSON.userFirebase = FirebaseAuth.getInstance().getCurrentUser();
                            if (DataBaseJSON.userFirebase != null) {
                                // Actualiza el nombre de usuario en el perfil del usuario
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nick)
                                        .build();
                                DataBaseJSON.userFirebase.updateProfile(profileUpdates);
                            }
                            // Obtiene la instancia de la base de datos Firebase Realtime Database
                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            // Crea un objeto Usuario con los datos del nuevo usuario
                            Usuario user = new Usuario(userId,nick,100);
                            // Guarda el objeto Usuario en la base de datos
                            db.getReference("usuarios")
                                    .child(userId)
                                    .setValue(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            solucion =1;
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            solucion = -1;
                                        }
                                    });
                        } else {
                            solucion=-1;
                        }
                    }
                });
        return solucion;
    }
    /**
     * Obtiene un usuario específico de la base de datos Firebase Realtime Database utilizando su identificador único (UID).
     *
     * @param uid      El identificador único (UID) del usuario a obtener.
     * @param callback El objeto UsuarioCallback para recibir el usuario obtenido de la base de datos.
     */
    public static void getUsuario(String uid,UsuarioCallback callback){
        List<Usuario> listaUsuarios = new ArrayList<>();
        refDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Usuario usr = null;
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        HashMap<String, Object> childData = (HashMap<String, Object>) userSnapshot.getValue();
                        Gson gson = new Gson();
                        String json = gson.toJson(childData);
                        Usuario usuario = gson.fromJson(json, Usuario.class);
                        if (usuario.getUid().equals(uid)){
                            usr = usuario;
                        }
                    }
                    callback.onUsuarioObtenido(usr);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    /**
     * Crea un nuevo torneo en la base de datos Firebase Realtime Database.
     *
     * @param torneo
     * @return
     */
    public static int createTournament(Torneo torneo){
        solucion = 0;
        try{
            dbFirebase.getReference("Torneos")
                    .child(""+torneo.getUid())
                    .setValue(torneo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            solucion = 1;
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            solucion = -1;
                        }
                    });
        }catch (Exception ex){
            solucion = -2;
        }
        return solucion;
    }

    /**
     * Crea un nuevo conjunto (Set) en la base de datos Firebase Realtime Database.
     *
     * @param set
     * @return
     */
    public static int createSet(Set set){
        solucion = 0;
        try{
            dbFirebase.getReference("Sets")
                    .child(""+set.getUid())
                    .setValue(set)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            solucion = 1;
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            solucion = -1;
                        }
                    });
        }catch (Exception ex){
            solucion = -2;
        }
        return solucion;
    }
    /**
     * Establece un torneo en la base de datos Firebase Realtime Database.
     *
     * @param torneo El objeto Torneo a establecer en la base de datos.
     * @return Un entero que indica el resultado de la operación: 1 si se estableció correctamente, -1 si se canceló la operación, -2 si ocurrió una excepción.
     */
    public static int setTrn(Torneo torneo){
        solucion = 0;
        try{
            dbFirebase.getReference("Torneos")
                    .child(""+torneo.getUid())
                    .setValue(torneo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            solucion = 1;
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            solucion = -1;
                        }
                    });
        }catch (Exception ex){
            solucion = -2;
        }
        return solucion;
    }
    public static int setUsuario(Usuario user){
        solucion = 0;
        try{
            dbFirebase.getReference("usuarios")
                    .child(""+user.getUid())
                    .setValue(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            solucion = 1;
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            solucion = -1;
                        }
                    });
        }catch (Exception ex){
            solucion = -2;
        }
        return solucion;
    }
    public static void setSet(Set set){
        DatabaseReference refJP1Enter = dbFirebase.getReference("Sets")
                .child(""+set.getUid());
        refJP1Enter.setValue(set);
    }
    /**
     * Clase AsyncTask para obtener conjuntos de la base de datos Firebase Realtime Database en segundo plano.
     */
    public static class GetSetsTask extends AsyncTask<Integer, Void, List<Set>> {
        private SetCallback callback;
        private String uidTrn;
        private List<Torneo> torneos;
        /**
         * Crea una instancia de GetSetsTask. para recoger un set de un torneo
         *
         * @param uidTrn   Identificador del torneo al que pertenecen los conjuntos a obtener.
         * @param callback Referencia al objeto SetCallback para manejar el resultado obtenido.
         */
        public GetSetsTask(String uidTrn, SetCallback callback) {
            this.callback = callback;
            this.uidTrn = uidTrn;
            this.torneos = null;
        }

        /**
         * Crea una instancia de GetSetsTask. para recoger un set de varios torneos
         * @param torneos
         * @param callback
         */
        public GetSetsTask(List<Torneo> torneos,SetCallback callback) {
            this.callback = callback;
            this.torneos = torneos;
        }
        /**
         * Realiza la operación en segundo plano para obtener conjuntos de la base de datos Firebase Realtime Database.
         *
         * @param voids The parameters of the task.
         *
         * @return
         */
        @Override
        protected List<Set> doInBackground(Integer... voids) {
            solucion = 0;
            List<Set> sets = new ArrayList<>();
            dbFirebase.getReference("Sets").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    for (DataSnapshot tsk: task.getResult().getChildren()) {
                        HashMap<String, Object> childData = (HashMap<String, Object>) tsk.getValue();
                        Gson gson = new Gson();
                        String json = gson.toJson(childData);
                        Set set = gson.fromJson(json, Set.class);
                        if (torneos == null){
                            if (uidTrn.equals(""+set.getUidTrns())){
                                sets.add(set);
                            }
                        }else{
                            Log.e("tres", "onComplete: asdsd" + torneos.get(0).getUid() + " 11 " + set.getUid());
                            for (int i = 0; i < torneos.size(); i++) {
                                if (torneos.get(i).getUid() == set.getUidTrns()){
                                    sets.add(set);
                                }
                            }
                        }
                    }
                    callback.onGetSets(sets);
                }
            });
            return sets;
        }

        /**
         * Método invocado cuando se cancela la tarea en segundo plano.
         * @param sets The result, if any, computed in
         *
         */
        @Override
        protected void onCancelled(List<Set> sets) {
            super.onCancelled(sets);
        }
    }
    /**
     * Clase AsyncTask para obtener torneos de la base de datos Firebase Realtime Database en segundo plano.
     */
    public static class GetTrnsTask extends AsyncTask<Integer, Void, List<Torneo>> {
        private UsuarioCallback callback;
        private String usrUID;
        /**
         * Crea una instancia de GetTrnsTask.
         *
         * @param callback Referencia al objeto UsuarioCallback para manejar el resultado obtenido.
         */
        public GetTrnsTask( UsuarioCallback callback) {
            this.callback = callback;
            this.usrUID = null;
        }
        /**
         * Crea una instancia de GetTrnsTask para recoger los torneos de un usuario
         *
         * @param callback Referencia al objeto UsuarioCallback para manejar el resultado obtenido.
         */
        public GetTrnsTask(String uid, UsuarioCallback callback) {
            this.callback = callback;
            this.usrUID = uid;
        }

        /**
         * Realiza la operación en segundo plano para obtener torneos de la base de datos Firebase Realtime Database.
         *
         * @param voids Parámetros opcionales (no utilizados en este método).
         * @return Una lista de objetos Torneo obtenidos de la base de datos.
         */
        @Override
        protected List<Torneo> doInBackground(Integer... voids) {
            solucion = 0;
            List<Torneo> torneos = new ArrayList<>();
            dbFirebase.getReference("Torneos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    for (DataSnapshot tsk: task.getResult().getChildren()) {
                        HashMap<String, Object> childData = (HashMap<String, Object>) tsk.getValue();
                        Gson gson = new Gson();
                        String json = gson.toJson(childData);
                        Torneo trn = gson.fromJson(json, Torneo.class);
                        if (usrUID == null){
                            torneos.add(trn);
                        }else{
                            if (trn.getUsersList().contains(usrUID)){
                                torneos.add(trn);
                            }
                        }

                    }
                    callback.onTrnsObtenido(torneos);
                }
            });
            return torneos;
        }
        /**
         * Método invocado cuando se cancela la tarea en segundo plano.
         *
         * @param tournaments Lista de torneos obtenidos hasta el momento de la cancelación.
         */
        @Override
        protected void onCancelled(List<Torneo> tournaments) {
            super.onCancelled(tournaments);
        }
    }
    /**
     * Clase asincrónica utilizada para obtener usuarios de la base de datos Firebase Realtime Database.
     */
    public static class GetUsersTask extends AsyncTask<Integer, Void, List<Usuario>> {
        private UsuarioCallback callback;
        private int tier;
        private List<String> usrs;
        /**
         * Constructor de la clase GetUsersTask para obtener usuarios según el nivel de tier.
         *
         * @param tier     El nivel de tier para filtrar los usuarios.
         * @param callback El objeto UsuarioCallback para recibir los usuarios obtenidos.
         */
        public GetUsersTask(int tier, UsuarioCallback callback) {
            this.callback = callback;
            this.tier = tier;
        }
        /**
         * Constructor de la clase GetUsersTask para obtener usuarios específicos.
         *
         * @param users    La lista de identificadores únicos (UID) de los usuarios específicos a obtener.
         * @param callback El objeto UsuarioCallback para recibir los usuarios obtenidos.
         */
        public GetUsersTask(List<String> users, UsuarioCallback callback) {
            this.callback = callback;
            this.usrs = users;
            tier = -2;
        }

        /**
         * Realiza la operación en segundo plano para obtener usuarios de la base de datos Firebase Realtime Database.
         * @param voids The parameters of the task.
         *
         * @return
         */
        @Override
        protected List<Usuario> doInBackground(Integer... voids) {
            List<Usuario> users = new ArrayList<>();
            dbFirebase.getReference("usuarios")
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            for (DataSnapshot postSnapshot: task.getResult().getChildren()) {
                                HashMap<String, Object> childData = (HashMap<String, Object>) postSnapshot.getValue();
                                Gson gson = new Gson();
                                String json = gson.toJson(childData);
                                Usuario usuario = gson.fromJson(json, Usuario.class);
                                /**
                                 * Si el tier es -2 cargaremos los usuarios de una lista especifica
                                 * Si el tier es -1 cargaremos todos los usuarios
                                 * Si es superior cargaremos los usuarios de unos puntos en concreto
                                 */
                                if (tier == -2){
                                    if (usrs != null){
                                        for (int i = 0; i < usrs.size(); i++) {
                                            if (usuario.getUid().equals(usrs.get(i))){
                                                users.add(usuario);
                                            }
                                        }
                                    }
                                }else{
                                    if (tier == -1){
                                        users.add(usuario);
                                    }else{
                                        if (usuario.getPoints() >= tier-500 && usuario.getPoints() <= tier){
                                            users.add(usuario);
                                        }
                                    }
                                }
                            }
                            callback.onUsersObtenido(users);
                        }
                    });
            return users;
        }

        /**
         *
         * @param usuarios The result, if any, computed in
         *               {@link (Object[])}, can be null
         *
         */
        @Override
        protected void onCancelled(List<Usuario> usuarios) {
            super.onCancelled(usuarios);
        }
    }
}
