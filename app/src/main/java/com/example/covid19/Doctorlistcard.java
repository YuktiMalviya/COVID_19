package com.example.covid19;

public class Doctorlistcard {

    private String mName = "";
    private String mEmail = "";
    private String mUid = "";

    public Doctorlistcard(String mName, String mEmail, String mUid) {
        this.mName = mName;
        this.mEmail = mEmail;
        this.mUid = mUid;
    }

    public String getmName() {
        return mName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }
}
