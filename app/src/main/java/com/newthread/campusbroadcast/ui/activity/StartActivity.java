package com.newthread.campusbroadcast.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.algebra.sdk.API;
import com.algebra.sdk.AccountApi;
import com.algebra.sdk.DeviceApi;
import com.algebra.sdk.entity.Constant;
import com.algebra.sdk.entity.Contact;
import com.google.gson.Gson;
import com.newthread.campusbroadcast.MainActivity;
import com.newthread.campusbroadcast.R;
import com.newthread.campusbroadcast.User.Channel;
import com.newthread.campusbroadcast.User.CurrentChannelBean;
import com.newthread.campusbroadcast.User.LoginBean;
import com.newthread.campusbroadcast.User.LoginGsonBean;
import com.newthread.campusbroadcast.User.User;
import com.newthread.campusbroadcast.User.UserIdBean;
import com.newthread.campusbroadcast.WebService.CurrentChannelService;
import com.newthread.campusbroadcast.WebService.LogInService;
import com.newthread.campusbroadcast.ui.listener.AccountListener;
import com.newthread.campusbroadcast.webApi.CurrentChannelApi;
import com.newthread.campusbroadcast.webApi.LogInApi;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 倪启航 on 2017/8/3.
 */

public class StartActivity extends BaseActivity {

    public static StartActivity act=null;
    public boolean newBind = true;
    private AccountApi accountApi = null;
    private DeviceApi deviceApi = null;
    public AccountListener accountListener;
    public User user = User.getInstance();
    private SharedPreferences sharedPreferences;
    private String userAccount = "";
    private String userPass = "";
    public boolean userBoundPhone = false;
    private boolean isLogin;
    public String userNick = "???";
    public boolean isVisitor = true;
    //    用户id
    public int selfId = 0;
    public int uState;
    public String userPhone;
    public static String TAG = "mmmmmmmmmmmmm";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        act=this;
        sharedPreferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
        isLogin=sharedPreferences.getBoolean("isLogin", false);
        if (isLogin) {
            newBind = API.init(this);
            userAccount=sharedPreferences.getString("userAccount","");
            userPass=sharedPreferences.getString("userPass","");
            if(userPass!=""&&userAccount!="")
            CheckLogIn(userAccount,userPass);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        User user=User.getInstance();
//        selfId=user.getUserID();
        if(isLogin){
            accountApi.logout(selfId);
            API.leave();
            Log.i(TAG, "StartActivity___onDestroy ..");
        }
    }

    private void CheckLogIn(String userTL, String pass) {
        LogInApi loginApi = new LogInApi();
        LogInService logInService = loginApi.getService();
        LoginBean loginBean = new LoginBean();
        loginBean.setUserTL(userTL);
        loginBean.setPassword(pass);
        Gson gson = new Gson();
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(loginBean));
        Call<LoginGsonBean> call = logInService.getState(requestBody);
        call.enqueue(new Callback<LoginGsonBean>() {
            @Override
            public void onResponse(Call<LoginGsonBean> call, Response<LoginGsonBean> response) {

                if (response.body() != null) {
                    LoginGsonBean bean = response.body();
                    int sign = bean.getSign();
                    if (sign == 2) {
                        delayInitApi.run();
                        int id = bean.getUserID();
                        String college = bean.getCollege();
                        String gender = bean.getGender();
                        int age = bean.getAge();
                        String nick = bean.getNickname();
                        int rank = bean.getRank();
                        String imgurl = bean.getImgURL();

                        //   Log.d(TAG,imgurl);
                        int credit = bean.getCreditValue();
                        user.setUserID(id);
                        user.setCollege(college);
                        user.setUsergender(gender);
                        user.setAge(age);
                        user.setRank(rank);
                        user.setImgUrl(imgurl);
                        user.setCreditValue(credit);
                        user.setUseraccount(userAccount);
                        int mrank = user.getRank();
                        int ID = user.getUserID();
                        if (mrank == 1) {
                            GetCurrentChannel(ID);
                        }
                    }
                } else Log.d(TAG, "onResponse: " + "Wrong");
            }


            @Override
            public void onFailure(Call<LoginGsonBean> call, Throwable t) {
                Toast.makeText(StartActivity.this, "连接失败 " + " " + t, Toast.LENGTH_SHORT).show();
                System.out.print("throwable:" + t);
            }
        });
    }

    private Runnable delayInitApi = new Runnable() {
        @Override
        public void run() {
            accountApi = API.getAccountApi();
            Log.d(TAG, "accountapi=:" + accountApi);
            deviceApi = API.getDeviceApi();
            if (accountApi != null && deviceApi != null) {
                accountListener = new AccountListener(StartActivity.this);
                accountApi.setOnAccountListener(accountListener);
                user.setAccountApi(accountApi);
                Contact me = accountApi.whoAmI();

                if (me != null) {
                    userBoundPhone = !me.phone.equals(null);
                    isVisitor = me.visitor;
                    userNick = new String(me.name);
                    selfId = me.id;
                    uState = me.state;
                    userPhone = me.phone;
                    user.setAccountListener(accountListener);
                    user.setState(uState);
                    user.setUserID(selfId);
                    user.setUsername(userNick);
                    user.toString();
                    System.out.println("_--------selfId=----" + selfId + "-----selfState=" + uState);
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d(TAG, "me is null");
                    accountApi.login(userAccount, API.md5(userPass));


                }
            } else {
                Log.d(TAG, "start SDK and waiting another 300ms.");
                handler.postDelayed(delayInitApi, 300);
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int result = msg.arg1;
            if (result == Constant.ACCOUNT_RESULT_OK
                    || result == Constant.ACCOUNT_RESULT_ALREADY_LOGIN) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    private void GetCurrentChannel(int id ){
        CurrentChannelApi currentChannelApi=new CurrentChannelApi();
        CurrentChannelService currentChannelService=currentChannelApi.getService();
        UserIdBean userIdBean=new UserIdBean();
        userIdBean.setUserID(id);
        Gson gson=new Gson();
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(userIdBean));
        Call<CurrentChannelBean> call=currentChannelService.getState(requestBody);
        call.enqueue(new Callback<CurrentChannelBean>() {
            @Override
            public void onResponse(Call<CurrentChannelBean> call, Response<CurrentChannelBean> response) {
                CurrentChannelBean currentChannelBean=response.body();
                int channelID=currentChannelBean.getChannelID();
                String channelName=currentChannelBean.getChannelName();
                String content =currentChannelBean.getContent();
                int weekday=currentChannelBean.getWeekday();
                long startTime=currentChannelBean.getStartTime();
                long endTime=currentChannelBean.getEndTime();
                int creditValue=currentChannelBean.getCreditValue();
                int fans=currentChannelBean.getFans();
                String channelImg=currentChannelBean.getChannelImg();
                Log.d(TAG,"channelName"+channelName);
                Log.d(TAG,"content"+content);
                Channel channel=Channel.getInstance();
                channel.setChannelID(channelID);
                channel.setChannelName(channelName);
                channel.setContent(content);
                channel.setWeekday(weekday);
                channel.setStartTime(startTime);
                channel.setEndTime(endTime);
                channel.setCreditValue(creditValue);
                channel.setFans(fans);
                channel.setChannelImg(channelImg);

            }

            @Override
            public void onFailure(Call<CurrentChannelBean> call, Throwable t) {

            }
        });

    }


}
