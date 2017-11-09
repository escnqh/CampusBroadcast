package com.newthread.campusbroadcast.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.newthread.campusbroadcast.R;
import com.newthread.campusbroadcast.User.User;
import com.newthread.campusbroadcast.WebService.HotSearchService;
import com.newthread.campusbroadcast.WebService.RecommendByCollegeService;
import com.newthread.campusbroadcast.adapter.ChannelAdapter;
import com.newthread.campusbroadcast.bean.ChannelBean.ChannelInfo;
import com.newthread.campusbroadcast.bean.ChannelBean.GChannelInfor;
import com.newthread.campusbroadcast.bean.ChannelBean.GHotSearch;
import com.newthread.campusbroadcast.bean.ChannelBean.SRecommendByCollege;
import com.newthread.campusbroadcast.util.JumpToChannelUtil;
import com.newthread.campusbroadcast.util.NetworkUtils;
import com.newthread.campusbroadcast.webApi.HotSearchApi;
import com.newthread.campusbroadcast.webApi.RecommendByCollegeApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.newthread.campusbroadcast.ui.activity.LoginActivity.TAG;


/**
 * Created by 张云帆 on 2017/7/21.
 */

public class MainFragment extends BaseFragment {
    private DrawerLayout mDrawerLayout;
    ImageView headProfit;
    private AdviseAdapter adapter;
    //主界面需要的信息数据类型和实例
//热搜榜电台数据

//根据学校推荐电台数据

    //热搜榜相关对象
    private ViewPager viewPager;//滑动组件
    private List<ImageView> imageViews;//滑动的图片的集合
    // private String[] titles;//图片标题
    // private Drawable [] imageResId;
    private int[] imageResId;//无网络时图片ID
    private String[] imagePath=new String[5];//有网络时图片地址
    private List<View> dots;//图片上的点
    // private TextView tv_title;
    private int currentItem = 0;//当前图片索引号
    private ScheduledExecutorService scheduledExecutorService;//周期执行服务
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            viewPager.setCurrentItem(currentItem);//切换显示当前图片
            //频道名字

        }
    };
    private String[] cnames=new String[5];//频道名字组
    private int[] channelID=new int[5];
    private TextView cn;


    //获取activity对象
    AppCompatActivity activity;
    //获取布局
    View view;


    //学校推荐数据设置
