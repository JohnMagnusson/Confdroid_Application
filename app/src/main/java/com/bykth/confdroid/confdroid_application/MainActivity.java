package com.bykth.confdroid.confdroid_application;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.bykth.confdroid.confdroid_application.model.Application;
import com.bykth.confdroid.confdroid_application.model.Authentication;
import com.bykth.confdroid.confdroid_application.model.User;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private String imei;
    private TextView imeiTextView;
    private TextView emailTextView;
    private TextView nameTextView;
    private TextView deviceTextView;
    private TextView statusTextView;
    private Button fetchButton;
    private Button settingsButton;
    private String ErrorCode = "";
    private EditText URL;
    private EditText authtoken;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Filehandler fh = new Filehandler(this);


        ActivityManager activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();

        for(ActivityManager.RunningAppProcessInfo app : procInfos){
            System.out.println(app.processName);
        }

        if (fh.readFromConfigurationFileBinary() == null) {
            createLoginview();
        }else{
            createStandardView();
        }

    }
    private void createLoginview(){
        final Filehandler fh = new Filehandler(this);
        setContentView(R.layout.setup_layout);
        Button submitButton = (Button) findViewById(R.id.Submittbutton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL = (EditText) findViewById(R.id.addWebsiteURL);
                authtoken = (EditText) findViewById(R.id.addAuthtoken);
                fh.WriteConfigurationFileToBinary(URL.getText().toString(),authtoken.getText().toString());
                createStandardView();
            }
        });
    }
    private void createStandardView(){
        setContentView(R.layout.activity_main);
        imeiTextView = (TextView) findViewById(R.id.imeiTextView);
        nameTextView = (TextView) findViewById(R.id.nameText);
        emailTextView = (TextView) findViewById(R.id.emailText);
        deviceTextView = (TextView) findViewById(R.id.deviceText);
        statusTextView = (TextView) findViewById(R.id.statusText);
        fetchButton = (Button) findViewById(R.id.button);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        doWithPermissions();
        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printJsonFromServer();
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLoginview();
            }
        });

    }

    /**
     * Creates a serverconnection and fetches an user which is then printed out on screen
     */
    private void printJsonFromServer() {
        new fetchUpdates().execute();

    }

    private void doWithPermissions() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();
        final String str = "Imei number: " + imei;
        imeiTextView.setText(str);

    }

    private class fetchUpdates extends AsyncTask<String, Integer, User> {

        @Override
        protected void onPreExecute() {
            fetchButton.setEnabled(false);
            statusTextView.setText("Status: Fetching data from server");
        }

        @Override
        protected User doInBackground(String... imeis) {

            final ServerConnection serverCon = new ServerConnection(getBaseContext());
            try {
                User user = serverCon.fetchUser(imei);
                if (user != null) {
                    if (!user.getDevices().isEmpty()) {
                        ArrayList<Application> apps = user.getDevices().get(0).getApplications();
                        for (Application app : apps) {
                            System.out.println(app.getFriendlyName());
                        }
                        AppParser appParser = new AppParser(apps, getBaseContext());
                        appParser.parse();
                    }
                }
                return user;
            } catch (Exception e) {
                setErrorCode(e.getMessage());
            }


            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(User user) {
            if (user != null) {
                nameTextView.setText("Name: " + user.getName());
                emailTextView.setText("Email: " + user.getEmail());
                statusTextView.setText("Status: Updates downloaded at " + DateFormat.getDateTimeInstance().format(new Date()));
                if (!user.getDevices().isEmpty()) {
                    deviceTextView.setText("Device: " + user.getDevices().get(0).getName());
                }
            } else {
                nameTextView.setText("");
                emailTextView.setText("");
                deviceTextView.setText("");
                if (ErrorCode.equals("304")) {
                    statusTextView.setText("Status: \"No new data\" Response Code: " + ErrorCode);
                } else {
                    statusTextView.setText("Status: Response Code: " + ErrorCode);
                }
            }
            fetchButton.setEnabled(true);
        }
    }

    private void setErrorCode(String errorcode) {
        this.ErrorCode = errorcode;
    }

}


