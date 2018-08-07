package com.custom.fang_androidcustomview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 悬停吸顶效果
 */
public class HoverItemDecoration extends RecyclerView.ItemDecoration {

    private Context context;
    private BindItemTextCallback bindItemTextCallback;

    private int width;

    /**
     * 分组text距离左边的距离
     */
    private int itemTextPaddingLeft;

    /**
     * 分组item的高度
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
     * 分组item的画笔
     */
    private Paint itemPaint;

    /**
     * 分组item的颜色
     */
    private int itemHoverPaintColor = 0xFFf4f4f4;

    private Paint itemHoverPaint;

    private Paint textPaint;

    public HoverItemDecoration(Context context, BindItemTextCallback bindItemTextCallback) {
        this.context = context;
        this.bindItemTextCallback = bindItemTextCallback;

        width = context.getResources().getDisplayMetrics().widthPixels;

        itemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        itemPaint.setColor(itemHoverPaintColor);
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
                c.drawRect(0, itemTop, width, itemBottom, itemPaint);
                drawText(c, itemTop, itemBottom, text);
            } else {

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
     * 绘制文字
     *
     * @param canvas
     * @param itemTop
     * @param itemBottom
     * @param textString
     */
    private void drawText(Canvas canvas, int itemTop, int itemBottom, String textString) {
        textRect.left = itemTextPaddingLeft;
        textRect.top = itemTop;
        textRect.right = textString.length();
        textRect.bottom = itemBottom;

        Paint.FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
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
}
