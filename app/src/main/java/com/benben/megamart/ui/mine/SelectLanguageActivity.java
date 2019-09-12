package com.benben.megamart.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.benben.megamart.BaseActivity;
import com.benben.megamart.R;
import com.benben.megamart.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择语言
 */
public class SelectLanguageActivity extends BaseActivity {
    @BindView(R.id.iv_select_chinese)
    ImageView ivSelectChinese;
    @BindView(R.id.rl_select_chinese)
    RelativeLayout rlSelectChinese;
    @BindView(R.id.iv_select_english)
    ImageView ivSelectEnglish;
    @BindView(R.id.rl_select_english)
    RelativeLayout rlSelectEnglish;
    @BindView(R.id.btn_save)
    Button btnSave;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_language;
    }

    @Override
    protected void initData() {
        initTitle("" + getString(R.string.language_select));
    }

    @OnClick({R.id.iv_select_chinese, R.id.rl_select_chinese, R.id.iv_select_english, R.id.rl_select_english, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //选择中文
            case R.id.iv_select_chinese:
            case R.id.rl_select_chinese:
                ivSelectChinese.setImageResource(R.mipmap.icon_select_theme);
                ivSelectEnglish.setImageResource(R.mipmap.icon_select_no);
                break;
            //选择英文
            case R.id.iv_select_english:
            case R.id.rl_select_english:
                ivSelectEnglish.setImageResource(R.mipmap.icon_select_theme);
                ivSelectChinese.setImageResource(R.mipmap.icon_select_no);
                break;
            case R.id.btn_save:
                break;
        }
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }
}
