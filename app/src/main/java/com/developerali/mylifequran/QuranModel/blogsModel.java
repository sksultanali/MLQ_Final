package com.developerali.mylifequran.QuranModel;

public class blogsModel {

    public String kind;
    public String id;
    transient public String  blog;
    transient public String  published;
    transient public String updated;
    public String url;
    public String selfLink;
    public String title;
    public String content;
    transient public String  author;
    transient public String  replies;
    public String etag;

    public blogsModel(String kind, String id, String blog, String published,
                      String updated, String url, String selfLink, String title,
                      String content, String author, String replies, String etag) {
        this.kind = kind;
        this.id = id;
        this.blog = blog;
        this.published = published;
        this.updated = updated;
        this.url = url;
        this.selfLink = selfLink;
        this.title = title;
        this.content = content;
        this.author = author;
        this.replies = replies;
        this.etag = etag;
    }

    public blogsModel() {
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReplies() {
        return replies;
    }

    public void setReplies(String replies) {
        this.replies = replies;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }
}
