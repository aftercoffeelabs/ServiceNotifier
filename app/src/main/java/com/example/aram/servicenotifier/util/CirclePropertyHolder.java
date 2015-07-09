package com.example.aram.servicenotifier.util;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Class Circle2d
 *
 * Used to draw circular objects on an Android View
 */
public class CirclePropertyHolder {

    private int mCenterX;
    private int mCenterY;
    private int mRadius;

    // Max size circle can be in view
    private int mMaxDiameter;

    private Paint mPaint;

    public CirclePropertyHolder(int x, int y, int radius, Paint p) {
        mCenterX = x;
        mCenterY = y;
        mRadius = radius;

        mPaint = p;
    }

    public int getCenterX() { return mCenterX; }

    public void setCenterX(int x) {
        mCenterX = x;
    }

    public int getCenterY() {
        return mCenterY;
    }

    public void setCenterY(int y) {
        mCenterY = y;
    }

    public Paint paint() {
        return mPaint;
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int r) {
        mRadius = r;
    }

    public int getMaxDiameter() {return mMaxDiameter; }

    public void setMaxDiameter(int d) {mMaxDiameter = d; }

    public void setDimensions(int centerX, int centerY, int radius, int d) {
        mCenterX = centerX;
        mCenterY = centerY;
        mRadius = radius;
        mMaxDiameter = d;
    }
}
