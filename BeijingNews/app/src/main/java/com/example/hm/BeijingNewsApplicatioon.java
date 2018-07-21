package com.example.hm;

import android.app.Application;

import org.xutils.x;

/**
 * 代表整个软件
 */

public class BeijingNewsApplicatioon extends Application{
    /**
     * 所有组件被创建之前执行
     */
    public void onCreate() {
        super.onCreate();
        x.Ext.setDebug(true);
        x.Ext.init(this);
    }
}
