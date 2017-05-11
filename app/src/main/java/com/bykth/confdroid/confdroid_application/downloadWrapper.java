package com.bykth.confdroid.confdroid_application;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import com.bykth.confdroid.confdroid_application.model.Application;
import com.bykth.confdroid.confdroid_application.model.User;

import java.util.ArrayList;

/**
 * Created by Glantz on 2017-05-09.
 */
public class downloadWrapper {

    /**
     * Wrapperclassed to download and install new updates
     * Used mainly by the notification
     * @param context
     */
    public void install(Context context) {
        final ServerConnection serverCon = new ServerConnection(context);
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();

            User user = serverCon.fetchUser(imei);
            System.out.println(user.getName());
            System.out.println(user.getDevices().get(0).getName());
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
            e.printStackTrace();
            e.getMessage();
        }
    }


}
