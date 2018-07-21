package com.example.hm.Activity;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.hm.beijingnews.R;
import com.example.hm.utils.CacheUtils;
import com.example.hm.utils.DensityUtil;

import java.util.ArrayList;

public class GuideActivity extends Activity {

    private static final String TAG = GuideActivity.class.getSimpleName();
    private ViewPager viewPager;
    private Button btn_start_main;
    private LinearLayout ll_point_group;
    private ArrayList<ImageView> imageViews;
    private ImageView iv_red_point;
    /**
     * 两点的间距
     */
    private int leftmax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        viewPager = findViewById(R.id.viewpager);
        btn_start_main = findViewById(R.id.btn_start_main);
        ll_point_group = findViewById(R.id.ll_point_group);
        iv_red_point = findViewById(R.id.iv_red_point);

        //准备图片
        int ids[] = new int[]{
          R.drawable.guide_1,
          R.drawable.guide_2,
          R.drawable.guide_3
        };

        int widthdpi = DensityUtil.dip2px(this, 10);


        imageViews = new ArrayList<>();
        for (int i = 0;i < ids.length; i++){
            ImageView imageView = new ImageView(this);
            //设置背景的方式
            imageView.setBackgroundResource(ids[i]);
            //添加到ArrayList中
            imageViews.add(imageView);
            //创建点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_normal);
            //添加到线性布局
            /**
             * 单位是像素，还要适配
             * 把单位当成dp转成对应的像素
             */
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthdpi,widthdpi);  //包含了布局的信息。
            if(i!=0){
                params.leftMargin = widthdpi;
            }
            point.setLayoutParams(params);
            ll_point_group.addView(point);
        }
        //设置ViewPager的适配器
        viewPager.setAdapter(new MyPagerAdapter());
        //根据View的生命周期，当视图执行到onLayout或者onDraw的时候，视图的高宽，边距都有了
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        //得到屏幕滑动的百分比
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        //设置按钮的点击事件
        btn_start_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1，保存记录参数,进入过主页面的参数
                CacheUtils.putBoolean(GuideActivity.this,SplashActivity.START_MAIN,true);
                //2，跳转到主页面
                Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);
                //3，关闭引导页面
                finish();
            }
        });
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        /**
         * 页面滑动的时候回调这个方法
         * @param position  当前页面的位置
         * @param positionOffset 页面滑动的百分比
         * @param positionOffsetPixels 滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //两点间移动的距离 = 屏幕滑动百分比 * 间距
            int leftmargin = (int) (positionOffset * leftmax);
         //   Log.e(TAG ,"position == "+position+"+positionOffset=="+positionOffset+"positionOffsetPixels=="+positionOffsetPixels);
            leftmargin = (int) (position * leftmax + (positionOffset * leftmax));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
            params.leftMargin = leftmargin;
            iv_red_point.setLayoutParams(params);
        }

        /**
         * 当页面被选中时候回调这个方法
         * @param position 被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            if(position == imageViews.size() -1){
                //最后一个页面
                btn_start_main.setVisibility(View.VISIBLE);
            }else{
                //其他页面
                btn_start_main.setVisibility(View.GONE);
            }
        }

        /**
         * 当页面滑动状态发生改变的时候   抓住， 放手 ，停止
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener{

        @Override
        public void onGlobalLayout() {
            //执行不止一次
            iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(MyOnGlobalLayoutListener.this);
            //计算间距 = 第一个点距离左边的距离 - 第零个点距离左边的距离
            leftmax = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();
        }
    }

    class MyPagerAdapter extends PagerAdapter{
        /**
         * 返回数据的总个数
         * @return
         */
        @Override
        public int getCount() {
            return imageViews.size();
        }

        /**
         *
         * @param container 容器
         * @param position 位置
         * @return 返回和创建当前页面有关的值
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            //添加到容器中
            container.addView(imageView);
            return imageView;
        }

        /**
         *
         * @param view 当前创建的视图
         * @param object  上面instantiateItem返回的结果值
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 销毁页面
         * @param container  容器ViewPager
         * @param position 位置
         * @param object    要销毁的页面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);  去掉这个方法
            container.removeView((View) object);
        }
    }
}
