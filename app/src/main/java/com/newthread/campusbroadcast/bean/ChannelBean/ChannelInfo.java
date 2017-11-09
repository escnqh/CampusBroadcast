package com.newthread.campusbroadcast.bean.ChannelBean;

/**
 * Created by 张云帆 on 2017/7/23.
 */

public class ChannelInfo {
    private String imageId;
    private String channelName;
    private String broadcasterName;
    private String sex;
    private int beFocusedNum;
    private int channelId;
    private int broadcastId;


    //获取全部电台构造类
    public ChannelInfo(String imageId, String channelName, String broadcasterName, String sex, int beFocusedNum, int channelId) {
        this.imageId = imageId;
        this.channelName = channelName;
        this.broadcasterName = broadcasterName;
        this.sex = sex;
        this.beFocusedNum = beFocusedNum;
        this.channelId = channelId;
    }

    public ChannelInfo(String channelName, String broadcasterName, int channelId) {
        this.channelName = channelName;
        this.broadcasterName = broadcasterName;
        this.channelId = channelId;
    }

    public String  getImageId() {
        return imageId;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getBroadcasterName() {
        return broadcasterName;
    }

    public String getSex() {
        return sex;
    }

    public int getBeFocusedNum() {
        return beFocusedNum;
    }

    public int getChannelId() {
        return channelId;
    }

    public int getBroadcastId() {
        return broadcastId;
    }
}
