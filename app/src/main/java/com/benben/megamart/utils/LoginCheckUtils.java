package com.benben.megamart.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.benben.commoncore.utils.StringUtils;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.bean.UserLoginSucBean;
import com.benben.megamart.ui.LoginActivity;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.hyphenate.chat.EMClient;

//import com.hyphenate.chat.EMClient;

/**
 * 用户是否登录检测工具类
 * Created by Administrator on 2017/11/28.
 */
public class LoginCheckUtils {
    private static final String TAG = "LoginCheckUtils";

    //验证是否登录的异步回调
    public interface CheckCallBack{
        /**
         *  检查结果
         * @param flag 是否登录
         *  true：已登录；false：未登录
         */
        void onCheckResult(boolean flag);
    }

    /**
     * 检查用户是否登录
     * @param context
     * @return
     */
    public static boolean checkUserIsLogin(Context context) {
        String uid = MegaMartApplication.mPreferenceProvider.getUId();
        String token = MegaMartApplication.mPreferenceProvider.getToken();
        if(!StringUtils.isEmpty(uid) && !StringUtils.isEmpty(token)){
            return true;
        }
        return false;
    }
    /**
     * 检查用户是否登录
     *
     * @param activity
     * @param callBack
     * @return
     */
    public static void checkUserIsLogin(Activity activity, CheckCallBack callBack) {
        String uid = MegaMartApplication.mPreferenceProvider.getUId();
        String token = MegaMartApplication.mPreferenceProvider.getToken();
        if(StringUtils.isEmpty(uid)||StringUtils.isEmpty(token)){
            //token过期后登出环信
            EMClient.getInstance().logout(false);
            callBack.onCheckResult(false);
            return;
        }
     //   StyledDialogUtils.getInstance().loading(activity);
      /*  BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.VERIFY_TOKEN)
                .json()
                .post().build().enqueue(activity, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                LogUtils.e(TAG,result+"----"+msg);
                StyledDialogUtils.getInstance().dismissLoading(activity);
                callBack.onCheckResult(true);
            }

            @Override
            public void onError(int code, String msg) {
                //token过期后登出环信
            //    EMClient.getInstance().logout(false);
                //清除本地保存的用户信息
                clearUserInfo();
                callBack.onCheckResult(false);
                StyledDialogUtils.getInstance().dismissLoading(activity);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                //token过期后登出环信
               // EMClient.getInstance().logout(false);
                //清除本地保存的用户信息
                clearUserInfo();
                callBack.onCheckResult(false);
                StyledDialogUtils.getInstance().dismissLoading(activity);
            }
        });*/
    }


    /**
     * 提示用户登录对话框
     *
     * @param context
     */
    public static void showYanZhengDialog(Context context, MyDialogListener listener) {
        StyledDialog.init(context);
        //set some style:
        StyledDialog.buildMdAlert(context.getResources().getString(R.string.tips), context.getResources().getString(R.string.please_login_first), listener)
                .setBtnSize(20)
                .setBtnText(context.getResources().getString(R.string.positive), context.getResources().getString(R.string.shop_cart_cancel))
                .show();
    }



    //检查是否登录
    public static boolean checkLoginShowDialog(Context mContext) {
        if (!LoginCheckUtils.checkUserIsLogin(mContext)) {
            LoginCheckUtils.showYanZhengDialog(mContext, new MyDialogListener() {
                @Override
                public void onFirst() {
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }

                @Override
                public void onSecond() {

                }
            });
            return true;

        }
        return false;
    }

    /**
     * 更新用户信息
     * //@param infoBean 用户信息
     */
   /* public static void updateUserInfo(UserInfoBean infoBean){
        MegaMartApplication.mPreferenceProvider.setPhoto(infoBean.getAvatar());
        MegaMartApplication.mPreferenceProvider.setUserName(infoBean.getNickname());
        MegaMartApplication.mPreferenceProvider.setSex(infoBean.getSex());
        MegaMartApplication.mPreferenceProvider.setBoBi(String.valueOf(infoBean.getUser_bobi()));
    }*/

    public static void saveLoginInfo(UserLoginSucBean data){
        MegaMartApplication.mPreferenceProvider.setToken(data.getUser_token());
        MegaMartApplication.mPreferenceProvider.setUId(String.valueOf(data.getUser_id()));
        MegaMartApplication.mPreferenceProvider.setUserName(data.getUser_name());
        MegaMartApplication.mPreferenceProvider.setPhoto(data.getUser_thumb());
        MegaMartApplication.mPreferenceProvider.setHuanXinId(data.getHuanxin_uuid());
        MegaMartApplication.mPreferenceProvider.setHuanXinName(data.getHuanxin_name());
        MegaMartApplication.mPreferenceProvider.setHuanXinPwd(data.getHuanxin_password());
    }

    public static void clearUserInfo(){
        MegaMartApplication.mPreferenceProvider.setToken("");
        MegaMartApplication.mPreferenceProvider.setUId("");
        MegaMartApplication.mPreferenceProvider.setUserName("");
        MegaMartApplication.mPreferenceProvider.setPhoto("");
        MegaMartApplication.mPreferenceProvider.setHuanXinId("");
        MegaMartApplication.mPreferenceProvider.setHuanXinName("");
        MegaMartApplication.mPreferenceProvider.setHuanXinPwd("");
    }

}
