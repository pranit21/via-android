package com.fierydevs.chatapp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Pranit on 10-12-2016.
 */

public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
