package com.example.prescriptionholder.model;

public class User {

    private int id;
    private String name, email, password;
    private boolean is_doctor;

    public User(int id, String name, String email, boolean is_doctor,  String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.is_doctor = is_doctor;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean getUsertype() {
        return is_doctor;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
