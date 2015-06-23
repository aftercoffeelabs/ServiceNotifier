package com.example.aram.servicenotifier.infrastructure;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Aram on 5/25/2015.
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
     *************************************************************************/
    public static Context getContext(){

        return mAppContext;
    }

    /**************************************************************************
     *
     * getContext
     *
     *************************************************************************/
    public static Resources getRes(){

        return mResources;
    }
}
