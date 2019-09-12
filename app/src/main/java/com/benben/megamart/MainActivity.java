package com.benben.megamart;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.benben.megamart.adapter.MainViewPagerAdapter;
import com.benben.megamart.bean.TabEntityBean;
import com.benben.megamart.frag.MainCartFragment;
import com.benben.megamart.frag.MainCategoryFragment;
import com.benben.megamart.frag.MainDiscountFragment;
import com.benben.megamart.frag.MainHomeFragment;
import com.benben.megamart.frag.MainMineFragment;
import com.benben.megamart.utils.RxBus;
import com.benben.megamart.utils.StatusBarUtils;
import com.benben.megamart.widget.NoScrollViewPager;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Create by wanghk on 2019-05-27.
 * Describe:主页面
 */
public class MainActivity extends BaseActivity {


    @BindView(R.id.vp_main)
    NoScrollViewPager vpMain;
    @BindView(R.id.tab_main)
    CommonTabLayout tabMain;
    //tab数据
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private MainCartFragment shopCartFragment;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {


        initTabData();


        List<LazyBaseFragments> mFragmentList = new ArrayList<>();
        MainHomeFragment homeFragment = MainHomeFragment.getInstance();
        MainCategoryFragment categoryFragment = MainCategoryFragment.getInstance();
        MainDiscountFragment discountFragment = MainDiscountFragment.getInstance();
        shopCartFragment = MainCartFragment.getInstance();
        MainMineFragment mineFragment = MainMineFragment.getInstance();
        mFragmentList.add(homeFragment);
        mFragmentList.add(categoryFragment);
        mFragmentList.add(discountFragment);
        mFragmentList.add(shopCartFragment);
        mFragmentList.add(mineFragment);
        vpMain.setNoScroll(true);
        vpMain.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(), mFragmentList));

        tabMain.setTabData(mTabEntities);

        tabMain.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vpMain.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                tabMain.setCurrentTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        RxBus.getInstance().toObservable(Integer.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                        switch (integer) {

                            case 0:
                                vpMain.setCurrentItem(0);
                                break;
                            case 2:
                                vpMain.setCurrentItem(3);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //初始化tab数据
    private void initTabData() {
        mTabEntities.add(new TabEntityBean(getResources().getString(R.string.main_home), R.mipmap.tab_home_pre, R.mipmap.icon_shouye));
        mTabEntities.add(new TabEntityBean(getResources().getString(R.string.main_category), R.mipmap.tab_fenlei_pre, R.mipmap.tab_fenlei));
        mTabEntities.add(new TabEntityBean(getResources().getString(R.string.main_discount), R.mipmap.tab_youhui_pre, R.mipmap.tab_youhui));
        mTabEntities.add(new TabEntityBean(getResources().getString(R.string.main_shop_cart), R.mipmap.tab_gouwuche_pre, R.mipmap.tab_gouwuche));
        mTabEntities.add(new TabEntityBean(getResources().getString(R.string.main_mine), R.mipmap.tab_wodepre, R.mipmap.tab_wode));

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
        if (requestCode == 101 && resultCode == 200) {
            shopCartFragment.initData();
        }
    }


    private long mPressedTime = 0;

    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间

        if ((mNowTime - mPressedTime) > 2000) {//比较两次按键时间差
            Toast.makeText(this, getResources().getString(R.string.press_again_exit_app), Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        } else {//退出程序
            this.finish();
            System.exit(0);
        }
    }


}
