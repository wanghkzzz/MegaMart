package com.benben.megamart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.commoncore.utils.StringUtils;
import com.benben.megamart.R;
import com.benben.megamart.bean.UsedAddressListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-05-30.
 * Describe:常用地址列表adapter
 */
public class LocationListAdapter extends AFinalRecyclerViewAdapter<UsedAddressListBean> {


    public LocationListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new LocationListViewHoler(m_Inflater.inflate(R.layout.item_location_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((LocationListViewHoler) holder).setContent(getItem(position), position);
    }

    public class LocationListViewHoler extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_city)
        TextView tvCity;
        @BindView(R.id.tv_provinces)
        TextView tvProvinces;
        View rootView;

        public LocationListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
            rootView = view;
        }

        private void setContent(UsedAddressListBean usedAddressListBean, int position) {
            if (StringUtils.isEmpty(usedAddressListBean.getZip_code()) || "null".equals(usedAddressListBean.getZip_code()) || "0".equals(usedAddressListBean.getZip_code())) {
                tvCity.setText(usedAddressListBean.getCity());
            } else {
                if (usedAddressListBean.getZip_code() != null && !StringUtils.isEmpty(usedAddressListBean.getZip_code())) {
                    tvCity.setText(usedAddressListBean.getCity() + "," + usedAddressListBean.getZip_code());

                } else {
                    tvCity.setText(usedAddressListBean.getCity());
                }

            }

            if (StringUtils.isEmpty(usedAddressListBean.getAddress())) {
                tvProvinces.setText("");
            } else {
                tvProvinces.setText(usedAddressListBean.getAddress());
            }

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position, usedAddressListBean);
                    }
                }
            });

        }
    }
}
