package com.benben.megamart.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.AFinalRecyclerViewAdapter;
import com.benben.megamart.adapter.ChooseCouponListAdapter;
import com.benben.megamart.adapter.DeliveryTimeListAdapter;
import com.benben.megamart.adapter.PaymentWayListAdapter;
import com.benben.megamart.adapter.SubmitOrderGoodsListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.api.PayResultListener;
import com.benben.megamart.bean.AliPayBean;
import com.benben.megamart.bean.CartGoodsListBean;
import com.benben.megamart.bean.CouponListBean;
import com.benben.megamart.bean.DefaultAddressBean;
import com.benben.megamart.bean.DeliveryTimeListBean;
import com.benben.megamart.bean.OrderGoodsListInfoBean;
import com.benben.megamart.bean.PayWayBean;
import com.benben.megamart.bean.SubmitOrderGoodsFormatBean;
import com.benben.megamart.bean.SubmitOrderResultInfoBean;
import com.benben.megamart.bean.WxPayBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.ui.mine.AddressManagerActivity;
import com.benben.megamart.utils.PayListenerUtils;
import com.benben.megamart.utils.PayPalHelper;
import com.benben.megamart.utils.RxBus;
import com.benben.megamart.utils.StatusBarUtils;
import com.benben.megamart.widget.PayWayDialog;
import com.google.gson.Gson;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-06-10.
 * Describe:提交订单页面
 */
