package com.bykth.confdroid.confdroid_application.model;

/**
 * This class stores data of an app.
 * Settings, if it should be force installed, data directory, apk name and apk url.
 */
public class Application
{
    private SQL_Setting sql_setting;
    private XML_Setting xml_setting;
    private boolean force_install;
    private String dataDir;
    private String apkName;
    private String apkUrl;

    /**
     * This class stores data of an app.
     * @param sql_setting SQL_Settings for the app.
     * @param xml_setting XML_Settings for the app.
     * @param force_install If th app should be force installed.
     * @param dataDir Where the app is stored.
     * @param apkName Name of the apk.
     * @param apkUrl Url to the apk.
     */
    public Application(SQL_Setting sql_setting, XML_Setting xml_setting, boolean force_install, String dataDir, String apkName, String apkUrl)
    {
        this.sql_setting = sql_setting;
        this.xml_setting = xml_setting;
        this.force_install = force_install;
        this.dataDir = dataDir;
        this.apkName = apkName;
        this.apkUrl = apkUrl;
    }

    /**
     * @return SQL_Setting
     */
    public SQL_Setting getSql_setting() {
        return sql_setting;
    }
    /**
     * @return XML_Setting
     */
    public XML_Setting getXml_setting() {
        return xml_setting;
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
}
