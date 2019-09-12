package com.benben.megamart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.megamart.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-05-30.
 * Describe:搜索记录adapter
 */
public class SearchRecordListAdapter extends AFinalRecyclerViewAdapter<String> {

    public SearchRecordListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new SearchListViewHoler(m_Inflater.inflate(R.layout.item_search_record, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((SearchListViewHoler) holder).setContent(getItem(position), position);
    }

    public class SearchListViewHoler extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_goods_name)
        TextView tvGoodsName;
        private View itemView;

        public SearchListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(String s, int position) {

            tvGoodsName.setText(s);

            tvGoodsName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(v,position,s);
                    }
                }
            });
        }
    }
}