//    !!!
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
    private ChannelAdapter channeladapter;
    NetworkUtils networkUtils;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_surface, container, false);
        imageResId = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};//设置图片
        cnames = new String[]{"无", "无", "无", "无", "无"};
        channelID=new int[]{0,0,0,0,0};
        networkUtils = new NetworkUtils();
        if (networkUtils.isNetworkAvailable(getActivity())) {
            //设置热搜
            setHotChannel();
            //设置推荐
            RecommendByCollegeApi recommendByCollegeApi = new RecommendByCollegeApi();
            RecommendByCollegeService recommendByCollegeService = recommendByCollegeApi.getService();
            SRecommendByCollege recommendByCollege = new SRecommendByCollege();
            recommendByCollege.setUserID(User.getInstance().getUserID());
            Gson gson = new Gson();
            String bean = gson.toJson(recommendByCollege);
            final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bean);
            Call<GChannelInfor> recommendChannelResult = recommendByCollegeService.getRecommendChannelInfor(requestBody);
            recommendChannelResult.enqueue(new Callback<GChannelInfor>() {
                @Override
                public void onResponse(Call<GChannelInfor> call, Response<GChannelInfor> response) {
                    Log.d("sesse", "onResponse: ");
                    if(response.body().getChannelInfs().isEmpty())
                    {
                        Log.d("sesse", "onResponse: 空空空");
                        setDialog("当前无校园电台推荐");
                    }else {
                        for (int i = 0; i < response.body().getChannelInfs().size(); i++) {
                            ChannelInfo channelInfo = new ChannelInfo(response.body().getChannelInfs().get(i).getImgURL(),
                                    response.body().getChannelInfs().get(i).getChannelName(),
                                    response.body().getChannelInfs().get(i).getNickname(),
                                    response.body().getChannelInfs().get(i).getGender(),
                                    response.body().getChannelInfs().get(i).getFans(),
                                    response.body().getChannelInfs().get(i).getChannelID());
                            Log.d(TAG, "onResponse: 频道id"+response.body().getChannelInfs().get(i).getChannelID());
                            channelInfoList.add(channelInfo);
                            Log.d("sesse", "onResponse: " + response.body().getChannelInfs().get(i).getChannelName());
                            Log.d("sesse", "onResponse: 地址" + response.body().getChannelInfs().get(i).getImgURL());
                        }
                        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.main_recycler_view);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2) {
                            @Override
                            public boolean canScrollHorizontally() {
                                return false;
                            }
                        };
                        recyclerView.setNestedScrollingEnabled(false);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        channeladapter = new ChannelAdapter(channelInfoList);
                        recyclerView.setAdapter(channeladapter);
                    }
                }

                @Override
                public void onFailure(Call<GChannelInfor> call, Throwable t) {
                    Log.d("sesse", "onFailure: "+t);

                }
            });


        } else {
            //设置热搜榜图片和相关信息
            imageViews = new ArrayList<ImageView>();
            // 热搜初始化图片资源
            for (int i = 0; i < imageResId.length; i++) {
                ImageView imageView = new ImageView(getActivity());
                //imageView.setImageDrawable(imageResId[i]);
                imageView.setImageResource(imageResId[i]);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageViews.add(imageView);
            }
            adapter = new AdviseAdapter();
            viewPager = (ViewPager) view.findViewById(R.id.vp);
            viewPager.setAdapter(adapter);// 设置填充ViewPager页面的适配器
            // 设置一个监听器，当ViewPager中的页面改变时调用
            viewPager.addOnPageChangeListener(new MypageChangeListener());


        }
        //设置热搜榜图片和相关信息
//        imageResId = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};//设置图片
//        cnames = new String[]{"无", "无", "无", "无", "无"};
//
//
//        imageViews = new ArrayList<ImageView>();
//        // 热搜初始化图片资源
//        for (int i = 0; i < imageResId.length; i++) {
//            ImageView imageView = new ImageView(getActivity());
//            //imageView.setImageDrawable(imageResId[i]);
//            imageView.setImageResource(imageResId[i]);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageViews.add(imageView);
//        }
        dots = new ArrayList<View>();
        dots.add(view.findViewById(R.id.v_dot0));
        dots.add(view.findViewById(R.id.v_dot1));
        dots.add(view.findViewById(R.id.v_dot2));
        dots.add(view.findViewById(R.id.v_dot3));
        dots.add(view.findViewById(R.id.v_dot4));
        cn = (TextView) view.findViewById(R.id.cn);
//        adapter = new AdviseAdapter();
//        viewPager = (ViewPager) view.findViewById(R.id.vp);
//        viewPager.setAdapter(adapter);// 设置填充ViewPager页面的适配器
//        // 设置一个监听器，当ViewPager中的页面改变时调用
//        viewPager.addOnPageChangeListener(new MypageChangeListener());


        //学校广播信息界面
