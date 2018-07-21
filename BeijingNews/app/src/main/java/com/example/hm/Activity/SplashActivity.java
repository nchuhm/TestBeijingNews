package com.example.hm.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.hm.beijingnews.R;
import com.example.hm.utils.CacheUtils;

public class SplashActivity extends Activity {

    public static final String START_MAIN = "start_main";
    private RelativeLayout rl_splahs_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rl_splahs_root = (RelativeLayout) findViewById(R.id.rl_splahs_root);

        //渐变，缩放，旋转动画
        AlphaAnimation aa = new AlphaAnimation(0,1);
    //    aa.setDuration(500); //持续播放时间
        aa.setFillAfter(true);

        ScaleAnimation sa = new ScaleAnimation(0,1,0,1,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
    //    sa.setDuration(500);
        sa.setFillAfter(true);

        RotateAnimation ra = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
    //    ra.setDuration(500);
        ra.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.addAnimation(aa);
        set.addAnimation(sa);
        set.addAnimation(ra);
        set.setDuration(1500);

        rl_splahs_root.startAnimation(set);

        set.setAnimationListener(new MyAnimationListener());
    }

    class MyAnimationListener implements Animation.AnimationListener{
        //动画开始播放
        @Override
        public void onAnimationStart(Animation animation) {

        }
        //动画结束播放
        @Override
        public void onAnimationEnd(Animation animation) {
            //判断是否进入过主页面，如果进入过主页面，直接进入，没有则进入
            Intent intent;
            boolean isStratMain = CacheUtils.getBoolean(SplashActivity.this,START_MAIN);
            if(isStratMain){
                intent = new Intent(SplashActivity.this,MainActivity.class);
            }else{
                intent = new Intent(SplashActivity.this,GuideActivity.class);
            }
            startActivity(intent);
            //关闭页面
            finish();

        }
        //动画重复播放时回调
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
