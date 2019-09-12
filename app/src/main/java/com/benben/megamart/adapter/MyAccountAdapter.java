package com.benben.megamart.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.commoncore.utils.DateUtils;
import com.benben.megamart.R;
import com.benben.megamart.bean.AccountListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAccountAdapter extends AFinalRecyclerViewAdapter<AccountListBean.UserBalanceBean> {



    private Activity mActivity;

    public MyAccountAdapter(Context ctx) {
        super(ctx);
        mActivity = (Activity) ctx;
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new MyAccountListViewHoler(m_Inflater.inflate(R.layout.item_my_account, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((MyAccountListViewHoler) holder).setContent(getItem(position), position);
    }

    public class MyAccountListViewHoler extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public MyAccountListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void setContent(AccountListBean.UserBalanceBean mUserBalanceBean, int position) {

            if("1".equals(mUserBalanceBean.getType())){
                tvTitle.setText(m_Context.getResources().getString(R.string.recharge));
                tvMoney.setText("-＄" + mUserBalanceBean.getChange_money());
            }else if("2".equals(mUserBalanceBean.getType())){
                tvTitle.setText(m_Context.getResources().getString(R.string.consumption));
                tvMoney.setText("-＄" + mUserBalanceBean.getChange_money());

            }else {
                tvTitle.setText(m_Context.getResources().getString(R.string.refunds));
                tvMoney.setText("+＄" + mUserBalanceBean.getChange_money());
            }
//            tvTitle.setText(mUserBalanceBean.getBal_title());
            tvTime.setText(DateUtils.stampToDate(String.valueOf(mUserBalanceBean.getChange_time())));



        }
    }

}
