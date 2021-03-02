package com.kotlin.mvp.base

import android.os.Bundle
import android.view.View
import com.kotlin.mvp.app.App.Companion.getAppComponent
import com.kotlin.mvp.dagger.component.DaggerFragmentComponent
import com.kotlin.mvp.dagger.component.FragmentComponent
import com.kotlin.mvp.dagger.module.FragmentModule
import com.kotlin.mvp.widget.loading.LoadingLayout
import javax.inject.Inject

/**
 * Created by  on 2020/12/10.
 * 文件说明：
 */
abstract class BaseFragment <T:RxPresenter<V>, V:BaseView> : SimpleFragment(), BaseView{
    @Inject
    protected lateinit var mPresenter:T

    protected open fun getFragmentComponent(): FragmentComponent {
        return DaggerFragmentComponent.builder()
                .appComponent(getAppComponent())
                .fragmentModule(getFragmentModule())
                .build()
    }

    protected open fun getFragmentModule(): FragmentModule? {
        return FragmentModule(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initInject()
        mPresenter.attachView(this as V)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

    protected abstract fun initInject()


    override fun stateError() {
        getmLoadingLayout().status = LoadingLayout.Error
    }

    override fun stateEmpty() {
        TODO("Not yet implemented")
    }

    override fun stateLoading() {
        TODO("Not yet implemented")
    }

    override fun stateMain() {
        getmLoadingLayout().status = LoadingLayout.Success
    }

    override fun showDialogLoading() {
        if (!loadingDialog.isShowing) {
            loadingDialog.show()
        }
    }

    override fun dismissDialogLoading() {
        loadingDialog.dismiss()
    }
}