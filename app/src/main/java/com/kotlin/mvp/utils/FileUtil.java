package com.kotlin.mvp.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * 作者：flyjiang
 * 说明: 文件夹初始化等(需要文件读写权限)
 */
public class FileUtil {
    /**
     * 初始化默认文件夹(如果存在SD卡则可以在SD卡下面Android/data/包名/cache下面看到创建的四个文件夹)
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        if (context == null) {
            return;
        }
        String rootPath = FilepathUtil.getCacheRootPath(context);
        //创建图片文件夹
        createFileDirs(new File(rootPath + File.separator + FilepathUtil.getIMAGES()));
        //创建文件文件夹
        createFileDirs(new File(rootPath + File.separator + FilepathUtil.getFILES()));
        //创建APK文件夹
        createFileDirs(new File(rootPath + File.separator + FilepathUtil.getAPKS()));
        //创建LOG文件夹
        createFileDirs(new File(rootPath + File.separator + FilepathUtil.getLOGS()));
    }

    /**
     * 创建文件目录
     *
     * @param file 需要创建的文件目录
     */
    public static void createFileDirs(File file) {
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
    }



    /**
     * 创建文件，如果已存在则不重新创建新文件
     *
     * @param file 需要创建的文件
     * @return 创建文件是否成功
     */
    public static boolean createFile(File file) {
        if (file != null && !file.exists()) {
            try {
                file.createNewFile();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 创建文件，如果存在该文件则删除后重新创建
     *
     * @param file 需要创建的文件
     * @return 创建文件是否成功
     */
    public static boolean createFileDelOld(File file) {
        if (file != null) {
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 获取文件夹中文件数量
     *
     * @param file 文件夹
     * @return 文件数量
     */
    public static long getFilesNumber(File file) {
        if (file != null && file.exists() && file.isDirectory()) {
            file.length();
        }
        return 0;
    }

    /**
     * 递归删除文件夹及其里面的文件
     *
     * @param file 需要删除的文件目录
     */
    public static void deleteFiles(File file) {
        if (file == null) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFiles(f);
            }
            file.delete();
        }
    }
    /**
     * 文件复制
     *
     * @param s 被复制的文件目录
     * @param t 复制后的文件目录
     */
    public static void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传图片时限制文件大小
     * @param files
     * @return
     */
    public static boolean checkFileSizeMax(List<File> files){
        Long length = 0l;
        for (int i = 0; i < files.size(); i++) {
            File file  = files.get(i);
            if(file.length()>=5*1024*1024){ //大于5M
                ToastUtil.showToast("您上传的图片太大，已达上限");
                return true;
            }
            length += file.length();
            if(length>=30*1024*1024){ //大于30M
                ToastUtil.showToast("您上传的图片太大，已达上限");
                return true;
            }
        }
        return false;
    }
}
