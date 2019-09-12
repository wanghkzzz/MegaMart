package com.benben.megamart.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.bean.ComposeDiscountListBean;
import com.benben.megamart.ui.GoodsDetailsActivity;
import com.benben.megamart.utils.LoginCheckUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-06-05.
 * Describe:组合优惠列表adapter
 */
public class ComposeDiscountListAdapter extends AFinalRecyclerViewAdapter<ComposeDiscountListBean> {

    private Activity mActivity;

    public ComposeDiscountListAdapter(Context ctx) {
        super(ctx);
        mActivity = (Activity) ctx;
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new ComposeDiscountListViewHoler(m_Inflater.inflate(R.layout.item_compose_discount_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((ComposeDiscountListViewHoler) holder).setContent(getItem(position), position);
    }

    public class ComposeDiscountListViewHoler extends BaseRecyclerViewHolder {

        @BindView(R.id.iv_first_goods_img)
        ImageView ivFirstGoodsImg;
        @BindView(R.id.tv_first_goods_name)
        TextView tvFirstGoodsName;
        @BindView(R.id.tv_first_goods_price)
        TextView tvFirstGoodsPrice;
        @BindView(R.id.iv_second_goods_img)
        ImageView ivSecondGoodsImg;
        @BindView(R.id.tv_second_goods_name)
        TextView tvSecondGoodsName;
        @BindView(R.id.tv_second_goods_price)
        TextView tvSecondGoodsPrice;
        @BindView(R.id.tv_present_price)
        TextView tvPresentPrice;
        @BindView(R.id.tv_save_money)
        TextView tvSaveMoney;
        @BindView(R.id.btn_purchase)
        Button btnPurchase;
        @BindView(R.id.view_divider)
        View viewDivider;

        public ComposeDiscountListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void setContent(ComposeDiscountListBean composeDiscountListBean, int position) {

            //如果为最后一个item 不绘制分割线
            if (position == (getList().size() - 1 )|| (position+1) % 2 == 0) {
                viewDivider.setVisibility(View.GONE);
            }else {
                viewDivider.setVisibility(View.VISIBLE);
            }
            if(composeDiscountListBean.getPreferential_list() != null && composeDiscountListBean.getPreferential_list().size() > 0){

                //第一件商品的图片
                ImageUtils.getCircularPic(composeDiscountListBean.getPreferential_list().get(0).getGoods_img(), ivFirstGoodsImg, m_Context, 10,R.drawable.image_placeholder);
                //第二件商品的图片
                ImageUtils.getCircularPic(composeDiscountListBean.getPreferential_list().get(1).getGoods_img(), ivSecondGoodsImg, m_Context, 10,R.drawable.image_placeholder);
                //第一件商品的名字
                tvFirstGoodsName.setText(composeDiscountListBean.getPreferential_list().get(0).getGoods_name());
                //第二件商品的名字
                tvSecondGoodsName.setText(composeDiscountListBean.getPreferential_list().get(1).getGoods_name());
                //第一件商品的价格
                tvFirstGoodsPrice.setText(m_Context.getString(R.string.goods_price, composeDiscountListBean.getPreferential_list().get(0).getGoods_amount()));
                //第二件商品的价格
                tvSecondGoodsPrice.setText(m_Context.getString(R.string.goods_price, composeDiscountListBean.getPreferential_list().get(1).getGoods_amount()));
            }

            //组合优惠后的价格
            tvPresentPrice.setText(m_Context.getString(R.string.goods_price, composeDiscountListBean.getPreferential_amount()));
            //省了多少钱
            tvSaveMoney.setText(m_Context.getString(R.string.save_money, String.valueOf(composeDiscountListBean.getSave_amount())));

            btnPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(LoginCheckUtils.checkLoginShowDialog(m_Context))return;

                    //加入购物车
                    GoodsDetailsActivity.addCart(mActivity,0, MegaMartApplication.mPreferenceProvider.getUId(),composeDiscountListBean.getPreferential_id(),1);
                }
            });
        }
    }
}
