package com.example.aram.servicenotifier.main;

/**
 * Interface Class MainView
 */
public interface MainView {

    void setButtonOn();

    void playButtonAnimation();

    void stopButtonAnimation();

    void setHintMessage(String message);

    void setStateMessage(String message);
}
