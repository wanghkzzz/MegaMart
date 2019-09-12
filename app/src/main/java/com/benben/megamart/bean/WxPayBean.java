package com.benben.megamart.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by: wanghk 2019-06-19.
 * Describe:微信支付bean
 */
public class WxPayBean {

    /**
     * pay_info : {"sign":"600537305471219F00750F377C435B6E","partnerid":"1499434982","package":"Sign=WXPay","appid":"wx232f23c5c84ced3e","timestamp":"1560937168","noncestr":"xbyofc8okmyxcsqzquk3ccbzug0cdlx9","prepayid":"wx1917392818308362c39057d21626455400"}
     */

    private PayInfoBean pay_info;

    public PayInfoBean getPay_info() {
        return pay_info;
    }

    public void setPay_info(PayInfoBean pay_info) {
        this.pay_info = pay_info;
    }

    public static class PayInfoBean {
        /**
         * sign : 600537305471219F00750F377C435B6E
         * partnerid : 1499434982
         * package : Sign=WXPay
         * appid : wx232f23c5c84ced3e
         * timestamp : 1560937168
         * noncestr : xbyofc8okmyxcsqzquk3ccbzug0cdlx9
         * prepayid : wx1917392818308362c39057d21626455400
         */

        private String sign;
        private String partnerid;
        @SerializedName("package")
        private String packageX;
        private String appid;
        private String timestamp;
        private String noncestr;
        private String prepayid;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }
    }
}
