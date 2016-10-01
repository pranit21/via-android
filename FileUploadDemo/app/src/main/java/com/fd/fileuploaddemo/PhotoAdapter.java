package com.fd.fileuploaddemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Pranit on 15-09-2016.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> implements UploadStatusDelegate {
    private static final String TAG = "PhotoAdapter";

    private Context context;
    private List<UpPhoto> photos;
    private static final String USER_AGENT = "PhotoAdapter/" + BuildConfig.VERSION_NAME;

    private Map<String, PhotoAdapter.ViewHolder> uploadProgressHolders = new HashMap<>();

    private List<PhotoAdapter.ViewHolder> viewHolders = new ArrayList<>();

    public PhotoAdapter(Context context, List<UpPhoto> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_upload_progress, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UpPhoto photo = photos.get(position);

        holder.title.setText(photo.getTitle());
        holder.progressBar.setMax(100);
        holder.progressBar.setProgress(0);
        ImageLoader.getInstance().displayImage("file:///" + photo.getUrl(), holder.imageView);

        viewHolders.add(holder);

        //uploadProgressHolders.put(uploadID, holder);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        View overlay;
        ProgressBar progressBar;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            overlay = itemView.findViewById(R.id.overlay);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            title = (TextView) itemView.findViewById(R.id.title);

            /*btnCancel = (Button) findViewById(R.id.cancel_upload);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (uploadId == null)
                        return;

                    UploadService.stopUpload(uploadId);
                }
            });*/
        }
    }

    private void logSuccessfullyUploadedFiles(List<String> files) {
        for (String file : files) {
            Log.e(TAG, "Success:" + file);
        }
    }

    @Override
    public void onProgress(UploadInfo uploadInfo) {
        Log.i(TAG, String.format(Locale.getDefault(), "ID: %1$s (%2$d%%) at %3$.2f Kbit/s",
                uploadInfo.getUploadId(), uploadInfo.getProgressPercent(),
                uploadInfo.getUploadRate()));
        logSuccessfullyUploadedFiles(uploadInfo.getSuccessfullyUploadedFiles());

        if (uploadProgressHolders.get(uploadInfo.getUploadId()) == null)
            return;

        ViewHolder viewHolder = uploadProgressHolders.get(uploadInfo.getUploadId());
        viewHolder.progressBar.setProgress(uploadInfo.getProgressPercent());
    }

    @Override
    public void onError(UploadInfo uploadInfo, Exception exception) {
        Log.e(TAG, "Error with ID: " + uploadInfo.getUploadId() + ": "
                + exception.getLocalizedMessage(), exception);
        logSuccessfullyUploadedFiles(uploadInfo.getSuccessfullyUploadedFiles());

        ViewHolder viewHolder = uploadProgressHolders.get(uploadInfo.getUploadId());
        if (viewHolder == null)
            return;

        //uploadProgressHolders.remove(uploadInfo.getUploadId());

        viewHolder.progressBar.setVisibility(View.GONE);
        viewHolder.overlay.setVisibility(View.GONE);
    }

    @Override
    public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
        Log.i(TAG, String.format(Locale.getDefault(),
                "ID %1$s: completed in %2$ds at %3$.2f Kbit/s. Response code: %4$d, body:[%5$s]",
                uploadInfo.getUploadId(), uploadInfo.getElapsedTime() / 1000,
                uploadInfo.getUploadRate(), serverResponse.getHttpCode(),
                serverResponse.getBodyAsString()));
        logSuccessfullyUploadedFiles(uploadInfo.getSuccessfullyUploadedFiles());
        for (Map.Entry<String, String> header : serverResponse.getHeaders().entrySet()) {
            Log.i("Header", header.getKey() + ": " + header.getValue());
        }

        Log.e(TAG, "Printing response body bytes");
        byte[] ba = serverResponse.getBody();
        for (int j = 0; j < ba.length; j++) {
            Log.e(TAG, String.format("%02X ", ba[j]));
        }

        ViewHolder viewHolder = uploadProgressHolders.get(uploadInfo.getUploadId());
        if (viewHolder == null)
            return;

        viewHolder.progressBar.setProgress(100);
        viewHolder.title.setVisibility(View.VISIBLE);
        viewHolder.progressBar.setVisibility(View.GONE);
        viewHolder.overlay.setVisibility(View.GONE);
    }

    @Override
    public void onCancelled(UploadInfo uploadInfo) {
        Log.i(TAG, "Upload with ID " + uploadInfo.getUploadId() + " is cancelled");
        logSuccessfullyUploadedFiles(uploadInfo.getSuccessfullyUploadedFiles());

        ViewHolder viewHolder = uploadProgressHolders.get(uploadInfo.getUploadId());
        if (viewHolder == null)
            return;
    }

    public void startUploading(String url) {
        for (ViewHolder viewHolder : viewHolders) {
            singleFileUpload(viewHolder, url);
        }
    }

    private void singleFileUpload(ViewHolder viewHolder, String url) {
        UpPhoto upPhoto = photos.get(viewHolder.getAdapterPosition());
        final String filename = upPhoto.getTitle();

        try {
            MultipartUploadRequest request = new MultipartUploadRequest(context, url)
                    .addFileToUpload(upPhoto.getUrl(), "uploaded_file")
                    .setNotificationConfig(getNotificationConfig(filename))
                    .setCustomUserAgent(USER_AGENT)
                    //.setAutoDeleteFilesAfterSuccessfulUpload(true)
                    //.setUsesFixedLengthStreamingMode(true)
                    .setMaxRetries(3);

            //request.setUtf8Charset();

            String uploadID = request.setDelegate(this).startUpload();

            uploadProgressHolders.put(uploadID, viewHolder);

            viewHolder.progressBar.setVisibility(View.VISIBLE);
            viewHolder.overlay.setVisibility(View.VISIBLE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private UploadNotificationConfig getNotificationConfig(String fileName) {
        return new UploadNotificationConfig()
                .setIcon(R.drawable.ic_upload)
                .setCompletedIcon(R.drawable.ic_upload_success)
                .setErrorIcon(R.drawable.ic_upload_error)
                .setTitle(fileName)
                .setInProgressMessage(context.getString(R.string.uploading))
                .setCompletedMessage(context.getString(R.string.upload_success))
                .setErrorMessage(context.getString(R.string.upload_failed))
                .setAutoClearOnSuccess(true)
                .setClickIntent(new Intent(context, MainActivity.class))
                .setClearOnAction(true)
                .setRingToneEnabled(true);
    }
}
