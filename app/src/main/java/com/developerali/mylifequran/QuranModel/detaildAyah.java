package com.developerali.mylifequran.QuranModel;

public class detaildAyah {

    String number, text, numberInSurah, juzm, manzil, page, ruku, hizbQuarter;
    transient String sajda;

    public detaildAyah(String number, String text, String numberInSurah, String juzm, String manzil, String page, String ruku, String hizbQuarter, String sajda) {
        this.number = number;
        this.text = text;
        this.numberInSurah = numberInSurah;
        this.juzm = juzm;
        this.manzil = manzil;
        this.page = page;
        this.ruku = ruku;
        this.hizbQuarter = hizbQuarter;
        this.sajda = sajda;
    }

    public detaildAyah() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNumberInSurah() {
        return numberInSurah;
    }

    public void setNumberInSurah(String numberInSurah) {
        this.numberInSurah = numberInSurah;
    }

    public String getJuzm() {
        return juzm;
    }

    public void setJuzm(String juzm) {
        this.juzm = juzm;
    }

    public String getManzil() {
        return manzil;
    }

    public void setManzil(String manzil) {
        this.manzil = manzil;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRuku() {
        return ruku;
    }

    public void setRuku(String ruku) {
        this.ruku = ruku;
    }

    public String getHizbQuarter() {
        return hizbQuarter;
    }

    public void setHizbQuarter(String hizbQuarter) {
        this.hizbQuarter = hizbQuarter;
    }

    public String getSajda() {
        return sajda;
    }

    public void setSajda(String sajda) {
        this.sajda = sajda;
    }
}
