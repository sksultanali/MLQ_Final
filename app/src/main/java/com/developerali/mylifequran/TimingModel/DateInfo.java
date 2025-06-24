package com.developerali.mylifequran.TimingModel;

public class DateInfo {

    private String readable;
    private String timestamp;
    private Hijri hijri;
    private GregorianDate gregorian;

    public DateInfo(String readable, String timestamp, Hijri hijri, GregorianDate gregorian) {
        this.readable = readable;
        this.timestamp = timestamp;
        this.hijri = hijri;
        this.gregorian = gregorian;
    }

    public DateInfo() {
    }

    public String getReadable() {
        return readable;
    }

    public void setReadable(String readable) {
        this.readable = readable;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Hijri getHijri() {
        return hijri;
    }

    public void setHijri(Hijri hijri) {
        this.hijri = hijri;
    }

    public GregorianDate getGregorian() {
        return gregorian;
    }

    public void setGregorian(GregorianDate gregorian) {
        this.gregorian = gregorian;
    }
}
