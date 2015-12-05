package com.fierydevs.customlistview;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fierydevs.customlistview.adapters.FeedListAdapter;
import com.fierydevs.customlistview.app.AppConstant;
import com.fierydevs.customlistview.models.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {
    private static final String TAG = FeedActivity.class.getName();
    private ProgressDialog progressDialog;
    private ListView listView;
    private List<FeedItem> items;
    private FeedListAdapter feedListAdapter;
    private String FEED_URL = AppConstant.API_ENDPOINT + "feed";
    private String apiKey = AppController.getInstance().getPrefManger().getApiKey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        progressDialog = new ProgressDialog(this);

        listView = (ListView) findViewById(R.id.list_view);

        items = new ArrayList<FeedItem>();

        feedListAdapter = new FeedListAdapter(getApplicationContext(), items);
        listView.setAdapter(feedListAdapter);

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(FEED_URL);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
            // making fresh volley request and getting json
            StringRequest request = new StringRequest(Request.Method.POST, FEED_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();

                        if (response != null) {
                            try {
                                parseJsonFeed(new JSONObject(response));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", apiKey);

                    return headers;
                }
            };

            AppController.getInstance().addToRequestQueue(request);
        }
    }

    private void parseJsonFeed(JSONObject jsonObject) {
        try {
            JSONArray feeds = jsonObject.getJSONArray("feeds");

            for (int i = 0; i < feeds.length(); i++) {
                JSONObject feedObject = (JSONObject) feeds.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObject.getInt("id"));
                item.setName(feedObject.getString("name"));

                // Image might be null sometimes
                String image = feedObject.isNull("image") ? null : feedObject
                        .getString("image");
                item.setFeedImage(image);
                item.setStatus(feedObject.getString("status"));
                item.setProfilePic(feedObject.getString("profile_pic"));
                item.setTimestamp(feedObject.getString("timestamp"));

                // url might be null sometimes
                String feedUrl = feedObject.isNull("url") ? null : feedObject
                        .getString("url");
                item.setUrl(feedUrl);

                items.add(item);
            }

            feedListAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
