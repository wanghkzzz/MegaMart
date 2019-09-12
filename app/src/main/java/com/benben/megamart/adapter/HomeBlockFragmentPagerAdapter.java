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
 * Create by wanghk on 2019-06-14.
 * Describe:首页二级页面viewpager适配器
 */
public class HomeBlockFragmentPagerAdapter extends FragmentPagerAdapter {

    //tab数据
    private List<HomeTopTabEntityBean> mDatas;

    private List<LazyBaseFragments> mFragments;
    private int mBlockType;

    public HomeBlockFragmentPagerAdapter(FragmentManager fm, List<HomeTopTabEntityBean> mDatas, List<LazyBaseFragments> fragments, int blockType) {
        super(fm);
        this.mDatas = mDatas;
        this.mFragments = fragments;
        this.mBlockType = blockType;
    }

    @Override
    public Fragment getItem(int position) {
        //传入分类id
        LazyBaseFragments lazyBaseFragments = mFragments.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_KEY_CATE_ID, mDatas.get(position).getCate_id());
        bundle.putInt(Constants.EXTRA_KEY_BLOCK_TYPE, mBlockType);
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
