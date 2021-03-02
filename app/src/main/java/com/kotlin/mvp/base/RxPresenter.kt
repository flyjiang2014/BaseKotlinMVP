package com.kotlin.mvp.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by  on 2020/11/17.
 * 文件说明：RxPresenter 基类
 */
abstract class RxPresenter<T:BaseView>: BasePresenter<T> {
    protected lateinit var mView:T
    private var mCompositeDisposable: CompositeDisposable? = null
     override fun attachView(view: T) {
        this.mView = view
    }

     override fun detachView() {
        unSubscribe()
    }

    private fun unSubscribe() {
        mCompositeDisposable?.clear()
        mCompositeDisposable = null
    }

    protected fun addSubscribe(subscription: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        subscription?.let { mCompositeDisposable?.add(it) }
    }

    protected  fun addSubscribeWithDialog(subscription: Disposable?) {
        mView.showDialogLoading()
        addSubscribe(subscription)
    }
}