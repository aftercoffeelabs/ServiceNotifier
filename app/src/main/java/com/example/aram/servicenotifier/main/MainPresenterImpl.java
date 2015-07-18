package com.example.aram.servicenotifier.main;

import com.example.aram.servicenotifier.R;
import com.example.aram.servicenotifier.infrastructure.MyApp;
import com.example.aram.servicenotifier.notifier.model.OnNotificationStateChangeListener;
import com.example.aram.servicenotifier.notifier.service.ServiceInteractor;
import com.example.aram.servicenotifier.notifier.service.ServiceInteractorImpl;

/**
 * Class MainPresenterImpl
 */
public class MainPresenterImpl implements MainPresenter, OnNotificationStateChangeListener {

    private MainView mMainView;
    private ServiceInteractor mServiceInteractor;

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

        // Need to know the service state to properly set
        // the hint and state messages
        boolean status = mServiceInteractor.isServiceRunning();

        if (status) {
            onNotificationStateEnabled();
        } else {
            onNotificationStateDisabled();
        }
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
