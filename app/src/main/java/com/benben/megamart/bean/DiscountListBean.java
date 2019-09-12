package com.benben.megamart.bean;

import java.util.List;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/18
 * Time: 9:38
 */
public class DiscountListBean {

    /**
     * is_used : 0
     * coupon_list : [{"coupon_id":14,"coupon_color":null,"validity":"2019-06-30 09:19:44","coupons_price":"10.00","limit_price":10},{"coupon_id":12,"coupon_color":null,"validity":"2019-06-30 13:14:17","coupons_price":"30.00","limit_price":200}]
     */

    private String is_used;
    private List<CouponListBean> coupon_list;

    public String getIs_used() {
        return is_used;
    }

    public void setIs_used(String is_used) {
        this.is_used = is_used;
    }

    public List<CouponListBean> getCoupon_list() {
        return coupon_list;
    }

    public void setCoupon_list(List<CouponListBean> coupon_list) {
        this.coupon_list = coupon_list;
    }

    public static class CouponListBean {
        /**
         * coupon_id : 14
         * coupon_color : null
         * validity : 2019-06-30 09:19:44
         * coupons_price : 10.00
         * limit_price : 10
         */

        private int coupon_id;
        private String coupon_color;
        private String validity;
        private String coupons_price;
        private String coupons_name;
        private String limit_price;
        private String coupon_validity;

        public String getCoupon_validity() {
            return coupon_validity;
        }

        public void setCoupon_validity(String coupon_validity) {
            this.coupon_validity = coupon_validity;
        }

        public String getCoupons_name() {
            return coupons_name;
        }

        public void setCoupons_name(String coupons_name) {
            this.coupons_name = coupons_name;
        }

        public int getCoupon_id() {
            return coupon_id;
        }

        public void setCoupon_id(int coupon_id) {
            this.coupon_id = coupon_id;
        }

        public String getCoupon_color() {
            return coupon_color;
        }

        public void setCoupon_color(String coupon_color) {
            this.coupon_color = coupon_color;
        }

        public String getValidity() {
            return validity;
        }

        public void setValidity(String validity) {
            this.validity = validity;
        }

        public String getCoupons_price() {
            return coupons_price;
        }

        public void setCoupons_price(String coupons_price) {
            this.coupons_price = coupons_price;
        }

        public String getLimit_price() {
            return limit_price;
        }

        public void setLimit_price(String limit_price) {
            this.limit_price = limit_price;
        }
    }
}
