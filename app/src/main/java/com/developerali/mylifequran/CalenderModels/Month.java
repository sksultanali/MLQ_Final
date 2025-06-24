package com.developerali.mylifequran.CalenderModels;

public class Month {

    public String number;
    public String en;
    public String ar;

    public Month(String number, String en, String ar) {
        this.number = number;
        this.en = en;
        this.ar = ar;
    }

    public Month() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
