package com.newthread.campusbroadcast.bean.ChannelBean;

import java.util.List;

/**
 * Created by 张云帆 on 2017/7/31.
 */

public class GHotSearch {

    private List<ChannelInfsBean> channelInfs;

    public List<ChannelInfsBean> getChannelInfs() {
        return channelInfs;
    }

    public void setChannelInfs(List<ChannelInfsBean> channelInfs) {
        this.channelInfs = channelInfs;
    }

    public static class ChannelInfsBean {
        /**
         * channelID : 1199
         * channelName : 兔子君的电台
         * userID : 5326
         * nickName : 兔子君
         * imgURL :
         */

        private int channelID;
        private String channelName;
        private int userID;
        private String nickName;
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

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getImgURL() {
            return imgURL;
        }

        public void setImgURL(String imgURL) {
            this.imgURL = imgURL;
        }
    }
}
