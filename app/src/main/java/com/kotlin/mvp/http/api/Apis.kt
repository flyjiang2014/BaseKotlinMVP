package com.kotlin.mvp.http.api

import com.kotlin.mvp.http.response.HttpResponse
import com.kotlin.mvp.model.*
import io.reactivex.Flowable
import retrofit2.http.*

/**
 * Created by  on 2020/12/9.
 * 文件说明：Apis
 */
interface Apis {
    @POST("user/indexShow.do") //获取主页信息
    fun getIndexData(): Flowable<HttpResponse<IndexShowBean>>
}