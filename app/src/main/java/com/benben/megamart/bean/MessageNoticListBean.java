package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-15.
 * Describe:
 */
public class MessageNoticListBean {


    /**
     * article_id : 2
     * article_img : /uploads/20190612/aa62cb385b16966a97748886338a39e4.jpg
     * article_title : 公告2
     * article_description : 这是公告2的简介
     * article_time : 1560303882
     */

    private int article_id;
    private String article_img;
    private String article_title;
    private String article_description;
    private int article_time;

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public String getArticle_img() {
        return article_img;
    }

    public void setArticle_img(String article_img) {
        this.article_img = article_img;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_description() {
        return article_description;
    }

    public void setArticle_description(String article_description) {
        this.article_description = article_description;
    }

    public int getArticle_time() {
        return article_time;
    }

    public void setArticle_time(int article_time) {
        this.article_time = article_time;
    }
}
