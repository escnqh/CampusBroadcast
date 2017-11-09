package com.newthread.campusbroadcast.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.Gson;
import com.newthread.campusbroadcast.User.User;
import com.newthread.campusbroadcast.WebService.InitViewService;
import com.newthread.campusbroadcast.WebService.JumpRequstChannelInfoService;
import com.newthread.campusbroadcast.WebService.isFollowService;
import com.newthread.campusbroadcast.bean.ChannelInfomationBean;
import com.newthread.campusbroadcast.bean.ChannelJumpRequstBean;
import com.newthread.campusbroadcast.bean.ChannelJumpResultBean;
import com.newthread.campusbroadcast.bean.isFollowBean;
import com.newthread.campusbroadcast.ui.activity.ChannelActivity;
import com.newthread.campusbroadcast.webApi.InitViewApi;
import com.newthread.campusbroadcast.webApi.JumpRequestChannelInfoApi;
import com.newthread.campusbroadcast.webApi.isFollowApi;

import java.text.DecimalFormat;
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

public class JumpToChannelUtil {

    private int selfId;
    private int channelId;
    private int isFocused = 0;
    private int channelHostId;
    private boolean isOnPlay = false;
    private Context act = null;

    public JumpToChannelUtil(int channelId, Context act) {
        this.act = act;
        this.channelId = channelId;
        if (channelId > 0)
            getInfo();


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
                    isfocused();
                } else {
                    Toast.makeText(act, "异常错误！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChannelJumpResultBean> call, Throwable t) {
                Toast.makeText(act, "获取不到电台！", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void isonplay() {
        InitViewApi initViewApi = new InitViewApi();
        InitViewService initViewService = initViewApi.getService();
        String re = "{\"userID\":" + 5955 + "}";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), re);
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
                    String weekDay = new DecimalFormat("0000000").format(response.body().getWeekday());
                    int a[] = new int[weekDay.length()];
                    for (int i = 0; i < weekDay.length(); i++) {
                        a[i] = Integer.parseInt(String.valueOf(weekDay.charAt(i)));
                    }
                    if (a[weekday - 1] == 1) {
                        if (nowtime >= startTime && nowtime <= endTime) {
                            isOnPlay = true;
                        }
                    }

                }
                startjump();
            }

            @Override
            public void onFailure(Call<ChannelInfomationBean> call, Throwable t) {

            }
        });

    }

    private void isfocused() {
        isFollowApi isApi = new isFollowApi();
        isFollowService isService = isApi.getService();
        String re = "{\"userID\":" + selfId + "}";
        RequestBody r = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), re);
        Call<isFollowBean> call_isFollow = isService.getState(r);
        call_isFollow.enqueue(new Callback<isFollowBean>() {
            @Override
            public void onResponse(Call<isFollowBean> call, Response<isFollowBean> response) {
                List<isFollowBean.ChannelInfsBean> infos = response.body().getChannelInfs();
                isFollowBean.ChannelInfsBean info;
                for (int i = 0; i < infos.size(); i++) {
                    info = infos.get(i);
                    if (channelId == info.getChannelID()) {
                        isFocused = 1;
                    }
                }
                isonplay();
            }

            @Override
            public void onFailure(Call<isFollowBean> call, Throwable t) {

            }
        });

    }

    public void startjump() {
        Intent intent = new Intent(act, ChannelActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("selfId", selfId);
        bundle.putInt("channelId", channelId);
        bundle.putInt("channelHostId", channelHostId);
        bundle.putInt("isFocused", isFocused);
        bundle.putBoolean("isOnPlay", isOnPlay);
        intent.putExtras(bundle);
        act.startActivity(intent);
    }


}
