package com.example.covid19;

public class Cardlayout {

    private String mState= "";
    private String mconfirmed = "";
    private String mactive = "";
    private String mrecovered = "";
    private String mdeaths = "";

    public Cardlayout(String state,String confirmed, String active, String recovered, String deaths) {

        this.mState = state;
        this.mconfirmed = confirmed;
        this.mactive = active;
        this.mrecovered = recovered;
        this.mdeaths = deaths;
    }

    public String getmState() {
        return mState;
    }

    public String getmconfirmed() {
        return mconfirmed;
    }

    public String getmactive() {
        return mactive;
    }

    public String getmrecovered() {
        return mrecovered;
    }

    public String getmdeaths() {
        return mdeaths;
    }
}
