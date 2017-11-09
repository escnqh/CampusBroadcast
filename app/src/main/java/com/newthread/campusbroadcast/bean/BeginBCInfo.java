package com.newthread.campusbroadcast.bean;

/**
 * Created by 倪启航 on 2017/7/27.
 */

public class BeginBCInfo {

    /**
     * channelHostID : 0
     * channelID : 0
     * startTime : 0
     * weekday : 0
     */

    private int channelHostID;
    private int channelID;
    private long startTime;
    private int weekday;

    public void initData(int channelHostID, int channelID, long startTime, int weekday) {
        setChannelHostID(channelHostID);
        setChannelID(channelID);
        setStartTime(startTime);
        setWeekday(weekday);
    }

    public int getChannelHostID() {
        return channelHostID;
    }

    public void setChannelHostID(int channelHostID) {
        this.channelHostID = channelHostID;
    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }
}
