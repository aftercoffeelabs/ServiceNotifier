package com.example.aram.servicenotifier.notifier.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.aram.servicenotifier.notifier.model.ServiceStateHandler;
import com.example.aram.servicenotifier.infrastructure.MyApp;

/**
 * Class SignalMonitorService
 *
 */
public class SignalMonitorService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private TelephonyManager mTelephonyMgr;
    private MyPhoneStateListener mPhoneState;
    private ServiceStateHandler mServiceStateHdlr;

    @Override
    public void onCreate(){
        super.onCreate();

        mServiceStateHdlr = new ServiceStateHandler();

        mPhoneState = new MyPhoneStateListener();
        mTelephonyMgr = (TelephonyManager) MyApp.getContext().getSystemService(Context.TELEPHONY_SERVICE);

        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //
        // TODO - can we just register the telephonyMgr listener here instead of in the Handler?
        //

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here,
        // restart with last intent
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        // Cleanup resources
        // Unregister the listener
        mTelephonyMgr.listen(mPhoneState, PhoneStateListener.LISTEN_NONE);

        mServiceStateHdlr = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        // Called when the overall system is running low on memory
        //
        // TODO: is this useful for performace analysis?

    }
    //
    // Handler Class
    //
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {

            // Register the listener
            mTelephonyMgr.listen(mPhoneState, PhoneStateListener.LISTEN_SERVICE_STATE);
        }
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
                    stateStr += "ALERT: In Service";
                    break;
                case ServiceState.STATE_OUT_OF_SERVICE:
                    stateStr += "ALERT: Out of Service";
                    break;
                case ServiceState.STATE_EMERGENCY_ONLY:
                    stateStr += "ALERT: Emergency Only";
                    break;
                case ServiceState.STATE_POWER_OFF:
                    stateStr += "ALERT: Power off";
                    break;
            }
            mServiceStateHdlr.handleServiceStateChange(stateCode);
            Log.d("testing", stateStr);
        }

    }

}