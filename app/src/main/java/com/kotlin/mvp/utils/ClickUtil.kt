package com.kotlin.mvp.utils

/**
 * Created by ${flyjiang} on 2016/8/23.
 * 文件说明：防止快速多次点击按钮工具类
 */
object ClickUtil {
    private var lastClickTime: Long = 0

    fun isFastDoubleClick():Boolean {
        val time = System.currentTimeMillis()
        val timeD = time - lastClickTime
        if (timeD in 1..1000) {
            return true
        }
        lastClickTime = time
        return false
    }
}