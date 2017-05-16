package com.bykth.confdroid.confdroid_application;

import android.content.Context;
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
     * When the app is launched for the first time the app prompts a window to ask for URL to the server and a AUTH token.
     * This method then takes the URL and through HTTPS connects to the server and then using a buffer stream reading in its
     * IMEI number and Friendly Phone name
     *
     * @param imei
     * @param phoneName
     * @param URL
     */

    public void firstConnectionForPhone(final String imei, final String phoneName, final String URL) {

        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(URL + "/api/device.json");
                    System.out.println(url);
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    /*
                     *  Creates the JSON object that is the passed to the server and then created as a string
                     */
                    JSONObject root = new JSONObject();
                    //
                    root.put("imei", imei);
                    root.put("name", phoneName);
                    String str = root.toString();

                    // ***************************
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(str);
                    writer.flush();
                    writer.close();
                    os.close();

                    // Possible to add a reader here to get information back to the phone, such as certificate etc.
                } catch (Exception e) {
                    e.printStackTrace();
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


    }

    /**
     * Fetches a json string from the server, uses the config file to get the URL to connect to and in that same file
     * the Authtoken is stored to verify the user.
     * The config file is of binary type which contains a object that contains all necessary information to authenticate and log on to the server
     * <p>
     * data varibles to be sent with te request, starting with a ?. (Ex.?imei=1234&user=2)
     *
     * @return JSONObject
     */
    private JSONObject fetch(final String data) throws Exception {
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result = "";
                Filehandler fh = new Filehandler(context);
                Authentication auth;

                BufferedReader reader = null;
                try {
                    // reads in the Binary file to a Authenticationobject
                    auth = fh.readFromConfigurationFileBinary();
                    // Creates the full URL used to fetch new updates
                    URL url = new URL(auth.getUrlToServer() + "/api/user/" + auth.getAuthenticateToken() + ".json" + data);
                    System.out.println(url);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    retrivedcode = connection.getResponseCode();
                    System.out.println(retrivedcode);
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    reader = new BufferedReader(new InputStreamReader(in));
                    result = reader.readLine();

                    // if no problems are received from the server a http 200 code is return as a success
                    //else the latest JSON will not be stored localy and a null object will be returned instead
                    if (retrivedcode == 200) {
                        fh.writeJSONtoTXT(result, false, false);
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
     * Useless method that is never used...
     *
     * @return
     * @throws JSONException
     */
    public User fetchUser() throws  Exception {
        JSONObject userJson = fetch("");
        User user = new User(userJson.getString("name"), userJson.getString("email"));
        return user;
    }

    /**
     * Fetch a user with only the device with the supplied imei, reads the lates successfully installed settings.
     *
     * @param imei
     * @return
     * @throws JSONException
     */
    public User fetchUser(String imei) throws  Exception {

        Filehandler fh = new Filehandler(context);
        JSONObject userJson = fetch("?imei=" + imei + "&hash=" + fh.readSuccessedSettingsAsString());
        // This checks if the connection was successfully ells the HTTP responsecode is thrown in a Exception
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

    /**
     * Not used at this moment
     *
     * @throws Exception
     */
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


}
