package com.example.aram.servicenotifier.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.example.aram.servicenotifier.R;
import com.example.aram.servicenotifier.util.Circle2d;

/**
 * Class FancyControlButton
 */
public class FancyControlButton extends View implements AnimatedButton, View.OnClickListener {

    /**
     * Member variables
     */
    private static final int FILL_INCREMENT = 1;
    private static final int FILL_DELAY = 2; // milliseconds

    private int mAttrOffStateButtonSize;
    private int mAttrOffStateButtonFillColor;
    private int mAttrOffStateButtonStrokeColor;
    private int mAttrOnStateButtonSize;
    private int mAttrOnStateButtonFillColor;
    private int mAttrOnStateButtonStrokeColor;

    private Circle2d mOffStateCircle;   // represents the OFF state of the button
    private Circle2d mOnStateCircle;    // represents the ON state of the button

    /**
     * FancyControlButton constructor
     */
    public FancyControlButton (Context context, AttributeSet attrs) {
        super(context, attrs);
        initProperties(context, attrs);

        setOnClickListener(this);
    }

    /**
     * onClick
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.control_button) {
            runAnimation();
        }
    }

    /**
     * initProperties
     */
    @Override
    public void initProperties(Context context, AttributeSet attrs) {

        // Read button attributes from layout XML
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.FancyControlButton, 0, 0);

        // TODO - create static conts defaults to use in the calls below
        mAttrOffStateButtonSize = (int)a.getDimension(
                R.styleable.FancyControlButton_offStateSize, 0.0f);

        mAttrOffStateButtonFillColor = a.getColor(
                R.styleable.FancyControlButton_offStateFillColor, 0xff000000);

        mAttrOffStateButtonStrokeColor = a.getColor(
                R.styleable.FancyControlButton_offStateStrokeColor, 0xff000000);

        mAttrOnStateButtonSize = (int)a.getDimension(
                R.styleable.FancyControlButton_onStateSize, 0.0f);

        mAttrOnStateButtonFillColor = a.getColor(
                R.styleable.FancyControlButton_onStateFillColor, 0xff000000);

        mAttrOnStateButtonStrokeColor = a.getColor(
                R.styleable.FancyControlButton_onStateStrokeColor, 0xff000000);

        a.recycle();


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int x = w / 2;
        int y = h / 2;

        // Create the 2d Circle objects to represent the control button
        mOffStateCircle = new Circle2d(x, y, mAttrOffStateButtonSize);
        mOnStateCircle = new Circle2d(x, y, mAttrOnStateButtonSize);
    }

    /**
     * runAnimation
     */
    @Override
    public void runAnimation() {

    }

}
