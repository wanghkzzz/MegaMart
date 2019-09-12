package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-14.
 * Describe: 首页banner图片
 */
public class HomeBannerImageBean {


    /**
     * banner_title : banner3
     * banner_img : http://megamart.brd-techi.com/uploads/20190619/0737ad8ca83af785aa077f7c8d14f784.jpg
     * param_type : 1
     * banner_param : banner3参数
     */

    private String banner_title;
    private String banner_img;
    private int param_type;
    private String banner_param;

    public String getBanner_title() {
        return banner_title;
    }

    public void setBanner_title(String banner_title) {
        this.banner_title = banner_title;
    }

    public String getBanner_img() {
        return banner_img;
    }

    public void setBanner_img(String banner_img) {
        this.banner_img = banner_img;
    }

    public int getParam_type() {
        return param_type;
    }

    public void setParam_type(int param_type) {
        this.param_type = param_type;
    }

    public String getBanner_param() {
        return banner_param;
    }

    public void setBanner_param(String banner_param) {
        this.banner_param = banner_param;
    }
}
