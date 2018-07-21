package com.example.hm.menudatailpager.tabdetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hm.base.MenuDetailBasePager;
import com.example.hm.beijingnews.R;
import com.example.hm.domain.NewsCenterPagerBean;
import com.example.hm.domain.TabDatailPagerBean;
import com.example.hm.utils.CacheUtils;
import com.example.hm.utils.Constants;
import com.example.hm.utils.LogUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * 页签详细页面
 */
public class TabDetailPager extends MenuDetailBasePager {

    private NewsCenterPagerBean.DataBean.ChildrenBean childrenData;

    private String url;
    private String saveJson;

    private ViewPager viewpager;

    private TextView tv_title;

    private LinearLayout ll_point_group;

    private ListView listView;

    private int preposition = 0;

    private MyTabDetailPagerListAdapter adapter;

    /**
     * 顶部轮播图的数据·
     */
    private List<TabDatailPagerBean.DataBean.TopnewsBean> topnews;
    /**
     * 新闻列表数据集合
     */
    private List<TabDatailPagerBean.DataBean.NewsBean> news;


    public TabDetailPager(Context context) {
        super(context);
    }

    public TabDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenBean childrenData) {
        super(context);
        this.childrenData = childrenData;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.tabdetail_pager,null);
        listView = view.findViewById(R.id.listview);

        View topNewsView = View.inflate(context,R.layout.topnews,null);

        viewpager =  topNewsView.findViewById(R.id.viewpager);
        tv_title = topNewsView.findViewById(R.id.tv_title);
        ll_point_group = topNewsView.findViewById(R.id.ll_point_group);

        //把顶部轮播图以头的方式添加到ListView中
        listView.addHeaderView(topNewsView);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + childrenData.getUrl();
        //取出缓存数据
        saveJson = CacheUtils.getString(context,url);
        if (!TextUtils.isEmpty(saveJson)){
            //解析数据
            processData(saveJson);
        }

        LogUtil.e(childrenData.getTitle() + "的联网地址==" + url);

        //联网请求
        getDataFromNet();
    }

    /**
     * 解析数据，并显示
     * @param Json
     */
    private void processData(String Json) {
        //将json数据解析成javabean对象
        TabDatailPagerBean bean = parsedJson(Json);
        LogUtil.e(childrenData.getTitle() + "解析成功 " +bean.getData().getNews().get(0).getTitle());

        //顶部轮播图数据
        topnews = bean.getData().getTopnews();
        //设置viewpager的适配器
        viewpager.setAdapter(new TabDetailPagerTopNews());

        ll_point_group.removeAllViews(); // 移除所有的红点

        //动态添加红点的个数
        for(int i = 0; i< topnews.size(); i++){
            //根据图片的个数设置红点的个数。
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.point_selector);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(org.xutils.common.util.DensityUtil.dip2px(8),org.xutils.common.util.DensityUtil.dip2px(8));

            //设置默认的选中页面点
            if(i==0){
                imageView.setEnabled(true);
            }else {
                imageView.setEnabled(false);
                params.leftMargin = org.xutils.common.util.DensityUtil.dip2px(8);
            }

            imageView.setLayoutParams(params);
            ll_point_group.addView(imageView);
        }

        //监听这个ViewPager的滑动监听，在里面设置红点的
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener() );
        tv_title.setText(topnews.get(0).getTitle());

        //准备ListView对应的集合数据
        news = bean.getData().getNews();
        //设置ListView的适配器
        adapter = new MyTabDetailPagerListAdapter();
        listView.setAdapter(adapter);
    }

    class MyTabDetailPagerListAdapter extends BaseAdapter{

        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = View.inflate(context,R.layout.item_tabdetail_pager,null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
                viewHolder.tv_time = convertView.findViewById(R.id.tv_time);

                convertView.setTag(viewHolder);

            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据位置得到数据
            TabDatailPagerBean.DataBean.NewsBean newsData = news.get(position);
            //获取图片的URL地址
            String imageUrl = Constants.BASE_URL + newsData.getListimage();
            //联网解析图片
            x.image().bind(viewHolder.iv_icon,imageUrl);


            viewHolder.tv_title.setText(newsData.getTitle());

            //设置更新
            viewHolder.tv_time.setText(newsData.getPubdate());

            return convertView;
        }
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
    }

    /**
     * x选中ViewPager的监听适配器
     */

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //1.设置文本
            tv_title.setText(topnews.get(position).getTitle());
            //2.让页面的红点高亮
                //把之前的设置为灰色
                ll_point_group.getChildAt(preposition).setEnabled(false);
                //把当前的点设置为红色
                ll_point_group.getChildAt(position).setEnabled(true);

                preposition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * Gosn解析数据
     * @param json
     * @return
     */
    private TabDatailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json,TabDatailPagerBean.class);
    }

    public void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //缓存数据
                CacheUtils.putString(context,url,result);

                //LogUtil.e(childrenData.getTitle()+"页面请求数据成功=="+result);
                //解析和处理显示数据
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(childrenData.getTitle()+"页面请求数据失败=="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //LogUtil.e(childrenData.getTitle()+"页面请求数据onCancelled=="+cex.getMessage());
            }

            @Override
            public void onFinished() {
                //LogUtil.e(childrenData.getTitle()+"页面请求数据onFinished==");
            }
        });
    }

    /**
     * 轮播图的ViewPager的适配器
     */
    class TabDetailPagerTopNews extends PagerAdapter{
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            //设置背景图
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //联网请求图片
            //获取topnewsBean对象，用来得到图片的Url地址
            TabDatailPagerBean.DataBean.TopnewsBean topnewsBean = topnews.get(position);
            String imageUrl = Constants.BASE_URL + topnewsBean.getTopimage();
            //用xUtil3联网解析图片
            x.image().bind(imageView,imageUrl);
            //添加到容器中
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
