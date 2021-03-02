package com.kotlin.mvp.http

import com.kotlin.mvp.app.Constants
import com.kotlin.mvp.http.api.Apis
import com.kotlin.mvp.http.api.FileUpApis
import com.kotlin.mvp.http.response.HttpResponse
import com.kotlin.mvp.model.*
import com.kotlin.mvp.utils.SharepreferenceUtil
import io.reactivex.Flowable
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Created by  on 2020/12/9.
 * 文件说明：
 */
class RetrofitHelper @Inject constructor(private var apiService: Apis,private var fileUpApiService: FileUpApis) {
//
//    fun doFileUpload(multipartBody: MultipartBody): Flowable<HttpResponse<SinglePicBean>> { //单图上传
//        return fileUpApiService.fileUpload(multipartBody)
//    }
//
//    fun doFilesUpload(multipartBody: MultipartBody): Flowable<HttpResponse<DataPage<SinglePicBean>>> {//多图上传
//        return fileUpApiService.filesUpload(multipartBody)
//    }

    fun fetchIndexData(): Flowable<HttpResponse<IndexShowBean>> {
        return apiService.getIndexData()
    }
}