package com.newthread.campusbroadcast.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newthread.campusbroadcast.R;
import com.newthread.campusbroadcast.WebService.AllChannelService;
import com.newthread.campusbroadcast.adapter.ChannelAdapter;
import com.newthread.campusbroadcast.bean.ChannelBean.ChannelInfo;
import com.newthread.campusbroadcast.bean.ChannelBean.GChannelInfor;
import com.newthread.campusbroadcast.util.NetworkUtils;
import com.newthread.campusbroadcast.webApi.AllChannelApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 张云帆 on 2017/7/22.
 */

public class AllChannelFragment extends BaseFragment {

    //全部电台信息
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
    private ChannelAdapter adapter;

    //获取activity对象
    AppCompatActivity activity;
    //获取布局
    View view;

    NetworkUtils networkUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_allchannel_surface, container, false);
        networkUtils = new NetworkUtils();
        if (networkUtils.isNetworkAvailable(getActivity())) {
            //从后台获取全部电台数据
            AllChannelApi allChannelApi = new AllChannelApi();
            AllChannelService allChannelService = allChannelApi.getService();
            Call<GChannelInfor> allChannelCall = allChannelService.getAllChannelInfor();
            allChannelCall.enqueue(new Callback<GChannelInfor>() {
                @Override
                public void onResponse(Call<GChannelInfor> call, Response<GChannelInfor> response) {
                    if (response.body().getChannelInfs().isEmpty()) {
                        Log.d("allchannel", "onResponse: 空空空");
                        setDialog("当前无电台");
                    } else {
                        //初始化数据
                        for (int i = 0; i < response.body().getChannelInfs().size(); i++) {
                            ChannelInfo channelInfo = new ChannelInfo(response.body().getChannelInfs().get(i).getImgURL(),
                                    response.body().getChannelInfs().get(i).getChannelName(),
                                    response.body().getChannelInfs().get(i).getNickname(),
                                    response.body().getChannelInfs().get(i).getGender(),
                                    response.body().getChannelInfs().get(i).getFans(),
                                    response.body().getChannelInfs().get(i).getChannelID());
                            channelInfoList.add(channelInfo);
                            Log.d("sesse", "onResponse: " + response.body().getChannelInfs().get(i).getChannelName());
                            Log.d("sesse", "onResponse: 性格"+response.body().getChannelInfs().get(i).getGender());
                            Log.d("sesse", "onResponse: 地址" + response.body().getChannelInfs().get(i).getImgURL());
                        }
                        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.allchannel_recycler_view);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        adapter = new ChannelAdapter(channelInfoList);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<GChannelInfor> call, Throwable t) {
                    setDialog("获取电台信息失败");
                    Log.d("allchan", "onFailure: "+t);
                }
            });

        }else {
            setDialog("当前没有网络");
        }




//        initChannelInfo();
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.allchannel_recycler_view);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        adapter = new ChannelAdapter(channelInfoList);
//        recyclerView.setAdapter(adapter);
        return view;
    }

    //    private void initChannelInfo() {
//        channelInfoList.clear();
//        for (int i=0;i<channelInfos.length;i++)
//        {
//            //随机添加
////            Random random = new Random();
////            int index = random.nextInt(channelInfos.length);
//            channelInfoList.add(channelInfos[i]);
//        }
//    }
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
