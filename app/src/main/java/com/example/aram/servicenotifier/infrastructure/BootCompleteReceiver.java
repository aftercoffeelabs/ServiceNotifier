package com.example.aram.servicenotifier.infrastructure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.aram.servicenotifier.main.MainActivity;
import com.example.aram.servicenotifier.notifier.service.SignalMonitorService;

/**
 * Class BootCompleteReceiver
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Restore the service state after device reboot
        SharedPreferences sharedPref = context.getSharedPreferences(
                MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);

        boolean startServicePostBoot = sharedPref.getBoolean(MainActivity.SERVICE_ENABLED, false);

        if (startServicePostBoot) {
            Intent theIntent = new Intent(context, SignalMonitorService.class);
            context.startService(theIntent);
        }
    }
}
