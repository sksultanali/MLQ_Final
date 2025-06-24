package com.developerali.mylifequran.TimingModel;

public class Data {
    Timings timings;
    DateInfo date;
    Meta meta;

    public Data(Timings timings, DateInfo date, Meta meta) {
        this.timings = timings;
        this.date = date;
        this.meta = meta;
    }

    public Data() {
    }

    public Timings getTimings() {
        return timings;
    }

    public void setTimings(Timings timings) {
        this.timings = timings;
    }

    public DateInfo getDate() {
        return date;
    }

    public void setDate(DateInfo date) {
        this.date = date;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
