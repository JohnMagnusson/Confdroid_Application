package com.bykth.confdroid.confdroid_application;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.bykth.confdroid.confdroid_application.model.Application;
import com.bykth.confdroid.confdroid_application.model.User;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {


    private String imei;
    private TextView imeiTextView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView deviceTextView;
    private TextView statusTextView;


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
        //gets all the textviews from the activity_main.xml file
        imeiTextView = (TextView) findViewById(R.id.imeiTextView);
        nameTextView = (TextView) findViewById(R.id.nameText);
        emailTextView = (TextView) findViewById(R.id.emailText);
        deviceTextView = (TextView) findViewById(R.id.deviceText);
        statusTextView = (TextView) findViewById(R.id.statusText);
        //getPermissions();
        doWithPermissions();
        Button fetchButton = (Button) findViewById(R.id.button);

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printJsonFromServer();
            }
        });
        Toast.makeText(getBaseContext(), "Toast", Toast.LENGTH_LONG).show();

    }


    /**
     * Creates a serverconnection and fetches an user which is then printed out on screen
     */
    private void printJsonFromServer()
    {
        statusTextView.setText("Status: Fetching data from server");
        final ServerConnection serverCon = new ServerConnection();
        try {
            User user = serverCon.fetchUser(imei);
            if (user != null) {
                nameTextView.setText("Name: " + user.getName());
                emailTextView.setText("Email: " + user.getEmail());

                statusTextView.setText("Status: Updates downloaded at " + DateFormat.getDateTimeInstance().format(new Date()));
                if (!user.getDevices().isEmpty()) {
                    deviceTextView.setText("Device: " + user.getDevices().get(0).getName());
                    ArrayList<Application> apps = user.getDevices().get(0).getApplications();
                    for (Application app : apps) {
                        System.out.println(app.getFriendlyName());
                    }
                    AppParser appParser = new AppParser(apps, this);
                    appParser.parse();
                }

            }else{
                nameTextView.setText("");
                emailTextView.setText("");
                deviceTextView.setText("");
                statusTextView.setText("Status: Could not fetch data");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doWithPermissions() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();
        final String str = "Imei number: " + imei;
        imeiTextView.setText(str);

    }
}
