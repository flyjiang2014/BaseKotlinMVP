package com.kotlin.mvp.component;

import android.content.Context;
import com.kotlin.mvp.utils.FilepathUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by ${flyjiang} on 2018/4/26.
 * 文件说明：Glide缓存配置
 */

@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(@NotNull Context context, GlideBuilder builder) {
        int cacheSize1000MegaBytes = 1000 * 1024 * 1024;
        builder.setDiskCache(  //设置缓存
                new DiskLruCacheFactory(FilepathUtil.getCacheRootPath(context) + File.separator + FilepathUtil.getIMAGES(), cacheSize1000MegaBytes)
        );
    }

    @Override
    public void registerComponents(@NotNull Context context, @NotNull Glide glide, @NotNull Registry registry) {
    }
}
