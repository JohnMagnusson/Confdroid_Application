package com.bykth.confdroid.confdroid_application;

import android.content.Context;
import android.os.Environment;
import com.bykth.confdroid.confdroid_application.model.Device;
import com.bykth.confdroid.confdroid_application.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

/**
 * Connection between the application and the server is handled in this class.
 */
public class ServerConnection {
    private JSONObject retrievedUpdates;
    private Context context ;

    public ServerConnection(Context context) {
        this.context = context;

    }


    /**
     * Fetches a json string from the server.
     *
     * @param request a slash notetated request string  (Ex. /user/device)
     * @param data    variables to be sent with te request, starting with a ?. (Ex.?imei=1234&user=2)
     * @return JSONObject
     */
    private JSONObject fetch(final String request, final String data) {
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result = "";
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://confdroid.tutus.se/api" + request + ".json" + data);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    reader = new BufferedReader(new InputStreamReader(in));
                    result = reader.readLine();
                    retrievedUpdates = new JSONObject(result);
                    writeJSONtoTXT(result);

                } catch (Exception e) {
                    System.out.println("URL request failed with: " + e.getMessage());
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        });
        serverThread.start();
        try {
            //Waits here until serverThread is done
            serverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return retrievedUpdates;
    }

    /**
     * Fetch a user with all its devices.
     *
     * @return
     * @throws JSONException
     */
    public User fetchUser() throws JSONException {
        JSONObject userJson = fetch("/user/testToken", "");
        User user = new User(userJson.getString("name"), userJson.getString("email"));
        return user;
    }

    /**
     * Fetch a user with only the device with the supplied imei.
     * @param imei
     * @return
     * @throws JSONException
     */
    public User fetchUser(String imei) throws JSONException {
        JSONObject userJson = fetch("/user/testToken", "&imei=" + imei);
        User user = new User(userJson.getString("name"), userJson.getString("email"), userJson.getJSONArray("devices"));
        if (userJson.getJSONArray("devices").length() > 0) {
            user.addDevice(new Device(userJson.getJSONArray("devices").getJSONObject(0).getString("name"), userJson.getJSONArray("devices").getJSONObject(0).getString("imei"), userJson.getJSONArray("devices").getJSONObject(0).getJSONArray("applications")));
        } else {
            System.out.println("This IMEI doesn't exists!");
        }
        return user;
    }
    private void writeJSONtoTXT(String Json) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(context.getFilesDir() + "/json.txt");
            stream.write(Json.getBytes());
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
