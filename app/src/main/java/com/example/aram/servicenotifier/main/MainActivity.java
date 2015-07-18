package com.example.aram.servicenotifier.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.aram.servicenotifier.R;
import com.example.aram.servicenotifier.view.FancyControlButton;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Class MainActivity
 */
public class MainActivity extends ActionBarActivity implements MainView, View.OnClickListener {

//    @InjectView(R.id.status_msg) Button mDebugButton;
    @InjectView(R.id.mainView_control_button) FancyControlButton mControlButton;
    @InjectView(R.id.mainView_hint_text) TextView mHintMessage;
    @InjectView(R.id.mainView_state_text) TextView mStateMessage;

    private MainPresenter mPresenter;

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
                // TODO
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

//        mDebugButton.setOnClickListener(this);
        mControlButton.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // TODO: resume animation thread here
        mPresenter.resume();
    }

    @Override
    protected void onPause() {

        // TODO: kill animation thread here
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // TODO: kill animation thread here
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        // TODO: kill animation thread here
        mPresenter = null;
    }

    @Override
    public void playButtonAnimation() {
        mControlButton.clicked();
    }

    @Override
    public void setHintMessage(String message) {

        mHintMessage.setText(message);
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
