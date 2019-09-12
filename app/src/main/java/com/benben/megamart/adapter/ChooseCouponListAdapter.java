package com.benben.megamart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.megamart.R;
import com.benben.megamart.bean.CouponListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-06-26.
 * Describe:订单页面选择优惠券列表adapter
 */
public class ChooseCouponListAdapter extends AFinalRecyclerViewAdapter<CouponListBean> {


    public ChooseCouponListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new ChooseCouponListViewHoler(m_Inflater.inflate(R.layout.item_choose_coupon_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((ChooseCouponListViewHoler) holder).setContent(getItem(position), position);
    }

    public class ChooseCouponListViewHoler extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_coupon_name)
        TextView tvCouponName;
        View itemView;

        public ChooseCouponListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(CouponListBean couponListBean, int position) {

            tvCouponName.setText(couponListBean.getCoupon_name()+ m_Context.getResources().getString(R.string.blank_space) + couponListBean.getCoupons_price());
            tvCouponName.setSelected(couponListBean.isSelect());


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectCoupon(position);
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position, couponListBean);
                    }
                }
            });

        }
    }


    //单选
    private void selectCoupon(int position) {
        if(getItem(position).isSelect()){
            getItem(position).setSelect(false);
            return;
        }
        for (int i = 0; i < getList().size(); i++) {
            if (i == position) {
                getList().get(i).setSelect(true);
            } else {
                getList().get(i).setSelect(false);
            }
        }
        notifyDataSetChanged();
    }
}
