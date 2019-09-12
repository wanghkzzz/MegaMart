package com.benben.megamart.frag;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.LazyBaseFragments;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.HomeCommonGoodsListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.GoodsOriginAndCompanyBean;
import com.benben.megamart.bean.SearchGoodsListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.ui.SearchGoodsRecordActivity;
import com.benben.megamart.utils.RxBus;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-06-01.
 * Describe:搜索商品列表fragment
 */
public class SearchGoodsListFragment extends LazyBaseFragments {

    @BindView(R.id.rlv_goods_list)
    RecyclerView rlvGoodsList;
    @BindView(R.id.stf_layout)
    SmartRefreshLayout stfLayout;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;
    Unbinder unbinder;
    //通用的商品列表adapter
    private HomeCommonGoodsListAdapter mCommonGoodsListAdapter;
    private boolean isRefreshing = false;
    private boolean isLoadMore = false;
    //分类id
    private String mCategoryId = "";
    //页数
    private int mPageStart = 1;
    //获取商品的数量
    private int mPageSize = 20;
    //排序字段：1:全部；2:销量，3:价格，4:新品
    private int mSortField = 1;
    //排序方式：desc:倒序;asc:正序
    private String mOrderBy = "";
    //查询关键字
    private String mKeyWord = "";
    //商品产地
    private String mGoodsOriginId = "";
    //商品所属公司
    private String mGoodsCompanyId = "";
    //商品列表
    private List<SearchGoodsListBean> mGoodsList;
    private RxBus mRxBus;




    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_common_goods_list, null);
        return mRootView;
    }

    @Override
    public void initView() {
        mRxBus = RxBus.getInstance();
        mCategoryId = getArguments().getString(Constants.EXTRA_KEY_CATE_ID);
        Log.e(Constants.WHK_TAG, "initView: mCategoryId = "+mCategoryId );

        mKeyWord = mContext.getIntent().getStringExtra(SearchGoodsRecordActivity.KEY_WORD);
        rlvGoodsList.setLayoutManager(new GridLayoutManager(mContext, 2));
        mCommonGoodsListAdapter = new HomeCommonGoodsListAdapter(mContext);
        rlvGoodsList.setAdapter(mCommonGoodsListAdapter);
        //下拉刷新 上拉加载
        initRefreshLayout();

        mRxBus.toObservable(Integer.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                        switch (integer) {

                            case 0://综合
                                mSortField = 1;
                                mOrderBy = "";
                                getGoodsList(false);
                                break;
                            case 1://销量
                                mSortField = 2;
                                mOrderBy = "";
                                getGoodsList(false);
                                break;
                            case 2://价格升序
                                mSortField = 3;
                                mOrderBy = "asc";
                                getGoodsList(false);
                                break;
                            case 3://价格降序
                                mSortField = 3;
                                mOrderBy = "desc";
                                getGoodsList(false);
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

        mRxBus.toObservable(GoodsOriginAndCompanyBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoodsOriginAndCompanyBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GoodsOriginAndCompanyBean goodsOriginAndCompanyBean) {
                        //公司及产地
                        mGoodsCompanyId = goodsOriginAndCompanyBean.getGoods_company_id();
                        mGoodsOriginId = goodsOriginAndCompanyBean.getGoods_origin_id();
                        getGoodsList(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

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
                getGoodsList(true);
            }
        });
        stfLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageStart++;
                isLoadMore = true;
                getGoodsList(true);
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
     * sort_field       排序字段：1:全部；2:销量，3:价格，4:新品
     * order_by         排序方式：desc:倒序;asc:正序
     * keyword          查询关键字
     * goods_origin_id  商品产地
     * goods_company_id 商品所属公司
     * user_id          用户ID：登录时必传
     */

    public void getGoodsList(boolean isManual) {

        StyledDialogUtils.getInstance().loading(mContext);
        Log.e(Constants.WHK_TAG, "getGoodsList: mCategoryId=" + mCategoryId + "****mPageStart=" + mPageStart + "****mPageSize=" + mPageSize + "****mSortField=" + mSortField +
                "****mOrderBy=" + mOrderBy + "****mKeyWord=" + mKeyWord + "****mGoodsOriginId=" + mGoodsOriginId + "****mGoodsCompanyId=" + mGoodsCompanyId + "****mUserId=" + MegaMartApplication.mPreferenceProvider.getUId());
        BaseOkHttpClient.newBuilder()
                .addParam("cate_id", mCategoryId)
                .addParam("page_start", mPageStart)
                .addParam("page_size", mPageSize)
                .addParam("sort_field", mSortField)
                .addParam("order_by", mOrderBy)
                .addParam("keyword", mKeyWord)
                .addParam("goods_origin_id", mGoodsOriginId)
                .addParam("goods_company_id", mGoodsCompanyId)
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.GET_GOODS_LIST)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {


            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();

                Log.e(Constants.WHK_TAG, "获取商品列表: 分类id = " + mCategoryId + "----result = " + result);
                String noteJson = JSONUtils.getNoteJson(result, "goods_list");
                mGoodsList = JSONUtils.jsonString2Beans(noteJson, SearchGoodsListBean.class);

                if (mPageStart == 1) {
                    mCommonGoodsListAdapter.clear();
                }
                if (mGoodsList != null
                        && !mGoodsList.isEmpty()) {
                    mCommonGoodsListAdapter.appendToList(mGoodsList);
                }else {
                    mCommonGoodsListAdapter.clear();
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
              //  ToastUtils.show(mContext, msg);
                StyledDialogUtils.getInstance().dismissLoading();
                if(isManual){
                    llytNoData.setVisibility(View.GONE);
                    rlvGoodsList.setVisibility(View.VISIBLE);
                }else {
                    llytNoData.setVisibility(View.VISIBLE);
                    rlvGoodsList.setVisibility(View.GONE);
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
    public void getGoodsList(boolean isManual,String keyword) {

        StyledDialogUtils.getInstance().loading(mContext);
        Log.e(Constants.WHK_TAG, "getGoodsList: mCategoryId=" + mCategoryId + "****mPageStart=" + mPageStart + "****mPageSize=" + mPageSize + "****mSortField=" + mSortField +
                "****mOrderBy=" + mOrderBy + "****mKeyWord=" + mKeyWord + "****mGoodsOriginId=" + mGoodsOriginId + "****mGoodsCompanyId=" + mGoodsCompanyId + "****mUserId=" + MegaMartApplication.mPreferenceProvider.getUId());
        BaseOkHttpClient.newBuilder()
                .addParam("cate_id", mCategoryId)
                .addParam("page_start", mPageStart)
                .addParam("page_size", mPageSize)
                .addParam("sort_field", mSortField)
                .addParam("order_by", mOrderBy)
                .addParam("keyword", keyword)
                .addParam("goods_origin_id", mGoodsOriginId)
                .addParam("goods_company_id", mGoodsCompanyId)
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.GET_GOODS_LIST)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {


            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();

                Log.e(Constants.WHK_TAG, "获取商品列表: 分类id = " + mCategoryId + "----result = " + result);
                String noteJson = JSONUtils.getNoteJson(result, "goods_list");
                mGoodsList = JSONUtils.jsonString2Beans(noteJson, SearchGoodsListBean.class);

                if (mPageStart == 1) {
                    mCommonGoodsListAdapter.clear();
                }
                if (mGoodsList != null
                        && !mGoodsList.isEmpty()) {
                    mCommonGoodsListAdapter.appendToList(mGoodsList);
                }else {
                    mCommonGoodsListAdapter.clear();
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
              //  ToastUtils.show(mContext, msg);
                StyledDialogUtils.getInstance().dismissLoading();
                if(isManual){
                    llytNoData.setVisibility(View.GONE);
                    rlvGoodsList.setVisibility(View.VISIBLE);
                }else {
                    llytNoData.setVisibility(View.VISIBLE);
                    rlvGoodsList.setVisibility(View.GONE);
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
        if (mGoodsList == null || mGoodsList.size() <= 0) {
            //获取商品列表
            getGoodsList(false);
        }

    }

/*

    @OnClick({R.id.rlyt_back_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlyt_back_top:
                //回到顶部
                break;
        }
    }
*/


}
