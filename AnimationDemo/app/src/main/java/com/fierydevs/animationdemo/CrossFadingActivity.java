package com.fierydevs.animationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CrossFadingActivity extends AppCompatActivity {
    TextView textView1, textView2;
    Animation animationFadeIn, animationFadeOut;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cross_fading);

        textView1 = (TextView) findViewById(R.id.cross_fade_in_text);
        textView2 = (TextView) findViewById(R.id.cross_fade_out_text);
        button = (Button) findViewById(R.id.start_cross_fading_animation);

        // load the animation
        animationFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animationFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setVisibility(View.VISIBLE);

                // start the animation
                textView2.startAnimation(animationFadeOut);
                textView1.startAnimation(animationFadeIn);
            }
        });
    }
}
