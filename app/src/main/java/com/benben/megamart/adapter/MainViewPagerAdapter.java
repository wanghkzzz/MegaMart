package com.benben.megamart.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.benben.megamart.LazyBaseFragments;

import java.util.List;

/**
 * Create by wanghk on 2019-05-27.
 * Describe:主页面viewpager适配器
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private List<LazyBaseFragments> fragments;
    private FragmentManager fragmentManager;

    public MainViewPagerAdapter(FragmentManager fm, List<LazyBaseFragments> fragmentList) {
        super(fm);
        this.fragments = fragmentList;
        this.fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
