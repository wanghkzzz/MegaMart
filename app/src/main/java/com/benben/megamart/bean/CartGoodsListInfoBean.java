package com.benben.megamart.bean;

import java.util.List;

/**
 * Created by: wanghk 2019-06-04.
 * Describe:购物车商品信息bean
 */
public class CartGoodsListInfoBean {


    /**
     * goods_list : [{"goods_id":17,"goods_name":"科迪纯牛奶，早餐推荐！！！超值购买。。抢购","goods_img":"http://megamart.brd-techi.com/uploads/20190703/61a40303a9519e6d28ecb5afa22895ca.png","goods_amount":"49.00","goods_number":2,"cart_id":898,"status":1},{"goods_id":23,"goods_name":"拉面说6盒装豚骨叉烧辣味番茄黑蒜冬阴功藤椒鸡方便速食拉面","goods_img":"http://megamart.brd-techi.com/uploads/20190704/4a24c292206243a0ddefecd93cff1f76.png","goods_amount":"87.20","goods_number":1,"cart_id":840,"status":3}]
     * preferential_list : [{"goods_list":[{"goods_id":11,"goods_name":"冻干藤椒豚骨面 74克*12盒","goods_img":"http://megamart.brd-techi.com/uploads/20190619/0858df534ac3dd8d25d646e30cfdf5cd.jpg","goods_amount":"110.00","goods_number":2,"cart_id":914},{"goods_id":10,"goods_name":"小龙虾","goods_img":"http://megamart.brd-techi.com/uploads/20190619/3902034fb27f559037638af6ca0a9b3d.png","goods_amount":"99.00","goods_number":1,"cart_id":913}],"preferential_id":7,"preferential_name":"零食大礼包","preferential_amount":"288.00","preferential_number":2,"status":1},{"goods_list":[{"goods_id":13,"goods_name":"浓缩即食燕窝 70克*3瓶 99%","goods_img":"http://megamart.brd-techi.com/uploads/20190619/a8551f59f5dc1f861f676974d7459a5a.jpg","goods_amount":"63.00","goods_number":1,"cart_id":912},{"goods_id":12,"goods_name":"常温纯牛奶 250毫升*12盒*2提","goods_img":"http://megamart.brd-techi.com/uploads/20190619/312421b4c792fd813033bb918cfec6c1.jpg","goods_amount":"100.00","goods_number":2,"cart_id":911}],"preferential_id":8,"preferential_name":"饮品大礼包","preferential_amount":"233.00","preferential_number":1,"status":1},{"goods_list":[{"goods_id":14,"goods_name":"比利时制造 什锦手工巧克力礼盒 432克","goods_img":"http://megamart.brd-techi.com/uploads/20190619/f6ab206fcc359b1ead935e480867dfc4.jpg","goods_amount":"233.00","goods_number":1,"cart_id":881},{"goods_id":10,"goods_name":"小龙虾","goods_img":"http://megamart.brd-techi.com/uploads/20190619/3902034fb27f559037638af6ca0a9b3d.png","goods_amount":"99.00","goods_number":1,"cart_id":880}],"preferential_id":9,"preferential_name":"龙虾大礼包","preferential_amount":"220.00","preferential_number":6,"status":1}]
     * total : 2314.2
     */

    private double total;
    private List<GoodsListBean> goods_list;
    private List<PreferentialListBean> preferential_list;
    private String freight;
    private String free_postage;
    private String freight_text;

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getFree_postage() {
        return free_postage;
    }

    public void setFree_postage(String free_postage) {
        this.free_postage = free_postage;
    }

    public String getFreight_text() {
        return freight_text;
    }

    public void setFreight_text(String freight_text) {
        this.freight_text = freight_text;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
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

    public static class GoodsListBean {
        /**
         * goods_id : 17
         * goods_name : 科迪纯牛奶，早餐推荐！！！超值购买。。抢购
         * goods_img : http://megamart.brd-techi.com/uploads/20190703/61a40303a9519e6d28ecb5afa22895ca.png
         * goods_amount : 49.00
         * goods_number : 2
         * cart_id : 898
         * status : 1
         */

        private int goods_id;
        private String goods_name;
        private String goods_img;
        private String goods_amount;
        private int goods_number;
        private int cart_id;
        private int status;

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

        public int getCart_id() {
            return cart_id;
        }

        public void setCart_id(int cart_id) {
            this.cart_id = cart_id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class PreferentialListBean {
        /**
         * goods_list : [{"goods_id":11,"goods_name":"冻干藤椒豚骨面 74克*12盒","goods_img":"http://megamart.brd-techi.com/uploads/20190619/0858df534ac3dd8d25d646e30cfdf5cd.jpg","goods_amount":"110.00","goods_number":2,"cart_id":914},{"goods_id":10,"goods_name":"小龙虾","goods_img":"http://megamart.brd-techi.com/uploads/20190619/3902034fb27f559037638af6ca0a9b3d.png","goods_amount":"99.00","goods_number":1,"cart_id":913}]
         * preferential_id : 7
         * preferential_name : 零食大礼包
         * preferential_amount : 288.00
         * preferential_number : 2
         * status : 1
         */

        private int preferential_id;
        private String preferential_name;
        private String preferential_amount;
        private int preferential_number;
        private int status;
        private List<GoodsListBeanX> goods_list;

        public int getPreferential_id() {
            return preferential_id;
        }

        public void setPreferential_id(int preferential_id) {
            this.preferential_id = preferential_id;
        }

        public String getPreferential_name() {
            return preferential_name;
        }

        public void setPreferential_name(String preferential_name) {
            this.preferential_name = preferential_name;
        }

        public String getPreferential_amount() {
            return preferential_amount;
        }

        public void setPreferential_amount(String preferential_amount) {
            this.preferential_amount = preferential_amount;
        }

        public int getPreferential_number() {
            return preferential_number;
        }

        public void setPreferential_number(int preferential_number) {
            this.preferential_number = preferential_number;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<GoodsListBeanX> getGoods_list() {
            return goods_list;
        }

        public void setGoods_list(List<GoodsListBeanX> goods_list) {
            this.goods_list = goods_list;
        }

        public static class GoodsListBeanX {
            /**
             * goods_id : 11
             * goods_name : 冻干藤椒豚骨面 74克*12盒
             * goods_img : http://megamart.brd-techi.com/uploads/20190619/0858df534ac3dd8d25d646e30cfdf5cd.jpg
             * goods_amount : 110.00
             * goods_number : 2
             * cart_id : 914
             */

            private int goods_id;
            private String goods_name;
            private String goods_img;
            private String goods_amount;
            private int goods_number;
            private int cart_id;

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

            public int getCart_id() {
                return cart_id;
            }

            public void setCart_id(int cart_id) {
                this.cart_id = cart_id;
            }
        }
    }
}
