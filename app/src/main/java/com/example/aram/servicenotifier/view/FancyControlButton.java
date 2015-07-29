package com.example.aram.servicenotifier.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.aram.servicenotifier.R;
import com.example.aram.servicenotifier.util.CirclePropertyHolder;

/**
 * Class FancyControlButton
 */
public class FancyControlButton extends View implements AnimatedButton,
        AnimatedButton.AnimationStateListener {

    /**
     * Member variables
     */
    private static final int EXPAND_INCREMENT = 1;
    private static final int FILL_DELAY_MS  = 4;
    private static final int RIPPLE_DELAY_MS = 33;
    private static final int MIN_ALPHA = 0;
    private static final int MAX_ALPHA = 255;

    private volatile boolean mIsRippleAnimationRunning;
    private volatile boolean mButtonOn;

    private boolean mButtonStartPositionOn;

    private int mFillAlphaIncrement;
    private int mRippleAlphaIncrement;

    // Default values to be overridden by xml attributes
    private int mAttrOffStateButtonSize = 100;
    private int mAttrOffStateButtonColor = 0xff000000;
    private float mAttrOffStateButtonStrokeWidth = 0.5f;

    private int mAttrOnStateButtonSize = 200;
    private int mAttrOnStateButtonColor = 0xff000000;
    private int mAttrOnStateButtonStrokeColor = 0xff000000;
    private float mAttrOnStateButtonStrokeWidth = 10.0f;

    private CirclePropertyHolder mOffStateCircle;   // represents the OFF state of the button
    private CirclePropertyHolder mOnStateCircle;    // represents the ON state of the button
    private CirclePropertyHolder mRipple;           // ripple effect

    private Bitmap mBitmap;
    private Paint mTextPaint = new Paint();;

    /**
     * FancyControlButton constructor
     */
    public FancyControlButton (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * init
     */
    @Override
    public void init(Context context, AttributeSet attrs) {

        // Read button attributes from layout XML
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.FancyControlButton, 0, 0);

        mAttrOffStateButtonSize = (int)a.getDimension(
                R.styleable.FancyControlButton_offStateSize, mAttrOffStateButtonSize);

        mAttrOffStateButtonColor = a.getColor(
                R.styleable.FancyControlButton_offStateColor, mAttrOffStateButtonColor);

        mAttrOffStateButtonStrokeWidth = a.getDimension(
                R.styleable.FancyControlButton_offStateStrokeWidth, mAttrOffStateButtonStrokeWidth);

        mAttrOnStateButtonSize = (int)a.getDimension(
                R.styleable.FancyControlButton_onStateSize, mAttrOnStateButtonSize);

        mAttrOnStateButtonColor = a.getColor(
                R.styleable.FancyControlButton_onStateColor, mAttrOnStateButtonColor);

        mAttrOnStateButtonStrokeColor = a.getColor(
                R.styleable.FancyControlButton_onStateStrokeColor, mAttrOnStateButtonStrokeColor);

        mAttrOnStateButtonStrokeWidth = a.getDimension(
                R.styleable.FancyControlButton_onStateStrokeWidth, mAttrOnStateButtonStrokeWidth);

        a.recycle();

        // The OFF state circle
        mOffStateCircle = new CirclePropertyHolder(0, 0, mAttrOffStateButtonSize, new Paint());
        mOffStateCircle.paint().setStrokeWidth(mAttrOffStateButtonStrokeWidth);
        mOffStateCircle.paint().setColor(mAttrOffStateButtonColor);
        mOffStateCircle.paint().setFlags(Paint.ANTI_ALIAS_FLAG);
        mOffStateCircle.paint().setStyle(Paint.Style.STROKE);

        // The ON state circle
        // Initially fully transparent and the same size as the OFF state circle
        mOnStateCircle = new CirclePropertyHolder(0, 0, mAttrOffStateButtonSize, new Paint());
        mOnStateCircle.paint().setStrokeWidth(mAttrOnStateButtonStrokeWidth);
        mOnStateCircle.paint().setColor(mAttrOnStateButtonColor);
        mOnStateCircle.paint().setAlpha(MIN_ALPHA);
        mOnStateCircle.paint().setFlags(Paint.ANTI_ALIAS_FLAG);
        mOnStateCircle.paint().setStyle(Paint.Style.FILL);

        // The Ripple effect
        float strokeWidthDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1.0f, getResources().getDisplayMetrics());

        mRipple = new CirclePropertyHolder(0, 0, mAttrOffStateButtonSize, new Paint());
        mRipple.paint().setStrokeWidth(strokeWidthDp);
        mRipple.paint().setColor(getResources().getColor(R.color.light_green_200));
        mRipple.paint().setAlpha(MIN_ALPHA);
        mRipple.paint().setFlags(Paint.ANTI_ALIAS_FLAG);
        mRipple.paint().setStyle(Paint.Style.STROKE);

        // Compute alpha value interpolation for the ON circle fill
        mFillAlphaIncrement = (int)Math.ceil(
                (double) MAX_ALPHA / (double) (mAttrOnStateButtonSize - mAttrOffStateButtonSize));

        // Compute alpha value interpolation for the ripple effect
        mRippleAlphaIncrement = (int)Math.floor(
                (double)MAX_ALPHA / (double)((mAttrOnStateButtonSize - mAttrOffStateButtonSize) / 2));

        mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.btn_bars);

        mButtonOn = false;
        mIsRippleAnimationRunning = false;

