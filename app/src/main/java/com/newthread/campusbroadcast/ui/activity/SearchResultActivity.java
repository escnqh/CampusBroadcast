package com.newthread.campusbroadcast.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.algebra.sdk.entity.Channel;
import com.google.gson.Gson;
import com.newthread.campusbroadcast.R;
import com.newthread.campusbroadcast.WebService.SearchChannelService;
import com.newthread.campusbroadcast.adapter.SearchAdapter;
import com.newthread.campusbroadcast.bean.ChannelBean.ChannelInfo;
import com.newthread.campusbroadcast.bean.ChannelBean.GSearchResult;
import com.newthread.campusbroadcast.bean.ChannelBean.SSendChannelIdArry;
import com.newthread.campusbroadcast.ui.listener.ChannelListener;
import com.newthread.campusbroadcast.webApi.SearchChannelApi;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 张云帆 on 2017/7/24.
 */

public class SearchResultActivity extends BaseActivity {
    private String TAG = "sra";
    //返回键
    ImageView backImage;
    //从途聆获取的电台信息
    ChannelListener channelListener = new ChannelListener(this);
    private List<Channel> searchResult = null;//从途聆返回来的电台信息
    private List<Integer> searchResultId;//传给后台判断的电台信息
    private List<ChannelInfo> finalSearch;//后台返回的待选择的电台信息（跳转给搜索结果界面）
    //全部电台信息
//    private ChannelInfo[] channelInfos = {
//            new ChannelInfo(R.drawable.a, "一整夜不睡觉", "来嗨", "男", 1, 0, 0),
//            new ChannelInfo(R.drawable.b, "一整夜不睡觉", "来嗨", "女", 10, 0, 1),
//            new ChannelInfo(R.drawable.c, "一整夜不睡觉", "来嗨", "男", 11, 0, 2),
//            new ChannelInfo(R.drawable.d, "一整夜不睡觉", "来嗨", "男", 100, 0, 3),
//            new ChannelInfo(R.drawable.e, "一整夜不睡觉", "来嗨", "男", 101, 0, 4),
//            new ChannelInfo(R.drawable.a, "一整夜不睡觉", "来嗨", "女", 110, 0, 5),
//            new ChannelInfo(R.drawable.b, "一整夜不睡觉", "来嗨", "男", 111, 0, 6),
//            new ChannelInfo(R.drawable.c, "一整夜不睡觉", "来嗨", "男", 1000, 0, 7),
//            new ChannelInfo(R.drawable.d, "一整夜不睡觉", "来嗨", "女", 1001, 0, 8),
//            new ChannelInfo(R.drawable.e, "一整夜不睡觉", "来嗨", "女", 1010, 0, 9),
//            new ChannelInfo(R.drawable.a, "一整夜不睡觉", "来嗨", "男", 1011, 0, 10),
//            new ChannelInfo(R.drawable.a, "一整夜不睡觉", "来嗨", "男", 1100, 0, 11),
//    };
    private List<ChannelInfo> channelInfoList = new ArrayList<>();
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);
        //得到途聆返的数据并设置上传给后台的频道id
        setSendChannelID();
//        for (int i=0;i<searchResultId.size();i++)
//        {
//            Log.d(TAG, "是否带密码"+searchResultId.get(i).intValue());
//        }
        //上传后台并得到返回数据
        getSearchFromBack();


        //初始化
//        initChannelInfo();
        //显示出数据
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        searchAdapter = new SearchAdapter(channelInfoList);
//        recyclerView.setAdapter(searchAdapter);
        onbackClickListener();
    }

    private void getSearchFromBack() {
        if (!searchResultId.isEmpty()) {
            SearchChannelApi searchChannelApi = new SearchChannelApi();
            SearchChannelService searchChannelService = searchChannelApi.getService();
            SSendChannelIdArry sSendChannelIdArry = new SSendChannelIdArry();
            sSendChannelIdArry.setChannelInfs(searchResultId);
            Gson gson = new Gson();
            String bean = gson.toJson(sSendChannelIdArry);
            final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bean);
            Call<GSearchResult> searchResultCall = searchChannelService.getSearchResult(requestBody);
            searchResultCall.enqueue(new Callback<GSearchResult>() {
                @Override
                public void onResponse(Call<GSearchResult> call, Response<GSearchResult> response) {
                    Log.d("sesse", "onFailure: " + response.body());
                    if (response.body().getChannelInfs().isEmpty()) {
                        Log.d(TAG, "onResponse: 空空空");
                        setDialog("找不到该电台");
                    } else {

                        //初始化数据
                        for (int i = 0; i < response.body().getChannelInfs().size(); i++) {
                            ChannelInfo channelInfo=new ChannelInfo(response.body().getChannelInfs().get(i).getChannelName(),
                                    response.body().getChannelInfs().get(i).getNickname(),
                                    response.body().getChannelInfs().get(i).getChannelID());
                            channelInfoList.add(channelInfo);
                            Log.d("sesse", "onResponse: " + response.body().getChannelInfs().get(i).getChannelName()
                                    + "" + response.body().getChannelInfs().get(i).getNickname());
                        }
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchResultActivity.this);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        searchAdapter = new SearchAdapter(channelInfoList);
                        recyclerView.setAdapter(searchAdapter);

                    }
                }

                @Override
                public void onFailure(Call<GSearchResult> call, Throwable t) {
                    Log.d("sesse", "onFailure: " + t);
                    setDialog("找不到该电台");
                }
            });
        }
    }

    private void setSendChannelID() {
        searchResult = ChannelListener.getSearchResult();
        searchResultId = new ArrayList<Integer>();
        if (searchResult != null) {
            for (int i = 0; i < searchResult.size(); i++) {
                searchResultId.add(new Integer(searchResult.get(i).cid.getId()));
                Log.d(TAG, "上传给后台的数据:" + "id：" + searchResult.get(i).cid.getId());
            }
        }
    }

//    private void initChannelInfo() {
//        channelInfoList.clear();
//        for (int i = 0; i < channelInfos.length; i++) {
//            //随机添加
////            Random random = new Random();
////            int index = random.nextInt(channelInfos.length);
//            channelInfoList.add(channelInfos[i]);
//        }
//    }

    //对返回键进行监听
    private void onbackClickListener() {
        backImage = (ImageView) findViewById(R.id.back);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SearchResultActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }
    private void setDialog(String point) {
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(SearchResultActivity.this);
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
