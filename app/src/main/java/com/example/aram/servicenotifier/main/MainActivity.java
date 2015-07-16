package com.example.aram.servicenotifier.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.aram.servicenotifier.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Class MainActivity
 */
public class MainActivity extends ActionBarActivity implements MainView, View.OnClickListener {

//    @InjectView(R.id.status_msg) Button mDebugButton;

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

//        mDebugButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // TODO: resume animation thread here
        //mPresenter.resume();
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
        //mPresenter = null;
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void setButtonText(boolean value) {

    }

    @Override
    public void onClick(View v) {

    }
}
