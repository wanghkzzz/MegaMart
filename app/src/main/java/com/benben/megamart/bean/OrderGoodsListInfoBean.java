package com.benben.megamart.bean;

import java.util.List;

/**
 * Created by: wanghk 2019-06-18.
 * Describe:订单商品信息
 */
public class OrderGoodsListInfoBean {


    /**
     * address_list : [{"is_default":"1","city_id":478,"zip_code":"22201","link_phone":"18211173994","link_man":"JJ哦","city":"Hurstville","district":0,"province_id":1,"province":"NSW","address_info":"铁路局","address_id":120}]
     * preferential_list : []
     * total : 10
     * goods_total : 10
     * freight : 0
     * coupon_id : 0
     * goods_list : [{"goods_name":"巧乐兹","goods_number":"1","goods_price":"10.00","is_preferential":"2","pic_image":"http://megamart.brd-techi.com/uploads/20190629/21877e66d24f5cf65562789378a55ed0.jpg","goods_id":1,"goods_attr":"糖果"}]
     */

    private String total;
    private String goods_total;
    private String freight;
    private String coupon_id;
    private String freight_text;
    private List<AddressListBean> address_list;
    private List<?> preferential_list;
    private List<GoodsListBean> goods_list;

    public String getFreight_text() {
        return freight_text;
    }

    public void setFreight_text(String freight_text) {
        this.freight_text = freight_text;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getGoods_total() {
        return goods_total;
    }

    public void setGoods_total(String goods_total) {
        this.goods_total = goods_total;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public List<AddressListBean> getAddress_list() {
        return address_list;
    }

    public void setAddress_list(List<AddressListBean> address_list) {
        this.address_list = address_list;
    }

    public List<?> getPreferential_list() {
        return preferential_list;
    }

    public void setPreferential_list(List<?> preferential_list) {
        this.preferential_list = preferential_list;
    }

    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }

    public static class AddressListBean {
        /**
         * is_default : 1
         * city_id : 478
         * zip_code : 22201
         * link_phone : 18211173994
         * link_man : JJ哦
         * city : Hurstville
         * district : 0
         * province_id : 1
         * province : NSW
         * address_info : 铁路局
         * address_id : 120
         */

        private String is_default;
        private String city_id;
        private String zip_code;
        private String link_phone;
        private String link_man;
        private String city;
        private String district;
        private String province_id;
        private String province;
        private String address_info;
        private String address_id;
        private String unit_number;
        private String street_number;

        public String getStreet_number() {
            return street_number;
        }

        public void setStreet_number(String street_number) {
            this.street_number = street_number;
        }

        public String getUnit_number() {
            return unit_number;
        }

        public void setUnit_number(String unit_number) {
            this.unit_number = unit_number;
        }

        public String getIs_default() {
            return is_default;
        }

        public void setIs_default(String is_default) {
            this.is_default = is_default;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getZip_code() {
            return zip_code;
        }

        public void setZip_code(String zip_code) {
            this.zip_code = zip_code;
        }

        public String getLink_phone() {
            return link_phone;
        }

        public void setLink_phone(String link_phone) {
            this.link_phone = link_phone;
        }

        public String getLink_man() {
            return link_man;
        }

        public void setLink_man(String link_man) {
            this.link_man = link_man;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getProvince_id() {
            return province_id;
        }

        public void setProvince_id(String province_id) {
            this.province_id = province_id;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getAddress_info() {
            return address_info;
        }

        public void setAddress_info(String address_info) {
            this.address_info = address_info;
        }

        public String getAddress_id() {
            return address_id;
        }

        public void setAddress_id(String address_id) {
            this.address_id = address_id;
        }
    }

    public static class GoodsListBean {
        /**
         * goods_name : 巧乐兹
         * goods_number : 1
         * goods_price : 10.00
         * is_preferential : 2
         * pic_image : http://megamart.brd-techi.com/uploads/20190629/21877e66d24f5cf65562789378a55ed0.jpg
         * goods_id : 1
         * goods_attr : 糖果
         */

        private String goods_name;
        private String goods_number;
        private String goods_price;
        private String is_preferential;
        private String pic_image;
        private int goods_id;
        private String goods_attr;

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_number() {
            return goods_number;
        }

        public void setGoods_number(String goods_number) {
            this.goods_number = goods_number;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getIs_preferential() {
            return is_preferential;
        }

        public void setIs_preferential(String is_preferential) {
            this.is_preferential = is_preferential;
        }

        public String getPic_image() {
            return pic_image;
        }

        public void setPic_image(String pic_image) {
            this.pic_image = pic_image;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_attr() {
            return goods_attr;
        }

        public void setGoods_attr(String goods_attr) {
            this.goods_attr = goods_attr;
        }
    }
}
