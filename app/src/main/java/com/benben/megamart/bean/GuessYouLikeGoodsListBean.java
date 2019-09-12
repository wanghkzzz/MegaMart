package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-06.
 * Describe:猜你喜欢商品列表
 */
public class GuessYouLikeGoodsListBean {

    /**
     * goods_id : 1
     * goods_name : 巧乐兹
     * goods_pic : /uploads/20190531/295f20a7861a99f7c07c14544f625086.jpg
     * goods_pric : 10.00
     */

    private int goods_id;
    private String goods_name;
    private String goods_pic;
    private String goods_pric;

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

    public String getGoods_pric() {
        return goods_pric;
    }

    public void setGoods_pric(String goods_pric) {
        this.goods_pric = goods_pric;
    }
}
