package com.benben.megamart.bean;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * Created by: wanghk 2019-06-10.
 * Describe:送货时间 二级联动bean
 */
public class DeliveryTimeListBean implements IPickerViewData {

    /**
     * delivery_key : 1
     * delivery_time : 2019-06-19 08:59:59
     */

    private String delivery_key;
    private String delivery_time;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getDelivery_key() {
        return delivery_key;
    }

    public void setDelivery_key(String delivery_key) {
        this.delivery_key = delivery_key;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    @Override
    public String getPickerViewText() {
        return delivery_time;
    }
}
