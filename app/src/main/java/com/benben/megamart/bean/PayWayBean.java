package com.benben.megamart.bean;

import java.util.List;

/**
 * Created by: wanghk 2019-07-25.
 * Describe: 支付方式bean
 */
public class PayWayBean {


    /**
     * is_buy : 1
     * prompt :
     * payway_list : [{"pay_id":"1","pay_name":"余额支付","pay_logo":"http://megamart.brd-techi.com/assets/img/icon_yue.png","balance":"1145.81"},{"pay_id":"3","pay_name":"微信支付","pay_logo":"http://megamart.brd-techi.com/assets/img/icon_weixin.png"},{"pay_id":"5","pay_name":"货到付款","pay_logo":"http://megamart.brd-techi.com/assets/img/icon_hdfk.png"},{"pay_id":"6","pay_name":"stripe","pay_logo":"http://megamart.brd-techi.com/assets/img/icon_stripe.jpg"}]
     */

    private String is_buy;
    private String prompt;
    private List<PaywayListBean> payway_list;

    public String getIs_buy() {
        return is_buy;
    }

    public void setIs_buy(String is_buy) {
        this.is_buy = is_buy;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public List<PaywayListBean> getPayway_list() {
        return payway_list;
    }

    public void setPayway_list(List<PaywayListBean> payway_list) {
        this.payway_list = payway_list;
    }

    public static class PaywayListBean {
        /**
         * pay_id : 1
         * pay_name : 余额支付
         * pay_logo : http://megamart.brd-techi.com/assets/img/icon_yue.png
         * balance : 1145.81
         */

        private boolean isSelect;
        private String pay_id;
        private String pay_name;
        private String pay_logo;
        private String balance;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getPay_id() {
            return pay_id;
        }

        public void setPay_id(String pay_id) {
            this.pay_id = pay_id;
        }

        public String getPay_name() {
            return pay_name;
        }

        public void setPay_name(String pay_name) {
            this.pay_name = pay_name;
        }

        public String getPay_logo() {
            return pay_logo;
        }

        public void setPay_logo(String pay_logo) {
            this.pay_logo = pay_logo;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }
    }
}
