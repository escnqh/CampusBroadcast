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
import com.newthread.campusbroadcast.WebService.FollowService;
import com.newthread.campusbroadcast.WebService.InitViewService;
import com.newthread.campusbroadcast.WebService.ReportService;
import com.newthread.campusbroadcast.WebService.UnfollowService;
import com.newthread.campusbroadcast.bean.ChannelInfomationBean;
import com.newthread.campusbroadcast.bean.FollowBean;
import com.newthread.campusbroadcast.bean.ReportBean;
import com.newthread.campusbroadcast.bean.UnFollowBean;
import com.newthread.campusbroadcast.bean.isTrueBean;
import com.newthread.campusbroadcast.ui.listener.ChannelListener;
import com.newthread.campusbroadcast.ui.listener.MediaListener;
import com.newthread.campusbroadcast.ui.listener.SessionListener;
import com.newthread.campusbroadcast.ui.view.ReportDialog;
import com.newthread.campusbroadcast.util.GoBackToMainActivityUtil;
import com.newthread.campusbroadcast.util.JumpToShareUtil;
import com.newthread.campusbroadcast.webApi.FollowApi;
import com.newthread.campusbroadcast.webApi.InitViewApi;
import com.newthread.campusbroadcast.webApi.ReportApi;
import com.newthread.campusbroadcast.webApi.UnfollowApi;
import com.squareup.picasso.Picasso;

import java.util.Date;

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

