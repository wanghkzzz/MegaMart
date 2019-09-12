package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-12.
 * Describe:商品产地和所属公司
 */
public class GoodsOriginAndCompanyBean {
    private String goods_origin_id;
    private String goods_company_id;

    public GoodsOriginAndCompanyBean(String goods_origin_id, String goods_company_id) {
        this.goods_origin_id = goods_origin_id;
        this.goods_company_id = goods_company_id;
    }


    public String getGoods_origin_id() {
        return goods_origin_id;
    }

    public void setGoods_origin_id(String goods_origin_id) {
        this.goods_origin_id = goods_origin_id;
    }

    public String getGoods_company_id() {
        return goods_company_id;
    }

    public void setGoods_company_id(String goods_company_id) {
        this.goods_company_id = goods_company_id;
    }
}
