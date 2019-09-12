package com.benben.megamart.bean;

import java.io.Serializable;

/**
 * Created by: wanghk 2019-06-04.
 * Describe:格式化后购物车商品信息bean
 */
public class CartGoodsListBean implements Serializable {
    private int goods_id;//商品id
    private int cart_id;//购物车id
    private int status;//商品状态（1:有效 2:无库存/下架）
    private int preferential_id;//组合商品ID
    private boolean isSelect;//是否选中
    private int goods_number;//商品数量
    private String goods_name;//商品名称
    private String preferential_name;//组合优惠名称
    private String goods_img;//商品图片
    private String goods_amount;//商品价格 （全部都有）
    private String preferential_price;//组合优惠商品的价格  （只有组合商品才有）
    private boolean isFirstPreferential;//是否是组合商品的第一个商品

    public CartGoodsListBean() {
    }

    public String getPreferential_name() {
        return preferential_name;
    }

    public void setPreferential_name(String preferential_name) {
        this.preferential_name = preferential_name;
    }

    public String getPreferential_price() {
        return preferential_price;
    }

    public void setPreferential_price(String preferential_price) {
        this.preferential_price = preferential_price;
    }

    public boolean isFirstPreferential() {
        return isFirstPreferential;
    }

    public void setFirstPreferential(boolean firstPreferential) {
        isFirstPreferential = firstPreferential;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }


    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPreferential_id() {
        return preferential_id;
    }

    public void setPreferential_id(int preferential_id) {
        this.preferential_id = preferential_id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(int goods_number) {
        this.goods_number = goods_number;
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

    public String getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(String goods_amount) {
        this.goods_amount = goods_amount;
    }
}
