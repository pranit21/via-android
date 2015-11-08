package com.itvedant.restdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.itvedant.restdemo.app.AppConstant;

/**
 * Created by Pranit on 16-07-2015.
 */
public class PrefManager {
    private static final String TAG = PrefManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "RestApi";

    // Google's username
    private static final String KEY_USERNAME = "username";

    // No of grid columns
    private static final String KEY_PASSWORD = "password";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

    }

    /**
     * Storing google username
     * */
    public void setUsername(String username) {
        editor = pref.edit();

        editor.putString(KEY_USERNAME, username);

        // commit changes
        editor.commit();
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, "");
    }
}
