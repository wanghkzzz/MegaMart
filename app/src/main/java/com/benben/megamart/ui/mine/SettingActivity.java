package com.benben.megamart.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.ActivityManagerUtils;
import com.benben.commoncore.utils.AppUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ThreadPoolUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.R;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.UpdateVersonBean;
import com.benben.megamart.config.SystemDir;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.ui.LoginActivity;
import com.benben.megamart.utils.DataCleanManager;
import com.benben.megamart.utils.LoginCheckUtils;
import com.benben.megamart.utils.StatusBarUtils;
import com.zjn.updateapputils.util.CheckVersionRunnable;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.tv_update_pwd)
    TextView tvUpdatePwd;
    @BindView(R.id.tv_select_language)
    TextView tvSelectLanguage;
    @BindView(R.id.tv_cache_size)
    TextView tvCacheSize;
    @BindView(R.id.rl_delete_cache)
    RelativeLayout rlDeleteCache;
    @BindView(R.id.tv_now_version)
    TextView tvNowVersion;
    @BindView(R.id.rl_now_version)
    RelativeLayout rlNowVersion;
    @BindView(R.id.btn_exit)
    Button btnExit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {
        initTitle("" + getString(R.string.setting));
        tvNowVersion.setText("" + getString(R.string.setting_now_version)
                + AppUtils.getVerName(this));
        try {
            tvCacheSize.setText("" + DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.tv_update_pwd, R.id.tv_select_language, R.id.rl_delete_cache, R.id.rl_now_version, R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //忘记密码
            case R.id.tv_update_pwd:
                if(LoginCheckUtils.checkLoginShowDialog(mContext))return;
                startActivity(new Intent(SettingActivity.this, UpdatePasswordActivity.class));
                break;
            //语言选择
            case R.id.tv_select_language:
                startActivity(new Intent(SettingActivity.this, SelectLanguageActivity.class));
                break;
            //清除缓存
            case R.id.rl_delete_cache:
                toast(getResources().getString(R.string.cleared_cache));
                tvCacheSize.setText("0.0KB");
                DataCleanManager.clearAllCache(SettingActivity.this);
                break;
            //当前版本
            case R.id.rl_now_version:
                updateVersion();
                break;
            //退出登录
            case R.id.btn_exit:
                ActivityManagerUtils.clear();
                LoginCheckUtils.clearUserInfo();
                Intent intent = new Intent(this,  LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * 版本更新
     */
    private void updateVersion() {
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.GET_VERSION)
                .addParam("now_version", "" + AppUtils.getVersionCode(this))
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                UpdateVersonBean bean = JSONUtils.jsonString2Bean(result, UpdateVersonBean.class);
                CheckVersionRunnable runnable = CheckVersionRunnable.from(SettingActivity.this)
                        .setApkPath(SystemDir.DIR_UPDATE_APK)//文件存储路径
                        .setDownLoadUrl(bean.getVersion_info().getDownloadurl())
                        .setServerUpLoadLocalVersion("" + (AppUtils.getVersionCode(SettingActivity.this) + 1))
                        .setServerVersion("" + (AppUtils.getVersionCode(SettingActivity.this) + 2))
                        .setUpdateMsg(bean.getVersion_info().getContent())
                        .isUseCostomDialog(true)
                        .setNotifyTitle(getResources().getString(R.string.app_name))
                        .setVersionShow(bean.getVersion_info().getContent());
                //启动通知，去下载
                ThreadPoolUtils.newInstance().execute(runnable);
            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast("" + msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
            }
        });
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }
}
