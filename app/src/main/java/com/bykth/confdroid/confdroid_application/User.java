package com.bykth.confdroid.confdroid_application;

/**
 * Created by Elias on 2017-04-05.
 */
public class User
{
    private String name;
    private String email;

    public User(String name)
    {
        this.name = name;
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
