package com.benben.megamart.frag;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.LazyBaseFragments;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.CouponAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.DiscountListBean;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.ui.mine.ReceiverCouponActivity;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;


/**
 * 我的优惠券，三个界面一样，复用
 */
public class CouponFragment extends LazyBaseFragments {
    @BindView(R.id.rv_coupon)
    RecyclerView rvCoupon;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;

    private CouponAdapter mAdapter;

    private String mCouponType = "0";//是否使用过：0.未使用（默认），1.已使用  2已失效
    private int mInvalid = 0;//是否失效：0:未失效，1:已失效

    private DiscountListBean mBean;//优惠券列表实体类  有个需求问题 代金券没接口
    private String mCouponStatus;

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_coupon, null);
        return mRootView;
    }

    @Override
    public void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvCoupon.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void initData() {
        mCouponType = getArguments().getString("type");
        mCouponStatus = mCouponType;
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), ReceiverCouponActivity.class), 101);
            }
        });
    }

    @Override
    protected void loadData() {
        super.loadData();
        if ("2".equals(mCouponType)) {
            mCouponType = "0";
            mInvalid = 1;
        }
        StyledDialogUtils.getInstance().loading(getActivity());
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.DISCOUNT_LIST)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("is_used", "" + mCouponType)
                .addParam("is_invalid", "" + mInvalid)
                .post()
                .json()
                .build().enqueue(getActivity(), new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                mBean = JSONUtils.jsonString2Bean(result, DiscountListBean.class);
                llytNoData.setVisibility(View.GONE);
                mAdapter = new CouponAdapter(getActivity(), mBean.getCoupon_list(),mCouponStatus);
                rvCoupon.setAdapter(mAdapter);
            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                llytNoData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
                llytNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            loadData();
        }
    }
}
