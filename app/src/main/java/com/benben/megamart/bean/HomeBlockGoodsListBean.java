package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-14.
 * Describe: 首页二级页面商品列表bean
 */
public class HomeBlockGoodsListBean {


    /**
     * cost_price : 6.00
     * goods_name : 幸运方便面
     * goods_pic : http://megamart.brd-techi.com/uploads/20190629/311f73dcdb24e0c6cbb51b833c32be01.png
     * sales_count : 1
     * goods_price : 3.60
     * goods_id : 3
     */

    private String cost_price;
    private String goods_name;
    private String goods_pic;
    private int sales_count;
    private String goods_price;
    private int goods_id;

    public String getCost_price() {
        return cost_price;
    }

    public void setCost_price(String cost_price) {
        this.cost_price = cost_price;
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

    public int getSales_count() {
        return sales_count;
    }

    public void setSales_count(int sales_count) {
        this.sales_count = sales_count;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }
}
