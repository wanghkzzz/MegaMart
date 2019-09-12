package com.benben.megamart;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.ActivityManagerUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.utils.StatusBarUtils;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/9/30.
 * 模板Activity的基类 对于简单的页面不使用mvp  Activity直接继承BaseActivity 如果需要使用mvp 则继承MvpBaseActivity
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Activity mContext;
    Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActivityManagerUtils.add(this);
        this.mContext = this;

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && StatusBarUtils.isTranslucentOrFloating(this)) {
            boolean result = fixOrientation();
            Log.i("wanghk", "onCreate fixOrientation when Oreo, result = " + result);
        }

        setStatusBar();
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this, this);
        initData();

    }

    //如果是8.0  并且是透明主题 则不进行横竖屏切换
    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && StatusBarUtils.isTranslucentOrFloating(this)) {
            Log.i("wanghk", "avoid calling setRequestedOrientation when Oreo.");
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }


    private boolean fixOrientation(){
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo)field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected abstract int getLayoutId();

    protected abstract void initData();

    /**
     * 设置标题
     *
     * @param title 标题名字
     */
    protected void initTitle(String title) {
        TextView tvTitle = findViewById(R.id.center_title);
        tvTitle.setText("" + title);
        RelativeLayout rlBack = findViewById(R.id.rl_back);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManagerUtils.remove(this);
                finish();
            }
        });
    }

    protected void setStatusBar() {

    }

    public void showProgress(){

    }

    public void closeProgress(){

    }

    public void toast(String strMsg){
        ToastUtils.show(this,strMsg);
    }

    public void toast(int resid){
        ToastUtils.show(this,resid);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
