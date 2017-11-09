package com.newthread.campusbroadcast.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.algebra.sdk.API;
import com.algebra.sdk.AccountApi;
import com.algebra.sdk.DeviceApi;
import com.algebra.sdk.entity.Contact;
import com.algebra.sdk.entity.Utils;
import com.google.gson.Gson;
import com.newthread.campusbroadcast.MainActivity;
import com.newthread.campusbroadcast.R;
import com.newthread.campusbroadcast.User.SignupBean;
import com.newthread.campusbroadcast.User.SignupGsonBean;
import com.newthread.campusbroadcast.User.User;
import com.newthread.campusbroadcast.WebService.SignUpService;
import com.newthread.campusbroadcast.ui.listener.AccountListener;
import com.newthread.campusbroadcast.util.PopuWindowTvInfo;
import com.newthread.campusbroadcast.webApi.SignUpApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017/7/20.
 */

public class SignupActivity extends BaseActivity {
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.nick_name)
    EditText nick;
    @BindView(R.id.passw)
    EditText passw;
    @BindView(R.id.passw_confirm)
    EditText pass_confir;
    @BindView(R.id.registerButton)
    Button btuRegister;

    private DeviceApi deviceApi = null;
    private AccountListener accountListener;
    AccountApi accountApi = null;

    public String invCode=null;
    public String nickname;
    public String pw;
    public String rePw;
    public int selfId = 0;
    public int uState;
    public String userNick;
    public String TAG="bbbbbbb";


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        API.init(this);


        btuRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invCode = code.getText().toString();
                nickname = nick.getText().toString();
                pw = passw.getText().toString();
                rePw = pass_confir.getText().toString();
                //注册信息处理
                if (invCode == null || invCode.length() < 8) {
                    PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(SignupActivity.this);
                    popuWindowTvInfo.ChangepopuInfo("激活码错误，请更改后注册");

                }
                if (!pw.equals(rePw) || "".equals(pw)
                        || pw.length() < 4) {
                    PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(SignupActivity.this);
                    popuWindowTvInfo.ChangepopuInfo("两次密码输入不一致，请更改后注册");
                }
                if (nick == null || nick.length() == 0) {
                    PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(SignupActivity.this);
                    popuWindowTvInfo.ChangepopuInfo("请输入昵称");
                } else if (Utils.getCharacterNum(String.valueOf(nick)) < AccountApi.MINNICKLEN) {
                    PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(SignupActivity.this);
                    popuWindowTvInfo.ChangepopuInfo("昵称不合法");
                } else {
                    handler.postDelayed(delayInitApi,300);
                    User user=User.getInstance();
                    String account=user.getRegisterAccount();
                    PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(SignupActivity.this);
                    popuWindowTvInfo.ChangepopuInfo("您的账号为："+account+"请牢记！");
                    accountApi.login(account, API.md5(pw));
                    Contact me = accountApi.whoAmI();
                    if (me != null&&account!=null) {

                        userNick = new String(me.name);
                        selfId = me.id;
                        uState = me.state;
                        user.setAccountApi(accountApi);
                        user.setState(uState);
                        user.setUserID(selfId);
                        user.toString();
                        System.out.println("_--------selfId=----" + selfId + "-----selfState="+uState);
                        CheckSignUp(selfId,account,pw,userNick);

                    }

                }
            }
        });
    }

    private Runnable delayInitApi = new Runnable() {
        @Override
        public void run() {
            accountApi = API.getAccountApi();
            deviceApi = API.getDeviceApi();
            if (accountApi != null && deviceApi != null) {
                accountApi.createUser(invCode, nickname, pw);
                accountListener = new AccountListener(SignupActivity.this);
                accountApi.setOnAccountListener(accountListener);

            } else {
                Log.d("aaaa", "start SDK and waiting another 300ms.");
                handler.postDelayed(delayInitApi,300);
            }
        }
    };
    //注册信息传入后台
    private void CheckSignUp(int id,String TL,String pw,String nick){
        SignUpApi signUpApi=new SignUpApi();
        SignUpService signUpService=signUpApi.getService();
        SignupBean signupBean=new SignupBean();
        signupBean.setUserId(id);
        signupBean.setUserTL(TL);
        signupBean.setPassword(pw);
        signupBean.setNickname(nick);
        Gson gson=new Gson();
        final RequestBody requestBody=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),gson.toJson(signupBean));
        Call<SignupGsonBean> call=signUpService.getState(requestBody);
        call.enqueue(new Callback<SignupGsonBean>() {
            @Override
            public void onResponse(Call<SignupGsonBean> call, Response<SignupGsonBean> response) {

                if (response.body()!=null){
                    SignupGsonBean bean1=response.body();
                    int m=bean1.getSign();
                    if (m==1){
                        Log.d(TAG,"m="+m);
                        Intent intent=new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(intent);
                    }


                }else Log.d(TAG, "onResponse: "+"Wrong");
            }



            @Override
            public void onFailure(Call<SignupGsonBean> call, Throwable t) {
                Toast.makeText(SignupActivity.this,"连接失败 "+" "+t,Toast.LENGTH_SHORT).show();
                System.out.print("throwable:"+t);
            }
        });

    }


}
