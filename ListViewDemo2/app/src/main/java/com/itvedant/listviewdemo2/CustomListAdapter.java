package com.itvedant.listviewdemo2;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Pranit on 19-01-2015.
 */
public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
    ImageView imageView;

    public CustomListAdapter(Activity activity, List<Movie> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int position) {
        return movieItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        imageView = (ImageView) convertView.findViewById(R.id.thumbnail);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        TextView genre = (TextView) convertView.findViewById(R.id.genre);
        TextView year = (TextView) convertView.findViewById(R.id.year);

        Movie m = movieItems.get(position);

        try
        {
            // get input stream
            InputStream ins = activity.getAssets().open(m.getThumbnailUrl());
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ins, null);
            // set image to ImageView
            imageView.setImageDrawable(d);
        }
        catch(IOException ex)
        {
            return null;
        }

        title.setText(m.getTitle());

        rating.setText("Rating: " + String.valueOf(m.getRating()));

        String genreStr = "";
        for (String str : m.getGenre()) {
            genreStr += str + ", ";
        }
        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
                genreStr.length() - 2) : genreStr;
        genre.setText(genreStr);

        year.setText(String.valueOf(m.getYear()));

        return convertView;
    }
}
