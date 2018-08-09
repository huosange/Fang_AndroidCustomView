package com.custom.fang_androidcustomview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * 悬停吸顶效果
 */
public class HoverItemDecoration extends RecyclerView.ItemDecoration {

    private Context context;

    /**
     * 获取点击位置的文本
     */
    private BindItemTextCallback bindItemTextCallback;

    /**
     * 屏幕的宽度，单位px
     */
    private int width;

    /**
     * 分组文本距离左边的距离
     */
    private int itemTextPaddingLeft;

    /**
     * 分组的高度
     */
    private int itemHeight;

    /**
     * 分割线的高度
     */
    private int itemDivideHeight;

    /**
     * 绘制文字的矩形边框
     */
    private Rect textRect = new Rect();


    /**
     * 分组的背景画笔
     */
    private Paint groupBgPaint;

    /**
     * 分组的内容画笔
     */
    private Paint groupContentPaint;

    /**
     * 同组中item之间的分隔线画笔
     */
    private Paint dividerPaint;


    public HoverItemDecoration(Context context, BindItemTextCallback bindItemTextCallback) {
        this.context = context;
        this.bindItemTextCallback = bindItemTextCallback;

        width = context.getResources().getDisplayMetrics().widthPixels;

        itemHeight = dp2px(30);
        itemTextPaddingLeft = dp2px(20);
        itemDivideHeight = dp2px(1);

        groupBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        groupBgPaint.setColor(Color.parseColor("#ff00ff00"));

        groupContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        groupContentPaint.setColor(Color.parseColor("#80ff0000"));
        groupContentPaint.setTextSize(sp2px(15));

        dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dividerPaint.setColor(Color.parseColor("#800000ff"));

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);
            //分组item的顶部和底部
            int itemTop = view.getTop() - itemHeight;
            int itemBottom = view.getTop();
            //可见item在adapter中真实的位置
            int position = parent.getChildAdapterPosition(view);
            //获取回调的分组文字（一般是字母）
            String text = bindItemTextCallback.getItemText(position);
            if (isFirstInGroup(position)) {
                c.drawRect(0, itemTop, width, itemBottom, groupBgPaint);
                drawText(c, itemTop, itemBottom, text);
            } else {
                c.drawRect(0, view.getTop() - itemDivideHeight, width, view.getTop(), dividerPaint);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        //绘制悬停的view
        int count = parent.getChildCount();
        if (count > 0) {
            //悬停只是第一个位置悬停
            //获取第一条可见的item
            View firstView = parent.getChildAt(0);
            //获取此view在整个adapter中的位置
            int position = parent.getChildAdapterPosition(firstView);
            String text = bindItemTextCallback.getItemText(position);
            if (firstView.getBottom() <= itemHeight && isFirstInGroup(position + 1)) {
                c.drawRect(0, 0, width, firstView.getBottom(), groupBgPaint);
                drawText(c, firstView.getBottom() - itemHeight, firstView.getBottom(), text);
            } else {
                c.drawRect(0, 0, width, itemHeight, groupBgPaint);
                drawText(c, 0, itemHeight, text);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        //如果是分组第一个，就留出绘制item的高度
        if (isFirstInGroup(position)) {
            outRect.top = itemHeight;
        } else {
            outRect.top = itemDivideHeight;
        }
    }

    /**
     * 绘制首字母
     *
     * @param canvas
     * @param itemTop
     * @param itemBottom
     * @param textString
     */
    private void drawText(Canvas canvas, int itemTop, int itemBottom, String textString) {
        textRect.left = itemTextPaddingLeft;
        textRect.top = itemTop;
        textRect.bottom = itemBottom;

        Paint.FontMetricsInt fontMetricsInt = groupContentPaint.getFontMetricsInt();
        int baseline = (textRect.bottom + textRect.top - fontMetricsInt.top - fontMetricsInt.bottom) / 2;
        canvas.drawText(textString, textRect.left, baseline, groupContentPaint);
    }

    private boolean isFirstInGroup(int position) {
        if (position == 0) {
            return true;
        } else {
            String prevItemText = bindItemTextCallback.getItemText(position - 1);
            String currentItemText = bindItemTextCallback.getItemText(position);
            //上一个和当前位置的值一样，说明是同一个组的，否则就是新的一组
            if (prevItemText.equals(currentItemText)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public interface BindItemTextCallback {
        String getItemText(int position);
    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }
}
