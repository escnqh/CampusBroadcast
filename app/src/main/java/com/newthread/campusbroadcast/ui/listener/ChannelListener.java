package com.newthread.campusbroadcast.ui.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.algebra.sdk.API;
import com.algebra.sdk.OnChannelListener;
import com.algebra.sdk.entity.Channel;
import com.algebra.sdk.entity.Constant;
import com.algebra.sdk.entity.Contact;
import com.algebra.sdk.entity.IntStr;
import com.newthread.campusbroadcast.ui.activity.ChannelActivity;
import com.newthread.campusbroadcast.ui.activity.ChannelInformationActivity;
import com.newthread.campusbroadcast.ui.activity.CreatChannelActivity;
import com.newthread.campusbroadcast.ui.activity.SearchResultActivity;
import com.newthread.campusbroadcast.ui.activity.TalkActivity;
import com.newthread.campusbroadcast.ui.fragment.MyFocusFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 倪启航 on 2017/7/17.
 */

public class ChannelListener implements OnChannelListener {
    private String TAG = "cllll";
    private static List<Channel> searchChannelResult;
    private Context context;
    private ChannelInformationActivity channelInformationActivity;
    private CreatChannelActivity creatChannelActivity;
    private MyFocusFragment myFocusFragment;

    public ChannelListener(MyFocusFragment myFocusFragment){
        this.myFocusFragment=myFocusFragment;
    }
    public ChannelListener(ChannelInformationActivity channelInformationActivity) {

        this.channelInformationActivity = channelInformationActivity;
    }

    public ChannelListener(CreatChannelActivity creatChannelActivity) {
        this.creatChannelActivity = creatChannelActivity;
    }


    public ChannelListener(Context context) {
        this.context = context;
    }

    public static List<Channel> getSearchResult(){
        return searchChannelResult;
    }

    private TalkActivity talkActivity = null;
    private ChannelActivity channelActivity = null;

    public ChannelListener(TalkActivity talkActivity) {

        this.talkActivity = talkActivity;

    }

    public ChannelListener(ChannelActivity channelActivity) {

        this.channelActivity = channelActivity;

    }

    @Override
    public void onDefaultChannelSet(int i, int i1, int i2) {

    }

    @Override
    public void onAdverChannelsGet(int i, Channel channel, List<Channel> list) {

    }

    @Override
    public void onChannelListGet(int userId, Channel channel, List<Channel> list) {
        List<Channel> channels = (List<Channel>) list;
        if (userId <= 0 || channels == null) {
            Log.e("mmm", "get channel lists error!" + userId);
            return;
        } else {
            Log.d("mmm", "get channel lists size:" + channels.size());
        }
        if (channels.size() > 0) {
            if (talkActivity == null && channelActivity != null) {
                //channelActivity.setChannels(channels);
            } else if (talkActivity != null && channelActivity == null) {
                //talkActivity.setChannels(channels);
            }
        }
    }

    @Override
    public void onChannelMemberListGet(int userId, int ctype, int channelId, List<Contact> mems) {
        Log.d("ccc", "memberlist size: " + mems.size());
        String selfNick = API.uid2nick(userId);
        Log.d("ccc", "self_nick: " + selfNick);
        if (userId <= 0) {
            Log.e("ccc", "get channel members error.");
            return;
        }
        List<Contact> mel = new ArrayList<Contact>();
        if (mems.size() == 0) {
            Contact me = new Contact(userId, selfNick, Constant.CONTACT_STATE_ONLINE);
            mel.add(me);
        } else {
            //在线人数
            List<IntStr> sessPresences = new ArrayList<IntStr>();
            sessPresences.add(new IntStr(userId, selfNick));
            for (Contact memb1 : mems) {
                if (memb1.isPresent && memb1.id != userId) {
                    sessPresences.add(new IntStr(memb1.id, memb1.name));
                    mel.add(memb1);
                    Log.d("ccc", "onChannelMemberListGet: " + memb1.name);
                }
            }
            Log.d("ccc", "sessPresencs list: " + sessPresences.size());
            if (talkActivity == null && channelActivity != null) {
                channelActivity.changeNumofOnline(sessPresences.size());
            } else if (talkActivity != null && channelActivity == null) {
                talkActivity.changeNumofOnline(sessPresences.size());
            }

        }

    }

    @Override
    public void onChannelNameChanged(int i, int i1, int i2, String s) {

    }

    @Override
    public void onChannelAdded(int i, int i1, int i2, String s) {

    }

    @Override
    public void onChannelRemoved(int i, int i1, int i2) {

    }

    @Override
    public void onChannelMemberAdded(int i, int i1, List<Contact> list) {

    }

    @Override
    public void onChannelMemberRemoved(int i, int i1, List<Integer> list) {

    }

    @Override
    public void onPubChannelCreate(int uid, int reason, int cid) {

        Log.d(TAG, "onPubChannelCreate: "+"创建电台成功");
        Log.d(TAG, "onPubChannelCreate: "+"用户id"+uid+""+"频道id"+cid+"reason"+reason);
        if (cid>0)
        {
            creatChannelActivity.upInfor(cid,2,true);
        }else {
            creatChannelActivity.upInfor(cid,2,false);
        }
    }
    //搜索接口回调
    @Override
    public void onPubChannelSearchResult(int i, List<Channel> list) {
        //把不需要密码的排除掉（要关注的情况况下）
        if (list.size()>0)
        {
            Log.d(TAG, "onPubChannelSearchResult: 找到该节目");
            for (int a=0;a<list.size();a++)
            {
                if(!list.get(a).needPassword)
                {
                    list.remove(a);
                }
                else
                    Log.d(TAG, "节目信息:电台Id"+list.get(a).cid.getId()+"名字"+list.get(a).name+"电台类型"+list.get(a).cid.getType());
            }
            searchChannelResult=list;
            if (searchChannelResult.size()>0){
                Intent intent = new Intent(context, SearchResultActivity.class);
                context.startActivity(intent);
            }
        }else
        {
            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
            normalDialog.setMessage("没有该电台");
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int wch) {
                        }
                    });
            // 显示
            normalDialog.show();
        }
    }

    @Override
    public void onPubChannelFocusResult(int i, int i1) {

    }

    @Override
    public void onPubChannelUnfocusResult(int i, int i1) {

    }

    @Override
    public void onPubChannelRenamed(int uid, int reason) {
        Log.d(TAG, "onPubChannelCreate: "+"修改电台成功");
        Log.d(TAG, "onPubChannelCreate: "+"用户id"+uid);
        if (uid>0)
        {
            channelInformationActivity.changeChannel(true);
        }else {
            channelInformationActivity.changeChannel(false);
        }

    }

    @Override
    public void onPubChannelDeleted(int uid, int result) {
        Log.d(TAG, "onPubChannelDeleted: "+uid+"---reason"+result);
        if(uid>0)
        {
            myFocusFragment.upInfor(1);
        }else {
            myFocusFragment.upInfor(0);
        }

    }

    @Override
    public void onCallMeetingStarted(int i, int i1, int i2, List<Contact> list) {

    }

    @Override
    public void onCallMeetingStopped(int i, int i1) {

    }
}
