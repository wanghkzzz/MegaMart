package com.benben.megamart.bean;

import java.util.List;

/**
 * Created by: wanghk 2019-06-11.
 * Describe:用户信息Bean
 */
public class UserInfoBean {
    /**
     * unread_number : 0
     * userinfo : [{"user_id":10,"user_name":"18211173994","user_avatar":""}]
     */

    private String unread_number;
    private List<UserinfoBean> userinfo;

    public String getUnread_number() {
        return unread_number;
    }

    public void setUnread_number(String unread_number) {
        this.unread_number = unread_number;
    }

    public List<UserinfoBean> getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(List<UserinfoBean> userinfo) {
        this.userinfo = userinfo;
    }

    public static class UserinfoBean {
        /**
         * user_id : 10
         * user_name : 18211173994
         * user_avatar :
         */

        private int user_id;
        private String user_name;
        private String user_avatar;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }
    }
}
