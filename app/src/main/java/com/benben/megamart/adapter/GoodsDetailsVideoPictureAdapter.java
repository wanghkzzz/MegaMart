package com.benben.megamart.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.megamart.R;
import com.benben.megamart.bean.GoodsDetailsVideoPictureBean;
import com.shuyu.gsyvideoplayer.utils.GSYVideoHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by wanghk on 2019-6-6.
 * Describe:商品视频和图片列表adapter
 */
public class GoodsDetailsVideoPictureAdapter extends AFinalRecyclerViewAdapter<GoodsDetailsVideoPictureBean> {

    private static final int VIDEO = 0;
    private static final int PICTURE = 1;
    private GSYVideoHelper mSmallVideoHelper;
    private GSYVideoHelper.GSYVideoHelperBuilder mSmallVideoHelperBuilder;


    public GoodsDetailsVideoPictureAdapter(Context ctx, GSYVideoHelper mSmallVideoHelper, GSYVideoHelper.GSYVideoHelperBuilder mSmallVideoHelperBuilder) {
        super(ctx);
        this.mSmallVideoHelper = mSmallVideoHelper;
        this.mSmallVideoHelperBuilder = mSmallVideoHelperBuilder;
    }

    @Override
    protected BaseRecyclerViewHolder onCreateCustomerViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIDEO) {
            return new VideoViewHolder(m_Inflater.inflate(R.layout.item_goods_details_video, parent, false));

        }
        return new PictureViewHolder(m_Inflater.inflate(R.layout.item_goods_details_picture, parent, false));

    }

    @Override
    protected void onBindCustomerViewHolder(BaseRecyclerViewHolder holder, int position) {
        if (holder.getItemViewType() == VIDEO) {
            ((VideoViewHolder) holder).setContent(getItem(position), position);

        } else {
            ((PictureViewHolder) holder).setContent(getItem(position), position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //最后一个item是视频  并且视频资源不为空
        if (position == (getList().size() - 1) && !StringUtils.isEmpty(getList().get(getList().size() - 1).getVideo_url())) {
            return VIDEO;
        }
        return PICTURE;
    }

    public class VideoViewHolder extends BaseRecyclerViewHolder {


        @BindView(R.id.list_item_container)
        FrameLayout videoContainer;
        @BindView(R.id.list_item_btn)
        ImageView btnPlayer;

        public VideoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void setContent(GoodsDetailsVideoPictureBean videoPictureBean, int position) {


            ImageView imageView = new ImageView(m_Context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(R.drawable.image_placeholder);
            mSmallVideoHelper.addVideoPlayer(position, imageView, String.valueOf(position), videoContainer, btnPlayer);
            btnPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyDataSetChanged();
                    mSmallVideoHelper.setPlayPositionAndTag(position, String.valueOf(position));
//                    final String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
                    final String url = videoPictureBean.getVideo_url();
                    mSmallVideoHelperBuilder.setVideoTitle("")
                            .setUrl(url);
                    mSmallVideoHelper.startPlay();
                }
            });
        }
    }

    public class PictureViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.iv_picture)
        ImageView ivPicture;
        private View itemView;

        public PictureViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }

        private void setContent(GoodsDetailsVideoPictureBean videoPictureBean, int position) {

            ImageUtils.getPic(videoPictureBean.getImage_url(), ivPicture, m_Context, R.drawable.image_placeholder);

        }
    }
}
