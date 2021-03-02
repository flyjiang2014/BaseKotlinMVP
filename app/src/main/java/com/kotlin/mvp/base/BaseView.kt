package com.kotlin.mvp.base

/**
 * Created by flyjiang on 2019/8/2.
 * View基类
 */
interface BaseView {
    fun stateError()
    fun stateEmpty()
    fun stateLoading()
    fun stateMain()
    fun showDialogLoading()
    fun dismissDialogLoading()
}