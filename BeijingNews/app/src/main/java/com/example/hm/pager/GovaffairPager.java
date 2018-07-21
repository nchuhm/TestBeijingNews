package com.example.hm.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.hm.base.BasePager;
import com.example.hm.utils.LogUtil;

/**
 * 政要指南
 */
public class GovaffairPager extends BasePager {

    public GovaffairPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("政要指南数据被初始化了");
        //设置标题
        tv_title.setText("政要指南");
        //联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //把子视图添加到BasePager的Frgment中
        fl_content.addView(textView);
        //绑定数据
        textView.setText("政要指南内容");
    }
}
