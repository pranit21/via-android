package com.itvedant.contactlist;

import android.net.Uri;

import java.io.InputStream;

/**
 * Created by Pranit on 29-08-2015.
 */
public class MyContacts {
    private String contactName;
    private String contactMobile;
    private InputStream contactPhoto;

    public MyContacts() {}

    public MyContacts(String contactName, String contactMobile) {
        this.contactName = contactName;
        this.contactMobile = contactMobile;
    }

    public String getContactName() {
        return contactName;
    }

    public String  getContactMobile() {
        return contactMobile;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public InputStream getContactPhoto() {
        return contactPhoto;
    }

    public void setContactPhoto(InputStream contactPhoto) {
        this.contactPhoto = contactPhoto;
    }
}
