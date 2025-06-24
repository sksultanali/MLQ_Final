package com.developerali.mylifequran.Models;

public class BooksModel {

    String title, image, link;
    long time;

    public BooksModel(String link, long time) {
        this.link = link;
        this.time = time;
    }

    public BooksModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
