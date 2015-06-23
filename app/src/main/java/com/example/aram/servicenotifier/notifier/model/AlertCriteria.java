package com.example.aram.servicenotifier.notifier.model;

import android.content.res.Resources;
import android.telephony.ServiceState;
import android.util.Log;

import com.example.aram.servicenotifier.R;
import com.example.aram.servicenotifier.infrastructure.MyApp;

/**
 * Class AlertCriteria
 */
public class AlertCriteria {

    // Service states: IN_SERVICE, OUT_OF_SERVICE, EMERGENCY_ONLY, POWER_OFF

    public static final long MIN_PERSISTENCE_DURATION = 10L; // seconds

    private int mStateCode = -1;
    private int mLastReportedStateCode = -1;
    private long mCreationTime = 0;

    private Resources res = MyApp.getRes();

    /**
     * Default class constructor.
     */
    public AlertCriteria() {}

    /**
     * Alternate class constructor.
     */
    public AlertCriteria(int code) {

        mStateCode = code;
        mLastReportedStateCode = code;

        mCreationTime = System.nanoTime();
    }

    public int getStateCode() {

        return mStateCode;
    }

    public void setStateCode(int code) {

        mStateCode = code;
    }

    public int getLastReportedStateCode() {

        return mLastReportedStateCode;
    }

    public void setLastReportedStateCode(int code) {

        mLastReportedStateCode = code;
    }

    public void setTimeStamp() {

        mCreationTime = System.nanoTime(); // current time
    }

    public boolean isCriteriaSatisfied() {

        // Notification Criteria:
        //  1. Persistence time is met
        //  2. Last reported state code is not equal to current code
        return (isPersisted() && mStateCode != mLastReportedStateCode) ? true: false;
    }

    public String getServiceMsgString() {

        String message = "";

        switch (mStateCode) {
            case ServiceState.STATE_IN_SERVICE:
                message = res.getString(R.string.notification_content_in_service);
                break;
            case ServiceState.STATE_OUT_OF_SERVICE:
                message = res.getString(R.string.notification_content_out_service);
                break;
            case ServiceState.STATE_EMERGENCY_ONLY:
                // TODO: do not use
                break;
            case ServiceState.STATE_POWER_OFF:
                // TODO: do not use
                message = res.getString(R.string.notification_content_out_service);
                break;
        }
        return message;
    }

    private boolean isPersisted() {

        return (getPersistenceDuration() >= MIN_PERSISTENCE_DURATION) ? true: false;
    }

    /**
     * Returns the duration that this alert has persisted (in seconds).
     */
    private long getPersistenceDuration() {

        long durationSecs = 0;

        long elapsedTimeNano = System.nanoTime() - mCreationTime;
        durationSecs = (elapsedTimeNano / 1000000000L) + 1L; // TODO - compensate division error

        return durationSecs;
    }
}
