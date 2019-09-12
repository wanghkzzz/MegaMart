package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-05.
 * Describe:特价优惠商品列表bean
 */
public class SpecialDiscountListBean {

    /**
     * goods_id : 1
     * goods_name : 巧乐兹
     * goods_img : /uploads/20190531/295f20a7861a99f7c07c14544f625086.jpg
     * goods_price : 10.00
     * cost_price : 100.00
     */

    private int goods_id;
    private String goods_name;
    private String goods_img;
    private String goods_price;
    private String cost_price;

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getCost_price() {
        return cost_price;
    }

    public void setCost_price(String cost_price) {
        this.cost_price = cost_price;
    }
}
