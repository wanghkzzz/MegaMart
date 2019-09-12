package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-21.
 * Describe:根据邮编查询的地址列表
 */
public class PostCodeAddressListBean {


    /**
     * area_id : 480
     * area_pid : 1
     * level : 2
     * area_name : Bexley South
     * zip_code : 2207
     */

    private int area_id;
    private int area_pid;
    private int level;
    private String area_name;
    private String zip_code;

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public int getArea_pid() {
        return area_pid;
    }

    public void setArea_pid(int area_pid) {
        this.area_pid = area_pid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }
}
