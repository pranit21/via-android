package com.itvedant.realmdemo.models;

import com.itvedant.realmdemo.app.AppController;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Pranit on 26-08-2016.
 */

public class Movie extends RealmObject {
    @PrimaryKey
    private int id;
    private String name, description, year, imageUrl;

    @Ignore
    private static int size;
    @Ignore
    private static AtomicInteger pk;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getSize() {
        return size;
    }

    public static int getNextPrimaryKey() {
        Realm realm = AppController.getInstance().getRealmInstance();

        int _id;
        try {
            _id = realm.where(Movie.class).max("id").intValue();
        } catch (Exception e) {
            _id = 0;
        }

        pk = new AtomicInteger(_id);

        return pk.incrementAndGet();
    }

    public static RealmResults<Movie> getAllMovies() {
        Realm realm = AppController.getInstance().getRealmInstance();

        RealmResults<Movie> results = realm.where(Movie.class).findAll();

        size = results.size();

        return results;
    }

    public static void updateMovie(final Movie movie) {
        Realm realm = AppController.getInstance().getRealmInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(movie);
            }
        });
    }

    public static void insertMovie(final Movie movie) {
        Realm realm = AppController.getInstance().getRealmInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(movie);
            }
        });
    }

    public static void deleteMovie(long id) {
        Realm realm = AppController.getInstance().getRealmInstance();

        final RealmResults<Movie> results = realm.where(Movie.class).equalTo("id", id).findAll();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteFromRealm(0);
            }
        });
    }

    public static Movie getMovie(long id) {
        Realm realm = AppController.getInstance().getRealmInstance();

        Movie movie = realm.where(Movie.class).equalTo("id", id).findFirst();

        return movie;
    }
}
