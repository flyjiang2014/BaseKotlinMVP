package com.kotlin.mvp.presenter
import android.Manifest
import com.kotlin.mvp.base.RxPresenter
import com.kotlin.mvp.contract.MainContract
import com.kotlin.mvp.http.RetrofitHelper
import com.kotlin.mvp.http.api.CommonSubscriber
import com.kotlin.mvp.http.api.RxUtil
import com.kotlin.mvp.http.response.HttpResponse
import com.kotlin.mvp.model.IndexShowBean
import com.tbruyelle.rxpermissions2.RxPermissions
import javax.inject.Inject
/**
 * Created by  on 2020/12/9.
 * 文件说明：首页
 */
class MainPresenter @Inject constructor(var retrofitHelper: RetrofitHelper) : RxPresenter<MainContract.View>(), MainContract.Presenter {

    /**
     * 页面数据获取
     */
    override fun getIndexData() {
        addSubscribeWithDialog(retrofitHelper.fetchIndexData()
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(object : CommonSubscriber<HttpResponse<IndexShowBean>>(mView, true) {
                    override fun onNext(httpResponse: HttpResponse<IndexShowBean>) {
                        super.onNext(httpResponse)
                        if (1 == httpResponse.success) {
                            mView.showContent(httpResponse.data)
                        }
                    }
                })
        )
    }

    /**
     * 危险权限申请
     */
    override fun permissionApply(rxPermissions: RxPermissions) {
        addSubscribe(rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { permission ->
                    when {
                        permission.granted -> {
                            // 用户已经同意该权限
                        }
                        permission.shouldShowRequestPermissionRationale -> {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        }
                        else -> {
                            // 用户拒绝了该权限，并且选中『不再询问』
                        }
                    }
                }
        )
    }
}