package com.fierydevs.customgridview;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fierydevs.customgridview.adapters.PhotoAdapter;
import com.fierydevs.customgridview.app.AppConstant;
import com.fierydevs.customgridview.app.Utils;
import com.fierydevs.customgridview.models.Photos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FLICKR_API_KEY = "ff2c27b56a8c4af4861d4f4124d7668d";
    private ProgressDialog progressDialog;
    private List<Photos> photos;
    private GridView gridView;
    private PhotoAdapter photoAdapter;
    private int columnWidth;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);

        gridView = (GridView) findViewById(R.id.grid_view);
        utils = new Utils(this);

        //initializeGridLayout();

        photos = new ArrayList<Photos>();
        photoAdapter = new PhotoAdapter(getApplicationContext(), photos);
        gridView.setAdapter(photoAdapter);

        getPhotos();
    }

    private void getPhotos() {
        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key="+FLICKR_API_KEY+"&per_page=100&page=1&format=json&nojsoncallback=1";

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
                                String photoUrl = "https://farm"+farm+".staticflickr.com/"+server+"/"+photoId+"_"+secret+"_q.jpg";

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

        AppController.getInstance().addToRequestQueue(request);
    }

    /**
     * Method to calculate the grid dimensions Calculates number columns and
     * columns width in grid
     * */
    private void initializeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());

        // Column width
        columnWidth = (int) ((utils.getScreenWidth() - ((
                AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

        // Setting number of grid columns
        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);

        // Setting horizontal and vertical padding
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }
}
