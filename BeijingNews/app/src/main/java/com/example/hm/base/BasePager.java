package com.example.hm.base;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hm.Activity.MainActivity;
import com.example.hm.beijingnews.R;

import org.xutils.view.annotation.ViewInject;

/**
 * 基类
 * HomePager,NewsCenterPager,SmartServicePager,GovaffairPager,SettingPager 都继承这个方法
 */
public class BasePager {
    //上下文
    public final Context context;

    //视图，代表各个不同的页面
    public View rootview;

    //标题
    public TextView tv_title;

    //点击侧滑
    public ImageButton ib_menu;

    //帧布局,子Fragment
    public FrameLayout fl_content;


    public BasePager(Context context){
        this.context = context;
        rootview = initView();
    }

    /**
     * 初始化公共部分数据，初始化公共部分视图
     * @return
     */
    private View initView() {
        View view =View.inflate(context, R.layout.base_pager,null);
        tv_title = view.findViewById(R.id.tv_title);
        ib_menu = view.findViewById(R.id.ib_menu);
        fl_content = view.findViewById(R.id.fl_content);
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();
            }
        });
        return view;
    }

    public void initData(){

    }
}
