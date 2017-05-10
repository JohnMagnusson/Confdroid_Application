package com.bykth.confdroid.confdroid_application;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.bykth.confdroid.confdroid_application.model.Authentication;
import com.bykth.confdroid.confdroid_application.model.Device;
import com.bykth.confdroid.confdroid_application.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Connection between the application and the server is handled in this class.
 */
public class ServerConnection {
    private JSONObject retrievedUpdates;
    private int retrivedcode;
    private Context context;

    public ServerConnection(Context context) {
        this.context = context;

    }


    /**
     * Fetches a json string from the server.
     *
     * @param data    variables to be sent with te request, starting with a ?. (Ex.?imei=1234&user=2)
     * @return JSONObject
     */
    private JSONObject fetch( final String data) throws Exception {
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result = "";
                Filehandler fh = new Filehandler(context);
                Authentication auth;

                BufferedReader reader = null;
                try {
                    AppRunning(context);
                    auth = fh.readFromConfigurationFileBinary();
                    URL url = new URL(auth.getUrlToServer()+"/api/user/"+auth.getAuthenticateToken() + ".json" + data);
                    System.out.println(url);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    retrivedcode = connection.getResponseCode();
                    System.out.println(retrivedcode);
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    reader = new BufferedReader(new InputStreamReader(in));
                    result = reader.readLine();

                    if (retrivedcode == 200) {
                        fh.writeJSONtoTXT(result, false);
                        retrievedUpdates = new JSONObject(result);
                    }
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
        if (retrivedcode != 200) {
            return null;
        }
        return retrievedUpdates;
    }

    /**
     * Fetch a user with all its devices.
     *
     * @return
     * @throws JSONException
     */
    public User fetchUser() throws JSONException, Exception {
        JSONObject userJson = fetch( "");
        User user = new User(userJson.getString("name"), userJson.getString("email"));
        return user;
    }

    /**
     * Fetch a user with only the device with the supplied imei.
     *
     * @param imei
     * @return
     * @throws JSONException
     */
    public User fetchUser(String imei) throws JSONException, Exception {
        Filehandler fh = new Filehandler(context);
        JSONObject userJson = fetch( "?imei=" + imei + "&hash=" + fh.readFileAsString());
        if (userJson == null) {
            throw new Exception("" + this.retrivedcode);
        }
        User user = new User(userJson.getString("name"), userJson.getString("email"), userJson.getJSONArray("devices"));
        if (userJson.getJSONArray("devices").length() > 0) {
            user.addDevice(new Device(userJson.getJSONArray("devices").getJSONObject(0).getString("name"), userJson.getJSONArray("devices").getJSONObject(0).getString("imei"), userJson.getJSONArray("devices").getJSONObject(0).getJSONArray("applications")));
        } else {
            System.out.println("This IMEI doesn't exists!");
        }
        return user;
    }

    private void loadkeystore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream fis = new FileInputStream("");
        // client cert password
        keyStore.load(fis, "".toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        // clientCert password
        kmf.init(keyStore, "".toCharArray());
        KeyManager[] keyManagers = kmf.getKeyManagers();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, null, null);


    }

    public void AppRunning(Context ctx) {
        ActivityManager actvityManager = (ActivityManager)
                ctx.getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningAppProcessInfo> procInfos = actvityManager.getRunningAppProcesses();

        for(ActivityManager.RunningAppProcessInfo runningProInfo:procInfos){

            Log.d("Running Processes", "()()"+runningProInfo.processName);
        }
    }
}
