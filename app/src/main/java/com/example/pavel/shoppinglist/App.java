package com.example.pavel.shoppinglist;

import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        configureImageLoader();
    }

    public void configureImageLoader(){

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheSize(4 * 1024 * 1024)
                .build();

        ImageLoader.getInstance().init(config);

    }

    public static DisplayImageOptions displayImageOptionsForGv(){

        return new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

    }


}
