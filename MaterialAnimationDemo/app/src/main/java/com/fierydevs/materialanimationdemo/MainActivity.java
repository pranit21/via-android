package com.fierydevs.materialanimationdemo;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.textView);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float x1 = fab.getX();
                float y1 = fab.getY();

                float x3 = textView.getX() + 100;
                float y3 = textView.getY() + 100;

                Path path = new Path();
                path.moveTo(x1, y1);

                float x2 = (x3 + x1) / 2;
                float y2 = y1;
                path.addArc(x1, y1, 500, 500, 0, 90);
                path.cubicTo(x1, y1, x2, y2, x3, y3);


                /*Path path = new Path();
                path.moveTo(0, 0);
                path.lineTo(0, 300);
                path.quadTo(100, 0, 300, 400);*/

                ObjectAnimator anim = ObjectAnimator.ofFloat(fab, View.X, View.Y, path);
                anim.start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
