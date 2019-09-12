package com.benben.megamart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/17
 * Time: 15:35
 */
public class OrderDetailBean implements Serializable {

    /**
     * order_id : 3
     * order_no : 20190615144347730947
     * order_status : 1
     * order_time : null
     * pay_money : 40.00
     * order_province : null
     * order_city : null
     * order_district : 0
     * order_address : 纤细地活着21312
     * order_username : 收货人123
     * order_phone : 13213692344
     * order_zip_code : 2218
     * shipping_fee : 0.00
     * goods_price : 40.00
     * coupons_price : 0.00
     * shipping_time : 1560646799
     * order_payway : 3
     * pay_time : 0
     * order_goods_count : 2
     * goods_list : [{"goods_id":6,"goods_name":"天天草莓","goods_img":"/uploads/20190531/fff3ed434702589ffccd53aa384ffb4b.jpg","goods_price":"30.00","goods_number":1,"goods_attr":"糖果"}]
     * preferential_list : []
     */

    private int order_id;
    private String order_no;
    private String order_status;
    private String order_status_text;
    private String order_time;
    private String pay_money;
    private String order_province;
    private String order_city;
    private int order_district;
    private String order_address;
    private String order_username;
    private String order_phone;
    private String order_zip_code;
    private String shipping_fee;
    private String goods_price;
    private String coupons_price;
    private String shipping_time;
    private String order_payway;
    private String unit_number;
    private String street_number;
    private int pay_time;
    private int order_goods_count;
    private List<GoodsListBean> goods_list;
    private List<PreferentialListBean> preferential_list;

    public List<PreferentialListBean> getPreferential_list() {
        return preferential_list;
    }

    public String getStreet_number() {
        return street_number;
    }

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
    }

    public String getUnit_number() {
        return unit_number;
    }

    public void setUnit_number(String unit_number) {
        this.unit_number = unit_number;
    }

    public void setPreferential_list(List<PreferentialListBean> preferential_list) {
        this.preferential_list = preferential_list;
    }

    public String getOrder_status_text() {
        return order_status_text;
    }

    public void setOrder_status_text(String order_status_text) {
        this.order_status_text = order_status_text;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_time() {
        return order_time==null?"":order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getPay_money() {
        return pay_money;
    }

    public void setPay_money(String pay_money) {
        this.pay_money = pay_money;
    }

    public String getOrder_province() {
        return order_province;
    }

    public void setOrder_province(String order_province) {
        this.order_province = order_province;
    }

    public String getOrder_city() {
        return order_city;
    }

    public void setOrder_city(String order_city) {
        this.order_city = order_city;
    }

    public int getOrder_district() {
        return order_district;
    }

    public void setOrder_district(int order_district) {
        this.order_district = order_district;
    }

    public String getOrder_address() {
        return order_address;
    }

    public void setOrder_address(String order_address) {
        this.order_address = order_address;
    }

    public String getOrder_username() {
        return order_username;
    }

    public void setOrder_username(String order_username) {
        this.order_username = order_username;
    }

    public String getOrder_phone() {
        return order_phone;
    }

    public void setOrder_phone(String order_phone) {
        this.order_phone = order_phone;
    }

    public String getOrder_zip_code() {
        return order_zip_code;
    }

    public void setOrder_zip_code(String order_zip_code) {
        this.order_zip_code = order_zip_code;
    }

    public String getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(String shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getCoupons_price() {
        return coupons_price;
    }

    public void setCoupons_price(String coupons_price) {
        this.coupons_price = coupons_price;
    }

    public String getShipping_time() {
        return shipping_time;
    }

    public void setShipping_time(String shipping_time) {
        this.shipping_time = shipping_time;
    }

    public String getOrder_payway() {
        return order_payway;
    }

    public void setOrder_payway(String order_payway) {
        this.order_payway = order_payway;
    }

    public int getPay_time() {
        return pay_time;
    }

    public void setPay_time(int pay_time) {
        this.pay_time = pay_time;
    }

    public int getOrder_goods_count() {
        return order_goods_count;
    }

    public void setOrder_goods_count(int order_goods_count) {
        this.order_goods_count = order_goods_count;
    }

    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }
}
