package com.developerali.mylifequran.Blogger;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class BloggerModel implements Parcelable {

    String authorName, content, id, published, selfLink, title, updated, url;

    public BloggerModel(String authorName, String content, String id,
                        String published, String selfLink, String title, String updated, String url) {
        this.authorName = authorName;
        this.content = content;
        this.id = id;
        this.published = published;
        this.selfLink = selfLink;
        this.title = title;
        this.updated = updated;
        this.url = url;
    }

    public BloggerModel() {
    }

    protected BloggerModel(Parcel in) {
        authorName = in.readString();
        content = in.readString();
        id = in.readString();
        published = in.readString();
        selfLink = in.readString();
        title = in.readString();
        updated = in.readString();
        url = in.readString();
    }

    public static final Creator<BloggerModel> CREATOR = new Creator<BloggerModel>() {
        @Override
        public BloggerModel createFromParcel(Parcel in) {
            return new BloggerModel(in);
        }

        @Override
        public BloggerModel[] newArray(int size) {
            return new BloggerModel[size];
        }
    };

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(authorName);
        dest.writeString(content);
        dest.writeString(id);
        dest.writeString(published);
        dest.writeString(selfLink);
        dest.writeString(title);
        dest.writeString(updated);
        dest.writeString(url);
    }
}
