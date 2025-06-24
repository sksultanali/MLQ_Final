package com.developerali.mylifequran.CalenderModels;

import com.developerali.mylifequran.TimingModel.Timings;

public class DateAdvanceData {
    public Timings timings;
    public DateData date;

    public Timings getTimings() {
        return timings;
    }

    public void setTimings(Timings timings) {
        this.timings = timings;
    }

    public DateData getDate() {
        return date;
    }

    public void setDate(DateData date) {
        this.date = date;
    }
}
