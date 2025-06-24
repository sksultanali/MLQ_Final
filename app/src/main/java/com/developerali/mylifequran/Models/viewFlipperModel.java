package com.developerali.mylifequran.Models;

public class viewFlipperModel {

    String hadisText1, hadisText2, homeText1, homeText2, practiceText1, practiceText2;

    public viewFlipperModel(String hadisText1, String hadisText2, String homeText1, String homeText2, String practiceText1, String practiceText2) {
        this.hadisText1 = hadisText1;
        this.hadisText2 = hadisText2;
        this.homeText1 = homeText1;
        this.homeText2 = homeText2;
        this.practiceText1 = practiceText1;
        this.practiceText2 = practiceText2;
    }

    public viewFlipperModel() {
    }

    public String getHadisText1() {
        return hadisText1;
    }

    public void setHadisText1(String hadisText1) {
        this.hadisText1 = hadisText1;
    }

    public String getHadisText2() {
        return hadisText2;
    }

    public void setHadisText2(String hadisText2) {
        this.hadisText2 = hadisText2;
    }

    public String getHomeText1() {
        return homeText1;
    }

    public void setHomeText1(String homeText1) {
        this.homeText1 = homeText1;
    }

    public String getHomeText2() {
        return homeText2;
    }

    public void setHomeText2(String homeText2) {
        this.homeText2 = homeText2;
    }

    public String getPracticeText1() {
        return practiceText1;
    }

    public void setPracticeText1(String practiceText1) {
        this.practiceText1 = practiceText1;
    }

    public String getPracticeText2() {
        return practiceText2;
    }

    public void setPracticeText2(String practiceText2) {
        this.practiceText2 = practiceText2;
    }
}
