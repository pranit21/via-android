package com.itvedant.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    private TextView txtSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        txtSent = (TextView) findViewById(R.id.txtSent);

        if (getIntent() != null) {
            Intent intent = getIntent();
            String str = intent.getStringExtra("str_key");

            txtSent.setText(str);
        }
    }
}
