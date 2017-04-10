package com.bykth.confdroid.confdroid_application;

import java.io.*;
import java.net.URL;

import com.bykth.confdroid.confdroid_application.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

/**
 * Connection between the application and the server is handled in this class.
 */
public class ServerConnection
{
    private JSONObject retrievedUpdates;

    public ServerConnection()
    {

    }

    /**
     * Fetches an user from the server with the imei number.
     * @param imei
     * @param versionHash
     */
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
            retrievedUpdates = new JSONObject(result);
            System.out.println(retrievedUpdates.getString("name") + " " + retrievedUpdates.getString("email"));
        }
        catch(Exception e)
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

    }

    public JSONObject getRetrievedUpdates()
    {
        return retrievedUpdates;
    }
}
