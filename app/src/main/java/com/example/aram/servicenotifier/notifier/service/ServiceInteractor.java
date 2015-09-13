package com.example.aram.servicenotifier.notifier.service;

import com.example.aram.servicenotifier.notifier.model.OnNotificationStateChangeListener;

/**
 * Created by Aram on 6/2/2015.
 */
public interface ServiceInteractor {

    public boolean isServiceRunning();

    public void toggleState(OnNotificationStateChangeListener listener);

    public void restart();
}
