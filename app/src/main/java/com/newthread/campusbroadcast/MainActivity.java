package com.newthread.campusbroadcast;


import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.algebra.sdk.API;
import com.algebra.sdk.AccountApi;
import com.algebra.sdk.ChannelApi;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.newthread.campusbroadcast.User.SignupGsonBean;
import com.newthread.campusbroadcast.User.User;
import com.newthread.campusbroadcast.WebService.UserImgService;
import com.newthread.campusbroadcast.ui.activity.FillInfoActivity;
import com.newthread.campusbroadcast.ui.activity.LoginActivity;
import com.newthread.campusbroadcast.ui.activity.PlayerInfoActivity;
import com.newthread.campusbroadcast.ui.activity.StartActivity;
import com.newthread.campusbroadcast.ui.fragment.AllChannelFragment;
import com.newthread.campusbroadcast.ui.fragment.MainFragment;
import com.newthread.campusbroadcast.ui.fragment.MyFocusFragment;
import com.newthread.campusbroadcast.ui.listener.AccountListener;
import com.newthread.campusbroadcast.ui.listener.ChannelListener;
import com.newthread.campusbroadcast.util.CircleImageView;
import com.newthread.campusbroadcast.util.DialogChangePassw;
import com.newthread.campusbroadcast.util.JumpToScanUtil;
import com.newthread.campusbroadcast.util.NetworkUtils;
import com.newthread.campusbroadcast.util.PopuWindowTvInfo;
import com.newthread.campusbroadcast.webApi.UserImgApi;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.newthread.campusbroadcast.R.id.frame_fragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.info_view)
    NavigationView infoView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    //searchToolbar

    ImageView headProfit;
    //CircleImageView headImage;

    //三个fragment
    private FragmentManager fragmentManager;
    private MainFragment mainFragment;
    private AllChannelFragment allChannelFragment;
    private MyFocusFragment myFocusFragment;
    public AccountListener accountListener;
    User user = User.getInstance();
    public int selfId = user.getUserID();
    public String userAccount = user.getUseraccount();
    public String nick = user.getUsername();
    public int rank = user.getRank();
    public int credit = user.getCreditValue();
    public AccountApi accountApi = user.getAccountApi();
    int code = 0;
    public String picturePath;
    public String path=user.getImgUrl();
    private int onBackPressed=1;


    //搜索栏参数
    private int uid;
    private String channelName;
    //    private List<Channel> searchResult;//从途聆返回来的电台信息
//    private List<Integer> searchResultId;//传给后台判断的电台信息
//    private List<ChannelInfo> finalSearch;//后台返回的待选择的电台信息（跳转给搜索结果界面）
    ChannelApi channelApi = null;//电台Api
    ChannelListener channelListener = null;//电台监听回调
    //
    CircleImageView headImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        judgeNetwork();
        ButterKnife.bind(this);


        //获取用户id
        uid = User.getInstance().getUserID();//先默认为5326
        path = user.getImgUrl();
        Log.d("suibian", "onCreate: " + path);
        //fragment跳转
        fragmentManager = getSupportFragmentManager();
        //加载视图
        initView();
        //加载主界面
        initFragment();
        //LitePal.getDatabase();


        //跳转到个人信息监听
        onheadprofitClickListener();



        //搜索部分
        //获取频道Api
        handler.post(channel);
        View headerView = infoView.inflateHeaderView(R.layout.mine_header);
        TextView name = (TextView) headerView.findViewById(R.id.name);
        name.setText(nick);
        TextView creditvalue = (TextView) headerView.findViewById(R.id.credit);
        if (rank == 1) {
            creditvalue.setText("(" + String.valueOf(credit) + ")");
        } else {
            creditvalue.setVisibility(View.GONE);
        }
        headImage = (CircleImageView) headerView.findViewById(R.id.head_image1);
        Picasso.with(MainActivity.this).load(path).into(headImage);
        Picasso.with(MainActivity.this).load(path).into(headProfit);
        Log.d("1111", "onCreate: "+path);
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(BGAPhotoPickerActivity.newIntent(MainActivity.this, null, 1, null, false), code);
            }
        });


    }

    private void judgeNetwork() {
        //判断有无网络
        NetworkUtils networkUtils = new NetworkUtils();
        if (!networkUtils.isNetworkAvailable(this)) {
            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
            normalDialog.setMessage("请检查网络链接");
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int wch) {
                        }
                    });
