package com.example.aram.servicenotifier.main;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aram.servicenotifier.R;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity implements MainView, View.OnClickListener {

    //////////////////////////////////////////////
    // TODO: NOTES
    //
    // (1) Use a notification status bar message with a PendingIntent that can message the Activity
    //
    // (2) LocalBroadcastManager for service<>activity comms
    //////////////////////////////////////////////

    @InjectView(R.id.textView) TextView mTextView;
    @InjectView(R.id.startButton) Button mStartButton;

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mStartButton.setOnClickListener(this);

        mPresenter = new MainPresenterImpl(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    public void onClick(View view) {

        mPresenter.toggleNotificationState();
    }

    @Override
    public void showMessage(String message) {

        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setButtonText(boolean value) {

        if(value == true) {
            mStartButton.setText("STOP");   // When service is running
            mTextView.setText("Service is Running");
        } else {
            mStartButton.setText("START");  // When service is stopped
            mTextView.setText("Service is Stopped");
        }
    }
}
