package com.benben.megamart.bean;

import java.util.List;

/**
 * Created by: wanghk 2019-06-12.
 * Describe:  商品产地和公司信息
 */
public class GoodsPlaceCompanyListBean {

    private List<CompanyListBean> company_list;
    private List<MadeListBean> made_list;

    public List<CompanyListBean> getCompany_list() {
        return company_list;
    }

    public void setCompany_list(List<CompanyListBean> company_list) {
        this.company_list = company_list;
    }

    public List<MadeListBean> getMade_list() {
        return made_list;
    }

    public void setMade_list(List<MadeListBean> made_list) {
        this.made_list = made_list;
    }

    public static class CompanyListBean {
        /**
         * company_name : 中犇
         * company_logo : null
         * company_id : 2
         */

        private String company_name;
        private Object company_logo;
        private int company_id;

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public Object getCompany_logo() {
            return company_logo;
        }

        public void setCompany_logo(Object company_logo) {
            this.company_logo = company_logo;
        }

        public int getCompany_id() {
            return company_id;
        }

        public void setCompany_id(int company_id) {
            this.company_id = company_id;
        }
    }

    public static class MadeListBean {
        /**
         * made_place_name : 西安
         * made_place_id : 1
         */

        private String made_place_name;
        private int made_place_id;

        public String getMade_place_name() {
            return made_place_name;
        }

        public void setMade_place_name(String made_place_name) {
            this.made_place_name = made_place_name;
        }

        public int getMade_place_id() {
            return made_place_id;
        }

        public void setMade_place_id(int made_place_id) {
            this.made_place_id = made_place_id;
        }
    }
}
