package com.benben.megamart.ui.mine;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alipay.sdk.app.PayTask;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.AFinalRecyclerViewAdapter;
import com.benben.megamart.adapter.PaymentWayListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.api.PayResultListener;
import com.benben.megamart.bean.AliPayBean;
import com.benben.megamart.bean.PayWayBean;
import com.benben.megamart.bean.RechargeOrderBean;
import com.benben.megamart.bean.WxPayBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.PayListenerUtils;
import com.benben.megamart.utils.PayPalHelper;
import com.benben.megamart.utils.StatusBarUtils;
import com.google.gson.Gson;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 充值界面
 */
public class RechargeActivity extends BaseActivity {
    @BindView(R.id.edt_money)
    EditText edtMoney;

    @BindView(R.id.btn_recharge)
    Button btnRecharge;
    @BindView(R.id.rlv_payment_way)
    RecyclerView rlvPaymentWay;

    //支付方式列表adapter
    private PaymentWayListAdapter mPaymentWayListAdapter;

    private String mPaymentWay = "";
    private String stripeToken;
    private RechargeOrderBean rechargeOrderBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initData() {
        initTitle("" + getString(R.string.wallet_recharge));

        rlvPaymentWay.setLayoutManager(new LinearLayoutManager(mContext));
        mPaymentWayListAdapter = new PaymentWayListAdapter(mContext);
        rlvPaymentWay.setAdapter(mPaymentWayListAdapter);

        mPaymentWayListAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<PayWayBean.PaywayListBean>() {
            @Override
            public void onItemClick(View view, int position, PayWayBean.PaywayListBean model) {
                mPaymentWay = model.getPay_id();
            }

            @Override
            public void onItemLongClick(View view, int position, PayWayBean.PaywayListBean model) {

            }
        });

        getPaymentWay();
        //微信支付的回调监听
        PayListenerUtils.getInstance(this).addListener(new PayResultListener() {
            @Override
            public void onPaySuccess() {
                //支付成功
                toast(getString(R.string.payment_successful));
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onPayError() {
                //支付失败
                toast(getString(R.string.payment_failure));
            }

            @Override
            public void onPayCancel() {
                //取消支付
                toast(getString(R.string.payment_cancel));
            }
        });
    }

    @OnClick({R.id.btn_recharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //确定充值
            case R.id.btn_recharge:
                String money = edtMoney.getText().toString().trim();

                if (TextUtils.isEmpty(money)) {
                    toast(getString(R.string.recharge_input_money));
                    return;
                }

                if (TextUtils.isEmpty(mPaymentWay)) {
                    toast(getString(R.string.select_pay_way));
                    return;
                }


                //调接口去充值
                goRecharge(money);
                break;
        }
    }

