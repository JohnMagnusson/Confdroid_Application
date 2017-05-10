package com.bykth.confdroid.confdroid_application.model;

/**
 * SQL_Setting stores the SQL setting for an application.
 */
public class SQL_Setting {
    private String dbLocation;
    private String dbQuerry;

    /**
     * @param dbLocation Location of the database, URL
     * @param dbQuerry   Queries to the database.
     */
    public SQL_Setting(String dbLocation, String dbQuerry) {
        this.dbLocation = dbLocation;
        this.dbQuerry = dbQuerry;
    }

    /**
     * @return String
     */
    public String getDbLocation() {
        return dbLocation;
    }

    /**
     * @return String
     */
    public String getDbQuerry() {
        return dbQuerry;
    }
}
