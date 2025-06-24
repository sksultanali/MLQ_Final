package com.developerali.mylifequran.TimingModel;

public class Timings {

    private String Fajr;
    private String Sunrise;
    private String Dhuhr;
    private String Asr;
    private String Sunset;
    private String Maghrib;
    private String Isha;
    private String Imsak;
    private String Midnight;
    private String Firstthird;
    private String Lastthird;

    public Timings(String fajr, String sunrise, String dhuhr, String asr,
                   String sunset, String maghrib, String isha, String imsak, String midnight, String firstthird, String lastthird) {
        Fajr = fajr;
        Sunrise = sunrise;
        Dhuhr = dhuhr;
        Asr = asr;
        Sunset = sunset;
        Maghrib = maghrib;
        Isha = isha;
        Imsak = imsak;
        Midnight = midnight;
        Firstthird = firstthird;
        Lastthird = lastthird;
    }

    public Timings() {
    }

    public String getFajr() {
        return Fajr;
    }

    public void setFajr(String fajr) {
        Fajr = fajr;
    }

    public String getSunrise() {
        return Sunrise;
    }

    public void setSunrise(String sunrise) {
        Sunrise = sunrise;
    }

    public String getDhuhr() {
        return Dhuhr;
    }

    public void setDhuhr(String dhuhr) {
        Dhuhr = dhuhr;
    }

    public String getAsr() {
        return Asr;
    }

    public void setAsr(String asr) {
        Asr = asr;
    }

    public String getSunset() {
        return Sunset;
    }

    public void setSunset(String sunset) {
        Sunset = sunset;
    }

    public String getMaghrib() {
        return Maghrib;
    }

    public void setMaghrib(String maghrib) {
        Maghrib = maghrib;
    }

    public String getIsha() {
        return Isha;
    }

    public void setIsha(String isha) {
        Isha = isha;
    }

    public String getImsak() {
        return Imsak;
    }

    public void setImsak(String imsak) {
        Imsak = imsak;
    }

    public String getMidnight() {
        return Midnight;
    }

    public void setMidnight(String midnight) {
        Midnight = midnight;
    }

    public String getFirstthird() {
        return Firstthird;
    }

    public void setFirstthird(String firstthird) {
        Firstthird = firstthird;
    }

    public String getLastthird() {
        return Lastthird;
    }

    public void setLastthird(String lastthird) {
        Lastthird = lastthird;
    }
}
