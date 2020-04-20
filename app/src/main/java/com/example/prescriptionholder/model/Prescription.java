package com.example.prescriptionholder.model;

public class Prescription {

    private int id,user;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Prescription(int id, int user, String url) {
        this.id = id;
        this.user = user;
        this.url = url;
    }
}

