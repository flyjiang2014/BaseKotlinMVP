package com.kotlin.mvp.widget;

/**
 * 作者：flyjiang
 * 说明: 初始化实现方法接口
 */
public interface InitializeStep {

    void initData();

    void initView();

    void initViewData();

    void initViewListener();
}
