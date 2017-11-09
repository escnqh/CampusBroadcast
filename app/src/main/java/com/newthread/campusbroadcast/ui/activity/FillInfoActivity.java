package com.newthread.campusbroadcast.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.algebra.sdk.AccountApi;
import com.google.gson.Gson;
import com.newthread.campusbroadcast.R;
import com.newthread.campusbroadcast.User.SignupGsonBean;
import com.newthread.campusbroadcast.User.User;
import com.newthread.campusbroadcast.User.UserInfoGsonBean;
import com.newthread.campusbroadcast.WebService.PersonInfoService;
import com.newthread.campusbroadcast.util.PopuWindowTvInfo;
import com.newthread.campusbroadcast.webApi.PersonInfoApi;

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

public class FillInfoActivity extends BaseActivity {
    @BindView(R.id.back1)
    ImageView back1;
    @BindView(R.id.sure)
    ImageView sure;
    @BindView(R.id.save)
    Button save2;
    @BindView(R.id.R_sex_boy)
    RadioButton RSexBoy;
    @BindView(R.id.R_sex_girl)
    RadioButton RSexGirl;
    @BindView(R.id.tour)
    EditText tour;
    @BindView(R.id.age)
    EditText age;
    @BindView(R.id.school)
    EditText school;
    @BindView(R.id.identity)
    EditText identity;
    @BindView(R.id.nick)
    EditText nick;
    User user = User.getInstance();
    public int selfId=user.getUserID();
    public String userAccount=user.getUseraccount();
    public int rank=user.getRank();
    public String nick2=user.getUsername();
    public String gender=user.getUsergender();
    public String college=user.getCollege();
    public AccountApi accountApi= user.getAccountApi();
    public int age1=user.getAge();
    public int position;
    public int isnick=0;
    private boolean judgeSure=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillinfo);
        ButterKnife.bind(FillInfoActivity.this);
        judgeSure=false;
        tour.setFocusable(false);
        tour.setEnabled(false);
        nick.setFocusable(false);
        nick.setEnabled(false);
        age.setFocusable(false);
        age.setEnabled(false);
        school.setFocusable(false);
        school.setEnabled(false);
        identity.setFocusable(false);
        identity.setEnabled(false);
        RSexBoy.setEnabled(false);
        RSexGirl.setEnabled(false);
        tour.setText(userAccount);
        nick.setText(nick2);
        if (gender.equals("男")){
            RSexBoy.setChecked(true);
        }
        else if (gender.equals("女")){
            RSexGirl.setChecked(true);
        }
        else{
            RSexGirl.setChecked(false);
            RSexBoy.setChecked(false);
        }
        if (rank==1){
            identity.setText("主播");}
        else{
            identity.setText("用户");}

        if (age1!=0)
        {age.setText(String.valueOf(age1));}
        school.setText(college);


        //返回键
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //编辑键
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!judgeSure)
                {
                    nick.setEnabled(true);
                    nick.setFocusableInTouchMode(true);
                    nick.setSelection(position);
                    age.setFocusable(true);
                    age.setEnabled(true);
                    age.setFocusableInTouchMode(true);
                    school.setFocusable(true);
                    school.setEnabled(true);
                    school.setFocusableInTouchMode(true);
                    RSexBoy.setEnabled(true);
                    RSexGirl.setEnabled(true);
                    judgeSure=true;
                }else {
                    nick.setEnabled(false);
                    nick.setFocusableInTouchMode(false);
//                    nick.setSelection(position);
                    age.setFocusable(false);
                    age.setEnabled(false);
                    age.setFocusableInTouchMode(false);
                    school.setFocusable(false);
                    school.setEnabled(false);
                    school.setFocusableInTouchMode(false);
                    RSexBoy.setEnabled(false);
                    RSexGirl.setEnabled(false);
                    judgeSure=false;

                }



            }
        });

        RSexBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RSexGirl.isChecked()) {
                    RSexBoy.setChecked(true);
                    RSexGirl.setChecked(false);
                }
            }
        });
        RSexGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RSexBoy.isChecked()) {
                    RSexGirl.setChecked(true);
                    RSexBoy.setChecked(false);
                }
            }
        });
        save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sure.isClickable()){
                    String nikname=nick.getText().toString();
                    String user_sex;
                    String year=age.getText().toString();
                    int i=0;
                    if(!year.isEmpty())
                    {
                        i=Integer.valueOf(year).intValue();
                    }
                    String college=school.getText().toString();
                    user_sex=GetSex();
                    if (nikname.length()<=2||nikname.length()>20||nikname==null){
                        PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(FillInfoActivity.this);
                        popuWindowTvInfo.ChangepopuInfo("昵称修改不合法，请修改后提交");
                    }else if (i<=0||i>100||year==null){
                        PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(FillInfoActivity.this);
                        popuWindowTvInfo.ChangepopuInfo("请填写正确年龄");
                    }
                    else if (college==null||college.length()>8){
                        PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(FillInfoActivity.this);
                        popuWindowTvInfo.ChangepopuInfo("请填写正确的学校名");
                    }
                    else if (!RSexBoy.isChecked()&&!RSexGirl.isChecked()){
                        PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(FillInfoActivity.this);
                        popuWindowTvInfo.ChangepopuInfo("请填写性别");
                    }
                    else{
                        accountApi.setNickName(selfId,nikname);
                        isnick=user.getIsNick();
                        if (isnick==1)
                        {
                            ChangePersonInfo(selfId,nikname,user_sex,college,i);
                        }

                    }
                }
                else{
                    PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(FillInfoActivity.this);
                    popuWindowTvInfo.ChangepopuInfo("您未修改个人信息，请点击修改");
                }

            }
        });

    }




    //性别获取
    private String GetSex(){
        String sex = "";
        if (RSexBoy.isChecked()){
            sex = RSexBoy.getText().toString();
        }else {
            sex = RSexGirl.getText().toString();
        }
        return sex;
    }

private void ChangePersonInfo(int id, final String nick, String gender, String college, int age) {
        PersonInfoApi personInfoApi = new PersonInfoApi();
        PersonInfoService personInfoService = personInfoApi.getService();
        UserInfoGsonBean userInfoGsonBean = new UserInfoGsonBean();
        userInfoGsonBean.setUserID(id);
        userInfoGsonBean.setNickname(nick);
        userInfoGsonBean.setGender(gender);
        userInfoGsonBean.setCollege(college);
        userInfoGsonBean.setAge(age);
        Gson gson = new Gson();
    final RequestBody requestBody=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),gson.toJson(userInfoGsonBean));
    Call<SignupGsonBean> call=personInfoService.getState(requestBody);
    call.enqueue(new Callback<SignupGsonBean>() {
        @Override
        public void onResponse(Call<SignupGsonBean> call, Response<SignupGsonBean> response) {
            SignupGsonBean bean1=response.body();
            int m=bean1.getSign();
            Log.d("mmm","m="+m);
            if (m==1){

                PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(FillInfoActivity.this);
                popuWindowTvInfo.ChangepopuInfo("信息修改成功");
            }else {Toast.makeText(FillInfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();}
        }

        @Override
        public void onFailure(Call<SignupGsonBean> call, Throwable t) {
            Toast.makeText(FillInfoActivity.this, "连接失败 " + " " + t, Toast.LENGTH_SHORT).show();
            System.out.print("throwable:" + t);
        }
    });



}


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
