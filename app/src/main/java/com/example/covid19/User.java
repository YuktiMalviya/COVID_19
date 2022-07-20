package com.example.covid19;

public class User {

    private String uid = "";
    private String Name = "";
    private String email = "";


    public User() {
    }

    public User(String uid, String name, String email) {
        this.uid = uid;
        Name = name;
        this.email = email;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
