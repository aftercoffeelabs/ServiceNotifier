package com.example.aram.servicenotifier.main;

import android.app.ActivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aram.servicenotifier.R;
import com.example.aram.servicenotifier.util.FileLogger;

public class DebugActivity extends ActionBarActivity implements MainView, View.OnClickListener {

    private TextView mTextView;
    private Button mStartButton;
    private Button mLogButton;
    private Button mDeleteButton;
    private TextView mLogMsgTextView;

    private MainPresenter mPresenter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // WARNING: Only used in developer mode
//        if (id == R.id.action_debug) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //LeakCanary.install(getApplication());

        setContentView(R.layout.activity_debug);

        mTextView = (TextView) findViewById(R.id.textView);
        mStartButton = (Button) findViewById(R.id.startButton);;
        mLogButton = (Button) findViewById(R.id.logButton);;
        mDeleteButton = (Button) findViewById(R.id.deleteButton);;
        mLogMsgTextView = (TextView) findViewById(R.id.logTextView);;

        mStartButton.setOnClickListener(this);
        mLogButton.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);

        mPresenter = new MainPresenterImpl(this);

        mLogMsgTextView.setMovementMethod(new ScrollingMovementMethod());

        //logHeapSize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mPresenter = null;
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.startButton:
                mPresenter.toggleNotificationState();
                break;
            case R.id.logButton:
                readLogFile();
                break;
            case R.id.deleteButton:
                FileLogger.deleteLogFile();
                mLogMsgTextView.setText("");
                break;
        }
    }

    private void readLogFile(){
        String text = FileLogger.openAndReadFile();
        mLogMsgTextView.setText(text.replace("\\n", "\n"));
    }

    @Override
    public void setButtonOn() {
        // Do nothing for DebugActivity
    }

    @Override
    public void playButtonAnimation() {
        // Do nothing for DebugActivity
    }

    @Override
    public void setHintMessage(String message) {

        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void stopButtonAnimation() {
        // Do nothing for DebugActivity
    }

    @Override
    public void setStateMessage(String message) {

        if(true == true) {
            mStartButton.setText("STOP");   // When service is running
            mTextView.setText("Service is Running");
        } else {
            mStartButton.setText("START");  // When service is stopped
            mTextView.setText("Service is Stopped");
        }
    }

    @Override
    public void saveSessionData(boolean isRunning) {
        // Do nothing for DebugActivity
    }

    private void logHeapSize() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        Log.d("testing", "memoryClass:" + Integer.toString(memoryClass));
    }
}
