package com.benben.megamart.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.PersonDataBean;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.StatusBarUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 查看个人资料
 */

public class PersonDataActivity extends BaseActivity {

    private int EXTRE_UPDATE_DATA = 101;//修改个人资料

    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_receiver_address)
    TextView tvReceiverAddress;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_phone)
    TextView tvPhone;

    private PersonDataBean mBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_data;
    }

    @Override
    protected void initData() {
        initTitle("" + getString(R.string.person_data));
        rightTitle.setText("" + getString(R.string.person_editor));
        rightTitle.setTextColor(getResources().getColor(R.color.white));

    }

    private void getData() {
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.MINE_PERSON_DATA)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                mBean = JSONUtils.jsonString2Bean(result, PersonDataBean.class);

                PersonDataBean.UserinfoBean bean = mBean.getUserinfo().get(0);
                ImageUtils.getPic(bean.getUser_avatar(), ivHeader
                        , mContext, R.mipmap.mine_no_login_header);
                tvId.setText("" + bean.getUser_id());
                tvName.setText("" + bean.getUser_name());
                if ("1".equals(bean.getUser_sex())) {
                    tvSex.setText(getResources().getString(R.string.man));
                } else if ("0".equals(bean.getUser_sex())) {
                    tvSex.setText(getResources().getString(R.string.woman));
                } else {
                    tvSex.setText(getResources().getString(R.string.unknown));
                }
                tvEmail.setText("" + bean.getUser_email());
                tvPhone.setText("" + bean.getUser_mobile());
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
    protected void onResume() {
        super.onResume();
        getData();
    }

    @OnClick({R.id.right_title, R.id.iv_header, R.id.rl_header, R.id.tv_id, R.id.tv_name, R.id.tv_sex, R.id.llyt_receiver_address, R.id.llyt_email, R.id.llyt_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //编辑
            case R.id.right_title:
                if (mBean == null) {
                    toast(getResources().getString(R.string.get_data_failure));
                    getData();
                    return;
                }
                Intent intent = new Intent(this, PersonEditorActivity.class);
                startActivityForResult(intent, EXTRE_UPDATE_DATA);
                finish();
                break;
            //头像
            case R.id.iv_header:
            case R.id.rl_header:
                break;
            //id
            case R.id.tv_id:
                break;
            //昵称
            case R.id.tv_name:
                break;
            //性别
            case R.id.tv_sex:
                break;
            //收货地址
            case R.id.llyt_receiver_address:
                startActivity(new Intent(PersonDataActivity.this, AddressManagerActivity.class));
                break;
            //邮箱
            case R.id.llyt_email:
                if (mBean == null) {
                    Toast.makeText(mContext, getResources().getString(R.string.get_data_failure), Toast.LENGTH_SHORT).show();
                    getData();
                    return;
                }
                if (mBean.getUserinfo() == null || mBean.getUserinfo().size() <= 0 || StringUtils.isEmpty(mBean.getUserinfo().get(0).getUser_email())) {
                    Intent intentEmail = new Intent(PersonDataActivity.this, BindEmailActivity.class);
                    startActivity(intentEmail);
                } else {
                    Intent intentEmail = new Intent(PersonDataActivity.this, BindingEmailActivity.class);
                    intentEmail.putExtra("email", "" + mBean.getUserinfo().get(0).getUser_email());
                    startActivity(intentEmail);
                }


                break;
            //手机号
            case R.id.llyt_phone:
                if (mBean == null) {
                    Toast.makeText(mContext, getResources().getString(R.string.get_data_failure), Toast.LENGTH_SHORT).show();
                    getData();
                    return;
                }
                Intent intentPhone = new Intent(PersonDataActivity.this, BindingPhoneActivity.class);
                intentPhone.putExtra("phone", "" + mBean.getUserinfo().get(0).getUser_mobile());
                startActivity(intentPhone);
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