public class SubmitOrderActivity extends BaseActivity {
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    @BindView(R.id.tv_zip_code)
    TextView tvZipCode;
    @BindView(R.id.tv_user_address)
    TextView tvUserAddress;
    @BindView(R.id.rlv_goods_list)
    RecyclerView rlvGoodsList;
    @BindView(R.id.tv_delivery_time)
    TextView tvDeliveryTime;
    @BindView(R.id.llyt_choose_delivery_time)
    LinearLayout llytChooseDeliveryTime;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.llyt_choose_coupon)
    LinearLayout llytChooseCoupon;
    @BindView(R.id.tv_should_pay_money)
    TextView tvShouldPayMoney;
    @BindView(R.id.tv_payable)
    TextView tvPayable;
    @BindView(R.id.tv_bottom_total_money)
    TextView tvBottomTotalMoney;
    @BindView(R.id.btn_submit_order)
    Button btnSubmitOrder;
    @BindView(R.id.rlyt_user_delivery_info)
    RelativeLayout rlytUserDeliveryInfo;
    @BindView(R.id.llyt_user_info)
    LinearLayout llytUserInfo;
    @BindView(R.id.tv_freight)
    TextView tvFreight;
    @BindView(R.id.tv_goods_total_number)
    TextView tvGoodsTotalNumber;
    @BindView(R.id.tv_select_address)
    TextView tvSelectAddress;
    @BindView(R.id.rlv_payment_way)
    RecyclerView rlvPaymentWay;
    //支付方式  1 余额  2 支付宝  3 微信  4 paypal  5 货到付款
    private String mPayWay = "1";
    //底部导航栏的高度
    private int mNavigationBarHeight = 0;
    //商品列表adapter
    private SubmitOrderGoodsListAdapter mGoodsListAdapter;
    //商品列表bean
    private List<CartGoodsListBean> mGoodsList;
    //用户id
    private String mUserId;
    //地址id
    private String mAddressId = "";
    //优惠券id
    private String mCouponId = "0";
    //实际商品列表
    private ArrayList<CartGoodsListBean> mFilterGoodsList;
    //送货时间列表
    private List<DeliveryTimeListBean> mDeliveryTimeList;
    //送货时间
    private String mDeliveryTime = "";
    //优惠券列表
    private List<CouponListBean> mCouponList;
    //订单信息
    private OrderGoodsListInfoBean mOrderGoodsListInfoBean;
    //支付方式列表
    private List<PayWayBean.PaywayListBean> mPaymentWayList;
    //支付方式列表adapter
    private PaymentWayListAdapter mPaymentWayListAdapter;
    //订单id
    private String mOrderId;
    //选择的优惠券bean
    private CouponListBean mCouponListBean;
    private DeliveryTimeListBean mDeliveryTimeListBean;
    private String stripeToken;
    private PayWayBean mPayWayBean;
    private double mCartTotalMoney;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_submit_order;
    }

    @Override
    protected void initData() {
        mUserId = MegaMartApplication.mPreferenceProvider.getUId();
        SubmitOrderGoodsFormatBean submitOrderGoodsFormatBean = (SubmitOrderGoodsFormatBean) getIntent().getSerializableExtra(Constants.EXTRA_KEY_GOODS_LIST);
        mCartTotalMoney = getIntent().getDoubleExtra("total_money", 0);
        mGoodsList = submitOrderGoodsFormatBean.getGoodsList();
        //开启PayPalService
        PayPalHelper.getInstance().startPayPalService(this);
        //过滤组合商品列表
        filterGoodsList();

        centerTitle.setText(getString(R.string.submit_order));

        //商品列表
        rlvGoodsList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mGoodsListAdapter = new SubmitOrderGoodsListAdapter(mContext);
        rlvGoodsList.setAdapter(mGoodsListAdapter);
        mGoodsListAdapter.refreshList(mGoodsList);

        //支付方式列表
        rlvPaymentWay.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mPaymentWayListAdapter = new PaymentWayListAdapter(mContext);
        rlvPaymentWay.setAdapter(mPaymentWayListAdapter);
        mPaymentWayListAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<PayWayBean.PaywayListBean>() {
            @Override
            public void onItemClick(View view, int position, PayWayBean.PaywayListBean model) {
                mPayWay = model.getPay_id();
            }

            @Override
            public void onItemLongClick(View view, int position, PayWayBean.PaywayListBean model) {

            }
        });


        //获取默认地址
        // getDefaultAddress();
        //获得订单信息
        getOrderInfo();
        //获取用户余额
        // getUserBalance();
        //获取支付方式列表
        getPaymentWay();


        //微信支付的回调
        PayListenerUtils.getInstance(mContext).addListener(new PayResultListener() {
            @Override
            public void onPaySuccess() {
                goPayResult(3);
                if (onPayCallback != null) {
                    onPayCallback.paySuccess();
                }
            }

            @Override
            public void onPayError() {
                if (onPayCallback != null) {
                    onPayCallback.payFail();
                }
            }

            @Override
            public void onPayCancel() {
                if (onPayCallback != null) {
                    onPayCallback.payCancel();
                }
            }
        });

        onPayCallback = new PayWayDialog.OnPayCallback() {
            @Override
            public void paySuccess() {
                toast(getResources().getString(R.string.payment_successful));
            }

            @Override
            public void payCancel() {
                toast(getResources().getString(R.string.payment_cancel));
            }

            @Override
            public void payFail() {
                toast(getResources().getString(R.string.payment_failure));
            }
        };
    }

    //获取支付方式列表
    private void getPaymentWay() {

        BaseOkHttpClient.newBuilder()
                .addParam("user_id", mUserId)
                .addParam("request_type", 1)//请求参数：1：订单页请求；2:余额支付请求
                .addParam("pay_money", mCartTotalMoney)
                .url(NetUrlUtils.GET_ORDER_PAY_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取支付方式列表----" + result);
                        StyledDialogUtils.getInstance().dismissLoading();
                        if (!StringUtils.isEmpty(result)) {
                            mPayWayBean = JSONUtils.jsonString2Bean(result, PayWayBean.class);
                            mPaymentWayListAdapter.setIsBuy(mPayWayBean.getIs_buy());
                            mPaymentWayListAdapter.setPrompt(mPayWayBean.getPrompt());
                            mPaymentWayList = mPayWayBean.getPayway_list();
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

    //获取优惠券列表
    private void getCouponList() {

        JSONArray normalGoodsListJson = new JSONArray();
        JSONArray composeGoodsListJson = new JSONArray();

        try {
            if (mFilterGoodsList != null && mFilterGoodsList.size() > 0) {

                for (CartGoodsListBean goodsListBean : mFilterGoodsList) {
                    if (goodsListBean.getPreferential_id() == -1) {

                        JSONObject jo = new JSONObject();
                        jo.put("goods_id", goodsListBean.getGoods_id());
                        jo.put("goods_price", goodsListBean.getGoods_amount());
                        jo.put("goods_number", goodsListBean.getGoods_number());
                        normalGoodsListJson.put(jo);
                    } else {

                        JSONObject jo = new JSONObject();
                        jo.put("preferential_id", goodsListBean.getPreferential_id());
                        jo.put("preferential_amount", goodsListBean.getPreferential_price());
                        jo.put("preferential_number", goodsListBean.getGoods_number());
                        composeGoodsListJson.put(jo);
                    }

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .addParam("goods_list", normalGoodsListJson)
                .addParam("preferential_list", composeGoodsListJson)
                .addParam("user_id", mUserId)
                .url(NetUrlUtils.GET_COUPON_ORDER_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取优惠券列表----" + result);
                        StyledDialogUtils.getInstance().dismissLoading();
                        if (!StringUtils.isEmpty(result)) {
                            String noteJson = JSONUtils.getNoteJson(result, "usable_list");
                            mCouponList = JSONUtils.jsonString2Beans(noteJson, CouponListBean.class);

                        }
                        if (mCouponList != null && mCouponList.size() > 0) {
                            showChooseCouponDialog();
                        } else {
                            toast(getResources().getString(R.string.no_usable_coupon));
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

    //获取送货时间列表
    private void getDeliveryTimeList() {
        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .addParam("address_id", mAddressId)
                .addParam("user_id", mUserId)
                .url(NetUrlUtils.GET_DELIVERY_TIME_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取送货时间----" + result);
                        StyledDialogUtils.getInstance().dismissLoading();
                        if (!StringUtils.isEmpty(result)) {
                            String noteJson = JSONUtils.getNoteJson(result, "delivery_list");
                            mDeliveryTimeList = JSONUtils.jsonString2Beans(noteJson, DeliveryTimeListBean.class);

                        }

                        if (mDeliveryTimeList != null && mDeliveryTimeList.size() > 0) {
                            showChooseDeliveryTimeDialog();
                        } else {
                            toast(getResources().getString(R.string.no_select_delivery_time));
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

    //过滤重复的组合商品
    private void filterGoodsList() {

        int flag = -1;
        mFilterGoodsList = new ArrayList<>();
        for (int i = 0; i < mGoodsList.size(); i++) {
            if (mGoodsList.get(i).getPreferential_id() == -1) {
                mFilterGoodsList.add(mGoodsList.get(i));
            } else {
                if (flag == i) {
                    continue;
                }
                mFilterGoodsList.add(mGoodsList.get(i));
                flag = (i + 1);
            }
        }
        int goodsNumber = 0;
        for (int i = 0; i < mFilterGoodsList.size(); i++) {
            goodsNumber += mFilterGoodsList.get(i).getGoods_number();
        }
        tvGoodsTotalNumber.setText(getString(R.string.goods_total, goodsNumber));
    }

    //获得订单信息
    private void getOrderInfo() {
        Log.e(Constants.WHK_TAG, "getOrderInfo: ");
        JSONArray normalGoodsListJson = new JSONArray();
        JSONArray composeGoodsListJson = new JSONArray();

        try {
            if (mFilterGoodsList != null && mFilterGoodsList.size() > 0) {

                for (CartGoodsListBean goodsListBean : mFilterGoodsList) {
                    if (goodsListBean.getPreferential_id() == -1) {

                        JSONObject jo = new JSONObject();
                        jo.put("goods_id", goodsListBean.getGoods_id());
                        jo.put("goods_number", goodsListBean.getGoods_number());
                        normalGoodsListJson.put(jo);
                    } else {

                        JSONObject jo = new JSONObject();
                        jo.put("preferential_id", goodsListBean.getPreferential_id());
                        jo.put("preferential_number", goodsListBean.getGoods_number());
                        composeGoodsListJson.put(jo);
                    }

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BaseOkHttpClient.newBuilder()
                .addParam("goods_list", normalGoodsListJson)
                .addParam("preferential_list", composeGoodsListJson)
                .addParam("coupon_id", mCouponId)
//                .addParam("address_id", mAddressId)
                .addParam("user_id", mUserId)
                .url(NetUrlUtils.GET_ORDER_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {

                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取订单信息----" + result);
                        StyledDialogUtils.getInstance().dismissLoading();
                        if (!StringUtils.isEmpty(result)) {
                            mOrderGoodsListInfoBean = JSONUtils.jsonString2Bean(result, OrderGoodsListInfoBean.class);

                            if (mOrderGoodsListInfoBean != null) {
                                String freightText = "<font color='#333333'>(" + mOrderGoodsListInfoBean.getFreight_text() + ")</font><font color='#EC5413'>＄" + mOrderGoodsListInfoBean.getFreight() + "</font>";
                                tvFreight.setText("0".equals(mOrderGoodsListInfoBean.getFreight()) ? mOrderGoodsListInfoBean.getFreight_text() : Html.fromHtml(freightText));
                                tvBottomTotalMoney.setText(getResources().getString(R.string.goods_price, mOrderGoodsListInfoBean.getTotal()));
                                tvShouldPayMoney.setText(getString(R.string.goods_price, mOrderGoodsListInfoBean.getTotal()));


                                if (mOrderGoodsListInfoBean.getAddress_list() != null && mOrderGoodsListInfoBean.getAddress_list().size() > 0) {
                                    tvSelectAddress.setVisibility(View.GONE);
                                    mAddressId = mOrderGoodsListInfoBean.getAddress_list().get(0).getAddress_id();
                                    tvUserAddress.setText(mOrderGoodsListInfoBean.getAddress_list().get(0).getUnit_number()+"  "+mOrderGoodsListInfoBean.getAddress_list().get(0).getStreet_number()+"  "+mOrderGoodsListInfoBean.getAddress_list().get(0).getAddress_info()+"  "+mOrderGoodsListInfoBean.getAddress_list().get(0).getCity()+"  "+mOrderGoodsListInfoBean.getAddress_list().get(0).getProvince());
                                    tvUserName.setText(mOrderGoodsListInfoBean.getAddress_list().get(0).getLink_man());
                                    tvZipCode.setText(getResources().getString(R.string.zip_code, mOrderGoodsListInfoBean.getAddress_list().get(0).getZip_code()));
                                    tvUserPhone.setText(mOrderGoodsListInfoBean.getAddress_list().get(0).getLink_phone());

                                } else {
                                    tvSelectAddress.setVisibility(View.VISIBLE);
                                    mAddressId = "";
                                    tvUserAddress.setText("");
                                    tvUserName.setText("");
                                    tvZipCode.setText("");
                                    tvUserPhone.setText("");
                                }

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

    //获取默认地址
    private void getDefaultAddress() {

        BaseOkHttpClient.newBuilder()
                .addParam("user_id", mUserId)
                .url(NetUrlUtils.GET_DEFAULT_ADDRESS)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取默认地址----" + result);
                        StyledDialogUtils.getInstance().dismissLoading();
                        if (!StringUtils.isEmpty(result)) {
                            String noteJson = JSONUtils.getNoteJson(result, "address_list");
                            List<DefaultAddressBean> defaultAddressList = JSONUtils.jsonString2Beans(noteJson, DefaultAddressBean.class);

                            if (defaultAddressList != null && defaultAddressList.size() > 0) {
                                tvSelectAddress.setVisibility(View.GONE);
                                mAddressId = String.valueOf(defaultAddressList.get(0).getAddress_id());
                                tvUserAddress.setText(defaultAddressList.get(0).getAddress_info());
                                tvUserName.setText(defaultAddressList.get(0).getLink_man());
                                tvZipCode.setText(getResources().getString(R.string.zip_code, defaultAddressList.get(0).getZip_code()));
                                tvUserPhone.setText(defaultAddressList.get(0).getLink_phone());
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

/*    //获取用户余额
    private void getUserBalance() {

        BaseOkHttpClient.newBuilder()
                .addParam("user_id", mUserId)
                .url(NetUrlUtils.MINE_USER_BALANCE)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取用户余额----" + result);
                        StyledDialogUtils.getInstance().dismissLoading();
                        String mBalance = JSONUtils.getNoteJson(result, "user_balance");

                        RadioButton rbBalance = (RadioButton) rgPaymentWay.getChildAt(0);
                        rbBalance.setText(getResources().getString(R.string.balance_payment) + getResources().getString(R.string.balance_money, mBalance));
                        SpannableStringBuilder builder = new SpannableStringBuilder(rbBalance.getText().toString());
                        //设置前景色为蓝色
                        ForegroundColorSpan blue = new ForegroundColorSpan(Color.parseColor("#F67F4C"));
                        //改变第0-3个字体颜色为蓝色
                        builder.setSpan(blue, 4, (rbBalance.getText().toString().length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //改变第0-3个字体大小
                        Parcel p = Parcel.obtain();
                        p.writeInt(14);//字体大小
                        p.writeInt(8);//是否是dip单位
                        p.setDataPosition(0);
                        builder.setSpan(new AbsoluteSizeSpan(p), 4, (rbBalance.getText().toString().length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        rbBalance.setText(builder);

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

    }*/

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
        mNavigationBarHeight = StatusBarUtils.getNavigationBarHeight(mContext);
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
                                pay("6");
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

    //弹出选择优惠券的dialog
    private void showChooseCouponDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.NoBackGroundDialog);
        View view = View.inflate(mContext, R.layout.dialog_choose_coupon, null);

        RecyclerView rlvCouponList = view.findViewById(R.id.rlv_coupon_list);
        Button btnAffirm = view.findViewById(R.id.btn_affirm);
        rlvCouponList.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        ChooseCouponListAdapter chooseCouponListAdapter = new ChooseCouponListAdapter(mContext);
        rlvCouponList.setAdapter(chooseCouponListAdapter);
        chooseCouponListAdapter.refreshList(mCouponList);

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        chooseCouponListAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<CouponListBean>() {
            @Override
            public void onItemClick(View view, int position, CouponListBean model) {
                mCouponListBean = model;
            }

            @Override
            public void onItemLongClick(View view, int position, CouponListBean model) {

            }
        });

        btnAffirm.setOnClickListener(v -> {

            if (mCouponListBean != null && mCouponListBean.isSelect()) {
                mCouponId = String.valueOf(mCouponListBean.getCoupon_id());
                tvCoupon.setText(mCouponListBean.getCoupon_name() + getResources().getString(R.string.blank_space) + mCouponListBean.getCoupons_price());
                //刷新订单信息
                getOrderInfo();
            } else {
                mCouponId = "0";
                tvCoupon.setText(getResources().getString(R.string.choose_coupon));
            }

            if (bottomSheetDialog.isShowing()) {
                bottomSheetDialog.dismiss();
            }
        });
    }

    //弹出选择送货时间的dialog
    private void showChooseDeliveryTimeDialog() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.NoBackGroundDialog);
        View view = View.inflate(mContext, R.layout.dialog_choose_delivery_time, null);

        RecyclerView rlvList = view.findViewById(R.id.rlv_list);
        Button btnAffirm = view.findViewById(R.id.btn_affirm);
        rlvList.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        DeliveryTimeListAdapter deliveryTimeListAdapter = new DeliveryTimeListAdapter(mContext);
        rlvList.setAdapter(deliveryTimeListAdapter);
        deliveryTimeListAdapter.refreshList(mDeliveryTimeList);

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        deliveryTimeListAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<DeliveryTimeListBean>() {
            @Override
            public void onItemClick(View view, int position, DeliveryTimeListBean model) {
                mDeliveryTimeListBean = model;
            }

            @Override
            public void onItemLongClick(View view, int position, DeliveryTimeListBean model) {

            }
        });


        btnAffirm.setOnClickListener(v -> {

            if (mDeliveryTimeListBean != null && mDeliveryTimeListBean.isSelect()) {
                mDeliveryTime = String.valueOf(mDeliveryTimeListBean.getDelivery_time());
                tvDeliveryTime.setText(mDeliveryTime);
                //刷新订单信息
                getOrderInfo();
            } else {
                mDeliveryTime = "";
                tvDeliveryTime.setText(getResources().getString(R.string.choose_delivery_time));
            }

            if (bottomSheetDialog.isShowing()) {
                bottomSheetDialog.dismiss();
            }
        });
        //setMarginNavigationBar();
    }

    //计算虚拟按键的高度 setmargin
   /* private void setMarginNavigationBar() {
        FrameLayout.LayoutParams bottomlayoutParams = (FrameLayout.LayoutParams) mPvOptions.getDialogContainerLayout().getLayoutParams();
        bottomlayoutParams.setMargins(0, 0, 0, mNavigationBarHeight);
        mPvOptions.getDialogContainerLayout().setLayoutParams(bottomlayoutParams);
    }*/

    private TextView tvShowPopupWindow;


    //支付的回调
    private PayWayDialog.OnPayCallback onPayCallback;

    //提交订单
    private void submitOrder() {

        if (StringUtils.isEmpty(mAddressId)) {
            toast(getResources().getString(R.string.no_choose_address));
            return;
        }
        if (StringUtils.isEmpty(mDeliveryTime)) {
            toast(getResources().getString(R.string.no_choose_delivery_time));
            return;
        }

        JSONArray normalGoodsListJson = new JSONArray();
        JSONArray composeGoodsListJson = new JSONArray();

        try {
            if (mFilterGoodsList != null && mFilterGoodsList.size() > 0) {

                for (CartGoodsListBean goodsListBean : mFilterGoodsList) {
                    if (goodsListBean.getPreferential_id() == -1) {

                        JSONObject jo = new JSONObject();
                        jo.put("goods_id", goodsListBean.getGoods_id());
                        jo.put("goods_number", goodsListBean.getGoods_number());
                        normalGoodsListJson.put(jo);
                    } else {

                        JSONObject jo = new JSONObject();
                        jo.put("preferential_id", goodsListBean.getPreferential_id());
                        jo.put("preferential_number", goodsListBean.getGoods_number());
                        composeGoodsListJson.put(jo);
                    }

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(Constants.WHK_TAG, "submitOrder: goods_list=" + normalGoodsListJson + "preferential_list=" + composeGoodsListJson + "freight=" + mOrderGoodsListInfoBean.getFreight()
                + "total=" + mOrderGoodsListInfoBean.getTotal() + "coupon_id=" + mCouponId + "address_id=" + mAddressId + "payway_id=" + mPayWay + "delivery_time=" + mDeliveryTime + "user_id=" + mUserId);

        if (mOrderGoodsListInfoBean == null) {
            toast(getResources().getString(R.string.server_exception));
            return;
        }
        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .addParam("goods_list", normalGoodsListJson)
                .addParam("preferential_list", composeGoodsListJson)
                .addParam("freight", mOrderGoodsListInfoBean.getFreight())
                .addParam("total", mOrderGoodsListInfoBean.getTotal())
                .addParam("coupon_id", mCouponId)
                .addParam("address_id", mAddressId)
                .addParam("payway_id", mPayWay)
                .addParam("delivery_time", mDeliveryTime)
                .addParam("user_id", mUserId)
                .url(NetUrlUtils.ADD_ORDER)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 提交订单----" + result);
                        StyledDialogUtils.getInstance().dismissLoading();
                        SubmitOrderResultInfoBean orderResultInfoBean = JSONUtils.jsonString2Bean(result, SubmitOrderResultInfoBean.class);

                        if (orderResultInfoBean != null) {
                            RxBus.getInstance().post(101);
                            mOrderId = orderResultInfoBean.getOrder_id();
                            switch (Integer.parseInt(orderResultInfoBean.getPay_way())) {
                                case 1://余额
                                    //订单状态：1、待支付 2、已支付 3、申请售后中 4、售后完成
                                    if (orderResultInfoBean.getOrder_type() == 2) {
                                        goPayResult(1);
                                    }
                                    break;
                                case 2://支付宝
                                    pay("2");
                                    break;
                                case 3://微信
                                    pay("3");
                                    break;
                                case 4://paypal
                                    pay("4");
                                    break;
                                case 5://货到付款
                                    if (orderResultInfoBean.getOrder_type() == 2) {
                                        goPayResult(5);
                                    }
                                    break;
                                case 6://stripe
                                    showStripePayDialog();
                                    break;
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

    private void goPayResult(int payWay) {
        Intent intent = new Intent(mContext, PaymentResultActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.EXTRA_KEY_PAYMENT_WAY, payWay);
        intent.putExtra(Constants.EXTRA_KEY_ORDER_ID, mOrderId);
        intent.putExtra(Constants.EXTRA_KEY_PAYMENT_MONEY, mOrderGoodsListInfoBean.getTotal());
        startActivity(intent);
        finish();
    }

    /**
     * 去付款
     */
    private void pay(String payWay) {

        BaseOkHttpClient.newBuilder().url(NetUrlUtils.ADD_PAYMENT)
                .addParam("user_id", mUserId)
                .addParam("amount", mOrderGoodsListInfoBean.getTotal())//支付金额
                .addParam("pay_way", payWay)//支付方式：2:支付宝；3:微信支付；4:paypal 6:stripe
                .addParam("pay_type", 2)//支付类型：1:充值余额；2:订单支付
                .addParam("order_id", mOrderId)//订单ID
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
                switch (payWay) {
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
                        //PayPalHelper.getInstance().doPayPalPay(mContext);
                        break;
                    case "6":
                        //stripe支付
                        goPayResult(6);
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

        api = WXAPIFactory.createWXAPI(mContext, null);
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
                PayTask alipay = new PayTask(mContext);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == 201) {
//            getDefaultAddress();
            Log.e(Constants.WHK_TAG, "onActivityResult: 刷新订单信息");
            getOrderInfo();
        }
        //paypal支付的回调
        PayPalHelper.getInstance().confirmPayResult(mContext, requestCode, resultCode, data, new PayPalHelper.DoResult() {
            @Override
            public void confirmSuccess(String id) {
                if (onPayCallback != null) {
                    onPayCallback.paySuccess();
                }

            }

            @Override
            public void confirmNetWorkError() {
                if (onPayCallback != null) {
                    onPayCallback.payFail();
                }
            }

            @Override
            public void customerCanceled() {
                if (onPayCallback != null) {
                    onPayCallback.payCancel();
                }

            }

            @Override
            public void confirmFuturePayment() {

            }

            @Override
            public void invalidPaymentConfiguration() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PayPalHelper.getInstance().stopPayPalService(mContext);
    }

    @OnClick({R.id.rl_back, R.id.rlyt_user_delivery_info, R.id.llyt_choose_delivery_time, R.id.llyt_choose_coupon, R.id.btn_submit_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rlyt_user_delivery_info://选择地址
                Intent intent = new Intent(mContext, AddressManagerActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.llyt_choose_delivery_time://选择送货时间
                //获取送货时间列表
                getDeliveryTimeList();
                break;
            case R.id.llyt_choose_coupon://选择优惠券
                //获取优惠券列表
                getCouponList();
                break;
            case R.id.btn_submit_order://提交订单
                if (StringUtils.isEmpty(mAddressId)) {
                    toast(getString(R.string.no_choose_address));
                    return;
                }
                //提交订单
                submitOrder();
                /*Intent intent = new Intent(mContext, PaymentResultActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();*/
                break;

        }
    }


}
