package com.fierydevs.materialstartdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fierydevs.materialstartdemo.adapters.PhotoAdapter;
import com.fierydevs.materialstartdemo.models.Photos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    // Flicker api key to fetch images
    private static final String FLICKR_API_KEY = "ff2c27b56a8c4af4861d4f4124d7668d";

    private ProgressDialog progressDialog;

    private List<Photos> photos;
    private PhotoAdapter photoAdapter;

    private RecyclerView recyclerView;

    // LinearLayout Manager for showing content in horizontal or vertical layout
    //private RecyclerView.LayoutManager layoutManager;

    // StaggeredGridLayout to show content in linear or grid manner
    private StaggeredGridLayoutManager layoutManager;

    private boolean isGrid = false;

    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add toolbar as an actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        progressDialog = new ProgressDialog(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        // layoutManager = new LinearLayoutManager(this);

        // use staggered grid layout manager
        // Here spanCount is specified as 1, so it will look like linear layout
        // If spanCount is specified more than 1 it will look like grid
        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        photos = new ArrayList<Photos>();
        photoAdapter = new PhotoAdapter(getApplicationContext(), photos);
        recyclerView.setAdapter(photoAdapter);

        // Add onItemClickListener that is defined by us in PhotoAdapter class
        // as RecyclerView don't have it's own onItemClickListener
        photoAdapter.setOnItemClickListener(new PhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "Clicked " + position, Toast.LENGTH_SHORT).show();

                Photos p = photos.get(position);

                Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                intent.putExtra(DisplayActivity.EXTRA_PARAM_IMG_URL, p.getUrl());
                intent.putExtra(DisplayActivity.EXTRA_PARAM_TITLE, p.getTitle());
                //startActivity(intent);

                ImageView photoView = (ImageView) view.findViewById(R.id.image_view);

                Pair<View, String> imagePair = Pair.create((View) photoView, "tImage");

                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imagePair);
                ActivityCompat.startActivity(MainActivity.this, intent, optionsCompat.toBundle());
            }
        });

        getPhotos();
    }

    /*
     * Get photos from flicker api and adds it to in PhotoAdapter.
     */
    private void getPhotos() {
        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key="+FLICKR_API_KEY+"&per_page=50&page=1&format=json&nojsoncallback=1";

        progressDialog.setMessage("Fetching photos...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("stat");
                            if (status.equals("ok")) {
                                JSONObject photosObject = jsonObject.getJSONObject("photos");
                                JSONArray photo = photosObject.getJSONArray("photo");

                                for (int i = 0; i < photo.length(); i++) {
                                    JSONObject photoObject = (JSONObject) photo.get(i);
                                    String photoId = photoObject.getString("id");
                                    String secret = photoObject.getString("secret");
                                    String server = photoObject.getString("server");
                                    int farm = photoObject.getInt("farm");
                                    String title = photoObject.getString("title");
                                    title = (title.length() > 10) ? (title.substring(0, 10) + "...") : title;
                                    String photoUrl = "https://farm"+farm+".staticflickr.com/"+server+"/"+photoId+"_"+secret+"_c.jpg";

                                    Photos photoObj = new Photos(photoId, title, photoUrl);
                                    photos.add(photoObj);
                                }
                            }
                            photoAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
            }
        });

        MainApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menuItem = menu.findItem(R.id.action_change_orientation);

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
        } else if (id == R.id.action_change_orientation) {
            // Change orientation from linear to grid and grid back to linear
            if (isGrid) {
                layoutManager.setSpanCount(1);
                isGrid = false;
                menuItem.setIcon(R.drawable.ic_action_grid);
            } else {
                layoutManager.setSpanCount(2);
                isGrid = true;
                menuItem.setIcon(R.drawable.ic_action_list);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
