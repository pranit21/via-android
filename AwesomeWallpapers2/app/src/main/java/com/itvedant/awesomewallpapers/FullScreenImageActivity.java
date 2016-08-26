package com.itvedant.awesomewallpapers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.itvedant.awesomewallpapers.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import static android.view.View.GONE;

public class FullScreenImageActivity extends AppCompatActivity {
    private ImageView imageView;
    private LinearLayout setWallpaper;
    private ProgressBar progressBar;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        imageView = (ImageView) findViewById(R.id.fullScreenImage);
        setWallpaper = (LinearLayout) findViewById(R.id.setWallpaper);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLoader);

        utils = new Utils(this);

        Intent intent = getIntent();
        String photoUrl = intent.getStringExtra(Constants.PHOTO_URL);
        photoUrl = photoUrl.replace("_n", "_b");

        ImageLoader.getInstance().displayImage(photoUrl, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
                setWallpaper.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
                setWallpaper.setVisibility(View.VISIBLE);
                adjustImageAspect(Constants.IMAGE_WIDTH, Constants.IMAGE_HEIGHT);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.GONE);
            }
        });

        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                utils.setAsWallpaper(bitmap);
            }
        });
    }

    private void adjustImageAspect(int imageWidth, int imageHeight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.MATCH_PARENT);

        int sHeight = 0;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        sHeight = size.y;

        int newWidth = (int) Math.floor((double) imageWidth * (double) sHeight
                / (double) imageHeight);

        params.width = newWidth;
        params.height = sHeight;

        Log.e("Dimensions", "Fullscreen image new dimensions: w = " + newWidth
                + ", h = " + sHeight);

        imageView.setLayoutParams(params);
    }
}
