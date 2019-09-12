package com.benben.megamart.ui;

import android.Manifest;
import android.location.Address;
import android.location.Location;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.AFinalRecyclerViewAdapter;
import com.benben.megamart.adapter.LocationListAdapter;
import com.benben.megamart.adapter.PostCodeAddressListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.GeocodingEntity;
import com.benben.megamart.bean.PostCodeAddressListBean;
import com.benben.megamart.bean.UsedAddressListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.GeocodingUtils;
import com.benben.megamart.utils.KeyBoardUtils;
import com.benben.megamart.utils.LocationHelper;
import com.benben.megamart.utils.StatusBarUtils;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Create by wanghk on 2019-05-27.
 * Describe:选择地址页面
 */
public class LocationActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.edt_post_code)
    EditText edtPostCode;
    @BindView(R.id.tv_positive)
    TextView tvPositive;
    @BindView(R.id.edt_location)
    EditText edtLocation;
    @BindView(R.id.btn_get_location)
    Button btnGetLocation;
    @BindView(R.id.rlv_address_list)
    RecyclerView rlvAddressList;
    @BindView(R.id.rlv_post_code_address_list)
    RecyclerView rlvPostCodeAddressList;
    @BindView(R.id.stf_layout)
    SmartRefreshLayout stfLayout;
    //常用地址列表
    private LocationListAdapter mLocationListAdapter;
    private LocationHelper mLocationHelper;
    private PostCodeAddressListAdapter mPostCodeAddressListAdapter;
    private String mDetailsAddress = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_location;
    }

    @Override
    protected void initData() {

        centerTitle.setText(getString(R.string.choose_address));

        stfLayout.setEnableLoadMore(false);
        rlvPostCodeAddressList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mPostCodeAddressListAdapter = new PostCodeAddressListAdapter(mContext);
        rlvPostCodeAddressList.setAdapter(mPostCodeAddressListAdapter);
        rlvAddressList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mLocationListAdapter = new LocationListAdapter(mContext);
        rlvAddressList.setAdapter(mLocationListAdapter);

        //获取常用地址列表
        getUsedAddressList();

        mPostCodeAddressListAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<PostCodeAddressListBean>() {
            @Override
            public void onItemClick(View view, int position, PostCodeAddressListBean model) {
                edtPostCode.setText("");
                edtLocation.setText(model.getArea_name());
                mPostCodeAddressListAdapter.clear();
                rlvPostCodeAddressList.setVisibility(View.GONE);
                MegaMartApplication.mPreferenceProvider.put(mContext, Constants.SP_KEY_USED_ADDRESS, model.getArea_name());
                setAddress(model.getZip_code(), model.getArea_name());
                setResult(200);
                finish();
            }

            @Override
            public void onItemLongClick(View view, int position, PostCodeAddressListBean model) {

            }
        });

        mLocationListAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<UsedAddressListBean>() {
            @Override
            public void onItemClick(View view, int position, UsedAddressListBean model) {
                MegaMartApplication.mPreferenceProvider.put(mContext, Constants.SP_KEY_USED_ADDRESS, model.getCity());
                setResult(200);
                finish();
                // setAddress(model.getZip_code(), model.getCity());
            }

            @Override
            public void onItemLongClick(View view, int position, UsedAddressListBean model) {

            }
        });

        getLocationPermisson();

    }

    //设置首页地址
    private void setAddress(String zip_code, String city) {

        if (StringUtils.isEmpty(zip_code)) {
            zip_code = "0";
        }
        try {
            //JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("zip_code", zip_code);
            jsonObject.put("city", city);
            jsonObject.put("address", mDetailsAddress);
            // jsonArray.put(jsonObject);
            Log.e(Constants.WHK_TAG, "setAddress: userid=" + MegaMartApplication.mPreferenceProvider.getUId() + "address_info =" + jsonObject.toString());
            BaseOkHttpClient.newBuilder()
                    .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                    .addParam("address_info", jsonObject)
                    .url(NetUrlUtils.SET_ADDRESS)
                    .json()
                    .post().build().enqueue(mContext, new
                    BaseCallBack<String>() {
                        @Override
                        public void onSuccess(String result, String msg) {
                            Log.e(Constants.WHK_TAG, "onSuccess: 首页设置地址----" + result);
                            StyledDialogUtils.getInstance().dismissLoading();

                            MegaMartApplication.mPreferenceProvider.put(mContext, Constants.SP_KEY_USED_ADDRESS, city);

                        }

                        @Override
                        public void onError(int code, String msg) {
                            ToastUtils.show(mContext, msg);
                            StyledDialogUtils.getInstance().dismissLoading();
                        }

                        @Override
                        public void onFailure(Call call, IOException e) {
                            ToastUtils.show(mContext, e.getMessage());
                            StyledDialogUtils.getInstance().dismissLoading();
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //获取定位权限
    private void getLocationPermisson() {

        XXPermissions.with(mContext)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        //授权成功
                        getLocation();
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        ToastUtils.show(mContext, mContext.getResources().getString(R.string.please_authorize));
                    }
                });

    }

    //获取常用地址列表
    private void getUsedAddressList() {

        BaseOkHttpClient.newBuilder()
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.GET_OLD_ADDRESS)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取历史地址----" + result);
                        StyledDialogUtils.getInstance().dismissLoading();
                        String noteJson = JSONUtils.getNoteJson(result, "address_list");
                        List<UsedAddressListBean> usedAddressList = JSONUtils.jsonString2Beans(noteJson, UsedAddressListBean.class);
                        mLocationListAdapter.appendToList(usedAddressList);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.show(mContext, e.getMessage());
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


    @OnClick({R.id.rl_back, R.id.tv_positive, R.id.btn_get_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                setResult(200);
                finish();
                break;
            case R.id.tv_positive:
                getPostCodeLocation();
                break;
            case R.id.btn_get_location:
                //获取定位
                getLocationPermisson();
                break;
        }
    }

    //邮编查询地址
    private void getPostCodeLocation() {
        if (StringUtils.isEmpty(edtPostCode.getText().toString().trim())) {
            toast(getResources().getString(R.string.postal_code_not_null));
            return;
        }

        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .addParam("search_keyword", edtPostCode.getText().toString().trim())//搜索关键词（地区名称/邮编）
                .addParam("area_level", 2) //0:全部 1:州；2:区
                .url(NetUrlUtils.GET_AREA_INFO)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 邮编查询地址----" + result);
                        StyledDialogUtils.getInstance().dismissLoading();
                        String noteJson = JSONUtils.getNoteJson(result, "area_info");
                        List<PostCodeAddressListBean> postCodeAddressList = JSONUtils.jsonString2Beans(noteJson, PostCodeAddressListBean.class);
                        if (postCodeAddressList != null && postCodeAddressList.size() > 0) {
//                            rlvPostCodeAddressList.setVisibility(View.VISIBLE);
                            mPostCodeAddressListAdapter.refreshList(postCodeAddressList);
                        }

                    }

                    @Override
                    public void onError(int code, String msg) {
                        ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.show(mContext, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                    }
                });

    }

    //获取定位
    private void getLocation() {//自己修改自己需要的语言，现在的是英文,不需要翻译的话就不用翻译

        mLocationHelper = new LocationHelper(mContext, new LocationHelper.OnLocationLoadedListener() {
            @Override
            public void locationLoaded(Location location) {

                mLocationHelper.stopRequestLocationUpdates();

                Locale locale = Locale.ENGLISH;

                Address address = GeocodingUtils.getLocality(location.getLatitude(), location.getLongitude(), locale);



                /*if (Locale.ENGLISH.getLanguage().equals(locale.getLanguage())) {//英文状态下
                    String str = addr.replace(" ", "");
                    if (!StringUtils.isEmpty(str) && GeocodingUtils.isChinese(str)) {//是中文的地址，翻译一下
                        new TranslateUtil().translate(mContext, "zh", "en", addr, new TranslateUtil.TranslateCallback() {
                            @Override
                            public void onTranslateDone(String result) {
                                edtLocation.setText(result);
                            }
                        });
                        return;
                    }
                }*/

                if (address != null) {
                    if (StringUtils.isEmpty(address.getLocality())) {

                        GeocodingUtils.makeGeocodeSearch(mContext, location.getLongitude(), location.getLatitude(), Locale.ENGLISH, new GeocodingUtils.OnGeocodingCompleteListener() {
                            @Override
                            public void onGeocodingSuccess(GeocodingEntity geocodingEntity) {
                                edtLocation.setText(geocodingEntity.getCityName());
                            }

                            @Override
                            public void onGeocodignFailure() {

                            }
                        });
                    } else {
                        edtLocation.setText(address.getLocality());
                    }

                    MegaMartApplication.mPreferenceProvider.put(mContext, Constants.SP_KEY_USED_ADDRESS, edtLocation.getText().toString());
                    setResult(200);
                    PostCodeAddressListBean postCodeAddressListBean = new PostCodeAddressListBean();
                    postCodeAddressListBean.setArea_name(address.getLocality());
                    postCodeAddressListBean.setZip_code(address.getPostalCode());

                    if (address.getAdminArea() != null) {
                        mDetailsAddress = address.getAdminArea();
                    }
                    if (address.getAdminArea() != null && address.getLocality() != null) {
                        mDetailsAddress = address.getAdminArea() + address.getLocality();
                    }
                    if (address.getAdminArea() != null && address.getLocality() != null && address.getSubThoroughfare() != null) {
                        mDetailsAddress = address.getAdminArea() + address.getLocality() + address.getSubThoroughfare();
                    }
                    if (!StringUtils.isEmpty(MegaMartApplication.mPreferenceProvider.getUId())) {
                        setAddress(postCodeAddressListBean.getZip_code(), postCodeAddressListBean.getArea_name());
                    }

                   // toast("current location:" + mDetailsAddress);
                    Log.e(Constants.WHK_TAG, "locationLoaded: 定位地址 ：" + address.toString());
                }


            }
        });

        mLocationHelper.startRequestLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyBoardUtils.hideKeyboard(edtPostCode);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationHelper != null) {
            mLocationHelper.stopRequestLocationUpdates();
        }
    }


}
