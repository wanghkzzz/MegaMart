package com.benben.megamart.frag;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.LazyBaseFragments;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.InvitationFriendAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.InvitationCodeBean;
import com.benben.megamart.bean.MyInvitationListBean;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.ui.mine.MyInvitationCodeActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我邀请的好友
 */
public class InvitationFriendFragment extends LazyBaseFragments {

    @BindView(R.id.rv_invitation)
    RecyclerView rvInvitation;
    @BindView(R.id.btn_invitation)
    Button btnInvitation;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;

    private InvitationFriendAdapter mAdapter;//邀请好友适配器

    private MyInvitationListBean mBean;
    //我的邀请链接
    private String mInvitationUrl;

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_invitation_friend, null);
        return mRootView;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rvInvitation.setLayoutManager(manager);
    }

    @OnClick(R.id.btn_invitation)
    public void onViewClicked() {
        MyInvitationCodeActivity activity = (MyInvitationCodeActivity) getActivity();
        if(activity != null){
            activity.setCurrentItem(1);
        }


    }

    @Override
    protected void loadData() {
        StyledDialogUtils.getInstance().loading(getActivity());
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.MINE_MY_INVITATION_FRIEND_LIST)
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .post()
                .json()
                .build().enqueue(getActivity(), new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                mBean = JSONUtils.jsonString2Bean(result, MyInvitationListBean.class);
                mAdapter = new InvitationFriendAdapter(getActivity(), mBean.getInvitation_list());
                rvInvitation.setAdapter(mAdapter);
                llytNoData.setVisibility(View.GONE);
            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                llytNoData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
                llytNoData.setVisibility(View.VISIBLE);
            }
        });

        //获取我的邀请码
        getInvitationCode();
    }

    //获取我的邀请码
    private void getInvitationCode() {

        BaseOkHttpClient.newBuilder().url(NetUrlUtils.MINE_MY_INVITATION_CODE)
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .post()
                .json()
                .build().enqueue(getActivity(), new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                InvitationCodeBean invitationCodeBean = JSONUtils.jsonString2Bean(result, InvitationCodeBean.class);
                if (invitationCodeBean != null && invitationCodeBean.getInvitation() != null && !StringUtils.isEmpty(invitationCodeBean.getInvitation().getInvit_url())) {
                    mInvitationUrl = invitationCodeBean.getInvitation().getInvit_url();
                }
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


}
