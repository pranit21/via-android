package com.itvedant.restdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText uname, pass;
    private Button login;
    String u, p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uname = (EditText) findViewById(R.id.uname);
        pass = (EditText) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.login_btn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u = uname.getText().toString();
                p = pass.getText().toString();

                Map<String, String> jsonParams = new HashMap<String, String>();
                jsonParams.put("email", u);
                jsonParams.put("password", p);

                // Preparing volley's json object request
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppConstant.API_ENDPOINT+"/login",
                        new JSONObject(jsonParams), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Albums Response: " + response.toString());
                        /*try {
                            // Parsing the json response
                            JSONArray entry = response.getJSONObject(TAG_FEED)
                                    .getJSONArray(TAG_ENTRY);

                            // loop through albums nodes and add them to album
                            // list
                            for (int i = 0; i < entry.length(); i++) {
                                JSONObject albumObj = (JSONObject) entry.get(i);
                                // album id
                                String albumId = albumObj.getJSONObject(
                                        TAG_GPHOTO_ID).getString(TAG_T);

                                // album title
                                String albumTitle = albumObj.getJSONObject(
                                        TAG_ALBUM_TITLE).getString(TAG_T);

                                Category album = new Category();
                                album.setId(albumId);
                                album.setTitle(albumTitle);

                                // add album to list
                                albums.add(album);

                                Log.d(TAG, "Album Id: " + albumId
                                        + ", Album Title: " + albumTitle);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.msg_unknown_error),
                                    Toast.LENGTH_LONG).show();
                        }*/

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley Error: " + error.getMessage());

                        // show error toast
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.splash_error),
                                Toast.LENGTH_LONG).show();

                    }
                }) /*{
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", u);
                        params.put("password", p);
                        return params;
                    }
                }*/;

                // disable the cache for this request, so that it always fetches updated
                // json
                jsonObjReq.setShouldCache(false);

                // Making the request
                AppController.getInstance().addToRequestQueue(jsonObjReq);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
