package com.benben.megamart.ui.mine;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.benben.megamart.BaseActivity;
import com.benben.megamart.R;
import com.benben.megamart.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 绑定邮箱前的展示界面
 */
public class BindingEmailActivity extends BaseActivity {
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.btn_exchange)
    Button btnExchange;

    private String mEmail = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_binding_email;
    }

    @Override
    protected void initData() {

        mEmail = getIntent().getStringExtra("email");

        initTitle("" + getString(R.string.binding_email));

        tvPhone.setText("" + mEmail);

    }

    @OnClick(R.id.btn_exchange)
    public void onViewClicked() {
        //更换邮箱
        Intent intentEmail = new Intent(BindingEmailActivity.this, UpdateEmailActivity.class);
        intentEmail.putExtra("email", "" + mEmail);
        startActivity(intentEmail);
        finish();
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }
}
