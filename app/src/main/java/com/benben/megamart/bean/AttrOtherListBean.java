package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-13.
 * Describe:其他规格
 */
public class AttrOtherListBean {

    /**
     * goods_id : 6
     * goods_name : 天天草莓
     * goods_pic : /uploads/20190531/fff3ed434702589ffccd53aa384ffb4b.jpg
     */

    private int goods_id;
    private String goods_name;
    private String goods_pic;

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
}
