package com.warkiz.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.util.Pair;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;


public class IndicatorSeekBar extends View {
    private static final int THUMB_MAX_WIDTH = 30;
    private static final String FORMAT_PROGRESS = "${PROGRESS}";
    private static final String FORMAT_TICK_TEXT = "${TICK_TEXT}";
    private Context mContext;
    private Paint mStockPaint;//the paint for seek bar drawing
    private TextPaint mTextPaint;//the paint for mTickTextsArr drawing
    private OnSeekChangeListener mSeekChangeListener;
    private Rect mRect;
    private float mCustomDrawableMaxHeight;//the max height for custom drawable
    private float lastProgress;
    private float mFaultTolerance = -1;//the tolerance for user seek bar touching
    private float mScreenWidth = -1;
    private boolean mClearPadding;
    private SeekParams mSeekParams;//save the params when seeking change.
    //seek bar
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mMeasuredWidth;
    private int mPaddingTop;
    private float mSeekLength;//the total length of seek bar
    private float mSeekBlockLength;//the length for each section part to seek
    private boolean mIsTouching;//user is touching the seek bar
    private float mMax;
    private float mMin;
    private float mProgress;
    private boolean mIsFloatProgress;// true for the progress value in float,otherwise in int.
    private int mScale = 1;//the scale of the float progress.
    private boolean mUserSeekable;//true if the user can seek to change the progress,otherwise only can be changed by setProgress().
    private boolean mOnlyThumbDraggable;//only drag the seek bar's thumb_img can be change the progress
    private boolean mSeekSmoothly;//seek continuously
    private float[] mProgressArr;//save the progress which at tickMark position.
    private boolean mR2L;//right to left,compat local problem.
    //tick texts
    private boolean mShowTickText;//the palace where the tick text show .
    private int mTickTextsHeight;//the height of text
    private String[] mTickTextsArr;//save the tick texts which at tickMark position.
    private float[] mTickTextsWidth;//save the tick texts width bounds.
    private float[] mTextCenterX;//the text's drawing X anchor
    private float mTickTextY;//the text's drawing Y anchor
    private int mTickTextsSize;
    private Typeface mTextsTypeface;//the tick texts and thumb_img texts' typeface
    private int mUnselectedTextsColor;//the color for the tick texts those thumb_img haven't reach.
    private CharSequence[] mTickTextsCustomArray;
    //indicator
    private Indicator mIndicator;//the pop window above the seek bar
    private int mIndicatorColor;
    private int typeColor = 1;
    private int mIndicatorTextColor;
    private boolean mIndicatorStayAlways;//true if the indicator didn't dismiss after initial.
    private int mIndicatorTextSize;
    private View mIndicatorContentView;//the view to replace the raw indicator all view
    private View mIndicatorTopContentView;//the view to replace the raw indicator content view
    private int mShowIndicatorType;//different indicator type.
    private String mIndicatorTextFormat;
    //tick marks
    private float[] mTickMarksX;//the tickMark's drawing X anchor
    private int mTicksCount;//the num of tickMarks
    private int mSelectedTickMarksColor;//the color for the tickMarks those thumb_img swept.
    private float mTickRadius;//the tick's radius
    private Bitmap mUnselectTickMarksBitmap;//the drawable bitmap for tick
    private Drawable mTickMarksDrawable;
    private int mTickMarksSize;//the width of tickMark
    //track
    private boolean mTrackRoundedCorners;
    private RectF mProgressTrack;//the background track on the thumb_img start
    private RectF mBackgroundTrack;//the background track on the thumb_img ends
    private int mBackgroundTrackSize;
    private int mProgressTrackSize;
    private int mBackgroundTrackColor;
    private int mProgressTrackColor;
    private int[] mSectionTrackColorArray;//save the color for each section tracks.
    private boolean mCustomTrackSectionColorResult;//true to confirm to custom the section track color
    //thumb_img
    private float mThumbRadius;//the thumb_img's radius
    private float mThumbTouchRadius;//the thumb_img's radius when touching
    private Bitmap mThumbBitmap;//the drawable bitmap for thumb_img
    private int mThumbColor;
    private int mThumbSize;
    private Drawable mThumbDrawable;
    private Bitmap mPressedThumbBitmap;//the bitmap for pressing status
    private int mPressedThumbColor;//the color for pressing status
    //thumb_img text
    private boolean mShowThumbText;//the place where the thumb_img text show .
    private float mThumbTextY;//the thumb_img text's drawing Y anchor
    private int mThumbTextColor;
    private boolean mHideThumb;
    private boolean mAdjustAuto;

    public IndicatorSeekBar(Context context) {
        this(context, null);
    }

    public IndicatorSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initAttrs(mContext, attrs);
        initParams();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public IndicatorSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initAttrs(mContext, attrs);
        initParams();
    }

    /**
     * if you want a java build, the way like:
     * <p>
     * IndicatorSeekBar
     * .with(getContext())
     * .max(50)
     * .min(10)
     * ...
     * .build();
     */
    IndicatorSeekBar(Builder builder) {
        super(builder.context);
        this.mContext = builder.context;
        int defaultPadding = SizeUtils.dp2px(mContext, 16);
        setPadding(defaultPadding, getPaddingTop(), defaultPadding, getPaddingBottom());
        this.apply(builder);
        initParams();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        Builder builder = new Builder(context);
        if (attrs == null) {
            this.apply(builder);
            return;
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IndicatorSeekBar);
        //seekBar
        mMax = ta.getFloat(R.styleable.IndicatorSeekBar_isb_max, builder.max);
        mMin = ta.getFloat(R.styleable.IndicatorSeekBar_isb_min, builder.min);
        mProgress = ta.getFloat(R.styleable.IndicatorSeekBar_isb_progress, builder.progress);
        mIsFloatProgress = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_progress_value_float, builder.progressValueFloat);
        mUserSeekable = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_user_seekable, builder.userSeekable);
        mClearPadding = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_clear_default_padding, builder.clearPadding);
        mOnlyThumbDraggable = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_only_thumb_draggable, builder.onlyThumbDraggable);
        mSeekSmoothly = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_seek_smoothly, builder.seekSmoothly);
        mR2L = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_r2l, builder.r2l);
        //track
        mBackgroundTrackSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_isb_track_background_size, builder.trackBackgroundSize);
        mProgressTrackSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_isb_track_progress_size, builder.trackProgressSize);
        mBackgroundTrackColor = ta.getColor(R.styleable.IndicatorSeekBar_isb_track_background_color, builder.trackBackgroundColor);
        mProgressTrackColor = ta.getColor(R.styleable.IndicatorSeekBar_isb_track_progress_color, builder.trackProgressColor);
        mTrackRoundedCorners = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_track_rounded_corners, builder.trackRoundedCorners);
        //thumb_img
        mThumbSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_isb_thumb_size, builder.thumbSize);
        mThumbDrawable = ta.getDrawable(R.styleable.IndicatorSeekBar_isb_thumb_drawable);
        mAdjustAuto = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_thumb_adjust_auto, true);
        initThumbColor(ta.getColorStateList(R.styleable.IndicatorSeekBar_isb_thumb_color), builder.thumbColor);
        //thumb_img text
        mShowThumbText = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_show_thumb_text, builder.showThumbText);
        mThumbTextColor = ta.getColor(R.styleable.IndicatorSeekBar_isb_thumb_text_color, builder.thumbTextColor);
        //tickMarks
        mTicksCount = ta.getInt(R.styleable.IndicatorSeekBar_isb_ticks_count, builder.tickCount);
        mTickMarksSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_isb_tick_marks_size, builder.tickMarksSize);
        mTickMarksDrawable = ta.getDrawable(R.styleable.IndicatorSeekBar_isb_tick_marks_drawable);
        //tickTexts
        mShowTickText = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_show_tick_texts, builder.showTickText);
        mTickTextsSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_isb_tick_texts_size, builder.tickTextsSize);
        initTickTextsColor(ta.getColorStateList(R.styleable.IndicatorSeekBar_isb_tick_texts_color), builder.tickTextsColor);
        mTickTextsCustomArray = ta.getTextArray(R.styleable.IndicatorSeekBar_isb_tick_texts_array);
        initTextsTypeface(ta.getInt(R.styleable.IndicatorSeekBar_isb_tick_texts_typeface, -1), builder.tickTextsTypeFace);
        //indicator
        mShowIndicatorType = ta.getInt(R.styleable.IndicatorSeekBar_isb_show_indicator, builder.showIndicatorType);
        mIndicatorColor = ta.getColor(R.styleable.IndicatorSeekBar_isb_indicator_color, builder.indicatorColor);
        mIndicatorTextSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_isb_indicator_text_size, builder.indicatorTextSize);
