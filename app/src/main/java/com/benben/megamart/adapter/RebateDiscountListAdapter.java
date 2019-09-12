package com.benben.megamart.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.bean.RebateDiscountListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.ui.GoodsDetailsActivity;
import com.benben.megamart.ui.LoginActivity;
import com.benben.megamart.utils.LoginCheckUtils;
import com.hss01248.dialog.interfaces.MyDialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-06-05.
 * Describe:折扣优惠列表adapter
 */
public class RebateDiscountListAdapter extends AFinalRecyclerViewAdapter<RebateDiscountListBean> {


    private boolean isSpecialDiscount = true;
    private Activity mActivity;

    public RebateDiscountListAdapter(Context ctx, boolean isSpecialDiscount) {
        super(ctx);
        this.isSpecialDiscount = isSpecialDiscount;
        this.mActivity = (Activity) ctx;
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new SpecialDiscountListListViewHoler(m_Inflater.inflate(R.layout.item_special_discount_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((SpecialDiscountListListViewHoler) holder).setContent(getItem(position), position);
    }

    public class SpecialDiscountListListViewHoler extends BaseRecyclerViewHolder {


        @BindView(R.id.iv_goods_image)
        ImageView ivGoodsImage;
        @BindView(R.id.tv_goods_name)
        TextView tvGoodsName;
        @BindView(R.id.tv_goods_old_price)
        TextView tvGoodsOldPrice;
        @BindView(R.id.tv_special_price)
        TextView tvSpecialPrice;
        @BindView(R.id.tv_goods_price)
        TextView tvGoodsPrice;
        @BindView(R.id.tv_discount_percent)
        TextView tvDiscountPercent;
        @BindView(R.id.btn_purchase)
        Button btnPurchase;
        @BindView(R.id.view_divider)
        View viewDivider;
        View itemView;

        public SpecialDiscountListListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(RebateDiscountListBean rebateDiscountListBean, int position) {

            //如果为最后一个item 不绘制分割线
            if (position == (getList().size() - 1)) {
                viewDivider.setVisibility(View.GONE);
            }else {
                viewDivider.setVisibility(View.VISIBLE);
            }
            //特价优惠
            if (isSpecialDiscount) {
                tvSpecialPrice.setVisibility(View.VISIBLE);
                tvDiscountPercent.setVisibility(View.GONE);

            } else {
                //折扣优惠
                tvSpecialPrice.setVisibility(View.GONE);
                tvDiscountPercent.setVisibility(View.VISIBLE);
                tvDiscountPercent.setText(m_Context.getString(R.string.discount_number, String.valueOf(rebateDiscountListBean.getGoods_discount())));
            }

            //商品图片
            ImageUtils.getCircularPic(rebateDiscountListBean.getGoods_img(), ivGoodsImage, m_Context, 10,R.drawable.image_placeholder);
            //商品名字
            tvGoodsName.setText(rebateDiscountListBean.getGoods_name());
            //商品现在的价格
            tvGoodsPrice.setText(m_Context.getString(R.string.goods_price, String.valueOf(rebateDiscountListBean.getGoods_price())));
            //商品原价
            tvGoodsOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

            tvGoodsOldPrice.setText(m_Context.getString(R.string.goods_price, String.valueOf(rebateDiscountListBean.getCost_price())));
            btnPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!LoginCheckUtils.checkUserIsLogin(m_Context)) {
                        LoginCheckUtils.showYanZhengDialog(m_Context, new MyDialogListener() {
                            @Override
                            public void onFirst() {
                                m_Context.startActivity(new Intent(m_Context, LoginActivity.class));
                            }

                            @Override
                            public void onSecond() {

                            }
                        });
                        return ;

                    }

                   /* //购买
                    CartGoodsListBean cartGoodsListBean = new CartGoodsListBean();
//                    cartGoodsListBean.setCart_id();
                    cartGoodsListBean.setGoods_id(rebateDiscountListBean.getGoods_id());
                    cartGoodsListBean.setGoods_img(rebateDiscountListBean.getGoods_img());
                    cartGoodsListBean.setGoods_amount(rebateDiscountListBean.getGoods_price());
                    cartGoodsListBean.setGoods_name(rebateDiscountListBean.getGoods_name());
                    cartGoodsListBean.setGoods_number(1);
                    cartGoodsListBean.setSelect(false);
                    cartGoodsListBean.setStatus(1);
                    cartGoodsListBean.setPreferential_id(-1);
                    ArrayList<CartGoodsListBean> cartGoodsListBeans = new ArrayList<>();
                    cartGoodsListBeans.add(cartGoodsListBean);
                    SubmitOrderGoodsFormatBean goodsFormatBean = new SubmitOrderGoodsFormatBean(cartGoodsListBeans);
                    Intent intent = new Intent(m_Context, SubmitOrderActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_GOODS_LIST, goodsFormatBean);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    m_Context.startActivity(intent);*/

                    GoodsDetailsActivity.addCart(mActivity,rebateDiscountListBean.getGoods_id(), MegaMartApplication.mPreferenceProvider.getUId(),0,1);


                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(m_Context, GoodsDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Constants.EXTRA_KEY_GOODS_ID,String.valueOf(rebateDiscountListBean.getGoods_id()));
                    m_Context.startActivity(intent);
                }
            });

        }
    }
}
