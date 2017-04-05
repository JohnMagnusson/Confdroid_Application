package com.bykth.confdroid.confdroid_application.model;

/**
 * SQL_Setting stores the SQL setting for an application.
 */
public class SQL_Setting
{
    private String dbLocation;
    private String dbQueries;

    /**
     * @param dbLocation Location of the database, URL
     * @param dbQueries Queries to the database.
     */
    public SQL_Setting(String dbLocation, String dbQueries)
    {
        this.dbLocation = dbLocation;
        this.dbQueries = dbQueries;
    }

    /**
     * @return String
     */
    public String getDbLocation()
    {
        return dbLocation;
    }

    /**
     * @return String
     */
    public String getDbQueries()
    {
        return dbQueries;
    }
}
