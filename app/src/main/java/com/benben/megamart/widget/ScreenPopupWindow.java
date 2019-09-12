package com.benben.megamart.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.R;
import com.benben.megamart.adapter.GoodsScreenConditionListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.GoodsOriginAndCompanyBean;
import com.benben.megamart.bean.GoodsPlaceCompanyListBean;
import com.benben.megamart.bean.GoodsScreenConditionBean;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by: wanghk 2019-06-01.
 * Describe:筛选popupWindow
 */
public class ScreenPopupWindow extends PopupWindow {

    private Activity mContext;
    //第一条属性的列表
    private List<GoodsScreenConditionBean> mFirstConditionList;
    //第二条属性的列表
    private List<GoodsScreenConditionBean> mSecondConditionList;
    private View mView;
    //第一条属性的列表adapter
    private GoodsScreenConditionListAdapter mFirstConditionListAdapter;
    //第二条属性的列表adapter
    private GoodsScreenConditionListAdapter mSecondConditionListAdapter;

    //商品属性
    private String mFirstCondition = "";
    private String mSecondCondition = "";

    public ScreenPopupWindow(Context context) {
        super(context);

    }

    public ScreenPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    public ScreenPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScreenPopupWindow(int width, int height, Context mContext) {
        super(width, height);
        this.mContext = (Activity) mContext;
        this.mFirstConditionList = new ArrayList<>();
        this.mSecondConditionList = new ArrayList<>();
        init();
    }

