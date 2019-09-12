package com.benben.megamart.ui.mine;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.AddressManagerBean;
import com.benben.megamart.bean.SelectAreaBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.RxBus;
import com.benben.megamart.utils.StatusBarUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/14
 * Time: 11:54
 * 新增地址
 */
public class AddAddressActivity extends BaseActivity {
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_detail_address)
    EditText edtDetailAddress;
    @BindView(R.id.edt_zip_code)
    EditText edtZipCode;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.iv_setting_default)
    ImageView ivSettingDefault;
    @BindView(R.id.edt_unit_number)
    EditText edtUnitNumber;
    @BindView(R.id.edt_street_number)
    EditText edtStreetNumber;

    private boolean isEditor = false;
    private AddressManagerBean.AddressListBean mBean;

    private String mProvince = null;
    private String mCity = null;

    private String mDetault = "1";//设为默认 1为默认 0为不设置默认

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_address;
    }

    @Override
    protected void initData() {

        isEditor = getIntent().getBooleanExtra("editor", false);
        if (isEditor) {
            mBean = (AddressManagerBean.AddressListBean) getIntent().getSerializableExtra("bean");
            //修改
            initTitle("" + getString(R.string.address_editor));
            edtName.setText("" + mBean.getLink_man());
            edtPhone.setText("" + mBean.getLink_phone());
            edtDetailAddress.setText("" + mBean.getAddress_info());
            edtZipCode.setText("" + mBean.getZip_code());
            edtUnitNumber.setText(null == mBean.getUnit_number() || "null".equals(mBean.getUnit_number()) ? "" : mBean.getUnit_number());
            edtStreetNumber.setText(null == mBean.getStreet_number() || "null".equals(mBean.getStreet_number()) ? "" : mBean.getStreet_number());
            tvArea.setText("" + mBean.getProvince_name());
            tvState.setText("" + mBean.getCity_name());
            mProvince = "" + mBean.getProvince();
            mCity = "" + mBean.getCity();
            mDetault = mBean.getIs_default();
            if ("1".equals(mBean.getIs_default())) {
                ivSettingDefault.setImageResource(R.mipmap.icon_kai);
            } else {
                ivSettingDefault.setImageResource(R.mipmap.icon_guan);
            }
        } else {
            //新增
            initTitle("" + getString(R.string.address_add));
        }
        rightTitle.setText("" + getString(R.string.address_save));
    }

    @OnClick({R.id.right_title, R.id.tv_area, R.id.tv_state, R.id.iv_setting_default})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            //保存或者编辑
            case R.id.right_title:
                if (isEditor) {
                    editorAddress();
                } else {
                    addAddress();
                }
                break;
            //设为默认
            case R.id.iv_setting_default:
                if ("1".equals(mDetault)) {
                    ivSettingDefault.setImageResource(R.mipmap.icon_guan);
                    mDetault = "0";
                } else {
                    mDetault = "1";
                    ivSettingDefault.setImageResource(R.mipmap.icon_kai);
                }
                break;
            case R.id.tv_area: //1 州 2区
                intent = new Intent(AddAddressActivity.this, SelectAreaActivity.class);
                intent.putExtra("type", 2);
                startActivityForResult(intent, 101);
                break;
            case R.id.tv_state:
                intent = new Intent(AddAddressActivity.this, SelectAreaActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, 102);
                break;
        }
    }

    /**
     * 增加收货地址
     */
    private void addAddress() {

        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String detail = edtDetailAddress.getText().toString().trim();
        String zipCode = edtZipCode.getText().toString().trim();
        String unitNumber = edtUnitNumber.getText().toString().trim();
        String streetNumber = edtStreetNumber.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            toast(getResources().getString(R.string.please_enter_consignee));
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            toast(getResources().getString(R.string.please_enter_phone));
            return;
        }
        if (TextUtils.isEmpty(detail)) {
            toast(getResources().getString(R.string.please_enter_street_name));
            return;
        }
        if (TextUtils.isEmpty(streetNumber)) {
            toast(getResources().getString(R.string.please_enter_street_number));
            return;
        }
       /* if (TextUtils.isEmpty(unitNumber)) {
            toast(getResources().getString(R.string.please_enter_house_number));
            return;
        }*/
        if (mCity == null) {
            toast(getResources().getString(R.string.please_select_district));
            return;
        }
        if (mProvince == null) {
            toast(getResources().getString(R.string.please_select_state));
            return;
        }
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.MINE_ADD_ADDRESS)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("link_man", "" + name)
                .addParam("link_phone", "" + phone)
                .addParam("address_info", "" + detail)
                .addParam("zip_code", "" + zipCode)
                .addParam("province", "" + mProvince)
                .addParam("city", "" + mCity)
                .addParam("is_default", "" + mDetault)
                .addParam("unit_number", "" + unitNumber)
                .addParam("street_number", "" + streetNumber)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                RxBus.getInstance().post(1002);
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

    /**
     * 编辑收货地址
     */
    private void editorAddress() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String detail = edtDetailAddress.getText().toString().trim();
        String zipCode = edtZipCode.getText().toString().trim();
        String unitNumber = edtUnitNumber.getText().toString().trim();
        String streetNumber = edtStreetNumber.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            toast(getResources().getString(R.string.please_enter_consignee));
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            toast(getResources().getString(R.string.please_enter_phone));
            return;
        }
        if (TextUtils.isEmpty(detail)) {
            toast(getResources().getString(R.string.please_enter_street_name));
            return;
        }
        if (TextUtils.isEmpty(streetNumber)) {
            toast(getResources().getString(R.string.please_enter_street_number));
            return;
        }
       /* if (TextUtils.isEmpty(unitNumber)) {
            toast(getResources().getString(R.string.please_enter_house_number));
            return;
        }*/
        if (TextUtils.isEmpty(zipCode)) {
            toast(getResources().getString(R.string.please_enter_postal_code));
            return;
        }
        if (mCity == null) {
            toast(getResources().getString(R.string.please_select_district));
            return;
        }
        if (mProvince == null) {
            toast(getResources().getString(R.string.please_select_state));
            return;
        }
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.UPDATE_ADDRESS)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("link_man", "" + name)
                .addParam("link_phone", "" + phone)
                .addParam("address_info", "" + detail)
                .addParam("zip_code", "" + zipCode)
                .addParam("province", "" + mProvince)
                .addParam("city", "" + mCity)
                .addParam("address_id", "" + mBean.getAddress_id())
                .addParam("is_default", "" + mDetault)
                .addParam("unit_number", "" + unitNumber)
                .addParam("street_number", "" + streetNumber)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                RxBus.getInstance().post(1001);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                SelectAreaBean.AreaInfoBean bean = (SelectAreaBean.AreaInfoBean) data.getSerializableExtra("bean");
                Log.e(Constants.WHK_TAG, "onActivityResult: bean.getArea_name() = " + bean.getArea_name());
                if (requestCode == 101) {
                    tvArea.setText("" + bean.getArea_name());
                    mCity = "" + bean.getArea_id();
                    edtZipCode.setText("" + bean.getZip_code());
                } else if (requestCode == 102) {
                    tvState.setText("" + bean.getArea_name());
                    mProvince = "" + bean.getArea_id();
//                    mProvince = "" + bean.getArea_pid();
                }
            }
        }
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }
}
