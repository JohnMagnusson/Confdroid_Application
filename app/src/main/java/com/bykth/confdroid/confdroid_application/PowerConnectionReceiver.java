package com.bykth.confdroid.confdroid_application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import com.bykth.confdroid.confdroid_application.model.User;


/**
 * Created by swehu on 2017-04-11.
 */
public class PowerConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationActivity Na = new NotificationActivity();
        boolean isCharging = false;
        if (intent.getAction() == Intent.ACTION_POWER_CONNECTED) isCharging = true;
        if (isCharging) {
            final ServerConnection serverCon = new ServerConnection(context);
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                User user = serverCon.fetchUser(telephonyManager.getDeviceId());
                if (user != null) {
                    Na.showNotification(context);
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getMessage();
            }
        }
    }
}
