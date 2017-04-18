package com.bykth.confdroid.confdroid_application.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Device class can be a phone, tablet etc.
 * The device have applications on it and policies that it should follow.
 */

public class Device
{
    private String name, imei;
    private ArrayList<Application> applications;
    private ArrayList<DeviceManagementPolicy> policies;

    public Device(String name, String imei)
    {
        this.name = name;
        this.imei = imei;
        this.applications = new ArrayList<>();
        this.policies = new ArrayList<>();

    }

    public Device(String name, String imei, JSONArray applications) throws JSONException {
        this.name = name;
        this.imei = imei;
        this.applications = new ArrayList<>();
        this.policies = new ArrayList<>();
        for (int i = 0; i < applications.length(); i++) {
            JSONObject app = applications.getJSONObject(i);
            Application application = new Application(app.getString("name"), app.getString("forceInstall").compareTo("1") == 0, app.getString("dataDir"), app.getString("apkName"), app.getString("apkURL"));
            JSONArray sqlSettings = app.getJSONArray("SQL_settings");
            for (int j = 0; j < sqlSettings.length(); j++) {
                application.addSqlSetting(new SQL_Setting(sqlSettings.getJSONObject(j).getString("dblocation"), sqlSettings.getJSONObject(j).getString("query")));
            }
            JSONArray xmlSettings = app.getJSONArray("XML_settings");
            for (int j = 0; j < xmlSettings.length(); j++) {
                application.addXmlSetting(new XML_Setting(xmlSettings.getJSONObject(j).getString("fileLocation"), xmlSettings.getJSONObject(j).getString("regexp"), xmlSettings.getJSONObject(j).getString("replaceWith")));
            }
            this.applications.add(application);
        }
    }

    /**
     * @return ArrayList<Application>
     */
    public ArrayList<Application> getApplications()
    {
        return (ArrayList<Application>)applications.clone();
    }
    /**
     * @return ArrayList<DeviceManagementPolicy>
     */
    public ArrayList<DeviceManagementPolicy> getDevicePolicies()
    {
        return (ArrayList<DeviceManagementPolicy>)policies.clone();
    }

    public String getName() {
        return name;
    }
}
