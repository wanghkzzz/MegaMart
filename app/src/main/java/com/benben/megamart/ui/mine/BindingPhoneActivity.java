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
 * 绑定手机号前的展示界面
 */
public class BindingPhoneActivity extends BaseActivity {
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.btn_exchange)
    Button btnExchange;

    private String mPhone = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_binding_phone;
    }

    @Override
    protected void initData() {
        mPhone = getIntent().getStringExtra("phone");

        initTitle( getString(R.string.binding_phone));

        tvPhone.setText(mPhone);
    }

    @OnClick(R.id.btn_exchange)
    public void onViewClicked() {
        //更换手机号
        Intent intentPhone = new Intent(BindingPhoneActivity.this, UpdatePhoneActivity.class);
        intentPhone.putExtra("phone", "" + mPhone);
        startActivity(intentPhone);
        finish();
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }
}
