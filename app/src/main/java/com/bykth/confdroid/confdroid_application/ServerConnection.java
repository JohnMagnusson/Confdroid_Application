package com.bykth.confdroid.confdroid_application;

import java.io.*;
import java.net.URL;

/**
 * Connection between the application and the server is handled in this class.
 */

import com.bykth.confdroid.confdroid_application.model.User;
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
