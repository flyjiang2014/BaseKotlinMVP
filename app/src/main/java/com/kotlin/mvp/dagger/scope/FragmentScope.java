package com.kotlin.mvp.dagger.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by flyjiang on 2019/8/2.
 */

@Scope
@Retention(RUNTIME)
public @interface FragmentScope {
}
