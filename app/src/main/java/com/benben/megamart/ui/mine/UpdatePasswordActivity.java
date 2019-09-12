package com.benben.megamart.ui.mine;

import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.LoginCheckUtils;
import com.benben.megamart.utils.StatusBarUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 修改密码
 */
public class UpdatePasswordActivity extends BaseActivity {
    @BindView(R.id.edt_login_account)
    EditText edtLoginAccount;
    @BindView(R.id.edt_new_pwd)
    EditText edtNewPwd;
    @BindView(R.id.iv_pwd_visible)
    ImageView ivPwdVisible;
    @BindView(R.id.edt_new_pwd_two)
    EditText edtNewPwdTwo;
    @BindView(R.id.iv_pwd_visible_two)
    ImageView ivPwdVisibleTwo;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private boolean isVisible[] = {false, false};//密码是否可见 第一个为第一个密码，第二个为确认密码

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_password;
    }

    @Override
    protected void initData() {
        initTitle("" + getString(R.string.password_update));
        edtNewPwdTwo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        edtNewPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});

//        edtLoginAccount.setText(""+MegaMartApplication.mPreferenceProvider.getMobile());
    }

    @OnClick({R.id.iv_pwd_visible, R.id.iv_pwd_visible_two, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pwd_visible:
                //密码可见
                if (isVisible[0]) {
                    //从密码可见模式变为密码不可见模式
                    isVisible[0] = false;
                    ivPwdVisible.setImageResource(R.mipmap.password_no_watch);
                    edtNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());

                } else {
                    //从密码不可见模式变为密码可见模式
                    isVisible[0] = true;
                    ivPwdVisible.setImageResource(R.mipmap.password_watch);
                    edtNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                break;
            case R.id.iv_pwd_visible_two:
                if (isVisible[1]) {
                    //从密码可见模式变为密码不可见模式
                    isVisible[1] = false;
                    ivPwdVisibleTwo.setImageResource(R.mipmap.password_no_watch);
                    edtNewPwdTwo.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    //从密码不可见模式变为密码可见模式
                    isVisible[1] = true;
                    ivPwdVisibleTwo.setImageResource(R.mipmap.password_watch);
                    edtNewPwdTwo.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                break;
            case R.id.btn_confirm:
                updatePwd();
                break;
        }
    }

    /**
     * 修改密码
     */
    private void updatePwd() {
        if(!LoginCheckUtils.checkUserIsLogin(mContext)){
            toast(getResources().getString(R.string.please_login_first));
            return;
        }
        String newPwd = edtNewPwd.getText().toString().trim();
        String newPwdConfirm = edtNewPwdTwo.getText().toString().trim();
        String phone = edtLoginAccount.getText().toString().trim();



        if (TextUtils.isEmpty(newPwd)) {
            toast("" + getString(R.string.password_not_null));
            return;
        }
        if (TextUtils.isEmpty(newPwdConfirm)) {
            toast("" + getString(R.string.password_confirm_new_password));
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            toast("" + getString(R.string.iphone_number_not_null));
            return;
        }
        if (!newPwd.equals(newPwdConfirm)) {
            toast("" + getString(R.string.pwd_no_same));
            return;
        }

        if (newPwd.length() < 6 || newPwd.length() > 12) {
            toast(getString(R.string.password_length));
            return;
        }
        if (newPwd.contains(" ")) {
            toast(getString(R.string.pwd_cant_include_blank));
            return;
        }

        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.MINE_UPDATE_PWD)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("new_pwd", "" + newPwd)
                .addParam("mobile", "" + phone)
                .addParam("re_new_pwd", "" + newPwd)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(msg);
                setResult(RESULT_OK);
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
                toast(getResources().getString(R.string.server_exception));
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
