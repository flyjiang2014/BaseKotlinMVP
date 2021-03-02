package com.kotlin.mvp.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * 常用单位转换的辅助类
 * Time:18:55
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率从dp的单位转成为px(像素)
     *
     * @param context 上下文
     * @param dpValue 需要转换的DP值
     *
     * @return 转换后的DP值
     */
    public static int dp2px(Context context, float dpValue) {
        return context == null ? (int) dpValue : (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从sp的单位转成为px(像素)
     *
     * @param context 上下文
     * @param spValue 需要转换的SP值
     *
     * @return 转换后的PX值
     */
    public static int sp2px(Context context, float spValue) {
        return context == null ? (int) spValue : (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }


    /**
     * 根据手机的分辨率从px(像素)的单位转成为dp
     *
     * @param context 上下文
     * @param pxValue 需要转换的PX值
     *
     * @return 转换后的PX值
     */
    public static float px2dp(Context context, float pxValue) {
        return context == null ? pxValue : pxValue / context.getResources().getDisplayMetrics().density;
    }

    /**
     * 描述：根据手机的分辨率从px(像素)的单位转成为sp
     *
     * @param context 上下文
     * @param pxValue 需要转换的PX值
     *
     * @return 转换后的SP值
     */
    public static float px2sp(Context context, float pxValue) {
        return context == null ? pxValue : pxValue / context.getResources().getDisplayMetrics().scaledDensity;
    }

}
