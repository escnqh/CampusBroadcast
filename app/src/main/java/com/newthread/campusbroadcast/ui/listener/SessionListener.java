package com.newthread.campusbroadcast.ui.listener;

import android.app.Activity;
import android.util.Log;

import com.algebra.sdk.OnSessionListener;
import com.algebra.sdk.entity.Contact;

import java.util.List;

/**
 * Created by 倪启航 on 2017/7/17.
 */

public class SessionListener implements OnSessionListener {
    Activity context;

    public SessionListener(Activity context){
        this.context=context;
    }

    @Override
    public void onSessionEstablished(int i, int i1, int i2) {
        Log.i("mmmmmmmmmm","1223232");
    }

    @Override
    public void onSessionReleased(int i, int i1, int i2) {

    }

    @Override
    public void onSessionGet(int i, int i1, int i2, int i3) {

    }

    @Override
    public void onSessionPresenceAdded(int i, int i1, List<Contact> list) {

    }

    @Override
    public void onSessionPresenceRemoved(int i, int i1, List<Integer> list) {

    }

    @Override
    public void onDialogEstablished(int i, int i1, int i2, List<Integer> list) {

    }

    @Override
    public void onDialogLeaved(int i, int i1) {

    }

    @Override
    public void onDialogPresenceAdded(int i, int i1, List<Integer> list) {

    }

    @Override
    public void onDialogPresenceRemoved(int i, int i1, List<Integer> list) {

    }
}
