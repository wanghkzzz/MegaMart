package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-15.
 * Describe:折扣商品bean
 */
public class RebateDiscountListBean {


    /**
     * goods_id : 2
     * goods_name : 康师傅
     * goods_img : /uploads/20190531/e7dee485a741163fc593e1f1ea1b2f61.jpg
     * goods_price : 8.00
     * cost_price : 10.00
     * goods_discount : 8
     */

    private int goods_id;
    private String goods_name;
    private String goods_img;
    private String goods_price;
    private String cost_price;
    private int goods_discount;

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

    public int getGoods_discount() {
        return goods_discount;
    }

    public void setGoods_discount(int goods_discount) {
        this.goods_discount = goods_discount;
    }
}
