package com.example.aram.servicenotifier.notifier.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.example.aram.servicenotifier.infrastructure.MyApp;
import com.example.aram.servicenotifier.notifier.model.OnNotificationStateChangeListener;

/**
 * Class ServiceInteractorImpl - Singleton
 *
 * Responsible for starting, stopping, and tracking the state of the
 * Signal Monitor service.
 */
public class ServiceInteractorImpl implements ServiceInteractor {

    private static ServiceInteractorImpl sInstance = null;

    private enum MonitorState { MONITOR_RUNNING, MONITOR_STOPPED }
    private MonitorState mMonitorState;

    /**
     * Class constructor.
     */
    private ServiceInteractorImpl() {

        // Initializes MonitorState
        getServiceStatus();
    }

    /**
     * Returns the active instance of this class.
     */
    public static ServiceInteractorImpl instance() {

        if(sInstance == null) {
            sInstance = new ServiceInteractorImpl();
        }
        return sInstance;
    }

    @Override
    public boolean isServiceRunning() {

        boolean status = false;

        switch (mMonitorState) {
            case  MONITOR_RUNNING: status = true;
                break;
            case MONITOR_STOPPED: status = false;
                break;
        }

        return status;
    }

    @Override
    public void toggleState(final OnNotificationStateChangeListener listener) {

        Intent theIntent = new Intent(MyApp.getContext(), SignalMonitorService.class);

        // toggle service state each time button is clicked
        switch (mMonitorState) {
            case MONITOR_RUNNING:
                mMonitorState = MonitorState.MONITOR_STOPPED;
                MyApp.getContext().stopService(theIntent);
                listener.onNotificationStateDisabled();
                break;
            case MONITOR_STOPPED:
                mMonitorState = MonitorState.MONITOR_RUNNING;
                MyApp.getContext().startService(theIntent);
                listener.onNotificationStateEnabled();
                break;
        }
    }

    /**
     * Queries the status of the SignalMonitorService.
     *
     * This method should be called each time upon creation of this class
     */
    private void getServiceStatus() {

        if (isMyServiceRunning(SignalMonitorService.class)) {
            mMonitorState = MonitorState.MONITOR_RUNNING;
        } else {
            mMonitorState = MonitorState.MONITOR_STOPPED;
        }
    }

    /**
     * Generic method that returns the status of the specified service.
     *
     * TODO: Consider moving this to a Utils class.
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {

        boolean status = false;

        ActivityManager manager = (ActivityManager) MyApp.getContext().getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                status = true;
            }
        }
        return status;
    }
}
