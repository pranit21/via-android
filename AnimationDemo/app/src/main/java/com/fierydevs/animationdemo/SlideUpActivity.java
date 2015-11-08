package com.fierydevs.animationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class SlideUpActivity extends AppCompatActivity {
    TextView textView;
    Animation animation;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_up);

        textView = (TextView) findViewById(R.id.slide_up_text);
        button = (Button) findViewById(R.id.start_slide_up_animation);

        // load the animation
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);

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
