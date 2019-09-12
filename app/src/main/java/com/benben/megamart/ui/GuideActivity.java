package com.benben.megamart.ui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.PreferenceProvider;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MainActivity;
import com.benben.megamart.R;
import com.benben.megamart.adapter.GuideAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.StatusBarUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;

/**
 * 引导页
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private GuideAdapter mGuideAdapter;

    private TextView tvSkip;
    private TextView tvEnter;

    private ImageView iv1, iv2, iv3;
    private List<String> mImageList;
    private RelativeLayout rlytRootView;
    private int mNavigationBarHeight;

    protected void initEvent() {
        tvSkip.setOnClickListener(this);
        tvEnter.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    tvEnter.setVisibility(View.VISIBLE);
                } else {
                    tvEnter.setVisibility(View.INVISIBLE);
                }
                if (position == 0) {
                    iv1.setImageResource(R.drawable.dot_selected);
                    iv2.setImageResource(R.drawable.dot_normal);
                    iv3.setImageResource(R.drawable.dot_normal);
                } else if (position == 1) {
                    iv1.setImageResource(R.drawable.dot_normal);
                    iv2.setImageResource(R.drawable.dot_selected);
                    iv3.setImageResource(R.drawable.dot_normal);
                } else if (position == 2) {
                    iv1.setImageResource(R.drawable.dot_normal);
                    iv2.setImageResource(R.drawable.dot_normal);
                    iv3.setImageResource(R.drawable.dot_selected);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_skip:
            case R.id.tv_enter:
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void initData() {

        PreferenceProvider preferenceProvider = new PreferenceProvider(mContext);
        boolean isFirst = (boolean) preferenceProvider.get(mContext,"is_first_open_app",true);

        if(isFirst){
            preferenceProvider.put(mContext,"is_first_open_app",false);
        }else {
            startActivity(new Intent(mContext,SplashActivity.class));
            finish();
            return;
        }
        rlytRootView = findViewById(R.id.rlyt_root_view);
        mViewPager = findViewById(R.id.view_guid);
        tvSkip = findViewById(R.id.tv_skip);
        tvEnter =  findViewById(R.id.tv_enter);
        iv1 =  findViewById(R.id.iv1);
        iv2 =  findViewById(R.id.iv2);
        iv3 =  findViewById(R.id.iv3);

        setMarginNavigationBar();

        getFirstAdvertImageList();


        initEvent();
    }

    private void getFirstAdvertImageList() {

        BaseOkHttpClient.newBuilder()
                .addParam("machine","1")//机型参数：1:安卓；2:苹果
                .url(NetUrlUtils.GET_FIRST_ADVERT_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取首次开启app时图片广告----" + result);
                        String noteJson = JSONUtils.getNoteJson(result, "advert_list");
                        mImageList = JSONUtils.jsonString2Beans(noteJson, String.class);
                        mGuideAdapter = new GuideAdapter(mContext, mImageList);
                        mViewPager.setAdapter(mGuideAdapter);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        toast(msg);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        toast(e.getMessage());
                    }
                });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        mNavigationBarHeight = StatusBarUtils.getNavigationBarHeight(mContext);

    }

    //计算虚拟按键的高度 setmargin
    private void setMarginNavigationBar() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlytRootView.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, mNavigationBarHeight);
        rlytRootView.setLayoutParams(layoutParams);
    }
}
