package com.developerali.mylifequran.TimingModel;

public class Offset {

    private int imsak;
    private int fajr;
    private int sunrise;
    private int dhuhr;
    private int asr;
    private int maghrib;
    private int sunset;
    private int isha;
    private int midnight;

    public Offset(int imsak, int fajr, int sunrise, int dhuhr, int asr, int maghrib, int sunset, int isha, int midnight) {
        this.imsak = imsak;
        this.fajr = fajr;
        this.sunrise = sunrise;
        this.dhuhr = dhuhr;
        this.asr = asr;
        this.maghrib = maghrib;
        this.sunset = sunset;
        this.isha = isha;
        this.midnight = midnight;
    }

    public Offset() {
    }

    public int getImsak() {
        return imsak;
    }

    public void setImsak(int imsak) {
        this.imsak = imsak;
    }

    public int getFajr() {
        return fajr;
    }

    public void setFajr(int fajr) {
        this.fajr = fajr;
    }

    public int getSunrise() {
        return sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getDhuhr() {
        return dhuhr;
    }

    public void setDhuhr(int dhuhr) {
        this.dhuhr = dhuhr;
    }

    public int getAsr() {
        return asr;
    }

    public void setAsr(int asr) {
        this.asr = asr;
    }

    public int getMaghrib() {
        return maghrib;
    }

    public void setMaghrib(int maghrib) {
        this.maghrib = maghrib;
    }

    public int getSunset() {
        return sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    public int getIsha() {
        return isha;
    }

    public void setIsha(int isha) {
        this.isha = isha;
    }

    public int getMidnight() {
        return midnight;
    }

    public void setMidnight(int midnight) {
        this.midnight = midnight;
    }
}
