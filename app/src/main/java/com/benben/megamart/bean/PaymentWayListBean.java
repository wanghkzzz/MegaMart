package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-19.
 * Describe:支付方式列表bean
 */
public class PaymentWayListBean {

    /**
     * pay_id : 1
     * pay_name : 余额支付
     * pay_logo : http://megamart.brd-techi.com/assets/img/icon_yue.png
     * balance : 0.00
     */

    private String pay_id;
    private String pay_name;
    private String pay_logo;
    private String balance;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getPay_id() {
        return pay_id;
    }

    public void setPay_id(String pay_id) {
        this.pay_id = pay_id;
    }

    public String getPay_name() {
        return pay_name;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }

    public String getPay_logo() {
        return pay_logo;
    }

    public void setPay_logo(String pay_logo) {
        this.pay_logo = pay_logo;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
