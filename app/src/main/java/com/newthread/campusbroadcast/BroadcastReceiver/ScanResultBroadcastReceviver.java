package com.newthread.campusbroadcast.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by 倪启航 on 2017/7/22.
 */

public class ScanResultBroadcastReceviver extends BroadcastReceiver {

    private int channelId;
    private ScanResultInteraction scanResultInteraction;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
        channelId=bundle.getInt("channelId");
        scanResultInteraction.getScanResult(channelId);
    }

    public interface ScanResultInteraction{
        public void getScanResult(int channelId);
    }

    public void setScanResultBRListener(ScanResultInteraction scanResultBRListener ){
        this.scanResultInteraction=scanResultBRListener;
    }
}
