package com.kotlin.mvp.contract

import com.kotlin.mvp.base.BasePresenter
import com.kotlin.mvp.base.BaseView
import com.kotlin.mvp.model.IndexShowBean
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * Created by flyjiang
 */
interface MainContract {
    interface View : BaseView {
        fun showContent(indexShowBean: IndexShowBean)
    }

    interface Presenter : BasePresenter<View> {
        fun getIndexData()
        fun permissionApply(rxPermissions: RxPermissions)
    }
}