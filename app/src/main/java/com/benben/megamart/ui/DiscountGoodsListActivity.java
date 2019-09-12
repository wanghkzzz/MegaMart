package com.benben.megamart.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.R;
import com.benben.megamart.adapter.ComposeDiscountListAdapter;
import com.benben.megamart.adapter.RebateDiscountListAdapter;
import com.benben.megamart.adapter.SpecialDiscountListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.ComposeDiscountListBean;
import com.benben.megamart.bean.RebateDiscountListBean;
import com.benben.megamart.bean.SpecialDiscountListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.frag.MainDiscountFragment;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.StatusBarUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-06-05.
 * Describe:优惠商品列表页面
 */
public class DiscountGoodsListActivity extends BaseActivity {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;
    @BindView(R.id.rlv_list)
    RecyclerView rlvList;
    @BindView(R.id.stf_layout)
    SmartRefreshLayout stfLayout;

    //优惠商品类型  0 组合优惠  1 特价优惠 2 折扣优惠
    private int mDiscountType = 0;
    //组合优惠adapter
    private ComposeDiscountListAdapter mComposeDiscountListAdapter;
    //特价优惠
    private SpecialDiscountListAdapter mSpecialDiscountListAdapter;
    //折扣优惠
    private RebateDiscountListAdapter mRebateDiscountListAdapter;

