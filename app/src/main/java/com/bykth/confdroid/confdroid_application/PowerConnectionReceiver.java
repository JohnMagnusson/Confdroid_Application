package com.bykth.confdroid.confdroid_application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by swehu on 2017-04-11.
 */
public class PowerConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isCharging = false;
        if (intent.getAction() == Intent.ACTION_POWER_CONNECTED) isCharging = true;

        Toast.makeText(context, "Charging: " + isCharging, Toast.LENGTH_LONG).show();
    }
}
