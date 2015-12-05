package com.itvedant.restdemo;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uname = (EditText) findViewById(R.id.uname);
        pass = (EditText) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.login_btn);
        progressDialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u = uname.getText().toString();
                p = pass.getText().toString();
                progressDialog.setMessage("Logging...");
                progressDialog.show();

                // Preparing volley's json object request
                StringRequest jsonObjReq = new StringRequest(Request.Method.POST, AppConstant.API_ENDPOINT+"login",
                        new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Albums Response: " + response);
                        progressDialog.hide();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String error = jsonObject.getString("error");
                            if(error.equals("true")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            } else {
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                String apiKey = jsonObject.getString("apiKey");
                                String createdAt = jsonObject.getString("createdAt");

                                Toast.makeText(getApplicationContext(), name + "\n" + email + "\n" + apiKey + "\n" + createdAt, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley Error: " + error.getMessage());
                        progressDialog.hide();

                        String message = error.getMessage();
                        NetworkResponse networkResponse = error.networkResponse;
                        if(networkResponse != null && networkResponse.data != null) {
                            switch (networkResponse.statusCode) {
                                case 400:
                                    String json = new String(networkResponse.data);
                                    try {
                                        JSONObject jsonObject = new JSONObject(json);
                                        message = jsonObject.getString("message");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                            }
                        }

                        // show error toast
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    }
                }) {
                    /*@Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }*/
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", u);
                        params.put("password", p);
                        return params;
                    }
                };

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
