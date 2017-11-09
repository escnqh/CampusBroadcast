package com.newthread.campusbroadcast.ui.fragment;

import android.support.v4.app.Fragment;

import com.newthread.campusbroadcast.ui.listener.FragmentBackListener;

/**
 * Created by 张云帆 on 2017/7/17.
 */

public class BaseFragment extends Fragment implements FragmentBackListener{
    @Override
    public boolean onBackPressed() {
        return false;
    }
}
