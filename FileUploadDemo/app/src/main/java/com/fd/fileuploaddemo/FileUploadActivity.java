package com.fd.fileuploaddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import static android.media.CamcorderProfile.get;

public class FileUploadActivity extends AppCompatActivity implements UploadStatusDelegate {
    private static final String TAG = "FileUploadServiceDemo";
    private ArrayList<String> filePaths;
    private ImageView imageView;
    private ProgressBar progressBar;
    private View overlay;
    private TextView title;

    private static final String USER_AGENT = "FileUploadServiceDemo/" + BuildConfig.VERSION_NAME;
    private Button btnUpload, btnCancelUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = (ImageView) findViewById(R.id.image_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        overlay = findViewById(R.id.overlay);
        title = (TextView) findViewById(R.id.title);

        btnUpload = (Button) findViewById(R.id.upload);
        btnCancelUpload = (Button) findViewById(R.id.cancel_upload);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilePickerBuilder.getInstance().setMaxCount(1)
                        //.setSelectedFiles(filePaths)
                        .setActivityTheme(R.style.AppTheme)
                        .pickDocument(FileUploadActivity.this);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMultipartUploadClick();
            }
        });
    }

    private void onMultipartUploadClick() {
        String url = "http://192.168.0.111/AndroidFileUpload/fileUpload.php";

        for (String path : filePaths) {
            try {
                final String filename = getFilename(path);

                MultipartUploadRequest request = new MultipartUploadRequest(this, url)
                        .addFileToUpload(path, "uploaded_file")
                        .setNotificationConfig(getNotificationConfig(filename))
                        .setCustomUserAgent(USER_AGENT)
                        //.setAutoDeleteFilesAfterSuccessfulUpload(true)
                        //.setUsesFixedLengthStreamingMode(true)
                        .setMaxRetries(3);

                //request.setUtf8Charset();

                progressBar.setMax(100);
                progressBar.setProgress(0);

                String uploadID = request.setDelegate(this).startUpload();

                displayProgress(uploadID, filename);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    void onUploadBinaryClick() {
        String url = "http://192.168.0.111/AndroidFileUpload/fileUpload.php";

        for (String path : filePaths) {
            try {
                final String filename = getFilename(path);

                progressBar.setMax(100);
                progressBar.setProgress(0);

                final String uploadID = new BinaryUploadRequest(this, url)
                        .addHeader("uploaded_file", new File(path).getName())
                        .setFileToUpload(path)
                        .setNotificationConfig(getNotificationConfig(filename))
                        .setCustomUserAgent(USER_AGENT)
                        //.setAutoDeleteFilesAfterSuccessfulUpload(true)
                        //.setUsesFixedLengthStreamingMode(true)
                        .setMaxRetries(2)
                        .setDelegate(this)
                        .startUpload();

                displayProgress(uploadID, filename);

                // these are the different exceptions that may be thrown
            } catch (FileNotFoundException exc) {
                //showToast(exc.getMessage());
            } catch (IllegalArgumentException exc) {
                //showToast("Missing some arguments. " + exc.getMessage());
            } catch (MalformedURLException exc) {
                //showToast(exc.getMessage());
            }
        }
    }

    private void displayProgress(final String uploadID, String filename) {
        progressBar.setVisibility(View.VISIBLE);
        overlay.setVisibility(View.VISIBLE);
        title.setText(filename);
        btnCancelUpload.setEnabled(true);
        btnUpload.setEnabled(false);

        btnCancelUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uploadID == null)
                    return;

                UploadService.stopUpload(uploadID);
                btnCancelUpload.setEnabled(false);
                btnUpload.setEnabled(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FilePickerConst.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            filePaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);
            Log.e("filePaths", filePaths.toString());
            if (!filePaths.isEmpty()) {
                String filename = getFilename(filePaths.get(0));
                String extension = filename.substring(filename.lastIndexOf(".") + 1);
                ImageLoader.getInstance().displayImage("drawable://" + getDocImage(extension), imageView);
                btnUpload.setEnabled(true);
            }
        }
    }

    private int getDocImage(String extension) {
        if (extension.equalsIgnoreCase("PDF")) {
            return R.drawable.ic_pdf;
        } else if (extension.equalsIgnoreCase("DOC") || extension.equalsIgnoreCase("DOCX")) {
            return R.drawable.ic_doc;
        } else if (extension.equalsIgnoreCase("XLS") || extension.equalsIgnoreCase("XLSX")) {
            return R.drawable.ic_xls;
        } else if (extension.equalsIgnoreCase("PPT") || extension.equalsIgnoreCase("PPTX")) {
            return R.drawable.icon_ppt;
        } else {
            return R.drawable.ic_txt;
        }
    }

    private String getFilename(String filepath) {
        if (filepath == null)
            return null;

        final String[] filepathParts = filepath.split("/");

        return filepathParts[filepathParts.length - 1];
    }

    private UploadNotificationConfig getNotificationConfig(String fileName) {
        return new UploadNotificationConfig()
                .setIcon(R.drawable.ic_upload)
                .setCompletedIcon(R.drawable.ic_upload_success)
                .setErrorIcon(R.drawable.ic_upload_error)
                .setTitle(fileName)
                .setInProgressMessage(getString(R.string.uploading))
                .setCompletedMessage(getString(R.string.upload_success))
                .setErrorMessage(getString(R.string.upload_failed))
                .setAutoClearOnSuccess(true)
                .setClickIntent(new Intent(this, SingleUploadActivity.class))
                .setClearOnAction(true)
                .setRingToneEnabled(true);
    }

    @Override
    public void onProgress(UploadInfo uploadInfo) {
        Log.i(TAG, String.format(Locale.getDefault(), "ID: %1$s (%2$d%%) at %3$.2f Kbit/s",
                uploadInfo.getUploadId(), uploadInfo.getProgressPercent(),
                uploadInfo.getUploadRate()));

        progressBar.setProgress(uploadInfo.getProgressPercent());
    }

    @Override
    public void onError(UploadInfo uploadInfo, Exception exception) {
        Log.e(TAG, "Error with ID: " + uploadInfo.getUploadId() + ": "
                + exception.getLocalizedMessage(), exception);

        progressBar.setVisibility(View.GONE);
        overlay.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        btnCancelUpload.setEnabled(false);
        btnUpload.setEnabled(true);
    }

    @Override
    public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
        Log.i(TAG, String.format(Locale.getDefault(),
                "ID %1$s: completed in %2$ds at %3$.2f Kbit/s. Response code: %4$d, body:[%5$s]",
                uploadInfo.getUploadId(), uploadInfo.getElapsedTime() / 1000,
                uploadInfo.getUploadRate(), serverResponse.getHttpCode(),
                serverResponse.getBodyAsString()));

        for (Map.Entry<String, String> header : serverResponse.getHeaders().entrySet()) {
            Log.i("Header", header.getKey() + ": " + header.getValue());
        }

        Log.e(TAG, "Printing response body bytes");
        byte[] ba = serverResponse.getBody();
        for (byte aBa : ba) {
            Log.e(TAG, String.format("%02X ", aBa));
        }

        progressBar.setProgress(100);
        progressBar.setVisibility(View.GONE);
        overlay.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        btnCancelUpload.setEnabled(false);
        btnUpload.setEnabled(true);
    }

    @Override
    public void onCancelled(UploadInfo uploadInfo) {
        Log.i(TAG, "Upload with ID " + uploadInfo.getUploadId() + " is cancelled");

        progressBar.setVisibility(View.GONE);
        overlay.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        btnCancelUpload.setEnabled(false);
        btnUpload.setEnabled(true);
    }
}
