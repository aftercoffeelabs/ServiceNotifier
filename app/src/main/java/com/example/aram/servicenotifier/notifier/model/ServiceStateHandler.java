package com.example.aram.servicenotifier.notifier.model;

import android.os.Handler;
import android.util.Log;

import com.example.aram.servicenotifier.util.FileLogger;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

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

            mHandler.postDelayed(mRunnable,
                    TimeUnit.SECONDS.toMillis(AlertCriteria.MIN_PERSISTENCE_DURATION ));
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

                AlertCriteria alertCriteria = serviceStateHdlr.mAlertCriteria;
                Notifier notifier = serviceStateHdlr.mNotifier;

                if (alertCriteria.isCriteriaSatisfied()) {

                    // Update the state code being sent in the alert
                    alertCriteria.setLastReportedStateCode(alertCriteria.getStateCode());

                    // Send the alert
                    notifier.setMessage(alertCriteria.getCurrentServiceStateString());
                    notifier.sendNotification();

                    Log.d("testing", "Passed criteria!");
                } else {
                    Log.d("testing", "Failed criteria...");
                }
            }
        }
    }
}
