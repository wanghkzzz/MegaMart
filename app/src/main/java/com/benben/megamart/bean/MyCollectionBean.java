package com.benben.megamart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/15
 * Time: 14:03
 */
public class MyCollectionBean implements Serializable {

    private List<GoodsListBean> goods_list;

    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }

    public static class GoodsListBean {
        /**
         * collect_id : 22
         * goods_id : 1
         * goods_name : 巧乐兹
         * goods_img : /uploads/20190531/295f20a7861a99f7c07c14544f625086.jpg
         * goods_price : 10.00
         * sale_count : 0
         */

        private int collect_id;
        private int goods_id;
        private String goods_name;
        private String goods_img;
        private String goods_price;
        private int sale_count;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public int getCollect_id() {
            return collect_id;
        }

        public void setCollect_id(int collect_id) {
            this.collect_id = collect_id;
        }

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
    }
}
