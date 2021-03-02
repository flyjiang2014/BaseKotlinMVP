package com.kotlin.mvp.dagger.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by flyjiang on 2019/12/20.
 * 图片上传url注解定义
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface FileUpUrl {
}