package com.benben.megamart.frag;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.LazyBaseFragments;
import com.benben.megamart.R;
import com.benben.megamart.adapter.ComposeDiscountListAdapter;
import com.benben.megamart.adapter.RebateDiscountListAdapter;
import com.benben.megamart.adapter.SpecialDiscountListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.ComposeDiscountListBean;
import com.benben.megamart.bean.RebateDiscountListBean;
import com.benben.megamart.bean.SpecialDiscountListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.ui.DiscountGoodsListActivity;
import com.benben.megamart.utils.StatusBarUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Create by wanghk on 2019-06-05.
 * Describe:优惠页面
 */
public class MainDiscountFragment extends LazyBaseFragments {

    public static final String EXTRA_KEY_DISCOUNT_TYPE = "discount_type";
    @BindView(R.id.llyt_compose_discount_more)
    LinearLayout llytComposeDiscountMore;
    @BindView(R.id.rlv_compose_discount)
    RecyclerView rlvComposeDiscount;
    @BindView(R.id.llyt_special_price_discount_more)
    LinearLayout llytSpecialPriceDiscountMore;
    @BindView(R.id.rlv_special_discount)
    RecyclerView rlvSpecialDiscount;
    @BindView(R.id.llyt_rebate_discount_more)
    LinearLayout llytRebateDiscountMore;
    @BindView(R.id.rlv_rebate_discount)
    RecyclerView rlvRebateDiscount;
    @BindView(R.id.stf_layout)
    SmartRefreshLayout stfLayout;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;
    //跳转优惠商品列表的intent
    private Intent mDiscountGoodsListIntent;
    //组合优惠列表adapter
    private ComposeDiscountListAdapter mComposeDiscountListAdapter;
    //特价优惠列表adapter
    private SpecialDiscountListAdapter mSpecialDiscountListAdapter;
    //折扣优惠列表adapter
    private RebateDiscountListAdapter mRebateDiscountListAdapter;

