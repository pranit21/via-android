package com.itvedant.awesomewallpapers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.itvedant.awesomewallpapers.adapters.PhotoAdapter;
import com.itvedant.awesomewallpapers.models.Photo;
import com.itvedant.awesomewallpapers.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.R.attr.columnWidth;
import static com.itvedant.awesomewallpapers.Constants.PHOTO_URL;

public class MainActivity extends AppCompatActivity {
    private GridView gridView;
    private ArrayList<Photo> photos;
    private PhotoAdapter adapter;
    private ProgressDialog progressDialog;
    private Utils utils;
    private int columnWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utils = new Utils(this);

        progressDialog = new ProgressDialog(this);

        gridView = (GridView) findViewById(R.id.gridView);

        // Initilizing Grid View
        initializeGridLayout();

        photos = new ArrayList<>();
        adapter = new PhotoAdapter(this, photos, columnWidth);
        gridView.setAdapter(adapter);

        getPhotos();
    }

    private void initializeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                Constants.GRID_PADDING, r.getDisplayMetrics());

        // Column width
        columnWidth = (int) ((utils.getScreenWidth() - ((
                Constants.NO_OF_GRID_COLUMNS + 1) * padding)) / Constants.NO_OF_GRID_COLUMNS);

        // Setting number of grid columns
        gridView.setNumColumns(Constants.NO_OF_GRID_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);

        // Setting horizontal and vertical padding
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, FullScreenImageActivity.class);

                String photoUrl = photos.get(i).getPhotoUrl();

                intent.putExtra(Constants.PHOTO_URL, photoUrl);

                startActivity(intent);
            }
        });
    }

    private void getPhotos() {
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, Constants.API_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();
                        Log.e("Response", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("stat");
                            if (status.equals("ok")) {
                                JSONObject photosObject = jsonObject.getJSONObject("photos");
                                JSONArray photosArray = photosObject.getJSONArray("photo");

                                for (int i = 0; i < photosArray.length(); i++) {
                                    JSONObject p = (JSONObject) photosArray.get(i);
                                    String photoId = p.getString("id");
                                    String secret = p.getString("secret");
                                    String server = p.getString("server");
                                    int farm = p.getInt("farm");
                                    String title = p.getString("title");
                                    String photoUrl = "https://farm"+farm+".staticflickr.com/"+server+"/"+photoId+"_"+secret+"_n.jpg";

                                    Photo photo = new Photo();
                                    photo.setTitle(title);
                                    photo.setPhotoUrl(photoUrl);

                                    photos.add(photo);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Log.e("Error", error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }
}
