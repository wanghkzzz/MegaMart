package com.benben.megamart.bean;

import java.util.List;
/**
 * Created by: wanghk 2019-06-12.
 * Describe:首页热销商品信息bean
 */
public class HomeHotGoodsInfoBean {

    /**
     * block_type : 2
     * block_name : 热销主题
     * block_img : /uploads/20190531/eda060e55c8e0800eef33b630c031102.png
     * block_url : 1
     * block_url_param : 1
     * block_goods_list : [{"goods_id":1,"goods_img":"/uploads/20190531/295f20a7861a99f7c07c14544f625086.jpg","goods_price":"10.00","goods_name":"巧乐兹","sales_count":0},{"goods_id":2,"goods_img":"/uploads/20190531/e7dee485a741163fc593e1f1ea1b2f61.jpg","goods_price":"8.00","goods_name":"康师傅","sales_count":0},{"goods_id":3,"goods_img":"/uploads/20190531/e7dee485a741163fc593e1f1ea1b2f61.jpg","goods_price":"3.60","goods_name":"幸运方便面","sales_count":0},{"goods_id":4,"goods_img":"/uploads/20190531/fa2c672c397b8dca6db6e030474947d5.jpg","goods_price":"30.00","goods_name":"德芙","sales_count":0},{"goods_id":5,"goods_img":"/uploads/20190531/fff3ed434702589ffccd53aa384ffb4b.jpg","goods_price":"60.00","goods_name":"大草莓","sales_count":0},{"goods_id":6,"goods_img":"/uploads/20190531/fff3ed434702589ffccd53aa384ffb4b.jpg","goods_price":"30.00","goods_name":"天天草莓","sales_count":0},{"goods_id":8,"goods_img":"","goods_price":"1.00","goods_name":"青岛大虾","sales_count":0}]
     */

    private int block_type;
    private String block_name;
    private String block_img;
    private String block_url;
    private String block_url_param;
    private List<BlockGoodsListBean> block_goods_list;

    public int getBlock_type() {
        return block_type;
    }

    public void setBlock_type(int block_type) {
        this.block_type = block_type;
    }

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public String getBlock_img() {
        return block_img;
    }

    public void setBlock_img(String block_img) {
        this.block_img = block_img;
    }

    public String getBlock_url() {
        return block_url;
    }

    public void setBlock_url(String block_url) {
        this.block_url = block_url;
    }

    public String getBlock_url_param() {
        return block_url_param;
    }

    public void setBlock_url_param(String block_url_param) {
        this.block_url_param = block_url_param;
    }

    public List<BlockGoodsListBean> getBlock_goods_list() {
        return block_goods_list;
    }

    public void setBlock_goods_list(List<BlockGoodsListBean> block_goods_list) {
        this.block_goods_list = block_goods_list;
    }

    public static class BlockGoodsListBean {
        /**
         * goods_id : 1
         * goods_img : /uploads/20190531/295f20a7861a99f7c07c14544f625086.jpg
         * goods_price : 10.00
         * goods_name : 巧乐兹
         * sales_count : 0
         */

        private int goods_id;
        private String goods_img;
        private String goods_price;
        private String goods_name;
        private int sales_count;

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
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

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public int getSales_count() {
            return sales_count;
        }

        public void setSales_count(int sales_count) {
            this.sales_count = sales_count;
        }
    }
}
