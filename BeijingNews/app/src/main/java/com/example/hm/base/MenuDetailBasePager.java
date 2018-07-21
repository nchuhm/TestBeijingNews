package com.example.hm.base;

import android.content.Context;
import android.view.View;

public abstract class MenuDetailBasePager {

    public final Context context;

    //各个详情页面的视图
    public View rootView;

    public MenuDetailBasePager(Context context){

        this.context = context;
        rootView = initView();
    }

    /**
     * 抽象方法，由于各个视图没有公共部分，强制重写这个方法，每个页面实行不同的效果
     * @return
     */
    public abstract View initView();

    /**
     * 子页面需要绑定数据，联网请求数据的时候，重写该方法
     */
    public void initData(){

    }
}
