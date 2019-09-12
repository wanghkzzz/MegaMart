package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-18.
 * Describe:默认地址bean
 */
public class DefaultAddressBean {


    /**
     * address_id : 19
     * link_man : wanghk
     * link_phone : 18211173994
     * address_info : 郑州市二七区大学北路华城国际中心
     * zip_code : 466100
     * province : NSW
     * city : NSW
     * district : 0
     * is_default : 1
     */

    private int address_id;
    private String link_man;
    private String link_phone;
    private String address_info;
    private String zip_code;
    private String province;
    private String city;
    private int district;
    private String is_default;

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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
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
}
