package com.benben.megamart.ui.mine;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.StatusBarUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的钱包
 */
public class MyWalletActivity extends BaseActivity {
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    @BindView(R.id.tv_record)
    TextView tvRecord;
    @BindView(R.id.llyt_top_view)
    LinearLayout llytTopView;

    private String mBalance = "";//余额

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_wallet;
    }

    @Override
    protected void initData() {
        initTitle("" + getString(R.string.wallet_my));
        getData();
    }

    private void getData() {
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.MINE_USER_BALANCE)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.has("user_balance") && !object.isNull("user_balance")) {
                        mBalance = object.getString("user_balance");
                    }
                    tvBalance.setText("" + mBalance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    @OnClick({R.id.tv_recharge, R.id.tv_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //充值
            case R.id.tv_recharge:
                startActivityForResult(new Intent(MyWalletActivity.this, RechargeActivity.class), 100);
                break;
            //我的账本：就是记录余额明细
            case R.id.tv_record:
                startActivity(new Intent(MyWalletActivity.this, MyAccountActivity.class));
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        StatusBarUtils.setStatusBarColor(this, R.color.transparent);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
        llytTopView.setPadding(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            getData();
        }
    }

}
