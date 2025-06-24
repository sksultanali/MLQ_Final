package com.developerali.mylifequran.QuranModel;

import java.util.List;

public class Ayah {

    String number;
    String audio;
    List<String> audioSecondary;
    String text, numberInSurah, juz, manzil, page, ruku, hizbQuarter;
    transient String sajda;


    public Ayah(String number, String audio, List<String> audioSecondary, String text,
                String numberInSurah, String juz, String manzil, String page,
                String ruku, String hizbQuarter, String sajda) {
        this.number = number;
        this.audio = audio;
        this.audioSecondary = audioSecondary;
        this.text = text;
        this.numberInSurah = numberInSurah;
        this.juz = juz;
        this.manzil = manzil;
        this.page = page;
        this.ruku = ruku;
        this.hizbQuarter = hizbQuarter;
        this.sajda = sajda;
    }

    public Ayah() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public List<String> getAudioSecondary() {
        return audioSecondary;
    }

    public void setAudioSecondary(List<String> audioSecondary) {
        this.audioSecondary = audioSecondary;
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

    public String getJuz() {
        return juz;
    }

    public void setJuz(String juz) {
        this.juz = juz;
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
