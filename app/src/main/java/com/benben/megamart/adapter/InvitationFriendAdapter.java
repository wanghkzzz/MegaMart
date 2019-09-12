package com.benben.megamart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.DateUtils;
import com.benben.commoncore.utils.ImageUtils;
import com.benben.megamart.R;
import com.benben.megamart.bean.MyInvitationListBean;

import java.util.List;

public class InvitationFriendAdapter extends RecyclerView.Adapter<InvitationFriendAdapter.ViewHolder> {


    private Context mContext;
    private List<MyInvitationListBean.InvitationListBean> mBean;

    public InvitationFriendAdapter(Context mContext, List<MyInvitationListBean.InvitationListBean> mBean) {
        this.mContext = mContext;
        this.mBean = mBean;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewHolder holder = new ViewHolder(View.inflate(mContext, R.layout.item_invitation_friend, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.tvName.setText("" + mBean.get(i).getUser_name());
        holder.tvTime.setText(DateUtils.stampToDate(mBean.get(i).getInvit_time()));
        if ("1".equals(mBean.get(i).getUser_type())) {
            holder.tvState.setText("" + mContext.getString(R.string.invitation_complete));
            holder.tvState.setTextColor(mContext.getResources().getColor(R.color.theme));
        } else {
            holder.tvState.setText("" + mContext.getString(R.string.invitation_no_complete));
            holder.tvState.setTextColor(mContext.getResources().getColor(R.color.color_333333));
        }
        ImageUtils.getPic(mBean.get(i).getUser_avatar(), holder.ivImg
                , mContext, R.drawable.image_placeholder);
    }

    @Override
    public int getItemCount() {
        return mBean.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        TextView tvState;
        TextView tvName;
        TextView tvTime;

        public ViewHolder(@NonNull View view) {
            super(view);
            ivImg = (ImageView) view.findViewById(R.id.iv_img);
            tvState = (TextView) view.findViewById(R.id.tv_state);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
        }
    }
}
