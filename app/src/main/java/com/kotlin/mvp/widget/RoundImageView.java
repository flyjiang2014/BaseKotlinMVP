package com.kotlin.mvp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by  on 2020/6/18.
 * 文件说明：
 */
public class RoundImageView extends AppCompatImageView {


    private Path mPath;
    private RectF mRectF;
    private float ridValue = dp2px(2);//默认2dp
    private float[] rids = {};

    public RoundImageView(Context context) {
        this(context,null);

    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPath= new Path();
        mRectF= new RectF();
        rids = new float[]{ridValue,ridValue,ridValue,ridValue,ridValue, ridValue, ridValue, ridValue,}; /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
    }

    private   int dp2px(final float dpValue) {
        final float scale = this.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 画图
     * @param canvas
     */
    protected void onDraw(Canvas canvas) {
        int w = this.getWidth();
        int h = this.getHeight();
        mRectF.set(0, 0, w, h);
        mPath.addRoundRect(mRectF, rids, Path.Direction.CW);
        canvas.clipPath(mPath);
        super.onDraw(canvas);
    }

    public void setRidValue(float ridValue) {
        this.ridValue = ridValue;
        rids = new float[]{ridValue,ridValue,ridValue,ridValue,ridValue, ridValue, ridValue, ridValue,};
    }

    public void setRids(float[] rids) {
        this.rids = rids;
    }
}

