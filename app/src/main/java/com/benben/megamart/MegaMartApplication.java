package com.benben.megamart;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.benben.commoncore.utils.ActivityManagerUtils;
import com.benben.commoncore.utils.PreferenceProvider;
import com.benben.megamart.cerror.TheAppCrashHandler;
import com.benben.megamart.config.Constants;
import com.hss01248.dialog.StyledDialog;
import com.hyphenate.helper.HyphenateHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

/**
 * Create by wanghk on 2019-05-24.
 * Describe: Application 初始化
 */
public class MegaMartApplication extends Application implements Application.ActivityLifecycleCallbacks {

    public static PreferenceProvider mPreferenceProvider;// preference Provider
    public TheAppCrashHandler m_CrashHandler;
    public static MegaMartApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        //友盟
        UMConfigure.init(this, "5ce6610b3fc195479c000f80"
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "7dedaf30b53a85870cb2961426772d23");
/*
        UMConfigure.init(this, "5cdfa2133fc19527870004a4"
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
*/

//        PlatformConfig.setWeixin("wx1a33b1b39e4e22ea", "01f446a3c92589ef8cbd748b78a2d6c0");
        //   PlatformConfig.setQQZone("101575657", "1a35f21578bf05874801b7d155be9b61");
        PlatformConfig.setWeixin(Constants.WX_APP_ID, Constants.WX_SECRET);

        mApplication = this;
        mPreferenceProvider = new PreferenceProvider(this);
        //错误日志捕捉
        m_CrashHandler = TheAppCrashHandler.getInstance();
        m_CrashHandler.init(this);
        //传入context初始化对话框的控件
        StyledDialog.init(this);
        registerActivityLifecycleCallbacks(this);

        //初始化环信客服
        HyphenateHelper.init(this);

    }

    public static MegaMartApplication getInstance() {
        if (mApplication == null) {
            mApplication = new MegaMartApplication();
        }
        return mApplication;
    }

    static {
        //设置全局的Header构建器

        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader
            createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.color_F5F5F5, R.color.color_333333);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                layout.setPrimaryColorsId(R.color.color_F5F5F5, R.color.color_333333);//全局设置主题颜色
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ActivityManagerUtils.addOneActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ActivityManagerUtils.removeClear(activity);
    }
}
