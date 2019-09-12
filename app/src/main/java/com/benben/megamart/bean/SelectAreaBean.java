package com.benben.megamart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/17
 * Time: 10:13
 */
public class SelectAreaBean implements Serializable {

    private List<AreaInfoBean> area_info;

    public List<AreaInfoBean> getArea_info() {
        return area_info;
    }

    public void setArea_info(List<AreaInfoBean> area_info) {
        this.area_info = area_info;
    }

    public static class AreaInfoBean implements Serializable{
        /**
         * area_id : 1
         * area_pid : 0
         * level : 1
         * area_name : NSW
         * zip_code : 1233
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
}
