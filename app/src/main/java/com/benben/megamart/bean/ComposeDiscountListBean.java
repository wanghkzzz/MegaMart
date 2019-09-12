package com.benben.megamart.bean;

import java.util.List;

/**
 * Created by: wanghk 2019-06-01.
 * Describe:组合优惠商品列表bean
 */
public class ComposeDiscountListBean {

    /**
     * preferential_id : 4
     * preferential_amount : 1.50
     * save_amount : 0.5
     * preferential_list : [{"goods_id":8,"goods_name":"青岛大虾","goods_img":"","goods_amount":"1.00","goods_number":2}]
     */

    private int preferential_id;
    private String preferential_amount;
    private double save_amount;
    private List<PreferentialListBean> preferential_list;

    public int getPreferential_id() {
        return preferential_id;
    }

    public void setPreferential_id(int preferential_id) {
        this.preferential_id = preferential_id;
    }

    public String getPreferential_amount() {
        return preferential_amount;
    }

    public void setPreferential_amount(String preferential_amount) {
        this.preferential_amount = preferential_amount;
    }

    public double getSave_amount() {
        return save_amount;
    }

    public void setSave_amount(double save_amount) {
        this.save_amount = save_amount;
    }

    public List<PreferentialListBean> getPreferential_list() {
        return preferential_list;
    }

    public void setPreferential_list(List<PreferentialListBean> preferential_list) {
        this.preferential_list = preferential_list;
    }

    public static class PreferentialListBean {
        /**
         * goods_id : 8
         * goods_name : 青岛大虾
         * goods_img :
         * goods_amount : 1.00
         * goods_number : 2
         */

        private int goods_id;
        private String goods_name;
        private String goods_img;
        private String goods_amount;
        private int goods_number;

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

        public String getGoods_amount() {
            return goods_amount;
        }

        public void setGoods_amount(String goods_amount) {
            this.goods_amount = goods_amount;
        }

        public int getGoods_number() {
            return goods_number;
        }

        public void setGoods_number(int goods_number) {
            this.goods_number = goods_number;
        }
    }
}
