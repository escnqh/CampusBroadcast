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
import android.widget.TextView;

import com.algebra.sdk.API;
import com.algebra.sdk.ChannelApi;
import com.google.gson.Gson;
import com.newthread.campusbroadcast.MainActivity;
import com.newthread.campusbroadcast.R;
import com.newthread.campusbroadcast.User.Channel;
import com.newthread.campusbroadcast.User.User;
import com.newthread.campusbroadcast.WebService.ChangeChannelService;
import com.newthread.campusbroadcast.WebService.UploadChannelImgService;
import com.newthread.campusbroadcast.bean.ChannelBean.GChannelResult;
import com.newthread.campusbroadcast.bean.ChannelBean.SChangeChannelBean;
import com.newthread.campusbroadcast.ui.listener.ChannelListener;
import com.newthread.campusbroadcast.util.JumpToShareUtil;
import com.newthread.campusbroadcast.webApi.ChangeChannelApi;
import com.newthread.campusbroadcast.webApi.UploadChannelImgApi;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 张云帆 on 2017/7/25.
 */

public class ChannelInformationActivity extends BaseActivity {

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
    private ImageView share;
    private Button submit;
    private ImageView channelImage;

    //电台信息部分
    private TextView credit;
    private TextView beFocousedNum;
    private EditText channelName;
    private EditText channelIllustration;
    private EditText channelOpenHour;
    private EditText channelOpenMinute;
    private EditText channelCloseHour;
    private EditText channelCloseMinute;

    //修改电台需要参数
    private String mcredit;
    private String mbeFocousedNum;
    private String mchannelName;
    private String mchannelIllustration;
    private int uid = 0;
    private int cid = 0;
    private int ctype = 2;


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
    private Channel channelIns;

    //途聆的参数
    ChannelApi channelApi = null;//电台Api
    ChannelListener channelListener = null;//电台监听回调

    //关于图片设置的参数
    private int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 55;
    private String channelImagPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_information);
        channelIns=Channel.getInstance();
        //绑定控件
        bindControl();
        handler.post(channel);
        //从单例里获取当前用户的电台信息(初始化信息)
        initInformation();
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
                channelListener = new ChannelListener(ChannelInformationActivity.this);
                channelApi.setOnChannelListener(channelListener);
                Log.d("ma", "获取channelApi: ");
            } else {
                handler.postDelayed(channel, 3000);
                Log.d("ma", "未获取channelApi: ");
            }
        }
    };

    private void initInformation() {
        if (channelIns.getChannelID() > 0) {

            //获取图像的地址的单例channelImagPath=??
            channelImagPath=channelIns.getChannelImg();
            uid = User.getInstance().getUserID(); //5326
            cid = channelIns.getChannelID();//1199
            mcredit = String.valueOf(channelIns.getCreditValue());
            mbeFocousedNum = String.valueOf(channelIns.getFans());
            mchannelName = channelIns.getChannelName();
            mchannelIllustration = channelIns.getContent();
            channelOpenData = channelIns.getWeekday();
            openTime = channelIns.getStartTime();
            closeTime = channelIns.getEndTime();
            Log.d("closeTime", "initInformation: "+closeTime);
            Log.d("tutu", "initInformation: channelImagPath:"+channelImagPath);
            //加载图片
            File file = new File(channelImagPath);
            if (file.exists()) {
                Picasso.with(ChannelInformationActivity.this)
                        .load(file)
                        .placeholder(R.drawable.a)
                        .into(channelImage);
            } else {
                Picasso.with(ChannelInformationActivity.this)
                        .load(channelImagPath)
                        .placeholder(R.drawable.a)
                        .into(channelImage);
            }
           // Picasso.with(ChannelInformationActivity.this).load(new File(channelImagPath)).placeholder(R.drawable.a).into(channelImage);

            credit.setText(mcredit);
            beFocousedNum.setText(mbeFocousedNum);
            channelName.setText(mchannelName);
            channelIllustration.setText(mchannelIllustration);
            showOpenDay(channelOpenData);
            channelOpenHour.setText(String.valueOf(changeTimeToHour(openTime))+"");
            channelOpenMinute.setText(String.valueOf(changeTimeToMinute(openTime))+"");
            channelCloseHour.setText(String.valueOf(changeTimeToHour(closeTime))+"");
            channelCloseMinute.setText(String.valueOf(changeTimeToMinute(closeTime)+""));
            Log.d("closehoue", "initInformation: "+String.valueOf(changeTimeToHour(closeTime)));
            Log.d("closeminute", "initInformation: "+String.valueOf(changeTimeToMinute(closeTime)));
        }

    }

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

    private void bindControl() {
        //播放日期绑定
        dayOne = (CheckBox) findViewById(R.id.day_one);
        dayTwo = (CheckBox) findViewById(R.id.day_two);
        dayThree = (CheckBox) findViewById(R.id.day_three);
        dayFour = (CheckBox) findViewById(R.id.day_four);
        dayFive = (CheckBox) findViewById(R.id.day_five);
        daySix = (CheckBox) findViewById(R.id.day_six);
        daySeven = (CheckBox) findViewById(R.id.day_seven);
        //功能键绑定
        back = (ImageView) findViewById(R.id.channel_information_back);
        share = (ImageView) findViewById(R.id.channel_information_share);
        submit = (Button) findViewById(R.id.channel_information_save);
        //电台信息绑定
        credit = (TextView) findViewById(R.id.channel_information_credit);
        beFocousedNum = (TextView) findViewById(R.id.channel_information_befocoused_num);
        channelName = (EditText) findViewById(R.id.channel_information_channel_name);
        channelIllustration = (EditText) findViewById(R.id.channel_information_channel_illustration);
        channelOpenHour = (EditText) findViewById(R.id.open_hour);
        channelOpenMinute = (EditText) findViewById(R.id.open_minute);
        channelCloseHour = (EditText) findViewById(R.id.close_hour);
        channelCloseMinute = (EditText) findViewById(R.id.close_minute);
        //电台图片绑定
        channelImage = (ImageView) findViewById(R.id.channel_Image);
    }

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
                if (mchannelName.isEmpty() || mchannelIllustration.length() > 100) {
                    if (mchannelName.isEmpty()) {
                        setDialog("电台名不能为空");
                    } else if (String.valueOf(channelIllustration).length() > 100) {
                        setDialog("电台介绍不能超过一百字");
                    }
                } else {
                    //在途聆修改电台名
                    Log.d("jjjj", "onClick: " + uid + "" + mchannelName);
                    channelApi.changePublicChannelName(uid, 2, cid, mchannelName);
                }


            }


        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChannelInformationActivity.this, MainActivity.class);
