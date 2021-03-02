package com.kotlin.mvp.app

import com.kotlin.mvp.utils.SharepreferenceUtil

/**
 * Created by  on 2020/11/11.
 * 文件说明：
 */
object Constants {
    var HOST = getBaseHost()  //接口请求地址

    /**
     * 请求接口地址
     */
    private const val BASE_HOST_IN_TEST = "http://218.90.187.218:6699/guide_m/"  //内测
    private const val BASE_HOST_OUT_TEST = "http://218.90.187.218:8888/guide_m/"//外测
    private  const val BASE_HOST_OUT_ONLINE = "http://218.90.187.218:8888/guide_m/"//外正


    private const val ENVIRONMENT = "ENVIRONMENT"//网络环境，内测，外测，外正
    const val TOKEN = "token" //token
    const val APP_FIRST_IN = "app_first_in"//是否首次打开App
    const val NOW_DATE = "now_date"//当前时间
    const val KDD_ANDROID_ID = "kdd_android_id" //app id

    /**
     * 获取接口请求地址
     */
    private fun getBaseHost():String {
        return when(SharepreferenceUtil.getString(ENVIRONMENT)){
            "1"-> BASE_HOST_IN_TEST
            "2"-> BASE_HOST_OUT_TEST
            "3"-> BASE_HOST_OUT_ONLINE
            else ->BASE_HOST_OUT_ONLINE  //默认外正
        }
    }
}