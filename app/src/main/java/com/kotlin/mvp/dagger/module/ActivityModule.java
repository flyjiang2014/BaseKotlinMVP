package com.kotlin.mvp.dagger.module;

import android.app.Activity;

import com.kotlin.mvp.dagger.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by flyjiang on 2019/8/2.
 */

@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityScope
    public Activity provideActivity() {
        return mActivity;
    }
}
