package com.benben.megamart.ui;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MainActivity;
import com.benben.megamart.R;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.UserLoginSucBean;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.LoginCheckUtils;
import com.benben.megamart.utils.RxBus;
import com.benben.megamart.utils.StatusBarUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Create by wanghk on 2019-05-28.
 * Describe:登录页面
 */
public class LoginActivity extends BaseActivity {

    private int GO_REGISTER = 101;//注册的请求码
    private int GO_FORGET_PASSWORD = 102;//忘记密码的请求码


    @BindView(R.id.edt_login_account)
    EditText edtLoginAccount;
    @BindView(R.id.edt_login_password)
    EditText edtLoginPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_register_account)
    TextView tvRegisterAccount;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.iv_wechat_login)
    ImageView ivWechatLogin;

    private UMShareAPI mShareAPI;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        //每次登陆都需要授权
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(LoginActivity.this).setShareConfig(config);
        mShareAPI = UMShareAPI.get(LoginActivity.this);

        edtLoginAccount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        edtLoginPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        //软键盘的搜索按钮 点击事件
        edtLoginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                }
                return false;
            }
        });
    }

    @OnClick({R.id.btn_login, R.id.tv_register_account, R.id.tv_forget_password, R.id.iv_wechat_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //登录
                login();
                break;
            case R.id.tv_register_account:
                //跳转注册页面
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("isRegister", true);
                startActivityForResult(intent, GO_REGISTER);
                break;
            case R.id.tv_forget_password:
                //跳转找回密码页面
                startActivityForResult(new Intent(this, RetrievePasswordAvtivity.class), GO_FORGET_PASSWORD);
                break;
            case R.id.iv_wechat_login:
                //微信登录
//                thirdLogin("123", "...", "测试");
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
        }
    }

    //登录
    private void login() {

        String phone = edtLoginAccount.getText().toString().trim();
        String password = edtLoginPassword.getText().toString().trim();

        if (StringUtils.isEmpty(phone)) {
            toast(getString(R.string.iphone_number_not_null));
            return;
        }
        if (StringUtils.isEmpty(password)) {
            toast(getString(R.string.password_not_null));
            return;
        }
        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.LOGIN_PASSWORD_LOGIN)
                .addParam("login_str", "" + phone)
                .addParam("password", "" + password)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                UserLoginSucBean userLoginSucBean = JSONUtils.jsonString2Bean(result, UserLoginSucBean.class);
                StyledDialogUtils.getInstance().dismissLoading();
                if (userLoginSucBean != null) {
                    LoginCheckUtils.saveLoginInfo(userLoginSucBean);
                    toast(getString(R.string.login_success));
                    RxBus.getInstance().post(true);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    toast(getResources().getString(R.string.user_not_exist_please_go_register));
                }
            }

            @Override
            public void onError(int code, String msg) {
                toast("" + msg);
                StyledDialogUtils.getInstance().dismissLoading();
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
        StatusBarUtils.setStatusBarColor(this, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GO_REGISTER) {
                //注册
                if (data != null) {
                    edtLoginAccount.setText("" + data.getStringExtra("phone"));
                    edtLoginPassword.setText("" + data.getStringExtra("password"));
                }
            }
            if (requestCode == GO_FORGET_PASSWORD) {
                //忘记密码
                if (data != null) {
                    edtLoginAccount.setText("" + data.getStringExtra("phone"));
                    edtLoginPassword.setText("" + data.getStringExtra("password"));
                }
            }
        }
    }

    //授权回调
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
            Log.e("TAG", "onStart=" + platform);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (platform == SHARE_MEDIA.QQ) {
                String openId = data.get("openid");
                String name = data.get("name");
                Log.e("TAG", "openId=" + openId);
                Log.e("TAG", "name=" + name);
//                thirdLogin(openId, "qq",name);
            } else if (platform == SHARE_MEDIA.WEIXIN) {
                String openId = data.get("openid");
                String name = data.get("name");
                String iconurl = data.get("iconurl");
                Log.e("TAG", "openId=" + openId);
                Log.e("TAG", "name=" + name);
                Log.e("TAG", "iconurl=" + iconurl);
                thirdLogin(openId, iconurl, name);
            } else if (platform == SHARE_MEDIA.SINA) {

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Log.e("TAG", "onError=" + action);
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Log.e("TAG", "onCancel=");
        }
    };

    private void thirdLogin(String openId, String headIm, String name) {
        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.WX_LOGIN)
                .addParam("openid", "" + openId)
                .addParam("head_image", "" + headIm)
                .addParam("user_name", "" + name)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                String userInfo = JSONUtils.getNoteJson(result, "userinfo");
                UserLoginSucBean userLoginSucBean = JSONUtils.jsonString2Bean(userInfo, UserLoginSucBean.class);
                if ("1".equals(userLoginSucBean.getIs_new())) {
                    //新用户去绑定
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.putExtra("isRegister", false);
                    intent.putExtra("openId", "" + openId);
                    intent.putExtra("headIm", "" + headIm);
                    intent.putExtra("name", "" + name);
                    startActivityForResult(intent, GO_REGISTER);
                } else {
                    //老用户直接进首页
                    LoginCheckUtils.saveLoginInfo(userLoginSucBean);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onError(int code, String msg) {
                toast("" + msg);
                StyledDialogUtils.getInstance().dismissLoading();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
            }
        });
    }

}
