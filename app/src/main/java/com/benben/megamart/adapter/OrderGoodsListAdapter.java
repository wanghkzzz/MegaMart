package com.benben.megamart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.megamart.R;
import com.benben.megamart.bean.GoodsListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/17
 * Time: 14:18
 */
public class OrderGoodsListAdapter extends BaseAdapter {

    private Context mContext;
    private List<GoodsListBean> mBean;

    private ViewHolder mHolder = null;

    public OrderGoodsListAdapter(Context mContext, List<GoodsListBean> mBean) {
        this.mContext = mContext;
        this.mBean = mBean;
    }

    @Override
    public int getCount() {
        return mBean == null ? 0 : mBean.size();
    }

    @Override
    public Object getItem(int position) {
        return mBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_order_goods_list, null);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        ImageUtils.getCircularPic(mBean.get(position).getGoods_img(), mHolder.ivImg, mContext, 10, R.drawable.image_placeholder);
        mHolder.tvGoodsTitle.setText(mBean.get(position).getGoods_name());
        mHolder.tvGoodsType.setText(mBean.get(position).getGoods_attr());
        mHolder.tvNumber.setText("x" + mBean.get(position).getGoods_number());
        mHolder.tvPrice.setText("＄" + mBean.get(position).getGoods_price());

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.iv_img)
        ImageView ivImg;
        @BindView(R.id.tv_goods_title)
        TextView tvGoodsTitle;
        @BindView(R.id.tv_goods_type)
        TextView tvGoodsType;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_number)
        TextView tvNumber;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
