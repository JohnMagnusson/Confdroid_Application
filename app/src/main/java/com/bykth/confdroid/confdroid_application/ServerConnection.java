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
    public JSONObject retrievedUpdates = null;
    private String imei = "1234";

    ServerConnection()
    {
        //new Thread(this).start();
    }

    public void fetch(String imei, String versionHash)
    {
        final String imeia = imei;
        new Thread(){
            public void run()
            {
                synchronized (retrievedUpdates)
                {
                    String result = "";
                    BufferedReader reader = null;
                    try {
                        System.out.println("1.1");
                        URL url = new URL("https://confdroid.tutus.se/api/user.json?userAuth=testToken&imei=" + imeia);
                        System.out.println("1.2");
                        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                        System.out.println("1.3");
                        InputStream in = new BufferedInputStream(connection.getInputStream());
                        System.out.println("1.4");
                        reader = new BufferedReader(new InputStreamReader(in));
                        System.out.println("1.5");
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
                        retrievedUpdates.notifyAll();
                        System.out.println("NOOOOOW?: " + retrievedUpdates);
                        User user = new User(retrievedUpdates.getString("name"));
                        user.setEmail(retrievedUpdates.getString("email"));
                        System.out.println(user.getName() + " " + user.getEmail());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public JSONObject getRetrievedUpdates()
    {
        System.out.println("Is retrievedUpdates null before returning?: " + retrievedUpdates);
        return retrievedUpdates;
    }

//    @Override
//    public void run()
//    {
//        fetch("1234", "aa");
//    }
}
