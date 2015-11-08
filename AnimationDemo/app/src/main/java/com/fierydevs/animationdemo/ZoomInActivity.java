package com.fierydevs.animationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class ZoomInActivity extends AppCompatActivity {
    TextView textView;
    Animation animation;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_in);

        textView = (TextView) findViewById(R.id.zoom_in_text);
        button = (Button) findViewById(R.id.start_zoom_in_animation);

        // load the animation
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setVisibility(View.VISIBLE);

                // start the animation
                textView.startAnimation(animation);
            }
        });
    }
}
