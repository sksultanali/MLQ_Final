package com.developerali.mylifequran.TimingModel;

public class PrayerTimes {

    private int code;
    private String status;
    private Data data;

    // Constructor
    public PrayerTimes(int code, String status, Data data) {
        this.code = code;
        this.status = status;
        this.data = data;
    }

    // Getters and setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
