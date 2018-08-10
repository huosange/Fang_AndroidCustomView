package com.custom.fang_androidcustomview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.custom.fang_androidcustomview.R;

public class CircleProgressBarView extends View {

    private Context mContext;
    private ProgressListener progressListener;
    private float mProgress;

    /**
     * 当前进度
     */
    private float currentProgress;

    /**
     * 进度的属性动画
     */
    private ValueAnimator progressAnimation;

    /**
     * 动画延时启动时间
     */
    private int startDelay = 500;

    /**
     * 扇形所在矩形
     */
    private RectF rectF = new RectF();

    private float centerX;
    private float centerY;
    private float radius;

    private int defaultStrokeWidth = 10;

    private Paint circleBgPaint;
    private Paint progressPaint;
    private Paint centerProgressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //****************自定义属性**********************
    private int circleBgStrokeWidth;
    private int progressStrokeWidth;
    private int circleBgColor = 0xFFe1e5e8;
    private int progressColor = 0xFFf66b12;
    private int circleAnimationDuration = 1000;
    private boolean isDrawCenterProgressText;
    private int centerProgressTextColor = Color.BLACK;
    private int centerProgressTextSize = 10;
    //**************************************

    public CircleProgressBarView(Context context) {
        this(context, null);
    }

    public CircleProgressBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        getAttr(attrs);
        initPaint();
        initTextPaint();
    }

    private void getAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CircleProgressBarView);

        circleBgStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.CircleProgressBarView_cirlceBgStrokeWidth, defaultStrokeWidth);
        progressStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.CircleProgressBarView_progressStrokeWidth, defaultStrokeWidth);

        circleBgColor = typedArray.getColor(R.styleable.CircleProgressBarView_circleBgColor, circleBgColor);
        progressColor = typedArray.getColor(R.styleable.CircleProgressBarView_progressColor, progressColor);

        circleAnimationDuration = typedArray.getInteger(R.styleable.CircleProgressBarView_circleAnimationDuration, circleAnimationDuration);
        isDrawCenterProgressText = typedArray.getBoolean(R.styleable.CircleProgressBarView_isDrawCenterProgressText, false);

        centerProgressTextColor = typedArray.getColor(R.styleable.CircleProgressBarView_centerProgressTextColor, centerProgressTextColor);
        centerProgressTextSize = typedArray.getDimensionPixelOffset(R.styleable.CircleProgressBarView_centerProgressTextSize, sp2px(centerProgressTextSize));

        typedArray.recycle();
    }

    private void initPaint() {
        circleBgPaint = getPaint(circleBgStrokeWidth, circleBgColor);
        progressPaint = getPaint(progressStrokeWidth, progressColor);
    }

    private void initTextPaint() {
        centerProgressTextPaint.setTextSize(centerProgressTextSize);
        centerProgressTextPaint.setColor(centerProgressTextColor);
        centerProgressTextPaint.setTextAlign(Paint.Align.CENTER);
        centerProgressTextPaint.setAntiAlias(true);
    }

    private Paint getPaint(int strokeWidth, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }

    private void initAnimation() {
        progressAnimation = ValueAnimator.ofFloat(0, mProgress);
        progressAnimation.setDuration(circleAnimationDuration);
        progressAnimation.setStartDelay(startDelay);
        progressAnimation.setInterpolator(new LinearInterpolator());
        progressAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mProgress = value;
                //根据百分比，计算扇形的角度，如果mProgress为60，表示扇形角度是360度的60%
                currentProgress = value * 360 / 100;

                if (progressListener != null) {
                    progressListener.currentProgressListener(roundTwo(value));
                }
                invalidate();
            }
        });
    }

    /**
     * 传入一个进度值，范围是0到100，从0到progress变化
     *
     * @param progress
     * @return
     */
    public CircleProgressBarView setProgressWithAnimation(float progress) {
        mProgress = progress;
        initAnimation();
        return this;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w / 2;
        centerY = h / 2;

        radius = Math.min(w, h) / 2 - Math.max(circleBgStrokeWidth, progressStrokeWidth);
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerX, centerY, radius, circleBgPaint);
        canvas.drawArc(rectF, 90, currentProgress, false, progressPaint);
        if (isDrawCenterProgressText) {
            drawCenterProgressText(canvas, (int) mProgress + "%");
        }
    }

    private void drawCenterProgressText(Canvas canvas, String currentProgress) {
        Paint.FontMetricsInt fontMetricsInt = centerProgressTextPaint.getFontMetricsInt();
        int baseline = (int) ((rectF.bottom + rectF.top - fontMetricsInt.top - fontMetricsInt.bottom) / 2);
        canvas.drawText(currentProgress, rectF.centerX(), baseline, centerProgressTextPaint);
    }

    /**
     * 启动动画
     */
    public void startProgressAnimation(){
        progressAnimation.start();
    }

    /**
     * 实时进度，适用于下载进度回调时候之类的场景
     *
     * @param progress
     * @return
     */
    public CircleProgressBarView setCurrentProgress(float progress) {
        mProgress = progress;
        return this;
    }

    public interface ProgressListener {
        void currentProgressListener(float currentProgress);
    }

    public CircleProgressBarView setProgressListener(ProgressListener listener) {
        this.progressListener = listener;
        return this;
    }

    /**
     * 将一个小数四舍五入，保留两位小数返回
     *
     * @return
     */
    public static float roundTwo(float originNum) {
        return (float) (Math.round(originNum * 10) / 10.00);
    }

    /**
     * dp转px
     *
     * @param dpVal
     * @return
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp转成px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }
}
