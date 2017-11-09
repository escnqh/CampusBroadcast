package com.newthread.campusbroadcast.User;

/**
 * Created by HP on 2017/7/27.
 */

public class SignupBean {
    private int userID;
    private String userTL;
    private String password;
    private String nickname;

    public int getUserID() {
        return userID;
    }

    public void setUserId(int userID) {
        this.userID = userID;
    }

    public String getUserTL() {
        return userTL;
    }

    public void setUserTL(String userTL) {
        this.userTL = userTL;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
