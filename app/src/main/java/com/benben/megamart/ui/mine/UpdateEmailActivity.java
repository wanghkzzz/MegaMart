package com.benben.megamart.ui.mine;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.R;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.StatusBarUtils;
import com.benben.megamart.utils.TimerUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 更改绑定邮箱
 */
public class UpdateEmailActivity extends BaseActivity {
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.edt_verification_code)
    EditText edtVerificationCode;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private String mEmail = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_email;
    }

    @Override
    protected void initData() {

        mEmail = getIntent().getStringExtra("email");

        initTitle("" + getString(R.string.binding_email));

        edtPhone.setText("" + mEmail);
        edtPhone.setFocusable(false);
    }

    @OnClick({R.id.tv_code, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //获取验证码
            case R.id.tv_code:

                sendMessage();
                break;
            //确定更换邮箱
            case R.id.btn_submit:
                exchangeEmail();
                break;
        }
    }

    /**
     * 确定更换邮箱
     */
    private void exchangeEmail() {
        String email = edtPhone.getText().toString().trim();
        String code = edtVerificationCode.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            toast(getString(R.string.enter_the_email));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            toast(getString(R.string.enter_the_verification_code));
            return;
        }
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.CHECK_EMS)
                .addParam("captcha", "" + code)
                .addParam("email", "" + email)
                .addParam("type", "" + 4)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(msg);
                Intent intent=new Intent(UpdateEmailActivity.this,BindEmailActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(e.getMessage());
            }
        });
    }

    /**
     * 发送验证码
     */
    private void sendMessage() {
        String email = edtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            toast(getString(R.string.enter_the_email));
            return;
        }
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.SEND_EMAIL_CODE)
                .addParam("email", "" + email)
                .addParam("type", "" + 4)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(msg);
                TimerUtil timerUtil = new TimerUtil(tvCode,mContext);
                timerUtil.timers();
            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(e.getMessage());
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
