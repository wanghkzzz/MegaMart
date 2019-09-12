package com.benben.megamart.frag;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.commoncore.widget.badgeview.BGABadgeImageView;
import com.benben.commoncore.widget.badgeview.BGABadgeViewHelper;
import com.benben.megamart.LazyBaseFragments;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.UserInfoBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.ui.CommonWebViewActivity;
import com.benben.megamart.ui.LoginActivity;
import com.benben.megamart.ui.MessageListActivity;
import com.benben.megamart.ui.mine.FeedbackActivity;
import com.benben.megamart.ui.mine.MyCollectionActivity;
import com.benben.megamart.ui.mine.MyCouponActivity;
import com.benben.megamart.ui.mine.MyInvitationCodeActivity;
import com.benben.megamart.ui.mine.MyWalletActivity;
import com.benben.megamart.ui.mine.OrderActivity;
import com.benben.megamart.ui.mine.PersonDataActivity;
import com.benben.megamart.ui.mine.SettingActivity;
import com.benben.megamart.utils.LoginCheckUtils;
import com.benben.megamart.utils.RxBus;
import com.benben.megamart.utils.StatusBarUtils;
import com.hss01248.dialog.interfaces.MyDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

/**
 * Create by wanghk on 2019-05-27.
 * Describe:个人中心
 */
public class MainMineFragment extends LazyBaseFragments {

