package com.example.proyectofinaldam1.models;

import android.content.Context;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectofinaldam1.R;

import java.util.List;

public class AnimatedCharapter {
    private int[] imgidle;
    private int[] imgrun;
    private int[] imgattack;
    private int[] imgdead;
    private int imgshield;
    private int status;
    private int postStatus;
    private int indice;
    public static interface SetListImg{
        void setListChr(int[] listImg,int index);
    }
    public AnimatedCharapter(int[] imgidle, int[] imgrun, int[] imgattack, int[] imgdead, int imgshield) {
        this.imgidle = imgidle;
        this.imgrun = imgrun;
        this.imgattack = imgattack;
        this.imgdead = imgdead;
        this.imgshield = imgshield;
        this.indice = 0;
        this.postStatus = 0;
        this.status = 0;
        this.seccion = 0;
    }
    public int getStatus() {
        return status;
    }
    public int getSeccion(){
        return  this.seccion;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(int postStatus) {
        this.postStatus = postStatus;

    }
    public void incrPostStatus(){
        postStatus = status;
        postStatus++;
    }

    /**
     * Método utilizado para cambiar la imagen de un conjunto de un objeto Set.
     * @param callback
     */
    public void changeImageSet(SetListImg callback){
        // Verificar si se ha cambiado el estado del conjunto
        if (postStatus != status){
            indice = 0;
            status = postStatus;
        }
        // Reiniciar el estado si el valor supera el límite
        if (status >= 5){
            status = 0;
            postStatus = 0;
        }
        int[] imgsAux = null;
        // Asignar las imágenes correspondientes al estado del conjunto
        switch (postStatus){
            case 0:
                imgsAux = imgidle;
                break;
            case 1:
                imgsAux = imgrun;
                break;
            case 2:
                imgsAux = imgattack;
                break;
            case 3:
                imgsAux = imgdead;
                break;
            case 4:
                imgsAux = new int[]{imgshield};
                break;
        }
        // Actualizar el índice de la imagen actual
        indice = (indice + 1) % imgidle.length;
        // Actualizar la lista de imágenes del conjunto mediante el objeto callback
        callback.setListChr(imgsAux,indice);
    }/**
     * Método utilizado para cambiar la imagen de un conjunto de un objeto Set en función al tiempo del jp1.
     *
     * @param play El tiempo en animacion.
     * @param callback Objeto que implementa la interfaz SetListImg y se utiliza para actualizar la lista de imágenes del conjunto.
     */
    public void changeImageSetJP1(int play,SetListImg callback){
        int[] imgsAux = null;
        if (play <= 100){
            imgsAux = imgattack;
        }else{
            if (play<=200){
                imgsAux = new int[]{imgshield};
            }
        }
        indice = (indice + 1) % imgidle.length;
        callback.setListChr(imgsAux,indice);
    }

    /**
     * Método utilizado para cambiar la imagen de un conjunto de un objeto Set en función al tiempo del JP2
     *
     * @param play El tiempo en animacion.
     * @param callback Objeto que implementa la interfaz SetListImg y se utiliza para actualizar la lista de imágenes del conjunto.
     */
    public void changeImageSetJP2(int play,SetListImg callback){
        int[] imgsAux = null;
        if (play <= 100){
            imgsAux = new int[]{imgshield};
        }else{
            if (play<=200){
                imgsAux = imgattack;
            }
        }
        indice = (indice + 1) % imgidle.length;
        callback.setListChr(imgsAux,indice);
    }

    /**
     * Método utilizado para cambiar la imagen de un conjunto de un objeto Set cuando el jugador muere.
     * @param play
     * @param callback
     */
    public void onDeath(int play,SetListImg callback) {
        int[] imgsAux = null;
        if (play <= 100) {
            imgsAux = imgidle;
        } else {
            imgsAux = imgdead;
        }
        indice = (indice + 1) % imgidle.length;
        callback.setListChr(imgsAux, indice);
    }

    /**
     * Método utilizado para mover hacia el centro la imagen el jugado 1
     * @param context
     * @param imgJP1
     * @param imgChrJP1
     * @param tvJP1
     */
    public void moveLeftToCenter(Context context, ImageView imgJP1, ImageView imgChrJP1, TextView tvJP1){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.moveinjp1);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                postStatus = 0;
                seccion++;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imgJP1.startAnimation(animation);
        imgChrJP1.startAnimation(animation);
        tvJP1.startAnimation(animation);
        postStatus = 1;
    }
    private int seccion;
    /**
     * Método utilizado para mover hacia el centro la imagen el jugado 2
     * @param context
     * @param imgJP2
     * @param imgChrJP2
     * @param tvJP2
     */
    public void moveRightToCenter(Context context, ImageView imgJP2, ImageView imgChrJP2, TextView tvJP2){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.moveinjp2);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                postStatus = 0;
                seccion++;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imgJP2.startAnimation(animation);
        imgChrJP2.startAnimation(animation);
        tvJP2.startAnimation(animation);
        postStatus = 1;
    }

    /**
     * Método utilizado para mover hacia afuera la imagen el jugado 1
     * @param context
     * @param imgJP1
     * @param imgChrJP1
     * @param tvJP1
     */
    public void moveCenterToLeft(Context context, ImageView imgJP1, ImageView imgChrJP1, TextView tvJP1){
        if (seccion != 0){
            imgJP1.setRotationY(-180);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.moveoutjp1);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    postStatus = 0;
                    imgJP1.setRotationY(0);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            imgJP1.startAnimation(animation);
            imgChrJP1.startAnimation(animation);
            tvJP1.startAnimation(animation);
            postStatus = 1;
            seccion--;
        }
    }
    /**
     * Método utilizado para mover hacia afuera la imagen el jugado 2
     * @param context
     * @param imgJP2
     * @param imgChrJP2
     * @param tvJP2
     */
    public void moveCenterToRight(Context context, ImageView imgJP2, ImageView imgChrJP2, TextView tvJP2){
        if (seccion !=0){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.moveoutjp2);
            imgJP2.setRotationY(0);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    postStatus = 0;
                    imgJP2.setRotationY(-180);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            imgJP2.startAnimation(animation);
            imgChrJP2.startAnimation(animation);
            tvJP2.startAnimation(animation);
            postStatus = 1;
            seccion--;
        }
    }
}
