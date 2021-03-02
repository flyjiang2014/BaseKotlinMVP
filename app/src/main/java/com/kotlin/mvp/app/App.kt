package com.kotlin.mvp.app
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.kotlin.mvp.R
import com.kotlin.mvp.dagger.component.AppComponent
import com.kotlin.mvp.dagger.component.DaggerAppComponent
import com.kotlin.mvp.dagger.module.AppModule
import com.kotlin.mvp.dagger.module.HttpModule
import com.kotlin.mvp.utils.DynamicTimeFormat
import com.kotlin.mvp.utils.FileUtil
import com.kotlin.mvp.utils.SharepreferenceUtil
import com.kotlin.mvp.utils.ToastUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader

/**
 * Created by  on 2020/11/16.
 * 文件说明：
 */
class App :Application() {
   private var appComponent: AppComponent? = null
    companion object {
        lateinit var instance: App
        fun getAppComponent(): AppComponent? {
            if (instance.appComponent == null) {
                instance.appComponent = DaggerAppComponent.builder()
                        .appModule(AppModule(instance))
                        .httpModule(HttpModule())
                        .build()
            }
            return instance.appComponent
        }
    }
    init{
        //设置刷新控件刷新head and foot
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true) //启用矢量图兼容
        ClassicsFooter.REFRESH_FOOTER_NOTHING = "已加载全部数据"
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.bg_app, R.color.text_grey_dark) //全局设置主题颜色
            ClassicsHeader(context).setTimeFormat(DynamicTimeFormat("更新于 %s"))
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setEnableLoadMoreWhenContentNotFull(false)
            ClassicsFooter(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreate() {
        super.onCreate()
        instance = this
        ToastUtil.init(applicationContext) //初始化Toast
        SharepreferenceUtil.init(applicationContext) //初始化Sharepreference
        FileUtil.init(applicationContext)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}