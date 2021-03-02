package com.kotlin.mvp.base
import android.view.View
import com.kotlin.mvp.app.App
import com.kotlin.mvp.dagger.component.ActivityComponent
import com.kotlin.mvp.dagger.component.DaggerActivityComponent
import com.kotlin.mvp.dagger.module.ActivityModule
import com.kotlin.mvp.widget.loading.LoadingLayout
import javax.inject.Inject

/**
 * Created by  on 2020/11/11.
 * 文件说明：
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseActivity< T:RxPresenter<V>, V:BaseView> : SimpleActivity(), BaseView{
    @Inject
    protected lateinit var mPresenter:T

    protected  fun getActivityComponent(): ActivityComponent {
        return DaggerActivityComponent.builder()
                .appComponent(App.getAppComponent())
                .activityModule(getActivityModule())
                .build()
    }

    private fun getActivityModule(): ActivityModule {
        return ActivityModule(this)
    }


    override fun onViewCreated() {
        super.onViewCreated()
        initInject()
        mPresenter.attachView(this as V)
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    protected abstract fun initInject()


    override fun stateError() {
        getmLoadingLayout().status = LoadingLayout.Error
        mTitleRightRelativeLayout.visibility = View.GONE
    }

    override fun stateEmpty() {
        getmLoadingLayout().status = LoadingLayout.Empty
    }

    override fun stateLoading() {
        getmLoadingLayout().status = LoadingLayout.Loading
    }

    override fun stateMain() {
        getmLoadingLayout().status = LoadingLayout.Success
        mTitleRightRelativeLayout.visibility = View.VISIBLE
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