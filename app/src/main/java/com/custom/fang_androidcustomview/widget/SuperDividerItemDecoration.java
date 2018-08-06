package com.custom.fang_androidcustomview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

public class SuperDividerItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private int orientation;

    /**
     * 是否显示列表最后一条分割线
     */
    private boolean dividerIsShowLastDivide=true;

    /**
     * 分割线开始的位置（解决recyclerview添加头布局的时候，要从header下边的position位置算起
     */
    private int dividerFromPosition = 0;

    /**
     * 分割线的画笔
     */
    private Paint dividerPaint;

    /**
     * 分割线的颜色
     */
    private int dividerColor;

    /**
     * 分割线的高度
     */
    private int dividerWidth;

    /**
     * 分割线距离左边的距离
     */
    private int dividerPaddingLeft;

    /**
     * 分割线距离右边的距离
     */
    private int dividerPaddingRight;

    /**
     * 分割线距离上边的距离
     */
    private int dividerPaddingTop;

    /**
     * 分割线距离下边的距离
     */
    private int dividerPaddingBottom;

    private static Context context;


    public SuperDividerItemDecoration(Builder builder) {
        context = builder.context;

        orientation=builder.orientation;
        dividerWidth=dp2px(builder.dividerWidth);
        dividerColor=builder.dividerColor;
        dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dividerPaint.setColor(dividerColor);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (orientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        int count = parent.getChildCount();
        if (count > 0) {
            int showCount = dividerIsShowLastDivide ? count : count - 1;
            for (int i = dividerFromPosition; i < showCount; i++) {
                View view = parent.getChildAt(i);
                int itemBottom = view.getBottom();
                int left = parent.getPaddingLeft() + dividerPaddingLeft;
                int top = itemBottom;
                int right = parent.getWidth() - parent.getPaddingRight() - dividerPaddingRight;
                int bottom = itemBottom + dividerWidth;
                c.drawRect(left, top, right, bottom, dividerPaint);
            }
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int count = parent.getChildCount();
        if (count > 0) {
            int showCount = dividerIsShowLastDivide ? count : count - 1;
            for (int i = dividerFromPosition; i < showCount; i++) {
                View view = parent.getChildAt(i);
                int left = view.getRight();
                int top = parent.getPaddingTop() + dividerPaddingTop;
                int right = left + dividerWidth;
                int bottom = parent.getHeight() - parent.getPaddingBottom() - dividerPaddingBottom;
                c.drawRect(left, top, right, bottom, dividerPaint);
            }
        }
    }

    /**
     * @param outRect 给item预留出的空间，用来绘制分割线
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (orientation == VERTICAL) {
            //在item的下方空出dividerWidth的高度
            outRect.bottom = dividerWidth;
        } else {
            //在item的右侧空出dividerWidth的高度
            outRect.right = dividerWidth;
        }
    }

    /**
     * dp转成px
     *
     * @param dpVal
     * @return
     */
    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    public static class Builder {

        private Context context;
        private int dividerWidth;
        private int dividerColor;
        private int dividerPadding;
        private int dividerPaddingLeft;
        private int dividerPaddingRight;
        private int dividerPaddingTop;
        private int dividerPaddingBottom;
        private int dividerFromPosition;
        private boolean dividerIsShowLastDivide;
        private int orientation=VERTICAL;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setDividerWidth(int dividerWidth) {
            this.dividerWidth = dividerWidth;
            return this;
        }

        public Builder setDividerColor(int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }

        public SuperDividerItemDecoration build() {
            return new SuperDividerItemDecoration(this);
        }
    }
}