public class ChannelActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    public static final String TAG = "activity.channel";
    private static final int NUMONLINE = 1;
    private static final int INITVIEW = 2;
    private static final int SETBACK = 3;
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
    @BindView(R.id.channel_talk)
    ImageButton channelTalk;
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


    private int selfId;
    private int channelId;
    private int channelHostId;
    private int channelType;
    private int isFocused;
    private String channel_name;
    private int channel_markednum = 0;
    private int channel_credit = 0;
    private String channel_introduction;
    private String path_imagebackground;
    private String path_iconHead;
    private boolean isOnPlay = false;

    private ChannelApi channelApi = null;
    private SessionApi sessionApi = null;
    private DeviceApi deviceApi = null;
    private AudioManager audioManager = null;
    private ChannelListener channelListener;
    private MediaListener mediaListener;
    private SessionListener sessionListener;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"_________onreate________");
        setContentView(R.layout.activity_channel);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        selfId = bundle.getInt("selfId");
        channelId = bundle.getInt("channelId");
        channelType = 2;
        channelHostId = bundle.getInt("channelHostId");
        isFocused = bundle.getInt("isFocused");
        isOnPlay = bundle.getBoolean("isOnPlay");
        getView();
        if (isOnPlay) {
            sessionListener = new SessionListener(this);
            channelListener = new ChannelListener(this);
            mediaListener = new MediaListener(this);
            startTL();
            audioManager = ((AudioManager) getSystemService(Context.AUDIO_SERVICE));
            channelTalk.setOnTouchListener(this);
            startSession();
        }
        buttonShare.setOnClickListener(this);
        button_report.setOnClickListener(this);
        button_back.setOnClickListener(this);
        buttonMark.setOnClickListener(this);
    }

    private void getView() {
        if (isFocused == 1) {
            buttonMark.setBackgroundResource(R.drawable.marked);
        }
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

    private void startTL() {
        channelApi = API.getChannelApi();
        sessionApi = API.getSessionApi();
        if (channelApi != null && sessionApi != null) {
            channelApi.setOnChannelListener(channelListener);
            sessionApi.setOnSessionListener(sessionListener);
            channelApi.focusPublicChannel(selfId,channelType,channelId,"androids");
            sessionApi.sessionCall(selfId, channelType, channelId);
            currSession=new CompactID(channelType,channelId);
            getChannelMemberList(channelType, channelId);
            sessionApi.getCurrentSession(selfId);
            Log.i(TAG,"_________TLstart________");

        } else {
            startTL();
        }
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
            channelApi.unfocusPublicChannel(selfId, channelType, channelId);
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

    public void changeNumofOnline(int num) {
        Message message = new Message();
        message.what = NUMONLINE;
        message.arg1 = num;
        handler.sendMessage(message);
    }

    private void startSession() {
        sessionApi.setOnMediaListener(mediaListener);
        Log.i(TAG,"_________sessionStart________");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_report:
                Report report = new Report(this, view);
                break;
            case R.id.button_mark:
                if (isFocused == 1) {
                    unFollowIt();
                } else if (isFocused == 0) {
                    followIt();
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
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            talkRequest(currSession);
            channelTalk.setBackgroundResource(R.drawable.micphone_click);
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            talkRelease(currSession);
            channelTalk.setBackgroundResource(R.drawable.micphone);
        }
        return false;
    }

    private void followIt() {
        FollowBean followBean = new FollowBean();
        followBean.setChannelID(channelId);
        followBean.setUserID1(selfId);
        followBean.setUserID2(channelHostId);
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(followBean));
        FollowApi followApi = new FollowApi();
        FollowService followService = followApi.getService();
        Call<isTrueBean> call_follow = followService.getState(requestBody);
        call_follow.enqueue(new Callback<isTrueBean>() {
            @Override
            public void onResponse(Call<isTrueBean> call, Response<isTrueBean> response) {
                int result = response.body().getSign();
                if (result == 1) {
                    Toast.makeText(getApplicationContext(), "关注成功！", Toast.LENGTH_SHORT).show();
                    isFocused = 1;
                    buttonMark.setBackgroundResource(R.drawable.marked);
                } else {
                    Toast.makeText(getApplicationContext(), "关注失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<isTrueBean> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "关注失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void unFollowIt() {
        UnFollowBean unFollowBean = new UnFollowBean();
        unFollowBean.setChannelID(channelId);
        unFollowBean.setUserID1(selfId);
        unFollowBean.setUserID2(channelHostId);
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(unFollowBean));
        UnfollowApi unFollowApi = new UnfollowApi();
        UnfollowService unFollowService = unFollowApi.getService();
        Call<isTrueBean> call_unfollow = unFollowService.getState(requestBody);
        call_unfollow.enqueue(new Callback<isTrueBean>() {
            @Override
            public void onResponse(Call<isTrueBean> call, Response<isTrueBean> response) {
                int result = response.body().getSign();
                if (result == 1) {
                    Toast.makeText(getApplicationContext(), "取消关注成功！", Toast.LENGTH_SHORT).show();
                    isFocused = 0;
                    buttonMark.setBackgroundResource(R.drawable.mark);
                } else {
                    Toast.makeText(getApplicationContext(), "取消关注失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<isTrueBean> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "取消关注失败！", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void talkRequest(CompactID session) {
        if (session != null && sessionApi != null)
            Log.d(TAG, "talkRequest: ");
        sessionApi.talkRequest(selfId, session.getType(), session.getId());
    }

    private void talkRelease(CompactID session) {
        if (session != null && sessionApi != null)
            Log.d(TAG, "talkrelease: ");
        sessionApi.talkRelease(selfId, session.getType(), session.getId());
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

    private class Report implements ReportDialog.ReportResult {

        Context context;

        public Report(Context context, View v) {
            this.context = context;
            ReportDialog reportDialog = new ReportDialog(context);
            reportDialog.show(v);
            reportDialog.getReportResultListener(this);
        }

        @Override
        public void getResult(int result) {
            if (result != 7) {
                Date now = new Date();
                long nowtime = now.getTime();
                ReportApi reportApi = new ReportApi();
                ReportService reportService = reportApi.getService();
                ReportBean reportBean = new ReportBean();
                reportBean.setChannelID(channelId);
                reportBean.setEvent(result);
                reportBean.setTime(nowtime);
                reportBean.setUserID(channelHostId);
                Gson gson = new Gson();
                final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(reportBean));
                Call<isTrueBean> call_beginBroadcast = reportService.getState(requestBody);
                call_beginBroadcast.enqueue(new Callback<isTrueBean>() {
                    @Override
                    public void onResponse(Call<isTrueBean> call, Response<isTrueBean> response) {
                        if (response.body() != null) {
                            int result = response.body().getSign();
                            if (result == 1) {
                                Log.i(TAG, "举报成功");
                            } else {
                                Log.i(TAG, "举报失败");
                            }
                        } else {
                            Log.i(TAG, "举报返回空");
                        }
                    }

                    @Override
                    public void onFailure(Call<isTrueBean> call, Throwable t) {
                        Log.i(TAG, "举报返回失败");
                    }
                });
            }
        }
    }

}
