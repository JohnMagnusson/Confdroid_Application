package com.bykth.confdroid.confdroid_application;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Elias on 2017-04-03.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.system.Os;
import android.telephony.TelephonyManager;
import org.apache.http.conn.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class ServerConnection implements Runnable
{
    ServerConnection()
    {

    }

    public void fetch(String imei, String versionHash)
    {
        String result = "";
        BufferedReader reader = null;
        try {
            URL url = new URL("https://brainstorm-labs.net/confdroid/api/user.json?userAuth=Mattias&imei=" + imei);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            reader = new BufferedReader(new InputStreamReader(in));
            result = reader.readLine();
            System.out.println("I read this: " + result);
        }
        catch(IOException e)
        {
            System.out.println("It went wrong: " + e.getMessage());
        }
        finally
        {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {}
            }
        }

        try {
            JSONObject userInfo = new JSONObject(result);
            User user = new User(userInfo.getString("Name"));
            user.setEmail(userInfo.getString("Email"));
            System.out.println(user.getName() + " " + user.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        fetch("1234", "aa");
    }
}
