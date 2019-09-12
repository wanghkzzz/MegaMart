package com.benben.megamart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.megamart.R;
import com.benben.megamart.bean.CategoryListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.ui.SearchGoodsListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-05-30.
 * Describe:商品分类adapter
 */
public class CategorySecondLevelAdapter extends AFinalRecyclerViewAdapter<CategoryListBean.ChildListBean> {


    public CategorySecondLevelAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new CategoryListViewHoler(m_Inflater.inflate(R.layout.item_category_second_level_list, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((CategoryListViewHoler) holder).setContent(getItem(position), position);
    }

    public class CategoryListViewHoler extends BaseRecyclerViewHolder {

        @BindView(R.id.iv_category_img)
        ImageView ivCategoryImg;
        @BindView(R.id.tv_category_name)
        TextView tvCategoryName;

        private View itemView;


        public CategoryListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(CategoryListBean.ChildListBean childListBean, int position) {

            if(position == 0){
                ivCategoryImg.setImageResource(Integer.parseInt(childListBean.getCate_img()));
            }else {
                //分类icon
                ImageUtils.getCircularPic(childListBean.getCate_img(), ivCategoryImg, m_Context, 10,R.drawable.image_placeholder);
            }

            //分类名称
            tvCategoryName.setText(childListBean.getCate_name());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(m_Context, SearchGoodsListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Constants.EXTRA_KEY_CATE_PARENT_ID, childListBean.getParent_id());
                    intent.putExtra(Constants.EXTRA_KEY_CATE_ID, childListBean.getCate_id());
                    m_Context.startActivity(intent);
                }
            });

        }
    }
}
