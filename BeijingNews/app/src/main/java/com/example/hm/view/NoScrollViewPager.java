package com.example.hm.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 通过自定义控件实现原有控件功能的删除
 */
public class NoScrollViewPager extends ViewPager{
    /**
     * 实例化时调用本方法
     * @param context
     */
    public NoScrollViewPager(Context context) {
        super(context);
    }

    /**
     * 在布局文件中使用该类时，实例化该类用这个构造方法，不可缺少
     * @param context
     * @param attrs
     */
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 重写触摸事件，取消掉触摸事件
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
