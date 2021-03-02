package com.kotlin.mvp.base

/**
 * Created by flyjiang on 2019/8/2.
 * Presenter基类
 */
interface BasePresenter<T : BaseView> {
    fun attachView(view: T)
    fun detachView()
}