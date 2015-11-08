package com.itvedant.imagegallery.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.itvedant.imagegallery.R;
import com.itvedant.imagegallery.helper.Utils;

import java.util.ArrayList;

/**
 * Created by Pranit on 05-07-2015.
 */
public class FullScreenImageAdapter extends PagerAdapter {
    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;
    private Utils utils;
    Bitmap bitmap;

    public FullScreenImageAdapter(Activity _activity, ArrayList<String> _imagePaths) {
        this._activity = _activity;
        this._imagePaths = _imagePaths;
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;
        LinearLayout btnClose, btnSetWallpaper;

        inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container, false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        btnClose = (LinearLayout) viewLayout.findViewById(R.id.btnClose);
        btnSetWallpaper = (LinearLayout) viewLayout.findViewById(R.id.btnSetWallpaper);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = BitmapFactory.decodeFile(_imagePaths.get(position), options);
        imgDisplay.setImageBitmap(bitmap);

        utils = new Utils(_activity);

        // layout click listeners
        btnSetWallpaper.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                utils.setAsWallpaper(bitmap);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                _activity.finish();
            }
        });

        // setting layout buttons alpha/opacity
        btnSetWallpaper.getBackground().setAlpha(70);
        btnClose.getBackground().setAlpha(70);

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
