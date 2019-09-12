package com.benben.megamart.frag;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.LazyBaseFragments;
import com.benben.megamart.R;
import com.benben.megamart.adapter.HomeBannerViewpagerAdapter;
import com.benben.megamart.adapter.HomeHotGoodsListAdapter;
import com.benben.megamart.adapter.HomeMenuListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.HomeAdvertInfoBean;
import com.benben.megamart.bean.HomeBannerImageBean;
import com.benben.megamart.bean.HomeHotGoodsInfoBean;
import com.benben.megamart.bean.HomeNavigationMenuBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.ui.CommonWebViewActivity;
import com.benben.megamart.widget.ViewPagerGallyPageTransformer;
import com.lwj.widget.viewpagerindicator.ViewPagerIndicator;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tmall.ultraviewpager.UltraViewPager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Create by wanghk on 2019-05-29.
 * Describe:商品首页热销商品通用fragment
 */
public class HomeHotGoodsFragment extends LazyBaseFragments {


    @BindView(R.id.vp_banner)
    UltraViewPager vpBanner;
    @BindView(R.id.vp_indicator)
    ViewPagerIndicator vpIndicator;
    @BindView(R.id.rlv_goods_category)
    RecyclerView rlvGoodsCategory;
    @BindView(R.id.iv_advert_info)
    ImageView ivAdvertInfo;
    @BindView(R.id.rlv_hot_goods)
    RecyclerView rlvHotGoods;
    @BindView(R.id.stf_layout)
    SmartRefreshLayout stfLayout;
    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;
    //banner 图片list
    private List<HomeBannerImageBean> mBannerImageList = new ArrayList<>();
    //轮播图的adapter
    private HomeBannerViewpagerAdapter mBannerViewPagerAdapter;
    //热销商品的adapter
    private HomeHotGoodsListAdapter mHomeHotGoodsListAdapter;
    //热销商品信息的list
    private List<HomeHotGoodsInfoBean> mHomeHotGoodsInfoList = new ArrayList<>();
    //首页菜单列表list
    List<HomeNavigationMenuBean> mHomeMenuList = new ArrayList<>();
    //菜单列表adapter
    private HomeMenuListAdapter mHomeMenuListAdapter;
    //分类id
    private String mCateId = "";
    //是否为热销：1:是，0:否
    int isHot = 1;
    //广告信息bean
    private HomeAdvertInfoBean mAdvertInfoBean;
    //是否刷新
    private boolean isRefreshing = false;


