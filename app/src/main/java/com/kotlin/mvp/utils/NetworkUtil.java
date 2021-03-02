package com.kotlin.mvp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


public class NetworkUtil {
    private static final String TAG = "NetworkUtil";

    /**
     * 获取网络信息类
     *
     * @param context 上下文
     * @return 网络信息类
     */
    private static NetworkInfo getNetworkInfo(final Context context) {
        if (context == null) {
            return null;
        }
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return conManager.getActiveNetworkInfo();
    }

    /**
     * 判断手机是否已联网
     *
     * @param context 上下文
     * @return 联网返回true, 未联网返回false
     */
    public static boolean isConnect(final Context context) {
        return getNetworkInfo(context) == null ? false : getNetworkInfo(context).isConnected();
    }

    /**
     * 判断手机网络是否可用
     *
     * @param context 上下文
     * @return 可用返回true, 不可用返回false
     */
    public static boolean isAvailable(final Context context) {
        return getNetworkInfo(context) == null ? false : getNetworkInfo(context).isAvailable();
    }

    /**
     * 判断是否处于漫游
     *
     * @param context 上下文
     * @return 漫游返回true，非漫游返回false
     */
    public static boolean isRoaming(final Context context) {
        return getNetworkInfo(context) == null ? false : getNetworkInfo(context).isRoaming();
    }

    /**
     * 判断是否连接的WIFI
     *
     * @param context 上下文
     * @return 连接WIFI返回true，未连接WIFI返回false
     */
    public static boolean isWifi(final Context context) {
        if (getNetworkInfo(context) != null && getNetworkInfo(context).getType() == ConnectivityManager.TYPE_WIFI) {
            return getNetworkInfo(context).isConnected();
        } else {
            return false;
        }
    }

    /**
     * 获取网络连接类型名称
     *
     * @param context 上下文
     * @return 网络连接类型名称
     */
    public static String getConnectName(final Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        if (networkInfo == null) {
            return "未能获取网络信息";
        }
        if (!networkInfo.isConnected()) {
            return "unConnected";
        } else if (!networkInfo.isAvailable()) {
            return "unAvailable";
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return "WIFI";
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return getPhoneNetName(context);
        } else {
            return "unknow";
        }
    }

    /**
     * 获取手机网络类型
     *
     * @param context 上下文
     * @return 网络类型字符串
     */
    private static String getPhoneNetName(final Context context) {
        String type = "手机网络未开";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final int networkType = telephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                type = "NETWORK_TYPE_UNKNOWN";
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                type = "NETWORK_TYPE_GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                type = "NETWORK_TYPE_EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                type = "NETWORK_TYPE_UMTS";
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                type = "NETWORK_TYPE_CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                type = "NETWORK_TYPE_EVDO_0";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                type = "NETWORK_TYPE_EVDO_A";
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                type = "NETWORK_TYPE_1xRTT";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                type = "NETWORK_TYPE_HSDPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                type = "NETWORK_TYPE_HSUPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                type = "NETWORK_TYPE_HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                type = "NETWORK_TYPE_IDEN";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                type = "NETWORK_TYPE_EVDO_B";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                type = "NETWORK_TYPE_LTE";
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                type = "NETWORK_TYPE_EHRPD";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                type = "NETWORK_TYPE_HSPAP";
                break;
            default:
                type = "未知网络";
                break;
        }
        return type;
    }


    /**
     * 是否能Ping通百度，需要放到子线程中(测试发现WIFI状态下不稳定)
     *
     * @return 能Ping通返回true, 不能Ping通返回false
     */
    public static boolean isSuccessPingInThread() {
        try {
            Process p = Runtime.getRuntime().exec("/system/bin/ping -c 3 www.baidu.com");
            int status = p.waitFor();
            if (status == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
