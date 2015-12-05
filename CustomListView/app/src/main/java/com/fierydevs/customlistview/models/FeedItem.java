package com.fierydevs.customlistview.models;

/**
 * Created by Pranit on 05-12-2015.
 */
public class FeedItem {
    private int id;
    private String name, feedImage, status, profilePic, timestamp, url;

    public FeedItem() {
    }

    public FeedItem(int id, String name, String feedImage, String status, String profilePic, String timestamp, String url) {
        this.id = id;
        this.name = name;
        this.feedImage = feedImage;
        this.status = status;
        this.profilePic = profilePic;
        this.timestamp = timestamp;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeedImage() {
        return feedImage;
    }

    public void setFeedImage(String feedImage) {
        this.feedImage = feedImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
