package com.developerali.mylifequran.CalenderModels;

import java.util.List;

public class DateResponse {
    public int code;
    public String status;
    public List<DateData> data;

    public DateResponse(int code, String status, List<DateData> data) {
        this.code = code;
        this.status = status;
        this.data = data;
    }

    public DateResponse() {
    }

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

    public List<DateData> getData() {
        return data;
    }

    public void setData(List<DateData> data) {
        this.data = data;
    }
}
