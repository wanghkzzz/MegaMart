package com.benben.megamart.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.commoncore.utils.DateUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.ReceiveCouponListBean;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class ReceiverCouponAdapter extends RecyclerView.Adapter<ReceiverCouponAdapter.ViewHolder> {

    private Activity mContext;
    private List<ReceiveCouponListBean> mBeans;

    public ReceiverCouponAdapter(Activity mContext, List<ReceiveCouponListBean> mBeans) {
        this.mContext = mContext;
        this.mBeans = mBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewHolder holder = new ViewHolder(View.inflate(mContext, R.layout.item_receiver_coupon, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.tvMoney.setText(mBeans.get(i).getCoupons_price());
        holder.tvTitle.setText(mBeans.get(i).getCoupons_name());
        holder.tvTime.setText(mContext.getResources().getString(R.string.valid_until, DateUtils.stampToDate(mBeans.get(i).getCoupon_validity())));
        holder.tvRule.setText(mContext.getResources().getString(R.string.full_less, mBeans.get(i).getLimit_price(), mBeans.get(i).getCoupons_price()));

        if ("1".equals(mBeans.get(i).getUser_coupons())) {
            holder.rlytCouponBg.setBackgroundResource(R.mipmap.img_has_receive_coupon_bg);
            holder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.color_999999));
            holder.tvUnit.setTextColor(mContext.getResources().getColor(R.color.color_999999));
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.color_666666));
            holder.tvGoUse.setText(mContext.getResources().getString(R.string.received));
        } else {
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            holder.rlytCouponBg.setBackgroundResource(R.mipmap.coupon_item_bg);
            holder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.theme));
            holder.tvUnit.setTextColor(mContext.getResources().getColor(R.color.theme));
            holder.tvGoUse.setText(mContext.getResources().getString(R.string.get_it_right_now));


        }
        holder.tvGoUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("1".equals(mBeans.get(i).getUser_coupons())) return;
                StyledDialogUtils.getInstance().loading(mContext);
                BaseOkHttpClient.newBuilder().url(NetUrlUtils.RECEIVER_DISCOUNT)
                        .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                        .addParam("coupon_id", "" + mBeans.get(i).getCoupon_id())
                        .post()
                        .json()
                        .build().enqueue(mContext, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        StyledDialogUtils.getInstance().dismissLoading();
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        mContext.setResult(-1);
                        mContext.finish();
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
        });
    }

    @Override
    public int getItemCount() {
        return mBeans == null ? 0 : mBeans.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_go_use)
        TextView tvGoUse;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_unit)
        TextView tvUnit;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_rule)
        TextView tvRule;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.rlyt_coupon_bg)
        RelativeLayout rlytCouponBg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}
