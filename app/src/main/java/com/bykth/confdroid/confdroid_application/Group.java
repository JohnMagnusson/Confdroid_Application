package com.bykth.confdroid.confdroid_application;

/**
 * Created by Elias on 2017-04-05.
 */
public class Group
{
    private String groupName;
    private int groupId;

    public Group(String groupName, int groupId)
    {
        this.groupName = groupName;
        this.groupId = groupId;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public int getGroupId()
    {
        return groupId;
    }
}
