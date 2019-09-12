package com.benben.megamart.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.InvitationPagerAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.AddressManagerBean;
import com.benben.megamart.frag.AddressFragment;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.RxBus;
import com.benben.megamart.utils.StatusBarUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/14
 * Time: 11:49
 * 地址管理
 */
public class AddressManagerActivity extends BaseActivity {
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.tl_address)
    XTabLayout tlAddress;
    @BindView(R.id.vp_address)
    ViewPager vpAddress;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;
    @BindView(R.id.center_title)
    TextView centerTitle;

    private List<Fragment> mFragments = new ArrayList<>();

    private InvitationPagerAdapter mAdapter;//tabLayout和viewPager联用的适配器

    private AddressFragment mFragment;

    private AddressManagerBean mBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_manager;
    }

    @Override
    protected void initData() {
        centerTitle.setText(getString(R.string.address_manager));
        rightTitle.setText("" + getString(R.string.address_add));
        getData();

        RxBus.getInstance().toObservable(Integer.class)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                        //编辑完收货地址/添加收货地址/修改默认地址  去刷新当前页面
                        if (integer == 1001 || integer == 1002) {
                            getData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getData() {
        //  StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.ADDRESS_MANAGER_LIST)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                mBean = JSONUtils.jsonString2Bean(result, AddressManagerBean.class);
                if (mBean == null || mBean.getAddress_list() == null || mBean.getAddress_list().size() <= 0) {
                    tlAddress.setVisibility(View.GONE);
                    llytNoData.setVisibility(View.VISIBLE);
                    return;
                } else {
                    tlAddress.setVisibility(View.VISIBLE);
                    llytNoData.setVisibility(View.GONE);
                }
                String[] mTabNames = new String[mBean.getAddress_list().size()];
                mFragments.clear();
                for (int i = 0; i < mBean.getAddress_list().size(); i++) {
                    mTabNames[i] = mBean.getAddress_list().get(i).getLink_man();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean", mBean.getAddress_list().get(i));
                    mFragment = new AddressFragment();
                    mFragment.setArguments(bundle);
                    mFragments.add(mFragment);
                }
                mAdapter = new InvitationPagerAdapter(getSupportFragmentManager(), mTabNames, mFragments);
                vpAddress.setAdapter(mAdapter);
                tlAddress.setupWithViewPager(vpAddress);

            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(msg);
                tlAddress.setVisibility(View.GONE);
                llytNoData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                tlAddress.setVisibility(View.GONE);
                llytNoData.setVisibility(View.VISIBLE);
                StyledDialogUtils.getInstance().dismissLoading();
                toast(getResources().getString(R.string.server_exception));
            }
        });
    }


    @OnClick({R.id.right_title, R.id.rl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_title:
                startActivity(new Intent(this, AddAddressActivity.class));
                break;
            case R.id.rl_back:
                setResult(201);
                super.onBackPressed();
                break;
        }
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }

    @Override
    public void onBackPressed() {
        setResult(201);
        super.onBackPressed();
    }
}
