package com.fd.fileuploaddemo;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import net.gotev.uploadservice.Logger;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.okhttp.OkHttpStack;

import okhttp3.OkHttpClient;

/**
 * Created by Pranit on 11-09-2016.
 */

public class SingletonApplication extends Application {
    private static SingletonApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        OkHttpClient client = new OkHttpClient();
        UploadService.HTTP_STACK = new OkHttpStack(client);

        // Set upload service debug log messages level
        Logger.setLogLevel(Logger.LogLevel.DEBUG);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(1000))
                .build();

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 MiB
                .tasksProcessingOrder(QueueProcessingType.LIFO);

        ImageLoader.getInstance().init(config.build());
    }

    public static synchronized SingletonApplication getInstance() {
        return instance;
    }
}
