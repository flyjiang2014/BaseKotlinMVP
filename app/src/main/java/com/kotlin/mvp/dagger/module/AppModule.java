package com.kotlin.mvp.dagger.module;

import com.kotlin.mvp.app.App;
import com.kotlin.mvp.http.RetrofitHelper;
import com.kotlin.mvp.http.api.Apis;
import com.kotlin.mvp.http.api.FileUpApis;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by flyjiang on 2019/8/2.
 */

@Module
public class AppModule {
    private final App application;

    public AppModule(App application) {
        this.application = application;
    }

    @Provides
    @Singleton
    App provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    RetrofitHelper provideRetrofitHelper(Apis apis, FileUpApis fileUpApis) {
        return new RetrofitHelper(apis,fileUpApis);
    }
}
