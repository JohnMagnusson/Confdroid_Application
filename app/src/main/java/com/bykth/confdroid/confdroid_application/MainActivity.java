package com.bykth.confdroid.confdroid_application;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    final private int REQUEST_CODE_READ_PHONE_STATE = 1;
    private TextView imeiTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        System.out.println("Hello");
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        imeiTextView = (TextView) findViewById(R.id.imeiTextView);
        getPermissionToReadPhoneState();
        Button fetchButton = (Button) findViewById(R.id.button);

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new ServerConnection());
                t.start();
            }
        });
    }

    public void getPermissionToReadPhoneState()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
        }
        else
        {
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            final String str = "Imei number: " + telephonyManager.getDeviceId();
            imeiTextView.setText(str);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch(requestCode)
        {
            case 1:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    final String str = "Imei number: " + telephonyManager.getDeviceId();
                    imeiTextView.setText(str);
                }
                else
                {
                    System.out.println("Fail!");
                }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
