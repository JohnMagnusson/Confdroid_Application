import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Elias on 2017-04-03.
 */

import android.system.Os;
import org.apache.http.conn.*;

import javax.net.ssl.HttpsURLConnection;

public class ServerConnection
{
    ServerConnection()
    {

    }

    public void fetch(String imei, String versionHash)
    {
        try {
            URL url = new URL("http://confdroid.localhost/api/user.json?userAuth=mattias&imei=" + imei);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String a = reader.readLine();
        }
        catch(IOException e)
        {
            System.out.println("It went wrong: " + e.getMessage());
        }
    }
}
