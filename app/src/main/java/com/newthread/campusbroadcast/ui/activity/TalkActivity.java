package com.newthread.campusbroadcast.ui.activity;


import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algebra.sdk.API;
import com.algebra.sdk.ChannelApi;
import com.algebra.sdk.DeviceApi;
import com.algebra.sdk.SessionApi;
import com.algebra.sdk.entity.CompactID;
import com.algebra.sdk.entity.Session;
import com.google.gson.Gson;
import com.newthread.campusbroadcast.R;
import com.newthread.campusbroadcast.WebService.BeginBroadcastingService;
import com.newthread.campusbroadcast.WebService.BreakBroadcastingService;
import com.newthread.campusbroadcast.WebService.InitViewService;
import com.newthread.campusbroadcast.bean.BeginBCInfo;
import com.newthread.campusbroadcast.bean.BreakBCInfo;
import com.newthread.campusbroadcast.bean.ChannelInfomationBean;
import com.newthread.campusbroadcast.bean.isTrueBean;
import com.newthread.campusbroadcast.ui.listener.ChannelListener;
import com.newthread.campusbroadcast.ui.listener.MediaListener;
import com.newthread.campusbroadcast.ui.listener.SessionListener;
import com.newthread.campusbroadcast.util.GoBackToMainActivityUtil;
import com.newthread.campusbroadcast.util.JumpToShareUtil;
import com.newthread.campusbroadcast.webApi.BeginBroadcastingApi;
import com.newthread.campusbroadcast.webApi.BreakBroadcastingApi;
import com.newthread.campusbroadcast.webApi.InitViewApi;
import com.squareup.picasso.Picasso;

import java.util.Date;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 倪启航 on 2017/7/22.
 */

public class TalkActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {


