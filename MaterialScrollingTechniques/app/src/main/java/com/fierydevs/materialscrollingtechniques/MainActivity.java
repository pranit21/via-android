package com.fierydevs.materialscrollingtechniques;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Toolbar main_toolbar;
    Button technique1, technique2, technique3, technique4, technique5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(main_toolbar);

        technique1 = (Button) findViewById(R.id.btn_technique_one);
        technique2 = (Button) findViewById(R.id.btn_technique_two);
        technique3 = (Button) findViewById(R.id.btn_technique_three);
        technique4 = (Button) findViewById(R.id.btn_technique_four);
        technique5 = (Button) findViewById(R.id.btn_technique_five);

        technique1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TechniqueOne.class);
                startActivity(intent);
            }
        });

        technique2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TechniqueTwo.class);
                startActivity(intent);
            }
        });

        technique3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TechniqueThree.class);
                startActivity(intent);
            }
        });

        technique4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TechniqueFour.class);
                startActivity(intent);
            }
        });

        technique5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TechniqueFive.class);
                startActivity(intent);
            }
        });
    }
}
