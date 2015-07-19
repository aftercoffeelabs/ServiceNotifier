package com.example.aram.servicenotifier.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Interface Class AnimatedButton
 */
public interface AnimatedButton {

    /**
     * Read XML attributes, create all shape objects and initialize properties
     */
    public void init(final Context context, AttributeSet attrs);

    /**
     * Interface definition for starting button toggle animation
     */
    void clicked();

    /**
     * Interface definition for setting the button state to On
     */
    void setStartPositionOn();

    /**
     * Interface definition for suspending all animation threads
     */
    void stopAnimation();

    /**
     * Interface definition for a callback to be invoked when the toggle animation ends
     */
    interface AnimationStateListener {

        // Called when the button toggle animation ends
        void onToggleAnimationEnd();
    }
}
