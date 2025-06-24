package com.developerali.mylifequran.Models;

public class updateModel {

    String title, description;
    int versionCode;

    public updateModel() {
    }

    public updateModel(String title, String description, int versionCode) {
        this.title = title;
        this.description = description;
        this.versionCode = versionCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

}
