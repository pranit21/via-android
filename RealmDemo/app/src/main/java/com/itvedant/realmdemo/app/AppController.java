package com.itvedant.realmdemo.app;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Pranit on 26-08-2016.
 */

public class AppController extends Application {
    private Realm realm;
    private RealmConfiguration realmConfiguration;
    private static AppController instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfiguration);
    }

    public static synchronized AppController getInstance() {
        return instance;
    }

    public Realm getRealmInstance() {
        return realm;
    }
}
