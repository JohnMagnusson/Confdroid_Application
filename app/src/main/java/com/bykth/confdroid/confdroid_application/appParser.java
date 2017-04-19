package com.bykth.confdroid.confdroid_application;

import android.content.Context;
import com.bykth.confdroid.confdroid_application.model.Application;
import com.bykth.confdroid.confdroid_application.model.SQL_Setting;
import com.bykth.confdroid.confdroid_application.model.XML_Setting;

import java.io.IOException;
import java.util.ArrayList;

/**
 * AppParser handles the incoming data and parse it and send it to the application where it is stored.
 */
public class AppParser
{
    private ArrayList<Application> applications = new ArrayList<>();
    private Context context;

    public AppParser(ArrayList<Application> applications, Context context) {
        this.applications = applications;
        this.context = context;
    }

    public void parse() {
        final ArrayList<Application> applications = this.applications;
        Thread parseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Application app : applications) {
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
