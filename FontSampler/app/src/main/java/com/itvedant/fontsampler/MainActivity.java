package com.itvedant.fontsampler;

import android.graphics.Typeface;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.custom);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/HandmadeTypewriter.ttf");

        textView.setTypeface(typeface);

        File font = new File(Environment.getExternalStorageDirectory(), "MgOpenCosmeticaBold.ttf");

        if(font.exists()) {
            textView = (TextView) findViewById(R.id.fromFile);
            typeface = Typeface.createFromFile(font);

            textView.setTypeface(typeface);
        } else {
            findViewById(R.id.fileRow).setVisibility(View.GONE);
        }
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
