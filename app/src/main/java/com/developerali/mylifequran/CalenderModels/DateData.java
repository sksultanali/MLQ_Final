package com.developerali.mylifequran.CalenderModels;

public class DateData {
    public Hijri hijri;
    public Gregorian gregorian;

    public DateData(Hijri hijri, Gregorian gregorian) {
        this.hijri = hijri;
        this.gregorian = gregorian;
    }

    public DateData() {
    }

    public Hijri getHijri() {
        return hijri;
    }

    public void setHijri(Hijri hijri) {
        this.hijri = hijri;
    }

    public Gregorian getGregorian() {
        return gregorian;
    }

    public void setGregorian(Gregorian gregorian) {
        this.gregorian = gregorian;
    }
}