    private void init() {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //setAnimationStyle(R.style.PermissionAnimFade);
        mView = inflater.inflate(R.layout.pop_goods_screen, null);
        ViewHolder holder = new ViewHolder(mView);

        setContentView(mView);

        holder.rlv_first_condition.setLayoutManager(new GridLayoutManager(mContext, 3));
        holder.rlv_second_condition.setLayoutManager(new GridLayoutManager(mContext, 3));


        mFirstConditionListAdapter = new GoodsScreenConditionListAdapter(mContext);
        mSecondConditionListAdapter = new GoodsScreenConditionListAdapter(mContext);

        holder.rlv_first_condition.setAdapter(mFirstConditionListAdapter);
        holder.rlv_second_condition.setAdapter(mSecondConditionListAdapter);

        //初始化数据
        initData();

        //item点击事件
        mFirstConditionListAdapter.setOnConditionSelectListener(new GoodsScreenConditionListAdapter.ConditionSelectListener() {
            @Override
            public void onSelectListener(int position, String condition) {
                mFirstCondition = condition;
            }
        });
        mSecondConditionListAdapter.setOnConditionSelectListener(new GoodsScreenConditionListAdapter.ConditionSelectListener() {
            @Override
            public void onSelectListener(int position, String condition) {
                mSecondCondition = condition;
            }
        });


        //向下收起
        holder.iv_first_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.rlv_first_condition.getVisibility() == View.GONE) {
                    holder.iv_first_condition.setImageResource(R.mipmap.icon_pack_bottom);
                    holder.rlv_first_condition.setVisibility(View.VISIBLE);

                } else {
                    holder.iv_first_condition.setImageResource(R.mipmap.icon_pack_top);
                    holder.rlv_first_condition.setVisibility(View.GONE);
                }
            }
        });
        //向下收起
        holder.iv_second_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.rlv_second_condition.getVisibility() == View.GONE) {
                    holder.iv_second_condition.setImageResource(R.mipmap.icon_pack_bottom);
                    holder.rlv_second_condition.setVisibility(View.VISIBLE);

                } else {
                    holder.iv_second_condition.setImageResource(R.mipmap.icon_pack_top);
                    holder.rlv_second_condition.setVisibility(View.GONE);
                }
            }
        });


        //确定按钮点击事件
        holder.btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mPositiveClickListener != null) {
                    GoodsOriginAndCompanyBean goodsOriginAndCompanyBean = new GoodsOriginAndCompanyBean(mSecondCondition, mFirstCondition);
                    mPositiveClickListener.positiveClick(v, goodsOriginAndCompanyBean);
                }
            }
        });
        //重置按钮点击事件
        holder.btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstConditionListAdapter.unSelectAll();
                mSecondConditionListAdapter.unSelectAll();
            }
        });


        //对popupWindows进行触摸监听

        holder.holderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        Activity activity = (Activity) mContext;
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    //添加测试数据
    private void initData() {


        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_GOODS_PLACE_COMPANY_LIST)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {


            @Override
            public void onSuccess(String result, String msg) {

                GoodsPlaceCompanyListBean goodsPlaceCompanyListBean = JSONUtils.jsonString2Bean(result, GoodsPlaceCompanyListBean.class);
                if (goodsPlaceCompanyListBean != null) {
                    if (goodsPlaceCompanyListBean.getCompany_list() != null && goodsPlaceCompanyListBean.getCompany_list().size() > 0) {

                        for (int i = 0; i < goodsPlaceCompanyListBean.getCompany_list().size(); i++) {

                            GoodsScreenConditionBean goodsScreenConditionBean = new GoodsScreenConditionBean(String.valueOf(goodsPlaceCompanyListBean.getCompany_list().get(i).getCompany_id()), goodsPlaceCompanyListBean.getCompany_list().get(i).getCompany_name(), false);
                            mFirstConditionList.add(goodsScreenConditionBean);
                        }
                        mFirstConditionListAdapter.appendToList(mFirstConditionList);
                    }

                    if (goodsPlaceCompanyListBean.getMade_list() != null && goodsPlaceCompanyListBean.getMade_list().size() > 0) {

                        for (int i = 0; i < goodsPlaceCompanyListBean.getMade_list().size(); i++) {

                            GoodsScreenConditionBean goodsScreenConditionBean = new GoodsScreenConditionBean(String.valueOf(goodsPlaceCompanyListBean.getMade_list().get(i).getMade_place_id()), goodsPlaceCompanyListBean.getMade_list().get(i).getMade_place_name(), false);
                            mSecondConditionList.add(goodsScreenConditionBean);
                        }
                        mSecondConditionListAdapter.appendToList(mSecondConditionList);
                    }
                }

            }

            @Override
            public void onError(int code, String msg) {
                ToastUtils.show(mContext, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.show(mContext, e.getMessage());
            }
        });


    }

    onPositiveClickListener mPositiveClickListener;

    //确定按钮的监听
    public interface onPositiveClickListener {
        void positiveClick(View v, GoodsOriginAndCompanyBean condition);
    }

    public void setOnPositiveClickListener(onPositiveClickListener mPositiveClickListener) {
        this.mPositiveClickListener = mPositiveClickListener;
    }


    public static
    class ViewHolder {
        public View rootView;
        public View holderView;
        public TextView tv_first_condition;
        public ImageView iv_first_condition;
        public RecyclerView rlv_first_condition;
        public TextView tv_second_condition;
        public ImageView iv_second_condition;
        public RecyclerView rlv_second_condition;
        public Button btn_reset;
        public Button btn_positive;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tv_first_condition = (TextView) rootView.findViewById(R.id.tv_first_condition);
            this.holderView = (View) rootView.findViewById(R.id.view_holder);
            this.iv_first_condition = (ImageView) rootView.findViewById(R.id.iv_first_condition);
            this.rlv_first_condition = (RecyclerView) rootView.findViewById(R.id.rlv_first_condition);
            this.tv_second_condition = (TextView) rootView.findViewById(R.id.tv_second_condition);
            this.iv_second_condition = (ImageView) rootView.findViewById(R.id.iv_second_condition);
            this.rlv_second_condition = (RecyclerView) rootView.findViewById(R.id.rlv_second_condition);
            this.btn_reset = (Button) rootView.findViewById(R.id.btn_reset);
            this.btn_positive = (Button) rootView.findViewById(R.id.btn_positive);
        }

    }
}
