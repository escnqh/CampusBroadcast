package com.newthread.campusbroadcast.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.algebra.sdk.API;
import com.algebra.sdk.AccountApi;
import com.google.gson.Gson;
import com.newthread.campusbroadcast.MainActivity;
import com.newthread.campusbroadcast.R;
import com.newthread.campusbroadcast.User.ChangePassBean;
import com.newthread.campusbroadcast.User.SignupGsonBean;
import com.newthread.campusbroadcast.User.User;
import com.newthread.campusbroadcast.WebService.ChangePassService;
import com.newthread.campusbroadcast.ui.activity.FillInfoActivity;
import com.newthread.campusbroadcast.ui.activity.SignupActivity;
import com.newthread.campusbroadcast.webApi.ChangePassApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP on 2017/7/25.
 */

public class DialogChangePassw extends Dialog {
    @BindView(R.id.prePassword)
    EditText prePassword;
    @BindView(R.id.newPassword)
    EditText newPassword;
    @BindView(R.id.confirmPassword)
    EditText confirmPassword;
    @BindView(R.id.dialog_button_cancel)
    Button dialogButtonCancel;
    @BindView(R.id.dialog_button_ok)
    Button dialogButtonOk;
    Context context;
    User user=User.getInstance();

    private int selfId=user.getUserID();
    private String userAccount;
    private AccountApi accountApi;
    public String oldpw;
    public String pw1;
    public boolean successful;

    public DialogChangePassw(Context context) {
        super(context);
        this.context=context;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_changepassw);
        ButterKnife.bind(DialogChangePassw.this);
        Window window =getWindow();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        WindowManager.LayoutParams layoutParams=window.getAttributes();
        layoutParams.width=(int) (displayMetrics.widthPixels*0.9);
        layoutParams.height = (int) (displayMetrics.heightPixels * 0.6);
        window.setAttributes(layoutParams);


        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
         @Override
            public void onClick(View v) {
                dismiss();
        }
        });
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpw=prePassword.getText().toString();
                pw1=newPassword.getText().toString();
                String pw2=confirmPassword.getText().toString();
                if (pw1!=null&&pw1.equals(pw2)&&pw1.length()>=4) {

                    oldpw = API.md5(oldpw);
                    userAccount = user.getUseraccount();
                    accountApi=user.getAccountApi();
                    accountApi.setPassWord(selfId, userAccount, oldpw,API.md5(pw1));
                    successful=user.isSuccessful();
                    Log.d("aaa","结果"+successful);
                    if (!successful)
                    {
                        ChangePass(selfId,pw1);}
                    else {
                        PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(context);
                        popuWindowTvInfo.ChangepopuInfo("密码修改失败！");
                        dismiss();}

                }

            }
        });

    }
    public  void ChangePass(int ID,String pass){
        ChangePassApi changePassApi=new ChangePassApi();
        ChangePassService changePassService=changePassApi.getService();
        ChangePassBean changePassBean=new ChangePassBean();
        changePassBean.setUserID(ID);
        changePassBean.setPassword(pass);
        Gson gson=new Gson();
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(changePassBean));
        Call<SignupGsonBean> call=changePassService.getState(requestBody);
        call.enqueue(new Callback<SignupGsonBean>() {
            @Override
            public void onResponse(Call<SignupGsonBean> call, Response<SignupGsonBean> response) {
                SignupGsonBean signupGsonBean=response.body();
                int sign = signupGsonBean.getSign();
                Log.d("aaa","sign"+sign);
                if (sign==1){
                    PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(context);
                    popuWindowTvInfo.ChangepopuInfo("密码修改成功");
                    dismiss();

                }else{PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(context);
                    popuWindowTvInfo.ChangepopuInfo("密码修改失败！");}

        }

            @Override
            public void onFailure(Call<SignupGsonBean> call, Throwable t) {

            }
        });




    }


}
