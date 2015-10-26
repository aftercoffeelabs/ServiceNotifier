package com.example.aram.servicenotifier.notifier.service;

import com.example.aram.servicenotifier.notifier.model.OnNotificationStateChangeListener;

/**
 * Interface ServiceInteractor
 */
public interface ServiceInteractor {

    public boolean isServiceRunning();

    public void toggleState(OnNotificationStateChangeListener listener);
}
