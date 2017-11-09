package com.newthread.campusbroadcast.util;

import android.content.Context;
import android.content.Intent;

import com.newthread.campusbroadcast.MainActivity;

/**
 * Created by 倪启航 on 2017/7/23.
 */

public class GoBackToMainActivityUtil {

    Context context = null;

    public GoBackToMainActivityUtil(Context context) {
        this.context = context;
    }

    public void Go() {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

}
