package com.benben.megamart.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.R;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.KeyBoardUtils;
import com.benben.megamart.utils.StatusBarUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Create by wanghk on 2019-05-28.
 * Describe:修改密码
 */
public class RetrievePasswordAvtivity extends BaseActivity {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.edt_login_account)
    EditText edtLoginAccount;
    @BindView(R.id.edt_verification_code)
    EditText edtVerificationCode;
    @BindView(R.id.tv_get_verification_code)
    TextView tvGetVerificationCode;
    @BindView(R.id.edt_confirm_new_password)
    EditText edtConfirmNewPassword;
    @BindView(R.id.iv_pwd_visible)
    ImageView ivPwdVisible;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    //密码是否可见
    private boolean isVisible = false;
    //验证码倒计时
    private CountDownTimer mCountDownTimer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_retrieve_password;
    }

    @Override
    protected void initData() {

        centerTitle.setText(getString(R.string.retrieve_password));
        edtConfirmNewPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }


    @OnClick({R.id.rl_back, R.id.tv_get_verification_code, R.id.iv_pwd_visible, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                KeyBoardUtils.hideKeyboard(view);
                finish();
                break;
            case R.id.tv_get_verification_code:
                //获取验证码
                getVerificationCode();
                break;
            case R.id.iv_pwd_visible:
                //密码可见
                if (isVisible) {
                    //从密码可见模式变为密码不可见模式
                    isVisible = false;
                    ivPwdVisible.setImageResource(R.mipmap.password_no_watch);
                    edtConfirmNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                } else {
                    //从密码不可见模式变为密码可见模式
                    isVisible = true;
                    ivPwdVisible.setImageResource(R.mipmap.password_watch);
                    edtConfirmNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                break;
            case R.id.btn_confirm:
                //提交修改密码
                confirm();
                break;
        }
    }

    //提交修改密码
    private void confirm() {
        String phone = edtLoginAccount.getText().toString().trim();
        String verificationCode = edtVerificationCode.getText().toString().trim();
        String password = edtConfirmNewPassword.getText().toString().trim();

        if (StringUtils.isEmpty(edtLoginAccount.getText().toString().trim())) {
            toast(getString(R.string.iphone_number_not_null));
            return;
        }
        if (StringUtils.isEmpty(edtVerificationCode.getText().toString().trim())) {
            toast(getString(R.string.verification_code_not_null));
            return;
        }
        if (StringUtils.isEmpty(edtConfirmNewPassword.getText().toString().trim())) {
            toast(getString(R.string.password_not_null));
            return;
        }


        if (password.length() < 6 || password.length() > 12) {
            toast(getString(R.string.password_length));
            return;
        }
        if (password.contains(" ")) {
            toast(getString(R.string.pwd_cant_include_blank));
            return;
        }

        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.LOGIN_FORGET_PASSWORD)
                .addParam("mobile", "" + phone)
                .addParam("code", "" + verificationCode)
                .addParam("newpassword", "" + password)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String s, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast("" + msg);
                Intent intent = new Intent();
                intent.putExtra("phone", "" + phone);
                intent.putExtra("password", "" + password);
                setResult(RESULT_OK, intent);
                finish();
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

    //获取验证码
    private void getVerificationCode() {
        String phone = edtLoginAccount.getText().toString().trim();
        if (StringUtils.isEmpty(phone)) {
            toast(getString(R.string.iphone_number_not_null));
            return;
        }
        mCountDownTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tvGetVerificationCode.setClickable(false);
                long time = millisUntilFinished / 1000;
                tvGetVerificationCode.setText(time + getResources().getString(R.string.resend_verification_code));

            }

            @Override
            public void onFinish() {
                tvGetVerificationCode.setClickable(true);
                tvGetVerificationCode.setText(getResources().getString(R.string.get_verification_code));

            }
        };

        //type 1=注册(未注册手机号),2=忘记密码(已经注册的手机号),3=短信登录,4修改手机号
        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.SEND_MESSAGE)
                .addParam("phone", "" + phone)
                .addParam("type", "" + 2)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String s, String msg) {
                mCountDownTimer.start();
                toast("" + msg);
                StyledDialogUtils.getInstance().dismissLoading();
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
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }
}
