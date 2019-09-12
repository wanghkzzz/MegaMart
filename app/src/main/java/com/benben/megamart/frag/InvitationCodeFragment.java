package com.benben.megamart.frag;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.LazyBaseFragments;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.InvitationCodeBean;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.DownloadSaveImg;
import com.benben.megamart.utils.ShareUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的邀请码界面
 */
public class InvitationCodeFragment extends LazyBaseFragments {

    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
    @BindView(R.id.tv_my_code)
    TextView tvMyCode;
    @BindView(R.id.tv_copy)
    TextView tvCopy;
    @BindView(R.id.btn_download)
    Button btnDownload;
    @BindView(R.id.btn_share)
    Button btnShare;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.edt_code)
    EditText edtCode;

    private InvitationCodeBean mBean;

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_invitation_code, null);
        return mRootView;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void loadData() {
        StyledDialogUtils.getInstance().loading(getActivity());
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.MINE_MY_INVITATION_CODE)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .post()
                .json()
                .build().enqueue(getActivity(), new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                mBean = JSONUtils.jsonString2Bean(result, InvitationCodeBean.class);
                ImageUtils.getPic(mBean.getInvitation().getInvit_img(), ivQrCode
                        , getActivity(), R.drawable.image_placeholder);
                tvMyCode.setText("" + getString(R.string.invitation_my_exclusive_code) + mBean.getInvitation().getInvit_code());
                if (mBean.getInvitation().getBind_code() == null || "".equals(mBean.getInvitation().getBind_code())) {
                    tvConfirm.setEnabled(true);
                } else {
                    edtCode.setText("" + mBean.getInvitation().getBind_code());
                    tvConfirm.setEnabled(false);
                    edtCode.setFocusable(false);
                }
            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
            }
        });
    }

    @OnClick({R.id.tv_copy, R.id.btn_download, R.id.btn_share, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_copy:
                if (mBean == null || StringUtils.isEmpty(mBean.getInvitation().getInvit_code())) {
                    return;
                }
                Toast.makeText(mContext, mContext.getResources().getString(R.string.successful_copy), Toast.LENGTH_SHORT).show();
                copy(mBean.getInvitation().getInvit_code());
                break;
            case R.id.btn_download://保存图片
                if (mBean == null || StringUtils.isEmpty(mBean.getInvitation().getInvit_img())) {
                    return;
                }
                DownloadSaveImg.donwloadImg(getActivity(), mBean.getInvitation().getInvit_img());
                break;
            case R.id.btn_share:
                if (mBean == null) {
                    ToastUtils.show(mContext,mContext.getResources().getString(R.string.server_exception));
                    return;
                }
                ShareUtils shareUtils = new ShareUtils(getActivity(), "http://megamart.brd-techi.com/mobile/registered?invit_code="+mBean.getInvitation().getInvit_code(), getResources().getString(R.string.friend_share_surprise_for_you), getResources().getString(R.string.invite_friends));
                shareUtils.share();
                break;
            case R.id.tv_confirm:
                Invited();
                break;
        }
    }

    /**
     * 被谁邀请
     */
    private void Invited() {
        String code = edtCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(mContext, getResources().getString(R.string.input_invitation_code), Toast.LENGTH_SHORT).show();
            return;
        }
        StyledDialogUtils.getInstance().loading(getActivity());
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.MINE_MY_INVITED)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("invit_code", "" + code)
                .post()
                .json()
                .build().enqueue(getActivity(), new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                Toast.makeText(mContext, getResources().getString(R.string.invitation_success), Toast.LENGTH_SHORT).show();
                tvConfirm.setEnabled(false);
                edtCode.setFocusable(false);
            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
                Toast.makeText(getActivity(), getResources().getString(R.string.server_exception), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 复制内容到剪切板
     *
     * @param copyStr
     * @return
     */
    private boolean copy(String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
