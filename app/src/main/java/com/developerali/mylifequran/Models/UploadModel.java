package com.developerali.mylifequran.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UploadModel implements Parcelable {

    String title, description, imageUrl, date, key, collection;
    Long love;

    public UploadModel(String title, String description, String imageUrl, String date, String key, String collection, Long love) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.date = date;
        this.key = key;
        this.collection = collection;
        this.love = love;
    }

    public UploadModel() {
    }

    protected UploadModel(Parcel in) {
        title = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        date = in.readString();
        key = in.readString();
        collection = in.readString();
        if (in.readByte() == 0) {
            love = null;
        } else {
            love = in.readLong();
        }
    }

    public static final Creator<UploadModel> CREATOR = new Creator<UploadModel>() {
        @Override
        public UploadModel createFromParcel(Parcel in) {
            return new UploadModel(in);
        }

        @Override
        public UploadModel[] newArray(int size) {
            return new UploadModel[size];
        }
    };

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public Long getLove() {
        return love;
    }

    public void setLove(Long love) {
        this.love = love;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeString(date);
        dest.writeString(key);
        dest.writeString(collection);
        if (love == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(love);
        }
    }
}
