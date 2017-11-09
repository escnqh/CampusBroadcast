package com.newthread.campusbroadcast.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.newthread.campusbroadcast.R;
import com.newthread.campusbroadcast.User.SignupGsonBean;
import com.newthread.campusbroadcast.User.User;
import com.newthread.campusbroadcast.User.UserIdBean;
import com.newthread.campusbroadcast.WebService.ApplyService;
import com.newthread.campusbroadcast.util.PopuWindowTvInfo;
import com.newthread.campusbroadcast.webApi.ApplyApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017/7/24.
 */

public class PlayerInfoActivity extends BaseActivity {
    @BindView(R.id.back1)
    ImageView back1;
    @BindView(R.id.name1)
    EditText name;
    @BindView(R.id.number_campus)
    EditText numberCampus;
    @BindView(R.id.pa)
    EditText pa;
    @BindView(R.id.reason)
    EditText reason;
    @BindView(R.id.submit)
    Button submit1;
    @BindView(R.id.accept)
    CheckBox accept;
    User user=User.getInstance();
    public int rank;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playerinfo);
        ButterKnife.bind(PlayerInfoActivity.this);

        back1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
         }
        });
        submit1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //
            final String name1=name.getText().toString();
            final String number=numberCampus.getText().toString();
            final String pa1=pa.getText().toString();
            final String reason1=reason.getText().toString();

            if (name1==null||name1.length()>5){
                PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(PlayerInfoActivity.this);
                popuWindowTvInfo.ChangepopuInfo("姓名输入有误");
            }
            else if (number==null||number.length()>15||number.length()<4){
                PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(PlayerInfoActivity.this);
                popuWindowTvInfo.ChangepopuInfo("学号输入有误");
            }
            else if (pa1.length()>20||pa1==null){
                PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(PlayerInfoActivity.this);
                popuWindowTvInfo.ChangepopuInfo("密码输入有误");
            }
            else if (reason1==null){
                PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(PlayerInfoActivity.this);
                popuWindowTvInfo.ChangepopuInfo("请填写申请原因");
            }
            else {
                if (accept.isChecked()) {
                    int id= user.getUserID();
                    ApplyChannel(id);

                }
            }
        }
    });


    }

    public  void ApplyChannel(int ID){
        ApplyApi applyApi=new ApplyApi();
        ApplyService applyService=applyApi.getService();
        UserIdBean userIdBean=new UserIdBean();
        userIdBean.setUserID(ID);
        Gson gson = new Gson();
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(userIdBean));
        Call<SignupGsonBean> call=applyService.getState(requestBody);
        call.enqueue(new Callback<SignupGsonBean>() {
            @Override
            public void onResponse(Call<SignupGsonBean> call, Response<SignupGsonBean> response) {
                SignupGsonBean signupGsonBean=response.body();
                int sign= signupGsonBean.getSign();
                Log.d("aaa","返回"+sign);
                if (sign==1){
                    user.setRank(1);
                    if (user.getRank()==1) {
                       PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(PlayerInfoActivity.this);
                       popuWindowTvInfo.ChangepopuInfo("恭喜您，您已成为主播，快去创建电台吧！");
                        finish();
                    }
                }else{PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(PlayerInfoActivity.this);
                    popuWindowTvInfo.ChangepopuInfo("申请失败");}
            }

            @Override
            public void onFailure(Call<SignupGsonBean> call, Throwable t) {

            }
        });
    }
}
