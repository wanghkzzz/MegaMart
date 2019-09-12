package com.benben.megamart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.megamart.R;
import com.benben.megamart.bean.GuessYouLikeGoodsListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.ui.GoodsDetailsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-6-6.
 * Describe:猜你喜欢商品列表adapter
 */
public class GuessYouLikeListAdapter extends AFinalRecyclerViewAdapter<GuessYouLikeGoodsListBean> {


    public GuessYouLikeListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new GuessYouLikeListViewHoler(m_Inflater.inflate(R.layout.item_guess_you_like_goods_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((GuessYouLikeListViewHoler) holder).setContent(getItem(position), position);
    }

    public class GuessYouLikeListViewHoler extends BaseRecyclerViewHolder {

        @BindView(R.id.iv_goods_img)
        ImageView ivGoodsImg;
        @BindView(R.id.tv_goods_name)
        TextView tvGoodsName;
        @BindView(R.id.tv_goods_price)
        TextView tvGoodsPrice;
        @BindView(R.id.view_holder_space)
        View viewHolderSpace;
        private View itemView;

        public GuessYouLikeListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(GuessYouLikeGoodsListBean guessYouLikeGoodsListBean, int position) {

            //最后一个item 现实占位的view
            if (position == (getList().size() - 1)) {
                viewHolderSpace.setVisibility(View.VISIBLE);
            } else {
                viewHolderSpace.setVisibility(View.GONE);
            }
            ImageUtils.getCircularPic(guessYouLikeGoodsListBean.getGoods_pic(), ivGoodsImg, m_Context, 10,R.drawable.image_placeholder);
            tvGoodsName.setText(guessYouLikeGoodsListBean.getGoods_name());
            tvGoodsPrice.setText(m_Context.getString(R.string.goods_price, String.valueOf(guessYouLikeGoodsListBean.getGoods_pric())));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(m_Context, GoodsDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Constants.EXTRA_KEY_GOODS_ID,String.valueOf(guessYouLikeGoodsListBean.getGoods_id()));
                    m_Context.startActivity(intent);
                }
            });
        }
    }
}