    @BindView(R.id.button_talk)
    CircleButton button_talk;
    @BindView(R.id.channel_talk)
    ImageButton channel_talk;
    @BindView(R.id.imagebackground)
    ImageView imagebackground;
    @BindView(R.id.button_report)
    ImageView button_report;
    @BindView(R.id.button_back)
    ImageView button_back;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.channel_name)
    TextView channelName;
    @BindView(R.id.number_credit)
    TextView numberCredit;
    @BindView(R.id.channel_introduction)
    TextView channelIntroduction;
    @BindView(R.id.button_soundlevel)
    ImageButton buttonSoundlevel;
    @BindView(R.id.bottom_bar)
    RelativeLayout bottomBar;
    @BindView(R.id.bg)
    LinearLayout bg;
    @BindView(R.id.icon_head)
    ImageView iconHead;
    @BindView(R.id.number_online)
    TextView numberOnline;
    @BindView(R.id.number_marked)
    TextView numberMarked;
    @BindView(R.id.button_mark)
    ImageView buttonMark;
    @BindView(R.id.button_share)
    ImageView buttonShare;

    public static final String TAG = "activity.talk";
    private static final int NUMONLINE = 1;
    private static final int INITVIEW = 2;
    private static final int SETBACK = 3;

    private boolean isOnPlay = false;

    private boolean isTriggered = true;
    private volatile boolean isTBPressed = false;


    private ChannelApi channelApi = null;
    private SessionApi sessionApi = null;
    private DeviceApi deviceApi = null;
    private AudioManager audioManager = null;
    private ChannelListener channelListener;
    private MediaListener mediaListener;
    private SessionListener sessionListener;

    private int selfId;
    private int channelId;
    private int channelHostId;
    private int channelType;
    private boolean isFocused;
    private String channel_name;
    private int channel_markednum;
    private int channel_credit;
    private String channel_introduction;
    private String path_imagebackground;
    private String path_iconHead;

    private Session currSess = null;
    private CompactID currSession = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NUMONLINE:
                    numberOnline.setText("在线：" + msg.arg1 + "人");
                    break;
                case INITVIEW:
                    initView();
                    break;
                case SETBACK:
                    buttonSoundlevel.setBackgroundResource(R.drawable.soundlevel0);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        Log.i(TAG,"_________onreate________");
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        selfId = bundle.getInt("selfId");
        channelId = bundle.getInt("channelId");
        channelType = 2;
        channelHostId = bundle.getInt("channelHostId");
        isOnPlay = bundle.getBoolean("isOnPlay");
        getView();
        if (isOnPlay) {
            sendBeginBroadcastingMessage();
            sessionListener = new SessionListener(this);
            channelListener = new ChannelListener(this);
            mediaListener = new MediaListener(this);
            startTL();
            audioManager = ((AudioManager) getSystemService(Context.AUDIO_SERVICE));
            button_talk.setOnTouchListener(this);
            channel_talk.setOnClickListener(this);
            startSession();
        }
        buttonShare.setOnClickListener(this);
        button_report.setOnClickListener(this);
        button_back.setOnClickListener(this);
        buttonMark.setOnClickListener(this);
    }

    private void getView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InitViewApi initViewApi = new InitViewApi();
                InitViewService initViewService = initViewApi.getService();
                String re = "{\"userID\":" + channelHostId + "}";
                final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), re);
                Call<ChannelInfomationBean> call_initView = initViewService.getState(requestBody);
                call_initView.enqueue(new Callback<ChannelInfomationBean>() {
                    @Override
                    public void onResponse(Call<ChannelInfomationBean> call, Response<ChannelInfomationBean> response) {
                        if (response.body() != null) {
                            channel_name = response.body().getChannelName();
                            channel_introduction = response.body().getContent();
                            channel_markednum = response.body().getFans();
                            channel_credit = response.body().getCreditValue();
                            path_iconHead = response.body().getUserImg();
                            path_imagebackground = response.body().getChannelImg();
                            Message message = new Message();
                            message.what = INITVIEW;
                            handler.sendMessage(message);
                        } else {
                            Toast.makeText(getApplicationContext(), "电台信息加载失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ChannelInfomationBean> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "电台信息加载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void initView() {
        channelName.setText(channel_name);
        numberMarked.setText(channel_markednum + "");
        numberCredit.setText(channel_credit + "");
        channelIntroduction.setText(channel_introduction);
        if (path_imagebackground != null && path_imagebackground != "") {
            Picasso.with(this).load(path_imagebackground).error(R.drawable.error).into(imagebackground);
        }
        if (path_iconHead != null && path_iconHead != "") {
            Picasso.with(this).load(path_iconHead).error(R.drawable.error).into(iconHead);
        }
    }

    public void changeNumofOnline(int num) {
        Message message = new Message();
        message.what = NUMONLINE;
        message.arg1 = num;
        handler.sendMessage(message);
    }

    private void startTL() {
        channelApi = API.getChannelApi();
        sessionApi = API.getSessionApi();
        if (channelApi != null && sessionApi != null) {
            channelApi.setOnChannelListener(channelListener);
            sessionApi.setOnSessionListener(sessionListener);
            //channelApi.focusPublicChannel(selfId,channelType,channelId,"androids");
            sessionApi.sessionCall(selfId, channelType, channelId);
            currSession=new CompactID(channelType,channelId);
            getChannelMemberList(channelType, channelId);
            sessionApi.getCurrentSession(selfId);
            Log.i(TAG,"_________TLstart________");

        } else {
            startTL();
        }

    }

    private void startSession() {
        sessionApi.setOnMediaListener(mediaListener);
    }

    private void getChannelMemberList(int ctype, int cid) {
        if (channelApi != null) {
            channelApi.channelMemberGet(selfId, ctype, cid);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(isOnPlay){
            sessionApi.sessionBye(selfId, channelType, channelId);
            sendBreakBroadcastingMessage();
            if (sessionApi != null) {
                sessionApi.setOnMediaListener(null);
                sessionApi.setOnSessionListener(null);
                sessionApi = null;
            }
            if (channelApi != null) {
                channelApi.setOnChannelListener(null);
                channelApi = null;
            }
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_mark:
                Toast.makeText(this, "不用关注自己哟~", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_report:
                Toast.makeText(this, "不用举报自己哟~", Toast.LENGTH_SHORT).show();
                break;
            case R.id.channel_talk:
                if (isTriggered == true) {
                    isTriggered = false;
                    channel_talk.setBackgroundResource(R.drawable.micphone_click);
                    Toast.makeText(this, "听众可以跟你交流啦~", Toast.LENGTH_SHORT).show();
                } else if (isTriggered == false) {
                    isTriggered = true;
                    Toast.makeText(this, "关闭交流功能啦~", Toast.LENGTH_SHORT).show();
                    channel_talk.setBackgroundResource(R.drawable.micphone);
                }
                break;
            case R.id.button_share:
                JumpToShareUtil jumpToShareUtil = new JumpToShareUtil(channelId + "", this);
                jumpToShareUtil.startJump();
                break;
            case R.id.button_back:
//                GoBackToMainActivityUtil goback = new GoBackToMainActivityUtil(this);
//                goback.Go();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (sessionApi == null || currSession == null) {
            if (isTBPressed) {
                isTBPressed = false;
            }
            if (action == MotionEvent.ACTION_UP) {
                //进入频道错误
                Log.i(TAG, "进入频道错误");
            }
            return false;
        } else {
            if (!isTriggered) {
                if (action == MotionEvent.ACTION_DOWN) {
                    talkRequest(currSession);
                    button_talk.setImageResource(R.drawable.talk);
                } else if (action == MotionEvent.ACTION_UP) {
                    talkRelease(currSession);
                    button_talk.setImageResource(R.drawable.talk_ready);
                }
            } else {
                if (action == MotionEvent.ACTION_DOWN) {
                    if (!isTBPressed) {
                        talkRequest(currSession);
                        button_talk.setImageResource(R.drawable.talk);
                        isTBPressed = false;
                    }else {
                        isTBPressed = true;
                    }

                } else if (action == MotionEvent.ACTION_UP) {
                    if (isTBPressed ) {
                        talkRelease(currSession);
                        button_talk.setImageResource(R.drawable.talk_ready);
                        isTBPressed = false;
                    }else {
                        isTBPressed = true;
                    }

                    //isTriggered = false;
                }
            }
        }
        return false;
    }

    private void talkRequest(CompactID session) {

        if (session != null && sessionApi != null)
            sessionApi.talkRequest(selfId, session.getType(), session.getId());
    }

    private void talkRelease(CompactID session) {

        if (session != null && sessionApi != null)
            sessionApi.talkRelease(selfId, session.getType(), session.getId());
    }

    private void sendBeginBroadcastingMessage() {
        Date now = new Date();
        long nowtime = now.getTime();
        int nowday = now.getDay();
        BeginBroadcastingApi beginApi = new BeginBroadcastingApi();
        BeginBroadcastingService beginService = beginApi.getService();
        BeginBCInfo beginBCInfo = new BeginBCInfo();
        beginBCInfo.setChannelID(channelId);
        beginBCInfo.setStartTime(nowtime);
        beginBCInfo.setWeekday(nowday);
        Gson gson = new Gson();
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(beginBCInfo));
        Call<isTrueBean> call_beginBroadcast = beginService.getState(requestBody);
        call_beginBroadcast.enqueue(new Callback<isTrueBean>() {
            @Override
            public void onResponse(Call<isTrueBean> call, Response<isTrueBean> response) {
                if (response.body() != null) {
                    int result = response.body().getSign();
                    if (result == 1) {
                        Log.i(TAG, "开始打卡成功");
                    } else {
                        Log.i(TAG, "开始打卡失败");
                    }
                } else {
                    Log.i(TAG, "开始打卡返回值错误");
                }
            }

            @Override
            public void onFailure(Call<isTrueBean> call, Throwable t) {
                Log.i(TAG, "开始打卡返回失败");
            }
        });

    }

    private void sendBreakBroadcastingMessage() {
        Date now = new Date();
        long nowtime = now.getTime();
        int nowday = now.getDay();
        BreakBroadcastingApi breakApi = new BreakBroadcastingApi();
        BreakBroadcastingService breakService = breakApi.getService();
        BreakBCInfo breakBCInfo = new BreakBCInfo();
        breakBCInfo.setWeekday(nowday);
        breakBCInfo.setStartTime(nowtime);
        breakBCInfo.setChannelID(channelId);
        Gson gson = new Gson();
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(breakBCInfo));
        Call<isTrueBean> call_breakBroadcast = breakService.getState(requestBody);
        call_breakBroadcast.enqueue(new Callback<isTrueBean>() {

            @Override
            public void onResponse(Call<isTrueBean> call, Response<isTrueBean> response) {
                if (response.body() != null) {
                    int result = response.body().getSign();
                    if (result == 1) {
                        Log.i(TAG, "结束打卡成功");
                    } else {
                        Log.i(TAG, "结束打卡失败");
                    }

                } else {
                    Log.i(TAG, "结束打卡返回值错误");
                }
            }

            @Override
            public void onFailure(Call<isTrueBean> call, Throwable t) {
                Log.i(TAG, "结束打卡返回失败");
            }
        });
    }


    public void onSomeoneSpeaking() {
        buttonSoundlevel.setBackgroundResource(R.drawable.soundlevel1);
        handler.postDelayed(setback, 1000);
    }

    private Runnable setback = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = SETBACK;
            handler.sendMessage(msg);
        }
    };
}
