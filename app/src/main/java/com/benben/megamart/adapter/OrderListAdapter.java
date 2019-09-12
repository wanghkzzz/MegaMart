package com.benben.megamart.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.commoncore.utils.DateUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.OrderBean;
import com.benben.megamart.frag.OrderFragment;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.ui.mine.ApplySaleActivity;
import com.benben.megamart.ui.mine.OrderDetailActivity;
import com.benben.megamart.widget.CustomListView;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/14
 * Time: 10:35
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private Activity mContext;
    private List<OrderBean.OrderListBean> mBean;
    private OrderFragment orderFragment;
    private OrderGoodsListAdapter mAdapter;
    private int mOrderType;
    public OrderListAdapter(Activity mContext, List<OrderBean.OrderListBean> mBean, int mOrderType, OrderFragment orderFragment) {
        this.mContext = mContext;
        this.mBean = mBean;
        this.mOrderType = mOrderType;
        this.orderFragment = orderFragment;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewHolder holder = new ViewHolder(View.inflate(mContext, R.layout.item_order_list, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        for (int j = 0; j < mBean.get(i).getPreferential_list().size(); j++) {
            if (!mBean.get(i).isAdd()) {
                mBean.get(i).getGoods_list().addAll(mBean.get(i).getPreferential_list().get(j).getGoods_list());
                mBean.get(i).setAdd(true);
                //没有添加过，进行添加
            }
        }
        mAdapter = new OrderGoodsListAdapter(mContext, mBean.get(i).getGoods_list());
        holder.lvGoods.setAdapter(mAdapter);

        holder.tvOrderNumber.setText(mContext.getString(R.string.order_number) + mBean.get(i).getOrder_no());
        holder.tvTime.setText(DateUtils.stampToDate(mBean.get(i).getOrder_time()));
        holder.tvTotalPrice.setText("＄" + mBean.get(i).getOrder_amount());
        holder.tvTotalNumber.setText(mBean.get(i).getOrder_goods_count() + "");


        switch (Integer.parseInt(mBean.get(i).getOrder_type())) {
            case 1://待支付
                holder.tvSubmit.setText(mContext.getResources().getString(R.string.view_details));
                holder.tvSubmit.setTextColor(mContext.getResources().getColor(R.color.color_666666));
//                holder.tvState.setText(mContext.getResources().getString(R.string.wait_to_paid));

                holder.tvSubmit.setBackgroundResource(R.drawable.shape_border_white_radius25);
                break;
            case 2://已支付
                holder.tvSubmit.setText(mContext.getResources().getString(R.string.order_apply_callback));
                holder.tvSubmit.setTextColor(mContext.getResources().getColor(R.color.color_EC5413));
//                holder.tvState.setText(mContext.getResources().getString(R.string.mine_order_complete));
                holder.tvSubmit.setBackgroundResource(R.drawable.shape_border_fef3ee_radius25);
                break;
            case 3://申请售后中
                holder.tvSubmit.setText(mContext.getResources().getString(R.string.view_details));
                holder.tvSubmit.setTextColor(mContext.getResources().getColor(R.color.color_666666));
                holder.tvSubmit.setBackgroundResource(R.drawable.shape_border_white_radius25);
//                holder.tvState.setText(mContext.getResources().getString(R.string.apply_saling));
                break;
            case 4://售后完成
                holder.tvSubmit.setText(mContext.getResources().getString(R.string.view_details));
                holder.tvSubmit.setTextColor(mContext.getResources().getColor(R.color.color_666666));
                holder.tvSubmit.setBackgroundResource(R.drawable.shape_border_white_radius25);
//                holder.tvState.setText(mContext.getResources().getString(R.string.apply_sale_completed));
                break;
            case 5://货到付款
                holder.tvSubmit.setBackgroundResource(R.drawable.shape_border_white_radius25);
                holder.tvSubmit.setText(mContext.getResources().getString(R.string.view_details));
                holder.tvSubmit.setTextColor(mContext.getResources().getColor(R.color.color_666666));
               /* if ("1".equals(mBean.get(i).getDelivery_status())) {
                    holder.tvState.setText(mContext.getResources().getString(R.string.wait_to_delivery));
                } else if ("2".equals(mBean.get(i).getDelivery_status())) {
                    holder.tvState.setText(mContext.getResources().getString(R.string.delivering));
                } else if ("3".equals(mBean.get(i).getDelivery_status())) {
                    holder.tvState.setText(mContext.getResources().getString(R.string.delivery_completed));
                }*/
                break;
        }
        holder.tvState.setText(mBean.get(i).getOrder_type_text());
        //商品的点击事件
        holder.lvGoods.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return holder.itemView.onTouchEvent(motionEvent);
            }
        });
        holder.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("2".equals(mBean.get(i).getOrder_type())) {
                    Intent intent = new Intent(mContext, ApplySaleActivity.class);
                    intent.putExtra("orderId", "" + mBean.get(i).getOrder_id());
                    intent.putExtra("bean", (Serializable) mBean.get(i).getGoods_list());
                    orderFragment.startActivityForResult(intent, 101);
                } else {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra("orderId", "" + mBean.get(i).getOrder_id());
                    orderFragment.startActivityForResult(intent,101);
                }

            }
        });

        //跳转订单详情
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra("orderId", "" + mBean.get(i).getOrder_id());
                orderFragment.startActivityForResult(intent,101);
            }
        });
        //删除订单
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View dialog = View.inflate(mContext, R.layout.dialog_shop_cart_confirm_delete, null);
                Button btnCancel = dialog.findViewById(R.id.btn_cancel);
                Button btnConfirm = dialog.findViewById(R.id.btn_confirm);

                AlertDialog mDelAlertDialog = new AlertDialog.Builder(mContext)
                        .setView(dialog)
                        .create();
                mDelAlertDialog.show();
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete("" + mBean.get(i).getOrder_id(), i);
                        mDelAlertDialog.dismiss();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDelAlertDialog.dismiss();
                    }
                });

            }
        });
    }

    private void delete(String orderId, int position) {
        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.DELETE_ORDER)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("order_id", "" + orderId)
                .post()
                .json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                mBean.remove(position);
                notifyDataSetChanged();
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

    @Override
    public int getItemCount() {
        return mBean.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_order_number)
        TextView tvOrderNumber;
        @BindView(R.id.lv_goods)
        CustomListView lvGoods;
        @BindView(R.id.tv_total_price)
        TextView tvTotalPrice;
        @BindView(R.id.tv_total_number)
        TextView tvTotalNumber;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_submit)
        TextView tvSubmit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
