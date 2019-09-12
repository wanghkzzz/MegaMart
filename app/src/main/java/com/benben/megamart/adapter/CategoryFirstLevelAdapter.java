package com.benben.megamart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benben.megamart.R;
import com.benben.megamart.bean.CategoryListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-05-30.
 * Describe:商品分类adapter
 */
public class CategoryFirstLevelAdapter extends AFinalRecyclerViewAdapter<CategoryListBean> {


    private int index = 0;

    public CategoryFirstLevelAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new CategoryListViewHoler(m_Inflater.inflate(R.layout.item_category_first_level_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((CategoryListViewHoler) holder).setContent(getItem(position), position);
    }

    public class CategoryListViewHoler extends BaseRecyclerViewHolder {
        @BindView(R.id.view_indicator)
        View viewIndicator;
        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;
        private View itemView;


        public CategoryListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(CategoryListBean categoryListBean, int position) {

            if(index == position){
                viewIndicator.setVisibility(View.VISIBLE);
                tvCategoryName.setTextColor(m_Context.getResources().getColor((R.color.color_EC5413)));
                itemView.setBackgroundColor(m_Context.getResources().getColor((R.color.color_F7F7F7)));
            }else {
                viewIndicator.setVisibility(View.GONE);
                tvCategoryName.setTextColor(m_Context.getResources().getColor((R.color.color_333333)));
                itemView.setBackgroundColor(m_Context.getResources().getColor((R.color.color_FFFFFF)));

            }
            //分类名称
            tvCategoryName.setText(categoryListBean.getCate_name());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    index = position;
                    if(mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(v,position,categoryListBean);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }
}
