package com.newthread.campusbroadcast.bean;

/**
 * Created by 倪启航 on 2017/7/29.
 */

public class UnFollowBean {
    /**
     * userID1 : 0
     * userID2 : 0
     * channelID : 0
     */

    private int userID1;
    private int userID2;
    private int channelID;

    public int getUserID1() {
        return userID1;
    }

    public void setUserID1(int userID1) {
        this.userID1 = userID1;
    }

    public int getUserID2() {
        return userID2;
    }

    public void setUserID2(int userID2) {
        this.userID2 = userID2;
    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }
}
