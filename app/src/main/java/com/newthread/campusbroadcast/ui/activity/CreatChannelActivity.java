package com.newthread.campusbroadcast.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.algebra.sdk.API;
import com.algebra.sdk.ChannelApi;
import com.google.gson.Gson;
import com.newthread.campusbroadcast.R;
import com.newthread.campusbroadcast.User.Channel;
import com.newthread.campusbroadcast.User.User;
import com.newthread.campusbroadcast.WebService.CreateChannelService;
import com.newthread.campusbroadcast.WebService.UploadChannelImgService;
import com.newthread.campusbroadcast.bean.ChannelBean.GChannelResult;
import com.newthread.campusbroadcast.bean.ChannelBean.SCreateChannelBean;
import com.newthread.campusbroadcast.ui.listener.ChannelListener;
import com.newthread.campusbroadcast.webApi.CreateChannelApi;
import com.newthread.campusbroadcast.webApi.UploadChannelImgApi;

import java.io.File;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 张云帆 on 2017/7/29.
 */

public class CreatChannelActivity extends BaseActivity {
    //播出日期
    private CheckBox dayOne;
    private CheckBox dayTwo;
    private CheckBox dayThree;
    private CheckBox dayFour;
    private CheckBox dayFive;
    private CheckBox daySix;
    private CheckBox daySeven;

    //三个功能键
    private ImageView back;
    private Button submit;
    private ImageView channelImage;

    //电台信息部分
    private EditText channelName;
    private EditText channelIllustration;
    private EditText channelOpenHour;
    private EditText channelOpenMinute;
    private EditText channelCloseHour;
    private EditText channelCloseMinute;

    //创建电台需要参数
    private String mchannelName = "";
    private String mchannelIllustration = "";
    private int uid = 0;
    private int cid = 0;
    private int ctype = 0;

    //判断开播日期需要参数
    private int channelOpenData;
    private int one;
    private int two;
    private int three;
    private int four;
    private int five;
    private int six;
    private int seven;

    //判断播放时间和关闭时间需要参数
    private long openTime = 0;
    private long openHour = 0;
    private long openMinute = 0;
    private long closeTime = 0;
    private long closeHour = 0;
    private long closeMinute = 0;

    //途聆的参数
    ChannelApi channelApi = null;//电台Api
    ChannelListener channelListener = null;//电台监听回调

    //从本地获取图片参数
    private int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 55;
    private String channelImagPath;

