package com.kotlin.mvp.utils;

import android.content.Context;

/**
 * 作者：flyjiang
 * 说明: 用于操作文件路径等
 */
public class FilepathUtil {
    /**
     * 图片
     */
    private final static String IMAGES = "images";
    /**
     * 小图片
     */
    private final static String IMAGES_SAMLL = "images_small";
    /**
     * 文件
     */
    private final static String FILES = "files";
    /**
     * APK
     */
    private final static String APKS = "apks";
    /**
     * logs
     */
    private final static String LOGS = "logs";


    /**
     * 获取包名对应地址
     *
     * @param context 上下文
     * @return 如果没有SD卡则返回系统对应目录
     */
    public static String getCacheRootPath(Context context) {
        try {
            if (context != null) {
                return SDUtil.isSDCanUse() ? context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getIMAGES() {
        return IMAGES;
    }

    public static String getIMAGES_SAMLL() {
        return IMAGES_SAMLL;
    }

    public static String getFILES() {
        return FILES;
    }

    public static String getAPKS() {
        return APKS;
    }

    public static String getLOGS() {
        return LOGS;
    }

}
