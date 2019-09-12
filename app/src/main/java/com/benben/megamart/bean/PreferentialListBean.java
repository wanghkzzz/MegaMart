package com.benben.megamart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/17
 * Time: 17:39
 */
public class PreferentialListBean implements Serializable {

    /**
     * preferential_id : 5
     * preferential_number : 1
     * goods_amount : 1
     * goods_list : [{"goods_id":1,"goods_name":"巧乐兹","goods_img":"http://megamart.brd-techi.com/uploads/20190531/295f20a7861a99f7c07c14544f625086.jpg","goods_price":"10.00","goods_number":1,"goods_attr":"糖果"},{"goods_id":2,"goods_name":"康师傅","goods_img":"http://megamart.brd-techi.com/uploads/20190531/e7dee485a741163fc593e1f1ea1b2f61.jpg","goods_price":"8.00","goods_number":1,"goods_attr":"饮料"}]
     */

    private int preferential_id;
    private int preferential_number;
    private String goods_amount;
    private List<GoodsListBean> goods_list;

    public int getPreferential_id() {
        return preferential_id;
    }

    public void setPreferential_id(int preferential_id) {
        this.preferential_id = preferential_id;
    }

    public int getPreferential_number() {
        return preferential_number;
    }

    public void setPreferential_number(int preferential_number) {
        this.preferential_number = preferential_number;
    }

    public String getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(String goods_amount) {
        this.goods_amount = goods_amount;
    }

    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }

}
