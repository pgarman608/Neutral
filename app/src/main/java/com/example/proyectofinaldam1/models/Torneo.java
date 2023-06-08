package com.example.proyectofinaldam1.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Torneo {
    private int uid;
    private String uidCreator;
    private String name;
    private String info;
    private int allPoints;
    private int numMaxUsers;
    private List<String> sets;
    private List<String> usersList;
    private int end;
    private int top1;
    private int top2;
    private int top3;

    public Torneo() {
    }

    public Torneo(int uid, String uidCreator, String name, String info, int numMaxUsers, int precioEntrada) {
        this.uid = uid;
        this.name = name;
        this.uidCreator = uidCreator;
        this.info = info;
        this.allPoints = 0;
        this.numMaxUsers = numMaxUsers;
        this.sets = new ArrayList<>();
        this.usersList = new ArrayList<>();
        this.end = 0;
        this.top1 = 0;
        this.top2 = 0;
        this.top3 = 0;
    }
    public Torneo(int uid, String uidCreator, String name, String info, int allPoints, int numMaxUsers, List<String> sets, List<String> usersList) {
        this.uid = uid;
        this.uidCreator = uidCreator;
        this.name = name;
        this.info = info;
        this.allPoints = allPoints;
        this.numMaxUsers = numMaxUsers;
        this.sets = sets;
        this.usersList = usersList;
        this.end = 0;
        this.top1 = 0;
        this.top2 = 0;
        this.top3 = 0;
    }
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public String getUidCreator() {
        return uidCreator;
    }
    public void setUidCreator(String uidCreator) {
        this.uidCreator = uidCreator;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public int getAllPoints() {
        return allPoints;
    }
    public void setAllPoints(int allPoints) {
        this.allPoints = allPoints;
    }
    public int getNumMaxUsers() {
        return numMaxUsers;
    }
    public void setNumMaxUsers(int numMaxUsers) {
        this.numMaxUsers = numMaxUsers;
    }
    public List<String> getSets() {
        return sets;
    }
    public void setSets(List<String> sets) {
        this.sets = sets;
    }
    public String getSets(int pos) {
        return sets.get(pos);
    }
    public void setSets(int pos,String set) {
        if (sets == null){
            sets = new ArrayList<>();
        }
        this.sets.add(pos,set);
    }
    public List<String> getUsersList() {
        return usersList;
    }
    public void setUsersList(List<String> usersList) {
        this.usersList = usersList;
    }
    public String getUsersList(int pos) {
        return usersList.get(pos);
    }
    public void setUsersList(int pos,String users) {
        if (usersList == null){
            usersList = new ArrayList<>();
        }
        this.usersList.add(pos,users);
    }
    public int getTop1() {
        return top1;
    }
    public void setTop1(int top1) {
        this.top1 = top1;
    }
    public int getTop2() {
        return top2;
    }
    public void setTop2(int top2) {
        this.top2 = top2;
    }
    public int getTop3() {
        return top3;
    }
    public void setTop3(int top3) {
        this.top3 = top3;
    }
    public int getEnd() {
        return end;
    }
    public void setEnd(int end) {
        this.end = end;
    }
}