package com.benben.megamart.frag;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.commoncore.widget.badgeview.BGABadgeImageView;
import com.benben.commoncore.widget.badgeview.BGABadgeViewHelper;
import com.benben.megamart.LazyBaseFragments;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.HomeTabViewPagerAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.GeocodingEntity;
import com.benben.megamart.bean.HomeTopTabEntityBean;
import com.benben.megamart.bean.HomeUnreadMessageCountBean;
import com.benben.megamart.bean.PostCodeAddressListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.ui.LocationActivity;
import com.benben.megamart.ui.MessageListActivity;
import com.benben.megamart.ui.SearchGoodsRecordActivity;
import com.benben.megamart.utils.GeocodingUtils;
import com.benben.megamart.utils.LocationHelper;
import com.benben.megamart.utils.LoginCheckUtils;
import com.benben.megamart.utils.StatusBarUtils;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Create by wanghk on 2019-05-27.
 * Describe:首页
 */
public class MainHomeFragment extends LazyBaseFragments {

    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.edt_goods_search)
    EditText edtGoodsSearch;
    @BindView(R.id.iv_message)
    BGABadgeImageView ivMessage;
    @BindView(R.id.vp_goods_home)
    ViewPager vpGoodsHome;
    @BindView(R.id.xTablayout)
    XTabLayout xTablayout;


    //tab数据list
    private List<HomeTopTabEntityBean> mTabEntities;
    //定位帮助类
    private LocationHelper mLocationHelper;
    //详细地址
    private String mDetailsAddress;

    public static MainHomeFragment getInstance() {
        MainHomeFragment sf = new MainHomeFragment();
        return sf;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_main_home, null);
        return mRootView;
    }

    @Override
    public void initView() {
        StatusBarUtils.setStatusBarColor(getActivity(), R.color.color_EC5413);
        mRootView.setPadding(0, StatusBarUtils.getStatusBarHeight(getActivity()), 0, 0);

        //ivMessage.showCirclePointBadge();
        ivMessage.getBadgeViewHelper().setBadgeGravity(BGABadgeViewHelper.BadgeGravity.RightTop);
        initTabEntity();

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isPrepared()){
            StatusBarUtils.setStatusBarColor(getActivity(), R.color.color_EC5413);
            mRootView.setPadding(0, StatusBarUtils.getStatusBarHeight(getActivity()), 0, 0);

        }
    }

    private void initFragment() {
        //添加fragment
        List<LazyBaseFragments> mFragmentList = new ArrayList<>();
        for (int i = 0; i < mTabEntities.size(); i++) {
            mFragmentList.add(HomeHotGoodsFragment.getInstance());

        }
        vpGoodsHome.setAdapter(new HomeTabViewPagerAdapter(getChildFragmentManager(), mTabEntities, mFragmentList));
        xTablayout.setupWithViewPager(vpGoodsHome);
    }


    //初始化tab数据
    private void initTabEntity() {

        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_INDEX_TOP_NAVIGATION)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                String noteJson = JSONUtils.getNoteJson(result, "cate_list");
                mTabEntities = JSONUtils.jsonString2Beans(noteJson, HomeTopTabEntityBean.class);
                //增加热销tab
                HomeTopTabEntityBean hotBlockTabEntity = new HomeTopTabEntityBean();
                hotBlockTabEntity.setCate_id(0);
                hotBlockTabEntity.setCate_name(getString(R.string.sell_well));
                mTabEntities.add(0, hotBlockTabEntity);
                for (int i = 0; i < mTabEntities.size(); i++) {
                    xTablayout.addTab(xTablayout.newTab().setText(mTabEntities.get(i).getCate_name()));
                }

                initFragment();
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
    public void initData() {
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

    @Override
    protected void loadData() {
        getUnreadMessageCount();
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
                                tvLocation.setText(geocodingEntity.getCityName());
                            }

                            @Override
                            public void onGeocodignFailure() {

                            }
                        });
                    } else {
                        tvLocation.setText(address.getLocality());
                    }

                    MegaMartApplication.mPreferenceProvider.put(mContext, Constants.SP_KEY_USED_ADDRESS, tvLocation.getText().toString());

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
                    //登录的状态再去保存
                    if (!StringUtils.isEmpty(MegaMartApplication.mPreferenceProvider.getUId())) {
                        setAddress(postCodeAddressListBean.getZip_code(), postCodeAddressListBean.getArea_name());
                    }


                    Log.e(Constants.WHK_TAG, "locationLoaded: 定位地址 ：" + address.toString());
                } else {
                    String spAddress = (String) MegaMartApplication.mPreferenceProvider.get(mContext, Constants.SP_KEY_USED_ADDRESS, "-");
                    tvLocation.setText(spAddress);
                }


            }
        });

        mLocationHelper.startRequestLocationUpdates();
    }

    //设置首页地址
    private void setAddress(String zip_code, String city) {

        if (StringUtils.isEmpty(zip_code)) {
            zip_code = "0";
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("zip_code", zip_code);
            jsonObject.put("city", city);
            jsonObject.put("address", mDetailsAddress);
            Log.e(Constants.WHK_TAG, "setAddress: userid=" + MegaMartApplication.mPreferenceProvider.getUId() + "address_info =" + jsonObject);
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

    //获取未读消息数量
    private void getUnreadMessageCount() {

        BaseOkHttpClient.newBuilder()
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.GET_UNREAD_NUMBER)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取未读消息数量----" + result);
                        HomeUnreadMessageCountBean unreadMessageCountBean = JSONUtils.jsonString2Bean(result, HomeUnreadMessageCountBean.class);
                        int count = Integer.parseInt(unreadMessageCountBean.getUnread_number());
                        if (count != 0) {
                            ivMessage.showTextBadge(unreadMessageCountBean.getUnread_number());
                        }

                    }

                    @Override
                    public void onError(int code, String msg) {
                        ToastUtils.show(mContext, msg);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.show(mContext, e.getMessage());
                    }
                });


    }


    @OnClick({R.id.tv_location, R.id.iv_message, R.id.edt_goods_search, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
            case R.id.edt_goods_search:
                Intent intent = new Intent(mContext, SearchGoodsRecordActivity.class);
                startActivity(intent);
//                参数1：进入时的动画、参数2：退出时的动画
                mContext.overridePendingTransition(R.anim.anim_search_activity_alpha_out, R.anim.anim_search_activity_alpha_in);

                break;
            case R.id.tv_location:
                startActivityForResult(new Intent(mContext, LocationActivity.class), 100);
                break;
            case R.id.iv_message:
                if (LoginCheckUtils.checkLoginShowDialog(mContext)) return;
                startActivity(new Intent(mContext, MessageListActivity.class));
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            //刷新地址
            String address = (String) MegaMartApplication.mPreferenceProvider.get(mContext, Constants.SP_KEY_USED_ADDRESS, "-");
            tvLocation.setText(address);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationHelper != null) {
            mLocationHelper.stopRequestLocationUpdates();
        }
    }

}
