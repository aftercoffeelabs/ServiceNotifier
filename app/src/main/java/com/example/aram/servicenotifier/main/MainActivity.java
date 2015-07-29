package com.example.aram.servicenotifier.main;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.aram.servicenotifier.R;
import com.example.aram.servicenotifier.about.AboutActivity;
import com.example.aram.servicenotifier.view.FancyControlButton;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Class MainActivity
 */
public class MainActivity extends ActionBarActivity implements MainView, View.OnClickListener {

    @InjectView(R.id.mainView_control_button) FancyControlButton mControlButton;
    @InjectView(R.id.mainView_hint_text) TextView mHintMessage;
    @InjectView(R.id.mainView_state_text) TextView mStateMessage;

    private MainPresenter mPresenter;
    private ObjectAnimator mHintMessageAnimator;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_debug:
                startActivity(new Intent(this, DebugActivity.class));
                break;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mPresenter = new MainPresenterImpl(this);

        mControlButton.setOnClickListener(this);

        // Setup animators
        mHintMessageAnimator = ObjectAnimator.ofFloat(mHintMessage, "alpha", 0.2f, 1f);
        mHintMessageAnimator.setDuration(4000);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("testing", "Activity - onStart");

        // Set the control button state, start looping animations
        mPresenter.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Suspend all looping animation threads
        mPresenter.pause();

        Log.d("testing", "Activity - onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter = null;
        mHintMessageAnimator = null;
    }

    /**
     * Sets the button to the On position upon activity startup
     *
     * Called during activity onStart() sequence
     */
    @Override
    public void setButtonOn() {

        mControlButton.setStartPositionOn();

    }

    @Override
    public void playButtonAnimation() {

        mControlButton.clicked();
    }

    @Override
    public void stopButtonAnimation() {

        mControlButton.stopAnimation();
    }

    @Override
    public void setHintMessage(String message) {

        mHintMessage.setText(message);
        mHintMessageAnimator.start();
    }

    @Override
    public void setStateMessage(String message) {

        mStateMessage.setText(message);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mainView_control_button:
                mPresenter.toggleNotificationState();
                break;
        }
    }
}
