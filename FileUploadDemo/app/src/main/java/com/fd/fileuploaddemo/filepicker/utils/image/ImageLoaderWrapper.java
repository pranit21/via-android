package com.fd.fileuploaddemo.filepicker.utils.image;

import android.net.Uri;
import android.support.annotation.DrawableRes;

import java.io.File;

/**
 * Created by Pranit on 12-09-2016.
 */

public interface ImageLoaderWrapper<TARGET extends Object,OPTION extends ImageLoaderWrapper.ImageOption> {
    interface ImageOption{}

    void showImage(TARGET imageView, Uri uri, OPTION option);
    void showImage(TARGET imageView, String uri, OPTION option);
    void showImage(TARGET imageView, File file, OPTION option);
    void showImage(TARGET imageView, @DrawableRes int id, OPTION option);

    OPTION newOption(int resizeW, int resizeH);
}
