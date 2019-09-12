package com.benben.megamart.bean;

import java.util.List;

/**
 * Created by: wanghk 2019-06-12.
 * Describe:满免运费公告bean
 */
public class HomeAdvertInfoBean {


    private List<BulletinBean> bulletin;

    public List<BulletinBean> getBulletin() {
        return bulletin;
    }

    public void setBulletin(List<BulletinBean> bulletin) {
        this.bulletin = bulletin;
    }

    public static class BulletinBean {
        /**
         * discount_img : /uploads/20190604/2e1d46cf05e28ee38a58f67bda0cd34d.jpg
         * discount_url : 1
         */

        private String discount_img;
        private String discount_url;

        public String getDiscount_img() {
            return discount_img;
        }

        public void setDiscount_img(String discount_img) {
            this.discount_img = discount_img;
        }

        public String getDiscount_url() {
            return discount_url;
        }

        public void setDiscount_url(String discount_url) {
            this.discount_url = discount_url;
        }
    }
}
