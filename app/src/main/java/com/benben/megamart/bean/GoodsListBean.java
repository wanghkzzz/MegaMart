package com.benben.megamart.bean;

import java.io.Serializable;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/17
 * Time: 14:14
 */
public class GoodsListBean implements Serializable {

    private int goods_id;
    private String goods_name;
    private String goods_img;
    private String goods_price;
    private int goods_number;
    private String goods_attr;

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

    public int getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(int goods_number) {
        this.goods_number = goods_number;
    }

    public String getGoods_attr() {
        return goods_attr;
    }

    public void setGoods_attr(String goods_attr) {
        this.goods_attr = goods_attr;
    }

}
