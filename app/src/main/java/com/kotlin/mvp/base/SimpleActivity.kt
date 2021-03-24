package com.kotlin.mvp.base

import android.content.Context
import me.yokeyword.fragmentation.SupportActivity
import android.widget.RelativeLayout
import com.kotlin.mvp.widget.loading.LoadingLayout
import android.widget.TextView
import com.kotlin.mvp.widget.loading.LoadingDialog
import android.os.Bundle
import android.content.pm.ActivityInfo
import android.view.WindowManager
import android.view.LayoutInflater
import com.kotlin.mvp.R
import android.os.Build
import android.view.ViewGroup
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.kotlin.mvp.app.Constants
import com.kotlin.mvp.utils.*
import com.readystatesoftware.systembartint.SystemBarTintManager

/**
 * Created by flyjiang on 2019/8/2.
 * 说明: Activity 基类,无mvp
 */
abstract class SimpleActivity() : SupportActivity() {
    /**
     * TAG
     */
    @JvmField
    val TAG = this.javaClass.simpleName

    /**
     * 上下文
     */
    protected lateinit var mContext: Context

    /**
     * 布局根View
     */
    private lateinit var mBaseContainer: RelativeLayout

    /**
     * 状态栏颜色
     */
    private var statusColor = -1

    /**
     * 是否不填充状态栏
     */
    private var isNotFillSysState = true

    /**
     * 是否显示标题
     */
    private var isShowTitle = true

    /**
     * 是否使用loading框架
     */
    private var isUseLoading = true

    /**
     * 屏幕宽度
     */
    protected var mScreenWidth = 0

    /**
     * 状态栏高度
     */
    protected var mStatusHeight = 0

    /**
     * 标题布局
     */
    private lateinit var mTitleLayout: RelativeLayout

    /**
     * 页面加载过程布局
     */
    protected lateinit var mLoadingLayout: LoadingLayout

    /**
     * 标题左边布局
     */
    protected lateinit var mTitleLeftRelativeLayout: RelativeLayout

    /**
     * 标题右边布局
     */
    protected lateinit var mTitleRightRelativeLayout: RelativeLayout

    /**
     * 标题中间布局
     */
    protected lateinit var mTitleMiddleRelativeLayout: RelativeLayout

    /**
     * 标题左边图片
     */
    private lateinit var mTitleLeftImageView: ImageView

    /**
     * 标题中间文字
     */
    private lateinit var mTitleMiddleTextView: TextView

    /**
     * 标题右边图片
     */
    private lateinit var mTitleRightImageView: ImageView

    /**
     * 标题右边文字
     */
    private lateinit var mTitleRightTextView: TextView

