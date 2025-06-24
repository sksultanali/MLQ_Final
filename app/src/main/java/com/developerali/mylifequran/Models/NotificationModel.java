package com.developerali.mylifequran.Models;

public class NotificationModel {

    String id, title, content, date, link;
    boolean expand;

    public NotificationModel(String id, String title, String content, String date, String link) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.link = link;
    }

    public NotificationModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
