package com.itvedant.volleydemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.itvedant.volleydemo.models.Anime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    /**
     * The P dialog.
     */
    ProgressDialog pDialog;
    private TextView title, broadcast, synopsis, genre;
    private NetworkImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (TextView) findViewById(R.id.title);
        broadcast = (TextView) findViewById(R.id.broadcast);
        synopsis = (TextView) findViewById(R.id.synopsis);
        genre = (TextView) findViewById(R.id.genre);
        image = (NetworkImageView) findViewById(R.id.image);

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String url = "https://jikan.me/api/anime/30694";
        /*String url = "https://jikan.me/api/anime/6033";
        String url = "https://jikan.me/api/anime/225";
        String url = "https://jikan.me/api/anime/813";
        String url = "https://jikan.me/api/anime/223";
        String url = "https://www.googleapis.com/books/v1/volumes?q=inspirational";*/

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        Log.d(TAG, response);
                        try {
                            parseJson(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // hide the progress dialog
                pDialog.hide();
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, tag_json_obj);
    }

    private void parseJson(JSONObject jsonObject) throws JSONException {
        Anime anime = new Anime();
        anime.setCanonicalLink(jsonObject.getString("link-canonical"));
        anime.setTitle(jsonObject.getString("title"));
        anime.setSynopsis(jsonObject.getString("synopsis"));
        anime.setImage(jsonObject.getString("image"));
        anime.setBroadcast(jsonObject.getString("broadcast"));
        anime.setDuration(jsonObject.getString("duration"));
        anime.setRating(jsonObject.getString("rating"));
        anime.setRanked(jsonObject.getInt("ranked"));

        ArrayList<String> genreList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("genre");
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < jsonArray.length(); i++) {
            genreList.add(jsonArray.getJSONArray(i).getString(1));
            str.append(jsonArray.getJSONArray(i).getString(1)).append(",");
        }
        anime.setGenre(genreList);


        title.setText(jsonObject.getString("title"));
        broadcast.setText(jsonObject.getString("broadcast"));
        synopsis.setText(jsonObject.getString("synopsis"));
        genre.setText(str.substring(0, str.length()-1));
        image.setImageUrl(jsonObject.getString("image"), AppController.getInstance().getImageLoader());
    }
}