//            normalDialog.setNegativeButton("关闭",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //...To-do
//                        }
//                    });
            // 显示
            normalDialog.show();
        }
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
                channelListener = new ChannelListener(MainActivity.this);
                channelApi.setOnChannelListener(channelListener);
                Log.d("ma", "获取channelApi: ");
            } else {
                handler.postDelayed(channel, 3000);
                Log.d("ma", "未获取channelApi: ");
            }
        }
    };


    //载入界面
    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        // toolbar.setSubtitle("这里是子标题");
        // toolbar.setLogo(R.drawable.head_portrait); //设置logo
        //  String SearchContent = getIntent().getStringExtra(SearchManager.QUERY);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        initNavigationBar();
    }

    private void initNavigationBar() {
        BottomNavigationBar navigationBar = (BottomNavigationBar) findViewById(R.id.bottomNavigationBar);

//        navigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
//        navigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        navigationBar.setBarBackgroundColor(R.color.navigationColor);
        navigationBar.setActiveColor(R.color.clickNavigation);

        navigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_main_surface, "主页"))
                .addItem(new BottomNavigationItem(R.drawable.ic_all_channel, "全部"))
                .addItem(new BottomNavigationItem(R.drawable.ic_focus, "关注"))
                .initialise();
        navigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                changeFragment(position);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
    }

    //跳转fragment
    private void changeFragment(int position) {
        //判断第三个fragment（易）
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        hideFragment(transaction);
        switch (position) {
            case 0:
                mainFragment = new MainFragment();
                transaction.replace(frame_fragment, mainFragment);
//                if (mainFragment == null) {
//                    mainFragment = new MainFragment();
//                    transaction.add(frame_fragment, mainFragment);
//                } else {
//                    transaction.show(mainFragment);
//                }
                break;
            case 1:
                allChannelFragment = new AllChannelFragment();
                transaction.replace(frame_fragment, allChannelFragment);
//                if (allChannelFragment == null) {
//                    allChannelFragment = new AllChannelFragment();
//                    transaction.add(frame_fragment, allChannelFragment);
//                } else {
//                    transaction.show(allChannelFragment);
//                }
                break;
            case 2:
                myFocusFragment = new MyFocusFragment();
                transaction.replace(frame_fragment, myFocusFragment);
//                if (myFocusFragment == null) {
//                    myFocusFragment = new MyFocusFragment();
//                    transaction.add(frame_fragment, myFocusFragment);
//                } else {
//                    transaction.show(myFocusFragment);
//                }
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transac) {
        if (mainFragment != null) {
            transac.hide(mainFragment);
        }
        if (allChannelFragment != null) {
            transac.hide(allChannelFragment);
        }
        if (myFocusFragment != null) {
            transac.hide(myFocusFragment);
        }
    }

    private void initFragment() {
        //加载主界面
        mainFragment = new MainFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frame_fragment, mainFragment);
        transaction.commit();
    }

    //对搜索设置和设置监听
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_serarch_activity, menu);
        //搜索
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("频道名称");
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast.makeText(MainActivity.this,query, Toast.LENGTH_SHORT).show();
                //  Log.d(TAG, "onQueryTextSubmit = " + queryText);
                //点击搜索监听键后的监听
                submitOnClickListener(searchView, query);


                return true;
                // return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //开启搜索后回调监听
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headProfit.setVisibility(View.INVISIBLE);
                Log.d("mm", "onOptionsItemSelected: 点击了搜索按钮");
            }
        });
        //关闭搜索后回调监听
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                headProfit.setVisibility(View.VISIBLE);
                return false;
            }
        });
        return true;
    }

    //监听搜索提交键
    private void submitOnClickListener(SearchView searchView, String query) {
        //！！先从途聆获取电台（排除不要口令的）
        //传给后台
        //后台传来的数据不唯一
        channelName = query;
        if ( channelListener==null)
        {
            channelListener = new ChannelListener(MainActivity.this);
        }
        channelApi.searchPublicChannel(uid, channelName);
//        if (query.equals("一整夜不睡觉")) {
//            Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
//            startActivity(intent);
//        }else if (query.equals("测试"))
//        {
//            Intent intent =new Intent(MainActivity.this, ChannelInformationActivity.class);
//            startActivity(intent);
//        }else if( query.equals("创建"))
//        {
//            Intent intent =new Intent(MainActivity.this, CreatChannelActivity.class);
//            startActivity(intent);
//        }else if (query.equals("神秘代码1")){
//            JumpToChannelUtil jumpToChannelUtil=new JumpToChannelUtil(1577,this);
//            jumpToChannelUtil.jump();
//
//        }else if(query.equals("神秘代码2")){
//
//        }

        searchView.setQuery(null, false);//提交后清空内容
        //if不存在则停留在搜索页面
        if (searchView != null) {
            // 得到输入管理对象
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                //弹出弹框显示查找不到
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
            }
            searchView.clearFocus(); // 不获取焦点
        }


    }

    //对menu菜单监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan:
                Toast.makeText(this, "点击了扫二维码按钮", Toast.LENGTH_SHORT).show();
                JumpToScanUtil jumpToScanUtil = new JumpToScanUtil(this);
                jumpToScanUtil.startJump();

        }
        return super.onOptionsItemSelected(item);
    }

    //对头像进行监听
    private void onheadprofitClickListener() {

        headProfit = (ImageView) findViewById(R.id.head_portrait);
        headProfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

//yijiali


        //infoView.getMenu();

        //菜单选项的监听事件
        infoView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.person_info:
                        Intent intent1 = new Intent(MainActivity.this, FillInfoActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.player:
                        if (rank==0){
                        Intent intent2 = new Intent(MainActivity.this, PlayerInfoActivity.class);
                        startActivity(intent2);}
                        else {
                        PopuWindowTvInfo popuWindowTvInfo = new PopuWindowTvInfo(MainActivity.this);
                        popuWindowTvInfo.ChangepopuInfo("您的身份已是主播！");
                        }
                        break;
                    case R.id.account_safe:
                        Dialog dialogChangePassw = new DialogChangePassw(MainActivity.this);
                        dialogChangePassw.show();
                        break;
                    case R.id.scanner:
                        JumpToScanUtil jumpToScanUtil = new JumpToScanUtil(MainActivity.this);
                        jumpToScanUtil.startJump();
                        break;
                    case R.id.exit_:
                        accountApi = user.getAccountApi();
                        accountApi.logout(selfId);
                        Log.d("newwwww", "onNavigationItemSelected: 退出登录");
                        if(LoginActivity.acti!=null)
                            LoginActivity.acti.finish();
                        if(StartActivity.act!=null)
                            StartActivity.act.finish();
                        SharedPreferences sharedPreferences=getSharedPreferences("loginInfo",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putBoolean("isLogin",false);
                        editor.commit();
                        Intent intent_exit = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent_exit);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == code) {
            picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
            Log.d("aaaa", picturePath);
            UpLoadImgurl(selfId);
        }

    }

    @Override
    public void onBackPressed() {

        if(onBackPressed==1){
            onBackPressed++;
            Toast.makeText(this,"再按一次退出！",Toast.LENGTH_SHORT).show();
        }else {
            super.onBackPressed();
            accountApi.logout(selfId);
            finish();
            if (LoginActivity.acti!=null)
                LoginActivity.acti.finish();
            if(StartActivity.act!=null)
                StartActivity.act.finish();
        }





    }
    private void UpLoadImgurl(int userID){
        UserImgApi userImgApi=new UserImgApi();
        UserImgService userImgService=userImgApi.getService();
        final File file=new File(picturePath);
        Log.d("mm", "UpLoadImgurl: "+picturePath+" file "+file);
        final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imgFile",file.getName(),requestFile);

        Call<SignupGsonBean> call = userImgService.upload(userID, body);
        call.enqueue(new Callback<SignupGsonBean>() {
            @Override
            public void onResponse(Call<SignupGsonBean> call, Response<SignupGsonBean> response) {
                SignupGsonBean signupGsonBean = response.body();
                int sign = signupGsonBean.getSign();
                Log.d("aaa", "sign" + sign);
                if (sign == 1) {
                    Picasso.with(MainActivity.this).load(file).into(headImage);
                    Picasso.with(MainActivity.this).load(file).into(headProfit);
                }
            }

            @Override
            public void onFailure(Call<SignupGsonBean> call, Throwable t) {

            }
        });

    }

//    private void initPermission(){
//        List<String> permissionList = new ArrayList<>();
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
//                permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
//            permissionList.add(Manifest.permission.READ_PHONE_STATE);
//        }
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
//                permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
//                permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
//            permissionList.add(Manifest.permission.CAMERA);
//        }
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
//                permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
//            permissionList.add(Manifest.permission.RECORD_AUDIO);
//        }
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
//                permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
//            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }
//        if (!permissionList.isEmpty())
//        {
//            String[] permissins = permissionList.toArray(new String[permissionList.size()]);
//            ActivityCompat.requestPermissions(MainActivity.this, permissins, 1);
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 1:
//                if (grantResults.length > 0) {
//                    for (int result : grantResults) {
//                        if (result != PackageManager.PERMISSION_GRANTED) {
//                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
//                            finish();
//                            return;
//                        }
//                    }
//                } else {
//                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                break;
//            default:
//        }
//    }


}
