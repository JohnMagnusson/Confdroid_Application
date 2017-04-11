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
public class sqlite3 {


    public sqlite3() {
        File varTmpDir = new File("/system/bin/sqlite3");
        boolean exists = varTmpDir.exists();
        if (exists) {
            System.out.println("Sqlite3 already installed");
        } else {
            System.out.println("Installing sqlite3");
            new DownloadFileFromURL(this).execute("https://confdroid.tutus.se/sqlite3");
        }

    }

    public void runQuery(String DBFile, String SQLQuerry) throws IOException {
        try {
            Runtime.getRuntime().exec("su sqlite3 -csv " + DBFile + " \"" + SQLQuerry + "\";");
        } catch (IOException e) {
            throw e;
        }

    }


    protected void runInstallation() {
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

    private sqlite3 caller;

    public DownloadFileFromURL(sqlite3 caller) {
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
