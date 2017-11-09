package com.newthread.campusbroadcast.bean.ChannelBean;

import java.util.List;

/**
 * Created by 张云帆 on 2017/7/31.
 */

public class GSearchResult {

    private List<ChannelInfsBean> channelInfs;

    public List<ChannelInfsBean> getChannelInfs() {
        return channelInfs;
    }

    public void setChannelInfs(List<ChannelInfsBean> channelInfs) {
        this.channelInfs = channelInfs;
    }

    public static class ChannelInfsBean {
        /**
         * nickname : nickname4
         * channelName : channelName1
         * userID : 6004
         * channelID : 6004
         */

        private String nickname;
        private String channelName;
        private int userID;
        private int channelID;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public int getChannelID() {
            return channelID;
        }

        public void setChannelID(int channelID) {
            this.channelID = channelID;
        }
    }
}