package com.developerali.mylifequran.CalenderModels;

import java.util.List;

public class Hijri {

    public String date;
    public String format;
    public String day;
    public Weekday weekday;
    public Month month;
    public String year;
    public Designation designation;
    public List<String> holidays;

    public Hijri(String date, String format, String day, Weekday weekday, Month month, String year, Designation designation, List<String> holidays) {
        this.date = date;
        this.format = format;
        this.day = day;
        this.weekday = weekday;
        this.month = month;
        this.year = year;
        this.designation = designation;
        this.holidays = holidays;
    }

    public Hijri() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Weekday getWeekday() {
        return weekday;
    }

    public void setWeekday(Weekday weekday) {
        this.weekday = weekday;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public List<String> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<String> holidays) {
        this.holidays = holidays;
    }
}
