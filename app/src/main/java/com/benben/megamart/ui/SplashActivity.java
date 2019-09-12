package com.benben.megamart.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MainActivity;
import com.benben.megamart.R;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.AdvertListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.StatusBarUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;

/**
 * 闪屏页面
 */
public class SplashActivity extends BaseActivity {

    private ImageView mIvSplash;
    private TextView tvSkip;
    private RelativeLayout rlytRootView;
    private int mNavigationBarHeight;
    private List<AdvertListBean> mAdvertImgList;
    private CountDownTimer mCountDownTimer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        mIvSplash = findViewById(R.id.iv_splash);
        tvSkip = findViewById(R.id.tv_skip);
        rlytRootView = findViewById(R.id.rlyt_root_view);

        startCountDown(5 * 1000);
        setMarginNavigationBar();
        getAdvertImageList();

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMainPager();
            }
        });

        mIvSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAdvertImgList != null && mAdvertImgList.size() > 0) {

                    if (mCountDownTimer != null) {
                        mCountDownTimer.cancel();
                        mCountDownTimer = null;
                    }

                    Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                    Intent mainIntent = new Intent(mContext, MainActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_URL, mAdvertImgList.get(0).getAdvert_url());
                    intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_TITLE, getResources().getString(R.string.active_details));
                    intent.putExtra(Constants.EXTRA_KEY_IS_URL, true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mainIntent);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void showAdvertImage(String img_path) {
        tvSkip.setVisibility(View.VISIBLE);
        //倒计时的时间 3s
        if (!StringUtils.isEmpty(img_path)) {
            //显示广告图片
           /* Bitmap imageThumbnail = ImageUtils.getImageThumbnail(img_path,
                    ScreenUtils.getScreenWidth(mContext), ScreenUtils.getScreenHeight(mContext));
            if (imageThumbnail != null && mIvSplash != null) {
                mIvSplash.setImageBitmap(imageThumbnail);
            }*/
            ImageUtils.getPic(img_path, mIvSplash, mContext, R.mipmap.banner_default);
        }
    }

    //获取广告图片地址
    private void getAdvertImageList() {
        BaseOkHttpClient.newBuilder()
                .addParam("machine", 1)//机型参数：1:安卓；2:苹果
                .url(NetUrlUtils.GET_ADVERT_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取开启app时图片广告----" + result);
                        String noteJson = JSONUtils.getNoteJson(result, "advert_list");
                        mAdvertImgList = JSONUtils.jsonString2Beans(noteJson, AdvertListBean.class);
                        if (mAdvertImgList != null && mAdvertImgList.size() > 0) {
                            showAdvertImage(mAdvertImgList.get(0).getAdvert_img());
                        } else {
                            showAdvertImage("");
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        //showAdvertImage("");
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        // showAdvertImage("");
                    }
                });

    }

    //倒计时
    private void startCountDown(long time_length) {

        mCountDownTimer = new CountDownTimer(time_length, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tvSkip.setText(getResources().getString(R.string.skip) + (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                Log.e(Constants.WHK_TAG, "onFinish: " );
                toMainPager();
            }
        };
        mCountDownTimer.start();
    }

    //跳转首页
    private void toMainPager() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }

        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();


    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        mNavigationBarHeight = StatusBarUtils.getNavigationBarHeight(mContext);
        //StatusBarUtils.hideBottomUIMenu(mContext);
    }

    //计算虚拟按键的高度 setmargin
    private void setMarginNavigationBar() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlytRootView.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, mNavigationBarHeight);
        rlytRootView.setLayoutParams(layoutParams);
    }
}
