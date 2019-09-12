package com.benben.megamart.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.bean.HomeHotGoodsInfoBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.ui.GoodsDetailsActivity;
import com.benben.megamart.ui.HomeBlockGoodsListActivity;
import com.benben.megamart.utils.LoginCheckUtils;
import com.benben.megamart.utils.UrlUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-05-30.
 * Describe:首页热卖商品列表adapter
 */
public class HomeHotGoodsListAdapter extends AFinalRecyclerViewAdapter<HomeHotGoodsInfoBean> {


    public static final String HOME_HOT_GOODS_TYPE = "home_hot_goods_type";

    private String mCateId;
    private Activity mActivity;

    public HomeHotGoodsListAdapter(Context ctx, String cate_id) {
        super(ctx);
        mActivity = (Activity) ctx;
        this.mCateId = cate_id;
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new HomeHotGoodsListViewHoler(m_Inflater.inflate(R.layout.item_home_hot_goods, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((HomeHotGoodsListViewHoler) holder).setContent(getItem(position), position);
    }

    public class HomeHotGoodsListViewHoler extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_goods_list_title)
        TextView tvGoodsListTitle;
        @BindView(R.id.llyt_more)
        LinearLayout llytMore;
        @BindView(R.id.iv_sell_well_products)
        ImageView ivSellWellProducts;
        @BindView(R.id.iv_first_goods_thumb)
        ImageView ivFirstGoodsThumb;
        @BindView(R.id.tv_first_goods_name)
        TextView tvFirstGoodsName;
        @BindView(R.id.tv_first_goods_price)
        TextView tvFirstGoodsPrice;
        @BindView(R.id.tv_first_sell_count)
        TextView tvFirstSellCount;
        @BindView(R.id.iv_first_add_cart)
        ImageView ivFirstAddCart;
        @BindView(R.id.iv_second_goods_thumb)
        ImageView ivSecondGoodsThumb;
        @BindView(R.id.tv_second_goods_name)
        TextView tvSecondGoodsName;
        @BindView(R.id.tv_second_goods_price)
        TextView tvSecondGoodsPrice;
        @BindView(R.id.tv_second_sell_count)
        TextView tvSecondSellCount;
        @BindView(R.id.iv_second_add_cart)
        ImageView ivSecondAddCart;

        @BindView(R.id.tv_first_goods_old_price)
        TextView tvFirstGoodsOldPrice;
        @BindView(R.id.tv_second_goods_old_price)
        TextView tvSecondGoodsOldPrice;

        @BindView(R.id.view_divider)
        View viewDivider;
        @BindView(R.id.llyt_first_goods)
        LinearLayout llytFirstGoods;
        @BindView(R.id.llyt_second_goods)
        LinearLayout llytSecondGoods;

        public HomeHotGoodsListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void setContent(HomeHotGoodsInfoBean homeHotGoodsInfoBean, int position) {

            //分类title
            tvGoodsListTitle.setText(homeHotGoodsInfoBean.getBlock_name());
            //商品活动图
            ImageUtils.getCircularPic(homeHotGoodsInfoBean.getBlock_img(), ivSellWellProducts, m_Context, 10);
            if (homeHotGoodsInfoBean.getBlock_goods_list() != null && homeHotGoodsInfoBean.getBlock_goods_list().size() > 0) {

                if (homeHotGoodsInfoBean.getBlock_goods_list().size() == 1) {
                    //第一件商品缩略图
                    ImageUtils.getCircularPic(homeHotGoodsInfoBean.getBlock_goods_list().get(0).getGoods_img(), ivFirstGoodsThumb, m_Context, 10, R.drawable.image_placeholder);
                    //第一件商品名字
                    tvFirstGoodsName.setText(homeHotGoodsInfoBean.getBlock_goods_list().get(0).getGoods_name());
                    //第一件商品价格
                    tvFirstGoodsPrice.setText(m_Context.getString(R.string.goods_price, homeHotGoodsInfoBean.getBlock_goods_list().get(0).getGoods_price()));
                    //第一件商品月销售量
                    if (homeHotGoodsInfoBean.getBlock_type() == 3 && homeHotGoodsInfoBean.getBlock_goods_list().get(0).getSales_count() == 0) {
                        tvFirstSellCount.setVisibility(View.GONE);
                    } else {
                        tvFirstSellCount.setVisibility(View.VISIBLE);
                    }
                    tvFirstSellCount.setText(m_Context.getString(R.string.sell_count, homeHotGoodsInfoBean.getBlock_goods_list().get(0).getSales_count()));
                    llytFirstGoods.setVisibility(View.VISIBLE);
                    llytSecondGoods.setVisibility(View.INVISIBLE);
                } else {
                    llytFirstGoods.setVisibility(View.VISIBLE);
                    llytSecondGoods.setVisibility(View.VISIBLE);
                    //第一件商品缩略图
                    ImageUtils.getCircularPic(homeHotGoodsInfoBean.getBlock_goods_list().get(0).getGoods_img(), ivFirstGoodsThumb, m_Context, 10, R.drawable.image_placeholder);
                    //第一件商品名字
                    tvFirstGoodsName.setText(homeHotGoodsInfoBean.getBlock_goods_list().get(0).getGoods_name());
                    //第一件商品价格
                    tvFirstGoodsPrice.setText(m_Context.getString(R.string.goods_price, homeHotGoodsInfoBean.getBlock_goods_list().get(0).getGoods_price()));
                    //第一件商品月销售量
                    tvFirstSellCount.setText(m_Context.getString(R.string.sell_count, homeHotGoodsInfoBean.getBlock_goods_list().get(0).getSales_count()));

                    //第二件商品缩略图
                    ImageUtils.getCircularPic(homeHotGoodsInfoBean.getBlock_goods_list().get(1).getGoods_img(), ivSecondGoodsThumb, m_Context, 10, R.drawable.image_placeholder);
                    //第二件商品名字
                    tvSecondGoodsName.setText(homeHotGoodsInfoBean.getBlock_goods_list().get(1).getGoods_name());
                    //第二件商品价格
                    tvSecondGoodsPrice.setText(m_Context.getString(R.string.goods_price, homeHotGoodsInfoBean.getBlock_goods_list().get(1).getGoods_price()));
                    //第二件商品月销量
                    tvSecondSellCount.setText(m_Context.getString(R.string.sell_count, homeHotGoodsInfoBean.getBlock_goods_list().get(1).getSales_count()));

                    if (homeHotGoodsInfoBean.getBlock_type() == 3 && (homeHotGoodsInfoBean.getBlock_goods_list().get(0).getSales_count() == 0 || homeHotGoodsInfoBean.getBlock_goods_list().get(1).getSales_count() == 0)) {
                        tvSecondSellCount.setVisibility(View.GONE);
                        tvFirstSellCount.setVisibility(View.GONE);
                    } else {
                        tvSecondSellCount.setVisibility(View.VISIBLE);
                        tvFirstSellCount.setVisibility(View.VISIBLE);
                    }
                }


            } else {
                llytFirstGoods.setVisibility(View.GONE);
                llytSecondGoods.setVisibility(View.GONE);
            }

            ivSellWellProducts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  Intent intent = new Intent(m_Context, CommonWebViewActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_URL, homeHotGoodsInfoBean.getBlock_url());
                    intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_TITLE, homeHotGoodsInfoBean.getBlock_name());
                    intent.putExtra(Constants.EXTRA_KEY_IS_URL, true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    m_Context.startActivity(intent);*/
                    UrlUtils.clickBannerUrl(mActivity, Integer.parseInt(homeHotGoodsInfoBean.getBlock_url_param()), homeHotGoodsInfoBean.getBlock_name(), homeHotGoodsInfoBean.getBlock_url());
                }
            });
            //中间加横线
            tvFirstGoodsOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            tvSecondGoodsOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

            //原价
           /* if(StringUtils.isEmpty(String.valueOf(homeHotGoodsInfoBean.getFirstGoodsOldPrice()))){
                tvFirstGoodsOldPrice.setVisibility(View.GONE);
            }else {
                tvFirstGoodsOldPrice.setVisibility(View.VISIBLE);
                tvFirstGoodsOldPrice.setText(m_Context.getString(R.string.goods_price, String.valueOf(homeHotGoodsInfoBean.getFirstGoodsOldPrice())));
            }
            if(StringUtils.isEmpty(String.valueOf(homeHotGoodsInfoBean.getFirstGoodsOldPrice()))){
                tvSecondGoodsOldPrice.setVisibility(View.GONE);
            }else {
                tvSecondGoodsOldPrice.setVisibility(View.VISIBLE);
                tvSecondGoodsOldPrice.setText(m_Context.getString(R.string.goods_price, String.valueOf(homeHotGoodsInfoBean.getSecondGoodsOldPrice())));
            }*/

            //如果是最后一个view  隐藏分割线
            if (position == (getList().size() - 1)) {
                viewDivider.setVisibility(View.GONE);
            }
            //更多  点击事件
            llytMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(m_Context, HomeBlockGoodsListActivity.class);
                    intent.putExtra(HOME_HOT_GOODS_TYPE, homeHotGoodsInfoBean.getBlock_type());
                    intent.putExtra(Constants.EXTRA_KEY_CATE_ID, mCateId);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    m_Context.startActivity(intent);
                }
            });


            ivFirstAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!LoginCheckUtils.checkUserIsLogin(m_Context)) {
                        ToastUtils.show(m_Context, m_Context.getResources().getString(R.string.please_login_first));
                        return;
                    }
                    if (homeHotGoodsInfoBean.getBlock_goods_list() != null && homeHotGoodsInfoBean.getBlock_goods_list().size() > 0) {
                        int firstGoodsId = homeHotGoodsInfoBean.getBlock_goods_list().get(0).getGoods_id();
                        GoodsDetailsActivity.addCart(mActivity, firstGoodsId, MegaMartApplication.mPreferenceProvider.getUId(), 0, 1);
                    }

                }
            });
            ivSecondAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!LoginCheckUtils.checkUserIsLogin(m_Context)) {
                        ToastUtils.show(m_Context, m_Context.getResources().getString(R.string.please_login_first));
                        return;
                    }
                    if (homeHotGoodsInfoBean.getBlock_goods_list() != null && homeHotGoodsInfoBean.getBlock_goods_list().size() > 1) {
                        int secondGoodsId = homeHotGoodsInfoBean.getBlock_goods_list().get(1).getGoods_id();
                        GoodsDetailsActivity.addCart(mActivity, secondGoodsId, MegaMartApplication.mPreferenceProvider.getUId(), 0, 1);
                    }


                }
            });

            llytFirstGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeHotGoodsInfoBean.getBlock_goods_list() == null || homeHotGoodsInfoBean.getBlock_goods_list().size() <= 0) {
                        return;
                    }
                    Intent intent = new Intent(m_Context, GoodsDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Constants.EXTRA_KEY_GOODS_ID, String.valueOf(homeHotGoodsInfoBean.getBlock_goods_list().get(0).getGoods_id()));
                    m_Context.startActivity(intent);
                }
            });
            llytSecondGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeHotGoodsInfoBean.getBlock_goods_list() == null || homeHotGoodsInfoBean.getBlock_goods_list().size() <= 1) {
                        return;
                    }
                    Intent intent = new Intent(m_Context, GoodsDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Constants.EXTRA_KEY_GOODS_ID, String.valueOf(homeHotGoodsInfoBean.getBlock_goods_list().get(1).getGoods_id()));
                    m_Context.startActivity(intent);
                }
            });


        }
    }
}
