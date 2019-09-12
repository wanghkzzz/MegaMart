package com.benben.megamart.ui.mine;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.DateUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.OrderGoodsListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.OrderDetailBean;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.RxBus;
import com.benben.megamart.utils.StatusBarUtils;
import com.benben.megamart.widget.CustomListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/14
 * Time: 11:03
 */
public class OrderDetailActivity extends BaseActivity {
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_zip_code)
    TextView tvZipCode;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_freight)
    TextView tvFreight;
    @BindView(R.id.tv_discount)
    TextView tvDiscount;
    @BindView(R.id.tv_real_price)
    TextView tvRealPrice;
    @BindView(R.id.tv_send_time)
    TextView tvSendTime;
    @BindView(R.id.tv_pay_way)
    TextView tvPayWay;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.lv_goods)
    CustomListView lvGoods;
    @BindView(R.id.center_title)
    TextView centerTitle;

    private String mOrderId = "";//订单id

    private OrderGoodsListAdapter mAdapter;//商品列表适配器

    private OrderDetailBean mBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initData() {
        mOrderId = getIntent().getStringExtra("orderId");
        initTitle("" + getString(R.string.order_detail));
        centerTitle.setText(getString(R.string.order_detail));
        getData();
    }

    private void getData() {
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.ORDER_DETAIL)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("order_id", "" + mOrderId)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                try {
                    JSONArray array = new JSONArray(result);
                    for (int i = 0; i < array.length(); i++) {
                        mBean = JSONUtils.jsonString2Bean(array.get(i).toString(), OrderDetailBean.class);
                    }
                    tvName.setText("" + mBean.getOrder_username() + "\t\t" + mBean.getOrder_phone());
                    tvZipCode.setText("" + mBean.getOrder_zip_code());
                    tvAddress.setText("" + mBean.getUnit_number()+"  "+mBean.getStreet_number()+"  "+mBean.getOrder_address()+"  "+mBean.getOrder_city()+"  "+mBean.getOrder_province());
                    tvOrderNumber.setText(getString(R.string.order_number) + mBean.getOrder_no());
                    tvTime.setText(DateUtils.stampToDate(mBean.getOrder_time()));
                    tvTotalPrice.setText("＄" + mBean.getGoods_price());
                    tvFreight.setText("＄" + mBean.getShipping_fee());
                    tvDiscount.setText("＄" + mBean.getCoupons_price());
                    tvRealPrice.setText("＄" + mBean.getPay_money());
                    tvSendTime.setText("" + mBean.getShipping_time());
                    tvPayWay.setText("" + mBean.getOrder_payway());
                    if ("1".equals(mBean.getOrder_status())) {
//                        tvState.setText(getResources().getString(R.string.wait_to_paid));
                        tvSubmit.setVisibility(View.GONE);
                    } else if ("2".equals(mBean.getOrder_status())) {
//                        tvState.setText(getResources().getString(R.string.paid));
                        tvSubmit.setText(getResources().getString(R.string.apply_after_sale));
                        tvSubmit.setBackgroundResource(R.color.color_999999);
                    } else if ("3".equals(mBean.getOrder_status())) {
//                        tvState.setText(getResources().getString(R.string.apply_saling));
                        tvSubmit.setText(getResources().getString(R.string.revoke_apply));
                        tvSubmit.setBackgroundResource(R.color.theme);

                    } else if ("4".equals(mBean.getOrder_status())) {
//                        tvState.setText(getResources().getString(R.string.apply_sale_completed));
                        tvSubmit.setText(getResources().getString(R.string.completed));

                    }
                    tvState.setText(mBean.getOrder_status_text());
                    for (int i = 0; i < mBean.getPreferential_list().size(); i++) {
                        mBean.getGoods_list().addAll(mBean.getPreferential_list().get(i).getGoods_list());
                    }
                    mAdapter = new OrderGoodsListAdapter(OrderDetailActivity.this, mBean.getGoods_list());
                    lvGoods.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
            }
        });
    }

    @OnClick({R.id.tv_pay_way, R.id.tv_submit, R.id.rl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_pay_way:
                break;
            case R.id.rl_back:
                onBackPressed();
                break;
            case R.id.tv_submit:
                if (mBean == null) {
                    toast(getResources().getString(R.string.get_data_failure));
                    return;
                }
                if ("1".equals(mBean.getOrder_status()) || "2".equals(mBean.getOrder_status())) {
                    Intent intent = new Intent(this, ApplySaleActivity.class);
                    intent.putExtra("orderId", "" + mOrderId);
                    intent.putExtra("bean", (Serializable) mBean.getGoods_list());
                    startActivityForResult(intent, 101);
                } else if ("3".equals(mBean.getOrder_status())) {
                    //申请售后中，撤销售后
                    cancelApply();
                }
                break;
        }
    }

    /**
     * 撤销申请售后
     */
    private void cancelApply() {
        //  StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.CANCEL_APPLY_SALE)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("order_id", "" + mOrderId)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {

                StyledDialogUtils.getInstance().dismissLoading();
                getData();
                toast(msg);
            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getData();
        }
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }

    @Override
    public void onBackPressed() {
        RxBus.getInstance().post(201);
        super.onBackPressed();
    }

}
