package com.benben.megamart.frag;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.LazyBaseFragments;
import com.benben.megamart.R;
import com.benben.megamart.adapter.AFinalRecyclerViewAdapter;
import com.benben.megamart.adapter.CategoryFirstLevelAdapter;
import com.benben.megamart.adapter.CategorySecondLevelAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.CategoryListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.ui.SearchGoodsRecordActivity;
import com.benben.megamart.utils.StatusBarUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Create by wanghk on 2019-05-27.
 * Describe:分类fragment
 */
public class MainCategoryFragment extends LazyBaseFragments {


    @BindView(R.id.edt_goods_search)
    EditText edtGoodsSearch;
    @BindView(R.id.rlv_first_level_List)
    RecyclerView rlvFirstLevelList;
    @BindView(R.id.iv_active)
    ImageView ivActive;
    @BindView(R.id.tv_category_name)
    TextView tvCategoryName;
    @BindView(R.id.rlv_second_level_list)
    RecyclerView rlvSecondLevelList;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;
    @BindView(R.id.stf_layout)
    SmartRefreshLayout stfLayout;
    @BindView(R.id.llye_search_goods)
    LinearLayout llyeSearchGoods;

    //活动图片list
    List<String> mImgList = new ArrayList<>();
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    private CategoryFirstLevelAdapter mFirstLevelAdapter;
    private CategorySecondLevelAdapter mSecondLevelAdapter;
    private List<CategoryListBean> mCategoryList;
    //当前选择的一级分类下标
    private int index = 0;

    public static MainCategoryFragment getInstance() {
        MainCategoryFragment sf = new MainCategoryFragment();
        return sf;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_main_category, null);
        return mRootView;
    }

    @Override
    public void initView() {

        StatusBarUtils.setStatusBarColor(getActivity(), R.color.color_EC5413);
        mRootView.setPadding(0, StatusBarUtils.getStatusBarHeight(getActivity()), 0, 0);

        initRefreshLayout();

        rlvFirstLevelList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rlvSecondLevelList.setLayoutManager(new GridLayoutManager(mContext, 4));

        mFirstLevelAdapter = new CategoryFirstLevelAdapter(mContext);
        rlvFirstLevelList.setAdapter(mFirstLevelAdapter);

        mSecondLevelAdapter = new CategorySecondLevelAdapter(mContext);
        rlvSecondLevelList.setAdapter(mSecondLevelAdapter);

        mFirstLevelAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<CategoryListBean>() {
            @Override
            public void onItemClick(View view, int position, CategoryListBean model) {
                index = position;
                if (model.getChild_list() != null && model.getChild_list().size() > 0) {
                    llytNoData.setVisibility(View.GONE);
                    rlvSecondLevelList.setVisibility(View.VISIBLE);

                    tvCategoryName.setText(model.getCate_name());
                    ImageUtils.getCircularPic(model.getCate_img(), ivActive, mContext, 10, R.drawable.image_placeholder);
                    mSecondLevelAdapter.refreshList(model.getChild_list());
                } else {
                    llytNoData.setVisibility(View.VISIBLE);
                    rlvSecondLevelList.setVisibility(View.GONE);
                }

            }

            @Override
            public void onItemLongClick(View view, int position, CategoryListBean model) {

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isPrepared()) {
            StatusBarUtils.setStatusBarColor(getActivity(), R.color.color_EC5413);
            mRootView.setPadding(0, StatusBarUtils.getStatusBarHeight(getActivity()), 0, 0);

        }
    }

    //下拉刷新 上拉加载
    private void initRefreshLayout() {

        stfLayout.setEnableLoadMore(false);
        stfLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getCategoryList();
            }
        });

    }

    @Override
    public void initData() {

        //获取分类列表数据
        getCategoryList();

    }

    //获取分类列表数据
    private void getCategoryList() {
        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_CATE_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取分类列表----" + result);

                        StyledDialogUtils.getInstance().dismissLoading();
                        String noteJson = JSONUtils.getNoteJson(result, "cate_list");
                        mCategoryList = JSONUtils.jsonString2Beans(noteJson, CategoryListBean.class);

                        if (mCategoryList == null || mCategoryList.size() <= 0) {
                            llytNoData.setVisibility(View.VISIBLE);
                            rlvSecondLevelList.setVisibility(View.GONE);
                            return;
                        }
                        mFirstLevelAdapter.clear();
                        mFirstLevelAdapter.appendToList(mCategoryList);

                        llytNoData.setVisibility(View.GONE);
                        rlvSecondLevelList.setVisibility(View.VISIBLE);
                        for (CategoryListBean categoryListBean :
                                mCategoryList) {
                            CategoryListBean.ChildListBean childListBean = new CategoryListBean.ChildListBean();
                            childListBean.setCate_id(0);
                            childListBean.setCate_img(String.valueOf(R.mipmap.icon_quanbu));
                            childListBean.setCate_name(mContext.getResources().getString(R.string.all_goods));
                            childListBean.setParent_id(categoryListBean.getCate_id());
                            if (categoryListBean.getChild_list() != null) {
                                categoryListBean.getChild_list().add(0, childListBean);
                            }


                        }
                        //默认显示第一个分类
                        if (mCategoryList.get(index).getChild_list() != null && mCategoryList.get(index).getChild_list().size() > 0) {
                            tvCategoryName.setText(mCategoryList.get(index).getCate_name());
                            ImageUtils.getCircularPic(mCategoryList.get(index).getCate_img(), ivActive, mContext, 10, R.drawable.image_placeholder);
                            mSecondLevelAdapter.refreshList(mCategoryList.get(index).getChild_list());
                        } else {
                            llytNoData.setVisibility(View.VISIBLE);
                            rlvSecondLevelList.setVisibility(View.GONE);
                        }
                        stfLayout.finishRefresh(true);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        stfLayout.finishRefresh(false);
                        ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        stfLayout.finishRefresh(false);
                        ToastUtils.show(mContext, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                    }
                });
    }


    @Override
    protected void loadData() {

    }


    @OnClick({R.id.iv_search, R.id.edt_goods_search, R.id.llye_search_goods})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
            case R.id.edt_goods_search:
            case R.id.llye_search_goods:
                Intent intent = new Intent(mContext, SearchGoodsRecordActivity.class);
                startActivity(intent);
//                参数1：进入时的动画、参数2：退出时的动画
                mContext.overridePendingTransition(R.anim.anim_search_activity_alpha_out, R.anim.anim_search_activity_alpha_in);
                break;
        }
    }
}
