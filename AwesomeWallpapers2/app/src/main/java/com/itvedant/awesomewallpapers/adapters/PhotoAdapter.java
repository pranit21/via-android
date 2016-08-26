package com.itvedant.awesomewallpapers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itvedant.awesomewallpapers.R;
import com.itvedant.awesomewallpapers.models.Photo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Pranit on 26-08-2016.
 */

public class PhotoAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private ArrayList<Photo> photos;
    private int imageWidth;

    public PhotoAdapter(Context context, ArrayList<Photo> photos, int imageWidth) {
        this.context = context;
        this.photos = photos;
        this.imageWidth = imageWidth;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int i) {
        return photos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = layoutInflater.inflate(R.layout.grid_item, null);

        TextView title = (TextView) view.findViewById(R.id.textView);
        ImageView photoImage = (ImageView) view.findViewById(R.id.imageView);
        photoImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        photoImage.setLayoutParams(new RelativeLayout.LayoutParams(imageWidth, imageWidth));

        Photo photo = photos.get(i);

        String t = photo.getTitle();
        if (!t.isEmpty() && t.length() >= 10)
            t = t.substring(0, 10);

        title.setText(t);
        ImageLoader.getInstance().displayImage(photo.getPhotoUrl(), photoImage);

        return view;
    }
}
