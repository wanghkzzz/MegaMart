package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-01.
 * Describe:通用商品列表bean
 */
public class SearchGoodsListBean {

    /**
     * goods_id : 1
     * goods_name : 巧乐兹
     * goods_pic : /uploads/20190531/295f20a7861a99f7c07c14544f625086.jpg
     * goods_price : 10.00
     * sales_count : 0
     */

    private int goods_id;
    private String goods_name;
    private String goods_pic;
    private String goods_price;
    private int sales_count;

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

    public String getGoods_pic() {
        return goods_pic;
    }

    public void setGoods_pic(String goods_pic) {
        this.goods_pic = goods_pic;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public int getSales_count() {
        return sales_count;
    }

    public void setSales_count(int sales_count) {
        this.sales_count = sales_count;
    }
}
