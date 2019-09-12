package com.benben.megamart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.megamart.R;
import com.benben.megamart.bean.AttrOtherListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.ui.GoodsDetailsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-6-6.
 * Describe:其他规格商品列表adapter
 */
public class OtherSpecListAdapter extends AFinalRecyclerViewAdapter<AttrOtherListBean> {


    public OtherSpecListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new OtherSpecListViewHoler(m_Inflater.inflate(R.layout.item_other_spec_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((OtherSpecListViewHoler) holder).setContent(getItem(position), position);
    }

    public class OtherSpecListViewHoler extends BaseRecyclerViewHolder {

        @BindView(R.id.iv_goods_img)
        ImageView ivGoodsImg;
        @BindView(R.id.tv_goods_name)
        TextView tvGoodsName;
        @BindView(R.id.view_holder_space)
        View viewHolderSpace;
        private View itemView;

        public OtherSpecListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(AttrOtherListBean attrOtherListBean, int position) {

            //最后一个item 现实占位的view
            if(position == (getList().size() -1 )){
                viewHolderSpace.setVisibility(View.VISIBLE);
            }else {
                viewHolderSpace.setVisibility(View.GONE);
            }
            ImageUtils.getCircularPic(attrOtherListBean.getGoods_pic(),ivGoodsImg,m_Context,10,R.drawable.image_placeholder);
            tvGoodsName.setText(attrOtherListBean.getGoods_name());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(m_Context, GoodsDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Constants.EXTRA_KEY_GOODS_ID,String.valueOf(attrOtherListBean.getGoods_id()));
                    m_Context.startActivity(intent);
                }
            });
        }
    }
}