//        mIndicatorTextColor = ta.getColor(R.styleable.IndicatorSeekBar_isb_indicator_text_color, builder.indicatorTextColor);

        int indicatorContentViewId = ta.getResourceId(R.styleable.IndicatorSeekBar_isb_indicator_content_layout, 0);
        if (indicatorContentViewId > 0) {
            mIndicatorContentView = View.inflate(mContext, indicatorContentViewId, null);
        }
        int indicatorTopContentLayoutId = ta.getResourceId(R.styleable.IndicatorSeekBar_isb_indicator_top_content_layout, 0);
        if (indicatorTopContentLayoutId > 0) {
            mIndicatorTopContentView = View.inflate(mContext, indicatorTopContentLayoutId, null);
        }
        ta.recycle();
    }

    private void initParams() {
        initProgressRangeValue();
        if (mBackgroundTrackSize > mProgressTrackSize) {
            mBackgroundTrackSize = mProgressTrackSize;
        }
        if (mThumbDrawable == null) {
            mThumbRadius = mThumbSize / 2.0f;
            mThumbTouchRadius = mThumbRadius * 1.2f;
        } else {
            mThumbRadius = Math.min(SizeUtils.dp2px(mContext, THUMB_MAX_WIDTH), mThumbSize) / 2.0f;
            mThumbTouchRadius = mThumbRadius;
        }
        if (mTickMarksDrawable == null) {
            mTickRadius = mTickMarksSize / 2.0f;
        } else {
            mTickRadius = Math.min(SizeUtils.dp2px(mContext, THUMB_MAX_WIDTH), mTickMarksSize) / 2.0f;
        }
        mCustomDrawableMaxHeight = Math.max(mThumbTouchRadius, mTickRadius) * 2.0f;
        initStrokePaint();
        measureTickTextsBonds();
        lastProgress = mProgress;

        collectTicksInfo();

        mProgressTrack = new RectF();
        mBackgroundTrack = new RectF();
        initDefaultPadding();
        initIndicatorContentView();
    }

    private void collectTicksInfo() {
        if (mTicksCount < 0 || mTicksCount > 50) {
            throw new IllegalArgumentException("the Argument: TICK COUNT must be limited between (0-50), Now is " + mTicksCount);
        }
        if (mTicksCount != 0) {
            mTickMarksX = new float[mTicksCount];
            if (mShowTickText) {
                mTextCenterX = new float[mTicksCount];
                mTickTextsWidth = new float[mTicksCount];
            }
            mProgressArr = new float[mTicksCount];
            for (int i = 0; i < mProgressArr.length; i++) {
                mProgressArr[i] = mMin + i * (mMax - mMin) / ((mTicksCount - 1) > 0 ? (mTicksCount - 1) : 1);
            }

        }
    }

    private void initDefaultPadding() {
        if (!mClearPadding) {
            int normalPadding = SizeUtils.dp2px(mContext, 16);
            if (getPaddingLeft() == 0) {
                setPadding(normalPadding, getPaddingTop(), getPaddingRight(), getPaddingBottom());
            }
            if (getPaddingRight() == 0) {
                setPadding(getPaddingLeft(), getPaddingTop(), normalPadding, getPaddingBottom());
            }
        }
    }

    private void initProgressRangeValue() {
        if (mMax < mMin) {
            throw new IllegalArgumentException("the Argument: MAX's value must be larger than MIN's.");
        }
        if (mProgress < mMin) {
            mProgress = mMin;
        }
        if (mProgress > mMax) {
            mProgress = mMax;
        }
    }

    private void initStrokePaint() {
        if (mStockPaint == null) {
            mStockPaint = new Paint();
        }
        if (mTrackRoundedCorners) {
            mStockPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        mStockPaint.setAntiAlias(true);
        if (mBackgroundTrackSize > mProgressTrackSize) {
            mProgressTrackSize = mBackgroundTrackSize;
        }
    }

    private void measureTickTextsBonds() {
        if (needDrawText()) {
            initTextPaint();
            mTextPaint.setTypeface(mTextsTypeface);
            mTextPaint.getTextBounds("j", 0, 1, mRect);
            mTickTextsHeight = mRect.height() + SizeUtils.dp2px(mContext, 3);//with the gap(3dp) between tickTexts and track.
        }
    }

    private boolean needDrawText() {
        return mShowThumbText || (mTicksCount != 0 && mShowTickText);
    }

    private void initTextPaint() {
        if (mTextPaint == null) {
            mTextPaint = new TextPaint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setTextSize(mTickTextsSize);
        }
        if (mRect == null) {
            mRect = new Rect();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = Math.round(mCustomDrawableMaxHeight + getPaddingTop() + getPaddingBottom());
        setMeasuredDimension(resolveSize(SizeUtils.dp2px(mContext, 170), widthMeasureSpec), height + mTickTextsHeight);
        initSeekBarInfo();
        refreshSeekBarLocation();
    }

    private void initSeekBarInfo() {
        mMeasuredWidth = getMeasuredWidth();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mPaddingLeft = getPaddingLeft();
            mPaddingRight = getPaddingRight();
        } else {
            mPaddingLeft = getPaddingStart();
            mPaddingRight = getPaddingEnd();
        }
        mPaddingTop = getPaddingTop();
        mSeekLength = mMeasuredWidth - mPaddingLeft - mPaddingRight;
        mSeekBlockLength = mSeekLength / (mTicksCount - 1 > 0 ? mTicksCount - 1 : 1);
    }

    private void refreshSeekBarLocation() {
        initTrackLocation();
        //init TickTexts Y Location
        if (needDrawText()) {
            mTextPaint.getTextBounds("j", 0, 1, mRect);
            mTickTextY = mPaddingTop + mCustomDrawableMaxHeight + Math.round(mRect.height() - mTextPaint.descent()) + SizeUtils.dp2px(mContext, 3);
            mThumbTextY = mTickTextY;
        }
        //init tick's X and text's X location;
        if (mTickMarksX == null) {
            return;
        }
        initTextsArray();
        //adjust thumb_img auto,so find out the closest progress in the mProgressArr array and replace it.
        //it is not necessary to adjust thumb_img while count is less than 2.
        if (mTicksCount > 2) {
            mProgress = mProgressArr[getClosestIndex()];
            lastProgress = mProgress;
        }
        refreshThumbCenterXByProgress(mProgress);
    }

    private void initTextsArray() {
        if (mTicksCount == 0) {
            return;
        }
        if (mShowTickText) {
            mTickTextsArr = new String[mTicksCount];
        }
        for (int i = 0; i < mTickMarksX.length; i++) {
            if (mShowTickText) {
                mTickTextsArr[i] = getTickTextByPosition(i);
                mTextPaint.getTextBounds(mTickTextsArr[i], 0, mTickTextsArr[i].length(), mRect);
                mTickTextsWidth[i] = mRect.width();
                mTextCenterX[i] = mPaddingLeft + mSeekBlockLength * i;
            }
            mTickMarksX[i] = mPaddingLeft + mSeekBlockLength * i;
        }
    }

    private void initTrackLocation() {
        if (mR2L) {
            mBackgroundTrack.left = mPaddingLeft;
            mBackgroundTrack.top = mPaddingTop + mThumbTouchRadius;
            //ThumbCenterX
            mBackgroundTrack.right = mPaddingLeft + mSeekLength * (1.0f - (mProgress - mMin) / (getAmplitude()));
            mBackgroundTrack.bottom = mBackgroundTrack.top;
            //ThumbCenterX
            mProgressTrack.left = mBackgroundTrack.right;
            mProgressTrack.top = mBackgroundTrack.top;
            mProgressTrack.right = mMeasuredWidth - mPaddingRight;
            mProgressTrack.bottom = mBackgroundTrack.bottom;
        } else {
            mProgressTrack.left = mPaddingLeft;
            mProgressTrack.top = mPaddingTop + mThumbTouchRadius;
            //ThumbCenterX
            mProgressTrack.right = (mProgress - mMin) * mSeekLength / (getAmplitude()) + mPaddingLeft;
            mProgressTrack.bottom = mProgressTrack.top;
            //ThumbCenterX
            mBackgroundTrack.left = mProgressTrack.right;
            mBackgroundTrack.top = mProgressTrack.bottom;
            mBackgroundTrack.right = mMeasuredWidth - mPaddingRight;
            mBackgroundTrack.bottom = mProgressTrack.bottom;
        }
    }

    private String getTickTextByPosition(int index) {
        if (mTickTextsCustomArray == null) {
            return getProgressString(mProgressArr[index]);
        }
        if (index < mTickTextsCustomArray.length) {
            return String.valueOf(mTickTextsCustomArray[index]);
        }
        return "";
    }

    /**
     * calculate the thumb_img's centerX by the changing progress.
     */
    private void refreshThumbCenterXByProgress(float progress) {
        //ThumbCenterX
        if (mR2L) {
            mBackgroundTrack.right = mPaddingLeft + mSeekLength * (1.0f - (progress - mMin) / (getAmplitude()));//ThumbCenterX
            mProgressTrack.left = mBackgroundTrack.right;
        } else {
            mProgressTrack.right = (progress - mMin) * mSeekLength / (getAmplitude()) + mPaddingLeft;
            mBackgroundTrack.left = mProgressTrack.right;
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        drawTrack(canvas);
//        drawTickMarks(canvas);
//        drawTickTexts(canvas);
        drawThumb(canvas);
        drawThumbText(canvas);
    }

    private void drawTrack(Canvas canvas) {
        if (mCustomTrackSectionColorResult) {//the track has custom the section track color
            int sectionSize = mTicksCount - 1 > 0 ? mTicksCount - 1 : 1;
            for (int i = 0; i < sectionSize; i++) {
                if (mR2L) {
                    mStockPaint.setColor(mSectionTrackColorArray[sectionSize - i - 1]);
                } else {
                    mStockPaint.setColor(mSectionTrackColorArray[i]);
                }
                float thumbPosFloat = getThumbPosOnTickFloat();
                if (i < thumbPosFloat && thumbPosFloat < (i + 1)) {
                    float thumbCenterX = getThumbCenterX();
                    mStockPaint.setStrokeWidth(getLeftSideTrackSize());
                    canvas.drawLine(mTickMarksX[i], mProgressTrack.top, thumbCenterX, mProgressTrack.bottom, mStockPaint);
                    mStockPaint.setStrokeWidth(getRightSideTrackSize());
                    canvas.drawLine(thumbCenterX, mProgressTrack.top, mTickMarksX[i + 1], mProgressTrack.bottom, mStockPaint);
                } else {
                    if (i < thumbPosFloat) {
                        mStockPaint.setStrokeWidth(getLeftSideTrackSize());
                    } else {
                        mStockPaint.setStrokeWidth(getRightSideTrackSize());
                    }
                    canvas.drawLine(mTickMarksX[i], mProgressTrack.top, mTickMarksX[i + 1], mProgressTrack.bottom, mStockPaint);
                }
            }
        } else {
            //draw progress track
            mStockPaint.setColor(mProgressTrackColor);
            mStockPaint.setStrokeWidth(mProgressTrackSize);
            canvas.drawLine(mProgressTrack.left, mProgressTrack.top, mProgressTrack.right, mProgressTrack.bottom, mStockPaint);
            //draw BG track
            mStockPaint.setColor(mBackgroundTrackColor);
            mStockPaint.setStrokeWidth(mBackgroundTrackSize);
            canvas.drawLine(mBackgroundTrack.left, mBackgroundTrack.top, mBackgroundTrack.right, mBackgroundTrack.bottom, mStockPaint);
        }
    }


    private void drawThumb(Canvas canvas) {
        if (mHideThumb) {
            return;
        }
        float thumbCenterX = getThumbCenterX();
        if (mThumbDrawable != null) {//check user has set thumb_img drawable or not.ThumbDrawable first, thumb_img color for later.
            if (mThumbBitmap == null || mPressedThumbBitmap == null) {
                initThumbBitmap();
            }
            if (mThumbBitmap == null || mPressedThumbBitmap == null) {
                //please check your selector drawable's format and correct.
                throw new IllegalArgumentException("the format of the selector thumb_img drawable is wrong!");
            }
            mStockPaint.setAlpha(255);
            if (mIsTouching) {
                canvas.drawBitmap(mPressedThumbBitmap, thumbCenterX - mPressedThumbBitmap.getWidth() / 2.0f, mProgressTrack.top - mPressedThumbBitmap.getHeight() / 2.0f, mStockPaint);
            } else {
                canvas.drawBitmap(mThumbBitmap, thumbCenterX - mThumbBitmap.getWidth() / 2.0f, mProgressTrack.top - mThumbBitmap.getHeight() / 2.0f, mStockPaint);
            }
        } else {
            if (mIsTouching) {
                mStockPaint.setColor(mPressedThumbColor);
            } else {
                mStockPaint.setColor(mThumbColor);
            }
            canvas.drawCircle(thumbCenterX, mProgressTrack.top, mIsTouching ? mThumbTouchRadius : mThumbRadius, mStockPaint);
        }
    }

    private void drawThumbText(Canvas canvas) {
        if (!mShowThumbText || (mShowTickText && mTicksCount > 2)) {
            return;
        }
        mTextPaint.setColor(mThumbTextColor);
        canvas.drawText(getProgressString(mProgress), getThumbCenterX(), mThumbTextY, mTextPaint);
    }

    private float getThumbCenterX() {
        if (mR2L) {
            return mBackgroundTrack.right;
        }
        return mProgressTrack.right;
    }


    private int getLeftSideTrackSize() {
        if (mR2L) {
            return mBackgroundTrackSize;
        }
        return mProgressTrackSize;
    }

    private int getRightSideTrackSize() {
        if (mR2L) {
            return mProgressTrackSize;
        }
        return mBackgroundTrackSize;
    }

    private int getThumbPosOnTick() {
        if (mTicksCount != 0) {
            return Math.round((getThumbCenterX() - mPaddingLeft) / mSeekBlockLength);
        }
        //when tick count = 0 ; seek bar has not tick(continuous series), return 0;
        return 0;
    }

    private float getThumbPosOnTickFloat() {
        if (mTicksCount != 0) {
            return (getThumbCenterX() - mPaddingLeft) / mSeekBlockLength;
        }
        return 0;
    }

    private int getHeightByRatio(Drawable drawable, int width) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        return Math.round(1.0f * width * intrinsicHeight / intrinsicWidth);
    }

    private Bitmap getDrawBitmap(Drawable drawable, boolean isThumb) {
        if (drawable == null) {
            return null;
        }
        int width;
        int height;
        int maxRange = SizeUtils.dp2px(mContext, THUMB_MAX_WIDTH);
        int intrinsicWidth = drawable.getIntrinsicWidth();
        if (intrinsicWidth > maxRange) {
            if (isThumb) {
                width = mThumbSize;
            } else {
                width = mTickMarksSize;
            }
            height = getHeightByRatio(drawable, width);

            if (width > maxRange) {
                width = maxRange;
                height = getHeightByRatio(drawable, width);
            }
        } else {
            width = drawable.getIntrinsicWidth();
            height = drawable.getIntrinsicHeight();
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void initThumbColor(ColorStateList colorStateList, int defaultColor) {
        //if you didn't set the thumb_img color, set a default color.
        if (colorStateList == null) {
            mThumbColor = defaultColor;
            mPressedThumbColor = mThumbColor;
            return;
        }
        int[][] states = null;
        int[] colors = null;
        Class<? extends ColorStateList> aClass = colorStateList.getClass();
        try {
            Field[] f = aClass.getDeclaredFields();
            for (Field field : f) {
                field.setAccessible(true);
                if ("mStateSpecs".equals(field.getName())) {
                    states = (int[][]) field.get(colorStateList);
                }
                if ("mColors".equals(field.getName())) {
                    colors = (int[]) field.get(colorStateList);
                }
            }
            if (states == null || colors == null) {
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException("Something wrong happened when parseing thumb_img selector color.");
        }
        if (states.length == 1) {
            mThumbColor = colors[0];
            mPressedThumbColor = mThumbColor;
        } else if (states.length == 2) {
            for (int i = 0; i < states.length; i++) {
                int[] attr = states[i];
                if (attr.length == 0) {//didn't have state,so just get color.
                    mPressedThumbColor = colors[i];
                    continue;
                }
                switch (attr[0]) {
                    case android.R.attr.state_pressed:
                        mThumbColor = colors[i];
                        break;
                    default:
                        //the color selector file was set by a wrong format , please see above to correct.
                        throw new IllegalArgumentException("the selector color file you set for the argument: isb_thumb_color is in wrong format.");
                }
            }
        } else {
            //the color selector file was set by a wrong format , please see above to correct.
            throw new IllegalArgumentException("the selector color file you set for the argument: isb_thumb_color is in wrong format.");
        }

    }

    private void initTickMarksColor(ColorStateList colorStateList, int defaultColor) {
        //if you didn't set the tick's text color, set a default selector color file.
        if (colorStateList == null) {
            mSelectedTickMarksColor = defaultColor;
            return;
        }
        int[][] states = null;
        int[] colors = null;
        Class<? extends ColorStateList> aClass = colorStateList.getClass();
        try {
            Field[] f = aClass.getDeclaredFields();
            for (Field field : f) {
                field.setAccessible(true);
                if ("mStateSpecs".equals(field.getName())) {
                    states = (int[][]) field.get(colorStateList);
                }
                if ("mColors".equals(field.getName())) {
                    colors = (int[]) field.get(colorStateList);
                }
            }
            if (states == null || colors == null) {
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException("Something wrong happened when parsing thumb_img selector color." + e.getMessage());
        }
        if (states.length == 1) {
            mSelectedTickMarksColor = colors[0];
        } else if (states.length == 2) {
            for (int i = 0; i < states.length; i++) {
                int[] attr = states[i];
                if (attr.length == 0) {//didn't have state,so just get color.
                    continue;
                }
                switch (attr[0]) {
                    case android.R.attr.state_selected:
                        mSelectedTickMarksColor = colors[i];
                        break;
                    default:
                        throw new IllegalArgumentException("the selector color file you set for the argument: isb_tick_marks_color is in wrong format.");
                }
            }
        } else {
            throw new IllegalArgumentException("the selector color file you set for the argument: isb_tick_marks_color is in wrong format.");
        }
    }

    private void initTickTextsColor(ColorStateList colorStateList, int defaultColor) {
        //if you didn't set the tick's texts color, will be set a selector color file default.
        if (colorStateList == null) {
            mUnselectedTextsColor = defaultColor;
            return;
        }
        int[][] states = null;
        int[] colors = null;
        Class<? extends ColorStateList> aClass = colorStateList.getClass();
        try {
            Field[] f = aClass.getDeclaredFields();
            for (Field field : f) {
                field.setAccessible(true);
                if ("mStateSpecs".equals(field.getName())) {
                    states = (int[][]) field.get(colorStateList);
                }
                if ("mColors".equals(field.getName())) {
                    colors = (int[]) field.get(colorStateList);
                }
            }
            if (states == null || colors == null) {
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException("Something wrong happened when parseing thumb_img selector color.");
        }
        if (states.length == 1) {
            mUnselectedTextsColor = colors[0];
        } else if (states.length == 3) {
            for (int i = 0; i < states.length; i++) {
                int[] attr = states[i];
                if (attr.length == 0) {//didn't have state,so just get color.
                    mUnselectedTextsColor = colors[i];
                    continue;
                }
                switch (attr[0]) {
                    case android.R.attr.state_selected:
                        break;
                    case android.R.attr.state_hovered:
                        break;
                    default:
                        //the color selector file was set by a wrong format , please see above to correct.
                        throw new IllegalArgumentException("the selector color file you set for the argument: isb_tick_texts_color is in wrong format.");
                }
            }
        } else {
            //the color selector file was set by a wrong format , please see above to correct.
            throw new IllegalArgumentException("the selector color file you set for the argument: isb_tick_texts_color is in wrong format.");
        }
    }

    private void initTextsTypeface(int typeface, Typeface defaultTypeface) {
        switch (typeface) {
            case 0:
                mTextsTypeface = Typeface.DEFAULT;
                break;
            case 1:
                mTextsTypeface = Typeface.MONOSPACE;
                break;
            case 2:
                mTextsTypeface = Typeface.SANS_SERIF;
                break;
            case 3:
                mTextsTypeface = Typeface.SERIF;
                break;
            default:
                if (defaultTypeface == null) {
                    mTextsTypeface = Typeface.DEFAULT;
                    break;
                }
                mTextsTypeface = defaultTypeface;
                break;
        }
    }

    private void initThumbBitmap() {
        if (mThumbDrawable == null) {
            return;
        }
        if (mThumbDrawable instanceof StateListDrawable) {
            try {
                StateListDrawable listDrawable = (StateListDrawable) mThumbDrawable;
                Class<? extends StateListDrawable> aClass = listDrawable.getClass();
                int stateCount = (int) aClass.getMethod("getStateCount").invoke(listDrawable);
                if (stateCount == 2) {
                    Method getStateSet = aClass.getMethod("getStateSet", int.class);
                    Method getStateDrawable = aClass.getMethod("getStateDrawable", int.class);
                    for (int i = 0; i < stateCount; i++) {
                        int[] stateSet = (int[]) getStateSet.invoke(listDrawable, i);
                        if (stateSet.length > 0) {
                            if (stateSet[0] == android.R.attr.state_pressed) {
                                Drawable stateDrawable = (Drawable) getStateDrawable.invoke(listDrawable, i);
                                mPressedThumbBitmap = getDrawBitmap(stateDrawable, true);
                            } else {
                                //please check your selector drawable's format, please see above to correct.
                                throw new IllegalArgumentException("the state of the selector thumb_img drawable is wrong!");
                            }
                        } else {
                            Drawable stateDrawable = (Drawable) getStateDrawable.invoke(listDrawable, i);
                            mThumbBitmap = getDrawBitmap(stateDrawable, true);
                        }
                    }
                } else {
                    //please check your selector drawable's format, please see above to correct.
                    throw new IllegalArgumentException("the format of the selector thumb_img drawable is wrong!");
                }
            } catch (Exception e) {
                mThumbBitmap = getDrawBitmap(mThumbDrawable, true);
                mPressedThumbBitmap = mThumbBitmap;
            }
        } else {
            mThumbBitmap = getDrawBitmap(mThumbDrawable, true);
            mPressedThumbBitmap = mThumbBitmap;
        }
    }

    private void initTickMarksBitmap() {
        if (mTickMarksDrawable instanceof StateListDrawable) {
            StateListDrawable listDrawable = (StateListDrawable) mTickMarksDrawable;
            try {
                Class<? extends StateListDrawable> aClass = listDrawable.getClass();
                Method getStateCount = aClass.getMethod("getStateCount");
                int stateCount = (int) getStateCount.invoke(listDrawable);
                if (stateCount == 2) {
                    Method getStateSet = aClass.getMethod("getStateSet", int.class);
                    Method getStateDrawable = aClass.getMethod("getStateDrawable", int.class);
                    for (int i = 0; i < stateCount; i++) {
                        int[] stateSet = (int[]) getStateSet.invoke(listDrawable, i);
                        if (stateSet.length > 0) {
                            if (stateSet[0] == android.R.attr.state_selected) {
                            } else {
                                //please check your selector drawable's format, please see above to correct.
                                throw new IllegalArgumentException("the state of the selector TickMarks drawable is wrong!");
                            }
                        } else {
                            Drawable stateDrawable = (Drawable) getStateDrawable.invoke(listDrawable, i);
                            mUnselectTickMarksBitmap = getDrawBitmap(stateDrawable, false);
                        }
                    }
                } else {
                    //please check your selector drawable's format, please see above to correct.
                    throw new IllegalArgumentException("the format of the selector TickMarks drawable is wrong!");
                }
            } catch (Exception e) {
                mUnselectTickMarksBitmap = getDrawBitmap(mTickMarksDrawable, false);
            }
        } else {
            mUnselectTickMarksBitmap = getDrawBitmap(mTickMarksDrawable, false);
        }

    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled == isEnabled()) {
            return;
        }
        super.setEnabled(enabled);
        if (isEnabled()) {
            setAlpha(1.0f);
            if (mIndicatorStayAlways) {
                mIndicatorContentView.setAlpha(1.0f);
            }
        } else {
            setAlpha(0.3f);
            if (mIndicatorStayAlways) {
                mIndicatorContentView.setAlpha(0.3f);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        ViewParent parent = getParent();
        if (parent == null) {
            return super.dispatchTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                parent.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                parent.requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("isb_instance_state", super.onSaveInstanceState());
        bundle.putFloat("isb_progress", mProgress);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            setProgress(bundle.getFloat("isb_progress"));
            super.onRestoreInstanceState(bundle.getParcelable("isb_instance_state"));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mUserSeekable || !isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performClick();
                float mX = event.getX();
                if (isTouchSeekBar(mX, event.getY())) {
                    if ((mOnlyThumbDraggable && !isTouchThumb(mX))) {
                        return false;
                    }
                    mIsTouching = true;
                    if (mSeekChangeListener != null) {
                        mSeekChangeListener.onStartTrackingTouch(this);
                    }
                    refreshSeekBar(event);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                refreshSeekBar(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsTouching = false;
                if (mSeekChangeListener != null) {
                    mSeekChangeListener.onStopTrackingTouch(this);
                }
                if (!autoAdjustThumb()) {
                    invalidate();
                }
                if (mIndicator != null) {
                    mIndicator.hide();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void refreshSeekBar(MotionEvent event) {
        refreshThumbCenterXByProgress(calculateProgress(calculateTouchX(adjustTouchX(event))));
        setSeekListener(true);
        invalidate();
        updateIndicator();
    }

    private boolean progressChange() {
        if (mIsFloatProgress) {
            return lastProgress != mProgress;
        } else {
            return Math.round(lastProgress) != Math.round(mProgress);
        }
    }

    private float adjustTouchX(MotionEvent event) {
        float mTouchXCache;
        if (event.getX() < mPaddingLeft) {
            mTouchXCache = mPaddingLeft;
        } else if (event.getX() > mMeasuredWidth - mPaddingRight) {
            mTouchXCache = mMeasuredWidth - mPaddingRight;
        } else {
            mTouchXCache = event.getX();
        }
        return mTouchXCache;
    }

    private float calculateProgress(float touchX) {
        lastProgress = mProgress;
        mProgress = mMin + (getAmplitude()) * (touchX - mPaddingLeft) / mSeekLength;
        return mProgress;
    }

    private float calculateTouchX(float touchX) {
        float touchXTemp = touchX;
        //make sure the seek bar to seek smoothly always
        // while the tick's count is less than 3(tick's count is 1 or 2.).
        if (mTicksCount > 2 && !mSeekSmoothly) {
            int touchBlockSize = Math.round((touchX - mPaddingLeft) / mSeekBlockLength);
            touchXTemp = mSeekBlockLength * touchBlockSize + mPaddingLeft;
        }
        if (mR2L) {
            return mSeekLength - touchXTemp + 2 * mPaddingLeft;
        }
        return touchXTemp;
    }

    private boolean isTouchSeekBar(float mX, float mY) {
        if (mFaultTolerance == -1) {
            mFaultTolerance = SizeUtils.dp2px(mContext, 5);
        }
        boolean inWidthRange = mX >= (mPaddingLeft - 2 * mFaultTolerance) && mX <= (mMeasuredWidth - mPaddingRight + 2 * mFaultTolerance);
        boolean inHeightRange = mY >= mProgressTrack.top - mThumbTouchRadius - mFaultTolerance && mY <= mProgressTrack.top + mThumbTouchRadius + mFaultTolerance;
        return inWidthRange && inHeightRange;
    }

    private boolean isTouchThumb(float mX) {
        float rawTouchX;
        refreshThumbCenterXByProgress(mProgress);
        if (mR2L) {
            rawTouchX = mBackgroundTrack.right;
        } else {
            rawTouchX = mProgressTrack.right;
        }
        return rawTouchX - mThumbSize / 2f <= mX && mX <= rawTouchX + mThumbSize / 2f;
    }

    private void updateIndicator() {
        if (mIndicatorStayAlways) {
            updateStayIndicator();
        } else {
            if (mIndicator == null) {
                return;
            }
            mIndicator.iniPop();
            if (mIndicator.isShowing()) {
                mIndicator.update(getThumbCenterX());
            } else {
                mIndicator.show(getThumbCenterX());
            }
        }
    }

    private void initIndicatorContentView() {
        if (mShowIndicatorType == IndicatorType.NONE) {
            return;
        }
        if (mIndicator == null) {
            mIndicator = new Indicator(mContext,
                    this,
                    mIndicatorColor,
                    mShowIndicatorType,
                    mIndicatorTextSize,
                    mIndicatorTextColor,
                    mIndicatorContentView,
                    mIndicatorTopContentView);
            this.mIndicatorContentView = mIndicator.getInsideContentView();
        }

    }

    private void updateStayIndicator() {
        if (!mIndicatorStayAlways || mIndicator == null) {
            return;
        }
//        mIndicator.setProgressTextView(getIndicatorTextString());
        mIndicator.setProgressTextView(mIndicatorTextFormat);
        mIndicatorContentView.measure(0, 0);
        int measuredWidth = mIndicatorContentView.getMeasuredWidth();
        float thumbCenterX = getThumbCenterX();

        if (mScreenWidth == -1) {
            DisplayMetrics metric = new DisplayMetrics();
            WindowManager systemService = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            if (systemService != null) {
                systemService.getDefaultDisplay().getMetrics(metric);
                mScreenWidth = metric.widthPixels;
            }
        }
        int indicatorOffset;
        int arrowOffset;
        if (measuredWidth / 2 + thumbCenterX > mMeasuredWidth) {
            indicatorOffset = mMeasuredWidth - measuredWidth;
            arrowOffset = (int) (thumbCenterX - indicatorOffset - measuredWidth / 2);
        } else if (thumbCenterX - measuredWidth / 2 < 0) {
            indicatorOffset = 0;
            arrowOffset = -(int) (measuredWidth / 2 - thumbCenterX);
        } else {
            indicatorOffset = (int) (getThumbCenterX() - measuredWidth / 2);
            arrowOffset = 0;
        }

        mIndicator.updateIndicatorLocation(indicatorOffset);
        mIndicator.updateArrowViewLocation(arrowOffset);
    }

    private boolean autoAdjustThumb() {
        if (mTicksCount < 3 || !mSeekSmoothly) {//it is not necessary to adjust while count less than 2.
            return false;
        }
        if (!mAdjustAuto) {
            return false;
        }
        final int closestIndex = getClosestIndex();
        final float touchUpProgress = mProgress;
        ValueAnimator animator = ValueAnimator.ofFloat(0, Math.abs(touchUpProgress - mProgressArr[closestIndex]));
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lastProgress = mProgress;
                if (touchUpProgress - mProgressArr[closestIndex] > 0) {
                    mProgress = touchUpProgress - (Float) animation.getAnimatedValue();
                } else {
                    mProgress = touchUpProgress + (Float) animation.getAnimatedValue();
                }
                refreshThumbCenterXByProgress(mProgress);
                //the auto adjust was happened after user touched up, so from user is false.
                setSeekListener(false);
                if (mIndicator != null && mIndicatorStayAlways) {
                    mIndicator.refreshProgressText();
                    updateStayIndicator();
                }
                invalidate();
            }
        });
        return true;
    }

    /**
     * transfer the progress value to string type
     */
    private String getProgressString(float progress) {
        if (mIsFloatProgress) {
            return FormatUtils.fastFormat(progress, mScale);
        } else {
            return String.valueOf(Math.round(progress));
        }
    }

    private int getClosestIndex() {
        int closestIndex = 0;
        float amplitude = Math.abs(mMax - mMin);
        for (int i = 0; i < mProgressArr.length; i++) {
            float amplitudeTemp = Math.abs(mProgressArr[i] - mProgress);
            if (amplitudeTemp <= amplitude) {
                amplitude = amplitudeTemp;
                closestIndex = i;
            }
        }
        return closestIndex;
    }

    private float getAmplitude() {
        return (mMax - mMin) > 0 ? (mMax - mMin) : 1;
    }

    private void setSeekListener(boolean formUser) {
        if (mSeekChangeListener == null) {
            return;
        }
        if (progressChange()) {
            mSeekChangeListener.onSeeking(collectParams(formUser));
        }
    }

    private SeekParams collectParams(boolean formUser) {
        if (mSeekParams == null) {
            mSeekParams = new SeekParams(this);
        }
        mSeekParams.progress = getProgress();
        mSeekParams.progressFloat = getProgressFloat();
        mSeekParams.fromUser = formUser;
        //for discrete series seek bar
        if (mTicksCount > 2) {
            int rawThumbPos = getThumbPosOnTick();
            if (mShowTickText && mTickTextsArr != null) {
                mSeekParams.tickText = mTickTextsArr[rawThumbPos];
            }
            if (mR2L) {
                mSeekParams.thumbPosition = mTicksCount - rawThumbPos - 1;
            } else {
                mSeekParams.thumbPosition = rawThumbPos;
            }
        }
        return mSeekParams;
    }

    private void apply(Builder builder) {
        //seek bar
        this.mMax = builder.max;
        this.mMin = builder.min;
        this.mProgress = builder.progress;
        this.mIsFloatProgress = builder.progressValueFloat;
        this.mTicksCount = builder.tickCount;
        this.mSeekSmoothly = builder.seekSmoothly;
        this.mR2L = builder.r2l;
        this.mUserSeekable = builder.userSeekable;
        this.mClearPadding = builder.clearPadding;
        this.mOnlyThumbDraggable = builder.onlyThumbDraggable;
        //indicator
        this.mShowIndicatorType = builder.showIndicatorType;
        this.mIndicatorColor = builder.indicatorColor;
        this.mIndicatorTextColor = builder.indicatorTextColor;
        this.mIndicatorTextSize = builder.indicatorTextSize;
        this.mIndicatorContentView = builder.indicatorContentView;
        this.mIndicatorTopContentView = builder.indicatorTopContentView;
        //track
        this.mBackgroundTrackSize = builder.trackBackgroundSize;
        this.mBackgroundTrackColor = builder.trackBackgroundColor;
        this.mProgressTrackSize = builder.trackProgressSize;
        this.mProgressTrackColor = builder.trackProgressColor;
        this.mTrackRoundedCorners = builder.trackRoundedCorners;
        //thumb_img
        this.mThumbSize = builder.thumbSize;
        this.mThumbDrawable = builder.thumbDrawable;
        this.mThumbTextColor = builder.thumbTextColor;
        initThumbColor(builder.thumbColorStateList, builder.thumbColor);
        this.mShowThumbText = builder.showThumbText;
        //tickMarks
//        this.mShowTickMarksType = builder.showTickMarksType;
        this.mTickMarksSize = builder.tickMarksSize;
        this.mTickMarksDrawable = builder.tickMarksDrawable;
//        this.mTickMarksEndsHide = builder.tickMarksEndsHide;
//        this.mTickMarksSweptHide = builder.tickMarksSweptHide;
        initTickMarksColor(builder.tickMarksColorStateList, builder.tickMarksColor);
        //tickTexts
        this.mShowTickText = builder.showTickText;
        this.mTickTextsSize = builder.tickTextsSize;
        this.mTickTextsCustomArray = builder.tickTextsCustomArray;
        this.mTextsTypeface = builder.tickTextsTypeFace;
        initTickTextsColor(builder.tickTextsColorStateList, builder.tickTextsColor);
    }

    /**
     * first showing when initial for indicator stay always.
     */
    void showStayIndicator() {
        mIndicatorContentView.setVisibility(INVISIBLE);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = new AlphaAnimation(0.1f, 1.0f);
                animation.setDuration(180);
                mIndicatorContentView.setAnimation(animation);
                updateStayIndicator();
                mIndicatorContentView.setVisibility(VISIBLE);
            }
        }, 300);
    }

    void setIndicatorStayAlways(boolean indicatorStayAlways) {
        this.mIndicatorStayAlways = indicatorStayAlways;
    }

    View getIndicatorContentView() {
        return mIndicatorContentView;
    }

    Pair<String, Integer> getIndicatorTextString() {
        if (mIndicatorTextFormat != null && mIndicatorTextFormat.contains(FORMAT_TICK_TEXT)) {
            if (mTicksCount > 2 && mTickTextsArr != null) {
                String text = mIndicatorTextFormat.replace(FORMAT_TICK_TEXT, mTickTextsArr[getThumbPosOnTick()]);
                return new Pair<>(text, getmIndicatorColor());
            }
        } else if (mIndicatorTextFormat != null && mIndicatorTextFormat.contains(FORMAT_PROGRESS)) {
            String text = mIndicatorTextFormat.replace(FORMAT_PROGRESS, getProgressString(mProgress));
            return new Pair<>(text, getmIndicatorColor());
        }
        return new Pair<>(getProgressString(mProgress), getmIndicatorColor());
    }

    Integer getmIndicatorColor() {
        return typeColor;
    }

    /*------------------API START-------------------*/

    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    /**
     * get current indicator
     *
     * @return the indicator
     */
    public Indicator getIndicator() {
        return mIndicator;
    }

    /**
     * get the tick' count
     *
     * @return tick' count
     */
    public int getTickCount() {
        return mTicksCount;
    }

    /**
     * Get the seek bar's current level of progress in float type.
     *
     * @return current progress in float type.
     */
    public synchronized float getProgressFloat() {
        BigDecimal bigDecimal = BigDecimal.valueOf(mProgress);
        return bigDecimal.setScale(mScale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * Get the seek bar's current level of progress in int type.
     *
     * @return progress in int type.
     */
    public int getProgress() {
        return Math.round(mProgress);
    }

    /**
     * @return the upper limit of this seek bar's range.
     */
    public float getMax() {
        return mMax;
    }

    /**
     * the lower limit of this seek bar's range.
     *
     * @return the seek bar min value
     */
    public float getMin() {
        return mMin;
    }

    /**
     * the listener to listen the seeking params changing.
     *
     * @return seeking listener.
     */
    public OnSeekChangeListener getOnSeekChangeListener() {
        return mSeekChangeListener;
    }

    /**
     * Sets the current progress to the specified value.also,
     * if the seek bar's tick'count is larger than 2,the progress will adjust to the closest tick's progress auto.
     *
     * @param progress a new progress value , if the new progress is less than min ,
     *                 it will set to min;
     *                 if over max ,will be max.
     */
    public synchronized void setProgress(float progress) {
        lastProgress = mProgress;
        mProgress = progress < mMin ? mMin : (progress > mMax ? mMax : progress);
        //adjust to the closest tick's progress
        if ((!mSeekSmoothly) && mTicksCount > 2) {
            mProgress = mProgressArr[getClosestIndex()];
        }
        setSeekListener(false);
        refreshThumbCenterXByProgress(mProgress);
        postInvalidate();
        updateStayIndicator();
    }

    /**
     * Set the upper range of the seek bar
     *
     * @param max the upper range of this progress bar.
     */
    public synchronized void setMax(float max) {
        this.mMax = Math.max(mMin, max);
        initProgressRangeValue();
        collectTicksInfo();
        refreshSeekBarLocation();
        invalidate();
        updateStayIndicator();
    }

    /**
     * Set the min value for SeekBar
     *
     * @param min the min value , if is larger than max, will set to max.
     */
    public synchronized void setMin(float min) {
        this.mMin = Math.min(mMax, min);
        initProgressRangeValue();
        collectTicksInfo();
        refreshSeekBarLocation();
        invalidate();
        updateStayIndicator();
    }

    /**
     * compat app local change
     *
     * @param isR2L True if see form right to left on the screen.
     */
    public void setR2L(boolean isR2L) {
        this.mR2L = isR2L;
        requestLayout();
        invalidate();
        updateStayIndicator();
    }

    /**
     * Set a new thumb_img drawable.
     *
     * @param drawable the drawable for thumb_img,selector drawable is ok.
     *                 selector format:
     */
    //<?xml version="1.0" encoding="utf-8"?>
    //<selector xmlns:android="http://schemas.android.com/apk/res/android">
    //<item android:drawable="@drawable/ic_launcher" android:state_pressed="true" />  <!--this drawable is for thumb_img when pressing-->
    //<item android:drawable="@drawable/ic_launcher_round" />  < !--for thumb_img when normal-->
    //</selector>
    public void setThumbDrawable(Drawable drawable) {
        if (drawable == null) {
            this.mThumbDrawable = null;
            this.mThumbBitmap = null;
            this.mPressedThumbBitmap = null;
        } else {
            this.mThumbDrawable = drawable;
            this.mThumbRadius = Math.min(SizeUtils.dp2px(mContext, THUMB_MAX_WIDTH), mThumbSize) / 2.0f;
            this.mThumbTouchRadius = mThumbRadius;
            this.mCustomDrawableMaxHeight = Math.max(mThumbTouchRadius, mTickRadius) * 2.0f;
            initThumbBitmap();
        }
        requestLayout();
        invalidate();
    }

    /**
     * call this will do not draw thumb_img, true if hide.
     */
    public void hideThumb(boolean hide) {
        mHideThumb = hide;
        invalidate();
    }

    /**
     * call this will do not draw the text which below thumb_img. true if hide.
     */
    public void hideThumbText(boolean hide) {
        mShowThumbText = !hide;
        invalidate();
    }

    /**
     * set the seek bar's thumb_img's color.
     */

    public void setTickMarksDrawable(Drawable drawable) {
        if (drawable == null) {
            this.mTickMarksDrawable = null;
            this.mUnselectTickMarksBitmap = null;
//            this.mSelectTickMarksBitmap = null;
        } else {
            this.mTickMarksDrawable = drawable;
            this.mTickRadius = Math.min(SizeUtils.dp2px(mContext, THUMB_MAX_WIDTH), mTickMarksSize) / 2.0f;
            this.mCustomDrawableMaxHeight = Math.max(mThumbTouchRadius, mTickRadius) * 2.0f;
            initTickMarksBitmap();
        }
        invalidate();
    }

    /**
     * set the seek bar's tick's color.
     *
     * @param tickMarksColor colorInt
     */
    public void tickMarksColor(@ColorInt int tickMarksColor) {
        this.mSelectedTickMarksColor = tickMarksColor;
//        this.mUnSelectedTickMarksColor = tickMarksColor;
        invalidate();
    }


    public void setDecimalScale(int scale) {
        this.mScale = scale;
    }

    public void setIndicatorTextFormat(String format) {
        this.mIndicatorTextFormat = format;
        initTextsArray();
        updateStayIndicator();
    }

    public void setmIndicatorTopContentView(int typeColor) {
        this.typeColor = typeColor;
        mIndicator.setTopContentViewNew(typeColor);

    }


    public void customSectionTrackColor(@NonNull ColorCollector collector) {
        int[] colorArray = new int[mTicksCount - 1 > 0 ? mTicksCount - 1 : 1];
        for (int i = 0; i < colorArray.length; i++) {
            //set the default section color
            colorArray[i] = mBackgroundTrackColor;
        }
        this.mCustomTrackSectionColorResult = collector.collectSectionTrackColor(colorArray);
        this.mSectionTrackColorArray = colorArray;
        invalidate();
    }

    /**
     * Replace the number ticks' texts with your's by String[].
     * Usually, the text array's length your set should equals seek bar's tickMarks' count.
     *
     * @param tickTextsArr The array contains the tick text
     */
    public void customTickTexts(@NonNull String[] tickTextsArr) {
        this.mTickTextsCustomArray = tickTextsArr;
        if (mTickTextsArr != null) {
            for (int i = 0; i < mTickTextsArr.length; i++) {
                String tickText;
                if (i < tickTextsArr.length) {
                    tickText = String.valueOf(tickTextsArr[i]);
                } else {
                    tickText = "";
                }
                int index = i;
                if (mR2L) {
                    index = mTicksCount - 1 - i;
                }
                mTickTextsArr[index] = tickText;
                if (mTextPaint != null && mRect != null) {
                    mTextPaint.getTextBounds(tickText, 0, tickText.length(), mRect);
                    mTickTextsWidth[index] = mRect.width();
                }
            }
            invalidate();
        }
    }

    public void setOnSeekChangeListener(@NonNull OnSeekChangeListener listener) {
        this.mSeekChangeListener = listener;
    }


}