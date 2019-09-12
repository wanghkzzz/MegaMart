package com.benben.megamart.widget;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.benben.megamart.R;
import com.benben.megamart.bean.PayBean;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.StringUtil;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2019/4/7.
 */

public class PayWayDialog extends Dialog {

    //余额的item，如不需要isShowBalance传true
    @BindView(R.id.dialog_my_wallet)
    LinearLayout dialogMyWallet;
    @BindViews({R.id.iv_balance_select, R.id.iv_alipay_select, R.id.iv_wxpay_select})
    List<ImageView> checks;
    //显示需要支付的金额
    @BindView(R.id.tv_price)
    TextView tvPrice;
    //去支付
    @BindView(R.id.tv_go_pay)
    TextView tvGoPay;

    /**
     * 判断是否需要显示余额支付 true为不显示 false显示
     **/
    private boolean isShowBalance;

    /**
     * 区别三种支付方式 0:我的钱包 1:支付宝 2:微信支付
     **/
    private int payWay = 1;

    private Activity context;
    private CashPwdPopupWindow cashPwdPopupWindow;//密码输入框

    private String orderNumber = "";//支付都是订单号支付，是必传项

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    //支付的回调
    private OnPayCallback onPayCallback;

    private TextView tvShowPopupWindow;

    //安全键盘需要传入的，如不需要后续需要更改密码弹出框
    public void setTvShowPopupWindow(TextView tvShowPopupWindow) {
        this.tvShowPopupWindow = tvShowPopupWindow;
    }

    /**
     * isShowBalance 传入true 则不显示余额,就隐藏掉我的钱包, 否则则显示
     *
     * @param context
     * @param themeResId
     * @param isShowBalance
     */
    public PayWayDialog(Activity context, int themeResId, boolean isShowBalance, OnPayCallback onPayCallback) {
        super(context, themeResId);
        this.context = context;
        this.isShowBalance = isShowBalance;
        this.onPayCallback = onPayCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay);
        ButterKnife.bind(this);
        if (isShowBalance) {
            dialogMyWallet.setVisibility(View.GONE);
            checks.get(1).setImageResource(R.mipmap.icon_select_theme);
            payWay = 1;
        } else {
            payWay = 0;
        }


        //确定支付按钮
        tvGoPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (payWay == 0) {
                    cashPwdPopupWindow = new CashPwdPopupWindow(context, new CashPwdPopupWindow.CashInterface() {
                        @Override
                        public void PwdCallback(String pwd) {
                            if (cashPwdPopupWindow.isShowing()) {
                                cashPwdPopupWindow.dismiss();
                            }
                            //余额支付
                            pay(pwd);
                        }
                    }, tvShowPopupWindow);
                    cashPwdPopupWindow.showAtLocation(tvGoPay, Gravity.CENTER, 0, 0);
                } else if (payWay == 1) {
                    //支付宝支付
                    pay("");
                } else if (payWay == 2) {
                    //微信支付
                    pay("");
                }
            }
        });

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = AbsListView.LayoutParams.MATCH_PARENT;
        lp.y = 0;//设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp);
    }

    /**
     * 设置显示的金额金额
     *
     * @param num
     */
    public void setRechargeNum(String num) {
        tvPrice.setText("¥ " + StringUtil.numberFormat(Double.parseDouble(num)));
    }

    @OnClick({R.id.dialog_my_wallet, R.id.dialog_zhifubao, R.id.dialog_wechat})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_my_wallet:
                checkChanges(0);
                break;
            case R.id.dialog_zhifubao:
                checkChanges(1);
                break;
            case R.id.dialog_wechat:
                checkChanges(2);
                break;
        }
    }

    /**
     * 改变选中
     */
    private void checkChanges(int index) {
        for (int i = 0; i < 3; i++) {
            if (i != index) {
                checks.get(i).setImageResource(R.mipmap.icon_select_no);
            }
        }
        payWay = index;
        checks.get(index).setImageResource(R.mipmap.icon_select_theme);
    }

    /**
     * 去付款
     */
    private void pay(String password) {
        String type = "";
        if (payWay == 0) {
            type = "balance";
        } else if (payWay == 1) {
            type = "alipay";
        } else if (payWay == 2) {
            type = "wxpay";
        }
        BaseOkHttpClient.newBuilder().url("")
                .addParam("type", "" + type)
                .addParam("orderNumber", "" + orderNumber)
                .addParam("payPassword", "" + password)
                .post()
                .json()
                .build().enqueue(context, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {

                if (payWay == 0) {
                    //余额支付
                    if (onPayCallback != null) {
                        onPayCallback.paySuccess();
                    }
                } else if (payWay == 1) {
                    //支付宝支付
                    Gson gson = new Gson();
                    PayBean bean = gson.fromJson(result, PayBean.class);
                    alipay(bean.getPay_info());
                } else if (payWay == 2) {
                    //微信支付
                    Gson gson = new Gson();
                    PayBean bean = gson.fromJson(result, PayBean.class);
                    wxpay(bean);
                }
            }

            @Override
            public void onError(int code, String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    IWXAPI api;

    /**
     * 微信支付
     */
    private void wxpay(PayBean bean) {

        api = WXAPIFactory.createWXAPI(context, null);
        api.registerApp("wxf92112054c7eb268");//微信的appkey

        PayReq request = new PayReq();
        request.appId = bean.getAppId();
        request.partnerId = bean.getPartnerId();
        request.prepayId = bean.getPrepayId();
        request.packageValue = bean.getPackageValue();
        request.nonceStr = bean.getNonceStr();
        request.timeStamp = bean.getTimeStamp();
        request.sign = bean.getSign();
        api.sendReq(request);
    }


    /**
     * 支付宝支付
     */
    private void alipay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
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
                    if (onPayCallback != null) {
                        onPayCallback.payFail();
                    }
                } else if (resultStatus.equals("9000")) {
                    //支付成功
                    if (onPayCallback != null) {
                        onPayCallback.paySuccess();
                    }
                } else {
                    if (onPayCallback != null) {
                        onPayCallback.payFail();
                    }
                }
            }
        }
    };

    /**
     * 支付的回调
     */
    public interface OnPayCallback {
        void paySuccess();

        void payCancel();

        void payFail();
    }
}
