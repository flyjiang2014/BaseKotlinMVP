package com.kotlin.mvp.dagger.component;

import android.app.Activity;

import com.kotlin.mvp.dagger.module.ActivityModule;
import com.kotlin.mvp.dagger.scope.ActivityScope;
import com.kotlin.mvp.view.MainActivity;

import dagger.Component;

/**
 * Created by flyjiang on 2019/8/2.
 */

@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();
    void inject(MainActivity activity);
}
