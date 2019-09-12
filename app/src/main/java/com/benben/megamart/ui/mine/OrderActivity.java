package com.benben.megamart.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.R;
import com.benben.megamart.adapter.InvitationPagerAdapter;
import com.benben.megamart.frag.OrderFragment;
import com.benben.megamart.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/14
 * Time: 10:20
 * 我的订单
 */
public class OrderActivity extends BaseActivity {
    @BindView(R.id.tl_order)
    XTabLayout tlOrder;
    @BindView(R.id.vp_order)
    ViewPager vpOrder;

    private String mTabNames[] = new String[4];
    private List<Fragment> mFragments = new ArrayList<>();

    private InvitationPagerAdapter mAdapter;//tabLayout和viewPager联用的适配器

    private OrderFragment mOrderFragment;

    private int mIndex = 0;//传过来的下标

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    protected void initData() {

        mIndex=getIntent().getIntExtra("index",0);

        initTitle("" + getString(R.string.order_title));

        mTabNames[0] = getResources().getString(R.string.order_all);
        mTabNames[1] = getResources().getString(R.string.order_cash);
        mTabNames[2] = getResources().getString(R.string.order_payed);
        mTabNames[3] = getResources().getString(R.string.order_callback);

        Bundle bundleAll = new Bundle();
        bundleAll.putString("type", "0");
        mOrderFragment = new OrderFragment();
        mOrderFragment.setArguments(bundleAll);
        mFragments.add(mOrderFragment);

        Bundle bundleCash = new Bundle();
        bundleCash.putString("type", "5");
        mOrderFragment = new OrderFragment();
        mOrderFragment.setArguments(bundleCash);
        mFragments.add(mOrderFragment);

        Bundle bundlePay = new Bundle();
        bundlePay.putString("type", "2");
        mOrderFragment = new OrderFragment();
        mOrderFragment.setArguments(bundlePay);
        mFragments.add(mOrderFragment);

        Bundle bundleCallback = new Bundle();
        bundleCallback.putString("type", "3");
        mOrderFragment = new OrderFragment();
        mOrderFragment.setArguments(bundleCallback);
        mFragments.add(mOrderFragment);

        mAdapter = new InvitationPagerAdapter(getSupportFragmentManager(), mTabNames, mFragments);
        vpOrder.setAdapter(mAdapter);
        tlOrder.setupWithViewPager(vpOrder);
        tlOrder.setSelectedTabIndicatorColor(getResources().getColor(R.color.theme));
        vpOrder.setCurrentItem(mIndex);
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
