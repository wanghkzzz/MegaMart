package com.benben.megamart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/15
 * Time: 15:58
 */
public class AddressManagerBean implements Serializable {

    private List<AddressListBean> address_list;

    public List<AddressListBean> getAddress_list() {
        return address_list;
    }

    public void setAddress_list(List<AddressListBean> address_list) {
        this.address_list = address_list;
    }

    public static class AddressListBean implements Serializable {
        /**
         * address_id : 10
         * link_man : 收货人
         * link_phone : 13213692344
         * address_info : 213
         * zip_code : 2207
         * province : 1
         * city : 480
         * district : 0
         * is_default : 1
         * province_name : NSW
         * city_name : Bexley South
         */

        private int address_id;
        private String link_man;
        private String link_phone;
        private String address_info;
        private String zip_code;
        private int province;
        private int city;
        private int district;
        private String is_default;
        private String province_name;
        private String city_name;
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

        public int getAddress_id() {
            return address_id;
        }

        public void setAddress_id(int address_id) {
            this.address_id = address_id;
        }

        public String getLink_man() {
            return link_man;
        }

        public void setLink_man(String link_man) {
            this.link_man = link_man;
        }

        public String getLink_phone() {
            return link_phone;
        }

        public void setLink_phone(String link_phone) {
            this.link_phone = link_phone;
        }

        public String getAddress_info() {
            return address_info;
        }

        public void setAddress_info(String address_info) {
            this.address_info = address_info;
        }

        public String getZip_code() {
            return zip_code;
        }

        public void setZip_code(String zip_code) {
            this.zip_code = zip_code;
        }

        public int getProvince() {
            return province;
        }

        public void setProvince(int province) {
            this.province = province;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public int getDistrict() {
            return district;
        }

        public void setDistrict(int district) {
            this.district = district;
        }

        public String getIs_default() {
            return is_default;
        }

        public void setIs_default(String is_default) {
            this.is_default = is_default;
        }

        public String getProvince_name() {
            return province_name == null ? "" : province_name;
        }

        public void setProvince_name(String province_name) {
            this.province_name = province_name;
        }

        public String getCity_name() {
            return city_name == null ? "" : city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }
    }
}
