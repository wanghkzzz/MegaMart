package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-18.
 * Describe:优惠券bean
 */
public class CouponListBean {

    /**
     * coupons_price : 5.00
     * coupon_id : 19
     * coupon_name : 满减券
     * limit_price : 30
     */

    private String coupons_price;
    private int coupon_id;
    private String coupon_name;
    private int limit_price;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getCoupons_price() {
        return coupons_price;
    }

    public void setCoupons_price(String coupons_price) {
        this.coupons_price = coupons_price;
    }

    public int getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public int getLimit_price() {
        return limit_price;
    }

    public void setLimit_price(int limit_price) {
        this.limit_price = limit_price;
    }
}
