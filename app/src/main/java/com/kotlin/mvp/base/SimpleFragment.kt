package com.kotlin.mvp.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kotlin.mvp.R
import com.kotlin.mvp.widget.loading.LoadingDialog
import com.kotlin.mvp.widget.loading.LoadingLayout
import com.kotlin.mvp.widget.loading.LoadingLayout.OnReloadListener
import me.yokeyword.fragmentation.SupportFragment

/**
 * Created by flyjiang on 2019/8/2.
 * 说明: Fragment基类，无mvp
 */
abstract class SimpleFragment : SupportFragment() {
    private var rootView: View? = null
    protected var mContext: Context? = null
    private var isVisibleToUser = false //是否可见
    private var isPrepared = false //View是否已加载完毕
    private var isFirst = true //是否第一次加载数据,为false时，切换不在重新加载数据

    /**
     * 获取可见次数
     */
    var visibleTimes = 0 //被可见的次数
        protected set
    protected var pageSize = 10

    /**
     * 页面加载过程布局
     */
    private lateinit var mLoadingLayout: LoadingLayout

    /**
     * 是否使用loading框架
     */
    private var isUseLoading = true

    /**
     * 全局dialog
     */
    protected lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = _mActivity!!.applicationContext //使用整个应用的上下文对象
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = initView(inflater)
        }
        if (isUseLoading) {
            mLoadingLayout = inflater.inflate(R.layout.loading_layout, null) as LoadingLayout
            mLoadingLayout.addView(rootView, 0) //自定义的界面加载到最底层
            mLoadingLayout.setOnReloadListener{reLoadData()}
        }
        return if (isUseLoading) mLoadingLayout else rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadingDialog = dialogCreate()
        isPrepared = true
        lazyLoad()
    }

    private fun lazyLoad() {
        if (!isPrepared || !isVisibleToUser || !isFirst) {
            return
        }
        initData()
        isFirst = false
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            visibleTimes++
        }
        if (userVisibleHint) {
            this.isVisibleToUser = true
            lazyLoad()
        } else {
            this.isVisibleToUser = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (rootView != null && rootView!!.parent != null) {
            (rootView!!.parent as ViewGroup).removeView(rootView)
        }
    }

    fun getmLoadingLayout(): LoadingLayout {
        return mLoadingLayout
    }

    /**
     * 子类实现初始化View操作
     */
    protected abstract fun initView(inflater: LayoutInflater?): View?

    /**
     * 子类实现初始化View本地数据初始化
     */
    protected abstract fun initViewData()

    /**
     * 子类实现初始化数据操作(子类自己调用)
     */
    abstract fun initData()

    /**
     * 设置是否使用loading框架
     */
    fun setUseLoading(useLoading: Boolean) {
        isUseLoading = useLoading
    }

    /**
     * 加载菊花提示
     *
     * @return
     */
   private fun dialogCreate(): LoadingDialog {
        return LoadingDialog.Builder(_mActivity).create()
    }

    /**
     * 获取RecycleView无数据时填充的emptyView
     */
    val adapterEmptyView: View
        get() = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_empty_view, null)

    /**
     * 重试处理，需重写的处理方法
     */
    open fun reLoadData() {}
}