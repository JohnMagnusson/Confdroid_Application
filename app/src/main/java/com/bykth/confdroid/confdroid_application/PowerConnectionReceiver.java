package com.bykth.confdroid.confdroid_application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import com.bykth.confdroid.confdroid_application.model.Application;
import com.bykth.confdroid.confdroid_application.model.User;

import java.util.ArrayList;

/**
 * Created by swehu on 2017-04-11.
 */
public class PowerConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isCharging = false;
        if (intent.getAction() == Intent.ACTION_POWER_CONNECTED) isCharging = true;

        Toast.makeText(context, "Charging: " + isCharging, Toast.LENGTH_LONG).show();
        if (isCharging) {
            final ServerConnection serverCon = new ServerConnection(context);
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                User user = serverCon.fetchUser(telephonyManager.getDeviceId());
                if (user != null) {
                    if (!user.getDevices().isEmpty()) {
                        ArrayList<Application> apps = user.getDevices().get(0).getApplications();
                        for (Application app : apps) {
                            System.out.println(app.getFriendlyName());
                        }
                        AppParser appParser = new AppParser(apps, context);
                        appParser.parse();
                    }

                }
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
