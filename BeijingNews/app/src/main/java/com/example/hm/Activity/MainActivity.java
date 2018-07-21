package com.example.hm.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Window;

import com.example.hm.beijingnews.R;
import com.example.hm.fragment.ContentFragment;
import com.example.hm.fragment.LeftmenuFragment;
import com.example.hm.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class MainActivity extends SlidingFragmentActivity {

    public static final String MAIN_CONTTENT_TAG = "main_conttent_tag";
    public static final String LEFTMENU_TAG = "leftmenu_tag";
    private LeftmenuFragment leftmenuFragment;
    private ContentFragment contentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //设置隐藏标题
        super.onCreate(savedInstanceState);

        //1.设置主页面
        setContentView(R.layout.activity_main);

        //2.设置左侧菜单
        setBehindContentView(R.layout.activity_leftmenu);

        //3.设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
//        slidingMenu.setSecondaryMenu(R.layout.activity_rightmenu); //设置右侧菜单

        //4.设置显示的模式：左侧菜单+主页，左侧菜单+右侧菜单+主页，主页+右侧菜单
        slidingMenu.setMode(SlidingMenu.LEFT);

        //5.设置滑动模式：滑动边缘，全屏滑动，不可滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //6.设置主页面占据的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(MainActivity.this,200));



        //初始化Fragment
        initFragment();
    }

    private void initFragment(){
        //得到FragmentManger
        FragmentManager fm = getSupportFragmentManager();
        //开启事务
        FragmentTransaction ft = fm.beginTransaction();
        //替换
        ft.replace(R.id.fl_main,new ContentFragment(),MAIN_CONTTENT_TAG); //主页
        ft.replace(R.id.fl_leftmenu,new LeftmenuFragment(), LEFTMENU_TAG); //左侧菜单
        //提交
        ft.commit();

//      getSupportFragmentManager().beginTransaction().replace(R.id.fl_main,new ContentFragment(),MAIN_CONTTENT_TAG).replace(R.id.fl_leftmenu,new ContentFragment(), LEFTMENU_TAG).commit();

    }

    /**
     * 得到左侧菜单的Fragment
     * @return
     */
    public LeftmenuFragment getLeftmenuFragment() {
//        FragmentManager fm = getSupportFragmentManager();
//        LeftmenuFragment leftmenuFragment = (LeftmenuFragment) fm.findFragmentByTag(LEFTMENU_TAG);
//        return leftmenuFragment;
          return  (LeftmenuFragment) getSupportFragmentManager().findFragmentByTag(LEFTMENU_TAG);
    }

    /**
     * 得到正文的Fragment
     * @return
     */

    public ContentFragment getContentFragment() {
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(MAIN_CONTTENT_TAG);
    }
}
