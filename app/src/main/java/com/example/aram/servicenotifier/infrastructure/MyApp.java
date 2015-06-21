package com.example.aram.servicenotifier.infrastructure;

import android.app.Application;
import android.content.Context;

/**
 * Created by Aram on 5/25/2015.
 */
public class MyApp extends Application {

    /***
     * Member Variables
     ***/
    private static Context _appContext;

    /**************************************************************************
     *
     * onCreate()
     *
     * Called by the system before any activities are created
     *************************************************************************/
    @Override
    public void onCreate() {

        super.onCreate();
        _appContext = this;
    }

    /**************************************************************************
     *
     * getContext
     *
     *************************************************************************/
    public static Context getContext(){

        return _appContext;
    }
}
