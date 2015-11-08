package com.fierydevs.staggeredimages;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexbbb.uploadservice.AbstractUploadServiceReceiver;
import com.alexbbb.uploadservice.UploadRequest;
import com.alexbbb.uploadservice.UploadService;

public class FullScreenImageActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar toolbar;
    public static final String IMAGE_PATH = "img_path";

    private ImageView img_to_upload;
    private TextInputLayout titleLayout, locationLayout, descriptionLayout;
    private EditText title, location, description;

    private ProgressBar progressBar;
    private TextView txtPercentage;
    String img_path = null;
    long totalSize = 0;

    private final AbstractUploadServiceReceiver uploadReceiver = new AbstractUploadServiceReceiver() {

        @Override
        public void onProgress(String uploadId, int progress) {
            txtPercentage.setText(progress+"%");
            progressBar.setProgress(progress);

            Log.i(TAG, "The progress of the upload with ID " + uploadId + " is: " + progress);
        }

        @Override
        public void onError(String uploadId, Exception exception) {
            txtPercentage.setText("Error");
            progressBar.setProgress(0);

            String message = "Error in upload with ID: " + uploadId + ". " + exception.getLocalizedMessage();
            Log.e(TAG, message, exception);
        }

        @Override
        public void onCompleted(String uploadId, int serverResponseCode, String serverResponseMessage) {
            txtPercentage.setText("100%");
            progressBar.setProgress(100);

            String message = "Upload with ID " + uploadId + " is completed: " + serverResponseCode + ", "
                    + serverResponseMessage;
            Log.i(TAG, message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        UploadService.NAMESPACE = "com.fierydevs.staggeredimages";

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();
        img_path = intent.getStringExtra(IMAGE_PATH);
        boolean isImageCaptured = intent.getBooleanExtra("isImageCaptured", false);

        img_to_upload = (ImageView) findViewById(R.id.img_to_upload);
        BitmapFactory.Options options = new BitmapFactory.Options();
        // down sizing image as it throws OutOfMemory Exception for larger images
        options.inSampleSize = 8;
        img_to_upload.setImageBitmap(BitmapFactory.decodeFile(img_path, options));

        titleLayout = (TextInputLayout) findViewById(R.id.title_layout);
        locationLayout = (TextInputLayout) findViewById(R.id.location_layout);
        descriptionLayout = (TextInputLayout) findViewById(R.id.description_layout);

        title = (EditText) findViewById(R.id.title);
        location = (EditText) findViewById(R.id.location);
        description = (EditText) findViewById(R.id.description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_screen_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_publish) {
            boolean errorFlag = false;
            if(title.getText().toString().length() == 0) {
                title.setError("Required");
                titleLayout.setErrorEnabled(true);
                titleLayout.setError("Title is compulsory");
                errorFlag = true;
            } else {
                title.setError(null);
                titleLayout.setErrorEnabled(false);
                titleLayout.setError(null);
            }

            if(location.getText().toString().length() == 0) {
                location.setError("Required");
                locationLayout.setErrorEnabled(true);
                locationLayout.setError("Location is compulsory");
                errorFlag = true;
            } else {
                location.setError(null);
                locationLayout.setErrorEnabled(false);
                locationLayout.setError(null);
            }

            if(description.getText().toString().length() == 0) {
                description.setError("Required");
                descriptionLayout.setErrorEnabled(true);
                descriptionLayout.setError("Description is compulsory");
                errorFlag = true;
            } else {
                description.setError(null);
                descriptionLayout.setErrorEnabled(false);
                descriptionLayout.setError(null);
            }

            if(errorFlag) {
                return false;
            } else {
                upload(getApplicationContext());
                return true;
            }
        } else if (id == R.id.action_discard) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void upload(final Context applicationContext) {
        final UploadRequest request = new UploadRequest(applicationContext,
                "image_to_upload",
                "http://192.168.1.104/AndroidFileUpload/fileUpload.php");

        request.addFileToUpload(img_path, "image", "img1.jpg", "image/jpg");
        request.setMaxRetries(2);

        try {
            progressBar.setVisibility(View.VISIBLE);
            txtPercentage.setVisibility(View.VISIBLE);
            //Start upload service and display the notification
            UploadService.startUpload(request);

        } catch (Exception exc) {
            //You will end up here only if you pass an incomplete UploadRequest
            Log.e("AndroidUploadService", exc.getLocalizedMessage(), exc);
        }
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
