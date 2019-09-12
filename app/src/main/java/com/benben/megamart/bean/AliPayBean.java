package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-19.
 * Describe:支付宝bean
 */
public class AliPayBean {

    /**
     * pay_info : app_id=2088921871712828&biz_content=%7B%22body%22%3A%22neirong%22%2C%22subject%22%3A%22mega_mart%22%2C%22out_trade_no%22%3A%2220190619173750132619%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%222.33%22%2C%22passback_params%22%3A%2220190619173750132619%22%2C%22goods_type%22%3A1%2C%22product_code%22%3A%22FAST_INSTANT_TRADE_PAY%22%7D&body=neirong&charset=utf-8&format=JSON&goods_type=1&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fmegamart.brd-techi.com%2Fapi%2Fv1%2Fpay%2FalipayCallback&out_trade_no=20190619173750132619&passback_params=20190619173750132619&product_code=FAST_INSTANT_TRADE_PAY&return_url=http%3A%2F%2Fmegamart.brd-techi.com%2Fmobile%2ForderConfirm&sign_type=RSA2&subject=mega_mart&timeout_express=30m&timestamp=2019-06-19+17%3A37%3A50&total_amount=2.33&version=1.0&sign=NnS1Hcimc3eKVLlYsKlg7MGhSnRkFm0rdZ1mOzoooIK7HThKDWHjEtHrJBJz4eoJDH36HGujTnplSLQ6eM6muXaukkB2lP%2BwEvVd3KnpfJ0fmvEvn3rcZA0Ke7xhFRktAFFroDpnRzZ4m9UWDAivuVVgssqdPQvorEG2Psv%2BVQ4%3D
     */

    private String pay_info;

    public String getPay_info() {
        return pay_info;
    }

    public void setPay_info(String pay_info) {
        this.pay_info = pay_info;
    }
}
