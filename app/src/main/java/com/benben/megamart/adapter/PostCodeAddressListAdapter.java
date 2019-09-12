package com.benben.megamart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.megamart.R;
import com.benben.megamart.bean.PostCodeAddressListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-06-21.
 * Describe:邮编地址列表adapter
 */
public class PostCodeAddressListAdapter extends AFinalRecyclerViewAdapter<PostCodeAddressListBean> {


    public PostCodeAddressListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new PostCodeAddressListViewHoler(m_Inflater.inflate(R.layout.item_post_code_address_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((PostCodeAddressListViewHoler) holder).setContent(getItem(position), position);
    }

    public class PostCodeAddressListViewHoler extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_city)
        TextView tvCity;

        public PostCodeAddressListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void setContent(PostCodeAddressListBean postCodeAddressListBean, int position) {
            tvCity.setText(postCodeAddressListBean.getArea_name());
            tvCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position, postCodeAddressListBean);
                    }
                }
            });
        }
    }
}
