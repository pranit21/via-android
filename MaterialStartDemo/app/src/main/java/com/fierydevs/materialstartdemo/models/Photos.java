package com.fierydevs.materialstartdemo.models;

import android.content.Context;

/**
 * Created by Pranit on 10-01-2016.
 */
public class Photos {
    private String photoId, title, url;

    public Photos() {
        this.photoId = "";
        this.title = "";
        this.url = "";
    }

    public Photos(String photoId, String title, String url) {
        this.photoId = photoId;
        this.title = title;
        this.url = url;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
