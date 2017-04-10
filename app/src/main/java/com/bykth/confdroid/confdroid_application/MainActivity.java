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
import com.bykth.confdroid.confdroid_application.model.Device;
import com.bykth.confdroid.confdroid_application.model.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    final private int REQUEST_CODE_READ_PHONE_STATE = 1;
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
        getPermissionToReadPhoneState();
        Button fetchButton = (Button) findViewById(R.id.button);

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printJsonFromServer();
            }
        });
    }

    /**
     * Creates a serverconnection and fetches an user which is then printed out on screen
     */
    private void printJsonFromServer()
    {
        statusTextView.setText("Status: Fetching data from server");
        final ServerConnection serverCon = new ServerConnection();
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                serverCon.fetch(imei, "aa");
            }
        });
        serverThread.start();
        try {
            //Waits here until serverThread is done
            serverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONObject retrievedInfo = serverCon.getRetrievedUpdates();
            //Retrieves info from jsonobject and puts it in the textviews
            if(retrievedInfo!= null) {
                User user = new User(retrievedInfo.getString("name"), retrievedInfo.getString("email"), new Device());
                nameTextView.setText("Name: " + retrievedInfo.getString("name"));
                emailTextView.setText("Email: " + retrievedInfo.getString("email"));
                deviceTextView.setText("Device: " + retrievedInfo.getString("devices"));
                statusTextView.setText("Status: Updates downloaded at " + DateFormat.getDateTimeInstance().format(new Date()));
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

    /**
     * Asks for permission to read phone state (imei)
     */
    private void getPermissionToReadPhoneState()
    {
        //Checks if we already have permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            //If the user declined the first time, we ask again, but this time we could show an explination
            //of why we need permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
            //The first time we ask
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
        }
        //If we have permission, get imei
        else
        {
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
            final String str = "Imei number: " + imei;
            imeiTextView.setText(str);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch(requestCode)
        {
            case 1:
                //If user grants permission, get imei
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    imei = telephonyManager.getDeviceId();
                    final String str = "Imei number: " + imei;
                    imeiTextView.setText(str);
                }
                //If user declines permission
                else
                {
                    System.out.println("Fail!");
                }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
