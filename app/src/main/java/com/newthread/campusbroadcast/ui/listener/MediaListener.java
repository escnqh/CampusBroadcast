package com.newthread.campusbroadcast.ui.listener;

import android.content.Context;

import com.algebra.sdk.OnMediaListener;
import com.algebra.sdk.entity.HistoryRecord;
import com.newthread.campusbroadcast.ui.activity.ChannelActivity;
import com.newthread.campusbroadcast.ui.activity.TalkActivity;

/**
 * Created by 倪启航 on 2017/7/17.
 */

public class MediaListener implements OnMediaListener {

    private Context context=null;
    private TalkActivity talkActivity = null;
    private ChannelActivity channelActivity = null;
//    private int userId;
//    private int channelType;
//    private int sessionId;




    public MediaListener(Context context){
        this.context=context;
    }

    public MediaListener(ChannelActivity channelActivity){

        this.channelActivity=channelActivity;

    }

    public MediaListener(TalkActivity talkActivity){

        this.talkActivity=talkActivity;

    }

    @Override
    public void onMediaInitializedEnd(int userId, int channelType, int sessionId) {
        //Toast.makeText(context, channelType + ":" + sessionId + " media initialized.", Toast.LENGTH_SHORT).show();

//        this.userId=userId;
//        this.channelType=channelType;
//        this.sessionId=sessionId;

    }

    @Override
    public void onPttButtonPressed(int userId, int state) {
        //Ptt键被按下

    }

    @Override
    public void onTalkRequestConfirm(int i, int i1, int i2, int i3, boolean b) {
        //获得话权

    }

    @Override
    public void onTalkRequestDeny(int i, int i1, int i2) {

    }

    @Override
    public void onTalkRequestQueued(int i, int i1, int i2) {

    }

    @Override
    public void onTalkReleaseConfirm(int i, int i1) {

    }

    @Override
    public void onTalkTransmitBroken(int i, int i1) {

    }

    @Override
    public void onStartPlaying(int i, int i1, int i2, int i3) {

    }

    @Override
    public void onPlayStopped(int i) {

    }

    @Override
    public void onSomeoneSpeaking(int i, int i1, int i2, int i3, int i4) {
        if (talkActivity == null && channelActivity != null) {
            channelActivity.onSomeoneSpeaking();
        } else if (talkActivity != null && channelActivity == null) {
            talkActivity.onSomeoneSpeaking();
        }

    }

    @Override
    public void onThatoneSayOver(int i, int i1) {

    }

    @Override
    public void onSomeoneAttempt(int i, int i1, int i2) {

    }

    @Override
    public void onThatAttemptQuit(int i, int i1, int i2) {

    }

    @Override
    public void onNewSpeakingCatched(HistoryRecord historyRecord) {

    }

    @Override
    public void onPlayLastSpeaking(int i, int i1) {

    }

    @Override
    public void onPlayLastSpeakingEnd(int i) {

    }

    @Override
    public void onMediaSenderCutted(int i, int i1) {

    }

    @Override
    public void onMediaSenderReport(long l, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onMediaReceiverReport(long l, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onRecorderMeter(int i, int i1) {

    }

    @Override
    public void onPlayerMeter(int i, int i1) {

    }

    @Override
    public void onBluetoothBatteryGet(int i) {

    }

    @Override
    public void onBluetoothConnect(int i) {

    }
}
