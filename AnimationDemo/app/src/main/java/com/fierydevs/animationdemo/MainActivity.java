package com.fierydevs.animationdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button fade_in, fade_out, cross_fading, blink, zoom_in, zoom_out,
        rotate, move, slide_up, slide_down, bounce, sequential, together;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fade_in = (Button) findViewById(R.id.fade_in);
        fade_out = (Button) findViewById(R.id.fade_out);
        cross_fading = (Button) findViewById(R.id.cross_fading);
        blink = (Button) findViewById(R.id.blink);
        zoom_in = (Button) findViewById(R.id.zoom_in);
        zoom_out = (Button) findViewById(R.id.zoom_out);
        rotate = (Button) findViewById(R.id.rotate);
        move = (Button) findViewById(R.id.move);
        slide_up = (Button) findViewById(R.id.slide_up);
        slide_down = (Button) findViewById(R.id.slide_down);
        bounce = (Button) findViewById(R.id.bounce);
        sequential = (Button) findViewById(R.id.sequential_animation);
        together = (Button) findViewById(R.id.together_animation);

        fade_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FadeInActivity.class);
                startActivity(i);
            }
        });

        fade_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FadeOutActivity.class);
                startActivity(i);
            }
        });

        cross_fading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CrossFadingActivity.class);
                startActivity(i);
            }
        });

        blink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BlinkActivity.class);
                startActivity(i);
            }
        });

        zoom_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ZoomInActivity.class);
                startActivity(i);
            }
        });

        zoom_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ZoomOutActivity.class);
                startActivity(i);
            }
        });

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RotateActivity.class);
                startActivity(i);
            }
        });

        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MoveActivity.class);
                startActivity(i);
            }
        });

        slide_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SlideUpActivity.class);
                startActivity(i);
            }
        });

        slide_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SlideDownActivity.class);
                startActivity(i);
            }
        });

        bounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BounceActivity.class);
                startActivity(i);
            }
        });

        sequential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SequentialActivity.class);
                startActivity(i);
            }
        });

        together.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TogetherActivity.class);
                startActivity(i);
            }
        });
    }
}
