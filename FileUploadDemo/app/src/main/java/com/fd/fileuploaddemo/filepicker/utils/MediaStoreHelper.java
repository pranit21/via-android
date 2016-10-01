package com.fd.fileuploaddemo.filepicker.utils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.fd.fileuploaddemo.filepicker.cursors.DocScannerTask;
import com.fd.fileuploaddemo.filepicker.cursors.loadercallbacks.FileResultCallback;
import com.fd.fileuploaddemo.filepicker.cursors.loadercallbacks.PhotoDirLoaderCallbacks;
import com.fd.fileuploaddemo.filepicker.models.Document;
import com.fd.fileuploaddemo.filepicker.models.PhotoDirectory;

/**
 * Created by Pranit on 12-09-2016.
 */

public class MediaStoreHelper {
    public static void getPhotoDirs(FragmentActivity activity, Bundle args, FileResultCallback<PhotoDirectory> resultCallback) {
        activity.getSupportLoaderManager()
                .initLoader(0, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
    }

    public static void getDocs(FragmentActivity activity, FileResultCallback<Document> fileResultCallback)
    {
        new DocScannerTask(activity,fileResultCallback).execute();
    }
}
