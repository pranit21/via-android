package com.fierydevs.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {
    public final static String ENTERED_TEXT = "entered_text";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Intent intent = getIntent();
        String str = intent.getStringExtra(ENTERED_TEXT);

        textView = (TextView) findViewById(R.id.displayText);
        textView.setText(str);
    }
}
