package com.fierydevs.animationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FadeInActivity extends AppCompatActivity implements Animation.AnimationListener {
    TextView textView;
    Animation animation;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fade_in);

        textView = (TextView) findViewById(R.id.fade_in_text);
        button = (Button) findViewById(R.id.start_fade_in_animation);

        // load the animation
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        animation.setAnimationListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setVisibility(View.VISIBLE);

                // start the animation
                textView.startAnimation(animation);
            }
        });
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == this.animation) {
            Toast.makeText(getApplicationContext(), "Animation Stopped",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