    //弹出stripe支付的dialog
    private void showStripePayDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.NoBackGroundDialog);
        View view = View.inflate(mContext, R.layout.dialog_stripe_input_widget, null);

        CardInputWidget stripeInputWidget = view.findViewById(R.id.stripe_input_widget);
        Button btnAffirm = view.findViewById(R.id.btn_affirm);

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        btnAffirm.setOnClickListener(v -> {
            StyledDialogUtils.getInstance().loading(mContext);
            //stripe的控件提供了直接获取输入的信息
            //获取输入框银行卡信息
            Card cardToSave = stripeInputWidget.getCard();
            //验证是否错误
            if (cardToSave == null) {
                //验证错误
                ToastUtils.show(mContext, mContext.getResources().getString(R.string.card_not_support_or_invalid));
            } else {
                //创建stripe对象
                Stripe stripe = new Stripe(mContext, Constants.STRIPE_PUBLISHABLE_KEY);
                stripe.createToken(
                        cardToSave,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                Log.e(Constants.WHK_TAG, "onSuccess: token = " + token.getId() + " **" + token.getType() + " **" + token.getCard() + " **" + token.getCreated() + " **" + token.getLivemode() + " **" + token.getUsed());
                                // Send token to your server
                                //成功创建令牌
                                //发起支付的请求接口
                                stripeToken = token.getId();
                                pay(rechargeOrderBean.getAmount(), rechargeOrderBean.getOrder_id());
                            }

                            public void onError(Exception error) {
                                // Show localized error message

                            }
                        }
                );
            }

            if (bottomSheetDialog.isShowing()) {
                bottomSheetDialog.dismiss();
            }
        });
    }


    //获取支付方式列表
    private void getPaymentWay() {
        BaseOkHttpClient.newBuilder()
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("request_type", 2)//请求参数：1：订单页请求；2:余额支付请求
                .url(NetUrlUtils.GET_ORDER_PAY_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {

                    private List<PayWayBean.PaywayListBean> mPaymentWayList;

                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取支付方式列表----" + result);
                        StyledDialogUtils.getInstance().dismissLoading();
                        if (!StringUtils.isEmpty(result)) {
                            String noteJson = JSONUtils.getNoteJson(result, "payway_list");
                            mPaymentWayList = JSONUtils.jsonString2Beans(noteJson, PayWayBean.PaywayListBean.class);
                            if (mPaymentWayList != null && mPaymentWayList.size() > 0) {
                                for (int i = 0; i < mPaymentWayList.size(); i++) {
                                    if ("1".equals(mPaymentWayList.get(i).getPay_id())) {
                                        mPaymentWayList.get(i).setSelect(true);
                                    } else {
                                        mPaymentWayList.get(i).setSelect(false);
                                    }

                                }
                                mPaymentWayListAdapter.refreshList(mPaymentWayList);
                            }

                        }

                    }

                    @Override
                    public void onError(int code, String msg) {
                        ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();

                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.show(mContext, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                    }
                });
    }

    //充值
    private void goRecharge(String money) {
        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("amount", money)
                .addParam("pay_way", mPaymentWay)
                .url(NetUrlUtils.ADD_RECHARGE_ORDER)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 提交充值----" + result);
                        StyledDialogUtils.getInstance().dismissLoading();
                        rechargeOrderBean = JSONUtils.jsonString2Bean(result, RechargeOrderBean.class);
                        String pay_way = rechargeOrderBean.getPay_way();
                        if("6".equals(pay_way)){
                            showStripePayDialog();
                            return;
                        }
                        pay(rechargeOrderBean.getAmount(), rechargeOrderBean.getOrder_id());
                    }

                    @Override
                    public void onError(int code, String msg) {
                        ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.show(mContext, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                    }
                });

    }


    /**
     * 去付款
     */
    private void pay(String amount, String orderId) {

        BaseOkHttpClient.newBuilder().url(NetUrlUtils.ADD_PAYMENT)
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("amount", amount)//支付金额
                .addParam("pay_way", mPaymentWay)//支付方式：2:支付宝；3:微信支付；4:paypal
                .addParam("pay_type", 1)//支付类型：1:充值余额；2:订单支付
                .addParam("order_id", orderId)//订单ID
                .addParam("client_type", 1)//客户端类型：1:app；2:wap；3:pc
                .addParam("model_type", 2)//机型：1:其他；2:android；3:iphone
                .addParam("stripe_msg", StringUtils.isEmpty(stripeToken) ? "" : stripeToken)//机型：1:其他；2:android；3:iphone
                .post()
                .json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                Log.e(Constants.WHK_TAG, "onSuccess: 提交支付----" + result);
                StyledDialogUtils.getInstance().dismissLoading();
                switch (mPaymentWay) {

                    case "2":
                        //支付宝支付
                        Gson gson = new Gson();
                        AliPayBean bean = gson.fromJson(result, AliPayBean.class);
                        aliPay(bean.getPay_info());
                        break;
                    case "3":
                        //微信支付
                        Gson wxGson = new Gson();
                        WxPayBean wxBean = wxGson.fromJson(result, WxPayBean.class);
                        wxPay(wxBean);
                        break;
                    case "4":
                        //paypal支付
                        PayPalHelper.getInstance().doPayPalPay(mContext);
                        break;
                    case "6":
                        //stripe支付
                        toast(getString(R.string.payment_successful));
                        setResult(RESULT_OK);

                        finish();
                        break;
                }
            }

            @Override
            public void onError(int code, String msg) {
                ToastUtils.show(mContext, msg);
                StyledDialogUtils.getInstance().dismissLoading();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.show(mContext, e.getMessage());
                StyledDialogUtils.getInstance().dismissLoading();
            }
        });


    }

    IWXAPI api;

    /**
     * 微信支付
     */
    private void wxPay(WxPayBean bean) {
        api = WXAPIFactory.createWXAPI(RechargeActivity.this, null);
        api.registerApp(Constants.WX_APP_ID);//微信的appkey
        PayReq request = new PayReq();
        request.appId = bean.getPay_info().getAppid();
        request.partnerId = bean.getPay_info().getPartnerid();
        request.prepayId = bean.getPay_info().getPrepayid();
        request.packageValue = bean.getPay_info().getPackageX();
        request.nonceStr = bean.getPay_info().getNoncestr();
        request.timeStamp = bean.getPay_info().getTimestamp();
        request.sign = bean.getPay_info().getSign();
        api.sendReq(request);
    }

    /**
     * 支付宝支付
     */
    private void aliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝支付的回调
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Map<String, String> result = (Map<String, String>) msg.obj;

                String resultStatus = result.get("resultStatus");

                if (resultStatus.equals("4000")) {
                    //支付失败
                    toast(getString(R.string.payment_failure));

                } else if (resultStatus.equals("9000")) {
                    //支付成功
                    toast(getString(R.string.payment_successful));
                } else {
                    toast(getString(R.string.payment_failure));
                }
            }
        }
    };

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }


}
