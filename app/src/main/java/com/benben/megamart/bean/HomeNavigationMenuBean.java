package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-11.
 * Describe:banner下二级导航菜单
 */
public class HomeNavigationMenuBean {

    /**
     * cate_id : 5
     * cate_name : 水果
     * cate_img : /uploads/20190531/bf395cbf2ba81cd3f60c00aed49dbe29.jpg
     */

    private int cate_id;
    private String cate_name;
    private String cate_img;

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getCate_img() {
        return cate_img;
    }

    public void setCate_img(String cate_img) {
        this.cate_img = cate_img;
    }


    public HomeNavigationMenuBean(int cate_id, String cate_name, String cate_img) {
        this.cate_id = cate_id;
        this.cate_name = cate_name;
        this.cate_img = cate_img;
    }


    public HomeNavigationMenuBean() {
    }
}
