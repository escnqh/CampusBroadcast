package com.newthread.campusbroadcast.bean;

import java.util.List;

/**
 * Created by 倪启航 on 2017/7/30.
 */

public class ChannelJumpResultBean {

    private List<ChannelInfsBean> channelInfs;

    public List<ChannelInfsBean> getChannelInfs() {
        return channelInfs;
    }

    public void setChannelInfs(List<ChannelInfsBean> channelInfs) {
        this.channelInfs = channelInfs;
    }

    public static class ChannelInfsBean {
        /**
         * channelID :
         * channelName :
         * userID :
         * nickname :
         */

        private int channelID;
        private String channelName;
        private int userID;
        private String nickname;

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

        public int getUserID() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
