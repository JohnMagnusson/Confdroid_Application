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

public class ServerConnection
{
    public JSONObject retrievedUpdates;
    private String imei = "1234";

    public ServerConnection()
    {

    }

    public void fetch(String imei, String versionHash)
    {
        String result = "";
        BufferedReader reader = null;
        try {
            URL url = new URL("https://confdroid.tutus.se/api/user.json?userAuth=testToken&imei=" + imei);
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
            retrievedUpdates = new JSONObject(result);
            User user = new User(retrievedUpdates.getString("name"));
            user.setEmail(retrievedUpdates.getString("email"));
            System.out.println(user.getName() + " " + user.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getRetrievedUpdates()
    {
        return retrievedUpdates;
    }
}
