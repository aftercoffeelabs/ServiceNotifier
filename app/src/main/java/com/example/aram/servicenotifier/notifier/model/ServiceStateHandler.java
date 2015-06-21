package com.example.aram.servicenotifier.notifier.model;

import android.os.Handler;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Class ServiceStateHandler
 */
public class ServiceStateHandler {

    private AlertCriteria mAlertCriteria;
    private Handler mHandler = new Handler();
    private final PersistenceVerifierRunnable mRunnable;
    private Notifier mNotifier;

    /**
     * Default class constructor.
     */
    public ServiceStateHandler(){
        mRunnable = new PersistenceVerifierRunnable(this);
        mNotifier = Notifier.instance();
    }

    /**
     * Handles service state changes
     */
    public void handleServiceStateChange(int stateCode) {

        if (mAlertCriteria == null) {
            mAlertCriteria = new AlertCriteria(stateCode);
        }

        // Check new vs. existing service state
        // TODO: consider checking here for stateCode to be IN_SERVICE or OUT_OF_SERVICE
        // before updating the mAlert object since those are the only codes we care about?

        if (mAlertCriteria.getStateCode() != stateCode) {

            // If there is a change in service state,
            // update the Alert parameters
            mAlertCriteria.setStateCode(stateCode);
            mAlertCriteria.setTimeStamp();

            // TODO: Notes-
            // check in 31 seconds, or alternate option would be to check
            // in mAlert.getTimeStamp + 30sec offset, OR call postDelay
            // from within the setTimeStamp() method
            //
            // Each time this is called, the previous existing, yet to executed
            // posted message should be cancelled and replaced with a new one,
            // essentially resetting the isPersisted() check timer
            mHandler.postDelayed(mRunnable, 30000);
        }
    }


    /**
     * Static Inner Class PersistenceVerifierRunnable
     *
     * Instances of static inner classes do not hold an implicit
     * reference to their outer class.
     */
    private static class PersistenceVerifierRunnable implements Runnable {
        private final WeakReference<ServiceStateHandler> mServiceStateHdlr;

        public PersistenceVerifierRunnable(ServiceStateHandler handler) {
            mServiceStateHdlr = new WeakReference<ServiceStateHandler>(handler);
        }

        @Override
        public void run() {
            // Get strong reference to ServiceStateHandler
            ServiceStateHandler serviceStateHdlr = mServiceStateHdlr.get();
            if (serviceStateHdlr != null) {

                if (serviceStateHdlr.mAlertCriteria.isCriteriaSatisfied()) {
                    Log.d("testing", "YES persisted!");
                    serviceStateHdlr.mAlertCriteria.setLastReportedStateCode(
                            serviceStateHdlr.mAlertCriteria.getStateCode());
                    serviceStateHdlr.mNotifier.sendNotification();
                    serviceStateHdlr.mNotifier.playAudio();
                } else {
                    Log.d("testing", "NO! persisted");
                }
            }
        }
    }
}
