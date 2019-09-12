package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-18.
 * Describe:提交订单信息结果bean
 */
public class SubmitOrderResultInfoBean {

    /**
     * pay_way : 2
     * pay_info :
     * order_id : 19
     * order_no : 20190618185642644955
     * order_type : 1
     */

    private String pay_way;
    private String pay_info;
    private String order_id;
    private String order_no;
    private int order_type;

    public String getPay_way() {
        return pay_way;
    }

    public void setPay_way(String pay_way) {
        this.pay_way = pay_way;
    }

    public String getPay_info() {
        return pay_info;
    }

    public void setPay_info(String pay_info) {
        this.pay_info = pay_info;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }
}
