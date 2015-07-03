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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.aram.servicenotifier.R;
import com.example.aram.servicenotifier.util.FileLogger;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class DebugActivity extends ActionBarActivity implements MainView, View.OnClickListener {

    @InjectView(R.id.textView) TextView mTextView;
    @InjectView(R.id.startButton) Button mStartButton;
    @InjectView(R.id.logButton) Button mLogButton;
    @InjectView(R.id.deleteButton) Button mDeleteButton;
    @InjectView(R.id.logTextView) TextView mLogMsgTextView;

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

        //LeakCanary.install(getApplication()); // TODO: memory debugging

        setContentView(R.layout.activity_debug);

        ButterKnife.inject(this);

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

    private void logHeapSize() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        Log.d("testing", "memoryClass:" + Integer.toString(memoryClass));
    }
}
