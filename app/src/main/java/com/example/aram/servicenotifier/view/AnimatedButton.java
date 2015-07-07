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
     * Start animation
     */
    public void runAnimation();
}
