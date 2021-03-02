package com.kotlin.mvp.dagger.component;

import android.app.Activity;

import com.kotlin.mvp.dagger.module.FragmentModule;
import com.kotlin.mvp.dagger.scope.FragmentScope;

import dagger.Component;

/**
 * Created by flyjiang on 2019/8/2.
 */

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    Activity getActivity();
}
