package com.newthread.campusbroadcast.Entity;

import com.algebra.sdk.entity.Contact;

/**
 * Created by 倪启航 on 2017/7/22.
 */

public class ContactExt extends Contact{
    private boolean selected = false;
    private boolean speaker = false;

    public ContactExt(Contact c) {
        this.id = c.id;
        this.name = c.name;
        this.state = c.state;
        this.isPresent = c.isPresent;
    }

    public void setSpeaker(boolean tf) {
        speaker = tf;
    }

    public boolean isSpeaker() {
        return speaker;
    }

    public void setSelected(boolean tf) {
        selected = tf;
    }

    public boolean isSelected() {
        return selected;
    }
}
