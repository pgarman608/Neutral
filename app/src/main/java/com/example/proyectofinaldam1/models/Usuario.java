package com.example.proyectofinaldam1.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Usuario {
    private String uid;
    private String nick;
    private int points;
    private List<String> uidgames;
    private List<String> uidTournament;
    public Usuario(String uid, String nick,int points) {
        this.uid = uid;
        this.nick = nick;
        this.points = points;
        this.uidgames = new ArrayList<>();
        this.uidTournament = new ArrayList<>();
    }

    public Usuario(String uid, String nick, int points, List<String> games, List<String> idTournament) {
        this.uid = uid;
        this.nick = nick;
        this.points = points;
        this.uidgames = games;
        this.uidTournament = idTournament;
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
    public List<String> getGames() {
        return uidgames;
    }
    public void setGames(List<String> games) {
        this.uidgames = games;
    }
    public String getGames(int pos) {
        return uidgames.get(pos);
    }
    public void setGames(int pos, String uid) {
        this.uidgames.add(pos,uid);
    }
    public List<String> getUidTournament() {
        return uidTournament;
    }
    public void setUidTournament(int pos, String uid) {
        this.uidTournament.add(pos,uid);
    }
    public String getUidTournament(int pos) {
        return uid;
    }
    public void setUidTournament(List<String> idTournament) {
        this.uidTournament = idTournament;
    }
    public String getNick() {
        return nick;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }
}
