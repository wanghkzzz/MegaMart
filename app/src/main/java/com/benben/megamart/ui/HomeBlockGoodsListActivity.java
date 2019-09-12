package com.benben.megamart.ui;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.LazyBaseFragments;
import com.benben.megamart.R;
import com.benben.megamart.adapter.HomeBlockFragmentPagerAdapter;
import com.benben.megamart.adapter.HomeHotGoodsListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.HomeTopTabEntityBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.frag.HomeBlockGoodsListFragment;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.StatusBarUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Create by wanghk on 2019-05-29.
 * Describe:商品列表通用activity
 */
public class HomeBlockGoodsListActivity extends BaseActivity {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.xTablayout)
    XTabLayout xTablayout;
    @BindView(R.id.vp_goods_list)
    ViewPager vpGoodsList;

    //tab数据list
    private List<HomeTopTabEntityBean> mTabEntities = new ArrayList<>();
    //分类id
    private String mCateId;
    //板块id  2:热销；3:新品：4：推荐
    private int mBlockType;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_common_goods_list;
    }

    @Override
    protected void initData() {


        mBlockType = getIntent().getIntExtra(HomeHotGoodsListAdapter.HOME_HOT_GOODS_TYPE, 0);
        mCateId = getIntent().getStringExtra(Constants.EXTRA_KEY_CATE_ID);
        if (StringUtils.isEmpty(mCateId)) {
            mCateId = "0";
        }
        switch (mBlockType) {
            case 2:
                centerTitle.setText(getString(R.string.sell_well_products));
                break;
            case 3:
                centerTitle.setText(getString(R.string.new_products));
                break;
            case 4:
                centerTitle.setText(getString(R.string.recommend_products));
                break;

        }

        initTabEntity();


    }


    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }


    private void initTabEntity() {

        BaseOkHttpClient.newBuilder()
                .addParam("cate_id", mCateId)
                .url(NetUrlUtils.GET_NAVI_GOODS_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {

                        StyledDialogUtils.getInstance().dismissLoading();

                        String noteJson = JSONUtils.getNoteJson(result, "cate_list");
                        mTabEntities = JSONUtils.jsonString2Beans(noteJson, HomeTopTabEntityBean.class);
                        //增加全部商品的tab
                        HomeTopTabEntityBean hotBlockTabEntity = new HomeTopTabEntityBean();
                        hotBlockTabEntity.setCate_id(0);
                        hotBlockTabEntity.setCate_name(getString(R.string.all_goods));
                        mTabEntities.add(0, hotBlockTabEntity);
                        for (int i = 0; i < mTabEntities.size(); i++) {
                            xTablayout.addTab(xTablayout.newTab().setText(mTabEntities.get(i).getCate_name()));
                        }

                        initFragment();
                    }

                    @Override
                    public void onError(int code, String msg) {
                        ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.show(mContext, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                    }
                });
    }

    private void initFragment() {

        //添加fragment
        List<LazyBaseFragments> mFragmentList = new ArrayList<>();
        for (int i = 0; i < mTabEntities.size(); i++) {
            mFragmentList.add(HomeBlockGoodsListFragment.getInstance());

        }

        vpGoodsList.setAdapter(new HomeBlockFragmentPagerAdapter(getSupportFragmentManager(), mTabEntities, mFragmentList, mBlockType));
        xTablayout.setupWithViewPager(vpGoodsList);

    }

    @OnClick(R.id.rl_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
        }
    }


}
