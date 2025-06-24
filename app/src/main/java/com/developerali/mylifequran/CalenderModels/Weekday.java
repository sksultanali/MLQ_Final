package com.developerali.mylifequran.CalenderModels;

public class Weekday {
    public String en;
    public String ar;

    public Weekday(String en, String ar) {
        this.en = en;
        this.ar = ar;
    }

    public Weekday() {
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getAr() {
        return ar;
    }

    public void setAr(String ar) {
        this.ar = ar;
    }
}
