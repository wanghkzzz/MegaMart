package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-07-08.
 * Describe: 领取优惠券bean
 */
public class ReceiveCouponListBean {


    /**
     * coupon_id : 11
     * coupons_name : 满100减20券
     * use_end_time : 1563602975
     * validity_type : 0
     * validity : 0
     * limit_price : 100
     * coupons_price : 20
     * number : 106
     * stock : 94
     * coupon_validity : 1563602975
     * user_coupons : 0
     */

    private int coupon_id;
    private String coupons_name;
    private String use_end_time;
    private String validity_type;
    private String validity;
    private String limit_price;
    private String coupons_price;
    private int number;
    private int stock;
    private String coupon_validity;
    private String user_coupons;

    public int getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getCoupons_name() {
        return coupons_name;
    }

    public void setCoupons_name(String coupons_name) {
        this.coupons_name = coupons_name;
    }

    public String getUse_end_time() {
        return use_end_time;
    }

    public void setUse_end_time(String use_end_time) {
        this.use_end_time = use_end_time;
    }

    public String getValidity_type() {
        return validity_type;
    }

    public void setValidity_type(String validity_type) {
        this.validity_type = validity_type;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getLimit_price() {
        return limit_price;
    }

    public void setLimit_price(String limit_price) {
        this.limit_price = limit_price;
    }

    public String getCoupons_price() {
        return coupons_price;
    }

    public void setCoupons_price(String coupons_price) {
        this.coupons_price = coupons_price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCoupon_validity() {
        return coupon_validity;
    }

    public void setCoupon_validity(String coupon_validity) {
        this.coupon_validity = coupon_validity;
    }

    public String getUser_coupons() {
        return user_coupons;
    }

    public void setUser_coupons(String user_coupons) {
        this.user_coupons = user_coupons;
    }
}
