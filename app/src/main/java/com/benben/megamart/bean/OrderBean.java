package com.benben.megamart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/17
 * Time: 14:13
 */
public class OrderBean implements Serializable {

    private List<OrderListBean> order_list;

    public List<OrderListBean> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(List<OrderListBean> order_list) {
        this.order_list = order_list;
    }

    public static class OrderListBean {
        /**
         * order_id : 3
         * order_no : 20190615144347730947
         * order_type : 1
         * order_time : null
         * order_amount : 40.00
         * order_goods_count : 2
         * goods_list : [{"goods_id":6,"goods_name":"天天草莓","goods_img":"/uploads/20190531/fff3ed434702589ffccd53aa384ffb4b.jpg","goods_price":"30.00","goods_number":1,"goods_attr":"糖果"}]
         * preferential_list : []
         */

        private int order_id;
        private String order_no;
        private String order_type;
        private String order_type_text;
        private String order_time;
        private String order_amount;
        private String delivery_status;
        private int order_goods_count;
        private boolean isAdd;
        private List<GoodsListBean> goods_list;
        private List<PreferentialListBean> preferential_list;

        public String getDelivery_status() {
            return delivery_status;
        }

        public void setDelivery_status(String delivery_status) {
            this.delivery_status = delivery_status;
        }

        public String getOrder_type_text() {
            return order_type_text;
        }

        public void setOrder_type_text(String order_type_text) {
            this.order_type_text = order_type_text;
        }

        public boolean isAdd() {
            return isAdd;
        }

        public void setAdd(boolean add) {
            isAdd = add;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getOrder_type() {
            return order_type;
        }

        public void setOrder_type(String order_type) {
            this.order_type = order_type;
        }

        public String getOrder_time() {
            return order_time == null ? "" : order_time;
        }

        public void setOrder_time(String order_time) {
            this.order_time = order_time;
        }

        public String getOrder_amount() {
            return order_amount;
        }

        public void setOrder_amount(String order_amount) {
            this.order_amount = order_amount;
        }

        public int getOrder_goods_count() {
            return order_goods_count;
        }

        public void setOrder_goods_count(int order_goods_count) {
            this.order_goods_count = order_goods_count;
        }

        public List<GoodsListBean> getGoods_list() {
            return goods_list;
        }

        public void setGoods_list(List<GoodsListBean> goods_list) {
            this.goods_list = goods_list;
        }


        public List<PreferentialListBean> getPreferential_list() {
            return preferential_list;
        }

        public void setPreferential_list(List<PreferentialListBean> preferential_list) {
            this.preferential_list = preferential_list;
        }
    }
}
