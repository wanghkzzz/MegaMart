package com.benben.megamart.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.megamart.R;
import com.benben.megamart.bean.PayWayBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-05-30.
 * Describe:支付方式列表adapter
 */
public class PaymentWayListAdapter extends AFinalRecyclerViewAdapter<PayWayBean.PaywayListBean> {

    private String isBuy = "1";
    private String prompt = "";
    private Activity mActivity;

    public PaymentWayListAdapter(Context ctx) {
        super(ctx);
        mActivity = (Activity) ctx;
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new PaymentWayListViewHoler(m_Inflater.inflate(R.layout.item_payment_way_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((PaymentWayListViewHoler) holder).setContent(getItem(position), position);
    }

    public void setIsBuy(String isBuy) {
        this.isBuy = isBuy;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public class PaymentWayListViewHoler extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_pay_way)
        TextView tvPayWay;
        @BindView(R.id.iv_pay_way)
        ImageView ivPayWay;
        View itemView;

        public PaymentWayListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(PayWayBean.PaywayListBean paymentWayListBean, int position) {

            if ("1".equals(paymentWayListBean.getPay_id())) {
                //余额支付
                tvPayWay.setText(paymentWayListBean.getPay_name() + "(" + paymentWayListBean.getBalance() + ")");
                SpannableStringBuilder builder = new SpannableStringBuilder(tvPayWay.getText().toString());
                //设置颜色
                ForegroundColorSpan blue = new ForegroundColorSpan(Color.parseColor("#F67F4C"));
                //改变money字体颜色
                builder.setSpan(blue, paymentWayListBean.getPay_name().length(), (tvPayWay.getText().toString().length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //改变余额支付后面的字体大小
                Parcel p = Parcel.obtain();
                p.writeInt(14);//字体大小
                p.writeInt(8);//是否是dip单位
                p.setDataPosition(0);
                builder.setSpan(new AbsoluteSizeSpan(p), paymentWayListBean.getPay_name().length(), (tvPayWay.getText().toString().length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                tvPayWay.setText(builder);
            } else {
                tvPayWay.setText(paymentWayListBean.getPay_name());
            }

            tvPayWay.setSelected(paymentWayListBean.isSelect());

            ImageUtils.getPic(paymentWayListBean.getPay_logo(), ivPayWay, m_Context);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("0".equals(isBuy) && "5".equals(paymentWayListBean.getPay_id())) {
                        showTipsDialog();
                        return;
                    }
                    selectPayWay(position);
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position, paymentWayListBean);
                    }
                }
            });

        }
    }

    //首单不可使用货到付款弹窗
    private void showTipsDialog() {
        View dialog = View.inflate(mActivity, R.layout.dialog_cant_use_hdfk, null);
        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm);
        tvTitle.setText(prompt);
        AlertDialog mDelAlertDialog = new AlertDialog.Builder(mActivity)
                .setView(dialog)
                .create();
        mDelAlertDialog.show();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDelAlertDialog.dismiss();
            }
        });
    }


    //单选
    private void selectPayWay(int position) {
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
