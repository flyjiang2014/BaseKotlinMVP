package com.kotlin.mvp.component;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kotlin.mvp.R;
import com.lzy.imagepicker.loader.ImageLoader;

import java.io.File;

/**
 * ================================================
 * GlideImageLoader 加载器
 * ================================================
 */
public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_bg)              //设置占位图片
                .error(R.drawable.default_bg)                  //设置错误图片
                .diskCacheStrategy((DiskCacheStrategy.ALL)); //缓存全尺寸
        Glide.with(activity).load(Uri.fromFile(new File(path))).apply(options).into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy((DiskCacheStrategy.ALL));
        Glide.with(activity).load(Uri.fromFile(new File(path))).apply(options).into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}
