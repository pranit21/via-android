package com.itvedant.restdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.itvedant.restdemo.app.AppConstant;
import com.itvedant.restdemo.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Splash extends Activity {
    private static final String TAG = Splash.class.getSimpleName();
    private static final String TAG_FEED = "feed", TAG_ENTRY = "entry",
            TAG_GPHOTO_ID = "gphoto$id", TAG_T = "$t",
            TAG_ALBUM_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //getActionBar().hide();
        setContentView(R.layout.activity_splash);

        if (AppController.getInstance().getPrefManger().getUsername().equals("")
                || AppController.getInstance().getPrefManger().getUsername() == null) {
            // String the main activity
            Intent intent = new Intent(getApplicationContext(),
                    MainActivity.class);
            startActivity(intent);
            // closing spalsh activity
            finish();
        } else {
            // Preparing volley's json object request
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, AppConstant.API_ENDPOINT + "/todos",
                    null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "Albums Response: " + response.toString());
                    //List<Category> albums = new ArrayList<Category>();
                    try {
                        // Parsing the json response
                        JSONObject entry = response.getJSONObject("");


                        // Store albums in shared pref
                        //AppController.getInstance().getPrefManger().storeCategories(albums);

                        // String the main activity
                        Intent intent = new Intent(getApplicationContext(),
                                MainActivity.class);
                        startActivity(intent);
                        // closing spalsh activity
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.msg_unknown_error),
                                Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Volley Error: " + error.getMessage());

                    // show error toast
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.splash_error),
                            Toast.LENGTH_LONG).show();

                    // Unable to fetch albums
                    // check for existing Albums data in Shared Preferences
                    if (AppController.getInstance().getPrefManger().getUsername().equals("")
                            || AppController.getInstance().getPrefManger().getUsername() == null) {
                        // String the main activity
                        Intent intent = new Intent(getApplicationContext(),
                                MainActivity.class);
                        startActivity(intent);
                        // closing spalsh activity
                        finish();
                    } else {
                        Intent i = new Intent(Splash.this,
                                MainActivity.class);
                        // clear all the activities
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }

                }
            });

            // disable the cache for this request, so that it always fetches updated
            // json
            jsonObjReq.setShouldCache(false);

            // Making the request
            AppController.getInstance().addToRequestQueue(jsonObjReq);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
