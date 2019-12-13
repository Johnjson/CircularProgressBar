package com.click.progress_library.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.click.progress_library.R;

/**
 * @author：jhonjson
 * @date：2019/12/13 10:16 AM
 * @describe：自定义圆形View
 */
public class CircularProgressBar extends View {

    /**
     * 动画时长
     */
    private int mDuration;
    /**
     * 接口
     */
    private OnFinishListener mListener;
    /**
     * 动画
     */
    private ValueAnimator mAnimator;

    /**
     * 逆向进度条最大值
     */
    private int mCurrentProgress;

    /**
     * 进度条最大值
     */
    private int maxValue = 100;

    /**
     * 当前进度值
     */
    private int mCurrentValue;

    /**
     * 每次扫过的角度，用来设置进度条圆弧所对应的圆心角，alphaAngle=(currentValue/maxValue)*360
     */
    private float mAlphaAngle;

    /**
     * 底部圆弧的颜色，默认 ResUtil.getColor(R.color.colorAccent)
     */
    private int mDefaultColor;

    /**
     * 进度条圆弧块的颜色, 默认 ResUtil.getColor(R.color.colorWhith)
     */
    private int mRunColor;

    /**
     * 中间文字颜色(默认蓝色)
     */
    private int cTextColor = Color.BLUE;

    /**
     * 中间文字的字体大小(默认30dp)
     */
    private int cTextSize;

    /**
     * 圆环的宽度
     */
    private int mCircleWidth;

    /**
     * 画圆弧的画笔
     */
    private Paint mCirclePaint;

    /**
     * 画文字的画笔
     */
    private Paint mTextPaint;

    /**
     * 进度条方向
     */
    private boolean mDirectionof;

    public void setListener(OnFinishListener mListener) {
        this.mListener = mListener;
    }

    public CircularProgressBar(Context context) {
        this(context, null);
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    //初始化
    @SuppressLint("ResourceAsColor")
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CirculaProgressBar,
                defStyleAttr, 0);
        // 默认底色白色透明度百分之20
        mDefaultColor = ta.getColor(R.styleable.CirculaProgressBar_circular_defaultColor, R.color.colorGray);
        // 默认进度条颜色为白色
        mRunColor = ta.getColor(R.styleable.CirculaProgressBar_circular_runColor, R.color.colorWhith);
        // 默认中间文字颜色为蓝色
        cTextColor = ta.getColor(R.styleable.CirculaProgressBar_circular_cTextColor, Color.BLUE);
        // 默认中间文字字体大小为30dp
        cTextSize = ta.getDimensionPixelSize(R.styleable.CirculaProgressBar_circular_cTextSize, DpDonversionPxUtils.px2dip(30));
        // 默认圆弧宽度为6dp
        mCircleWidth = ta.getDimensionPixelSize(R.styleable.CirculaProgressBar_circular_circleWidth, DpDonversionPxUtils.px2dip(6));
        // 默认顺时针方向
        mDirectionof = ta.getBoolean(R.styleable.CirculaProgressBar_circular_directionof, false);

        ta.recycle();

        //画圆画笔
        mCirclePaint = new Paint();
        // 抗锯齿
        mCirclePaint.setAntiAlias(true);
        // 防抖动
        mCirclePaint.setDither(true);
        //画笔宽度
        mCirclePaint.setStrokeWidth(mCircleWidth);


        //画文字画笔
        mTextPaint = new Paint();
        // 抗锯齿
        mTextPaint.setAntiAlias(true);
        // 防抖动
        mTextPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取屏幕宽
        int widthPixels = this.getResources().getDisplayMetrics().widthPixels;
        //获取屏幕高
        int heightPixels = this.getResources().getDisplayMetrics().heightPixels;

        int rwidth = MeasureSpec.getSize(widthMeasureSpec);
        int rhedight = MeasureSpec.getSize(heightMeasureSpec);

        int minWidth = Math.min(widthPixels, rwidth);
        int minHedight = Math.min(heightPixels, rhedight);

        setMeasuredDimension(Math.min(minWidth, minHedight), Math.min(minWidth, minHedight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = this.getWidth() / 2;
        int radius = center - mCircleWidth / 2;
        // 绘制进度圆弧
        drawCircle(canvas, center, radius);
        drawText(canvas, center);
    }

    /**
     * 绘制进度圆弧
     *
     * @param canvas 画布对象
     * @param center 圆心的x和y坐标
     * @param radius 圆的半径
     */
    private void drawCircle(Canvas canvas, int center, int radius) {
        if (mCirclePaint == null) {
            return;
        }
        // 清除上一次的shader
        mCirclePaint.setShader(null);
        // 设置底部圆环的颜色，这里使用第一种颜色
        mCirclePaint.setColor(mDefaultColor);
        // 设置绘制的圆为空心
        mCirclePaint.setStyle(Paint.Style.STROKE);
        // 画底部的空心圆
        canvas.drawCircle(center, center, radius, mCirclePaint);
        // 圆的外接正方形
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);


        mCirclePaint.setShadowLayer(10, 10, 10, Color.BLUE);
        // 设置圆弧的颜色
        mCirclePaint.setColor(mRunColor);
        // 把每段圆弧改成圆角的
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        // 计算每次画圆弧时扫过的角度，这里计算要注意分母要转为float类型，否则alphaAngle永远为0
        if (mDirectionof) {
            canvas.drawArc(oval, -90, mCurrentProgress - 360, false, mCirclePaint);
        } else {
            mAlphaAngle = mCurrentValue * 360.0f / maxValue * 1.0f;
            canvas.drawArc(oval, -90, mAlphaAngle, false, mCirclePaint);
        }
    }

