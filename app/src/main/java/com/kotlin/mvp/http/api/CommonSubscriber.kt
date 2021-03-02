package com.kotlin.mvp.http.api
import com.google.gson.JsonSyntaxException
import com.kotlin.mvp.base.BaseView
import com.kotlin.mvp.http.response.HttpResponse
import com.kotlin.mvp.utils.ToastUtil
import io.reactivex.subscribers.ResourceSubscriber
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by flyjiang
 */
abstract class CommonSubscriber<T> : ResourceSubscriber<T> {
    private var showStatue = false //请求是否响应loading状态,用于展示noNetView, loadingView，emptyView,errorView = false
    private var showToast = true
    private var mView: BaseView

    constructor(mView: BaseView) {
        this.mView = mView
    }

    constructor(mView: BaseView, showStatue: Boolean) {
        this.showStatue = showStatue
        this.mView = mView
    }

    open fun setShowToast(showToast: Boolean): CommonSubscriber<T> {
        this.showToast = showToast
        return this
    }

    override fun onNext(httpResponse: T) {
        if (httpResponse is HttpResponse<*>) {
            if ((httpResponse as HttpResponse<*>).success != 1 && showToast) {
                ToastUtil.showToast((httpResponse as HttpResponse<*>).message)
            }
            if (showStatue) {
                mView.stateMain()
            }
        }
    }

    override fun onComplete() {
        mView.dismissDialogLoading()
    }

    override fun onError(e: Throwable) {
        mView.dismissDialogLoading()
        if (e is ConnectException) {   //网络异常，请求超时
            if (showStatue) {
                mView.stateError()
            } else {
                ToastUtil.showToast("网络连接失败")
            }
        } else if (e is UnknownHostException || e is HttpException) {
            if (showStatue) {
                mView.stateError()
            } else {
                ToastUtil.showToast("服务器连接失败")
            }
        } else if (e is SocketException) {
            if (showStatue) {
                mView.stateError()
            } else {
                ToastUtil.showToast("网络异常，读取数据超时")
            }
        } else if (e is SocketTimeoutException) {
            if (showStatue) {
                mView.stateError()
            } else {
                ToastUtil.showToast("连接超时")
            }
        } else if (e is JsonSyntaxException) {
            if (showStatue) {
                mView.stateError()
            } else {
                ToastUtil.showToast("解析异常")
            }
        } else if (e is IllegalStateException) {
            ToastUtil.showToast(e.toString())
        }
    }
}