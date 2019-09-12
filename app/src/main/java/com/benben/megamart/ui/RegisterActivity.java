package com.benben.megamart.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.R;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.StatusBarUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Create by wanghk on 2019-05-28.
 * Describe:注册页面
 */
public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity_TAG";
    @BindView(R.id.edt_register_email)
    EditText edtRegisterEmail;
    @BindView(R.id.edt_register_account)
    EditText edtRegisterAccount;
    @BindView(R.id.edt_verification_code)
    EditText edtVerificationCode;
    @BindView(R.id.tv_get_verification_code)
    TextView tvGetVerificationCode;
    @BindView(R.id.edt_register_password)
    EditText edtRegisterPassword;
    @BindView(R.id.edt_invitation_code)
    EditText edtInvitationCode;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.tv_user_registration_agreement)
    TextView tvUserRegistrationAgreement;
    @BindView(R.id.ll_agree)
    LinearLayout llAgree;
    //验证码倒计时
    private CountDownTimer mCountDownTimer;

    private boolean isRegister = true;//true为注册 否为微信登录绑定手机号
    private String mOpenId = "";//微信id
    private String mName = "";//昵称
    private String mHeadIm = "";//头像


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initData() {

       // edtRegisterPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        isRegister = getIntent().getBooleanExtra("isRegister", true);
        if (!isRegister) {
            btnRegister.setText("" + getString(R.string.bind));
            llAgree.setVisibility(View.GONE);
            mOpenId = getIntent().getStringExtra("openId");
            mName = getIntent().getStringExtra("name");
            mHeadIm = getIntent().getStringExtra("headIm");
        }
    }

    @OnClick({R.id.tv_get_verification_code, R.id.btn_register, R.id.tv_user_registration_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_verification_code:
                //获取验证码
                if (isRegister) {
                    getVerificationCode(1);
                } else {
                    getVerificationCode(6);
                }
                break;
            case R.id.btn_register:
                //注册
                if (isRegister) {
                    register();
                } else {
                    bindWx();
                }
                break;
            case R.id.tv_user_registration_agreement:
                //用户注册协议
                getRegistrationAgreement();
                break;
        }
    }

    //获取用户注册协议
    private void getRegistrationAgreement() {

        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_REGISTER_AGREEMENT)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取用户注册协议----" + result);
                        StyledDialogUtils.getInstance().dismissLoading();
                        String noteJson = JSONUtils.getNoteJson(result, "agreement_info");
                        Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                        intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_URL, noteJson);
                        intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_TITLE, getResources().getString(R.string.user_register_agreement));
                        intent.putExtra(Constants.EXTRA_KEY_IS_URL, false);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.show(mContext, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                    }
                });


    }

    //获取验证码
    private void getVerificationCode(int type) {

        String phone = edtRegisterAccount.getText().toString().trim();
        if (StringUtils.isEmpty(phone)) {
            toast(getString(R.string.iphone_number_not_null));
            return;
        }
        if (mCountDownTimer == null) {

            mCountDownTimer = new CountDownTimer(60000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                    long time = millisUntilFinished / 1000;
                    tvGetVerificationCode.setClickable(false);
                    tvGetVerificationCode.setText(time + getResources().getString(R.string.resend_verification_code));
                }

                @Override
                public void onFinish() {
                    tvGetVerificationCode.setClickable(true);
                    tvGetVerificationCode.setText(getResources().getString(R.string.get_verification_code));

                }
            };
        }

        StyledDialogUtils.getInstance().loading(mContext);
        //type 1=注册(未注册手机号),2=忘记密码(已经注册的手机号),3=短信登录,4修改手机号
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.SEND_MESSAGE)
                .addParam("phone", "" + phone)
                .addParam("type", "" + type)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String s, String msg) {
                toast("" + msg);
                mCountDownTimer.start();
                StyledDialogUtils.getInstance().dismissLoading();
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

    //注册
    private void register() {

      //  String email = edtRegisterEmail.getText().toString().trim();
        String phone = edtRegisterAccount.getText().toString().trim();
        String verificationCode = edtVerificationCode.getText().toString().trim();
        String password = edtRegisterPassword.getText().toString().trim();
        String invitationCode = edtInvitationCode.getText().toString().trim();

//        if (StringUtils.isEmpty(email)) {
//            toast(getString(R.string.email_not_null));
//            return;
//        }
        if (StringUtils.isEmpty(phone)) {
            toast(getString(R.string.iphone_number_not_null));
            return;
        }
        if (StringUtils.isEmpty(verificationCode)) {
            toast(getString(R.string.verification_code_not_null));
            return;
        }

        if (edtRegisterPassword.getText().toString().contains(" ")) {
            toast(getString(R.string.pwd_cant_include_blank));
            return;
        }
        if (StringUtils.isEmpty(password)) {
            toast(getString(R.string.password_not_null));
            return;
        }
        if (password.length() < 6 || password.length() > 12) {
            toast(getString(R.string.password_length));
            return;
        }


        StyledDialogUtils.getInstance().loading(mContext);
        //手机号注册
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.LOGIN_PHONE_REGISTER)
                .addParam("phone", "" + phone)
                .addParam("code", "" + verificationCode)
                .addParam("password", "" + password)
                .addParam("email", "" )
//                .addParam("email", "" + email)
                .addParam("invit_code", "" + (invitationCode == null ? "" : invitationCode))
                .addParam("client_type", "" + 1)
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

    //微信登录绑定手机号
    private void bindWx() {

//        String email = edtRegisterEmail.getText().toString().trim();
        String phone = edtRegisterAccount.getText().toString().trim();
        String verificationCode = edtVerificationCode.getText().toString().trim();
        String password = edtRegisterPassword.getText().toString().trim();
        String invitationCode = edtInvitationCode.getText().toString().trim();


        /*if (StringUtils.isEmpty(email)) {
            toast(getString(R.string.email_not_null));
            return;
        }*/
        if (StringUtils.isEmpty(phone)) {
            toast(getString(R.string.iphone_number_not_null));
            return;
        }
        if (StringUtils.isEmpty(verificationCode)) {
            toast(getString(R.string.verification_code_not_null));
            return;
        }

        if (edtRegisterPassword.getText().toString().contains(" ")) {
            toast(getString(R.string.pwd_cant_include_blank));
            return;
        }
        if (StringUtils.isEmpty(password)) {
            toast(getString(R.string.password_not_null));
            return;
        }
        if (password.length() < 6 || password.length() > 12) {
            toast(getString(R.string.password_length));
            return;
        }
        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.WX_LOGIN_GIND_PHONE)
                .addParam("user_mobile", "" + phone)
                .addParam("code", "" + verificationCode)
                .addParam("password", "" + password)
                .addParam("email", "")
//                .addParam("email", "" + email)
                .addParam("openid", "" + mOpenId)
                .addParam("user_name", "" + mName)
                .addParam("head_image", "" + mHeadIm)
                .addParam("invit_code", "" + (invitationCode == null ? "" : invitationCode))
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
        StatusBarUtils.setStatusBarColor(this, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, true);
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
