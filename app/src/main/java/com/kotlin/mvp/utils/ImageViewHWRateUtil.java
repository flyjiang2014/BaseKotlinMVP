package com.kotlin.mvp.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * Created by ${flyjiang} on 2017/11/23.
 * 文件说明：设置ImgView宽高比例
 */

/**
 * 宽度为屏幕宽度时设置参数
 */
public class ImageViewHWRateUtil {
    public static void setHeightWidthRate(ImageView imageView, int width , double rate) {
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = width;
        params.height = (int)( params.width/rate);
        imageView.setLayoutParams(params);
    }

    /**
     * 宽度为屏幕宽度时设置参数
     * @param context
     * @param imageView
     * @param rate 宽高比
     */
    public static void setHeightWidthRate(Context context, ImageView imageView, double rate) {
        setHeightWidthRate(imageView, PhoneUtil.getScreenWidth(context),rate);
    }
}
