package com.example.aram.servicenotifier.main;

import com.example.aram.servicenotifier.notifier.model.OnNotificationStateChangeListener;
import com.example.aram.servicenotifier.notifier.service.ServiceInteractor;
import com.example.aram.servicenotifier.notifier.service.ServiceInteractorImpl;

/**
 * Created by Aram on 5/30/2015.
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
    }

    @Override
    public void resume() {

        // Need to know the service state to properly set
        // the start/stop button text
        boolean status = mServiceInteractor.isServiceRunning();
        mMainView.setButtonText(status);
    }

    @Override
    public void onNotificationStateEnabled() {

        mMainView.setButtonText(true);
        //mMainView.showMessage("Service is RUNNING");
    }

    @Override
    public void onNotificationStateDisabled() {

        mMainView.setButtonText(false);
        //mMainView.showMessage("Service is STOPPED");
    }

}
