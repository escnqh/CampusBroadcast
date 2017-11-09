package com.newthread.campusbroadcast.bean;

import java.util.List;

/**
 * Created by 倪启航 on 2017/7/31.
 */

public class isFollowBean {

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
         * content :
         * fans :
         * startTime :
         * endTime :
         * weekday :
         * imgURL :
         */

        private int channelID;
        private String channelName;
        private int userID;
        private String nickname;
        private String content;
        private int fans;
        private long startTime;
        private long endTime;
        private int weekday;
        private String imgURL;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getFans() {
            return fans;
        }

        public void setFans(int fans) {
            this.fans = fans;
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

        public int getWeekday() {
            return weekday;
        }

        public void setWeekday(int weekday) {
            this.weekday = weekday;
        }

        public String getImgURL() {
            return imgURL;
        }

        public void setImgURL(String imgURL) {
            this.imgURL = imgURL;
        }
    }
}
