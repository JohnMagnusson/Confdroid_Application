package com.bykth.confdroid.confdroid_application.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public User(String name, String email)
    {
        this.name = name;
        this.email = email;
        this.devices = new ArrayList<>();
        this.groups = new ArrayList<>();
    }

    public User(String name, String email, JSONArray devices) throws JSONException {
        this.name = name;
        this.email = email;
        this.devices = new ArrayList<>();
        this.groups = new ArrayList<>();
        for (int i = 0; i < devices.length(); i++) {
            JSONObject device = devices.getJSONObject(i);
            this.devices.add(new Device(device.getString("name"), device.getString("imei"), device.getJSONArray("applications")));
        }
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

    public ArrayList<Device> getDevices() {
        return (ArrayList<Device>) devices.clone();
    }
}
