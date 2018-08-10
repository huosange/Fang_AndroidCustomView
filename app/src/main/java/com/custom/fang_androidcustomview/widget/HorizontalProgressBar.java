package com.custom.fang_androidcustomview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * 自定义水平进度条
 */
public class HorizontalProgressBar extends View {

    /**
     * 进度画笔
     */
    private Paint progressPaint;

    /**
     * 进度条画笔的宽度
     */
    private int progressPaintWidth;

    /**
     * 进度条颜色
     */
    private int progressColor = 0xFFf66b12;

    /**
     * 背景画笔
     */
    private Paint bgPaint;

    /**
     * 底色
     */
    private int bgColor = 0xFFe1e5e8;

    /**
     * 提示框画笔
     */
    private Paint tipPaint;

    /**
     * 提示框画笔的宽度
     */
    private int tipPaintWidth;

    /**
     * 提示框文字的画笔
     */
    private Paint textPaint;

    /**
     * 提示框文字的字体大小
     */
    private int textPaintSize;

    /**
     * 动画执行时间
     */
    private int duration = 5000;

    /**
     * 动画延时启动时间
     */
    private int startDelay = 0;

    /**
     * 提示框文字的矩形
     */
    private Rect textRect = new Rect();

    /**
     * 绘制提示框的矩形
     */
    private RectF rectF = new RectF();

    /**
     * 控件宽度
     */
    private int mWidth;

    /**
     * 百分比提示框的高度
     */
    private int tipHeight;

    /**
     * 百分比提示框的宽度
     */
    private int tipWidth;

    /**
     * 当前进度
     */
    private float currentProgress;

    /**
     * 画三角形的path
     */
    private Path path = new Path();

    /**
     * 三角形的高度
     */
    private int triangleHeight;

    /**
     * 进度条距离提示框的高度
     */
    private int progressMarginTop;

    /**
     * 进度条移动的距离
     */
    private float moveDis;

    /**
     * 圆角矩形的圆角半径
     */
    private int roundRectRadius;


    /**
     * 进度的属性动画
     */
    private ValueAnimator progressAnimation;

    /**
     * 终止时的进度
     */
    private float mProgress;

    /**
     * 提示框的内容
     */
    private String textString = "0";

    private ProgressListener progressListener;


    public HorizontalProgressBar(Context context) {
        super(context);
    }

    public HorizontalProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initPaint();
    }

    private void init() {
        progressPaintWidth = dp2px(4);

        tipHeight = dp2px(15);
        tipWidth = dp2px(30);
        tipPaintWidth = dp2px(1);

        textPaintSize = sp2px(10);

        roundRectRadius = dp2px(2);

        triangleHeight = dp2px(3);
        progressMarginTop = dp2px(8);
    }

    private void initPaint() {
        bgPaint = getPaint(progressPaintWidth, bgColor, Paint.Style.STROKE);
        progressPaint = getPaint(progressPaintWidth, progressColor, Paint.Style.STROKE);
        tipPaint = getPaint(tipPaintWidth, progressColor, Paint.Style.FILL);
        initTextPaint();
    }

    /**
     * 初始化文字画笔
     */
    private void initTextPaint() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textPaintSize);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    private Paint getPaint(int strokeWidth, int color, Paint.Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(style);
        return paint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(getPaddingLeft(), tipHeight + progressMarginTop, mWidth, tipHeight + progressMarginTop, bgPaint);

        canvas.drawLine(getPaddingLeft(), tipHeight + progressMarginTop, currentProgress, tipHeight + progressMarginTop, progressPaint);

        drawTipView(canvas);
        drawText(canvas, textString);
    }

    /**
     * 绘制进度条上边提示百分比的view
     */
    private void drawTipView(Canvas canvas) {
        drawRoundRect(canvas);
        drawTriangle(canvas);
    }

    private void drawRoundRect(Canvas canvas) {
        rectF.set(moveDis, 0, tipWidth + moveDis, tipHeight);
        canvas.drawRoundRect(rectF, roundRectRadius, roundRectRadius, tipPaint);
    }

    private void drawTriangle(Canvas canvas) {
        path.moveTo(tipWidth / 2 - triangleHeight + moveDis, tipHeight);
        path.lineTo(tipWidth / 2 + moveDis, tipHeight + triangleHeight);
        path.lineTo(tipWidth / 2 + triangleHeight + moveDis, tipHeight);
        canvas.drawPath(path, tipPaint);
        path.reset();
    }

    private void drawText(Canvas canvas, String textString) {
        textRect.left = (int) moveDis;
        textRect.top = 0;
        textRect.right = (int) (tipWidth + moveDis);
        textRect.bottom = tipHeight;
        Paint.FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
        int baseline = (textRect.top + textRect.bottom - fontMetricsInt.top - fontMetricsInt.bottom) / 2;
        canvas.drawText(textString + "%", textRect.centerX(), baseline, textPaint);
    }

    private void initAnimation() {
        progressAnimation = ValueAnimator.ofFloat(0, mProgress);
        progressAnimation.setDuration(duration);
        progressAnimation.setStartDelay(startDelay);
        progressAnimation.setInterpolator(new LinearInterpolator());
        progressAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                textString = format2Int(value) + "";
                currentProgress = value * mWidth / 100;
                if (progressListener != null) {
                    progressListener.currentProgressListener(value);
                }
                if (currentProgress >= tipWidth / 2 && currentProgress <= mWidth - tipWidth / 2) {
                    moveDis = currentProgress - tipWidth / 2;
                }
                invalidate();
            }
        });
    }

    public HorizontalProgressBar setProgressWithAnimation(float progress) {
        mProgress = progress;
        initAnimation();
        return this;
    }

    /**
     * 开启动画
     */
    public void startProgressAnimation() {
        if (progressAnimation != null && !progressAnimation.isRunning()
                && !progressAnimation.isStarted()) {
            progressAnimation.start();
        }
    }

    /**
     * 回调接口
     */
    public interface ProgressListener {
        void currentProgressListener(float currentProgress);
    }

    public HorizontalProgressBar setProgressListener(ProgressListener listener) {
        this.progressListener = listener;
        return this;
    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }

    public static int format2Int(double i) {
        return (int) i;
    }

}
