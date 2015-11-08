package com.fierydevs.staggeredimages;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.util.List;

public class FullScreenPhotoActivity extends AppCompatActivity {
    public static final String EXTRA_PARAM_ID = "photo_id";
    public static final String EXTRA_PARAM_TITLE = "title";
    public static final String EXTRA_PARAM_URL = "url";

    private Toolbar toolbar;
    private TextView full_screen_title;
    private TextView description;
    private ImageView full_screen_img;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_photo);

        toolbar = (Toolbar) findViewById(R.id.full_screen_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String photo_id = intent.getStringExtra(EXTRA_PARAM_ID);
        String title = intent.getStringExtra(EXTRA_PARAM_TITLE);
        String url = intent.getStringExtra(EXTRA_PARAM_URL);

        /*full_screen_title = (TextView) findViewById(R.id.full_screen_title);
        full_screen_title.setText(title);*/
        description = (TextView) findViewById(R.id.description);
        description.setText("That’s the core part of this article. First, the collapsing layout specify how it will behave when the content is scrolled using the flags scroll|exitUntilCollapsed, so it will scroll until it’s completely collapsed. Then we specify the contentScrim, which is the color the toolbar will take when it reaches it’s collapsed state. I’ll be changing this programmatically and use palette to decide its color. We can also specify the margins for the title when it’s expanded. It will create a nice effect over the toolbar title. You can define some other things, such as the statusScrim or the textAppearance for the collapsed and expanded title.With some simple steps, now we have an amazingly animated detail screen that includes most of the things we’ve seen in Material Design specs. We can customize this in many ways really easily, and even manage some more complex effects by creating custom behaviors, which is what we’ll see in next article. You can subscribe to the blog feed to stay tuned.I’m also tinting the toolbar and the FAB using Palette. I won’t get into details about this, but you can [check the code] (https://github.com/antoniolg/MaterializeYourApp/blob/master/app/src/main/java/com/antonioleiva/materializeyourapp/DetailActivity.java) for more details:We also need to set the Collapsing Toolbar Layout title programmatically. I also set the expanded color for the title to transparent, because it was interfering with the transition. Besides, it creates a nice alpha effect:Now everything should be working, but I added some code that will tint the toolbar using palette, or will help with material transitions.\n" +
                "An important thing to know about the latter point is that we cannot use the image as the transition target. For some reason it won’t work. After trying some different options I found that the one that works best is targeting the AppBarLayout:");

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        full_screen_img = (ImageView) findViewById(R.id.full_screen_img);

        /*List<Bitmap> bm = MemoryCacheUtils.findCachedBitmapsForImageUri(url, ImageLoader.getInstance().getMemoryCache());
        if(bm.size() > 0) {
            Drawable d = new BitmapDrawable(getResources(), bm.get(0));
        }

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(1000))
                .build();

        if (bm.size() > 0) {
            options.showImageOnLoading(d);
        }*/

        ImageLoader.getInstance().displayImage(url, full_screen_img, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Palette.generateAsync(loadedImage, new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
                        int primary = getResources().getColor(R.color.colorPrimary);
                        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
                        collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(primaryDark));
                        supportStartPostponedEnterTransition();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_screen_photo, menu);
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
}
