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
            System.out.println("IMEI: " + imei);
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
            System.out.println(retrievedUpdates.getString("name") + " " + retrievedUpdates.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getRetrievedUpdates()
    {
        return retrievedUpdates;
    }
}
