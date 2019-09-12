package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-06.
 * Describe:常用地址bean
 */
public class CommonLocationListBean {
    private String city;
    private String provinces;

    public CommonLocationListBean(String city, String provinces) {
        this.city = city;
        this.provinces = provinces;
    }

    public CommonLocationListBean() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvinces() {
        return provinces;
    }

    public void setProvinces(String provinces) {
        this.provinces = provinces;
    }
}
