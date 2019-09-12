package com.benben.megamart.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.bean.SearchGoodsListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.ui.GoodsDetailsActivity;
import com.benben.megamart.utils.LoginCheckUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-05-29.
 * Describe:商品列表通用adapter
 */
public class HomeCommonGoodsListAdapter extends AFinalRecyclerViewAdapter<SearchGoodsListBean> {



    private Activity mActivity;
    public HomeCommonGoodsListAdapter(Context ctx) {
        super(ctx);
        mActivity = (Activity) ctx;
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new HomeCommonGoodsListViewHoler(m_Inflater.inflate(R.layout.item_home_common_goods_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((HomeCommonGoodsListViewHoler) holder).setContent(getItem(position), position);
    }

    public class HomeCommonGoodsListViewHoler extends BaseRecyclerViewHolder {


        @BindView(R.id.iv_goods_thumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tv_goods_name)
        TextView tvGoodsName;
        @BindView(R.id.tv_goods_price)
        TextView tvGoodsPrice;
        @BindView(R.id.tv_sell_count)
        TextView tvSellCount;
        @BindView(R.id.iv_add_cart)
        ImageView ivAddCart;
        @BindView(R.id.view_holder_space)
        View viewHolderSpace;
        private View rootView;

        public HomeCommonGoodsListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
            rootView = view;
        }

        private void setContent(SearchGoodsListBean commonGoodsListBean, int position) {


            //最后一个item 显示占位的view
            if (position == (getList().size() - 1) || (position + 1) % 2 == 0) {
                viewHolderSpace.setVisibility(View.VISIBLE);
            } else {
                viewHolderSpace.setVisibility(View.GONE);
            }

            ImageUtils.getCircularPic(commonGoodsListBean.getGoods_pic(), ivGoodsThumb, m_Context, 10,R.drawable.image_placeholder);

            tvGoodsName.setText(commonGoodsListBean.getGoods_name());
            tvGoodsPrice.setText(m_Context.getString(R.string.goods_price, String.valueOf(commonGoodsListBean.getGoods_price())));
            tvSellCount.setText(m_Context.getString(R.string.sell_count, commonGoodsListBean.getSales_count()));

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(m_Context, GoodsDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Constants.EXTRA_KEY_GOODS_ID, String.valueOf(commonGoodsListBean.getGoods_id()));
                    m_Context.startActivity(intent);
                }
            });
            ivAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!LoginCheckUtils.checkUserIsLogin(m_Context)){
                        ToastUtils.show(m_Context,m_Context.getResources().getString(R.string.please_login_first));
                        return;
                    }
                    GoodsDetailsActivity.addCart(mActivity, commonGoodsListBean.getGoods_id(), MegaMartApplication.mPreferenceProvider.getUId(), 0, 1);
                }
            });
        }
    }
}
