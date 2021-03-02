package com.kotlin.mvp.widget.loading;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

/**
 * create by flyjiang
 * on date 2016-01-06
 * TODO
 */

public class Utils {

    public static Drawable getDrawable(Context context, @DrawableRes int id){
        return ContextCompat.getDrawable(context,id);
    }

    public static int getColor(Context context, @ColorRes int id){
        return  ContextCompat.getColor(context,id);
    }

    public static String getString(Context context, @StringRes int id){
        return  context.getResources().getString(id);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics()
                .scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int dp2px(Context context, int dip) {
        final float scale =  context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public static <T extends View> T findViewById(View v, int id) {
        return (T) v.findViewById(id);
    }

}