package com.benben.megamart.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 *
 * tabLayout与ViewPager联用使用的adapter
 */

public class InvitationPagerAdapter extends FragmentStatePagerAdapter {

    private String[] mTabName;
    private List<Fragment> mFragments;

    public InvitationPagerAdapter(FragmentManager fm, String[] tabName, List<Fragment> fragments) {
        super(fm);
        this.mTabName = tabName;
        this.mFragments = fragments;

    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabName[position];
    }

}
