package com.benben.commoncore.utils;

import android.app.Activity;

import com.hss01248.dialog.StyledDialog;

/**
 * 通用进度，对话框封装工具类
 * create by zjn on 2019/5/15 0015
 * email:168455992@qq.com
 */
public class StyledDialogUtils {

    private  boolean isShowing = false;
    private static StyledDialogUtils mInstance;

    public static StyledDialogUtils getInstance(){
        if(mInstance == null){
            mInstance = new StyledDialogUtils();
        }
        return mInstance;
    }

    private StyledDialogUtils(){

    }

    /*开启进度条*/
    public void loading(Activity activity){
        if(isShowing) return;
        StyledDialog.init(activity);
        StyledDialog.buildLoading().show();
        isShowing = true;
    }

    /*关闭进度条*/
    public void dismissLoading(){
        isShowing = false;
        StyledDialog.dismissLoading();
        StyledDialog.dismiss();
    }
}
