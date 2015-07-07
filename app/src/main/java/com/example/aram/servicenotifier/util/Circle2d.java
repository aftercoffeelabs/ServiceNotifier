package com.example.aram.servicenotifier.util;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Class Circle2d
 *
 * Used to draw circular objects on an Android View
 */
public class Circle2d {

    private int mCenterX;
    private int mCenterY;
    private int mRadius;

    private Paint mPaint;

    public Circle2d(int x, int y, int radius) {
        mCenterX = x;
        mCenterY = y;
        mRadius = radius;

        mPaint = new Paint();
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

    public void setDimensions(int centerX, int centerY, int radius) {
        mCenterX = centerX;
        mCenterY = centerY;
        mRadius = radius;
    }
}
