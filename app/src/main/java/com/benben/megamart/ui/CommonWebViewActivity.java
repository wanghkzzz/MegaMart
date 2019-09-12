package com.benben.megamart.ui;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.widget.TopProgressWebView;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.R;
import com.benben.megamart.config.Constants;
import com.benben.megamart.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by: wanghk 2019-06-05.
 * Describe:通用的Webview页面
 */
public class CommonWebViewActivity extends BaseActivity {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.wv_details)
    TopProgressWebView wvAdvertDetails;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_advert_details;
    }

    @Override
    protected void initData() {
        //url 或者是 富文本
        String url = getIntent().getStringExtra(Constants.EXTRA_KEY_WEB_VIEW_URL);
        //标题
        String title = getIntent().getStringExtra(Constants.EXTRA_KEY_WEB_VIEW_TITLE);
        //是否是url 或是富文本
        boolean isUrl = getIntent().getBooleanExtra(Constants.EXTRA_KEY_IS_URL, true);

        Log.e(Constants.WHK_TAG, "initData: url =" + url);
        centerTitle.setText(title);


        if (isUrl) {
            //加载URL
            wvAdvertDetails.loadUrl(url);
        } else {
            //加载HTML文本
            wvAdvertDetails.loadTextContent(url);
        }


    }


    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }


    @OnClick({R.id.rl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
        }
    }
}
