package com.newthread.campusbroadcast.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.newthread.campusbroadcast.ui.activity.MakeUpShareQRCodeActivity;


/**
 * Created by 倪启航 on 2017/7/15.
 */

public class JumpToShareUtil {

    private String ChannelId;
    Context act=null;

    public JumpToShareUtil(String ChannelId, Context act){
        this.ChannelId=ChannelId;
        this.act=act;
    }

    public void startJump(){
        Intent intent=new Intent(act, MakeUpShareQRCodeActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("ChannelId",ChannelId);
        intent.putExtras(bundle);
        act.startActivity(intent);
    }

}
