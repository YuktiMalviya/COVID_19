package com.example.covid19;

public class Doctorfirebase {

    private String Name = "";
    private String Email = "";
    private String Uid = "";

    public Doctorfirebase() {
    }

    public Doctorfirebase(String name, String email, String uid) {
        Name = name;
        Email = email;
        Uid = uid;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getUid() {
        return Uid;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
