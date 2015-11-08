package com.fierydevs.staggeredimages.models;

/**
 * Created by Pranit on 12-09-2015.
 */
public class Photos {
    private String photoId;
    private String title;
    private String url;

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

    public void setPhoto_id(String photo_id) {
        this.photoId = photo_id;
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
