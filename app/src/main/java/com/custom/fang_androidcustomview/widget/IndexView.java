package com.custom.fang_androidcustomview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class IndexView extends View {

    private Context mContext;
    private TextView mShowTextDialog;

    private int mWordSize;
    private int mWordColor;
    private int mChoose = -1;//选中
    private Paint mPaint;

    private int mWidth;
    private int mHeight;

    private int mCellWidth;
    private int mCellHeight;

    private static final String[] WORDS = new String[]{
            "↑", "☆", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
            "X", "Y", "Z", "#"
    };

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    private int GRAY = 0xFFe8e8e8;
    private int DEFAULT_TEXT_COLOR = 0xFF999999;

    public IndexView(Context context) {
        this(context, null);
    }

    public IndexView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mWordSize = sp2px(mContext, 12);
        mWordColor = 0;
        initPaint();
    }

    public void setmShowTextDialog(TextView textDialog) {
        this.mShowTextDialog = textDialog;
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(DEFAULT_TEXT_COLOR);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mWordSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        mCellHeight = mHeight / WORDS.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < WORDS.length; i++) {
            float xPos = mWidth / 2 - mPaint.measureText(WORDS[i]) / 2;
            float yPos = mCellHeight * i + mCellHeight;
            canvas.drawText(WORDS[i], xPos, yPos, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();//点击点的y坐标
        final int oldChoose = mChoose;

        final int c = (int) (y / getHeight() * WORDS.length);

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(0x00000000);
                mChoose = -1;
                if (mShowTextDialog != null) {
                    mShowTextDialog.setVisibility(INVISIBLE);
                }
                break;
            default:
                setBackgroundColor(GRAY);
                if (oldChoose != c) {
                    if (onTouchingLetterChangedListener != null) {
                        onTouchingLetterChangedListener.onTouchingLetterChanged(WORDS[c]);
                    }
                    if (mShowTextDialog != null) {
                        mShowTextDialog.setText(WORDS[c]);
                        mShowTextDialog.setVisibility(VISIBLE);
                    }
                    mChoose = c;
                    invalidate();
                }
                break;

        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener listener) {
        onTouchingLetterChangedListener = listener;
    }

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String letter);
    }

    /**
     * sp转px
     *
     * @param context
     * @param spValue
     * @return
     */
    public int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }
}