    public static MainDiscountFragment getInstance() {
        MainDiscountFragment sf = new MainDiscountFragment();
        return sf;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_main_discount, null);
        return mRootView;
    }

    @Override
    public void initView() {
        StatusBarUtils.setStatusBarColor(getActivity(), R.color.color_EC5413);
        mRootView.setPadding(0,StatusBarUtils.getStatusBarHeight(getActivity()),0,0);

        stfLayout.setEnableLoadMore(false);

        mDiscountGoodsListIntent = new Intent(mContext, DiscountGoodsListActivity.class);
        rlvComposeDiscount.setLayoutManager(new GridLayoutManager(mContext, 2));
        mComposeDiscountListAdapter = new ComposeDiscountListAdapter(mContext);
        rlvComposeDiscount.setAdapter(mComposeDiscountListAdapter);

        rlvSpecialDiscount.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mSpecialDiscountListAdapter = new SpecialDiscountListAdapter(mContext, true);
        rlvSpecialDiscount.setAdapter(mSpecialDiscountListAdapter);

        rlvRebateDiscount.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRebateDiscountListAdapter = new RebateDiscountListAdapter(mContext, false);
        rlvRebateDiscount.setAdapter(mRebateDiscountListAdapter);


        stfLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getComposeDiscountList();
                getSpecialDiscountList();
                getRebateDiscountList();
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isPrepared()){
            StatusBarUtils.setStatusBarColor(getActivity(), R.color.color_EC5413);
            mRootView.setPadding(0, StatusBarUtils.getStatusBarHeight(getActivity()), 0, 0);

        }
    }
    //获取折扣优惠商品列表
    private void getRebateDiscountList() {
        BaseOkHttpClient.newBuilder()
                .addParam("page_start", 1)//第几页
                .addParam("page_size", 3)//显示数量
                .url(NetUrlUtils.GET_DISCOUNT_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 折扣优惠商品列表----" + result);
                        String noteJson = JSONUtils.getNoteJson(result, "goods_list");
                        List<RebateDiscountListBean> rebateDiscountList = JSONUtils.jsonString2Beans(noteJson, RebateDiscountListBean.class);
                        mRebateDiscountListAdapter.refreshList(rebateDiscountList);
                        showNoDataView();
                    }

                    @Override
                    public void onError(int code, String msg) {
                        ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                        showNoDataView();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.show(mContext, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                        showNoDataView();
                    }
                });
    }

    //获取特价优惠商品列表
    private void getSpecialDiscountList() {
        BaseOkHttpClient.newBuilder()
                .addParam("page_start", 1)//第几页
                .addParam("page_size", 3)//显示数量
                .url(NetUrlUtils.GET_SPECIAL_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 特价优惠商品列表----" + result);
                        String noteJson = JSONUtils.getNoteJson(result, "goods_list");
                        List<SpecialDiscountListBean> specialDiscountList = JSONUtils.jsonString2Beans(noteJson, SpecialDiscountListBean.class);
                        mSpecialDiscountListAdapter.refreshList(specialDiscountList);
                        showNoDataView();
                    }

                    @Override
                    public void onError(int code, String msg) {
                        ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                        showNoDataView();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.show(mContext, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                        showNoDataView();
                    }
                });
    }

    //获取组合优惠商品列表
    private void getComposeDiscountList() {


       // StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .addParam("page_start", 1)//第几页
                .addParam("page_size", 2)//显示数量
                .url(NetUrlUtils.GET_COMPOSE_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 组合优惠商品列表----" + result);
                        String noteJson = JSONUtils.getNoteJson(result, "goods_list");
                        List<ComposeDiscountListBean> composeDiscountList = JSONUtils.jsonString2Beans(noteJson, ComposeDiscountListBean.class);
                        mComposeDiscountListAdapter.refreshList(composeDiscountList);
                        showNoDataView();

                    }

                    @Override
                    public void onError(int code, String msg) {
                        ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                        showNoDataView();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.show(mContext, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                        showNoDataView();
                    }
                });


    }

    @Override
    public void initData() {

    }

    @Override
    protected void loadData() {
        if (mComposeDiscountListAdapter.getItemCount() <= 0) {
            getComposeDiscountList();
        }

        if (mSpecialDiscountListAdapter.getItemCount() <= 0) {
            getSpecialDiscountList();
        }
        if (mRebateDiscountListAdapter.getItemCount() <= 0) {
            getRebateDiscountList();
        }

    }


    @OnClick({R.id.llyt_compose_discount_more, R.id.llyt_special_price_discount_more, R.id.llyt_rebate_discount_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llyt_compose_discount_more:
                mDiscountGoodsListIntent.putExtra(EXTRA_KEY_DISCOUNT_TYPE, 0);
                startActivity(mDiscountGoodsListIntent);
                break;
            case R.id.llyt_special_price_discount_more:
                mDiscountGoodsListIntent.putExtra(EXTRA_KEY_DISCOUNT_TYPE, 1);
                startActivity(mDiscountGoodsListIntent);
                break;
            case R.id.llyt_rebate_discount_more:
                mDiscountGoodsListIntent.putExtra(EXTRA_KEY_DISCOUNT_TYPE, 2);
                startActivity(mDiscountGoodsListIntent);
                break;
        }
    }

    public void showNoDataView() {
        if (mComposeDiscountListAdapter.getItemCount() <= 0 && mSpecialDiscountListAdapter.getItemCount() <= 0 && mRebateDiscountListAdapter.getItemCount() <= 0) {
            llytNoData.setVisibility(View.VISIBLE);
            stfLayout.setVisibility(View.GONE);
        } else {
            llytNoData.setVisibility(View.GONE);
            stfLayout.setVisibility(View.VISIBLE);
        }
        stfLayout.finishRefresh(true);
    }
}
