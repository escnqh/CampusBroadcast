package com.newthread.campusbroadcast.util;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.algebra.sdk.entity.Constant;
import com.google.gson.Gson;
import com.newthread.campusbroadcast.User.User;
import com.newthread.campusbroadcast.WebService.InitViewService;
import com.newthread.campusbroadcast.WebService.JumpRequstChannelInfoService;
import com.newthread.campusbroadcast.bean.ChannelInfomationBean;
import com.newthread.campusbroadcast.bean.ChannelJumpRequstBean;
import com.newthread.campusbroadcast.bean.ChannelJumpResultBean;
import com.newthread.campusbroadcast.ui.activity.TalkActivity;
import com.newthread.campusbroadcast.webApi.InitViewApi;
import com.newthread.campusbroadcast.webApi.JumpRequestChannelInfoApi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 倪启航 on 2017/7/17.
 */

public class JumpToTalkUtil {


    private int selfId;
    private int channelId;
    private int channelHostId;
    private boolean isOnPlay = false;
    private Context act = null;

    public JumpToTalkUtil(int channelId, Context act) {
        this.act = act;
        this.channelId = channelId;
        if (channelId > 0)
            getInfo();
    }

    public void startjump() {
        Intent intent = new Intent(act, TalkActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("selfId", selfId);
        bundle.putInt("channelId", channelId);
        bundle.putInt("channelHostId", channelHostId);
        bundle.putBoolean("isOnPlay", isOnPlay);
        intent.putExtras(bundle);
        act.startActivity(intent);
    }

    private void getInfo() {
        User me = User.getInstance();
        selfId = me.getUserID();
        Gson g = new Gson();
        ChannelJumpRequstBean requstBean = new ChannelJumpRequstBean();
        List<Integer> channelIDs = new ArrayList<>();
        channelIDs.add(channelId);
        requstBean.setChannelIDs(channelIDs);
        JumpRequestChannelInfoApi requstApi = new JumpRequestChannelInfoApi();
        JumpRequstChannelInfoService requstService = requstApi.getService();
        String i = g.toJson(requstBean);
        RequestBody r = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), i);
        Call<ChannelJumpResultBean> call_info = requstService.getState(r);
        call_info.enqueue(new Callback<ChannelJumpResultBean>() {
            @Override
            public void onResponse(Call<ChannelJumpResultBean> call, Response<ChannelJumpResultBean> response) {
                if (response.body() != null) {
                    List<ChannelJumpResultBean.ChannelInfsBean> infos = response.body().getChannelInfs();
                    ChannelJumpResultBean.ChannelInfsBean info = infos.get(0);
                    channelHostId = info.getUserID();
                    isonplay();
                } else {
                    Toast.makeText(act, "异常错误！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChannelJumpResultBean> call, Throwable t) {
                Toast.makeText(act, "跳转失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void isonplay() {
        InitViewApi initViewApi = new InitViewApi();
        InitViewService initViewService = initViewApi.getService();
        String re = "{\"userID\":" + channelHostId + "}";
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), re);
        Call<ChannelInfomationBean> call_initView = initViewService.getState(requestBody);
        call_initView.enqueue(new Callback<ChannelInfomationBean>() {
            @Override
            public void onResponse(Call<ChannelInfomationBean> call, Response<ChannelInfomationBean> response) {
                if (response.body() != null) {
                    Date d = new Date();
                    int weekday = d.getDay();
                    long nowtime = d.getHours() * 3600 * 1000 + d.getMinutes() * 60 * 1000 + d.getSeconds() * 1000;
                    long startTime = response.body().getStartTime();
                    long endTime = response.body().getEndTime();
                    String weekDay = response.body().getWeekday() + "";
                    int a[] = new int[weekDay.length()];
                    for (int i = 0; i < weekDay.length(); i++) {
                        a[i] = Integer.parseInt(String.valueOf(weekDay.charAt(i)));
                    }
                    if (a[weekday - 1] == 1) {
                        if (nowtime >= startTime && nowtime <= endTime) {
                            isOnPlay = true;
                        }
                    }

                } else {

                }
                startjump();
            }

            @Override
            public void onFailure(Call<ChannelInfomationBean> call, Throwable t) {

            }
        });
    }

}
