package com.benben.megamart.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.benben.megamart.LazyBaseFragments;
import com.benben.megamart.bean.HomeTopTabEntityBean;
import com.benben.megamart.config.Constants;

import java.util.List;

/**
 * Create by wanghk on 2019-05-27.
 * Describe:首页viewpager适配器
 */
public class HomeTabViewPagerAdapter extends FragmentPagerAdapter {

    //tab数据
    private List<HomeTopTabEntityBean> mDatas;

    private List<LazyBaseFragments> mFragments;

    public HomeTabViewPagerAdapter(FragmentManager fm, List<HomeTopTabEntityBean> mDatas, List<LazyBaseFragments> fragments) {
        super(fm);
        this.mDatas = mDatas;
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        //传入分类id
        LazyBaseFragments lazyBaseFragments = mFragments.get(position);
        Bundle bundle = new Bundle();
        int cate_id = mDatas.get(position).getCate_id();
        if(cate_id == 0){
            bundle.putString(Constants.EXTRA_KEY_CATE_ID,"");
        }else {
            bundle.putString(Constants.EXTRA_KEY_CATE_ID,String.valueOf(cate_id));
        }

        lazyBaseFragments.setArguments(bundle);
        return lazyBaseFragments;
    }

    @Override
    public int getCount() {
        return mDatas.size() == mFragments.size() ? mFragments.size() : 0;
    }

    /**
     * 重写此方法，返回TabLayout的内容
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mDatas.get(position).getCate_name();
    }
}
