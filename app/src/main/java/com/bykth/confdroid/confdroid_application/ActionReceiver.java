package com.bykth.confdroid.confdroid_application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by Glantz on 2017-05-09.
 */
public class ActionReceiver extends BroadcastReceiver {
    /**
     * A listner to listen if a install button is pressed.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getStringExtra("action");

        if (action.equals("action1")) {
            performAction1(context);
        } else if (action.equals("action2")) {
            performAction2();

        }
        //This is used to close the notification tray
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

    /**
     * installs the latest downloaded settings after the notificationbutton install is pressed
     * @param context
     */
    private void performAction1(Context context) {
        downloadWrapper dw = new downloadWrapper();
        dw.install(context);
    }

    private void performAction2() {
    }

}