    @BindView(R.id.iv_message)
    BGABadgeImageView ivMessage;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_wallet)
    TextView tvWallet;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.tv_invitation)
    TextView tvInvitation;
    @BindView(R.id.tv_order_all)
    TextView tvOrderAll;
    @BindView(R.id.tv_order_delivery)
    TextView tvOrderDelivery;
    @BindView(R.id.tv_order_paid)
    TextView tvOrderPaid;
    @BindView(R.id.tv_order_refund)
    TextView tvOrderRefund;
    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.tv_feedback)
    TextView tvFeedback;
    @BindView(R.id.tv_connection)
    TextView tvConnection;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.llyt_mine_top)
    LinearLayout llytMineTop;

    private UserInfoBean mBean;

    public static MainMineFragment getInstance() {
        MainMineFragment sf = new MainMineFragment();
        return sf;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_main_mine, null);
        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isPrepared()) {
            StatusBarUtils.setStatusBarColor(getActivity(), R.color.transparent);
            llytMineTop.setPadding(0, StatusBarUtils.getStatusBarHeight(getActivity()), 0, 0);
        }
    }

    @Override
    public void initView() {
        StatusBarUtils.setStatusBarColor(getActivity(), R.color.color_EC5413);
        llytMineTop.setPadding(0, StatusBarUtils.getStatusBarHeight(getActivity()), 0, 0);

        ivMessage.getBadgeViewHelper().setBadgeGravity(BGABadgeViewHelper.BadgeGravity.RightTop);

        RxBus.getInstance().toObservable(Boolean.class)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        //表示登录成功，刷新ui
                        if (aBoolean) {
                            loadData();
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


    @Override
    public void initData() {

    }


    @Override
    protected void loadData() {
        StatusBarUtils.setStatusBarColor(getActivity(), R.color.transparent);
        llytMineTop.setPadding(0, StatusBarUtils.getStatusBarHeight(getActivity()), 0, 0);

        // StyledDialogUtils.getInstance().loading(getActivity());
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.MINE_USER_INFO)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .post()
                .json()
                .build().enqueue(getActivity(), new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                mBean = JSONUtils.jsonString2Bean(result, UserInfoBean.class);
                tvName.setText("" + mBean.getUserinfo().get(0).getUser_name());
                ImageUtils.getPic(mBean.getUserinfo().get(0).getUser_avatar(), ivHeader
                        , getActivity(), R.mipmap.mine_no_login_header);
                if (!"0".equals(mBean.getUnread_number())) {
                    ivMessage.showTextBadge(mBean.getUnread_number());
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

    @OnClick({R.id.iv_message, R.id.iv_setting
            , R.id.tv_wallet, R.id.tv_coupon, R.id.tv_invitation
            , R.id.tv_order_all, R.id.tv_order_delivery, R.id.tv_order_paid
            , R.id.tv_order_refund, R.id.tv_collection, R.id.tv_feedback
            , R.id.tv_connection, R.id.tv_about, R.id.tv_whole_order, R.id.rlyt_user_info})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            //消息
            case R.id.iv_message:
                if (checkLogin()) return;
                startActivity(new Intent(getActivity(), MessageListActivity.class));
                break;
            //设置
            case R.id.iv_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            //头像
            case R.id.rlyt_user_info:
            case R.id.iv_header:
            case R.id.tv_name:
                //名字
                if (checkLogin()) return;
                startActivityForResult(new Intent(getActivity(), PersonDataActivity.class), 101);
                break;
            //我的钱包
            case R.id.tv_wallet:
                if (checkLogin()) return;
                startActivity(new Intent(getActivity(), MyWalletActivity.class));

                break;
            //我的优惠券
            case R.id.tv_coupon:
                if (checkLogin()) return;
                startActivity(new Intent(getActivity(), MyCouponActivity.class));
                break;
            //我的邀请
            case R.id.tv_invitation:
                if (checkLogin()) return;
                startActivity(new Intent(getActivity(), MyInvitationCodeActivity.class));

                break;
            //全部订单
            case R.id.tv_whole_order:
            case R.id.tv_order_all:
                if (checkLogin()) return;
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("index", 0);
                startActivity(intent);
                break;
            //货到付款
            case R.id.tv_order_delivery:
                if (checkLogin()) return;
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("index", 1);
                startActivity(intent);

                break;
            //已付款
            case R.id.tv_order_paid:
                if (checkLogin()) return;
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("index", 2);
                startActivity(intent);
                break;
            //退款/售后
            case R.id.tv_order_refund:
                if (checkLogin()) return;
                intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("index", 3);
                startActivity(intent);
                break;
            //我的收藏
            case R.id.tv_collection:
                if (checkLogin()) return;
                startActivity(new Intent(getActivity(), MyCollectionActivity.class));
                break;
            //意见反馈
            case R.id.tv_feedback:
                if (checkLogin()) return;
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            //联系我们
            case R.id.tv_connection:
                connectionMine();
                break;
            //关于我们
            case R.id.tv_about:
                getAbout();
                break;
        }
    }

    //检查是否登录
    private boolean checkLogin() {
        if (!LoginCheckUtils.checkUserIsLogin(mContext)) {
            LoginCheckUtils.showYanZhengDialog(mContext, new MyDialogListener() {
                @Override
                public void onFirst() {
                    startActivityForResult(new Intent(mContext, LoginActivity.class), 101);
                }

                @Override
                public void onSecond() {

                }
            });
            return true;

        }
        return false;
    }

    /**
     * 关于我们
     */
    private void getAbout() {
        StyledDialogUtils.getInstance().loading(getActivity());
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.ABOUT)
                .addParam("client_type", "" + 1)
                .post()
                .json()
                .build().enqueue(getActivity(), new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                String mega_info = "";
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.has("mega_info") && !object.isNull("mega_info")) {
                        mega_info = object.getString("mega_info");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getActivity(), CommonWebViewActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_TITLE, "" + getActivity().getResources().getString(R.string.about));
                intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_URL, "" + mega_info);
                intent.putExtra(Constants.EXTRA_KEY_IS_URL, false);
                startActivity(intent);
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

    /**
     * 联系我们
     */
    private void connectionMine() {
        StyledDialogUtils.getInstance().loading(getActivity());
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.CONNECTION_MINE)
                .addParam("client_type", "" + 1)
                .post()
                .json()
                .build().enqueue(getActivity(), new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                String contact_info = "";
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.has("contact_info") && !object.isNull("contact_info")) {
                        contact_info = object.getString("contact_info");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getActivity(), CommonWebViewActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_TITLE, "" + getActivity().getResources().getString(R.string.connection_mine));
                intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_URL, "" + contact_info);
                intent.putExtra(Constants.EXTRA_KEY_IS_URL, false);
                startActivity(intent);
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
