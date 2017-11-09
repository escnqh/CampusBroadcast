package com.newthread.campusbroadcast.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.algebra.sdk.API;
import com.algebra.sdk.ChannelApi;
import com.google.gson.Gson;
import com.newthread.campusbroadcast.R;
import com.newthread.campusbroadcast.User.Channel;
import com.newthread.campusbroadcast.User.User;
import com.newthread.campusbroadcast.WebService.DeleteChannelService;
import com.newthread.campusbroadcast.WebService.isFollowService;
import com.newthread.campusbroadcast.adapter.SearchAdapter;
import com.newthread.campusbroadcast.bean.ChannelBean.ChannelInfo;
import com.newthread.campusbroadcast.bean.ChannelBean.GChannelResult;
import com.newthread.campusbroadcast.bean.ChannelBean.SDeleteChannelBean;
import com.newthread.campusbroadcast.bean.isFollowBean;
import com.newthread.campusbroadcast.ui.activity.ChannelInformationActivity;
import com.newthread.campusbroadcast.ui.activity.CreatChannelActivity;
import com.newthread.campusbroadcast.ui.listener.ChannelListener;
import com.newthread.campusbroadcast.util.JumpToTalkUtil;
import com.newthread.campusbroadcast.util.NetworkUtils;
import com.newthread.campusbroadcast.webApi.DeleteChannelApi;
import com.newthread.campusbroadcast.webApi.isFollowApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 倪启航 on 2017/7/25.
 */

public class MyFocusFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.broadcast)
    LinearLayout broadcast;
    @BindView(R.id.mychannel_pic)
    ImageView mychannelPic;
    @BindView(R.id.changeinfo)
    Button changeinfo;
    @BindView(R.id.gotochannel)
    Button gotochannel;
    @BindView(R.id.delchannel)
    Button delchannel;
    @BindView(R.id.newchannel)
    Button newchannel;
    Unbinder unbinder;
