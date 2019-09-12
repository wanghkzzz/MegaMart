package com.benben.megamart.frag;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.LazyBaseFragments;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.OrderListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.OrderBean;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.RxBus;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.IOException;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/14
 * Time: 10:26
 * 我的订单，复用的四个界面
 */
public class OrderFragment extends LazyBaseFragments implements OnRefreshLoadMoreListener {
    private static final String TAG = "OrderFragment";
    @BindView(R.id.rv_order)
    RecyclerView rvOrder;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;
    @BindView(R.id.sf_order)
    SmartRefreshLayout sfOrder;

    private OrderListAdapter mAdapter;

    private String mStatusType = "0";//0全部订单， 5货到付款，2已付款 ，3退货售后

    private OrderBean mBean;

    private int mPage = 1;



    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_order, null);
        return mRootView;
    }

    @Override
    public void initView() {
        sfOrder.setOnLoadMoreListener(this);
        sfOrder.setOnRefreshListener(this);
        RxBus.getInstance().toObservable(Integer.class)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        if(integer == 201){
                            loadData();
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

    @Override
    public void initData() {
        mStatusType = getArguments().getString("type");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvOrder.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void loadData() {

        Log.e(TAG, "loadData: " );
        //StyledDialogUtils.getInstance().loading(getActivity());
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.ORDER_LIST)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("order_status", "" + mStatusType)
                .addParam("page_size", "" + 10)
                .addParam("page_start", "" + mPage)
                .post()
                .json()
                .build().enqueue(getActivity(), new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                llytNoData.setVisibility(View.GONE);
                OrderBean bean = JSONUtils.jsonString2Bean(result, OrderBean.class);
                if (mPage == 1) {
                    mBean = bean;
                    mAdapter = new OrderListAdapter(getActivity(), mBean.getOrder_list(),Integer.parseInt(mStatusType),OrderFragment.this);
                    rvOrder.setAdapter(mAdapter);
                    sfOrder.finishRefresh();
                } else {
                    mBean.getOrder_list().addAll(bean.getOrder_list());
                    mAdapter.notifyDataSetChanged();
                    sfOrder.finishLoadMore();
                }
            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                if (mPage == 1) {
                    llytNoData.setVisibility(View.VISIBLE);
                    sfOrder.finishRefresh();
                } else {
                    sfOrder.finishLoadMore();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
                if (mPage == 1) {
                    llytNoData.setVisibility(View.VISIBLE);
                    sfOrder.finishRefresh();
                } else {
                    sfOrder.finishLoadMore();
                }
            }
        });
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPage++;
        loadData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPage = 1;
        loadData();
    }

}
