package com.kotlin.mvp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import in.srain.cube.util.CLog;


/**
 * 多线程断点下载工具类
 */
public class MultiThreadDownloadUtil {
    private static final String TAG = "MultiThreadDownloadUtil";
    /**
     * 线程的数量,默认为开启1个线程
     */
    private static int threadCount = 1;
    /**
     * 每个下载区块的大小
     */
    private static long blocksize;
    /**
     * 当前正在工作的线程个数
     */
    private static int runningThreadCount;
    /**
     * 线程下载缓冲区,默认为10M
     */
    private static int defaultBuff = 1024 * 1024 * 10;

    /**
     * 上下文
     */
    private static Context mContext;
    /**
     * 是否成功下载
     */
    private static Boolean finished = false;

    private static List<DownloadThread> threads = new ArrayList<>();

    /**
     * 断点下载文件的方法
     *
     * @param context                 上下文
     * @param path                    下载路径
     * @param downloadResponseHandler 下载信息回调
     */
    public static void downloadFile(Context context, String path, DownloadResponseHandler downloadResponseHandler) {
        if (context == null) {
            //  ToastUtil.showToast("Context不能为空!");
            return;
        }
        if (TextUtils.isEmpty(path)) {
            ToastUtil.showToast("下载路径不能为空!");
            return;
        }
        HttpURLConnection conn = null;
        mContext = context;
        try {
            /***********************************重定向判断***********************************/
            String redictURL = null;
            HttpURLConnection connection;
            if (path.toLowerCase().startsWith("https")) {
                connection = (HttpsURLConnection) new URL(path).openConnection();
            } else {
                connection = (HttpURLConnection) new URL(path).openConnection();
            }
            Map<String, List<String>> fieldMap = connection.getHeaderFields();
            if (fieldMap != null) {
                for (String key : fieldMap.keySet()) {
                    if ("location".equalsIgnoreCase(key)) {
                        redictURL = fieldMap.get(key).get(0);
                    }
                }
            }
            if (redictURL == null) {
                redictURL = connection.getURL().toString().toLowerCase();
            }
            // 说明是重定向
            if (redictURL.startsWith("http")
                    && !redictURL.equals(path.toLowerCase())) {
                path = redictURL;
                CLog.d(TAG, "重定向地址后的文件下载地址:" + path);
            }
            connection.disconnect();
            /********************************************************************************/
            // URL url = new URL(path);
            URL url = new URL(URLEncoder.encode(path, "UTF-8").replaceAll("\\+", "%20").replaceAll("%3A", ":").replaceAll("%2F", "/") //utf转码，支持中文
            );
            if (path.toLowerCase().startsWith("https")) {
                conn = (HttpsURLConnection) url.openConnection();
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(1000);
            int code = conn.getResponseCode();
            if (code == 200) {
                // 得到服务器端返回的文件大小,单位byte,字节
                long size = conn.getContentLength();
                CLog.d(TAG, "服务器文件的大小:" + size);
                /**
                 * 1.首先在本地创建一个文件大小跟服务器一模一样的空白文件,RandomAccessFile类的实例支持对随机访问文件的读取和写入
                 * 参数1:目标文件 参数2:打开该文件的访问模式,"r" 以只读方式打开 ,"rw" 打开以便读取和写入
                 */
                File file = new File(getFileSavePath(path), getFileName(path));
                if (file.exists() && file.length() == size && AppUtil.isCanInstallApp(mContext, file.getCanonicalPath())) {
                    downloadResponseHandler.onSuccess("SD卡已存在需要下载的APK", getFileName(path));
                } else {
                    deleteDir(file.getParentFile()); //删除父目录下原有的Apk文件
                    if (file.exists()) {  //删除存在的大小不同的APK
                        file.delete();
                    }
                    // 计算每个blocksize的大小和记录当前线程的总数
                    blocksize = size / threadCount;
                    runningThreadCount = threadCount;
                    RandomAccessFile raf = new RandomAccessFile(file, "rw");
                    raf.setLength(size);// 设置文件的大小
                    // 2.开启若干个子线程,分别下载对应的资源
                    threads.clear();
                    for (int i = 1; i <= threadCount; i++) {
                        long startIndex = (i - 1) * blocksize; // 由于服务端下载文件是从0开始的
                        long endIndex = i * blocksize - 1;
                        if (i == threadCount) {
                            // 最后一个线程
                            endIndex = size - 1;
                        }
                        CLog.d(TAG, "开启线程:" + i + "下载的位置" + startIndex + "~" + endIndex);
                        //开启线程
                        DownloadThread a = new DownloadThread(path, i, startIndex, endIndex, downloadResponseHandler);
                        threads.add(a);
                        a.start();
                    }
                }
            } else {
                downloadResponseHandler.onFailed("连接失败");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            downloadResponseHandler.onFailed("下载失败,请确认你的URL下载地址是否可用");
        } catch (ProtocolException e) {
            e.printStackTrace();
            downloadResponseHandler.onFailed("下载失败");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            downloadResponseHandler.onFailed("下载失败,请检查你的sd卡是否存在");
        } catch (IOException e) {
            e.printStackTrace();
            downloadResponseHandler.onFailed("下载失败,请检查你的网络是否存在,或者sd卡空间不足");
        } finally {
            if (null != conn) {
                conn.disconnect();
                conn = null;
                System.gc();
            }
        }
    }


    public static void cancelThread() {
        for (DownloadThread a : threads) {
            a.stopTask();
        }
    }
    /**
     * 自定义下载线程
     *
     * @author Chenys
     */
    private static class DownloadThread extends Thread {
        private int threadId; // 线程id
        private long startIndex; // 开始下载的位置
        private long endIndex; // 结束下载的位置
        private String path;
        private DownloadResponseHandler downloadResponseHandler;
        private volatile boolean mWorking = true;
        public boolean ismWorking() {
            return mWorking;
        }

        public DownloadThread(String path, int threadId, long startIndex, long endIndex, DownloadResponseHandler downloadResponseHandler) {
            this.path = path;
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.downloadResponseHandler = downloadResponseHandler;
        }

        protected void stopTask() {
            mWorking = false;
        }
                /**
         * 执行下载任务
         */
        @Override
        public void run() {
          while (mWorking)
            {
                HttpURLConnection conn = null;
                //计算每个线程的下载总长度
                long totalSize = endIndex - startIndex;
                final String finalName = getFileName(path);
                try {
                    //创建记录资源当前下载到什么位置的临时文件
                    File tempFile = new File(getFileSavePath(path), threadId + ".temp");
                    //记录当前线程下载的大小
                    int currentSize = 0;
                    //  URL url = new URL(path);
                    URL url = new URL(URLEncoder.encode(path, "UTF-8").replaceAll("\\+", "%20").replaceAll("%3A", ":").replaceAll("%2F", "/"));  //utf转码，支持中文
                    if (path.toLowerCase().startsWith("https")) {
                        conn = (HttpsURLConnection) url.openConnection();
                    } else {
                        conn = (HttpURLConnection) url.openConnection();
                    }
                    conn.setRequestMethod("GET");
                    //接着从上一次断点的位置继续下载数据
                    if (tempFile.exists() && tempFile.length() > 0) {
                        FileInputStream lastDownload = new FileInputStream(tempFile);
                        BufferedReader br = new BufferedReader(new InputStreamReader(lastDownload));
                        //获取当前线程上次下载的总大小是多少
                        String lastSizeStr = br.readLine();
                        int lastSize = Integer.parseInt(lastSizeStr);
                        CLog.d(TAG, "上次线程" + threadId + "下载的总大小:" + lastSize);
                        //更新startIndex的位置和每条线程下载的当前大小数
                        startIndex += lastSize;
                        currentSize += lastSize;
                        lastDownload.close();
                    }
                    //设置http协议请求头: 指定每条线程从文件的什么位置开始下载,下载到什么位置为止
                    conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
                    conn.setConnectTimeout(5000);
                    int code = conn.getResponseCode();
                    CLog.d(TAG, "服务器返回码code=" + code);// 如果是下载一部分资源,那么返回码是206
                    //获取服务器返回的流
                    InputStream is = conn.getInputStream();
                    File file = new File(getFileSavePath(path), finalName);
                    RandomAccessFile raf = new RandomAccessFile(file, "rw");
                    //指定文件的下载起始位置
                    raf.seek(startIndex);
                    CLog.d(TAG, "第" + threadId + "个线程写文件的开始位置" + String.valueOf(startIndex));
                    //开始下载文件
                    int len = 0;
                    byte[] buff = new byte[defaultBuff]; //10M的缓冲数组,提高下载速度
                    while ((len = is.read(buff)) != -1&&mWorking) {
                        RandomAccessFile rf = new RandomAccessFile(tempFile, "rwd");//注意:这里采用的模式是"rwd",即无缓存模式的读写
                        //写数据到目标文件
                        raf.write(buff, 0, len);
                        //记录当前下载记录的位置到文件中
                        currentSize += len;
                        rf.write(String.valueOf(currentSize).getBytes());
                        rf.close();
                        //回调显示下载的进度
                        downloadResponseHandler.onProgress(threadId - 1, totalSize, currentSize);
                    }
                    is.close();
                    raf.close();
                    finished = true;
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                    finished = false;
                    downloadResponseHandler.onFailed("下载失败,请确认你的URL下载地址是否可用");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                    finished = false;
                    downloadResponseHandler.onFailed("下载失败");
                } catch (IOException e) {
                    e.printStackTrace();
                    finished = false;
                    downloadResponseHandler.onFailed("下载失败,请检查你的网络是否可用");
                } finally {
                    //以下是处理清理缓存的任务
                    synchronized (MultiThreadDownloadUtil.class) {
                        CLog.d(TAG, "线程" + threadId + "下载完毕了");
                        runningThreadCount--;
                        if (runningThreadCount < 1) {
                            CLog.d(TAG, "所有的线程都工作完毕了,删除临时文件");
                            for (int i = 1; i <= threadCount; i++) {
                                File tempFile = new File(getFileSavePath(path), i + ".temp");
                                CLog.d(TAG, "删除临时文件成功与否" + tempFile.delete());
                                if (finished&&mWorking) {
                                    downloadResponseHandler.onSuccess("下载完成", finalName);
                                }
                                //下载完毕后,恢复线程数量为允许设置的状态
                                SharedPreferences sp = mContext.getSharedPreferences("threadCount_sp_name", Context.MODE_PRIVATE);
                                sp.edit().putBoolean("is_count_setted", false).commit();
                            }
                            if (null != conn) {
                                conn.disconnect();
                                conn = null;
                                System.gc();
                            }
                        }
                    }

                }
            }
        }
    }

    /**
     * 设置总线程个数的方法,如果第一次已经设置过了,则不能则设置,只有等当前的任务下载完毕后才能设置
     *
     * @param threadCount
     */
    public static void setThreadCount(Context context, int threadCount) {
        if (context == null) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences("threadCount_sp_name", Context.MODE_PRIVATE);
        boolean setted = sp.getBoolean("isCountSetted", false);
        CLog.d(TAG, "是否已经设置过下载线程数" + setted);
        if (threadCount > 0 && !setted) {
            MultiThreadDownloadUtil.threadCount = threadCount;
            SharedPreferences.Editor edit = sp.edit();
            edit.putInt("thread_count", threadCount);
            edit.putBoolean("is_count_setted", true).commit();
        } else {
            int lastCountSet = sp.getInt("thread_count", 3);
            Toast.makeText(context, "你上次设置的下载线程数是:" + lastCountSet + "当前任务现在完毕后可重新设置线程数", Toast.LENGTH_LONG).show();
            MultiThreadDownloadUtil.threadCount = lastCountSet;
        }
    }

    /**
     * 默认的线程个数
     *
     * @return
     */
    public static int getDefaultThreadCount() {
        return 3;
    }

    /**
     * 设置缓存区大小
     */
    public static void setCacheSize(int defaultBuff) {
        if (defaultBuff < 0) {
            MultiThreadDownloadUtil.defaultBuff = defaultBuff;
        }
    }

    /**
     * 从一个apk文件去获取该文件的版本信息
     *
     * @param context
     *            本应用程序上下文
     * @param archiveFilePath
     *            APK文件的路径。如：/sdcard/download/XX.apk
     * @return
     */
    public static String getVersionNameFromApk(Context context, String archiveFilePath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo packInfo = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        String version = packInfo.versionName;
        return version;
    }
    /**
     * 获取文件的保存路径
     *
     * @return
     */
    private static String getFileSavePath(String path) {
        return getFileName(path).toLowerCase().endsWith(".apk") ? FilepathUtil.getCacheRootPath(mContext) + File.separator + FilepathUtil.getAPKS() + File.separator : FilepathUtil.getCacheRootPath(mContext) + File.separator + FilepathUtil.getFILES() + File.separator;
    }

    /**
     * 获取文件名
     */
    private static String getFileName(String path) {
        int index = path.lastIndexOf("/");
        String fileName = path.substring(index + 1);
        return fileName;
    }

    /**
     * 删除目录下的文件
     */
    private static void deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                if (!(new File(dir, children[i]).isDirectory())) { //非文件夹
                    new File(dir, children[i]).delete();
                }
            }
        }
    }

    /**
     * 下载成功或失败的回调接口
     */
    public interface DownloadResponseHandler {
        //下载成功时回调
        void onSuccess(String resultCode, String fileName);

        //下载失败时回调
        void onFailed(String result);

        //下载过程中回调,用于显示下载的进度
        void onProgress(int threadId, long totalBytes, long currentBytes);
    }

}