//        mTextPaint.setColor(getResources().getColor(R.color.white));
//        mTextPaint.setTextAlign(Paint.Align.CENTER);
//        mTextPaint.setTextSize(30);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Get view dimensions with padding factored in
        int padX = getPaddingLeft() + getPaddingRight();
        int padY = getPaddingTop() + getPaddingBottom();

        int viewWidth = w - padX;
        int viewHeight = h - padY;

        // Max size of circle to fit within View
        int diameter = Math.min(viewWidth, viewHeight);

        // Get center of view
        int centerX = w / 2;
        int centerY = h / 2;

        // Update 2d circle dimensions
        if (mOffStateCircle != null) {
            mOffStateCircle.setDimensions(centerX, centerY, mAttrOffStateButtonSize, diameter);
        }

        if (mOnStateCircle != null) {
            if (!mButtonStartPositionOn) {
                mOnStateCircle.setDimensions(centerX, centerY, mAttrOffStateButtonSize, diameter);
            } else {
                // TODO: does this work??
                mOnStateCircle.setDimensions(centerX, centerY, mAttrOnStateButtonSize, diameter);
                // This path of the code effectively toggles off the button, so toggle it back on!
                mButtonOn = false;
                toggleOn();
                mButtonStartPositionOn = false;
            }
        }

        if (mRipple != null) {
            mRipple.setDimensions(centerX, centerY, mAttrOffStateButtonSize, diameter);
        }

        // After view is initialized, check if the button needs to be automatically
        // set to the ON position for the user. This happens in the case where the
        // service is already running prior to the app starting
