package com.benben.megamart.ui.mine;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.R;
import com.benben.megamart.adapter.InvitationPagerAdapter;
import com.benben.megamart.config.Constants;
import com.benben.megamart.frag.InvitationCodeFragment;
import com.benben.megamart.frag.InvitationFriendFragment;
import com.benben.megamart.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的邀请
 */
public class MyInvitationCodeActivity extends BaseActivity {
    @BindView(R.id.tl_invitation)
    XTabLayout tlInvitation;
    @BindView(R.id.vp_invitation)
    ViewPager vpInvitation;

    private String mTabNames[] = new String[2];
    private List<Fragment> mFragments = new ArrayList<>();

    private InvitationPagerAdapter mAdapter;

    private InvitationFriendFragment mFriendFragment;//我邀请的好友
    private InvitationCodeFragment mCodeFragment;//我的邀请码

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_invitation_code;
    }

    @Override
    protected void initData() {
        initTitle(getString(R.string.invitation_my));
        //是否显示我的二维码
        boolean isShowMyCode = getIntent().getBooleanExtra(Constants.EXTRA_KEY_MY_QR_CODE, false);
        mTabNames[0] = getResources().getString(R.string.invitation_my_friend);
        mTabNames[1] = getResources().getString(R.string.invitation_my_code);

        mFriendFragment = new InvitationFriendFragment();
        mCodeFragment = new InvitationCodeFragment();

        mFragments.add(mFriendFragment);
        mFragments.add(mCodeFragment);

        mAdapter = new InvitationPagerAdapter(getSupportFragmentManager(), mTabNames, mFragments);
        vpInvitation.setAdapter(mAdapter);
        tlInvitation.setupWithViewPager(vpInvitation);
        tlInvitation.setSelectedTabIndicatorColor(getResources().getColor(R.color.theme));

        if(isShowMyCode){
            vpInvitation.setCurrentItem(1);

        }
    }


    public void  setCurrentItem(int position){
        vpInvitation.setCurrentItem(position);
    }
    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }
}
