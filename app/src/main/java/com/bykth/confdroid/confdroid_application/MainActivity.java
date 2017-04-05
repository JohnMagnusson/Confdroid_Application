package com.bykth.confdroid.confdroid_application;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        final TextView imeiTextView = (TextView) findViewById(R.id.imeiTextView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE))
            {

            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},1);
                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                final String str = telephonyManager.getDeviceId();
                imeiTextView.setText(str);
            }
        }
        else
        {
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            final String str = "Imei number: " + telephonyManager.getDeviceId();
            imeiTextView.setText(str);
        }
        System.out.println("ABC");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
           /* if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET))
            {

            }
            else {*/
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},3);
            //}
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
           /* if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET))
            {

            }
            else {*/
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE},5);
            //}
        }
        System.out.println("DEF");
        Thread t = new Thread(new ServerConnection());
        t.start();

    }

}
