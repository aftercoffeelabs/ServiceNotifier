package com.example.aram.servicenotifier.notifier.model;

/**
 * Class AlertCriteria
 */
public class AlertCriteria {

    // Service states: IN_SERVICE, OUT_OF_SERVICE, EMERGENCY_ONLY, POWER_OFF

    public static final long MIN_PERSISTENCE_DURATION = 10;

    private int mStateCode = -1;
    private int mLastReportedStateCode = -1;
    private long mCreationTime = 0;

    /**
     * Default class constructor.
     */
    public AlertCriteria() {}

    /**
     * Alternate class constructor.
     */
    public AlertCriteria(int stateCode) {

        mStateCode = stateCode;
        mLastReportedStateCode = stateCode;

        mCreationTime = System.nanoTime();
    }

    public int getStateCode() {

        return mStateCode;
    }

    public void setStateCode(int mStateCode) {

        this.mStateCode = mStateCode;
    }

    public int getLastReportedStateCode() {

        return mLastReportedStateCode;
    }

    public void setLastReportedStateCode(int stateCode) {

        mLastReportedStateCode = stateCode;
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

    private boolean isPersisted() {

        return (getPersistanceDuration() >= MIN_PERSISTENCE_DURATION) ? true: false;
    }

    /**
     * Returns the duration that this alert has persisted (in seconds).
     */
    private long getPersistanceDuration() {

        long durationSecs = 0;

        long elapsedTimeNano = System.nanoTime() - mCreationTime;
        durationSecs = (long)(elapsedTimeNano / 1000000000.0);

        return durationSecs;
    }
}
