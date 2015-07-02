package com.example.aram.servicenotifier.notifier.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.aram.servicenotifier.notifier.model.ServiceStateHandler;
import com.example.aram.servicenotifier.util.FileLogger;

/**
 * Class SignalMonitorService
 *
 */
public class SignalMonitorService extends Service {

    private static Context mServiceContext = null;

    private TelephonyManager mTelephonyMgr;
    private MyPhoneStateListener mPhoneState;
    private ServiceStateHandler mServiceStateHdlr;

    private FileLogger mLogger;

    /**
     * Get Service Context
     */
    public static Context getContext(){
        return mServiceContext;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("testing", "Service - onCreate");

        mServiceContext = this;
        mLogger = new FileLogger(mServiceContext);
        mServiceStateHdlr = new ServiceStateHandler();

        mPhoneState = new MyPhoneStateListener();
        mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("testing", "Service - onStartCommand");

        mTelephonyMgr.listen(mPhoneState, PhoneStateListener.LISTEN_SERVICE_STATE);

        // If we get killed, after returning from here, restart with null intent
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        // Cleanup resources
        // Unregister the listener
        mTelephonyMgr.listen(mPhoneState, PhoneStateListener.LISTEN_NONE);
        mTelephonyMgr = null;
        mPhoneState = null;
        mServiceStateHdlr = null;
        mServiceContext = null;
        mLogger = null;
        Log.d("testing", "Service - onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        mLogger.log("LOW MEMORY!");

        // Called when the overall system is running low on memory
        //
        // TODO: is this useful for performace analysis?

    }

    /**
     * Inner Class MyPhoneStateListener
     *
     * Monitor changes in radio signal availability
     */
    public class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onServiceStateChanged(ServiceState state) {

            String stateStr = "";
            int stateCode = state.getState();

            switch (stateCode) {
                case ServiceState.STATE_IN_SERVICE:
                    stateStr += "State: In Service";
                    break;
                case ServiceState.STATE_OUT_OF_SERVICE:
                    stateStr += "State: Out of Service";
                    break;
                case ServiceState.STATE_EMERGENCY_ONLY:
                    stateStr += "State: Emergency Only";
                    break;
                case ServiceState.STATE_POWER_OFF:
                    stateStr += "State: Power off";
                    break;
            }
            mServiceStateHdlr.handleServiceStateChange(stateCode);

            // Debug logging
            Log.d("testing", stateStr);
            mLogger.log(stateStr);
        }
    }
}
