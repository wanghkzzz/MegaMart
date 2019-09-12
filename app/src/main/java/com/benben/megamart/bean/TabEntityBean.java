package com.benben.megamart.bean;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Create by wanghk on 2019-05-27.
 * Describe:Tab数据bean
 */
public class TabEntityBean implements CustomTabEntity {

    public String title;
    public int selectedIcon;
    public int unSelectedIcon;

    public TabEntityBean(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }
    public TabEntityBean(String title) {
        this.title = title;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
