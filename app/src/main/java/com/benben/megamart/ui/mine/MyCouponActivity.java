package com.benben.megamart.ui.mine;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.R;
import com.benben.megamart.adapter.InvitationPagerAdapter;
import com.benben.megamart.frag.CouponFragment;
import com.benben.megamart.utils.StatusBarUtils;
import com.benben.megamart.utils.TabLayoutUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 我的代金券
 */
public class MyCouponActivity extends BaseActivity {
    @BindView(R.id.tl_coupon)
    XTabLayout tlCoupon;
    @BindView(R.id.vp_coupon)
    ViewPager vpCoupon;

    private String mTabNames[] = new String[3];
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private InvitationPagerAdapter mAdapter;//tabLayout和viewPager联用的适配器

    private CouponFragment mCouponFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_coupon;
    }

    @Override
    protected void initData() {
        initTitle("" + getString(R.string.coupon_my));

        mTabNames[0] = getResources().getString(R.string.coupon_no_use);
        mTabNames[1] = getResources().getString(R.string.coupon_used);
        mTabNames[2] = getResources().getString(R.string.coupon_cash);

        Bundle bundleNoUse = new Bundle();
        bundleNoUse.putString("type", "0");
        mCouponFragment = new CouponFragment();
        mCouponFragment.setArguments(bundleNoUse);
        mFragments.add(mCouponFragment);

        Bundle bundleUsed = new Bundle();
        bundleUsed.putString("type", "1");
        mCouponFragment = new CouponFragment();
        mCouponFragment.setArguments(bundleUsed);
        mFragments.add(mCouponFragment);

        Bundle bundleCash = new Bundle();
        bundleCash.putString("type", "2");
        mCouponFragment = new CouponFragment();
        mCouponFragment.setArguments(bundleCash);
        mFragments.add(mCouponFragment);

        mAdapter = new InvitationPagerAdapter(getSupportFragmentManager(), mTabNames, mFragments);
        vpCoupon.setAdapter(mAdapter);
        tlCoupon.setupWithViewPager(vpCoupon);
        tlCoupon.setSelectedTabIndicatorColor(getResources().getColor(R.color.theme));
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }

}
