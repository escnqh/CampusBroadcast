package com.newthread.campusbroadcast.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.newthread.campusbroadcast.BroadcastReceiver.ScanResultBroadcastReceviver;
import com.newthread.campusbroadcast.ui.activity.ScanActivity;


/**
 * Created by 倪启航 on 2017/7/15.
 */

public class JumpToScanUtil implements ScanResultBroadcastReceviver.ScanResultInteraction{
    private int ChannelId;
    Context act=null;

    public JumpToScanUtil(Context act){
        this.act=act;
    }

    public void startJump(){

        Intent intent=new Intent(act, ScanActivity.class);
        act.startActivity(intent);
    }


    @Override
    public void getScanResult(int channelId) {
        if(channelId!=0){
            this.ChannelId=channelId;
        }
    }
}
