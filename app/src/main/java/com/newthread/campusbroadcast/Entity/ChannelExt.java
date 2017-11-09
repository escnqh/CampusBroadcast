package com.newthread.campusbroadcast.Entity;

import com.algebra.sdk.entity.Channel;
import com.algebra.sdk.entity.IntStr;

import java.util.ArrayList;

/**
 * Created by 倪启航 on 2017/7/22.
 */

public class ChannelExt extends Channel {

    public boolean isCurrent = false;
    public ArrayList<ContactExt> members = null;

    public ChannelExt(int type, int id, String name) {
        super(type, id, name);
        members=new ArrayList<ContactExt>();
    }

    public void copyAttrs(Channel ch) {
        this.isHome = ch.isHome;
        this.memberCount = ch.memberCount;
        this.presenceCount = ch.presenceCount;
        this.needPassword = ch.needPassword;
        this.visibility = ch.visibility;
        this.owner = new IntStr(ch.owner.i, ch.owner.s);
    }

}
