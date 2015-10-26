package com.example.aram.servicenotifier.infrastructure;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Class MyApp
 */
public class MyApp extends Application {

    /***
     * Member Variables
     ***/
    private static Context mAppContext;
    private static Resources mResources;

    /**************************************************************************
     *
     * onCreate()
     *
     * Called by the system before any activities are created
     *************************************************************************/
    @Override
    public void onCreate() {

        super.onCreate();
        mAppContext = this;
        mResources = getResources();
    }

    /**************************************************************************
     *
     * getContext
     *
     * Caution: Do not call this from a Service.
     *
     *************************************************************************/
    public static Context getContext(){

        return mAppContext;
    }

    /**************************************************************************
     *
     * Get Resources
     *
     *************************************************************************/
    public static Resources getRes(){

        return mResources;
    }
}
