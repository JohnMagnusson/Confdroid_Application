package com.bykth.confdroid.confdroid_application.model;

import java.util.ArrayList;

/**
 * This class stores data of an app.
 * Settings, if it should be force installed, data directory, apk name and apk url.
 */
public class Application
{
    private ArrayList<SQL_Setting> sql_settings;
    private ArrayList<XML_Setting> xml_settings;
    private boolean force_install;
    private String dataDir;
    private String apkName;
    private String apkUrl;
    private String friendlyName;

    /**
     * This class stores data of an app.
     * @param force_install If th app should be force installed.
     * @param dataDir Where the app is stored.
     * @param apkName Name of the apk.
     * @param apkUrl Url to the apk.
     */
    public Application(String friendlyName, boolean force_install, String dataDir, String apkName, String apkUrl)
    {
        this.friendlyName = friendlyName;
        this.sql_settings = new ArrayList();
        this.xml_settings = new ArrayList();
        this.force_install = force_install;
        this.dataDir = dataDir;
        this.apkName = apkName;
        this.apkUrl = apkUrl;
    }

    /**
     * @return SQL_Setting
     */
    public ArrayList<SQL_Setting> getSqlSettings() {
        return (ArrayList<SQL_Setting>) sql_settings.clone();
    }
    /**
     * @return XML_Setting
     */
    public ArrayList<XML_Setting> getXmlSettings() {
        return (ArrayList<XML_Setting>) xml_settings.clone();
    }

    public void addSqlSetting(SQL_Setting sqlSetting) {
        this.sql_settings.add(sqlSetting);
    }

    public void addXmlSetting(XML_Setting xmlSetting) {
        this.xml_settings.add(xmlSetting);
    }
    /**
     * @return boolean
     */
    public boolean isForce_install() {
        return force_install;
    }
    /**
     * @return String
     */
    public String getDataDir() {
        return dataDir;
    }
    /**
     * @return String
     */
    public String getApkName() {
        return apkName;
    }
    /**
     * @return String
     */
    public String getApkUrl() {
        return apkUrl;
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}
