package com.bykth.confdroid.confdroid_application.model;

/**
 * XML_Setting stores the xml setting for an app.
 */
public class XML_Setting
{
    private String xmlLocation;
    private String xmlQuery;

    /**
     * @param xmlLocation The location of the xml setting.
     * @param xmlQuery Query of the xml.
     */
    public XML_Setting(String xmlLocation, String xmlQuery)
    {
        this.xmlLocation = xmlLocation;
        this.xmlQuery = xmlQuery;
    }

    /**
     * @return String
     */
    public String getXmlLocation() {
        return xmlLocation;
    }

    /**
     * @return String
     */
    public String getXmlQuery() {
        return xmlQuery;
    }
}
