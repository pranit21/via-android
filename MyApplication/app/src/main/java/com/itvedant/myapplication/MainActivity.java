package com.itvedant.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText enterText;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterText = (EditText) findViewById(R.id.enterText);
        btnSend = (Button) findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = enterText.getText().toString();
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                /*Toast toast = Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();*/
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("str_key", str);
                startActivity(intent);
            }
        });
    }
}
