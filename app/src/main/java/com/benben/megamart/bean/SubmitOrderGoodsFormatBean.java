package com.benben.megamart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by: wanghk 2019-06-18.
 * Describe: 购物车传值到提交订单的bean
 */
public class SubmitOrderGoodsFormatBean implements Serializable {

    private List<CartGoodsListBean> goodsList;

    public SubmitOrderGoodsFormatBean(List<CartGoodsListBean> goodsList) {
        this.goodsList = goodsList;
    }

    public List<CartGoodsListBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<CartGoodsListBean> goodsList) {
        this.goodsList = goodsList;
    }
}
