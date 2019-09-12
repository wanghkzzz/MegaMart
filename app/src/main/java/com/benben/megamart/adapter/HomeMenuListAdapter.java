package com.benben.megamart.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.bean.HomeNavigationMenuBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.ui.HomeGoodsListActivity;
import com.benben.megamart.ui.mine.MyInvitationCodeActivity;
import com.benben.megamart.ui.mine.ReceiverCouponActivity;
import com.benben.megamart.utils.LoginCheckUtils;
import com.hyphenate.helper.HyphenateHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-05-30.
 * Describe:首页 菜单列表adapter
 */
public class HomeMenuListAdapter extends AFinalRecyclerViewAdapter<HomeNavigationMenuBean> {


    private Activity mContext;

    public HomeMenuListAdapter(Context ctx) {
        super(ctx);
        this.mContext = (Activity) ctx;
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new HomeMenuListViewHoler(m_Inflater.inflate(R.layout.item_home_menu_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((HomeMenuListViewHoler) holder).setContent(getItem(position), position);
    }

    public class HomeMenuListViewHoler extends BaseRecyclerViewHolder {


        @BindView(R.id.iv_menu_img)
        ImageView ivMenuImg;
        @BindView(R.id.tv_menu_name)
        TextView tvMenuName;

        private View itemView;

        public HomeMenuListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(HomeNavigationMenuBean homeNavigationMenuBean, int position) {

            switch (homeNavigationMenuBean.getCate_id()) {
                case -101:
                case -102:
                case -103:
                case -104:
                case -105:
                    ivMenuImg.setImageResource(Integer.parseInt(homeNavigationMenuBean.getCate_img()));
                    break;
                default:
                    ImageUtils.getCircularPic(homeNavigationMenuBean.getCate_img(), ivMenuImg, m_Context,10, R.drawable.image_placeholder);
                    break;
            }
            //名称
            tvMenuName.setText(homeNavigationMenuBean.getCate_name());
            //item点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (homeNavigationMenuBean.getCate_id()) {
                        case -101://全部
                            mContext.startActivity(new Intent(mContext, HomeGoodsListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra(Constants.EXTRA_KEY_CATE_ID, "").putExtra(Constants.EXTRA_KEY_CATE_TITLE, mContext.getResources().getString(R.string.total_goods)));
                            break;
                        case -102://晒单优惠
                            showUnDevelopDialog();
                            break;
                        case -103://推荐好友
                            if(LoginCheckUtils.checkLoginShowDialog(mContext))return;
                            Intent intent = new Intent(mContext, MyInvitationCodeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(Constants.EXTRA_KEY_MY_QR_CODE, true);
                            mContext.startActivity(intent);
                            break;
                        case -104://联系客服
                            if(LoginCheckUtils.checkLoginShowDialog(mContext))return;
                            HyphenateHelper.callServiceIM(mContext, MegaMartApplication.mPreferenceProvider.getHuanXinName(), MegaMartApplication.mPreferenceProvider.getHuanXinPwd(),MegaMartApplication.mPreferenceProvider.getPhoto());
                            break;
                        case -105://领券中心
                            if(LoginCheckUtils.checkLoginShowDialog(mContext))return;
                            mContext.startActivity(new Intent(mContext, ReceiverCouponActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            break;
                        default:
                            mContext.startActivity(new Intent(mContext, HomeGoodsListActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra(Constants.EXTRA_KEY_CATE_ID, String.valueOf(homeNavigationMenuBean.getCate_id())).putExtra(Constants.EXTRA_KEY_CATE_TITLE, homeNavigationMenuBean.getCate_name()));
                            break;
                    }
                }
            });


        }
    }


    private void showUnDevelopDialog() {
        View view = View.inflate(mContext, R.layout.dialog_function_develop, null);

        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .create();
        Window window = alertDialog.getWindow();
        if (window != null) {
            //去除系统自带的margin
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置dialog在界面中的属性
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        alertDialog.show();

        view.findViewById(R.id.tv_got_it).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        });
    }
}
