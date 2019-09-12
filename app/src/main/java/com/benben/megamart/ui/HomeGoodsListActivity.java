package com.benben.megamart.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.HomeCommonGoodsListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.SearchGoodsListBean;
import com.benben.megamart.config.Constants;
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
 * Created by: wanghk 2019-06-27.
 * Describe: 首页菜单进入的商品列表
 */
public class HomeGoodsListActivity extends BaseActivity {

    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;
    @BindView(R.id.rlv_goods_list)
    RecyclerView rlvGoodsList;
    @BindView(R.id.stf_layout)
    SmartRefreshLayout stfLayout;
    @BindView(R.id.center_title)
    TextView centerTitle;

    //通用的商品列表adapter
    private HomeCommonGoodsListAdapter mCommonGoodsListAdapter;
    private boolean isRefreshing = false;
    private boolean isLoadMore = false;
    //分类id
    private String mCateId = "";
    //页数
    private int mPageStart = 1;
    //获取商品的数量
    private int mPageSize = 15;
    //排序字段：1:全部；2:销量，3:价格，4:新品
    private int mSortField = 1;
    //排序方式：desc:倒序;asc:正序
    private String mOrderBy = "asc";
    //查询关键字
    private String mKeyWord = "";
    //商品产地
    private String mGoodsOriginId = "";
    //商品所属公司
    private String mGoodsCompanyId = "";
    //商品列表
    private List<SearchGoodsListBean> mGoodsList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_goods_list;
    }

    @Override
    protected void initData() {

        String title = getIntent().getStringExtra(Constants.EXTRA_KEY_CATE_TITLE);
        centerTitle.setText(title);
        mCateId = getIntent().getStringExtra(Constants.EXTRA_KEY_CATE_ID);
        rlvGoodsList.setLayoutManager(new GridLayoutManager(mContext, 2));
        mCommonGoodsListAdapter = new HomeCommonGoodsListAdapter(mContext);
        rlvGoodsList.setAdapter(mCommonGoodsListAdapter);
        //下拉刷新 上拉加载
        initRefreshLayout();
        getGoodsList();
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

    private void getGoodsList() {

        Log.e(Constants.WHK_TAG, "getGoodsList: mCateId=" + mCateId + "****mPageStart=" + mPageStart + "****mPageSize=" + mPageSize + "****mSortField=" + mSortField +
                "****mOrderBy=" + mOrderBy + "****mKeyWord=" + mKeyWord + "****mGoodsOriginId=" + mGoodsOriginId + "****mGoodsCompanyId=" + mGoodsCompanyId + "****mUserId=" + MegaMartApplication.mPreferenceProvider.getUId());
        BaseOkHttpClient.newBuilder()
                .addParam("cate_id", mCateId)
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

                Log.e("wanghk", "首页二级页面获取商品列表: 分类id = " + mCateId + "----result = " + result);
                String noteJson = JSONUtils.getNoteJson(result, "goods_list");
                mGoodsList = JSONUtils.jsonString2Beans(noteJson, SearchGoodsListBean.class);

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
               // ToastUtils.show(mContext, msg);
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

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);

    }

    @OnClick({R.id.rl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
        }
    }
}
