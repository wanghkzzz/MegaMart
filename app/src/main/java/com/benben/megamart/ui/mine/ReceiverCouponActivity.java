package com.benben.megamart.ui.mine;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.ReceiverCouponAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.ReceiveCouponListBean;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.StatusBarUtils;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/18
 * Time: 9:46
 * 领取优惠券
 */
public class ReceiverCouponActivity extends BaseActivity {
    private static final String TAG = "ReceiverCouponActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_coupon)
    RecyclerView rvCoupon;

    private ReceiverCouponAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_receiver_coupon;
    }

    @Override
    protected void initData() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCoupon.setLayoutManager(linearLayoutManager);
        getData();
    }

    private void getData() {
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder()
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.RECEIVER_DISCOUNT_LIST)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                Log.e(TAG, "onSuccess: result=" + result);
                StyledDialogUtils.getInstance().dismissLoading();
                String noteJson = JSONUtils.getNoteJson(result, "coupon_list");
                List<ReceiveCouponListBean> receiveCouponListBeans = JSONUtils.jsonString2Beans(noteJson, ReceiveCouponListBean.class);
                mAdapter = new ReceiverCouponAdapter(ReceiverCouponActivity.this, receiveCouponListBeans);
                rvCoupon.setAdapter(mAdapter);
            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
            }
        });
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_transparent);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }
}
