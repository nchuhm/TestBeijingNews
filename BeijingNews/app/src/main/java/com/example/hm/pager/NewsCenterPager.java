package com.example.hm.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.hm.Activity.MainActivity;
import com.example.hm.base.BasePager;
import com.example.hm.base.MenuDetailBasePager;
import com.example.hm.domain.NewsCenterPagerBean;
import com.example.hm.domain.NewsCenterPagerBean2;
import com.example.hm.fragment.LeftmenuFragment;
import com.example.hm.menudatailpager.InteracMennnuDatailPager;
import com.example.hm.menudatailpager.NewsMennnuDatailPager;
import com.example.hm.menudatailpager.PhotosMennnuDatailPager;
import com.example.hm.menudatailpager.TopicMennnuDatailPager;
import com.example.hm.utils.CacheUtils;
import com.example.hm.utils.Constants;
import com.example.hm.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻中心
 */
public class NewsCenterPager extends BasePager {

    private List<NewsCenterPagerBean.DataBean> data;

    private ArrayList<MenuDetailBasePager> menuDetailBasePagers;

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻中心数据被初始化了");
        ib_menu.setVisibility(View.VISIBLE);
        //设置标题
        tv_title.setText("新闻中心");
        //联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //把子视图添加到BasePager的Frgment中
        fl_content.addView(textView);
        //绑定数据
        textView.setText("新闻中心内容");
        //得到缓存的数据
        String saveJson = CacheUtils.getString(context,Constants.NEWSCENTER_PAGER_URL); // ""
        if (!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }
        getDataFromNet();
    }

    public void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL) ;
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("使用xUtils3联网请请求成功"+result);

                CacheUtils.putString(context,Constants.NEWSCENTER_PAGER_URL,result);

                //设置适配器
                processData(result);
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("使用xUtils3联网请请求失败"+ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("使用xUtils3-onCancelled"+cex);
            }

            @Override
            public void onFinished() {
                LogUtil.e("使用xUtils3-onFinished");
            }
        });
    }

    /**
     * 解析json数据和显示数据
     * @param json
     */
    private void processData(String json) {
        //创建解析对象
        NewsCenterPagerBean bean = parsedJson(json);
//        NewsCenterPagerBean2 bean2 = parsedJson2(json);

        //解析测试
        String title = bean.getData().get(0).getChildren().get(1).getTitle();
        LogUtil.e("gson解析json是数据成功"+title);

//        //手动解析测试
//        String title2 = bean2.getData().get(0).getChildren().get(1).getTitle();
//        LogUtil.e("手动解析json是数据成功"+title2);

        //获取解析后的标题数据
        data = bean.getData(); //获取标题

        //获取到左侧Fragment对象
        MainActivity mainActivity = (MainActivity) context;
        LeftmenuFragment leftmenuFragment = mainActivity.getLeftmenuFragment();

        //添加详情页面
        menuDetailBasePagers = new ArrayList<>();
        menuDetailBasePagers.add(new NewsMennnuDatailPager(context,data.get(0))); //新闻详情
        menuDetailBasePagers.add(new TopicMennnuDatailPager(context)); //专题详情页面
        menuDetailBasePagers.add(new PhotosMennnuDatailPager(context)); //图组详情页面
        menuDetailBasePagers.add(new InteracMennnuDatailPager(context)); //互动详情页面

        //把数据传递给左侧菜单
        leftmenuFragment.setData(data);


    }

    /**
     * 手动解析json数据
     * @param json
     * @return
     */
    private NewsCenterPagerBean2 parsedJson2(String json) {

        NewsCenterPagerBean2 bean2 = new NewsCenterPagerBean2();
        try {
            JSONObject object = new JSONObject(json);
            int retcode = object.optInt("retcode");

            bean2.setRetcode(retcode); //retcode字段解析成功，装载retcode

            JSONArray data = object.optJSONArray("data");
            if(data != null && data.length() > 0){

                List<NewsCenterPagerBean2.DetailPagerData> detailPagerDatas = new ArrayList<>();
                //设置列表数据
                bean2.setData(detailPagerDatas);

                //循环解析每条data数据
                for (int i=0;i < data.length(); i++){
                    JSONObject jsonObject = (JSONObject) data.opt(i);

                    //创建DetailPagerData的对象
                    NewsCenterPagerBean2.DetailPagerData detailPagerData = new NewsCenterPagerBean2.DetailPagerData();
                    detailPagerDatas.add(detailPagerData);


                    //解析数据+传入数据到NewsCenterPagerBean2中
                    int id = jsonObject.optInt("id");
                    detailPagerData.setId(id);

                    int type = jsonObject.optInt("type");
                    detailPagerData.setType(type);

                    String title = jsonObject.optString("title");
                    detailPagerData.setTitle(title);

                    String url = jsonObject.optString("url");
                    detailPagerData.setUrl(url);

                    String url1 = jsonObject.optString("url1");
                    detailPagerData.setUrl1(url1);

                    String dayurl = jsonObject.optString("dayurl");
                    detailPagerData.setDayurl(dayurl);

                    String excurl = jsonObject.optString("excurl");
                    detailPagerData.setExcurl(excurl);

                    String weekurl = jsonObject.optString("weekurl");
                    detailPagerData.setWeekurl(weekurl);

                    JSONArray children = jsonObject.optJSONArray("children");
                    if (children != null && children.length() > 0){

                        List<NewsCenterPagerBean2.DetailPagerData.ChildrenData> childrenDatas = new ArrayList<>();

                        //设置集合
                        detailPagerData.setChildren(childrenDatas);

                        for (int j = 0; j < children.length() ; j++){
                            JSONObject childrenitem = (JSONObject) data.opt(j);

                            //创建对象NewsCenterPagerBean2.DetailPagerData.ChildrenData，把每一个实例添加到集合中
                            NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData = new NewsCenterPagerBean2.DetailPagerData.ChildrenData();
                            //把数据添加到集合中
                            childrenDatas.add(childrenData);

                            //解析数据 + 传入数据到bean2中
                            int childid = childrenitem.optInt("id");
                            childrenData.setId(childid);

                            int childtype = childrenitem.optInt("type");
                            childrenData.setType(childtype);

                            String childtitle = childrenitem.optString("title");
                            childrenData.setTitle(childtitle);

                            String childurl = childrenitem.optString("url");
                            childrenData.setUrl(childurl);
                        }
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return bean2;
    }

    /**
     * 解析json数据 1:使用系统的Api解析 2.使用第三方框架解析json数据，比如，Gson.fastjson
     * @param json
     * @return
     */

    private NewsCenterPagerBean parsedJson(String json) {
//        Gson gson = new Gson();
//        NewsCenterPagerBean bean = gson.fromJson(json,NewsCenterPagerBean.class);
        return new Gson().fromJson(json,NewsCenterPagerBean.class);
    }

    /**
     * 根据位置切换详情页面，切换页面之后做具体的操作
     * @param i  当前选中的页面号
     */
    public void swichPager(int i) {
        //1.设置标题
        tv_title.setText(data.get(i).getTitle());
        //2.移除之前的内容
        fl_content.removeAllViews(); //移除之前的视图
        //3.添加新内容
        MenuDetailBasePager menuDetailBasePager = menuDetailBasePagers.get(i); //获取选择的页面
        View rootView = menuDetailBasePager.rootView; //获取那个页面的视图
        menuDetailBasePager.initData(); //对这个页面初始化数据
        fl_content.addView(rootView);
    }
}