    public static HomeHotGoodsFragment getInstance() {
        HomeHotGoodsFragment sf = new HomeHotGoodsFragment();
        return sf;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_home_hot_goods, null);
        return mRootView;
    }

    @Override
    public void initView() {

        mCateId = getArguments().getString(Constants.EXTRA_KEY_CATE_ID);
        isHot = StringUtils.isEmpty(mCateId) ? 1 : 0;
        //设置 Header 背景为透明
        stfLayout.setPrimaryColorsId(R.color.transparent, R.color.text_white);
        stfLayout.setRefreshHeader(new ClassicsHeader(mContext));
        //设置 Footer 为 球脉冲 样式
        stfLayout.setRefreshFooter(new ClassicsFooter(mContext));
        //预加载3个
        // vpBanner.setOffscreenPageLimit(3);

        vpBanner.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        //UltraPagerAdapter 绑定子view到UltraViewPager
        mBannerViewPagerAdapter = new HomeBannerViewpagerAdapter(mBannerImageList, mContext);
        vpBanner.setAdapter(mBannerViewPagerAdapter);
        vpBanner.setPageTransformer(true, new ViewPagerGallyPageTransformer());
        vpBanner.setMultiScreen(0.8f);
        vpIndicator.setViewPager(vpBanner.getViewPager(), mBannerImageList.size());

        //设定页面循环播放
        vpBanner.setInfiniteLoop(true);
        //设定页面自动切换  间隔3秒
        vpBanner.setAutoScroll(3000);

        initRefreshLayout();

        //获取banner轮播图
        getBannerImageList();
    }


    //下拉刷新 上拉加载
    private void initRefreshLayout() {

        stfLayout.setEnableLoadMore(false);
        stfLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefreshing = true;
                getBannerImageList();
                initHomeMenuList();
                initHotGoodsList();
                getAdvertInfo();
            }
        });

    }

    //获取banner轮播图片
    private void getBannerImageList() {

        BaseOkHttpClient.newBuilder()
                .addParam("cate_id", mCateId)//分类ID:热销为空
                .addParam("is_hot", isHot) //是否为热销：1:是，0:否
                .addParam("client_type", 1) //客户端类型：1:app/wap 2:pc
                .url(NetUrlUtils.GET_BANNER_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取首页轮播图----" + result);

                        StyledDialogUtils.getInstance().dismissLoading();
                        String noteJson = JSONUtils.getNoteJson(result, "contact_info");
                        mBannerImageList = JSONUtils.jsonString2Beans(noteJson, HomeBannerImageBean.class);
                        if (mBannerImageList != null && mBannerImageList.size() > 0) {
                            mBannerViewPagerAdapter.refreshList(mBannerImageList);
                            vpBanner.getViewPager().getAdapter().notifyDataSetChanged();
                            if (mBannerImageList.size() > 2) {
                                vpBanner.getViewPager().setCurrentItem(1);
                            }
                            vpIndicator.setNum(mBannerImageList.size());

                        }

                        if (isRefreshing) {
                            isRefreshing = false;
                            stfLayout.finishRefresh(true);
                        }
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

    @Override
    public void initData() {


        rlvHotGoods.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mHomeHotGoodsListAdapter = new HomeHotGoodsListAdapter(mContext, mCateId);
        rlvHotGoods.setAdapter(mHomeHotGoodsListAdapter);

        rlvGoodsCategory.setLayoutManager(new GridLayoutManager(mContext, 4, LinearLayoutManager.VERTICAL, false));
        mHomeMenuListAdapter = new HomeMenuListAdapter(mContext);
        rlvGoodsCategory.setAdapter(mHomeMenuListAdapter);


    }

    //获取满免运费公告
    private void getAdvertInfo() {

        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_DISCOUNT_INFO)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();

                mAdvertInfoBean = JSONUtils.jsonString2Bean(result, HomeAdvertInfoBean.class);
                if (mAdvertInfoBean == null || mAdvertInfoBean.getBulletin() == null || mAdvertInfoBean.getBulletin().size() <= 0) {
                    return;
                }
                ivAdvertInfo.setVisibility(View.VISIBLE);
                ImageUtils.getCircularPic(mAdvertInfoBean.getBulletin().get(0).getDiscount_img(), ivAdvertInfo, mContext, 10, R.drawable.image_placeholder);
                ivAdvertInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                        intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_URL, mAdvertInfoBean.getBulletin().get(0).getDiscount_url());
                        intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_TITLE, mContext.getResources().getString(R.string.free_of_freight));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                if (isRefreshing) {
                    isRefreshing = false;
                    stfLayout.finishRefresh(true);
                }

            }

            @Override
            public void onError(int code, String msg) {
                //   ToastUtils.show(mContext, msg);
                StyledDialogUtils.getInstance().dismissLoading();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // ToastUtils.show(mContext, e.getMessage());
                StyledDialogUtils.getInstance().dismissLoading();
            }
        });
    }

    //初始化首页菜单列表
    private void initHomeMenuList() {

        //   StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .addParam("cate_id", mCateId)
                .url(NetUrlUtils.GET_INDEX_SECOND_NAVIGATION)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {


            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                String noteJson = JSONUtils.getNoteJson(result, "navigation_list");
                mHomeMenuList = JSONUtils.jsonString2Beans(noteJson, HomeNavigationMenuBean.class);
                //如果为热销  增加：全部 晒单优惠 推荐好友 联系客服 领券中心
                if (isHot == 1) {
                    mHomeMenuList.add(new HomeNavigationMenuBean(-101, getString(R.string.all_goods), String.valueOf(R.mipmap.icon_home_quanbu)));
                    mHomeMenuList.add(new HomeNavigationMenuBean(-102, getString(R.string.drying_order_discount), String.valueOf(R.mipmap.icon_shaidanyouhui)));
                    mHomeMenuList.add(new HomeNavigationMenuBean(-103, getString(R.string.recommend_friend), String.valueOf(R.mipmap.icon_tuijianhaoyou)));
                    mHomeMenuList.add(new HomeNavigationMenuBean(-104, getString(R.string.custom_services), String.valueOf(R.mipmap.icon_lianxikefu)));
                    mHomeMenuList.add(new HomeNavigationMenuBean(-105, getString(R.string.coupon_center), String.valueOf(R.mipmap.icon_lingjuan)));
                }
                mHomeMenuListAdapter.refreshList(mHomeMenuList);

                if (isRefreshing) {
                    isRefreshing = false;
                    stfLayout.finishRefresh(true);
                }
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

    //初始化热卖商品列表
    private void initHotGoodsList() {

        BaseOkHttpClient.newBuilder()
                .addParam("cate_id", StringUtils.isEmpty(mCateId) ? 0 : mCateId)
                .addParam("client_type", 1)//客户端类型：1:app/wap 2:pc
                .url(NetUrlUtils.GET_INDEX_BLOCK_LIST)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {


            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();

                String noteJson = JSONUtils.getNoteJson(result, "block_list");
                mHomeHotGoodsInfoList = JSONUtils.jsonString2Beans(noteJson, HomeHotGoodsInfoBean.class);
                mHomeHotGoodsListAdapter.refreshList(mHomeHotGoodsInfoList);

                if (isRefreshing) {
                    isRefreshing = false;
                    stfLayout.finishRefresh(true);
                }
            }

            @Override
            public void onError(int code, String msg) {
                // ToastUtils.show(mContext, msg);
                StyledDialogUtils.getInstance().dismissLoading();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                //  ToastUtils.show(mContext, e.getMessage());
                StyledDialogUtils.getInstance().dismissLoading();
            }
        });
    }

    @Override
    protected void loadData() {

        if (mHomeMenuList == null || mHomeMenuList.size() <= 0) {
            //初始化首页菜单列表
            initHomeMenuList();
        }

        if (mHomeHotGoodsInfoList == null || mHomeHotGoodsInfoList.size() <= 0) {
            //初始化热卖商品列表
            initHotGoodsList();
        }

        if (mAdvertInfoBean == null || mAdvertInfoBean.getBulletin() == null || mAdvertInfoBean.getBulletin().size() <= 0) {
            //获取满免运费公告
            getAdvertInfo();
        }

    }


}
