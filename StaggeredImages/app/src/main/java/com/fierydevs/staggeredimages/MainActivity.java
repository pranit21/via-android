package com.fierydevs.staggeredimages;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fierydevs.staggeredimages.adapters.MyAdapter;
import com.fierydevs.staggeredimages.models.Photos;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.android.volley.Request.*;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private MyAdapter adapter;
    private ProgressBar progressBar;
    private static final String FLICKR_API_KEY = "ff2c27b56a8c4af4861d4f4124d7668d";
    private ArrayList<Photos> listData = new ArrayList<Photos>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private int pageNo = 1;

    private FloatingActionButton uploadFromGal;
    private FloatingActionButton cameraCapture;
    private FloatingActionsMenu fabMenu;

    public static final String FILE_UPLOAD_URL = "http://192.168.1.104/AndroidFileUpload/fileUpload.php";
    public static final String IMAGE_DIRECTORY_NAME = "Devaganesha Pics";
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        uploadFromGal = (FloatingActionButton) findViewById(R.id.upload_from_gal);
        uploadFromGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.collapse();
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        cameraCapture = (FloatingActionButton) findViewById(R.id.camera_capture);
        cameraCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.collapse();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // start the image capture Intent
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        MyAdapter.OnItemClickListener onItemClickListener = new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Clicked " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FullScreenPhotoActivity.class);
                intent.putExtra(FullScreenPhotoActivity.EXTRA_PARAM_ID, listData.get(position).getPhotoId());
                intent.putExtra(FullScreenPhotoActivity.EXTRA_PARAM_TITLE, listData.get(position).getTitle());
                intent.putExtra(FullScreenPhotoActivity.EXTRA_PARAM_URL, listData.get(position).getUrl());

                /*ImageView imgView = (ImageView) findViewById(R.id.imgView);
                LinearLayout nameHolder = (LinearLayout) findViewById(R.id.nameHolder);

                Pair<View, String> imagePair = Pair.create((View) imgView, "tImage");
                Pair<View, String> holderPair = Pair.create((View) nameHolder, "tNameHolder");

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imagePair, holderPair);
                ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());*/

                startActivity(intent);
            }
        };

        listData = generateData(listData, true);
        adapter = new MyAdapter(this, listData);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(onItemClickListener);

        swipeRefreshLayout.setOnRefreshListener(this);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        listData = generateData(listData, true);
                                    }
                                }
        );*/
    }

    private ArrayList<Photos> generateData(final ArrayList<Photos> listData, final boolean showPDialog) {
        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key="+FLICKR_API_KEY+"&tags=ganpati%2Cganesha&tag_mode=any&per_page=15&page="+pageNo+"&format=json&nojsoncallback=1";
        swipeRefreshLayout.setRefreshing(true);

        if(showPDialog)
            progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.GET,
                url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("stat");
                            if(status.equals("ok")) {
                                JSONObject photosObj = response.getJSONObject("photos");
                                JSONArray photos = photosObj.getJSONArray("photo");

                                for (int i = 0; i < photos.length(); i++) {
                                    JSONObject photo = (JSONObject) photos.get(i);
                                    String photo_id = photo.getString("id");
                                    String secret = photo.getString("secret");
                                    String server = photo.getString("server");
                                    int farm = photo.getInt("farm");
                                    String title = photo.getString("title");
                                    String photo_url = "https://farm"+farm+".staticflickr.com/"+server+"/"+photo_id+"_"+secret+"_n.jpg";

                                    Photos photoObj = new Photos(photo_id, title, photo_url);
                                    listData.add(photoObj);
                                    pageNo++;
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            if(showPDialog)
                                progressBar.setVisibility(View.GONE);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(showPDialog)
                            progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                    }
        });

        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        return listData;
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

    @Override
    public void onRefresh() {
        listData = generateData(listData, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] fileFromPath = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, fileFromPath,
                        null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(fileFromPath[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                Intent intent = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                intent.putExtra(FullScreenImageActivity.IMAGE_PATH, imgDecodableString);
                intent.putExtra("isImageCaptured", false);
                startActivity(intent);
            } else if(requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    // successfully captured the image
                    // launching upload activity
                    Intent intent = new Intent(MainActivity.this, FullScreenImageActivity.class);
                    intent.putExtra(FullScreenImageActivity.IMAGE_PATH, fileUri.getPath());
                    intent.putExtra("isImageCaptured", true);
                    startActivity(intent);
                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(getApplicationContext(),
                            "User cancelled image capture", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // failed to capture image
                    Toast.makeText(getApplicationContext(),
                            "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                Toast.makeText(this, "You haven't picked an Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }
}