    /**
     * 全局dialog
     */
    protected lateinit var loadingDialog: LoadingDialog
    protected var pageSize = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        onCreateBefore()
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //设置固定竖屏
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        // 添加当前Activity到所有管理列表
        container()
        init()
        ActivityManager.getInstance().addActivity(this)
        mLoadingLayout.setOnReloadListener { reLoadData() }
    }

    /**
     * 在setContentView之前的一些操作
     */
    protected fun onCreateBefore() {
        mContext = this
        onViewCreated()
        mScreenWidth = PhoneUtil.getScreenWidth(this)
        mStatusHeight = PhoneUtil.getStatusHeight(this)
    }

    protected open fun onViewCreated() {}

    /**
     * 设置Activity的布局
     */
    private fun container() {
        val inflater = LayoutInflater.from(this)
        mBaseContainer = inflater.inflate(R.layout.base_container, null) as RelativeLayout
        val view = inflater.inflate(setBaseContentView(), null)
        //如果版本高于安卓4.4则透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //安卓4.4
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            val tintManager = SystemBarTintManager(this)
            tintManager.isStatusBarTintEnabled = true
            tintManager.setStatusBarTintColor(if (statusColor == -1) ContextCompat.getColor(mContext, R.color.black) else statusColor)
        }
        if (!isNotFillSysState) {
            mBaseContainer.clipToPadding = false
            mBaseContainer.fitsSystemWindows = false
        }

        //标题布局控件
        mTitleLayout = inflater.inflate(R.layout.title_layout, null) as RelativeLayout
        mTitleLeftRelativeLayout = mTitleLayout.findViewById(R.id.title_layout_left)
        mTitleRightRelativeLayout = mTitleLayout.findViewById(R.id.title_layout_right)
        mTitleMiddleRelativeLayout = mTitleLayout.findViewById(R.id.title_layout_middle)
        mTitleLeftImageView = mTitleLayout.findViewById(R.id.title_layout_left_image)
        mTitleMiddleTextView = mTitleLayout.findViewById(R.id.title_layout_middle_tv)
        mTitleRightImageView = mTitleLayout.findViewById(R.id.title_layout_right_image)
        mTitleRightTextView = mTitleLayout.findViewById(R.id.title_layout_right_tv)
        //除标题外的布局参数
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        //标题布局参数
        val mParamsRelativeMW = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        //是否显示标题
        if (isShowTitle) {
            //如果需要填充标题栏,则标题栏顶部空出状态栏
            if (!isNotFillSysState && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mTitleLayout.setPadding(0, mStatusHeight, 0, 0)
            }
            //添加标题栏
            mBaseContainer.addView(mTitleLayout, mParamsRelativeMW)
            //添加左边点击事件
            mTitleLeftRelativeLayout.setOnClickListener(leftClickListener)
            //添加右边点击事件
            mTitleRightRelativeLayout.setOnClickListener(rightClickListener)
            //需要使用mTitleLayout.getId(),先要在XML中给mTitleLayout控件添加ID
            params.addRule(RelativeLayout.BELOW, mTitleLayout.id)
        }
        //loadingDialog 页面加载中dialog
        loadingDialog = dialogCreate()
        //loading 布局,页面状态展示
        mLoadingLayout = inflater.inflate(R.layout.loading_layout, null) as LoadingLayout
        //是否使用loading框架,自定义标题栏的或其他特殊需求的可设为false,然后到具体的界面去实现LoadingLayout
        if (isUseLoading && isShowTitle) {
            mLoadingLayout.addView(view, 0) //自定义的界面加载到最底层
            mBaseContainer.addView(mLoadingLayout, params)
        } else {
            mBaseContainer.addView(view, params)
        }
        setContentView(mBaseContainer)
    }

    /**
     * 设置活动的布局ID
     * @return
     */
    abstract fun setBaseContentView(): Int

    /**
     * 初始化
     */
    abstract fun init()
    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.getInstance().finishActivity(this)
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        //执行Activity切换动画
    }

    override fun finish() {
        super.finish()
        KeyboardUtil.closeKeyboard(this)
        //执行Activity切换动画
    }

    /**
     * 加载菊花提示
     * @return
     */
    fun dialogCreate(): LoadingDialog {
        return LoadingDialog.Builder(this).create()
    }

    /**
     * Toast的显示(默认位置)
     * @param message 需要显示的信息
     */
    fun showToast(message: CharSequence?) {
        ToastUtil.showToast(message)
    }

    /**
     * 设置状态栏背景色(需要在onCreateBefore中才有效)
     * @param statusColor 需要设置的状态栏颜色
     */
    fun setStatusColor(statusColor: Int) {
        this.statusColor = statusColor
    }

    /**
     * 设置是否需要填充状态栏
     * @param isNotFillSysState 需要View填充到状态栏设为false
     */
    fun setIsNotFillSysState(isNotFillSysState: Boolean) {
        this.isNotFillSysState = isNotFillSysState
    }

    /**
     * 设置是否显示标题,如需设置需要在onCreateBefore方法执行
     * @param isShowTitle
     */
    fun setIsShowTitle(isShowTitle: Boolean) {
        this.isShowTitle = isShowTitle
    }

    /**
     * 设置是否使用loading布局,如需设置需要在onCreateBefore方法执行
     * @param isUseLoading
     */
    fun setIsUseLoading(isUseLoading: Boolean) {
        this.isUseLoading = isUseLoading
    }

    /**
     * 左边点击监听
     */
    private val leftClickListener: View.OnClickListener = View.OnClickListener { view -> onClickLeft(view) }

    /**
     * 右边点击监听
     */
    private val rightClickListener: View.OnClickListener = View.OnClickListener { view -> onClickRight(view) }

    /**
     * 标题左边点击事件
     * @param view 点击的View
     */
    open fun onClickLeft(view: View) {
        onBackPressed()
    }

    /**
     * 标题右边点击事件
     * @param view 点击的View
     */
    open fun onClickRight(view: View) {}

    /**
     * 获取LoadingLayout控件
     */
    fun getmLoadingLayout(): LoadingLayout {
        return mLoadingLayout
    }

    /**
     * 设置标题左边图片
     * @param resId 图片资源
     */
    fun setTitleLeftImage(resId: Int) {
        mTitleLeftImageView.setImageResource(resId)
    }

    /**
     * 设置中间标题
     * @param title
     */
    fun setTitleMiddleText(title: String?) {
        if (!TextUtils.isEmpty(title)) {
            mTitleMiddleTextView.text = title
        }
    }

    /**
     * 设置标题右边图片
     * @param resId 图片资源
     */
    fun setTitleRightImage(resId: Int) {
        mTitleRightImageView.setImageResource(resId)
        mTitleRightRelativeLayout.visibility = View.VISIBLE
    }

    /**
     * 设置右边文字
     * @param title
     */
    fun setTitleRightText(title: String?) {
        if (!TextUtils.isEmpty(title)) {
            mTitleRightTextView.text = title
            mTitleRightRelativeLayout.visibility = View.VISIBLE
        }
    }

    /**
     * 获取标题左边ImageView
     * @return 左边ImageView
     */
    fun getmTitleLeftImageView(): ImageView {
        return mTitleLeftImageView
    }

    /**
     * 获取中间TextView
     * @return 左边TextView
     */
    fun getmTitleMiddleTextView(): TextView {
        return mTitleMiddleTextView
    }

    /**
     * 获取标题右边ImageView
     * @return 右边ImageView
     */
    fun getmTitleRightImageView(): ImageView {
        return mTitleRightImageView
    }

    /**
     * 获取右边TextView
     * @return 右边TextView
     */
    fun getmTitleRightTextView(): TextView {
        return mTitleRightTextView
    }

    /**
     * 获取标题栏
     * @return 标题栏布局
     */
    fun getmTitleLayout(): RelativeLayout {
        return mTitleLayout
    }

    /**
     * 获取标题右边布局
     * @return 标题右边布局
     */
    fun getmTitleRightRelativeLayout(): RelativeLayout {
        return mTitleRightRelativeLayout
    }

    /**
     * 获取RecycleView无数据时填充的emptyView
     * @return emptyView
     */
    val adapterEmptyView: View
        get() = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_empty_view, null)

    /**
     * 重试处理，需重写的处理方法
     */
    open fun reLoadData() {}

    fun checkNetStatue(): Boolean {
        if (!NetworkUtil.isConnect(mContext) || !NetworkUtil.isAvailable(mContext)) {
            ToastUtil.showToast("请检查网络设置")
            return false
        }
        return true
    }

    companion object {
        const val PULL_DOWN_TIME: Long = 1000
        const val PULL_UP_TIME: Long = 1000
    }
}