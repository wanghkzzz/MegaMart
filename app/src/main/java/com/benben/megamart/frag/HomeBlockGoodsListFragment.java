package com.benben.megamart.frag;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.LazyBaseFragments;
import com.benben.megamart.R;
import com.benben.megamart.adapter.HomeBlockGoodsListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.HomeBlockGoodsListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-06-14.
 * Describe:主页二级页面商品列表fragment
 */
public class HomeBlockGoodsListFragment extends LazyBaseFragments {

    @BindView(R.id.rlv_goods_list)
    RecyclerView rlvGoodsList;
    @BindView(R.id.stf_layout)
    SmartRefreshLayout stfLayout;
    @BindView(R.id.rlyt_back_top)
    RelativeLayout rlytBackTop;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;
    Unbinder unbinder;
    //通用的商品列表adapter
    private HomeBlockGoodsListAdapter mCommonGoodsListAdapter;
    private boolean isRefreshing = false;
    private boolean isLoadMore = false;
    //分类id
    private int mCateId = 0;
    //页数
    private int mPageStart = 1;
    //获取商品的数量
    private int mPageSize = 20;
    //商品列表
    private List<HomeBlockGoodsListBean> mGoodsList;
    //板块id
    private int mBlockType;

    public static HomeBlockGoodsListFragment getInstance() {
        HomeBlockGoodsListFragment sf = new HomeBlockGoodsListFragment();
        return sf;
    }


    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_common_goods_list, null);
        return mRootView;
    }

    @Override
    public void initView() {

        mCateId = getArguments().getInt(Constants.EXTRA_KEY_CATE_ID, 0);
        mBlockType = getArguments().getInt(Constants.EXTRA_KEY_BLOCK_TYPE, 0);

        rlvGoodsList.setLayoutManager(new GridLayoutManager(mContext, 2));
        mCommonGoodsListAdapter = new HomeBlockGoodsListAdapter(mContext);
        rlvGoodsList.setAdapter(mCommonGoodsListAdapter);
        //下拉刷新 上拉加载
        initRefreshLayout();

    }


    //下拉刷新 上拉加载
    private void initRefreshLayout() {


        stfLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageStart = 1;
                isRefreshing = true;
                getGoodsList();
            }
        });
        stfLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageStart++;
                isLoadMore = true;
                getGoodsList();
            }
        });
    }

    @Override
    public void initData() {

    }


    /**
     * 获取商品列表
     * <p>
     * cate_id          分类ID（点击全部进入则为空）
     * page_start       获取页数（第几页）
     * page_size        当前页获取商品数量
     * search_field     关键字段：2:热销；3:新品：4：推荐
     */

    private void getGoodsList() {

        BaseOkHttpClient.newBuilder()
                .addParam("cate_id", mCateId)
                .addParam("search_field", mBlockType)
                .addParam("page_start", mPageStart)
                .addParam("page_size", mPageSize)
                .url(NetUrlUtils.GET_BLOCK_GOODS_LIST)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {


            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();

                Log.e(Constants.WHK_TAG, "获取二级页面商品列表: 分类id = " + mCateId + "----result = " + result);
                String noteJson = JSONUtils.getNoteJson(result, "goods_list");
                mGoodsList = JSONUtils.jsonString2Beans(noteJson, HomeBlockGoodsListBean.class);

                if (mPageStart == 1) {
                    mCommonGoodsListAdapter.clear();
                }
                if (mGoodsList != null
                        && !mGoodsList.isEmpty()) {
                    mCommonGoodsListAdapter.appendToList(mGoodsList);
                }

                if (mCommonGoodsListAdapter.getItemCount() == 0) {
                    llytNoData.setVisibility(View.VISIBLE);
                    rlvGoodsList.setVisibility(View.GONE);
                } else {
                    llytNoData.setVisibility(View.GONE);
                    rlvGoodsList.setVisibility(View.VISIBLE);
                }

                if (isRefreshing) {
                    isRefreshing = false;
                    stfLayout.finishRefresh();
                }
                if (isLoadMore) {
                    isRefreshing = false;
                    stfLayout.finishLoadMore();
                }
            }

            @Override
            public void onError(int code, String msg) {
                ToastUtils.show(mContext, msg);
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

    @Override
    protected void loadData() {
        //获取商品列表
        if (mGoodsList != null && mGoodsList.size() > 0) {
            return;
        }
        getGoodsList();
    }


    @OnClick({R.id.rlyt_back_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlyt_back_top:
                //回到顶部
                break;
        }
    }


}