//        initChannelInfo();
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.main_recycler_view);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2) {
//            @Override
//            public boolean canScrollHorizontally() {
//                return false;
//            }
//        };
//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        channeladapter = new ChannelAdapter(channelInfoList);
//        recyclerView.setAdapter(channeladapter);


        return view;
    }

    private void setHotChannel() {
        WindowManager wm = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        final int widthPhone = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        //获取热搜
        HotSearchApi hotSearchApi = new HotSearchApi();
        HotSearchService hotSearchService=hotSearchApi.getService();
        Call<GHotSearch> hotSearchCall = hotSearchService.getInfor();
        hotSearchCall.enqueue(new Callback<GHotSearch>() {
            @Override
            public void onResponse(Call<GHotSearch> call, Response<GHotSearch> response) {
                Log.d(TAG, "onResponse: "+call.request().url().toString());
                if(!response.body().getChannelInfs().isEmpty())
                {
                    for(int i= 0;i<response.body().getChannelInfs().size();i++)
                    {
                        if(!response.body().getChannelInfs().get(i).getImgURL().isEmpty())
                        {
                            imagePath[i]=response.body().getChannelInfs().get(i).getImgURL();
                        }
                        cnames[i]=response.body().getChannelInfs().get(i).getChannelName();
                        channelID[i]=response.body().getChannelInfs().get(i).getChannelID();
                        Log.d(TAG, "hotSearch电台ID"+response.body().getChannelInfs().get(i).getChannelID()+"---图片地址"+response.body().getChannelInfs().get(i).getImgURL());
                    }
                    imageViews = new ArrayList<ImageView>();
                    // 热搜初始化图片资源
                    for (int i = 0; i < imagePath.length; i++) {
                        ImageView imageView = new ImageView(getActivity());
                        //imageView.setImageDrawable(imageResId[i]);
                        Picasso.with(getActivity())
                                .load(imagePath[i])
                                .placeholder(R.drawable.b)
                                .fit()
                                .into(imageView);
                        Log.d(TAG, "MainF:onResponse: "+imagePath[i]);
//                            imageView.setImageResource(imageResId[i]);
//                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageViews.add(imageView);
                    }
                    for(int i=imagePath.length-1;i<5;i++)
                    {
                        ImageView imageView = new ImageView(getActivity());
                        //imageView.setImageDrawable(imageResId[i]);
                        imageView.setImageResource(imageResId[i]);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageViews.add(imageView);
                    }
                    adapter = new AdviseAdapter();
                    viewPager = (ViewPager) view.findViewById(R.id.vp);
                    viewPager.setAdapter(adapter);// 设置填充ViewPager页面的适配器
                    // 设置一个监听器，当ViewPager中的页面改变时调用
                    viewPager.addOnPageChangeListener(new MypageChangeListener());

                }


            }

            @Override
            public void onFailure(Call<GHotSearch> call, Throwable t) {

            }
        });
    }

//

//!!
//    private void initChannelInfo() {
//        channelInfoList.clear();
//        for (int i = 0; i < channelInfos.length; i++) {
//            //随机添加
////            Random random = new Random();
////            int index = random.nextInt(channelInfos.length);
//            channelInfoList.add(channelInfos[i]);
//        }
//    }

    @Override
    public void onStart() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每5秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 5, TimeUnit.SECONDS);
        super.onStart();
    }

    @Override
    public void onStop() {
        scheduledExecutorService.shutdown();
        super.onStop();
    }

    /**
     * 换行切换任务
     *
     * @author Administrator
     */
    private class ScrollTask implements Runnable {

        public void run() {
            synchronized (viewPager) {
                System.out.println("currentItem: " + currentItem);
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
            }
        }
    }

    private class AdviseAdapter extends PagerAdapter {

        //要显示图片的个数
        @Override
        public int getCount() {
            return imageResId.length;
        }
        // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            container.addView(imageViews.get(position));
            Log.d("kkk", "instantiateItem: "+"cn"+channelID[position]+"aa"+position);

            Log.d("ss", "instantiateItem: " + position);
            View view = imageViews.get(position);
            //点击图片跳转事件
            if (networkUtils.isNetworkAvailable(getActivity()))
            {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!String.valueOf(channelID[position]).equals("0"))
                        {
                            //跳转
                            JumpToChannelUtil jumpToChannelUtil=new JumpToChannelUtil(channelID[position],getActivity());
                            //channelID[position];
                        }else {
                            setDialog("不存在此电台"+position);
                        }

                        Log.d("mf", "onClick: 显示图片个数" + position);
                    }
                });
            }else{
                setDialog("网络链接失败");
            }

            return imageViews.get(position);
        }

        // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }
    }

    /**
     * 当ViewPager中页面的状态发生改变时调用
     *
     * @author Administrator
     */
    private class MypageChangeListener implements ViewPager.OnPageChangeListener {
        private int oldPosition = 0;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            //  tv_title.setText(titles[position]);
            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
            cn.setText(cnames[currentItem]);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

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
