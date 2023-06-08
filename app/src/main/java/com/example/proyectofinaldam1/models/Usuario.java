package com.example.proyectofinaldam1.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Usuario {
    private String uid;
    private String nick;
    private int points;
    private List<Set> games;
    private List<String> idTournament;

    public Usuario(String uid, String nick,int points) {
        this.uid = uid;
        this.nick = nick;
        this.points = points;
        games = new ArrayList<>();
        idTournament = new ArrayList<>();
    }

    public Usuario(String uid, String nick, int points, List<Set> games, List<String> idTournament) {
        this.uid = uid;
        this.nick = nick;
        this.points = points;
        this.games = games;
        this.idTournament = idTournament;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<Set> getGames() {
        return games;
    }

    public void setGames(List<Set> games) {
        this.games = games;
    }

    public List<String> getIdTournament() {
        return idTournament;
    }

    public void setIdTournament(List<String> idTournament) {
        this.idTournament = idTournament;
    }
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "uid='" + uid + '\'' +
                ", nick='" + nick + '\'' +
                ", points=" + points +
                ", games=" + games +
                ", idTournament=" + idTournament +
                '}';
    }
}
