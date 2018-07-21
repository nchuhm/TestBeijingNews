package com.example.hm.menudatailpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hm.Activity.MainActivity;
import com.example.hm.base.MenuDetailBasePager;
import com.example.hm.beijingnews.R;
import com.example.hm.domain.NewsCenterPagerBean;
import com.example.hm.menudatailpager.tabdetailpager.TabDetailPager;
import com.example.hm.utils.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻详情页面
 */
public class NewsMennnuDatailPager extends MenuDetailBasePager {

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    @ViewInject(R.id.tabPageIndicator)
    private TabPageIndicator tabPageIndicator;

    @ViewInject(R.id.ib_tab_next)
    private ImageButton ib_tab_next;


    //解析后的children数据，装载该数据的集合
    private List<NewsCenterPagerBean.DataBean.ChildrenBean> childrens;

    //装载多个页签详情页面的ViewPager集合
    private ArrayList<TabDetailPager> tabDetailPagers;


    public NewsMennnuDatailPager(Context context, NewsCenterPagerBean.DataBean dataBean) {
        super(context);
        childrens = dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.newsmenu_detail_pager,null);
        //1.把视图注入到框架中，NewsMennnuDatailPager.this 和 view 关联起来
        x.view().inject(NewsMennnuDatailPager.this,view);

        ib_tab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻详情页面被初始化了");
        //准备数据，然后设置适配器
        tabDetailPagers = new ArrayList<>();
        for (int i=0;i<childrens.size();i++){
            tabDetailPagers.add(new TabDetailPager(context,childrens.get(i)));
        }
        //设置适配器
        viewPager.setAdapter(new MyNewsMenuDetailPagerAdapter());
        //设置ViewPager适配器之后 ViewPager与TabPageIndicator关联

        //以后监听页面的变化用TabPageIndicator
        tabPageIndicator.setViewPager(viewPager);
        //tabPageIndicator.getParent().requestDisallowInterceptTouchEvent(true);

        tabPageIndicator.setOnPageChangeListener(new MyOnPageChangeListener());

    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(position == 0){
                //Slidingmenu可以全屏滑动
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
            }else{
                //不可以滑动
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void isEnableSlidingMenu(int TOUCHMODE_FULLSCREEN) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(TOUCHMODE_FULLSCREEN);
    }

    /**
     * 详情页面的ViewPager的适配器
     * tabDetailPagers 包含要创建的ViewPager对象
     */
    class MyNewsMenuDetailPagerAdapter extends PagerAdapter{

        @Override
        public CharSequence getPageTitle(int position) {
            return childrens.get(position).getTitle();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            View rootview = tabDetailPager.rootView;
            tabDetailPager.initData();//初始化数据
            container.addView(rootview);
            return rootview;
    }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);  //一定要移除之前的方法
        }

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
