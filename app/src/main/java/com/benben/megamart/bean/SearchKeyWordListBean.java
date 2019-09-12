package com.benben.megamart.bean;

import java.util.List;

/**
 * Created by: wanghk 2019-06-13.
 * Describe: 搜索记录及热门搜索
 */
public class SearchKeyWordListBean {

    private List<String> hot_list;
    private List<String> used_list;

    public List<String> getHot_list() {
        return hot_list;
    }

    public void setHot_list(List<String> hot_list) {
        this.hot_list = hot_list;
    }

    public List<String> getUsed_list() {
        return used_list;
    }

    public void setUsed_list(List<String> used_list) {
        this.used_list = used_list;
    }
}
