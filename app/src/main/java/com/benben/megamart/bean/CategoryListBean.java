package com.benben.megamart.bean;

import java.util.List;

/**
 * Created by: wanghk 2019-05-31.
 * Describe: 分类列表bean
 */
public class CategoryListBean {


    /**
     * cate_id : 4
     * parent_id : 0
     * cate_name : 休闲食品
     * cate_img : /uploads/default/logo.png
     * child_list : [{"cate_id":8,"parent_id":4,"cate_name":"方便面","cate_img":"/uploads/20190531/e7dee485a741163fc593e1f1ea1b2f61.jpg"},{"cate_id":12,"parent_id":4,"cate_name":"网红零食","cate_img":"/uploads/default/logo.png"},{"cate_id":13,"parent_id":4,"cate_name":"口水面","cate_img":"/uploads/default/logo.png"},{"cate_id":14,"parent_id":4,"cate_name":"芒果干","cate_img":"/uploads/default/logo.png"},{"cate_id":15,"parent_id":4,"cate_name":"烤鱿鱼丝","cate_img":"/uploads/default/logo.png"}]
     */

    private int cate_id;
    private int parent_id;
    private String cate_name;
    private String cate_img;
    private List<ChildListBean> child_list;

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
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

    public List<ChildListBean> getChild_list() {
        return child_list;
    }

    public void setChild_list(List<ChildListBean> child_list) {
        this.child_list = child_list;
    }

    public static class ChildListBean {
        /**
         * cate_id : 8
         * parent_id : 4
         * cate_name : 方便面
         * cate_img : /uploads/20190531/e7dee485a741163fc593e1f1ea1b2f61.jpg
         */

        private int cate_id;
        private int parent_id;
        private String cate_name;
        private String cate_img;

        public int getCate_id() {
            return cate_id;
        }

        public void setCate_id(int cate_id) {
            this.cate_id = cate_id;
        }

        public int getParent_id() {
            return parent_id;
        }

        public void setParent_id(int parent_id) {
            this.parent_id = parent_id;
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
    }
}
