package com.fierydevs.customlistview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fierydevs.customlistview.app.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private ProgressDialog progressDialog;
    private EditText email, password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(AppController.getInstance().getPrefManger().getApiKey() != "") {
            Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
            startActivity(intent);
            finish();
        }

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btn_login);

        progressDialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Logging in...");
                progressDialog.show();
                StringRequest request = new StringRequest(Request.Method.POST, AppConstant.API_ENDPOINT + "login",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.hide();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String error = jsonObject.getString("error");
                                if (error.equals("true")) {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                } else {
                                    /*String name = jsonObject.getString("name");
                                    String email = jsonObject.getString("email");*/
                                    String apiKey = jsonObject.getString("apiKey");

                                    AppController.getInstance().getPrefManger().setApiKey(apiKey);

                                    Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();

                        String message = error.getMessage();
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.data != null) {
                            switch (networkResponse.statusCode) {
                                case 400:
                                    String json = new String(networkResponse.data);
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(json);
                                        message = jsonObject.getString("messsage");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                            }
                        }
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", email.getText().toString());
                        params.put("password", password.getText().toString());
                        return params;
                    }
                };

                request.setShouldCache(false);

                AppController.getInstance().addToRequestQueue(request);
            }
        });
    }
}