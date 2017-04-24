package com.bykth.confdroid.confdroid_application.model;

/**
 * XML_Setting stores the xml setting for an app.
 */
public class XML_Setting
{
    private String xmlLocation;
    private String xmlRegexp;
    private String toReplaceWith;

    /**
     * @param xmlLocation The location of the xml setting.
     * @param xmlRegexp Regexp to search for and replace.
     * @param toReplaceWith the string to replace the matched string with.
     */
    public XML_Setting(String xmlLocation, String xmlRegexp, String toReplaceWith)
    {
        this.xmlLocation = xmlLocation;
        this.xmlRegexp = xmlRegexp;
        this.toReplaceWith = toReplaceWith;
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
    public String getXmlRegexp() {
        return xmlRegexp;
    }

    public String getToReplaceWith() {
        return toReplaceWith;
    }
}
