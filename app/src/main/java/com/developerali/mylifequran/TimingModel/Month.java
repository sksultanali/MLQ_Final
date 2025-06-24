package com.developerali.mylifequran.TimingModel;

public class Month {

    private int number;
    private String en;
    private String ar;


    public Month(int number, String en, String ar) {
        this.number = number;
        this.en = en;
        this.ar = ar;
    }

    public Month() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
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
