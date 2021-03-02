package com.kotlin.mvp.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.io.File;

public class AppUtil {
    /**
     * 获取包名管理类
     *
     * @param context 上下文
     * @return 包名管理类
     */
    private static PackageInfo getPackageInfo(final Context context) {
        if (context == null) {
            return null;
        }
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    /**
     * 获取当前APP的版本名
     *
     * @param context 上下文
     * @return 当前APP的版本名
     */
    public static String getAppVersionName(final Context context) {
        return context == null ? "" : getPackageInfo(context) == null ? "" : getPackageInfo(context).versionName;
    }

    /**
     * 获取当前APP版本号(一般用于APP升级)
     *
     * @param context 上下文
     * @return 当前APP版本号
     */
    public static int getAppVersionCode(final Context context) {
        return context == null ? -1 : getPackageInfo(context) == null ? -1 : getPackageInfo(context).versionCode;
    }

    /**
     * 获取包名
     *
     * @param context 上下文
     * @return 包名
     */
    public static String getPackageName(final Context context) {
        return context == null ? "" : getPackageInfo(context) == null ? "" : getPackageInfo(context).packageName;
    }

    /**
     * 判断是否安装该应用
     *
     * @param packageName 应用包名
     * @return 安装返回true, 未安装返回false
     */
    public static boolean isInstallAPP(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        } else {
            return new File("/data/data/" + packageName).exists();
        }
    }

    /**
     * 判断APP是否完整可以安装
     *
     * @param context  上下文
     * @param filePath 文件路径
     * @return
     */
    public static boolean isCanInstallApp(Context context, String filePath) {
        if (context == null || TextUtils.isEmpty(filePath)) {
            return false;
        }
        boolean result = false;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                result = true;//完整
            }
        } catch (Exception e) {
            result = false;//不完整
        }
        return result;
    }
}
