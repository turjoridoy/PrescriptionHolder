package com.example.prescriptionholder.model;

public class User {

    private int id;
    private String name, email, usertype, phone, password;

    public User(int id, String name, String email, String usertype, String phone, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.usertype = usertype;
        this.phone = phone;
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

    public String getUsertype() {
        return usertype;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
