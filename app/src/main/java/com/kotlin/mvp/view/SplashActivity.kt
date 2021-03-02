package com.kotlin.mvp.view

import android.content.Intent
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.kotlin.mvp.R
import com.kotlin.mvp.app.Constants
import com.kotlin.mvp.base.SimpleActivity
import com.kotlin.mvp.utils.DeviceIdUtil
import com.kotlin.mvp.utils.SharepreferenceUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 *欢迎页
 */
class SplashActivity : SimpleActivity() {
    companion object {
        private const val GUIDE_FLAG = "1"
    }
    private var mDisposable: Disposable? = null
    override fun setBaseContentView(): Int {
        setIsShowTitle(false)
        setStatusColor(ContextCompat.getColor(mContext,R.color.transparent))
        return R.layout.activity_splash_layout
    }

    override fun init() {
        if (!isTaskRoot) { //防止应用最小化后，重新打开，重启欢迎页
            finish()
        }
        mDisposable = Observable.timer(1, TimeUnit.SECONDS) //延时1秒执行
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) //
                .subscribe {
                    val intent = Intent()
                    if (!SharepreferenceUtil.getBoolean(Constants.APP_FIRST_IN)) {
                        intent.setClass(mContext, WelcomeActivity::class.java)
                    } else {
                        intent.setClass(mContext, MainActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                }
        SharepreferenceUtil.saveString(Constants.KDD_ANDROID_ID, DeviceIdUtil.getDeviceId(mContext))//保存设备id
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDisposable?.isDisposed ==false) {
            mDisposable?.dispose()
        }
    }
}