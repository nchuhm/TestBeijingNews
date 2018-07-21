package com.example.hm.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.hm.Activity.MainActivity;
import com.example.hm.base.BaseFragment;
import com.example.hm.base.BasePager;
import com.example.hm.beijingnews.R;
import com.example.hm.pager.GovaffairPager;
import com.example.hm.pager.HomePager;
import com.example.hm.pager.NewsCenterPager;
import com.example.hm.pager.SettingPager;
import com.example.hm.pager.SmartServicePager;
import com.example.hm.utils.LogUtil;
import com.example.hm.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/*
 * 正文内容Fragment
 */
public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.viewpager)
    private NoScrollViewPager viewpager;
    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;

    //装五个子页面
    private ArrayList<BasePager> basePagers;
    private NewsCenterPager newsCenterPager;


    @Override
    public View initView() {
        LogUtil.e("正文Fragment视图被初始化了");
        View view = View.inflate(context, R.layout.content_fragment,null);
        //1.把视图注入到框架中，让ContentFragment.this 和 view 关联起来
        x.view().inject(ContentFragment.this,view);
        return view;

    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("正文Fragment数据被初始化了");


        //初始化五个页面，并且放入集合中
        basePagers = new ArrayList<>();
        basePagers.add(new HomePager(context)); //主页面
        basePagers.add(new NewsCenterPager(context));  //新闻页面
        basePagers.add(new SmartServicePager(context)); //智慧服务页面
        basePagers.add(new GovaffairPager(context)); //政要指南页面
        basePagers.add(new SettingPager(context)); // 设置中心页面

        //设置ViewPager的适配器
        viewpager.setAdapter( new ContentFragmentAdapter());

        //RadioGroup的选中状态改变的事件
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        //监听某个页面被选中，初始对应页面数据
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        //设置默认选中首页
        rg_main.check(R.id.rb_home);

        //默认初始化首页
        basePagers.get(0).initData();

        //设置SlidingMenu不可以滑动
        isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);

    }

    /**
     * 得到新闻中心
     * @return
     */
    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) basePagers.get(1);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当某个页面被选中的时候回调这个方法
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            basePagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        /**
         * 被选中的RidioButton的id
         * @param radioGroup
         * @param checkedId  被选中对的按键的id
         */
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            switch (checkedId){
                case R.id.rb_home:
                    viewpager.setCurrentItem(0,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_newscenter:
                    viewpager.setCurrentItem(1,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                case R.id.rb_smartservice_home:
                    viewpager.setCurrentItem(2,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_govaffair_home:
                    viewpager.setCurrentItem(3,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_setting:
                    viewpager.setCurrentItem(4,false);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
            }
        }
    }

    /**
     * 根据传入的参数设置是否让SlidingMenu可以滑动
     * @param TOUCHMODE_FULLSCREEN
     */
    private void isEnableSlidingMenu(int TOUCHMODE_FULLSCREEN) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(TOUCHMODE_FULLSCREEN);
    }

    class ContentFragmentAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
           BasePager basePager =  basePagers.get(position);  //各个页面的实例
            View rootView = basePager.rootview;
            //调用各个页面的initData方法
            //basePager.initData();
            container.addView(rootView);
            return rootView;
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
