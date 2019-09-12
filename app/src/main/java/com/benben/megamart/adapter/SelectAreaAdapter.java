package com.benben.megamart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.benben.megamart.R;
import com.benben.megamart.bean.SelectAreaBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/17
 * Time: 9:57
 */
public class SelectAreaAdapter extends BaseAdapter {

    private Context mContext;
    private List<SelectAreaBean.AreaInfoBean> mBeans;

    private ViewHolder mHolder = null;
    private int mPType ;

    public SelectAreaAdapter(Context mContext, List<SelectAreaBean.AreaInfoBean> mBeans,int mPType) {
        this.mContext = mContext;
        this.mBeans = mBeans;
        this.mPType = mPType;
    }

    @Override
    public int getCount() {
        return mBeans == null ? 0 : mBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_select_area, null);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        if(mPType == 1){
            mHolder.tvCode.setVisibility(View.GONE);
        }else {
            mHolder.tvCode.setVisibility(View.VISIBLE);
        }
        mHolder.tvName.setText( mBeans.get(position).getArea_name());
        mHolder.tvCode.setText( mBeans.get(position).getZip_code());
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_code)
        TextView tvCode;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
