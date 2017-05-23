package com.bykth.confdroid.confdroid_application;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Mattias Kågström on 2017-04-10.
 */
class SQLHandler {

    /**
     * Initilizies the sql handler, and cheks if sqlite3 is installed. if not, it will install it on the system.
     * REQUIRES ROOT!!!
     */
    SQLHandler() {
        File varTmpDir = new File("/system/bin/sqlite3");
        boolean exists = varTmpDir.exists();
        if (exists) {
            System.out.println("Sqlite3 already installed");
        } else {
            System.out.println("Installing sqlite3");
            new DownloadFileFromURL(this).execute("https://confdroid.tutus.se/sqlite3");
        }

    }

    /**
     * Runs a sql query on the selected sqlite file.
     *
     * @param DBFile    the file to run the query on.
     * @param SQLQuerry The query to run on the file.
     * @throws IOException
     */
    void runQuery(String DBFile, String SQLQuerry) throws IOException {
        try {
            SQLQuerry = SQLQuerry.replaceAll("\"", "\\\"");
            Process proc = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(proc.getOutputStream());
            os.writeBytes("sqlite3 -csv " + DBFile + " '" + SQLQuerry + ";'\n");
            os.writeBytes("exit\n");
            os.flush();
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));


            String s;
            while (stdInput.readLine() != null) ;


            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        } catch (IOException e) {
            throw e;
        }
    }

    void runInstallation() {
        System.out.println("Running installation");
        Process p;
        try {

            // Preform su to get root privileges
            p = Runtime.getRuntime().exec("su");
            // Attempt to write a file to a root-only
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            System.out.println("Trying to install SQLite3 into /system/bin");
            os.writeBytes("mount -o remount,rw /system\n");
            os.writeBytes("cat " + Environment.getExternalStorageDirectory().toString() + "/sqlite3 > /system/bin/sqlite3\n");
            os.writeBytes("chmod 4755 /system/bin/sqlite3\n");
            os.writeBytes("mount -o remount,ro /system\n");
            System.out.println("SQLite have been installed!");
            // Close the terminal
            os.writeBytes("exit\n");
            os.flush();
            try {
                p.waitFor();
                if (p.exitValue() != 255) {
                    // TODO Code to run on success
                    System.out.println("root");
                } else {
                    // TODO Code to run on unsuccessful
                    System.out.println("not root");
                }
            } catch (InterruptedException e) {
                // TODO Code to run in interrupted exception
                System.out.println("not root");
            }
        } catch (IOException e) {
            // TODO Code to run in input/output exception
            System.out.println("not root");
        }
    }
}

class DownloadFileFromURL extends AsyncTask<String, String, String> {

    private SQLHandler caller;

    DownloadFileFromURL(SQLHandler caller) {
        this.caller = caller;
    }

    /**
     * Before starting background thread Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        System.out.println("Downloading file");
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();
            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            // Output stream
            OutputStream output = new FileOutputStream(Environment
                    .getExternalStorageDirectory().toString()
                    + "/sqlite3");

            byte data[] = new byte[1024];


            while ((count = input.read(data)) != -1) {
                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String file_url) {
        caller.runInstallation();
    }

}

