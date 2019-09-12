package com.benben.megamart.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.megamart.R;
import com.benben.megamart.bean.DiscountListBean;
import com.benben.megamart.utils.RxBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {

    private static final String TAG ="CouponAdapter" ;
    private Context mContext;
    private Activity mActivity;
    private String mType;
    private List<DiscountListBean.CouponListBean> mBeans;

    public CouponAdapter(Context mContext, List<DiscountListBean.CouponListBean> mBeans, String mCouponType) {
        this.mContext = mContext;
        this.mActivity = (Activity) mContext;
        this.mBeans = mBeans;
        this.mType = mCouponType;
        Log.e(TAG, "CouponAdapter:mType= "+mType );
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewHolder holder = new ViewHolder(View.inflate(mContext, R.layout.item_coupon, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.tvMoney.setText(mBeans.get(i).getCoupons_price().replace(".00",""));
        holder.tvTitle.setText(mContext.getResources().getString(R.string.full_coupon));
        holder.tvTime.setText(mContext.getResources().getString(R.string.valid_until, mBeans.get(i).getValidity()));
        holder.tvRule.setText(mContext.getResources().getString(R.string.full_less,mBeans.get(i).getLimit_price().replace(".00",""),mBeans.get(i).getCoupons_price().replace(".00","")));
        if("0".equals(mType)){//未使用
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            holder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.theme));
            holder.tvUnit.setTextColor(mContext.getResources().getColor(R.color.theme));
            holder.tvGoUse.setBackgroundResource(R.mipmap.bg_djqy);
            holder.tvGoUse.setEnabled(true);
            holder.tvGoUse.setText(mContext.getResources().getString(R.string.use_immediately));

        }else if("1".equals(mType)){//已使用
            holder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.color_999999));
            holder.tvUnit.setTextColor(mContext.getResources().getColor(R.color.color_999999));
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.color_666666));
            holder.tvGoUse.setBackgroundResource(R.mipmap.img_invalid_coupon);
            holder.tvGoUse.setEnabled(false);
            holder.tvGoUse.setText(mContext.getResources().getString(R.string.coupon_used));
        }else {//已失效
            holder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.color_999999));
            holder.tvUnit.setTextColor(mContext.getResources().getColor(R.color.color_999999));
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.color_666666));
            holder.tvGoUse.setBackgroundResource(R.mipmap.img_invalid_coupon);
            holder.tvGoUse.setEnabled(false);
            holder.tvGoUse.setText(mContext.getResources().getString(R.string.coupon_cash));

        }
        holder.tvGoUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RxBus.getInstance().post(0);
                mActivity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBeans.size();
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}
