package com.example.hm.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hm.Activity.MainActivity;
import com.example.hm.base.BaseFragment;
import com.example.hm.beijingnews.R;
import com.example.hm.domain.NewsCenterPagerBean;
import com.example.hm.pager.NewsCenterPager;
import com.example.hm.utils.DensityUtil;
import com.example.hm.utils.LogUtil;

import java.util.List;

/*
 * 左侧菜单的Fragment
 */
public class LeftmenuFragment extends BaseFragment {

    private List<NewsCenterPagerBean.DataBean> data;
    private ListView listView;
    private LeftmenuFragmentAdapter leftmenuFragmentAdapter;
    private int prePosition;

    @Override
    public View initView() {
        LogUtil.e("左侧视图被初始化了");
        listView = new ListView(context);
        listView.setPadding(0, DensityUtil.dip2px(context,40),0,0);
        listView.setDividerHeight(0); //设置分割线的高度
        listView.setCacheColorHint(Color.TRANSPARENT); //设置ListView透明
        //设置按下ListView的item不变色
        listView.setSelection(android.R.color.transparent); //透明

        //设置item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //1，点击的位置变成红色
                prePosition = i;
                leftmenuFragmentAdapter.notifyDataSetChanged();

                //2.把左侧菜单关闭
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();

                //3.进入到对应的详情页面：新闻详情页面，专题详情页面，图组详情页面，互动详情页面

                swichWhatPager(prePosition);
            }
        });
        return listView;

    }

    /**
     * 根据位置切换不同的页面
     * @param i
     */

    private void swichWhatPager(int i) {
        MainActivity mainActivity = (MainActivity) context;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        newsCenterPager.swichPager(i);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("左侧菜单数据被初始化了");

    }

    /**
     * 接收数据
     * @param data
     */
    public void setData(List<NewsCenterPagerBean.DataBean> data) {
        this.data = data;
        for(int i = 0; i < data.size(); i++){
            LogUtil.e("title" + data.get(i).getTitle());
        }

        //设置适配器，因为这里一定有数据
        leftmenuFragmentAdapter = new LeftmenuFragmentAdapter();
        listView.setAdapter(leftmenuFragmentAdapter);

        //设置默认的页面
        swichWhatPager(prePosition);
    }

    class LeftmenuFragmentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView textView = (TextView) View.inflate(context, R.layout.item_leftmenu,null);
            textView.setText(data.get(i).getTitle());
            textView.setEnabled(i == prePosition);
            return textView;
        }
    }
}