//        Log.d("testing", "onSizeChanged");
//        if (mButtonStartPositionOn == true && !mButtonOn) {
//            toggleOn();
//            mButtonStartPositionOn = false;
//        }
    }

    /**
     * setStartPositionOn
     */
    @Override
    public void setStartPositionOn() {

        mButtonStartPositionOn = true;
        toggleOn();
    }

    @Override
    public void stopAnimation() {

        // Stop all looping animations here
        mIsRippleAnimationRunning = false;
        resetRippleProperties();
    }

    /**
     * clicked
     */
    @Override
    public void clicked() {

        // Toggle button state
        mButtonOn = !mButtonOn;

        // Stop all animations if set to Off
        if (!mButtonOn) {
            stopAnimation();
        }

        startToggleAnimation();
    }

    /**
     * toggleOn
     */
    private void toggleOn() {

//        if (!mButtonOn) { // TODO: do we need this check?

            mButtonOn = true;

            mOnStateCircle.paint().setAlpha(MAX_ALPHA);
            mOnStateCircle.setRadius(mAttrOnStateButtonSize);

            mOffStateCircle.paint().setStyle(Paint.Style.FILL);
            mOffStateCircle.setRadius(mAttrOffStateButtonSize);

            mRipple.paint().setAlpha(MIN_ALPHA);
            mRipple.setRadius(mAttrOffStateButtonSize);

            // Start ripple animation
            startRippleAnimation();

            invalidate();
//        }
    }

    /**
     * startToggleAnimation
     */
    private void startToggleAnimation() {

        // TODO: prevent new thread from starting again until animation cycle completes
        (new Thread(new ToggleAnimationRunnable(this))).start();
    }

    /**
     * onToggleAnimationEnd
     */
    @Override
    public void onToggleAnimationEnd() {

        // Start looping the ripple effect animation once the button
        // is in the On position
        if (mButtonOn) {
            startRippleAnimation();
        }
    }

    /**
     * startRippleAnimation
     */
    public void startRippleAnimation() {

        // If ripple is not running, start it!
        if (!mIsRippleAnimationRunning) {
            mIsRippleAnimationRunning = true;
            resetRippleProperties();
            (new Thread(new RippleAnimationRunnable())).start();
        }
    }

    /**
     * resetRippleProperties
     */
    private void resetRippleProperties() {

        if (mRipple != null) {
            mRipple.paint().setAlpha(MIN_ALPHA);
            mRipple.setRadius(mAttrOffStateButtonSize);
        }
    }

    /**
     * onDraw
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int offRadius;
        int onRadius;

        offRadius = mOffStateCircle.getRadius();
        onRadius = mOnStateCircle.getRadius();

        // Draw the OFF circle
        if (onRadius == offRadius) {

            mOffStateCircle.paint().setColor(mAttrOffStateButtonColor);
            mOffStateCircle.paint().setStyle(Paint.Style.STROKE);
            canvas.drawCircle(mOffStateCircle.getCenterX(), mOffStateCircle.getCenterY(),
                    mOffStateCircle.getRadius(), mOffStateCircle.paint());

        } else {

            // Draw the ON circle
            canvas.drawCircle(mOnStateCircle.getCenterX(), mOnStateCircle.getCenterY(),
                    mOnStateCircle.getRadius(), mOnStateCircle.paint());

            mOffStateCircle.paint().setColor(mAttrOffStateButtonColor);
            mOffStateCircle.paint().setStyle(Paint.Style.FILL);
            canvas.drawCircle(mOffStateCircle.getCenterX(), mOffStateCircle.getCenterY(),
                    mOffStateCircle.getRadius(), mOffStateCircle.paint());

            // Draw the ripple effect
            canvas.drawCircle(mRipple.getCenterX(), mRipple.getCenterY(),
                    mRipple.getRadius(), mRipple.paint());
        }

        // Always draw signal icon
        canvas.drawBitmap(mBitmap, ((getWidth() - mBitmap.getWidth()) / 2),
                ((getHeight() - mBitmap.getHeight()) / 2), null);

//        float offset = mBitmap.getWidth() * 0.80f;
//        canvas.drawText("-89 dBm", mOnStateCircle.getCenterX(), mOnStateCircle.getCenterY() + offset, mTextPaint);
    }

    /**
     * Inner Class ToggleAnimationRunnable
     */
    private class ToggleAnimationRunnable implements Runnable {

        private final AnimationStateListener mAnimationStateListener;

        public ToggleAnimationRunnable(final AnimationStateListener listener) {
            mAnimationStateListener = listener;
        }

        public void run() {

            boolean isExpanding;

            isExpanding = mOnStateCircle.getRadius() < mAttrOnStateButtonSize;

            while (keepRunning(isExpanding)) {

                int currentRadius = mOnStateCircle.getRadius();
                int currentFillAlpha = mOnStateCircle.paint().getAlpha();

                if (isExpanding) {
                    mOnStateCircle.setRadius(currentRadius + EXPAND_INCREMENT);

                    // Prevent alpha value overflow
                    if ((currentFillAlpha + mFillAlphaIncrement) <= MAX_ALPHA) {
                        mOnStateCircle.paint().setAlpha((currentFillAlpha + mFillAlphaIncrement));
                    }
                } else {
                    mOnStateCircle.setRadius(currentRadius - EXPAND_INCREMENT);

                    // Prevent alpha value overflow
                    if ((currentFillAlpha - mFillAlphaIncrement) >= MIN_ALPHA) {
                        mOnStateCircle.paint().setAlpha((currentFillAlpha - mFillAlphaIncrement));
                    }
                }

                postInvalidate();
                SystemClock.sleep(FILL_DELAY_MS);
            }
            // Notify animation end listeners
            mAnimationStateListener.onToggleAnimationEnd();
        }

        private boolean keepRunning(boolean isExpanding) {

            boolean result;

            if (isExpanding) {
                result = mOnStateCircle.getRadius() < mAttrOnStateButtonSize;
            } else {
                result = mOnStateCircle.getRadius() > mAttrOffStateButtonSize;
            }

            return result;
        }
    }

    /**
     * Inner class RippleAnimationRunnable
     */
    private class RippleAnimationRunnable implements Runnable {

        public void run() {

            int currentAlpha;
            int currentRadius;
            int newRadius;
            int sign = 1; // start positive

            while (mIsRippleAnimationRunning) {

                currentRadius = mRipple.getRadius();
                newRadius = currentRadius + EXPAND_INCREMENT;

                if (newRadius <= mAttrOnStateButtonSize) {
                    // Keep expanding ripple out
                    mRipple.setRadius(newRadius);

                    currentAlpha = mRipple.paint().getAlpha();

                    // At halfway point of radius expansion, flip the sign to decrease the alpha
                    sign = (currentRadius >= (
                            mAttrOnStateButtonSize + mAttrOffStateButtonSize) / 2) ? -1 : 1;

                    mRipple.paint().setAlpha(currentAlpha + (sign * mRippleAlphaIncrement));
                } else {
                    // Go back to starting position
                    resetRippleProperties();
                }

                postInvalidate();
                SystemClock.sleep(RIPPLE_DELAY_MS);
            }
            Log.d("testing", "ripple thread exit");
        }
    }
}
