package com.developerali.mylifequran.Models;


import com.google.firebase.Timestamp;

public class ResultModel {

    String email, id;
    int correct, points, rank;
    String dateTime;

    public ResultModel(String email, int correct, int points, String dateTime) {
        this.email = email;
        this.correct = correct;
        this.points = points;
        this.dateTime = dateTime;
    }

    public ResultModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
