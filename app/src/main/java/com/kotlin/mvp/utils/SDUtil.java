package com.kotlin.mvp.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Environment.getDataDirectory().getPath() : /data
 * Environment.getDownloadCacheDirectory().getPath()  : /cache
 * Environment.getExternalStorageDirectory().getPath(): /mnt/sdcard
 * Environment.getRootDirectory().getPath()           : /system
 * Context.getCacheDir().getPath()                    : /data/data/com.zhd/cache
 * Context.getExternalCacheDir().getPath()            : /mnt/sdcard/Android/data/com.zhd/cache
 * Context.getFilesDir().getPath()                    : /data/data/com.zhd/files
 * Context.getObbDir().getPath()                      : /mnt/sdcard/Android/obb/com.zhd
 * Context.getPackageName()                           : com.zhd
 * Context.getPackageCodePath()                       : /data/app/com.zhd-1.apk
 * Context.getPackageResourcePath()                   : /data/app/com.zhd-1.apk
 */

/**
 * 作者：flyjiang
 * 说明: 用于获取手机SD卡信息
 */
public class SDUtil {
    /**
     * SD卡是否能用
     *
     * @return
     */
    public static boolean isSDCanUse() {
        try {
            return Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取SD卡的绝对路径
     *
     * @return 如果存在SD卡则返回路径，否则返回空字符
     */
    public static String getSDAbsolutePath() {
        if (isSDCanUse()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return "";
        }
    }

    /**
     * 获取SD卡总容量
     *
     * @return SD卡总容量大小
     */
    public static long getSDTotalSize() {
        if (!isSDCanUse()) {
            return 0;
        }
    /*获取存储卡路径*/
        File sdcardDir = Environment.getExternalStorageDirectory();
    /*StatFs 看文件系统空间使用情况*/
        StatFs statFs = new StatFs(sdcardDir.getPath());
        if (Build.VERSION.SDK_INT >= 18) {
            long blockSize = statFs.getBlockSizeLong();
            long totalSize = statFs.getBlockCountLong();
            return blockSize * totalSize;
        } else {
            long blockSize = statFs.getBlockSize();
            long totalSize = statFs.getBlockCount();
            return blockSize * totalSize;
        }
    }

    /**
     * 获得sd卡剩余容量，即可以大小
     *
     * @return sd卡剩余容量
     */
    public static long getSDAvaliableSize() {
        if (!isSDCanUse()) {
            return 0;
        }
        File path = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(path.getPath());
        if (Build.VERSION.SDK_INT >= 18) {
            long blockSize = statFs.getBlockSizeLong();
            long availableBlocks = statFs.getAvailableBlocksLong();
            return blockSize * availableBlocks;
        } else {
            long blockSize = statFs.getBlockSize();
            long availableBlocks = statFs.getAvailableBlocks();
            return blockSize * availableBlocks;
        }
    }

    /**
     * 获得机身内存大小
     *
     * @return 机身内存大小
     */
    public static long getRomTotalSize() {
        File path = Environment.getDataDirectory();
        StatFs statFs = new StatFs(path.getPath());
        if (Build.VERSION.SDK_INT >= 18) {
            long blockSize = statFs.getBlockSizeLong();
            long tatalBlocks = statFs.getBlockCountLong();
            return blockSize * tatalBlocks;
        } else {
            long blockSize = statFs.getBlockSize();
            long tatalBlocks = statFs.getBlockCount();
            return blockSize * tatalBlocks;
        }
    }

    /**
     * 获得机身可用内存大小
     *
     * @return 机身可用内存大小
     */
    public static long getRomAvailableSize() {
        File path = Environment.getDataDirectory();
        StatFs statFs = new StatFs(path.getPath());
        if (Build.VERSION.SDK_INT >= 18) {
            long blockSize = statFs.getBlockSizeLong();
            long availableBlocks = statFs.getAvailableBlocksLong();
            return blockSize * availableBlocks;
        } else {
            long blockSize = statFs.getBlockSize();
            long availableBlocks = statFs.getAvailableBlocks();
            return blockSize * availableBlocks;
        }
    }

    /**
     * 获取可用内存大小
     *
     * @param context 上下文
     * @return
     */
    public static long getRamAvailableSize(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        return mi.availMem;
    }

    /**
     * 获取总运存大小
     *
     * @param context
     * @return
     */
    public static long getRamTotalSize(Context context) {
        if (Build.VERSION.SDK_INT >= 16) {
            // 获取android当前总内存大小
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(mi);
            //mi.totalMem; 当前系统的总内存
            //return Formatter.formatFileSize(context, mi.totalMem);// 将获取的内存大小规格化
            return mi.totalMem;
        } else {
            String dir = "/proc/meminfo";
            try {
                FileReader fr = new FileReader(dir);
                BufferedReader br = new BufferedReader(fr, 2048);
                String memoryLine = br.readLine();
                String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
                br.close();
                return Long.parseLong(subMemoryLine.replaceAll("\\D+", "")) * 1024l;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
}