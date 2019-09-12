package com.benben.megamart.bean;

/**
 * Created by mxy on 2019/6/17.
 * 地理位置
 */

public class GeocodingEntity {

    String detailAddr = "";//详细地址，国家省份城市

    String countryName = "";//国家名称

    String countryCode = "";//国家码

    String cityName = "";//城市

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
