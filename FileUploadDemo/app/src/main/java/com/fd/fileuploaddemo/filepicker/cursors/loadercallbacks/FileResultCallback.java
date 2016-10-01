package com.fd.fileuploaddemo.filepicker.cursors.loadercallbacks;

import java.util.List;

/**
 * Created by Pranit on 12-09-2016.
 */

public interface FileResultCallback<T> {
    void onResultCallback(List<T> files);
}
