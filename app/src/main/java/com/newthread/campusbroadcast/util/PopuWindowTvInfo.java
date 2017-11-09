package com.newthread.campusbroadcast.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.newthread.campusbroadcast.R;

/**
 * Created by HP on 2017/7/29.
 */

public class PopuWindowTvInfo extends PopupWindow {
    private Context PContext;

    public PopuWindowTvInfo (Context context){
        PContext = context;
    }

    public void ChangepopuInfo(String text){

        View view = LayoutInflater.from(PContext).inflate(R.layout.popuwindow_signup,null);
        final PopupWindow pop = new PopupWindow(view, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);

        //获取屏幕宽度
        pop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setAnimationStyle(R.style.PopupAnimation);
        pop.showAtLocation(view, Gravity.CENTER, 0, 0);

        //提示信息更新
        TextView tv_Info = (TextView) view.findViewById(R.id.tv_info);
        tv_Info.setText(text);

        //“确认”按钮的返回事件
        Button btu_confirm = (Button) view.findViewById(R.id.btu_confirm);
        btu_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });

    }
}
