package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-13.
 * Describe:购物车指定商品数量
 */
public class GoodsCartNumberBean {

    /**
     * user_id : 10
     * goods_id : 1
     * number : 1
     */

    private String user_id;
    private String goods_id;
    private int number;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
