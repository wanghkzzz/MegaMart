package com.benben.megamart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.megamart.R;
import com.benben.megamart.bean.CartGoodsListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-06-05.
 * Describe:提交订单商品列表adapter
 */
public class SubmitOrderGoodsListAdapter extends AFinalRecyclerViewAdapter<CartGoodsListBean> {

    public SubmitOrderGoodsListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new SubmitOrderGoodsListViewHoler(m_Inflater.inflate(R.layout.item_submit_order_goods_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((SubmitOrderGoodsListViewHoler) holder).setContent(getItem(position), position);
    }

    public class SubmitOrderGoodsListViewHoler extends BaseRecyclerViewHolder {

        @BindView(R.id.iv_goods_image)
        ImageView ivGoodsImage;
        @BindView(R.id.tv_goods_name)
        TextView tvGoodsName;
        @BindView(R.id.tv_goods_old_price)
        TextView tvGoodsOldPrice;
        @BindView(R.id.tv_goods_price)
        TextView tvGoodsPrice;
        @BindView(R.id.tv_goods_number)
        TextView tvGoodsNumber;
        @BindView(R.id.view_divider)
        View viewDivider;

        public SubmitOrderGoodsListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void setContent(CartGoodsListBean cartGoodsListBean, int position) {

            //如果为最后一个item 不绘制分割线
            if (position == (getList().size() - 1)) {
                viewDivider.setVisibility(View.GONE);
            } else {
                viewDivider.setVisibility(View.VISIBLE);
            }


            //商品图片
            ImageUtils.getCircularPic(cartGoodsListBean.getGoods_img(), ivGoodsImage, m_Context, 10,R.drawable.image_placeholder);
            //商品名字
            tvGoodsName.setText(cartGoodsListBean.getGoods_name());
            //商品现在的价格
            tvGoodsPrice.setText(m_Context.getString(R.string.goods_price, String.valueOf(cartGoodsListBean.getGoods_amount())));
            //商品原价
          //  tvGoodsOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

            tvGoodsNumber.setText("x"+cartGoodsListBean.getGoods_number());
        }
    }
}
