package com.fierydevs.fragmentdemo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Pranit on 31-10-2015.
 */
public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_URL = "url";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if dual pane mode is active
        // if yes, finish this activity
        if (getResources().getBoolean(R.bool.dual_pane)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_detail);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String url = extras.getString(EXTRA_URL);
            DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.detailFragment);
            detailFragment.setText(url);
        }
    }
}
