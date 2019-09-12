package com.benben.megamart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.DateUtils;
import com.benben.commoncore.utils.ImageUtils;
import com.benben.megamart.R;
import com.benben.megamart.bean.MessageNoticListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.ui.CommonWebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-05-30.
 * Describe:消息列表adapter
 */
public class MessageNoticListAdapter extends AFinalRecyclerViewAdapter<MessageNoticListBean> {

    public MessageNoticListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new MessageListViewHoler(m_Inflater.inflate(R.layout.item_message_notic_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((MessageListViewHoler) holder).setContent(getItem(position), position);
    }

    public class MessageListViewHoler extends BaseRecyclerViewHolder {


        @BindView(R.id.iv_notic_thumb)
        ImageView ivNoticThumb;
        @BindView(R.id.tv_article_title)
        TextView tvArticleTitle;
        @BindView(R.id.tv_article_time)
        TextView tvArticleTime;

        private View itemView;

        public MessageListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(MessageNoticListBean messageNoticListBean, int position) {
            //图片
            ImageUtils.getCircularPic(messageNoticListBean.getArticle_img(), ivNoticThumb, m_Context,10, R.drawable.image_placeholder);
            //文章标题
            tvArticleTitle.setText(messageNoticListBean.getArticle_title());
            //时间
            tvArticleTime.setText(DateUtils.stampToDate(String.valueOf(messageNoticListBean.getArticle_time())));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(m_Context, CommonWebViewActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_URL, messageNoticListBean.getArticle_description());
                    intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_TITLE, messageNoticListBean.getArticle_title());
                    intent.putExtra(Constants.EXTRA_KEY_IS_URL, false);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    m_Context.startActivity(intent);
                }
            });


        }
    }
}
