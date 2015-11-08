package com.itvedant.listviewdemo2;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String jsonFile = "movies.json";
    private ProgressDialog progressDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, movieList);
        listView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        // Showing progress dialog before loading json data
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        // changing action bar color
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1b1b1b")));

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray arr = obj.getJSONArray("movies");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject inside = arr.getJSONObject(i);
                Movie movie = new Movie();
                movie.setTitle(inside.getString("title"));
                movie.setThumbnailUrl(inside.getString("image"));
                movie.setRating(((Number) inside.get("rating")).doubleValue());
                movie.setYear(inside.getInt("releaseYear"));

                // Genre is json array
                JSONArray genreArray = inside.getJSONArray("genre");
                ArrayList<String> genre = new ArrayList<String>();
                for (int j = 0; j < genreArray.length(); j++) {
                    genre.add((String) genreArray.get(j));
                }
                movie.setGenre(genre);

                // adding movie to movies array
                movieList.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // notifying list adapter about data changes
        // so that it renders the list view with updated data
        adapter.notifyDataSetChanged();
        hidePDialog();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open(jsonFile);
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void hidePDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
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
