package com.kotlin.mvp.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 作者：flyjiang
 * 说明: 
 */
public class ToastUtil {
    private static Toast mToast = null;

    /**
     * Toast的初始化
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        if (mToast == null) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Toast的显示(默认位置)
     *
     * @param message 需要显示的信息
     */
    public static void showToast(CharSequence message) {
        if (mToast != null) {
            mToast.setText(message);
            mToast.show();
        } else {
            throw new NullPointerException("请执行ToastUtil.init");
        }
    }

    /**
     * Toast的显示(指定位置)
     *
     * @param message  需要显示的信息
     * @param position 需要显示的位置(Gravity.CENTER、Gravity.BOTTOM)
     */
    public static void showToast(CharSequence message, int position) {
        if (mToast != null) {
            mToast.setText(message);
            mToast.setGravity(position, 0, 0);
            mToast.show();
        } else {
            throw new NullPointerException("请执行ToastUtil.init");
        }
    }

    /**
     * Toast的显示(指定位置)
     *
     * @param message  需要显示的信息
     * @param position 需要显示的位置(Gravity.CENTER、Gravity.BOTTOM)
     * @param x        X轴的偏移量
     * @param y        Y轴的偏移量
     */
    public static void showToast(CharSequence message, int position, int x, int y) {
        if (mToast != null) {
            mToast.setText(message);
            mToast.setGravity(position, x, y);
            mToast.show();
        } else {
            throw new NullPointerException("请执行ToastUtil.init");
        }
    }
}
