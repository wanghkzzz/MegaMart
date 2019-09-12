package com.benben.megamart.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.megamart.R;
import com.benben.megamart.bean.HomeBannerImageBean;
import com.benben.megamart.utils.UrlUtils;

import java.util.List;

/**
 * Create by wanghk on 2019-6-6.
 * Describe:首页轮播图adapter
 */
public class HomeBannerViewpagerAdapter extends PagerAdapter {
    private List<HomeBannerImageBean> images;
    private Activity mContext;


    public HomeBannerViewpagerAdapter(List<HomeBannerImageBean> images, Context context) {
        this.mContext = (Activity) context;
        this.images = images;
    }


    public void refreshList(List<HomeBannerImageBean> list) {
        if (images == null) {
            return;
        }
        images.clear();
        images.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = View.inflate(mContext, R.layout.item_banner_image_view, null);
        ImageView iv = view.findViewById(R.id.iv_banner);
        ImageUtils.getCircularPic(images.get(position).getBanner_img(), iv, mContext, 10);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrlUtils.clickBannerUrl(mContext,images.get(position).getParam_type(),images.get(position).getBanner_title(),images.get(position).getBanner_param());
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


}

