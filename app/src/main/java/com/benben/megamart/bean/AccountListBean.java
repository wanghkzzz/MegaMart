package com.benben.megamart.bean;

import java.util.List;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/14
 * Time: 18:04
 */
public class AccountListBean {


    /**
     * page_size : 20
     * page_start : 1
     * page_total : 1
     * bal_total : -98
     * bal_year : 2019
     * bal_month : 6
     * user_balance : [{"bal_title":"支付订单","change_money":"8.00","change_time":1561536475,"type":"2"},{"bal_title":"支付订单","change_money":"30.00","change_time":1561449754,"type":"2"},{"bal_title":"支付订单","change_money":"30.00","change_time":1561449750,"type":"2"},{"bal_title":"支付订单","change_money":"30.00","change_time":1561449728,"type":"2"}]
     */

    private String page_size;
    private String page_start;
    private int page_total;
    private int bal_total;
    private String bal_year;
    private String bal_month;
    private List<UserBalanceBean> user_balance;

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public String getPage_start() {
        return page_start;
    }

    public void setPage_start(String page_start) {
        this.page_start = page_start;
    }

    public int getPage_total() {
        return page_total;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }

    public int getBal_total() {
        return bal_total;
    }

    public void setBal_total(int bal_total) {
        this.bal_total = bal_total;
    }

    public String getBal_year() {
        return bal_year;
    }

    public void setBal_year(String bal_year) {
        this.bal_year = bal_year;
    }

    public String getBal_month() {
        return bal_month;
    }

    public void setBal_month(String bal_month) {
        this.bal_month = bal_month;
    }

    public List<UserBalanceBean> getUser_balance() {
        return user_balance;
    }

    public void setUser_balance(List<UserBalanceBean> user_balance) {
        this.user_balance = user_balance;
    }

    public static class UserBalanceBean {
        /**
         * bal_title : 支付订单
         * change_money : 8.00
         * change_time : 1561536475
         * type : 2
         */

        private String bal_title;
        private String change_money;
        private int change_time;
        private String type;

        public String getBal_title() {
            return bal_title;
        }

        public void setBal_title(String bal_title) {
            this.bal_title = bal_title;
        }

        public String getChange_money() {
            return change_money;
        }

        public void setChange_money(String change_money) {
            this.change_money = change_money;
        }

        public int getChange_time() {
            return change_time;
        }

        public void setChange_time(int change_time) {
            this.change_time = change_time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
