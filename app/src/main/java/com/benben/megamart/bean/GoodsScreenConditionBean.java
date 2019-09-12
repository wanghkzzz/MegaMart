package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-03.
 * Describe:筛选条件bean
 */
public class GoodsScreenConditionBean {

    private String conditionId;
    private String conditionName;
    private boolean isSelect;

    public GoodsScreenConditionBean(String conditionId ,String conditionName, boolean isSelect) {
        this.conditionId = conditionId;
        this.conditionName = conditionName;
        this.isSelect = isSelect;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }
}
