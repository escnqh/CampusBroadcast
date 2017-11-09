package com.newthread.campusbroadcast.bean;

/**
 * Created by 倪启航 on 2017/7/29.
 */

public class ReportBean {
    /**
     * userID : 0
     * channelID : 0
     * event : 0
     * time : 1233433
     */

    private int userID;
    private int channelID;
    private int event;
    private long time;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
