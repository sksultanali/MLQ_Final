package com.developerali.mylifequran.Models;

public class socialModel {

    String facebook, whatsapp, youtube;

    public socialModel(String facebook, String whatsapp, String youtube) {
        this.facebook = facebook;
        this.whatsapp = whatsapp;
        this.youtube = youtube;
    }

    public socialModel() {
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }
}
