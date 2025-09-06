package org.smartregister.chw.ayp.domain;

import java.io.Serializable;

public class GroupObject implements Serializable {
    private String groupId;
    private String groupName;
    private String location;
    private int memberCount;

    public GroupObject() { }

    public GroupObject(String groupId, String groupName, String location, int memberCount) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.location = location;
        this.memberCount = memberCount;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }
}

