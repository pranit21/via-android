package com.fierydevs.customlistview.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Pranit on 05-12-2015.
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
    private static final String PREF_NAME = "CustomListView";

    // Api Key
    private static final String KEY_API_KEY = "api_key";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

    }

    /**
     * Storing google username
     * */
    public void setApiKey(String apiKey) {
        editor = pref.edit();

        editor.putString(KEY_API_KEY, apiKey);

        // commit changes
        editor.commit();
    }

    public String getApiKey() {
        return pref.getString(KEY_API_KEY, "");
    }
}
