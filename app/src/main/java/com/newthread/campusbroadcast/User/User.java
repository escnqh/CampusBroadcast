package com.newthread.campusbroadcast.User;

import com.algebra.sdk.AccountApi;
import com.newthread.campusbroadcast.ui.listener.AccountListener;

import java.io.Serializable;

/**
 * Created by HP on 2017/7/27.
 */

public class User implements Serializable {
    public static User user;

    private User() {
    }

    public static User getInstance() {
        if (user == null) {
            synchronized (User.class) {
                if (user == null) {
                    user = new User();
                }
            }
        }
        return user;
    }

    private boolean isLogin;
    private boolean successful;
    private int userID;//用户id
    private int state;
    private String username;//昵称
    private String useraccount;//途聆号
    private String userpassword;//密码
    private String usergender;//性别
    private String userphone;//绑定手机号
    private int rank;//身份
    private String college;
    private int creditValue;
    private int age;
    private String imgUrl;//用户头像
    private String registerAccount;
    private AccountApi accountApi;
    private AccountListener accountListener;
    private int isNick;

    public int getIsNick() {
        return isNick;
    }

    public void setIsNick(int isNick) {
        this.isNick = isNick;
    }

    public AccountListener getAccountListener() {
        return accountListener;
    }

    public void setAccountListener(AccountListener accountListener) {
        this.accountListener = accountListener;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public AccountApi getAccountApi() {
        return accountApi;
    }

    public void setAccountApi(AccountApi accountApi) {
        this.accountApi = accountApi;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        User.user = user;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseraccount() {
        return useraccount;
    }

    public void setUseraccount(String useraccount) {
        this.useraccount = useraccount;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getUsergender() {
        return usergender;
    }

    public void setUsergender(String usergender) {
        this.usergender = usergender;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getRegisterAccount() {
        return registerAccount;
    }

    public void setRegisterAccount(String registerAccount) {
        this.registerAccount = registerAccount;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getCreditValue() {
        return creditValue;
    }

    public void setCreditValue(int creditValue) {
        this.creditValue = creditValue;
    }

    @Override
    public String toString() {
        return "USER{" +
                "state=" + state + '\'' +
                "isLogin='" + isLogin + '\'' +
                ", userID='" + userID + '\'' +
                ", username='" + username + '\'' +
                ", useraccount='" + useraccount + '\'' +
                ", userpassword='" + userpassword + '\'' +
                ", usergender='" + usergender + '\'' +
                ", userphone='" + userphone + '\'' +
                '}';
    }
}


