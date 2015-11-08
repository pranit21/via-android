package com.itvedant.gridviewdemo1;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Pranit on 21-01-2015.
 */
public class SingleImageActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_view);

        Intent intent = getIntent();

        final int position = intent.getExtras().getInt("id");
        final ImageAdapter imageAdapter = new ImageAdapter(this);

        ImageView imageView = (ImageView) findViewById(R.id.SingleView);
        imageView.setImageResource(imageAdapter.mThumbIds[position]);

        Button btnSetWallpaper = (Button) findViewById(R.id.set_wallpaper);
        btnSetWallpaper.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                try {
                    wallpaperManager.setResource(imageAdapter.mThumbIds[position]);
                    Toast.makeText(getApplicationContext(), "Wallpaper set successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
