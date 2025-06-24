package com.developerali.mylifequran.CalenderModels;

import java.util.List;

public class AdvanceDateResponse {

    public int code;
    public String status;
    public List<DateAdvanceData> data;

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

    public List<DateAdvanceData> getData() {
        return data;
    }

    public void setData(List<DateAdvanceData> data) {
        this.data = data;
    }
}
