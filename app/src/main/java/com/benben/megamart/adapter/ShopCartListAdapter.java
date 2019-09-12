package com.benben.megamart.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.PreciseCompute;
import com.benben.megamart.R;
import com.benben.megamart.bean.CartGoodsListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.widget.NumberView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-6-4.
 * Describe:购物车列表adapter
 */
public class ShopCartListAdapter extends AFinalRecyclerViewAdapter<CartGoodsListBean> {

    private Activity mActivity;

    public ShopCartListAdapter(Context ctx) {
        super(ctx);
        mActivity = (Activity) ctx;
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new ShopCartGoodsListViewHolder(m_Inflater.inflate(R.layout.item_shop_cart_goods_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((ShopCartGoodsListViewHolder) holder).setContent(getItem(position), position);
    }

    public class ShopCartGoodsListViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.llyt_itemView)
        LinearLayout llytItemView;
        @BindView(R.id.iv_goods_image)
        ImageView ivGoodsImage;
        @BindView(R.id.tv_goods_name)
        TextView tvGoodsName;
        @BindView(R.id.tv_goods_price)
        TextView tvGoodsPrice;
        @BindView(R.id.numberView_cart)
        NumberView numberViewCart;
        @BindView(R.id.tv_compose_goods_name)
        TextView tvComposeGoodsName;
        @BindView(R.id.tv_compose_goods_price)
        TextView tvComposeGoodsPrice;
        @BindView(R.id.tv_invalid_tips)
        TextView tvInvalidTips;
        @BindView(R.id.llyt_compose_goods)
        LinearLayout llytComposeGoods;

        public ShopCartGoodsListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void setContent(CartGoodsListBean cartGoodsListBean, int position) {
            //商品价格
            tvGoodsPrice.setText(m_Context.getResources().getString(R.string.goods_price, cartGoodsListBean.getGoods_amount()));
            //商品名称
            tvGoodsName.setText(cartGoodsListBean.getGoods_name());
            //商品数量
            numberViewCart.setNum(cartGoodsListBean.getGoods_number());
            //商品缩略图
            ImageUtils.getCircularPic(cartGoodsListBean.getGoods_img(), ivGoodsImage, m_Context, 10, R.drawable.image_placeholder);

            //商品选中状态
            ivSelect.setSelected(cartGoodsListBean.isSelect());
            if (cartGoodsListBean.getStatus() != 1) {
                tvInvalidTips.setVisibility(View.VISIBLE);
            } else {
                tvInvalidTips.setVisibility(View.GONE);
            }

            //组合商品
            if (cartGoodsListBean.getPreferential_id() != -1) {
                //是否是组合商品的第一个
                if (cartGoodsListBean.isFirstPreferential()) {
                    ivSelect.setVisibility(View.VISIBLE);
                    numberViewCart.setVisibility(View.VISIBLE);
                    llytComposeGoods.setVisibility(View.VISIBLE);
                    tvComposeGoodsName.setText(cartGoodsListBean.getPreferential_name());
                    tvComposeGoodsPrice.setText(m_Context.getResources().getString(R.string.goods_price, cartGoodsListBean.getPreferential_price()));


                } else {
                    ivSelect.setVisibility(View.INVISIBLE);
                    numberViewCart.setVisibility(View.INVISIBLE);
                    llytComposeGoods.setVisibility(View.GONE);
                }
                tvGoodsPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                tvGoodsPrice.setTextColor(m_Context.getResources().getColor(R.color.color_666666));
                tvGoodsPrice.setTextSize(12f);
            } else { //单商品
                llytComposeGoods.setVisibility(View.GONE);
                ivSelect.setVisibility(View.VISIBLE);
                numberViewCart.setVisibility(View.VISIBLE);

                tvGoodsPrice.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
                tvGoodsPrice.setTextColor(m_Context.getResources().getColor(R.color.color_FE4918));
                tvGoodsPrice.setTextSize(15f);
            }
            //选择商品的监听
            ivSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartGoodsListBean.setSelect(!cartGoodsListBean.isSelect());
                    if (cartGoodsListBean.getPreferential_id() != -1) {
                        getItem((position + 1)).setSelect(!getItem((position + 1)).isSelect());
                    }

                    checkSelectAll();
                }
            });

            //加减控件的监听
            numberViewCart.setOnNumberChangeListener(new NumberView.OnNumberChangeListener() {
                @Override
                public void onChange(int newnum) {
                    if (newnum < 1) {
                        numberViewCart.setNum(1);
                        return;
                    }
                    cartGoodsListBean.setGoods_number(newnum);
                    notifyItemChanged(position);
                    if (mListener != null) {
                        mListener.onCartNumChange(position, cartGoodsListBean, newnum);
                    }
                }
            });

            //item点击事件
            llytItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(position, cartGoodsListBean);
                    }
                }
            });
        }
    }

    //回调
    public interface ShopCartListener {

        void onItemClick(int position, CartGoodsListBean data);

        void onCartNumChange(int position, CartGoodsListBean data, int num);

        void onListSelectAll(boolean isSelectAll, double totalMoney);
    }

    private ShopCartListener mListener;

    public void setListener(ShopCartListener listener) {
        this.mListener = listener;
    }

    //检查是否全选了,并计算购物车选中的总金额
    public void checkSelectAll() {
        boolean isSelectAll = true;
        int flag = -1;
        double totalMoney = 0.0;
        if (getItemCount() == 0) {
            isSelectAll = false;
        } else {
            for (int i = 0; i < getList().size(); i++) {
                if (getList().get(i).isSelect()) {
                    if (getList().get(i).getPreferential_id() == -1) { // -1代表是普通商品 反之是组合商品
                        totalMoney = PreciseCompute.add(totalMoney,
                                PreciseCompute.mul(getList().get(i).getGoods_number(), Double.parseDouble(getList().get(i).getGoods_amount())));
                        Log.e(Constants.WHK_TAG, "checkSelectAll: 金额 = " + totalMoney);
                    } else {
                        //需要过滤掉下一个商品  否则会重复计算组合商品的价格
                        if (i == flag) {
                            continue;
                        }
                        totalMoney = PreciseCompute.add(totalMoney,
                                PreciseCompute.mul(getList().get(i).getGoods_number(), Double.parseDouble(getList().get(i).getPreferential_price())));
                        flag = (i + 1);

                    }

                } else {
                    isSelectAll = false;
                }
            }
        }
        if (mListener != null) {
            mListener.onListSelectAll(isSelectAll, totalMoney);
        }
        notifyDataSetChanged();
    }

    //更新购物车数量
    public void updateCartNum(int position, int num) {
        getItem(position).setGoods_number(num);
        notifyItemChanged(position);
        checkSelectAll();
    }


    //全选/全不选
    public void selectAll(boolean selectFlag) {
        for (int i = 0; i < getList().size(); i++) {
            getList().get(i).setSelect(selectFlag);
        }
        notifyDataSetChanged();
        checkSelectAll();
    }

    //检查是否有选中的条目
    public boolean checkSelected() {
        for (CartGoodsListBean item : getList()) {
            if (item.isSelect()) {
                return true;
            }
        }
        return false;
    }

    //获取选中商品的ids
    public String getSelectedGoodsIds(boolean isPreferential) {
        StringBuilder normalString = new StringBuilder();
        StringBuilder composeString = new StringBuilder();

        if (isPreferential) {
            for (CartGoodsListBean item : getList()) {
                if (item.isSelect() && item.getPreferential_id() != -1 && item.isFirstPreferential()) {
                    if (composeString.length() > 0) {
                        composeString.append(",");
                    }
                    composeString.append(item.getPreferential_id());

                }
            }
            if (composeString.length() > 0) {
                return composeString.toString();
            } else {
                return "";
            }

        } else {
            for (CartGoodsListBean item : getList()) {
                if (item.isSelect() && item.getPreferential_id() == -1) {
                    if (normalString.length() > 0) {
                        normalString.append(",");
                    }
                    normalString.append(item.getGoods_id());

                }
            }
            if (normalString.length() > 0) {
                return normalString.toString();
            } else {
                return "";
            }
        }

    }

    //获取选中商品的list
    public ArrayList<CartGoodsListBean> getSelectedGoodsList() {
        ArrayList<CartGoodsListBean> selectCartGoodsList = new ArrayList<>();
        for (int i = 0; i < getList().size(); i++) {
            if (getList().get(i).isSelect()) {
                selectCartGoodsList.add(getList().get(i));
            }
        }
        return selectCartGoodsList;
    }

    //选中的商品 是否包含无效商品
    public boolean isHaveInvalidGoods() {
        for (CartGoodsListBean item : getList()) {
            if (item.isSelect() && item.getStatus() != 1) {
                return true;
            }
        }
        return false;
    }
}
