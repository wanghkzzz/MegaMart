package com.benben.megamart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.megamart.R;
import com.benben.megamart.bean.GoodsScreenConditionBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-05-30.
 * Describe:商品筛选条件列表adapter
 */
public class GoodsScreenConditionListAdapter extends AFinalRecyclerViewAdapter<GoodsScreenConditionBean> {


    //未选中的list
    private List<GoodsScreenConditionBean> mUnSelectedList = new ArrayList<>();
    //选中的list
    private List<GoodsScreenConditionBean> mSelectedList = new ArrayList<>();


    public GoodsScreenConditionListAdapter(Context ctx) {
        super(ctx);

    }

    @Override
    public void appendToList(List<GoodsScreenConditionBean> list) {
        super.appendToList(list);
        mUnSelectedList.addAll(list);
        refreshList(mUnSelectedList);

    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        return new GoodsScreenConditionListViewHoler(m_Inflater.inflate(R.layout.item_goods_screen, parent, false));
    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        ((GoodsScreenConditionListViewHoler) holder).setContent(getItem(position), position);
    }

    public class GoodsScreenConditionListViewHoler extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.iv_cancel)
        ImageView ivCancel;
        @BindView(R.id.rlyt_screen_item)
        RelativeLayout rlytScreenItem;
        private View itemView;

        public GoodsScreenConditionListViewHoler(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(GoodsScreenConditionBean conditionBean, int position) {

            //内容
            tvContent.setText(conditionBean.getConditionName());

            //选中
            if (conditionBean.isSelect()) {

                ivCancel.setVisibility(View.VISIBLE);
                tvContent.setTextColor(m_Context.getResources().getColor(R.color.color_EC5413));
                rlytScreenItem.setBackground(m_Context.getResources().getDrawable(R.drawable.shape_goods_screen_item_selected_bg));
            } else {
                //未选中
                tvContent.setTextColor(m_Context.getResources().getColor(R.color.color_333333));
                rlytScreenItem.setBackground(m_Context.getResources().getDrawable(R.drawable.shape_goods_screen_item_unselected_bg));
                ivCancel.setVisibility(View.GONE);
            }


            //选择item的监听
            rlytScreenItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (conditionBean.isSelect()) {
                        return;
                    }

                    conditionBean.setSelect(true);
                    mSelectedList.clear();
                    mSelectedList.add(conditionBean);
                    refreshList(mSelectedList);

                    if (mConditionSelectListener != null) {
                        mConditionSelectListener.onSelectListener(position, conditionBean.getConditionId());
                    }
                }
            });

            //点击取消选择
            ivCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unSelectAll();

                    if (mConditionSelectListener != null) {
                        mConditionSelectListener.onSelectListener(position, "");
                    }
                }
            });
        }
    }

    //选择商品属性的监听
    private ConditionSelectListener mConditionSelectListener;

    public interface ConditionSelectListener {
        void onSelectListener(int position, String condition);
    }

    public void setOnConditionSelectListener(ConditionSelectListener mConditionSelectListener) {
        this.mConditionSelectListener = mConditionSelectListener;
    }

    //全不选
    public void unSelectAll() {
        if (null != mUnSelectedList && mUnSelectedList.size() > 0) {
            for (int i = 0; i < mUnSelectedList.size(); i++) {
                mUnSelectedList.get(i).setSelect(false);
            }
        }
        refreshList(mUnSelectedList);
    }
}
