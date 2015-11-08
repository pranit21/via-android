package com.fierydevs.animationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class SequentialActivity extends AppCompatActivity {
    TextView textView;
    Animation animation;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequential);

        textView = (TextView) findViewById(R.id.sequential_text);
        button = (Button) findViewById(R.id.start_sequential_animation);

        // load the animation
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.sequential);

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
