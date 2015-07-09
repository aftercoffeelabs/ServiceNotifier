package com.example.aram.servicenotifier.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.aram.servicenotifier.R;
import com.example.aram.servicenotifier.util.CirclePropertyHolder;

/**
 * Class FancyControlButton
 */
public class FancyControlButton extends View implements AnimatedButton, View.OnClickListener {

    /**
     * Member variables
     */
    private static final int EXPAND_INCREMENT = 1;
    private static final int FILL_DELAY_MS  = 2;
    private static final int FINAL_FILL_ALPHA = 120;

    private boolean mButtonOn = false;

    private int mAlphaIncrement;

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

    /**
     * FancyControlButton constructor
     */
    public FancyControlButton (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

        setOnClickListener(this);
    }

    /**
     * onClick
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.control_button) {

            // Toggle state
            mButtonOn = !mButtonOn;
            runAnimation();

            //this.animate().scaleX(2.0f).scaleY(2.0f);
        }
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

        // Create the 2d Circle objects to represent the control button
        mOffStateCircle = new CirclePropertyHolder(0, 0, mAttrOffStateButtonSize, new Paint());
        mOffStateCircle.paint().setStrokeWidth(mAttrOffStateButtonStrokeWidth);
        mOffStateCircle.paint().setColor(mAttrOffStateButtonColor);
        mOffStateCircle.paint().setFlags(Paint.ANTI_ALIAS_FLAG);
        mOffStateCircle.paint().setStyle(Paint.Style.STROKE);

        // The ON state circle is initially fully transparent and the same
        // size as the OFF state circle
        mOnStateCircle = new CirclePropertyHolder(0, 0, mAttrOffStateButtonSize, new Paint());
        mOnStateCircle.paint().setStrokeWidth(mAttrOnStateButtonStrokeWidth);
        mOnStateCircle.paint().setColor(mAttrOnStateButtonColor);
        mOnStateCircle.paint().setAlpha(255);
        mOnStateCircle.paint().setFlags(Paint.ANTI_ALIAS_FLAG);
        mOnStateCircle.paint().setStyle(Paint.Style.FILL);

//        mAlphaIncrement = 2;
//        mAlphaIncrement = 255 / (
//                (mOnStateCircle.getRadius() - mOffStateCircle.getRadius()) / EXPAND_INCREMENT);
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
        int diameter = (int)Math.min(viewWidth, viewHeight);

        // Get center of view
        int centerX = w / 2;
        int centerY = h / 2;

        // Update 2d circle dimensions
        if (mOffStateCircle != null) {
            mOffStateCircle.setDimensions(centerX, centerY, mAttrOffStateButtonSize, diameter);
        }

        if (mOnStateCircle != null) {
            mOnStateCircle.setDimensions(centerX, centerY, mAttrOffStateButtonSize, diameter);
        }
    }

    /**
     * runAnimation
     */
    @Override
    public void runAnimation() {

        // TODO: prevent new thread from starting again until animation cycle completes

        (new Thread(new AnimationRunnable())).start();
    }

    /**
     * onDraw
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the OFF circle
        if (mOffStateCircle.getRadius() == mOnStateCircle.getRadius()) {
            canvas.drawCircle(mOffStateCircle.getCenterX(), mOffStateCircle.getCenterY(),
                    mOffStateCircle.getRadius(), mOffStateCircle.paint());
        } else {

            synchronized (mOnStateCircle) {

                mOnStateCircle.paint().setStyle(Paint.Style.STROKE);
                mOnStateCircle.paint().setAlpha(50);
                mOnStateCircle.paint().setColor(mAttrOnStateButtonStrokeColor);
                canvas.drawCircle(mOnStateCircle.getCenterX(), mOnStateCircle.getCenterY(),
                        mOnStateCircle.getRadius(), mOnStateCircle.paint());

                // Draw the ON circle button fill
                mOnStateCircle.paint().setStyle(Paint.Style.FILL);
                mOnStateCircle.paint().setColor(mAttrOnStateButtonColor);
                mOnStateCircle.paint().setAlpha(255);
                canvas.drawCircle(mOnStateCircle.getCenterX(), mOnStateCircle.getCenterY(),
                        mOnStateCircle.getRadius(), mOnStateCircle.paint());


            }
        }
    }

    /**
     * Inner class AnimationRunnable
     */
    private class AnimationRunnable implements Runnable {

        public void run() {

            Log.d("testing", "Run - start radius is " + Integer.toString(mOnStateCircle.getRadius()));

            boolean isExpanding;

            synchronized (mOnStateCircle) {
                isExpanding = mOnStateCircle.getRadius() < mAttrOnStateButtonSize;
            }

            while (keepRunning(isExpanding, mAttrOnStateButtonSize)) {

                synchronized (mOnStateCircle) {

                    int currentRadius = mOnStateCircle.getRadius();
                    int currentFillAlpha = mOnStateCircle.paint().getAlpha();

                    if (isExpanding) {
                        mOnStateCircle.setRadius(currentRadius + EXPAND_INCREMENT);
                        //mOnStateCircle.paint().setAlpha(currentFillAlpha + mAlphaIncrement);
                    } else {
                        mOnStateCircle.setRadius(currentRadius - EXPAND_INCREMENT);
                        //mOnStateCircle.paint().setAlpha(currentFillAlpha - mAlphaIncrement);
                    }
                }
                postInvalidate();
                SystemClock.sleep(FILL_DELAY_MS);
            }
            Log.d("testing", "Run - end radius is " + Integer.toString(mOnStateCircle.getRadius()));
        }

        private boolean keepRunning(boolean isExpanding, int size) {

            boolean result;

            synchronized (mOnStateCircle) {
                if (isExpanding) {
                    result = mOnStateCircle.getRadius() < size;
                } else {
                    result = mOnStateCircle.getRadius() > mOffStateCircle.getRadius();
                }
            }
            return result;
        }
    }

}
