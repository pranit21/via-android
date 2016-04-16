package com.itvedant.todorestdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodosActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private ListView listView;
    private List<Todo> todosList;
    private TodosAdapter todosAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos);

        sharedPreferences = getSharedPreferences("TodoRestDemo", MODE_PRIVATE);

        progressDialog = new ProgressDialog(this);

        todosList = new ArrayList<Todo>();
        todosAdapter = new TodosAdapter(TodosActivity.this, todosList);

        listView = (ListView) findViewById(R.id.todos);
        listView.setAdapter(todosAdapter);

        //Toast.makeText(TodosActivity.this, apiKey, Toast.LENGTH_SHORT).show();

        getTodos();
    }

    private void getTodos() {
        progressDialog.setMessage("Getting todos...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, AppConstants.API_ENDPOINT + AppConstants.GET_TODOS,
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
                                Toast.makeText(TodosActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray jsonArray = jsonObject.getJSONArray("todos");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    int id = obj.getInt("id");
                                    String todo = obj.getString("todo");
                                    int status = obj.getInt("status");
                                    String createdAt = obj.getString("createdAt");

                                    Todo td = new Todo();
                                    td.setId(id);
                                    td.setTodo(todo);
                                    td.setStatus(status);
                                    td.setCreatedAt(createdAt);

                                    todosList.add(td);
                                }
                                todosAdapter.notifyDataSetChanged();
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
                if (networkResponse != null && networkResponse.data != null) {
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

                Toast.makeText(TodosActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                String apiKey = sharedPreferences.getString("apiKey", "");
                params.put("Authorization", apiKey);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "GetTodos");
    }
}
