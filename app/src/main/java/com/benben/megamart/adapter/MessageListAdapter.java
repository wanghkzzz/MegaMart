package com.benben.megamart.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.widget.badgeview.BGABadgeRelativeLayout;
import com.benben.commoncore.widget.badgeview.BGABadgeViewHelper;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.bean.MessageListBean;
import com.benben.megamart.ui.MessageNoticListActivity;
import com.hyphenate.helper.HyphenateHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-05-30.
 * Describe:消息列表adapter
 */
public class MessageListAdapter extends AFinalRecyclerViewAdapter<MessageListBean> {


    private Activity mActivity;

    public MessageListAdapter(Context ctx) {
        super(ctx);
        mActivity = (Activity) ctx;
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new MessageListViewHoler(m_Inflater.inflate(R.layout.item_message_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((MessageListViewHoler) holder).setContent(getItem(position), position);
    }

    public class MessageListViewHoler extends BaseRecyclerViewHolder {

        @BindView(R.id.iv_message_avatar)
        ImageView ivMessageAvatar;
        @BindView(R.id.tv_message_title)
        TextView tvMessageTitle;
        @BindView(R.id.tv_message_content)
        TextView tvMessageContent;
        @BindView(R.id.tv_message_time)
        TextView tvMessageTime;
        @BindView(R.id.view_divider)
        View viewDivider;
        @BindView(R.id.rlyt_content)
        BGABadgeRelativeLayout rlytContent;
        private View itemView;

        public MessageListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(MessageListBean messageInfoBean, int position) {
/*
            if (position == (getList().size() - 1)) {
                viewDivider.setVisibility(View.GONE);
            }*/
            tvMessageTime.setText(messageInfoBean.getArticle_time());
            rlytContent.getBadgeViewHelper().setBadgeGravity(BGABadgeViewHelper.BadgeGravity.RightBottom);

            if (messageInfoBean.getUnreadMessageCount() != 0) {
                rlytContent.showTextBadge(String.valueOf(messageInfoBean.getUnreadMessageCount()));
            }

            tvMessageTitle.setText(messageInfoBean.getArticle_title());
            tvMessageContent.setText(messageInfoBean.getArticle_description());

            ivMessageAvatar.setImageResource(Integer.parseInt(messageInfoBean.getArticle_img()));
          //  ImageUtils.getPic(messageInfoBean.getArticle_img(), ivMessageAvatar, m_Context, R.drawable.image_placeholder);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == (getList().size() - 1)) {
                        HyphenateHelper.callServiceIM(mActivity, MegaMartApplication.mPreferenceProvider.getHuanXinName(), MegaMartApplication.mPreferenceProvider.getHuanXinPwd(), MegaMartApplication.mPreferenceProvider.getPhoto());
                    } else {
                        Intent intent = new Intent(m_Context, MessageNoticListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        m_Context.startActivity(intent);
                    }

                }
            });


        }
    }
}
