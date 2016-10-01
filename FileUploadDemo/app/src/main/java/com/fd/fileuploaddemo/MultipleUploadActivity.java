package com.fd.fileuploaddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fd.fileuploaddemo.filepicker.FilePickerBuilder;
import com.fd.fileuploaddemo.filepicker.FilePickerConst;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.gotev.uploadservice.BinaryUploadRequest;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.fd.fileuploaddemo.R.id.overlay;

public class MultipleUploadActivity extends AppCompatActivity {
    private static final String TAG = "MultipleUploadService";
    private ArrayList<String> filePaths;
    private ViewGroup container;

    private static final String USER_AGENT = "MultipleUploadService/" + BuildConfig.VERSION_NAME;
    private Button btnUpload, btnCancelAll;

    private StaggeredGridLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;
    private List<UpPhoto> photos;

    private void logSuccessfullyUploadedFiles(List<String> files) {
        for (String file : files) {
            Log.e(TAG, "Success:" + file);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        container = (ViewGroup) findViewById(R.id.container);

        btnUpload = (Button) findViewById(R.id.upload);
        btnCancelAll = (Button) findViewById(R.id.cancel_all_upload);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        photos = new ArrayList<>();
        photoAdapter = new PhotoAdapter(this, photos);

        recyclerView.setAdapter(photoAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePickerBuilder.getInstance().setMaxCount(5)
                        //.setSelectedFiles(filePaths)
                        .setActivityTheme(R.style.AppTheme)
                        .pickPhoto(MultipleUploadActivity.this);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://192.168.0.111/AndroidFileUpload/fileUpload.php";
                photoAdapter.startUploading(url);
                btnCancelAll.setEnabled(true);
                btnUpload.setEnabled(false);
            }
        });

        btnCancelAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadService.stopAllUploads();
                btnCancelAll.setEnabled(false);
                btnUpload.setEnabled(false);
            }
        });
    }

    private String getFilename(String filepath) {
        if (filepath == null)
            return null;

        final String[] filepathParts = filepath.split("/");

        return filepathParts[filepathParts.length - 1];
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FilePickerConst.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            filePaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);
            Log.e("filePaths", filePaths.toString());
            if (!filePaths.isEmpty()) {
                //ImageLoader.getInstance().displayImage("file:///" + filePaths.get(0), imageView);
                for (String file : filePaths) {
                    UpPhoto photo = new UpPhoto();
                    photo.setTitle(getFilename(file));
                    photo.setUrl(file);
                    //photo.setUploadId();

                    photos.add(photo);
                }
                photoAdapter.notifyDataSetChanged();
                btnUpload.setEnabled(true);
            }
        }
    }
}