//    private ChannelInfo[] channelInfos = {
//            new ChannelInfo(R.drawable.a,"一整夜不睡觉","来嗨","男",1,0,0),
//            new ChannelInfo(R.drawable.b,"一整夜不睡觉","来嗨","女",10,0,1),
//            new ChannelInfo(R.drawable.c,"一整夜不睡觉","来嗨","男",11,0,2),
//            new ChannelInfo(R.drawable.d,"一整夜不睡觉","来嗨","男",100,0,3),
//            new ChannelInfo(R.drawable.e,"一整夜不睡觉","来嗨","男",101,0,4),
//            new ChannelInfo(R.drawable.a,"一整夜不睡觉","来嗨","女",110,0,5),
//            new ChannelInfo(R.drawable.b,"一整夜不睡觉","来嗨","男",111,0,6),
//            new ChannelInfo(R.drawable.c,"一整夜不睡觉","来嗨","男",1000,0,7),
//            new ChannelInfo(R.drawable.d,"一整夜不睡觉","来嗨","女",1001,0,8),
//            new ChannelInfo(R.drawable.e,"一整夜不睡觉","来嗨","女",1010,0,9),
//            new ChannelInfo(R.drawable.a,"一整夜不睡觉","来嗨","男",1011,0,10),
//            new ChannelInfo(R.drawable.a,"一整夜不睡觉","来嗨","男",1100,0,11),
//    };
    private List<ChannelInfo> channelInfoList = new ArrayList<>();
    private SearchAdapter adapter;
    private boolean ishaveChannel = false;
    private boolean isChannelHost = false;
    private LinearLayout myChannelInfo;
    private LinearLayout myChannel;
    private LinearLayout newChannelVisibale;
    private RecyclerView recyclerView;
    private ChannelApi channelApi;
    User user;
    Channel channel;
    NetworkUtils networkUtils;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myfocous, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.myfocuschannel_recycler_view);
        myChannelInfo = (LinearLayout) view.findViewById(R.id.myBroadcastShow);
        myChannel = (LinearLayout) view.findViewById(R.id.mychannel);
        newChannelVisibale = (LinearLayout) view.findViewById(R.id.newchannelvisibale);//R.id.newchannelvisibale
        unbinder = ButterKnife.bind(this, view);
        user=User.getInstance();
        channel=Channel.getInstance();
        networkUtils=new NetworkUtils();
        changeView();
        initChannelInfo();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SearchAdapter(channelInfoList);
        recyclerView.setAdapter(adapter);
        channelApi= API.getChannelApi();
        ChannelListener channelListener=new ChannelListener(MyFocusFragment.this);
        channelApi.setOnChannelListener(channelListener);
        delchannel.setOnClickListener(this);
        gotochannel.setOnClickListener(this);
        changeinfo.setOnClickListener(this);
        newchannel.setOnClickListener(this);
        return view;
    }

    private void changeView() {
        if (user.getRank() == 1) isChannelHost = true;
        if (channel.getChannelID() > 0) ishaveChannel = true;
        //修改相对应布局中的visible属性为gone
        if (!isChannelHost) {
            myChannelInfo.setVisibility(View.GONE);
        }
        if (!ishaveChannel) {
            myChannel.setVisibility(View.GONE);
        } else if (ishaveChannel) {
            newChannelVisibale.setVisibility(View.GONE);
        }
    }

    private void initChannelInfo() {
        if(networkUtils.isNetworkAvailable(getActivity())){
            channelInfoList.clear();
            isFollowApi isFollowApi=new isFollowApi();
            isFollowService isService=isFollowApi.getService();
            String re = "{\"userID\":" + user.getUserID() + "}";
            RequestBody r = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), re);
            Call<isFollowBean> call_isFollow = isService.getState(r);
            call_isFollow.enqueue(new Callback<isFollowBean>() {
                @Override
                public void onResponse(Call<isFollowBean> call, Response<isFollowBean> response) {
                    List<isFollowBean.ChannelInfsBean> infos = response.body().getChannelInfs();
                    for (int i = 0; i < infos.size(); i++) {
                        ChannelInfo channelInfo = new ChannelInfo(
                                response.body().getChannelInfs().get(i).getChannelName(),
                                response.body().getChannelInfs().get(i).getNickname(),
                                response.body().getChannelInfs().get(i).getChannelID());
                        channelInfoList.add(channelInfo);
                    }
                }

                @Override
                public void onFailure(Call<isFollowBean> call, Throwable t) {
                    setDialog("获取电台信息失败");
                    Log.d("allchan", "onFailure: "+t);
                }
            });
        }else {
            setDialog("当前没有网络");
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delchannel:
                channelApi.deletePublicChannel(user.getUserID(),2,channel.getChannelID());
                break;
            case R.id.newchannel:
                Intent newchannel =new Intent(getContext(), CreatChannelActivity.class);
                startActivity(newchannel);
                break;
            //点击事件
            case R.id.changeinfo:
                Intent changeInfo =new Intent(getContext(), ChannelInformationActivity.class);
                startActivity(changeInfo);
                break;
            case R.id.gotochannel:
                JumpToTalkUtil jumpToTalkUtil=new JumpToTalkUtil(channel.getChannelID(),getContext());
            default:
                break;
        }
    }

    public void upInfor(int i) {
        if(i==1){
            //先判断i的值，1是TL成功

            DeleteChannelApi deleteChannelApi = new DeleteChannelApi();
            DeleteChannelService deleteChannelService = deleteChannelApi.getService();
            SDeleteChannelBean sDeleteChannelBean = new SDeleteChannelBean();
            sDeleteChannelBean.setChannelID(channel.getChannelID());//1695
            sDeleteChannelBean.setUserID(user.getUserID());//5532
            Gson deleteGson = new Gson();
            String deleteBean = deleteGson.toJson(sDeleteChannelBean);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),deleteBean);
            Call<GChannelResult> channelResultCall = deleteChannelService.getDeleteResult(requestBody);
            channelResultCall.enqueue(new Callback<GChannelResult>() {
                @Override
                public void onResponse(Call<GChannelResult> call, Response<GChannelResult> response) {
                    Log.d("mfff", "onFailure: 执行删除操作"+response.body().getSign());
                    if (String.valueOf(response.body().getSign()).equals("1")) {
                        setDialog("电台删除成功");
                        Channel.getInstance().setChannelID(0);
                        Channel.getInstance().setChannelName("");
                        Channel.getInstance().setContent("");
                        Channel.getInstance().setWeekday(0);
                        Channel.getInstance().setStartTime(0);
                        Channel.getInstance().setEndTime(0);
                        Channel.getInstance().setCreditValue(0);
                        Channel.getInstance().setFans(0);
                        Channel.getInstance().setChannelImg("");
                        User.getInstance().setRank(0);
                        Log.d("mfff", "onFailure: 后台删除成功");
                    } else {
                        setDialog("电台删除失败");
                        Log.d("mfff", "onFailure: 后台删除失败");
                    }
                }
                @Override
                public void onFailure(Call<GChannelResult> call, Throwable t) {
                    setDialog("电台删除失败");
                    Log.d("mfff", "onFailure: 后台删除失败");
                }
            });

        }else{
            setDialog("电台删除失败");
            Log.d("mfff", "onFailure: 途聆删除电台失败");
        }


    }

    private void setDialog(String point) {
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getActivity());
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