    private boolean isRefreshing = false;
    private boolean isLoadMore = false;
    //当前页数
    private int mPageStart = 1;
    //每页的商品数量
    private int mPageSize = 10;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_common_list;
    }

    @Override
    protected void initData() {
        mDiscountType = getIntent().getIntExtra(MainDiscountFragment.EXTRA_KEY_DISCOUNT_TYPE, 0);
        switch (mDiscountType) {
            case 0://组合优惠
                centerTitle.setText(getResources().getString(R.string.compose_discount));
                rlvList.setLayoutManager(new GridLayoutManager(mContext, 2));
                mComposeDiscountListAdapter = new ComposeDiscountListAdapter(mContext);
                rlvList.setAdapter(mComposeDiscountListAdapter);
                break;
            case 1://特价优惠
                centerTitle.setText(getResources().getString(R.string.special_discount));
                rlvList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                mSpecialDiscountListAdapter = new SpecialDiscountListAdapter(mContext, true);
                rlvList.setAdapter(mSpecialDiscountListAdapter);
                break;
            case 2://折扣优惠
                centerTitle.setText(getResources().getString(R.string.rebate_discount));
                rlvList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                mRebateDiscountListAdapter = new RebateDiscountListAdapter(mContext, false);
                rlvList.setAdapter(mRebateDiscountListAdapter);
                break;
        }

        //下拉刷新 上拉加载
        initRefreshLayout();
        initDiscountGoodsList();
    }

    private void initDiscountGoodsList() {
        llytNoData.setVisibility(View.GONE);
        stfLayout.setVisibility(View.VISIBLE);
        switch (mDiscountType) {
            case 0:
                //组合优惠
                getComposeDiscountList();
                break;
            case 1:
                //特价优惠
                getSpecialDiscountList();
                break;
            case 2:
                //折扣优惠
                getRebateDiscountList();
                break;

        }
    }


    //获取折扣优惠商品列表
    private void getRebateDiscountList() {
        BaseOkHttpClient.newBuilder()
                .addParam("page_start", mPageStart)//第几页
                .addParam("page_size", mPageSize)//显示数量
                .url(NetUrlUtils.GET_DISCOUNT_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 折扣优惠商品列表----" + result);
                        String noteJson = JSONUtils.getNoteJson(result, "goods_list");
                        List<RebateDiscountListBean> rebateDiscountList = JSONUtils.jsonString2Beans(noteJson, RebateDiscountListBean.class);
                        if (mPageStart == 1) {
                            mRebateDiscountListAdapter.clear();
                        }
                        if (rebateDiscountList != null
                                && !rebateDiscountList.isEmpty()) {
                            mRebateDiscountListAdapter.appendToList(rebateDiscountList);
                        }

                        if (mRebateDiscountListAdapter.getItemCount() == 0) {
                            llytNoData.setVisibility(View.VISIBLE);
                            rlvList.setVisibility(View.GONE);
                        } else {
                            llytNoData.setVisibility(View.GONE);
                            rlvList.setVisibility(View.VISIBLE);
                        }

                        if (isRefreshing) {
                            isRefreshing = false;
                            stfLayout.finishRefresh(true);
                        }
                        if (isLoadMore) {
                            isRefreshing = false;
                            stfLayout.finishLoadMore(true);
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        //ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                        if (isRefreshing) {
                            isRefreshing = false;
                            stfLayout.finishRefresh(true);
                        }
                        if (isLoadMore) {
                            isRefreshing = false;
                            stfLayout.finishLoadMore(true);
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.show(mContext, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                        if (isRefreshing) {
                            isRefreshing = false;
                            stfLayout.finishRefresh(false);
                        }
                        if (isLoadMore) {
                            isRefreshing = false;
                            stfLayout.finishLoadMore(false);
                        }
                    }
                });
    }

    //获取特价优惠商品列表
    private void getSpecialDiscountList() {
        BaseOkHttpClient.newBuilder()
                .addParam("page_start", mPageStart)//第几页
                .addParam("page_size", mPageSize)//显示数量
                .url(NetUrlUtils.GET_SPECIAL_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 特价优惠商品列表----" + result);
                        String noteJson = JSONUtils.getNoteJson(result, "goods_list");
                        List<SpecialDiscountListBean> specialDiscountList = JSONUtils.jsonString2Beans(noteJson, SpecialDiscountListBean.class);
                        if (mPageStart == 1) {
                            mSpecialDiscountListAdapter.clear();
                        }
                        if (specialDiscountList != null
                                && !specialDiscountList.isEmpty()) {
                            mSpecialDiscountListAdapter.appendToList(specialDiscountList);
                        }

                        if (mSpecialDiscountListAdapter.getItemCount() == 0) {
                            llytNoData.setVisibility(View.VISIBLE);
                            rlvList.setVisibility(View.GONE);
                        } else {
                            llytNoData.setVisibility(View.GONE);
                            rlvList.setVisibility(View.VISIBLE);
                        }

                        if (isRefreshing) {
                            isRefreshing = false;
                            stfLayout.finishRefresh(true);
                        }
                        if (isLoadMore) {
                            isRefreshing = false;
                            stfLayout.finishLoadMore(true);
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                      //  ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                        if (isRefreshing) {
                            isRefreshing = false;
                            stfLayout.finishRefresh(true);
                        }
                        if (isLoadMore) {
                            isRefreshing = false;
                            stfLayout.finishLoadMore(true);
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.show(mContext, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                        if (isRefreshing) {
                            isRefreshing = false;
                            stfLayout.finishRefresh(false);
                        }
                        if (isLoadMore) {
                            isRefreshing = false;
                            stfLayout.finishLoadMore(false);
                        }
                    }
                });
    }

    //获取组合优惠商品列表
    private void getComposeDiscountList() {


        // StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .addParam("page_start", mPageStart)//第几页
                .addParam("page_size", mPageSize)//显示数量
                .url(NetUrlUtils.GET_COMPOSE_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 组合优惠商品列表----" + result);
                        String noteJson = JSONUtils.getNoteJson(result, "goods_list");
                        List<ComposeDiscountListBean> composeDiscountList = JSONUtils.jsonString2Beans(noteJson, ComposeDiscountListBean.class);
                        if (mPageStart == 1) {
                            mComposeDiscountListAdapter.clear();
                        }
                        if (composeDiscountList != null
                                && !composeDiscountList.isEmpty()) {
                            mComposeDiscountListAdapter.appendToList(composeDiscountList);
                        }

                        if (mComposeDiscountListAdapter.getItemCount() == 0) {
                            llytNoData.setVisibility(View.VISIBLE);
                            rlvList.setVisibility(View.GONE);
                        } else {
                            llytNoData.setVisibility(View.GONE);
                            rlvList.setVisibility(View.VISIBLE);
                        }

                        if (isRefreshing) {
                            isRefreshing = false;
                            stfLayout.finishRefresh(true);
                        }
                        if (isLoadMore) {
                            isRefreshing = false;
                            stfLayout.finishLoadMore(true);
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        //ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                        if (isRefreshing) {
                            isRefreshing = false;
                            stfLayout.finishRefresh(true);
                        }
                        if (isLoadMore) {
                            isRefreshing = false;
                            stfLayout.finishLoadMore(true);
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.show(mContext, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                        if (isRefreshing) {
                            isRefreshing = false;
                            stfLayout.finishRefresh(false);
                        }
                        if (isLoadMore) {
                            isRefreshing = false;
                            stfLayout.finishLoadMore(false);
                        }
                    }
                });


    }


    //下拉刷新 上拉加载
    private void initRefreshLayout() {


        stfLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageStart = 1;
                isRefreshing = true;
                switch (mDiscountType) {
                    case 0:
                        getComposeDiscountList();
                        break;
                    case 1:
                        getSpecialDiscountList();
                        break;
                    case 2:
                        getRebateDiscountList();
                        break;
                }
            }
        });
        stfLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageStart++;
                isLoadMore = true;
                switch (mDiscountType) {
                    case 0:
                        getComposeDiscountList();
                        break;
                    case 1:
                        getSpecialDiscountList();
                        break;
                    case 2:
                        getRebateDiscountList();
                        break;
                }
            }
        });
    }

    @OnClick({R.id.rl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
        }
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }
}
