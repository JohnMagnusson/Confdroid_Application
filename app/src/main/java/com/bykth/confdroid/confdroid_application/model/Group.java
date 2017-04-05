package com.bykth.confdroid.confdroid_application.model;

/**
 * User can be in groups and have different settings depending on the groups set standard.
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
