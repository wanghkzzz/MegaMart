package com.benben.megamart.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.bean.MyCollectionBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.ui.GoodsDetailsActivity;

import java.util.List;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/14
 * Time: 15:00
 * 收藏适配器
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {

    private Activity mContext;
    private List<MyCollectionBean.GoodsListBean> mBeans;

    private boolean isEditor = false;//是否批量删除 false不删除 true为删除

    public void setEditor(boolean isEditor) {
        this.isEditor = isEditor;
    }

    public CollectionAdapter(Activity mContext, List<MyCollectionBean.GoodsListBean> mBeans) {
        this.mContext = mContext;
        this.mBeans = mBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewHolder holder = new ViewHolder(View.inflate(mContext, R.layout.item_collection, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.tvGoodsName.setText(mBeans.get(i).getGoods_name());
        holder.tvGoodsPrice.setText("＄" + mBeans.get(i).getGoods_price());
        holder.tvSellCount.setText(mContext.getResources().getString(R.string.collection_price, String.valueOf(mBeans.get(i).getSale_count())));
        ImageUtils.getCircularPic(mBeans.get(i).getGoods_img(), holder.ivGoodsThumb, mContext, 10,R.drawable.image_placeholder);

        if (isEditor) {
            holder.ivItemShadow.setVisibility(View.VISIBLE);
            holder.ivSelect.setVisibility(View.VISIBLE);
            if (mBeans.get(i).isSelect()) {
                holder.ivSelect.setImageResource(R.mipmap.icon_select_theme);
            } else {
                holder.ivSelect.setImageResource(R.mipmap.icon_select_no);
            }
        } else {
            holder.ivSelect.setVisibility(View.GONE);
            holder.ivItemShadow.setVisibility(View.INVISIBLE);        }

        //item的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditor) {
                    if (mBeans.get(i).isSelect()) {
                        mBeans.get(i).setSelect(false);
                        holder.ivSelect.setImageResource(R.mipmap.icon_select_no);
                    } else {
                        mBeans.get(i).setSelect(true);
                        holder.ivSelect.setImageResource(R.mipmap.icon_select_theme);
                    }
                    notifyDataSetChanged();
                } else {
                    //进入详情页
                    Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_GOODS_ID, String.valueOf(mBeans.get(i).getGoods_id()));
                    mContext.startActivity(intent);
                }
            }
        });

        //添加购物车
        holder.ivAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditor) {
                    GoodsDetailsActivity.addCart(mContext
                            , mBeans.get(i).getGoods_id()
                            , MegaMartApplication.mPreferenceProvider.getUId()
                            , 0, 1);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mBeans.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGoodsThumb;
        TextView tvGoodsName;
        TextView tvGoodsPrice;
        TextView tvSellCount;
        ImageView ivAddCart;
        ImageView ivSelect;
        ImageView ivItemShadow;

        public ViewHolder(@NonNull View view) {
            super(view);
            ivGoodsThumb = (ImageView) view.findViewById(R.id.iv_goods_thumb);
            tvGoodsName = (TextView) view.findViewById(R.id.tv_goods_name);
            tvGoodsPrice = (TextView) view.findViewById(R.id.tv_goods_price);
            tvSellCount = (TextView) view.findViewById(R.id.tv_sell_count);
            ivAddCart = (ImageView) view.findViewById(R.id.iv_add_cart);
            ivSelect = (ImageView) view.findViewById(R.id.iv_select);
            ivItemShadow = (ImageView) view.findViewById(R.id.iv_item_shadow);
        }
    }
}
