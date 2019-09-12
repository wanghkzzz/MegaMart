package com.benben.megamart.ui.mine;

import android.content.Intent;

import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.R;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.ui.CommonWebViewActivity;
import com.benben.megamart.utils.StatusBarUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/18
 * Time: 14:07
 *
 * 联系我们
 */
public class ConnectionMineActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_connection_mine;
    }

    @Override
    protected void initData() {
        initTitle(""+getString(R.string.connection_mine));
        getData();
    }

    private void getData(){
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.CONNECTION_MINE)
                .addParam("client_type", "" + 1)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
            }

            @Override
            public void onError(int code, String msg) {
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
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }
}
