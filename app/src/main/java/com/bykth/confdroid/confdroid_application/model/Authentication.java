package com.bykth.confdroid.confdroid_application.model;

import java.io.Serializable;

/**
 * Authenicate the conenction between the client and the server.
 */
public class Authentication implements Serializable{
    private String urlToServer;
    private String authenticateToken;

    /**
     * Used to secure the connection between server and application.
     *
     * @param urlToServer       The url to the server.
     * @param authenticateToken Token authenicate who the user is.
     */
    public Authentication(String urlToServer, String authenticateToken) {
        this.urlToServer = urlToServer;
        this.authenticateToken = authenticateToken;
    }


    /**
     * @return String
     */
    public String getUrlToServer() {
        return urlToServer;
    }

    /**
     * @return String
     */
    public String getAuthenticateToken() {
        return authenticateToken;
    }
}