    /**
     * 绘制文字
     *
     * @param canvas 画布对象
     * @param center 圆心的x和y坐标
     */
    private void drawText(Canvas canvas, int center) {
        // 计算进度
        int result = ((maxValue - mCurrentValue) * (mDuration / 1000) / maxValue);
        String percent;
        if (maxValue == mCurrentValue) {
            percent = "完成";
            // 设置要绘制的文字大小
            mTextPaint.setTextSize(cTextSize);
        } else {
            percent = result + "";
            // 设置要绘制的文字大小
            mTextPaint.setTextSize(cTextSize + cTextSize / 3);
        }
        // 设置文字居中，文字的x坐标要注意
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        // 设置文字颜色
        mTextPaint.setColor(cTextColor);
        // 注意此处一定要重新设置宽度为0,否则绘制的文字会重叠
        mTextPaint.setStrokeWidth(0);
        // 文字边框
        Rect bounds = new Rect();
        // 获得绘制文字的边界矩形
        mTextPaint.getTextBounds(percent, 0, percent.length(), bounds);
        // 获取绘制Text时的四条线
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        // 计算文字的基线,方法见http://blog.csdn.net/harvic880925/article/details/50423762
        int baseline = center + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        // 绘制表示进度的文字
        canvas.drawText(percent, center, baseline, mTextPaint);

    }


    /**
     * 设置圆环的宽度
     *
     * @param width
     */
    public void setCircleWidth(int width) {
        this.mCircleWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources()
                .getDisplayMetrics());
        mCirclePaint.setStrokeWidth(mCircleWidth);
        //一般只是希望在View发生改变时对UI进行重绘。invalidate()方法系统会自动调用 View的onDraw()方法。
        invalidate();
    }

    /**
     * 设置圆环的底色，默认为亮灰色LTGRAY
     *
     * @param color
     */
    public void setDefaultColor(int color) {
        this.mDefaultColor = color;
        mCirclePaint.setColor(mDefaultColor);
        //一般只是希望在View发生改变时对UI进行重绘。invalidate()方法系统会自动调用 View的onDraw()方法。
        invalidate();
    }

    /**
     * 设置进度条的颜色，默认为白色<br>
     *
     * @param color
     */
    public void setRunColor(int color) {
        this.mRunColor = color;
        mCirclePaint.setColor(mRunColor);
        //一般只是希望在View发生改变时对UI进行重绘。invalidate()方法系统会自动调用 View的onDraw()方法。
        invalidate();
    }

    /**
     * 设置进度条运行方向
     *
     * @param directionof
     */
    public void setRunDirectionof(boolean directionof) {
        this.mDirectionof = directionof;
        //一般只是希望在View发生改变时对UI进行重绘。invalidate()方法系统会自动调用 View的onDraw()方法。
        invalidate();
    }


    /**
     * 按进度显示百分比，可选择是否启用数字动画
     *
     * @param duration 动画时长
     */
    public void setDuration(int duration, OnFinishListener listener) {
        this.mListener = listener;
        this.mDuration = duration + 1000;
        if (mAnimator != null) {
            mAnimator.cancel();
        } else {
            mAnimator = ValueAnimator.ofInt(0, maxValue);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentValue = (int) animation.getAnimatedValue();
                    Log.e(" onAnimationUpdate ", " onAnimationUpdate  =   " + mCurrentValue);
                    mCurrentProgress = (int) (360 * (mCurrentValue / 100f));
                    Log.e(" onAnimationUpdate ", " onAnimationUpdate  mCurrentProgress  =   " + mCurrentProgress);
                    //一般只是希望在View发生改变时对UI进行重绘。invalidate()方法系统会自动调用 View的onDraw()方法。
                    invalidate();
                    if (maxValue == mCurrentValue && CircularProgressBar.this.mListener != null) {
                        CircularProgressBar.this.mListener.onFinish();
                    }
                }
            });
            mAnimator.setInterpolator(new LinearInterpolator());
        }
        mAnimator.setDuration(duration);
        mAnimator.start();
    }

    /**
     *  停止动画
     */
    public void stopAnimator() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

}
