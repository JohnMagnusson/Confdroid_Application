package com.bykth.confdroid.confdroid_application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import com.bykth.confdroid.confdroid_application.model.User;

/**
 * Created by Glantz on 2017-05-17.
 */
public class WifiConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationActivity Na = new NotificationActivity();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
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
        } else {
            //stop service
        }
    }

}
