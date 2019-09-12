package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-05-31.
 * Describe:消息列表bean
 */
public class MessageListBean {


    /**
     * article_img : http://megamart.brd-techi.com/uploads/20190612/aa62cb385b16966a97748886338a39e4.jpg
     * article_title : 公告2
     * article_time : 1560303882
     * article_description : 这是公告2的简介
     */

    private String article_img;
    private String article_title;
    private String article_time;
    private String article_description;
    private int  unreadMessageCount;

    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
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

    public String getArticle_time() {
        return article_time;
    }

    public void setArticle_time(String article_time) {
        this.article_time = article_time;
    }

    public String getArticle_description() {
        return article_description;
    }

    public void setArticle_description(String article_description) {
        this.article_description = article_description;
    }
}