//                startActivityForResult(intent,19961);
//                startActivity(intent);
                finish();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpToShareUtil jumpToShare = new JumpToShareUtil(String.valueOf(cid), ChannelInformationActivity.this);
                jumpToShare.startJump();
            }
        });
        channelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(BGAPhotoPickerActivity.newIntent(ChannelInformationActivity.this, null, 1, null, false), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
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

    //将毫秒数转换成小时
    private long changeTimeToHour(long time) {
        long hour = 0;
        hour = time / (1000 * 60 * 60);
        return hour;
    }

    //将毫秒数转换成分钟
    private long changeTimeToMinute(long time) {
        long minute = 0;
        minute =  (time % (1000 * 60 * 60)) / (1000 * 60);//time % (1000 * 60 * 60);
        return minute;
    }


    private void showOpenDay(int time) {
        String ti = String.valueOf(time);
        for (int i = 0; i < ti.length(); i++) {
            Log.d("ttt", "onCreate: " + Integer.parseInt(String.valueOf(ti.charAt(i))));
            if (Integer.parseInt(String.valueOf(ti.charAt(i))) == 1) {
                if (ti.length() - i == 7) {
                    daySeven.setChecked(true);
                }
                if (ti.length() - i == 6) {
                    daySix.setChecked(true);
                }
                if (ti.length() - i == 5) {
                    dayFive.setChecked(true);
                }
                if (ti.length() - i == 4) {
                    dayFour.setChecked(true);
                }
                if (ti.length() - i == 3) {
                    dayThree.setChecked(true);
                }
                if (ti.length() - i == 2) {
                    dayTwo.setChecked(true);
                }
                if (ti.length() - i == 1) {
                    dayOne.setChecked(true);
                }

            }
        }
    }


    private void saveChannel() {
        Channel.getInstance().setChannelName(mchannelName);
        Channel.getInstance().setContent(mchannelIllustration);
        Channel.getInstance().setWeekday(channelOpenData);
        Channel.getInstance().setStartTime(openTime);
        Channel.getInstance().setEndTime(closeTime);
        Channel.getInstance().setFans(Integer.parseInt(mbeFocousedNum));
        Channel.getInstance().setCreditValue(Integer.parseInt(mcredit));
        Channel.getInstance().setChannelImg(channelImagPath);
    }

    public void changeChannel(boolean tlIsOK) {
        if (tlIsOK) {
            ChangeChannelApi changeChannelApi = new ChangeChannelApi();
            ChangeChannelService changeChannelService = changeChannelApi.getService();
            SChangeChannelBean sChangeChannelBean = new SChangeChannelBean();
            sChangeChannelBean.setChannelID(cid);//1199
            sChangeChannelBean.setChannelName(mchannelName);
            sChangeChannelBean.setContent(mchannelIllustration);
            sChangeChannelBean.setStartTime(openTime);
            sChangeChannelBean.setEndTime(closeTime);
            sChangeChannelBean.setWeekDay(channelOpenData);
            sChangeChannelBean.setChannelType(2);
            Gson changeGson = new Gson();
            String changeBean = changeGson.toJson(sChangeChannelBean);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), changeBean);
            Call<GChannelResult> channelResultCall = changeChannelService.getChangeResult(requestBody);
            channelResultCall.enqueue(new Callback<GChannelResult>() {
                @Override
                public void onResponse(Call<GChannelResult> call, Response<GChannelResult> response) {
                    Log.d("ccc", "修改频道返回值" + response.body().getSign());
                    if (String.valueOf(response.body().getSign()).equals("1")) {
                        setDialog("电台信息修改成功");
                           saveChannel() ;
                    } else {
                        setDialog("电台信息修改失败");
                    }

                }

                @Override
                public void onFailure(Call<GChannelResult> call, Throwable t) {
                    setDialog("电台信息修改失败");
                }
            });
            if (channelImagPath.isEmpty()) {
//
            } else {
                upImage(channelImagPath, cid);
            }
        }else {
            setDialog("电台信息修改失败");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
            channelImagPath = picturePath;
            //  Picasso.with(ChannelInformationActivity.this).load(channelImagPath).into(channelImage);
            // channelImage.setImageResource(R.drawable.a);
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

    private void setDialog(String point) {
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(ChannelInformationActivity.this);
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

}
