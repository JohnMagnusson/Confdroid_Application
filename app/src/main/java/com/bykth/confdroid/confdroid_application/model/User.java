package com.bykth.confdroid.confdroid_application.model;

import java.util.ArrayList;

/**
 * User have a name and email.
 */
public class User
{
    private String name;
    private String email;
    private ArrayList<Device> devices;
    private ArrayList<Group> groups;

    public User(String name, String email, Device device)
    {
        this.name = name;
        this.email = email;
        devices = new ArrayList<>();
        devices.add(device);
        groups = new ArrayList<>();
    }

    public void addDevice(Device device)
    {
        if(device != null)
            devices.add(device);
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }
}
