package com.newthread.campusbroadcast.bean.ChannelBean;

import java.util.List;

/**
 * Created by 张云帆 on 2017/8/1.
 */

//用于全部电台和学校推荐电台
public class GChannelInfor {

    private List<ChannelInfsBean> channelInfs;

    public List<ChannelInfsBean> getChannelInfs() {
        return channelInfs;
    }

    public void setChannelInfs(List<ChannelInfsBean> channelInfs) {
        this.channelInfs = channelInfs;
    }

    public static class ChannelInfsBean {
        /**
         * channelID : 1165
         * channelName : channelName1
         * nickname : nickname1
         * gender : 男
         * fans : 5
         * imgURL : 这是地址
         */

        private int channelID;
        private String channelName;
        private String nickname;
        private String gender;
        private int fans;
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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getFans() {
            return fans;
        }

        public void setFans(int fans) {
            this.fans = fans;
        }

        public String getImgURL() {
            return imgURL;
        }

        public void setImgURL(String imgURL) {
            this.imgURL = imgURL;
        }
    }
}
