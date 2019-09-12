package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-21.
 * Describe: 常用地址列表
 */
public class UsedAddressListBean {


    /**
     * address :
     * zip_code : 2207
     * city : Bexley South
     */

    private String address;
    private String zip_code;
    private String city;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
