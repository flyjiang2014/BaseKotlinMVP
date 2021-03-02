package com.kotlin.mvp.dagger.component;

import com.kotlin.mvp.app.App;
import com.kotlin.mvp.dagger.module.AppModule;
import com.kotlin.mvp.dagger.module.HttpModule;
import com.kotlin.mvp.http.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 *Created by flyjiang on 2019/8/2.
 */

@Singleton
@Component(modules = {AppModule.class, HttpModule.class})
public interface AppComponent {

    App getContext();  // 提供App的Context

    RetrofitHelper retrofitHelper();  //提供http的帮助类
}
