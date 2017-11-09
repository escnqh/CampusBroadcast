package com.newthread.campusbroadcast.ui.listener;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.widget.Toast;
import com.algebra.sdk.OnAccountListener;
import com.algebra.sdk.entity.Constant;
import com.algebra.sdk.entity.Contact;
import com.algebra.sdk.entity.UserProfile;
import com.newthread.campusbroadcast.MainActivity;
import com.newthread.campusbroadcast.User.User;
import com.newthread.campusbroadcast.ui.activity.LoginActivity;
import java.util.List;

/**
 * Created by 倪启航 on 2017/7/16.
 */

public class AccountListener implements OnAccountListener{
    public String userAccount;
    public Context context;
    public  LoginActivity loginActivity;
    User user=User.getInstance();
    public   AccountListener(Context context){
         this.context=context;
     }



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case Constant.ACCOUNT_RESULT_PREOK:
                Toast.makeText(context, "登录中", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.ACCOUNT_RESULT_TIMEOUT:
                    Toast.makeText(context, "登录超时", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.ACCOUNT_RESULT_ERR_USER_NOTEXIST:
                    Toast.makeText(context, "账号输入有误", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.ACCOUNT_RESULT_ERR_USER_PWD:
                    Toast.makeText(context, "密码输入有误", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.ACCOUNT_RESULT_ERR_SERVER_UNAVAILABLE:
                    Toast.makeText(context, "无服务", Toast.LENGTH_SHORT).show();
                    break;
                case Constant.ACCOUNT_RESULT_ERR_NETWORK:
                    Toast.makeText(context, "无网络连接", Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    };

    @Override
    public void onLogin(int uid, int result, UserProfile userProfile) {


        if (result == Constant.ACCOUNT_RESULT_OK
                || result == Constant.ACCOUNT_RESULT_ALREADY_LOGIN) {
            if (userProfile != null) {
                boolean  userBoundPhone = !userProfile.userPhone.equals("none");
                String userAccount = new String(userProfile.userName);
                String userNick = new String(userProfile.userNick);
                boolean isVisitor = (userProfile.userType == Constant.USER_TYPE_VISITOR);
                Log.d("mmmm", "onLogin: "+userBoundPhone+"  "+userAccount+" "+userNick+" "+isVisitor);

                user.setUserID(uid);
                user.setState(Constant.CONTACT_STATE_ONLINE);
                user.setUseraccount(userAccount);
                user.setUsername(userNick);

                Intent intent=new Intent(context,MainActivity.class);
                context.startActivity(intent);

                }

            return;
        }

        if (result == Constant.ACCOUNT_RESULT_PREOK) {
            Log.d("mmmm", "Login... ");
            Message message=new Message();
            message.what=result;
            handler.sendMessage(message);
            return;
        }

        if (result == Constant.ACCOUNT_RESULT_TIMEOUT) {
            Log.d("mmm", "time out...: ");
            Message message=new Message();
            message.what=result;
            handler.sendMessage(message);
            return;
        }

        if (result == Constant.ACCOUNT_RESULT_ERR_USER_NOTEXIST) {
            Log.d("mmmm", "user not exit... ");
            Message message=new Message();
            message.what=result;
            handler.sendMessage(message);
        }
        if (result == Constant.ACCOUNT_RESULT_ERR_USER_PWD) {
            Log.d("mmmm", "user pass is error.. ");
            Message message=new Message();
            message.what=result;
            handler.sendMessage(message);

        }
        if (result == Constant.ACCOUNT_RESULT_ERR_SERVER_UNAVAILABLE) {
            Log.d("mmmm", "Err service.. ");
            Message message=new Message();
            message.what=result;
            handler.sendMessage(message);
        }
        if (result == Constant.ACCOUNT_RESULT_ERR_NETWORK) {
            Log.d("mmmm", "Err network.. ");
            Message message=new Message();
            message.what=result;
            handler.sendMessage(message);
        }
        if (result == Constant.ACCOUNT_RESULT_OTHER_LOGIN) {
            Log.d("mmmm", "ERROR ACCOUNT");
            Message message=new Message();
            message.what=result;
            handler.sendMessage(message);
        }
        return;



    }
    //创建用户回调
    @Override
    public void onCreateUser(int selfId, int result, String account) {
        if (result == Constant.ACCOUNT_RESULT_OK) { // successful, auto login..
            userAccount = new String(account);

            user.setRegisterAccount(userAccount);


        }


    }

    @Override
    public void onLogout() {
        Log.d("aaaaaa","exit");



    }

    @Override
    public void onSetNickName(int id) {
        if (id>0){
        user.setIsNick(1);
        }
    }

    @Override
    public void onChangePassWord(int id, boolean successful) {
        if (successful){
        user.setSuccessful(successful);
//        Message message=new Message();
//            message.obj=successful;
        }
    }

    @Override
    public void onAskUnbind(int i) {

    }

    @Override
    public void onAuthRequestReply(int i, int i1, String s) {

    }

    @Override
    public void onAuthBindingReply(int i, int i1, String s) {

    }

    @Override
    public void onAuthRequestPassReply(int i, int i1, String s) {

    }

    @Override
    public void onAuthResetPassReply(int i, int i1) {

    }

    @Override
    public void onFriendsSectionGet(int i, int i1, int i2, int i3, List<Contact> list) {

    }

    @Override
    public void onFriendStatusUpdate(int i, int i1, int i2) {

    }

    @Override
    public void onShakeScreenAck(int i, int i1, int i2) {

    }

    @Override
    public void onShakeScreenReceived(int i, String s, String s1) {

    }

    @Override
    public void onSetStatusReturn(int i, boolean b) {

    }

    @Override
    public void onHearbeatLost(int i, int i1) {

    }

    @Override
    public void onKickedOut(int i, int i1) {

    }

    @Override
    public void onSelfStateChange(int userId, int state) {

    }

    @Override
    public void onSelfLocationAvailable(int i, Double aDouble, Double aDouble1, Double aDouble2) {

    }

    @Override
    public void onSelfLocationReported(int i) {

    }

    @Override
    public void onUserLocationNotify(int i, String s, Double aDouble, Double aDouble1) {

    }

    @Override
    public void onLogger(int i, String s) {

    }

    @Override
    public void onSmsRequestReply(int i) {

    }
}
