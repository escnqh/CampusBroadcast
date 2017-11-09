package com.newthread.campusbroadcast.bean.ChannelBean;

/**
 * Created by 张云帆 on 2017/7/30.
 */

public class SCreateChannelBean {

    /**
     * userID : 5326
     * channelID : 1199
     * channelName : tuzi
     * content : 这里是兔子君
     * startTime : 123456
     * endTime : 456789
     * weekDay : 1110000
     * channelType : 2
     */

    private int userID;
    private int channelID;
    private String channelName;
    private String content;
    private long startTime;
    private long endTime;
    private int weekday;
    private int channelType;

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

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getWeekDay() {
        return weekday;
    }

    public void setWeekDay(int weekDay) {
        this.weekday = weekDay;
    }

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }
}
