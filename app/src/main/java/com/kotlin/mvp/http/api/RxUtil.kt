package com.kotlin.mvp.http.api

import com.kotlin.mvp.http.exception.ApiException
import com.kotlin.mvp.http.response.HttpResponse
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * Created by flyjiang on 2019/8/2.
 */
object RxUtil {
    /**
     * 统一线程处理
     * @param <T>
     * @return
    </T> */
    fun <T> rxSchedulerHelper(): FlowableTransformer<T, T> {    //compose简化线程
        return FlowableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * 统一返回结果处理
     * @param <T>
     * @return
    </T> */
    fun <T> handleHttpResult(): FlowableTransformer<HttpResponse<T>, T> {   //compose判断结果
        return FlowableTransformer<HttpResponse<T>, T> { httpResponseFlowable ->
            httpResponseFlowable.flatMap(Function<HttpResponse<T>, Flowable<T>> { httpResponse ->
                if (httpResponse.success == 1) {
                    createData(httpResponse.data)
                } else {
                    Flowable.error(ApiException(httpResponse.message, httpResponse.success))
                }
            })
        }
    }

    /**
     * 生成Flowable
     * @param <T>
     * @return
    </T> */
    private fun <T> createData(t: T): Flowable<T> {
        return Flowable.create({ emitter ->
            try {
                emitter.onNext(t)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }, BackpressureStrategy.BUFFER)
    }
}