    //上传结果参数
    private boolean isUpImage;
    private boolean isUpInfor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_channel);
        //绑定控件
        bindControl();
        handler.post(channel);
        isUpImage = false;
        isUpInfor = false;
        channelOpenData = 0;


        //功能键监听
        functionOnClickListener();


    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // super.handleMessage(msg);
        }
    };
    private Runnable channel = new Runnable() {
        @Override
        public void run() {
            channelApi = API.getChannelApi();
            if (channelApi != null) {
                channelListener = new ChannelListener(CreatChannelActivity.this);
                channelApi.setOnChannelListener(channelListener);
                Log.d("cc", "获取channelApi: ");
            } else {
                handler.postDelayed(channel, 3000);
                Log.d("cc", "未获取channelApi: ");
            }
        }
    };


    //从控件设置时间
    private void setTimeFromControl() {
        if (!channelOpenHour.getText().toString().equals("") && !channelOpenMinute.getText().toString().equals("") && !channelCloseHour.getText().toString().equals("") && !channelCloseMinute.getText().toString().equals("")) {
            openHour = Integer.parseInt(String.valueOf(channelOpenHour.getText()));
            openMinute = Integer.parseInt(String.valueOf(channelOpenMinute.getText()));
            closeHour = Integer.parseInt(String.valueOf(channelCloseHour.getText()));
            closeMinute = Integer.parseInt(String.valueOf(channelCloseMinute.getText()));
            openTime = getTime(openHour, openMinute);
            closeTime = getTime(closeHour, closeMinute);
        }
    }

    //绑定控件
    private void bindControl() {
        //播放日期绑定
        dayOne = (CheckBox) findViewById(R.id.day_one_two);
        dayTwo = (CheckBox) findViewById(R.id.day_two_two);
        dayThree = (CheckBox) findViewById(R.id.day_three_two);
        dayFour = (CheckBox) findViewById(R.id.day_four_two);
        dayFive = (CheckBox) findViewById(R.id.day_five_two);
        daySix = (CheckBox) findViewById(R.id.day_six_two);
        daySeven = (CheckBox) findViewById(R.id.day_seven_two);
        //功能键绑定
        back = (ImageView) findViewById(R.id.channel_information_back_two);
        submit = (Button) findViewById(R.id.channel_information_save_two);
        //电台信息绑定
        channelName = (EditText) findViewById(R.id.channel_information_channel_name_two);
        channelIllustration = (EditText) findViewById(R.id.channel_information_channel_illustration_two);
        channelOpenHour = (EditText) findViewById(R.id.open_hour_two);
        channelOpenMinute = (EditText) findViewById(R.id.open_minute_two);
        channelCloseHour = (EditText) findViewById(R.id.close_hour_two);
        channelCloseMinute = (EditText) findViewById(R.id.close_minute_two);
        //电台图片绑定
        channelImage = (ImageView) findViewById(R.id.channel_Image_two);
    }

    //事件监听
    private void functionOnClickListener() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置电台名字
                mchannelName = String.valueOf(channelName.getText());
                //获取频道简介
                mchannelIllustration = String.valueOf(channelIllustration.getText());
                //设置电台播出日期
                judgeChannelOpenDate();
                //设置电台播出时段
                setTimeFromControl();
                //|| channelImagPath.isEmpty())
                if (mchannelName.isEmpty() || mchannelIllustration.length() > 100||channelImagPath.isEmpty()) {
                    if (mchannelName.isEmpty()) {
                        setDialog("电台名不能为空");
                    } else if (String.valueOf(channelIllustration).length() > 100) {
                        setDialog("电台介绍不能超过一百字");
                    }else {
                        setDialog("电台没有设置图片");
                    }
                } else {
                    //在途聆创建电台
                    if (User.getInstance().getUserID()>0)
                    {
                        uid =User.getInstance().getUserID(); //5326
                    }
                    Log.d("jjjj", "Crea.onClick: "+uid+""+mchannelName);
                    channelApi.createPublicChannel(uid, mchannelName, "androids");
                }


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        channelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(BGAPhotoPickerActivity.newIntent(CreatChannelActivity.this, null, 1, null, false), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
            }
        });
    }

    //判断频道播出日期
    private void judgeChannelOpenDate() {
        if (dayOne.isChecked()) {
            one = 1;
        } else {
            one = 0;
        }
        if (dayTwo.isChecked()) {
            two = 10;
        } else {
            two = 0;
        }
        if (dayThree.isChecked()) {
            three = 100;
        } else {
            three = 0;
        }
        if (dayFour.isChecked()) {
            four = 1000;
        } else {
            four = 0;
        }
        if (dayFive.isChecked()) {
            five = 10000;
        } else {
            five = 0;
        }
        if (daySix.isChecked()) {
            six = 100000;
        } else {
            six = 0;
        }
        if (daySeven.isChecked()) {
            seven = 1000000;
        } else {
            seven = 0;
        }
        channelOpenData = one + two + three + four + five + six + seven;
    }

    //将日期转换成毫秒数
    private Long getTime(long hour, long minute) {
        long a = hour * 60 * 60 * 1000;
        long b = minute * 60 * 1000;
        return a + b;
    }


    //添加图片回掉,判断赋值图片地址
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
            channelImagPath = picturePath;
            Bitmap bitMap = BitmapFactory.decodeFile(channelImagPath);
            channelImage.setImageBitmap(bitMap);
            Log.d("AAA", "onActivityResult: " + channelImagPath);

        }
    }

    private void upImage(String path, int channelID) {
        File file = new File(path);
        final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imgFile", file.getName(), requestFile);
        UploadChannelImgApi uploadChannelImgApi = new UploadChannelImgApi();
        UploadChannelImgService uploadChannelImgService = uploadChannelImgApi.getService();
        Call<GChannelResult> channelResultCall = uploadChannelImgService.UploadChannelImg(channelID, body);
        channelResultCall.enqueue(new Callback<GChannelResult>() {
            @Override
            public void onResponse(Call<GChannelResult> call, Response<GChannelResult> response) {
                if (String.valueOf(response.body().getSign()).equals("0"))
                {
                    setDialog("图片上传失败");
                }
            }

            @Override
            public void onFailure(Call<GChannelResult> call, Throwable t) {

            }
        });
    }

    public void upInfor(int channelId, int channelType, boolean tlIsOK) {
        if (tlIsOK)
        {
            cid = channelId;
            ctype = channelType;
            //创建电台信息请求
            CreateChannelApi createChannelApi = new CreateChannelApi();
            CreateChannelService createChannelService = createChannelApi.getService();
            SCreateChannelBean sCreateChannelBean = new SCreateChannelBean();
            sCreateChannelBean.setUserID(uid);//5326
            sCreateChannelBean.setChannelID(cid);//1199
            sCreateChannelBean.setChannelName(mchannelName);
            sCreateChannelBean.setContent(mchannelIllustration);
            sCreateChannelBean.setStartTime(openTime);
            sCreateChannelBean.setEndTime(closeTime);
            sCreateChannelBean.setWeekDay(channelOpenData);
            sCreateChannelBean.setChannelType(ctype);
            Gson gson = new Gson();
            String bean = gson.toJson(sCreateChannelBean);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bean);
            Call<GChannelResult> channelResultCall = createChannelService.getCreatResult(requestBody);
            channelResultCall.enqueue(new Callback<GChannelResult>() {
                @Override
                public void onResponse(Call<GChannelResult> call, Response<GChannelResult> response) {
                    Log.d("cccc", "创建频道返回值" + response.body().getSign());
                    if (String.valueOf(response.body().getSign()).equals("1")) {
                        setDialog("创建电台成功");
                        saveChannel(cid) ;
                    } else {
                        setDialog("电台创建失败");
                    }
                }
                @Override
                public void onFailure(Call<GChannelResult> call, Throwable t) {
                    Log.d("cccc", "失败" + t);
                    setDialog("电台创建失败");
                }
            });
            if (channelImagPath.isEmpty()) {
//
            } else {
                upImage(channelImagPath, cid);
            }
        }else {
            setDialog("电台创建失败");
        }


    }

    private void setDialog(String point) {
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(CreatChannelActivity.this);
        normalDialog.setMessage(point);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int wch) {
                    }
                });
        // 显示
        normalDialog.show();
    }
    private void saveChannel(int channelId) {
        Channel.getInstance().setChannelID(channelId);
        Channel.getInstance().setChannelName(mchannelName);
        Channel.getInstance().setContent(mchannelIllustration);
        Channel.getInstance().setWeekday(channelOpenData);
        Channel.getInstance().setStartTime(openTime);
        Channel.getInstance().setEndTime(closeTime);
        Channel.getInstance().setFans(0);
        Channel.getInstance().setCreditValue(100);
        Channel.getInstance().setChannelImg(channelImagPath);
    }
}
