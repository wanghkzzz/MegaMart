package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-28.
 * Describe:充值订单bean
 */
public class RechargeOrderBean {

    /**
     * user_id : 10
     * order_id : 148
     * order_sn : 20190628115406807990
     * amount : 1000
     * pay_way : 2
     */

    private String user_id;
    private String order_id;
    private String order_sn;
    private String amount;
    private String pay_way;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPay_way() {
        return pay_way;
    }

    public void setPay_way(String pay_way) {
        this.pay_way = pay_way;
    }
}
