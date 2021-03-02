package com.kotlin.mvp.view
import androidx.core.content.ContextCompat
import com.kotlin.mvp.R
import com.kotlin.mvp.app.Constants
import com.kotlin.mvp.base.BaseActivity
import com.kotlin.mvp.component.GlideApp
import com.kotlin.mvp.component.GlideCircleTransformWithBorder
import com.kotlin.mvp.contract.MainContract
import com.kotlin.mvp.model.IndexShowBean
import com.kotlin.mvp.presenter.MainPresenter
import com.kotlin.mvp.utils.ActivityManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_main_bottom_layout.*
import kotlinx.android.synthetic.main.include_main_middle_layout.*
import kotlin.system.exitProcess

/**
 * Created by  on 2020/11/12.
 * 文件说明：首页主页
 */
class MainActivity : BaseActivity<MainPresenter, MainContract.View>(), MainContract.View, OnRefreshListener {


    private var indexShowBean: IndexShowBean? = null
    override fun initInject() {
        getActivityComponent().inject(this)
    }

    override fun setBaseContentView(): Int {
        setIsShowTitle(false)
        return R.layout.activity_main
    }

    override fun reLoadData() {
        super.reLoadData()
        mPresenter.getIndexData()
    }

    override fun init() {
        mLoadingLayout = findViewById(R.id.loading)
        mPresenter.permissionApply(RxPermissions(this))
        smartRefreshLayout.setOnRefreshListener(this)
        smartRefreshLayout.setEnableLoadMore(false)
    }


    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLayout.layout.postDelayed({
            mPresenter.getIndexData()
            refreshLayout.finishRefresh()
        }, 100)
    }

    override fun showContent(indexShowBean: IndexShowBean) {
        this.indexShowBean = indexShowBean
    }

    private var mExitClickTime: Long = 0
    override fun onBackPressedSupport() {
        if (System.currentTimeMillis() - mExitClickTime > 2000) {
            showToast("再按一次返回键关闭程序")
            mExitClickTime = System.currentTimeMillis()
        } else {
            ActivityManager.getInstance().finishAllActivity()
            exitProcess(0)
        }
    }
}