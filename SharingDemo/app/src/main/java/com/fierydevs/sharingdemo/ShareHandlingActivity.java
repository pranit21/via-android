package com.fierydevs.sharingdemo;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class ShareHandlingActivity extends AppCompatActivity {
    private TextView sharedText;
    private ImageView sharedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_handling);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if(Intent.ACTION_SEND.equals(action) && type != null) {
            if("text/plain".equals(type)) {
                handleSendText(intent);
            } else if(type.startsWith("image/")) {
                handleSendImage(intent);
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                //handleSendMultipleImages(intent);
            }
        } else {

        }
    }

    void handleSendText(Intent intent) {
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (text != null) {
            // Update UI to reflect text being shared
            sharedText = (TextView) findViewById(R.id.shared_text);
            sharedText.setText(text);
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
            sharedImage = (ImageView) findViewById(R.id.shared_image);
            try {
                sharedImage.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share_handling, menu);
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
