package com.benben.megamart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/14
 * Time: 16:01
 */
public class PersonDataBean implements Serializable {

    private List<UserinfoBean> userinfo;

    public List<UserinfoBean> getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(List<UserinfoBean> userinfo) {
        this.userinfo = userinfo;
    }

    public static class UserinfoBean implements Serializable{
        /**
         * user_id : 10
         * user_name : 18211173994
         * user_avatar :
         * user_sex : 0
         * user_email : 1157038440@qq.com
         * user_mobile : 18211173994
         */

        private int user_id;
        private String user_name;
        private String user_avatar;
        private String user_sex;
        private String user_email;
        private String user_mobile;

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

        public String getUser_sex() {
            return user_sex;
        }

        public void setUser_sex(String user_sex) {
            this.user_sex = user_sex;
        }

        public String getUser_email() {
            return user_email;
        }

        public void setUser_email(String user_email) {
            this.user_email = user_email;
        }

        public String getUser_mobile() {
            return user_mobile;
        }

        public void setUser_mobile(String user_mobile) {
            this.user_mobile = user_mobile;
        }
    }
}
