package com.example.proyectofinaldam1.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Set {
    private int uidTrns;
    private int uid;
    private Usuario uid_j1;
    private Usuario uid_j2;
    private List<Integer> games;
    private List<Integer> char_j1;
    private List<Integer> char_j2;
    private List<Integer> stages;
    private String round;
    private int startSS;
    private int typeSets = 5;
    private int end;
    public Set() {
    }
    private int jp1join;
    private int jp2join;
    public Set(int trnuid,int uid,Usuario uid_j1, Usuario uid_j2, String round) {
        this.uidTrns = trnuid;
        this.uid = uid;
        this.uid_j1 = uid_j1;
        this.uid_j2 = uid_j2;
        this.round = round;
        this.games = new ArrayList<>();
        this.char_j1 = new ArrayList<>();
        this.char_j2 = new ArrayList<>();
        this.stages = new ArrayList<>();
        this.end = 0;
        this.startSS = new Random().nextInt(2);
        this.jp1join = 0;
        this.jp2join = 0;
    }
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public Usuario getUid_j1() {
        return uid_j1;
    }
    public void setUid_j1(Usuario uid_j1) {
        this.uid_j1 = uid_j1;
    }
    public Usuario getUid_j2() {
        return uid_j2;
    }
    public void setUid_j2(Usuario uid_j2) {
        this.uid_j2 = uid_j2;
    }
    public List<Integer> getGames() {
        return games;
    }
    public void setGames(int pos,int games) {
        this.games.add(pos,games);
    }
    public int getGames(int pos) {
        return games.get(pos);
    }
    public void setGames(List<Integer> games) {
        this.games = games;
    }
    public List<Integer> getListChar_j1() {
        return char_j1;
    }
    public int getChar_j1(int pos) {
        return char_j1.get(pos);
    }
    public void setListChar_j1(List<Integer> char_j1) {
        this.char_j1 = char_j1;
    }
    public void setChar_j1(int pos ,int char_j1) {
        this.char_j1.add(pos,char_j1);
    }
    public int getChar_j2(int pos) {
        return char_j2.get(pos);
    }
    public List<Integer> getListChar_j2() {
        return char_j2;
    }
    public void setListChar_j2(List<Integer> char_j2) {
        this.char_j2 = char_j2;
    }
    public void setChar_j2(int pos, int char_j2) {
        this.char_j2.add(pos,char_j2);
    }
    public String getRound() {
        return round;
    }
    public void setRound(String round) {
        this.round = round;
    }
    public int getStartSS() {
        return startSS;
    }
    public void setStartSS(int startSS) {
        this.startSS = startSS;
    }
    public int getTypeSets() {
        return typeSets;
    }
    public void setTypeSets(int typeSets) {
        this.typeSets = typeSets;
    }
    public int getEnd() {
        return end;
    }
    public void setEnd(int end) {
        this.end = end;
    }
    public int getJp1join() {
        return jp1join;
    }
    public void setJp1join(int jp1join) {
        this.jp1join = jp1join;
    }
    public int getJp2join() {
        return jp2join;
    }
    public void setJp2join(int jp2join) {
        this.jp2join = jp2join;
    }
    public List<Integer> getChar_j1() {
        return char_j1;
    }
    public List<Integer> getStages() {
        return stages;
    }
    public void setStages(List<Integer> stage) {
        this.stages = stage;
    }
    public int getStages(int pos) {
        return stages.get(pos);
    }
    public void setStages(int pos,int stage) {
        this.stages.add(pos,stage);
    }
    public List<Integer> getChar_j2() {
        return char_j2;
    }
    public int getUidTrns() {
        return uidTrns;
    }

    public void setUidTrns(int uidTrns) {
        this.uidTrns = uidTrns;
    }
}
