package com.example.hm.menudatailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.hm.base.MenuDetailBasePager;
import com.example.hm.utils.LogUtil;

/**
 * 专题详情页面
 */
public class TopicMennnuDatailPager extends MenuDetailBasePager {

    private  TextView textView;
    
    public TopicMennnuDatailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("专题详情页面被初始化了");
        textView.setText("专题详情页面内容");
    }
}
