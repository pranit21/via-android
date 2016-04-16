package com.itvedant.todorestdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText email, password;
    private Button login;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences("TodoRestDemo", MODE_PRIVATE);

        boolean isLoggedIn = sharedPreferences.getBoolean("loggedIn", false);
        if (isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, TodosActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = email.getText().toString();
                String p = password.getText().toString();
                appLogin(e, p);
            }
        });
    }

    private void appLogin(final String email, final String password) {
        progressDialog.setMessage("Loggin in...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        /*JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppConstants.API_ENDPOINT + AppConstants.LOGIN,
                new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Success", response);
                    progressDialog.hide();

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean error = jsonObject.getBoolean("error");

                        if (error) {
                            String message = jsonObject.getString("message");
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            String apiKey = jsonObject.getString("apiKey");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("apiKey", apiKey);
                            editor.putBoolean("loggedIn", true);
                            editor.commit();

                            Intent intent = new Intent(MainActivity.this, TodosActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", error.toString());
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

                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "LoginApi");
    }
}
