package com.bykth.confdroid.confdroid_application.model;

import com.bykth.confdroid.confdroid_application.AppParser;
import com.bykth.confdroid.confdroid_application.ServerConnection;

/**
 * Modelfacede is used to make the program more module and easy to change in the future.
 */
public class ModelFacade
{
    private ServerConnection serverConnection;
    private AppParser appParser;
    private User user;

    /**
     * Stores all important models in one place.
     * @param serverConnection Connection between server and application.
     * @param appParser Parses the new settings to the apps on the device.
     * @param user User have devices.
     */
    public ModelFacade(ServerConnection serverConnection, AppParser appParser, User user)
    {
        this.serverConnection = serverConnection;
        this.appParser = appParser;
        this.user = user;
    }

    /**
     * @return ServerConnection
     */
    public ServerConnection getServerConnection()
    {
        return serverConnection;
    }

    /**
     * @return AppParser
     */
    public AppParser getAppParser() {
        return appParser;
    }

    /**
     * @return User
     */
    public User getUser() {
        return user;
    }
}
