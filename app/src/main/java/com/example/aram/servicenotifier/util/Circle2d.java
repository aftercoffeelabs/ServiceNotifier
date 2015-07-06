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
    private int mFillColor;
    private int mStrokeColor;
    private float mStrokeWidth;
    private Paint mPaint;

    public Circle2d(int x, int y, int radius) {
        mCenterX = x;
        mCenterY = y;
        mRadius = radius;
    }

    public int getCenterX() {
        return mCenterX;
    }

    public void setCenterX(int x) {
        mCenterX = x;
    }

    public int getCenterY() {
        return mCenterY;
    }

    public void setCenterY(int y) {
        mCenterY = y;
    }

    public int getFillColor() {
        return mFillColor;
    }

    public void setFillColor(int color) {
        mFillColor = color;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public void setPaint(Paint paint) {
        mPaint = paint;
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int r) {
        mRadius = r;
    }

    public int getStrokeColor() {
        return mStrokeColor;
    }

    public void setStrokeColor(int color) {
        mStrokeColor = color;
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(float width) {
        mStrokeWidth = width;
    }

    public int getFillColorAlpha() {
        return Color.alpha(mFillColor);
    }

    public int getStrokeColorAlpha() {
        return Color.alpha(mStrokeColor);
    }
}
