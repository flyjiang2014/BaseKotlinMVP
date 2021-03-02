package com.kotlin.mvp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.kotlin.mvp.R;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;


/**
 * Created by ${flyjiang} on 2017/3/29.
 * 文件说明：RecyclerView LinearLayoutManager的分割线
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private Drawable mDivider;
    private int mOrientation;
    private boolean hasHeadView; //列表是否有headView


    public DividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    /**
     * drawable样式分割线
     * @param context
     * @param orientation 列表方向
     * @param drawableId  自定义drawable样式
     */
    public DividerItemDecoration(Context context, int orientation, int drawableId) {
        mDivider = context.getResources().getDrawable(drawableId);
        setOrientation(orientation);
    }

    /**
     * drawable样式分割线（默认纵向）
     * @param context
     * @param drawableId  自定义drawable样式
     */
    public DividerItemDecoration(int drawableId,Context context) {
        mDivider = context.getResources().getDrawable(drawableId);
        setOrientation(VERTICAL_LIST);
    }
    /**
     * drawable样式分割线（默认纵向且样式固定为自定义的某个drawable）
     * @param context
     */
    public DividerItemDecoration(Context context) {
        mDivider = context.getResources().getDrawable(R.drawable.divider_drawable01);
        setOrientation(VERTICAL_LIST);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    public DividerItemDecoration setHasHeadView(boolean hasHeadView) {
        this.hasHeadView = hasHeadView;
        return this;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {

        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
           RecyclerView v = new RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
             int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
      //  (hasHeadView&&itemPosition==0) headView作为第一个item时，不要分割线
        if ( !(hasHeadView&&itemPosition==0)&&itemPosition + 1 < parent.getAdapter().getItemCount()) { //除去最后一个item.
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }
    }
}