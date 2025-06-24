package com.developerali.mylifequran.CalenderModels;

import java.util.List;

public class SingleDateResponse {

    public int code;
    public String status;
    public DateData data;

    public SingleDateResponse(int code, String status, DateData data) {
        this.code = code;
        this.status = status;
        this.data = data;
    }

    public SingleDateResponse() {
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

    public DateData getData() {
        return data;
    }

    public void setData(DateData data) {
        this.data = data;
    }
}
