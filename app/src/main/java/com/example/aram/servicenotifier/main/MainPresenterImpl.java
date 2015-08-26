package com.example.aram.servicenotifier.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.aram.servicenotifier.R;
import com.example.aram.servicenotifier.infrastructure.MyApp;
import com.example.aram.servicenotifier.notifier.model.OnNotificationStateChangeListener;
import com.example.aram.servicenotifier.notifier.service.ServiceInteractor;
import com.example.aram.servicenotifier.notifier.service.ServiceInteractorImpl;
import com.example.aram.servicenotifier.view.AnimatedButton;

/**
 * Class MainPresenterImpl
 */
public class MainPresenterImpl implements MainPresenter, OnNotificationStateChangeListener {

    private MainView mMainView;
    private ServiceInteractor mServiceInteractor;

    /**
     * Constructor
     */
    public MainPresenterImpl(MainView view) {
        mMainView = view;
        mServiceInteractor = ServiceInteractorImpl.instance();
    }

    @Override
    public void toggleNotificationState() {

        mServiceInteractor.toggleState(this);
        mMainView.playButtonAnimation();
    }

    @Override
    public void resume() {

        // Get the service state to properly set the hint
        // and state messages when the app starts
        // along with the control button state
        if (mServiceInteractor.isServiceRunning()) {
            onNotificationStateEnabled();
            mMainView.setButtonOn();
        } else {
            onNotificationStateDisabled();
        }
    }

    @Override
    public void pause() {

        mMainView.stopButtonAnimation();

        mMainView.saveSessionData(mServiceInteractor.isServiceRunning());
    }

    @Override
    public void onNotificationStateEnabled() {

        mMainView.setStateMessage(MyApp.getRes().getString(R.string.notification_state_on_message));
        mMainView.setHintMessage(MyApp.getRes().getString(R.string.hint_tap_off_message));
    }

    @Override
    public void onNotificationStateDisabled() {

        mMainView.setStateMessage(MyApp.getRes().getString(R.string.notification_state_off_message));
        mMainView.setHintMessage(MyApp.getRes().getString(R.string.hint_tap_on_message));
    }
}
