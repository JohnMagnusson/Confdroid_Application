package com.bykth.confdroid.confdroid_application;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.bykth.confdroid.confdroid_application.model.Application;
import com.bykth.confdroid.confdroid_application.model.SQL_Setting;
import com.bykth.confdroid.confdroid_application.model.XML_Setting;
import java.io.*;
import java.util.ArrayList;

/**
 * AppParser handles the incoming data and parse it and send it to the application where it is stored.
 */
public class AppParser {
    private ArrayList<Application> applications = new ArrayList<>();
    private Context context;
    private Boolean Processrunning;
    private ArrayList<Application> terminatedApps;


    /**
     * Initilizes the class with the list of applications to be parsed, and the cantext of the main actvity
     *
     * @param applications
     * @param context
     */
    AppParser(ArrayList<Application> applications, Context context) {
        this.applications = applications;
        this.context = context;
        terminatedApps = new ArrayList<>();
    }


    /**
     * Parses all the apps supplied in the constructor and applies their settings.
     */
    void parse() {
        final ArrayList<Application> applications = this.applications;

        Thread parseThread = new Thread(new Runnable() {
            @Override
            public void run() {

                for (Application app : applications) {
                    if(findprocess(app.getPackageName())!=null){
                        Processrunning= true;
                        if(Kill(app.getPackageName())&& Processrunning){
                            terminatedApps.add(app);
                            Processrunning = false;
                        }
                    }
                    if (!app.getSqlSettings().isEmpty()) {

                        System.out.println("SQL settings avalible for " + app.getFriendlyName() + ", starting run.");
                        SQLHandler sqlite3 = new SQLHandler();
                        for (SQL_Setting sqlSetting : app.getSqlSettings()) {
                            System.out.println("Running " + sqlSetting.getDbQuerry() + " on " + sqlSetting.getDbLocation());
                            try {
                                sqlite3.runQuery(sqlSetting.getDbLocation(), sqlSetting.getDbQuerry());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (!app.getXmlSettings().isEmpty()) {
                        XML_editor xmlEditor = new XML_editor(context);
                        for (XML_Setting xmlSetting : app.getXmlSettings()) {
                            System.out.println("Running " + xmlSetting.getXmlRegexp() + " on " + xmlSetting.getXmlLocation() + " and replacing it with " + xmlSetting.getToReplaceWith());
                            try {
                                xmlEditor.ApplyXMLSetting(xmlSetting.getXmlLocation(), xmlSetting.getXmlRegexp(), xmlSetting.getToReplaceWith());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        parseThread.start();
        try {
            //Waits here until serverThread is done
            parseThread.join();
            Filehandler fh = new Filehandler(context);
            fh.writeJSONtoTXT(fh.readLatestReceivedFileAsString(), true, true);
            for (Application app:terminatedApps) {
                openApp(app.getPackageName());
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean Kill(String packagename) {
        try {
            Process proc = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(proc.getOutputStream());
            os.writeBytes("kill " + findprocess(packagename) + "\n");
            os.writeBytes("exit\n");
            os.flush();
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // wait for the output from the command
            String s = null;
            while ((s = stdInput.readLine()) != null) ;
            // wait for any errors from the attempted command
            while ((s = stdError.readLine()) != null) ;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


    }

    private String findprocess(String packagename) {


        try {
            // Executes the command.
            Process process = Runtime.getRuntime().exec("pgrep -f " + packagename);


            // Reads stdout.
            // NOTE: You can write to stdin of the command using
            //       process.getOutputStream().
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            // Waits for the command to finish.
            process.waitFor();

            return output.toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    private void openApp(String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                return;
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
        } catch (Exception e) {
        }
    }


}





