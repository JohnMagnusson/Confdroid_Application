package com.bykth.confdroid.confdroid_application.model;

import java.util.ArrayList;

/**
 * Device class can be a phone, tablet etc.
 * The device have applications on it and policies that it should follow.
 */

public class Device
{
    private ArrayList<Application> applications;
    private ArrayList<DeviceManagementPolicy> policies;

    public Device()
    {
        applications = new ArrayList<>();
        policies = new ArrayList<>();
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
}
