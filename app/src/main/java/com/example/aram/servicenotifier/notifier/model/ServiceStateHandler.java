package com.example.aram.servicenotifier.notifier.model;

import android.content.res.Resources;
import android.os.Handler;
import android.telephony.ServiceState;
import android.util.Log;

import com.example.aram.servicenotifier.R;
import com.example.aram.servicenotifier.notifier.service.SignalMonitorService;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

/**
 * Class ServiceStateHandler
 */
public class ServiceStateHandler {

    // Additional 1 sec is to compensate for time delay precision errors
    // Example: Service state changes at 20.738, postDelayed is set using 15 sec, persistence
    //          is checked at 35.071, resulting in time delta of 14.333, which fails.
    //public static final int DELAY_DURATION = AlertCriteria.persistenceDuration + 1;

    private Resources mResources = SignalMonitorService.getContext().getResources();

    private AlertCriteria mAlertCriteria;
    private Handler mHandler = new Handler();
    private final PersistenceVerifierRunnable mVerifierRunnable;
    private Notifier mNotifier;

    /**
     * Default class constructor.
     */
    public ServiceStateHandler(){
        mVerifierRunnable = new PersistenceVerifierRunnable(this);
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

            mHandler.postDelayed(mVerifierRunnable,
                    TimeUnit.SECONDS.toMillis(AlertCriteria.persistenceDuration + 1));
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

                    // Send the alert message properties
                    notifier.setMessage(alertCriteria.getCurrentServiceStateString());

                    if (alertCriteria.getStateCode() != ServiceState.STATE_IN_SERVICE) {
                        notifier.setMessageIconColor(
                                serviceStateHdlr.mResources.getColor(R.color.red_500));
                        notifier.setVibratePattern(Notifier.VIBRATE_ONE_LONG);
                    } else {
                        notifier.setMessageIconColor(
                                serviceStateHdlr.mResources.getColor(R.color.light_green_600));
                        notifier.setVibratePattern(Notifier.VIBRATE_TWO_SHORT);
                    }

                    // Send the alert
                    notifier.sendNotification();

                    Log.d("testing", "Passed criteria!");
                } else {
                    Log.d("testing", "Failed criteria...");
                }
            }
        }
    }
}
