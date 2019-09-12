package com.benben.megamart.bean;

import java.util.List;

/**
 * Created by: wanghk 2019-06-13.
 * Describe:商品详情信息
 */
public class GoodsDetailsInfoBean {


    /**
     * goods_id : 1
     * goods_name : 巧乐兹
     * goods_img : ["/uploads/20190531/295f20a7861a99f7c07c14544f625086.jpg","/uploads/20190531/fa2c672c397b8dca6db6e030474947d5.jpg"]
     * goods_video :
     * goods_price : 10.00
     * sale_count : 0
     * goods_content : <p><img src="/uploads/20190531/295f20a7861a99f7c07c14544f625086.jpg" data-filename="filename" style="width: 450px;"></p><p>甜品</p>
     * collection_count : 2
     * is_collection : 0
     */

    private int goods_id;
    private String goods_name;
    private String goods_video;
    private String goods_price;
    private int sale_count;
    private String goods_content;
    private int collection_count;
    private int is_collection;
    private List<String> goods_img;

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

    public String getGoods_video() {
        return goods_video;
    }

    public void setGoods_video(String goods_video) {
        this.goods_video = goods_video;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public int getSale_count() {
        return sale_count;
    }

    public void setSale_count(int sale_count) {
        this.sale_count = sale_count;
    }

    public String getGoods_content() {
        return goods_content;
    }

    public void setGoods_content(String goods_content) {
        this.goods_content = goods_content;
    }

    public int getCollection_count() {
        return collection_count;
    }

    public void setCollection_count(int collection_count) {
        this.collection_count = collection_count;
    }

    public int getIs_collection() {
        return is_collection;
    }

    public void setIs_collection(int is_collection) {
        this.is_collection = is_collection;
    }

    public List<String> getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(List<String> goods_img) {
        this.goods_img = goods_img;
    }
}
