package com.benben.megamart.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.megamart.BaseActivity;
import com.benben.megamart.MainActivity;
import com.benben.megamart.R;
import com.benben.megamart.config.Constants;
import com.benben.megamart.ui.mine.OrderActivity;
import com.benben.megamart.utils.RxBus;
import com.benben.megamart.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by: wanghk 2019-06-11.
 * Describe:支付结果页面
 */
public class PaymentResultActivity extends BaseActivity {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.iv_payment_successful)
    ImageView ivPaymentSuccessful;
    @BindView(R.id.tv_payment_successful)
    TextView tvPaymentSuccessful;
    @BindView(R.id.tv_payment_amount)
    TextView tvPaymentAmount;
    @BindView(R.id.tv_payment_method)
    TextView tvPaymentMethod;
    @BindView(R.id.btn_check_order)
    Button btnCheckOrder;
    @BindView(R.id.btn_back_to_home)
    Button btnBackToHome;
    private int mPayWay;
    private String mPaymentMoney;
    private String mOrderId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_payment_result;
    }

    @Override
    protected void initData() {
        centerTitle.setText(R.string.payment_successful);
        //支付方式
        mPayWay = getIntent().getIntExtra(Constants.EXTRA_KEY_PAYMENT_WAY, 1);
        //订单id
        mOrderId = getIntent().getStringExtra(Constants.EXTRA_KEY_ORDER_ID);
        //支付金额
        mPaymentMoney = getIntent().getStringExtra(Constants.EXTRA_KEY_PAYMENT_MONEY);

        tvPaymentAmount.setText(getString(R.string.goods_price,mPaymentMoney));
        switch (mPayWay){
            case 1://余额
                tvPaymentMethod.setText(getResources().getString(R.string.balance));
                break;
            case 2://支付宝
                tvPaymentMethod.setText(getResources().getString(R.string.alipy));
                break;
            case 3://微信
                tvPaymentMethod.setText(getResources().getString(R.string.wechat));
                break;
            case 4://paypal
                tvPaymentMethod.setText(getResources().getString(R.string.paypal));
                break;
            case 5://货到付款
                tvPaymentMethod.setText(getResources().getString(R.string.cash_on_delivery));
                break;
            case 6://Stripe
                tvPaymentMethod.setText(getResources().getString(R.string.stripe));
                break;
        }

    }


    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }



    @OnClick({R.id.rl_back, R.id.btn_check_order, R.id.btn_back_to_home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.btn_check_order:
                Intent intent = new Intent(mContext, OrderActivity.class);
                //intent.putExtra(Constants.EXTRA_KEY_ORDER_ID,mOrderId);
                RxBus.getInstance().post(101);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_back_to_home:
                startActivity(new Intent(mContext, MainActivity.class));
                RxBus.getInstance().post(0);
                finish();
                break;
        }
    }
}
