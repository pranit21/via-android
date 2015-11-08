package com.fierydevs.photouploadtoserver;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexbbb.uploadservice.AbstractUploadServiceReceiver;
import com.alexbbb.uploadservice.UploadRequest;
import com.alexbbb.uploadservice.UploadService;

public class MainActivity extends AppCompatActivity {
    private Button send;
    private ImageView imageView;
    private TextView percentage;
    private ProgressBar progressBar;
    private static final String TAG = "UploadServiceDemo";

    private final AbstractUploadServiceReceiver uploadReceiver = new AbstractUploadServiceReceiver() {

        @Override
        public void onProgress(String uploadId, int progress) {
            percentage.setText(progress+"%");
            progressBar.setProgress(progress);

            Log.i(TAG, "The progress of the upload with ID " + uploadId + " is: " + progress);
        }

        @Override
        public void onError(String uploadId, Exception exception) {
            percentage.setText("Error");
            progressBar.setProgress(0);

            String message = "Error in upload with ID: " + uploadId + ". " + exception.getLocalizedMessage();
            Log.e(TAG, message, exception);
        }

        @Override
        public void onCompleted(String uploadId, int serverResponseCode, String serverResponseMessage) {
            percentage.setText("100%");
            progressBar.setProgress(0);

            String message = "Upload with ID " + uploadId + " is completed: " + serverResponseCode + ", "
                    + serverResponseMessage;
            Log.i(TAG, message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UploadService.NAMESPACE = "com.fierydevs.photouploadtoserver";

        send = (Button) findViewById(R.id.send);
        imageView = (ImageView) findViewById(R.id.img_to_upload);
        percentage = (TextView) findViewById(R.id.txt_perc);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        percentage.setText("0%");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload(getApplicationContext(), "IMG_20150623_181803.jpg");
                /*upload(getApplicationContext(), "IMG_20150623_192111.jpg");
                upload(getApplicationContext(), "IMG_20150623_192445.jpg");
                upload(getApplicationContext(), "IMG_20150623_192518.jpg");
                upload(getApplicationContext(), "IMG_20150624_174246.jpg");
                upload(getApplicationContext(), "IMG_20150624_174250.jpg");
                upload(getApplicationContext(), "IMG_20150624_174338.jpg");
                upload(getApplicationContext(), "IMG_20150624_174342.jpg");*/
            }
        });

        progressBar.setMax(100);
        progressBar.setProgress(0);
    }

    private void upload(final Context applicationContext, String img_name) {
        final UploadRequest request = new UploadRequest(applicationContext,
                "image_to_upload",
                "http://192.168.1.104/AndroidFileUpload/fileUpload.php");

        request.addFileToUpload(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/"+img_name, "image", img_name, "image/jpg");
        request.setMaxRetries(2);

        try {
            //Start upload service and display the notification
            UploadService.startUpload(request);

        } catch (Exception exc) {
            //You will end up here only if you pass an incomplete UploadRequest
            Log.e("AndroidUploadService", exc.getLocalizedMessage(), exc);
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

    private void onCancelUploadButtonClick() {
        UploadService.stopCurrentUpload();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uploadReceiver.unregister(this);
    }
}
