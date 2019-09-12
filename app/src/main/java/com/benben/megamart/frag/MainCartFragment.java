package com.benben.megamart.frag;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.LazyBaseFragments;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.GuessYouLikeListAdapter;
import com.benben.megamart.adapter.ShopCartListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.CartGoodsListBean;
import com.benben.megamart.bean.CartGoodsListInfoBean;
import com.benben.megamart.bean.GuessYouLikeGoodsListBean;
import com.benben.megamart.bean.SubmitOrderGoodsFormatBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.ui.GoodsDetailsActivity;
import com.benben.megamart.ui.LocationActivity;
import com.benben.megamart.ui.LoginActivity;
import com.benben.megamart.ui.SubmitOrderActivity;
import com.benben.megamart.utils.LoginCheckUtils;
import com.benben.megamart.utils.RxBus;
import com.benben.megamart.utils.StatusBarUtils;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

/**
 * Create by wanghk on 2019-05-27.
 * Describe:购物车页面
 */
public class MainCartFragment extends LazyBaseFragments {

    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;
    @BindView(R.id.rlv_cart_list)
    RecyclerView rlvCartList;
    @BindView(R.id.rlv_guess_you_like)
    RecyclerView rlvGuessYouLike;
    @BindView(R.id.stf_cart_layout)
    SmartRefreshLayout stfCartLayout;
    @BindView(R.id.tv_select_all)
    TextView tvSelectAll;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.llyt_total)
    LinearLayout llytTotal;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_freight_info)
    TextView tvFreightInfo;
    Unbinder unbinder;


    //购物车列表adapter
    private ShopCartListAdapter mCartListAdapter;
    //猜你喜欢列表adapter
    private GuessYouLikeListAdapter mGuessYouLikeListAdapter;
    //正在下拉刷新
    private boolean isRefreshing = false;

    //是否全选
    private boolean isSelectAll = false;
    //是否正在编辑
    private boolean isEdit = false;


    //总价格
    private double mTotalMoney = 0.0;
    //猜你喜欢列表
    private List<GuessYouLikeGoodsListBean> mGuessYouLikeGoodsList;
    //购物车商品列表
    private List<CartGoodsListBean> mCartGoodsList;

    public static MainCartFragment getInstance() {
        MainCartFragment sf = new MainCartFragment();
        return sf;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.frag_main_shop_cart, null);
        return mRootView;
    }

    @Override
    public void initView() {
        StatusBarUtils.setStatusBarColor(getActivity(), R.color.color_EC5413);
        mRootView.setPadding(0, StatusBarUtils.getStatusBarHeight(getActivity()), 0, 0);


        stfCartLayout.setEnableLoadMore(false);
        rlvGuessYouLike.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mGuessYouLikeListAdapter = new GuessYouLikeListAdapter(mContext);
        rlvGuessYouLike.setAdapter(mGuessYouLikeListAdapter);

        rlvCartList.setLayoutManager(new LinearLayoutManager(mContext));
        rlvCartList.setNestedScrollingEnabled(false);
        rlvCartList.setHasFixedSize(true);
        rlvCartList.setFocusable(false);

        mCartListAdapter = new ShopCartListAdapter(mContext);
        rlvCartList.setAdapter(mCartListAdapter);


        mCartListAdapter.setListener(new ShopCartListAdapter.ShopCartListener() {

            @Override
            public void onItemClick(int position, CartGoodsListBean data) {
                Intent goodsDetailsIntent = new Intent(mContext, GoodsDetailsActivity.class);
                goodsDetailsIntent.putExtra(Constants.EXTRA_KEY_GOODS_ID, String.valueOf(data.getGoods_id()));
                mContext.startActivityForResult(goodsDetailsIntent, Constants.CONFIRM_ORDER_REQUEST_CODE);
            }

            @Override
            public void onCartNumChange(int position, CartGoodsListBean data, int num) {
                editCartNum(position, data, num);
            }

            @Override
            public void onListSelectAll(boolean mIsSelectAll, double totalMoney) {
                isSelectAll = mIsSelectAll;
                tvSelectAll.setSelected(isSelectAll);
                mTotalMoney = totalMoney;
                tvTotalMoney.setText(mContext.getResources().getString(R.string.goods_price, String.valueOf(mTotalMoney)));

            }
        });

        //初始化下拉刷新
        initRefreshLayout();

        RxBus.getInstance().toObservable(Integer.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                        Log.e(Constants.WHK_TAG, "onNext: integer=" + integer);
                        if (integer == 101) {
                            mCartListAdapter.clear();
                            getCartGoodsList();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isPrepared()) {
            StatusBarUtils.setStatusBarColor(getActivity(), R.color.color_EC5413);
            mRootView.setPadding(0, StatusBarUtils.getStatusBarHeight(getActivity()), 0, 0);

        }
    }

    @Override
    public void initData() {
        if (!LoginCheckUtils.checkUserIsLogin(mContext)) {
            LoginCheckUtils.showYanZhengDialog(mContext, new MyDialogListener() {
                @Override
                public void onFirst() {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }

                @Override
                public void onSecond() {
                }
            });
        }
    }

    @Override
    protected void loadData() {

        //获取购物车商品列表数据
        getCartGoodsList();
        if (mGuessYouLikeGoodsList == null || mGuessYouLikeGoodsList.size() <= 0) {
            getGuessYouLikeList();
        }

    }


    //初始化下拉刷新
    private void initRefreshLayout() {
        stfCartLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefreshing = true;
                getCartGoodsList();
            }
        });
        stfCartLayout.setEnableLoadMore(false);
    }

    //获取购物车商品列表
    private void getCartGoodsList() {

        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.GET_CART_LIST)
                .json()
                .post().build().enqueue(getActivity(), new BaseCallBack<String>() {

            @Override
            public void onSuccess(String result, String msg) {
                Log.e(Constants.WHK_TAG, "onSuccess: ----获取购物车列表 =" + result);
                if (!isPrepared()) {
                    return;
                }

                CartGoodsListInfoBean cartGoodsListInfoBeans = JSONUtils.jsonString2Bean(result, CartGoodsListInfoBean.class);
                tvFreightInfo.setText(cartGoodsListInfoBeans.getFreight_text());
                formatGoodsListBean(cartGoodsListInfoBeans);

                if (mCartGoodsList != null && mCartGoodsList.size() > 0) {
                    mCartListAdapter.refreshList(mCartGoodsList);
                    mCartListAdapter.selectAll(isSelectAll);
                    rlvCartList.setVisibility(View.VISIBLE);
                    llytNoData.setVisibility(View.GONE);
                } else {
                    mCartListAdapter.clear();
                    mCartListAdapter.checkSelectAll();
                    rlvCartList.setVisibility(View.GONE);
                    llytNoData.setVisibility(View.VISIBLE);
                }
                if (isRefreshing) {
                    isRefreshing = false;
                    stfCartLayout.finishRefresh(true);
                }
                StyledDialogUtils.getInstance().dismissLoading();
            }

            @Override
            public void onError(int code, String msg) {
                Log.e(Constants.WHK_TAG, "onError: 获取购物车列表---" + msg);
                // toast(msg);
                if (isRefreshing) {
                    isRefreshing = false;
                    stfCartLayout.finishRefresh(true);
                }
                mCartListAdapter.clear();
                mCartListAdapter.checkSelectAll();
                StyledDialogUtils.getInstance().dismissLoading();
                rlvCartList.setVisibility(View.GONE);
                llytNoData.setVisibility(View.VISIBLE);
                DisplayMetrics dm = getResources().getDisplayMetrics();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llytNoData.getLayoutParams();
                layoutParams.height = dm.heightPixels - 800;
                llytNoData.setLayoutParams(layoutParams);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (isRefreshing) {
                    isRefreshing = false;
                    stfCartLayout.finishRefresh(true);
                }
                StyledDialogUtils.getInstance().dismissLoading();
                rlvCartList.setVisibility(View.GONE);
                llytNoData.setVisibility(View.VISIBLE);
                DisplayMetrics dm = getResources().getDisplayMetrics();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llytNoData.getLayoutParams();
                layoutParams.height = dm.heightPixels - 800;
                llytNoData.setLayoutParams(layoutParams);
            }
        });
    }

    //格式化商品列表
    private void formatGoodsListBean(CartGoodsListInfoBean cartGoodsListInfoBeans) {
        mCartGoodsList = new ArrayList<>();
        if (cartGoodsListInfoBeans.getGoods_list() != null && cartGoodsListInfoBeans.getGoods_list().size() > 0) {
            for (int i = 0; i < cartGoodsListInfoBeans.getGoods_list().size(); i++) {
                CartGoodsListBean cartGoodsListBean = new CartGoodsListBean();
                cartGoodsListBean.setCart_id(cartGoodsListInfoBeans.getGoods_list().get(i).getCart_id());
                cartGoodsListBean.setGoods_id(cartGoodsListInfoBeans.getGoods_list().get(i).getGoods_id());
                cartGoodsListBean.setGoods_img(cartGoodsListInfoBeans.getGoods_list().get(i).getGoods_img());
                cartGoodsListBean.setGoods_amount(cartGoodsListInfoBeans.getGoods_list().get(i).getGoods_amount());
                cartGoodsListBean.setGoods_name(cartGoodsListInfoBeans.getGoods_list().get(i).getGoods_name());
                cartGoodsListBean.setGoods_number(cartGoodsListInfoBeans.getGoods_list().get(i).getGoods_number());
                cartGoodsListBean.setSelect(false);
                cartGoodsListBean.setStatus(cartGoodsListInfoBeans.getGoods_list().get(i).getStatus());
                cartGoodsListBean.setPreferential_id(-1);
                mCartGoodsList.add(cartGoodsListBean);
            }
        }

        if (cartGoodsListInfoBeans.getPreferential_list() != null && cartGoodsListInfoBeans.getPreferential_list().size() > 0) {
            for (int i = 0; i < cartGoodsListInfoBeans.getPreferential_list().size(); i++) {

                for (int j = 0; j < cartGoodsListInfoBeans.getPreferential_list().get(i).getGoods_list().size(); j++) {
                    CartGoodsListBean cartGoodsListBean = new CartGoodsListBean();
                    cartGoodsListBean.setCart_id(cartGoodsListInfoBeans.getPreferential_list().get(i).getGoods_list().get(j).getCart_id());
                    cartGoodsListBean.setGoods_id(cartGoodsListInfoBeans.getPreferential_list().get(i).getGoods_list().get(j).getGoods_id());
                    cartGoodsListBean.setGoods_amount(cartGoodsListInfoBeans.getPreferential_list().get(i).getGoods_list().get(j).getGoods_amount());
                    cartGoodsListBean.setGoods_img(cartGoodsListInfoBeans.getPreferential_list().get(i).getGoods_list().get(j).getGoods_img());
                    cartGoodsListBean.setGoods_name(cartGoodsListInfoBeans.getPreferential_list().get(i).getGoods_list().get(j).getGoods_name());
                    cartGoodsListBean.setGoods_number(cartGoodsListInfoBeans.getPreferential_list().get(i).getGoods_list().get(j).getGoods_number());
                    cartGoodsListBean.setPreferential_price(cartGoodsListInfoBeans.getPreferential_list().get(i).getPreferential_amount());
                    cartGoodsListBean.setSelect(false);
                    cartGoodsListBean.setFirstPreferential(j == 0);
                    cartGoodsListBean.setStatus(cartGoodsListInfoBeans.getPreferential_list().get(i).getStatus());
                    cartGoodsListBean.setPreferential_id(cartGoodsListInfoBeans.getPreferential_list().get(i).getPreferential_id());
                    cartGoodsListBean.setPreferential_name(cartGoodsListInfoBeans.getPreferential_list().get(i).getPreferential_name());
                    mCartGoodsList.add(cartGoodsListBean);
                }

            }
        }

    }

    //弹出确定删除的dialog
    private void initDeleteDialog() {

        View dialog = View.inflate(mContext, R.layout.dialog_shop_cart_confirm_delete, null);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm);

        AlertDialog mDelAlertDialog = new AlertDialog.Builder(mContext)
                .setView(dialog)
                .create();
        mDelAlertDialog.show();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCartGoods();
                mDelAlertDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDelAlertDialog.dismiss();
            }
        });
    /*    Log.e(Constants.WHK_TAG, "initDeleteDialog: ");
        StyledDialog.buildIosAlert(getResources().getString(R.string.do_you_confirm_deletion), "", new MyDialogListener() {
            @Override
            public void onFirst() {

                //删除商品
                deleteCartGoods();
                StyledDialog.dismiss();
            }

            @Override
            public void onSecond() {
                StyledDialog.dismiss();
            }
        }).setBtnText(getResources().getString(R.string.shop_cart_positive), getResources().getString(R.string.shop_cart_cancel)).show();
*/
    }


    //获取猜你喜欢商品列表
    private void getGuessYouLikeList() {

        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_GUESS_GOODS)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取猜你喜欢商品列表----" + result);

                        StyledDialogUtils.getInstance().dismissLoading();
                        String noteJson = JSONUtils.getNoteJson(result, "goods_list");
                        mGuessYouLikeGoodsList = JSONUtils.jsonString2Beans(noteJson, GuessYouLikeGoodsListBean.class);
                        mGuessYouLikeListAdapter.refreshList(mGuessYouLikeGoodsList);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.show(mContext, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                    }
                });
    }

    //删除购物车商品
    private void deleteCartGoods() {
        // StyledDialogUtils.getInstance().loading(mContext);
        Log.e(Constants.WHK_TAG, "deleteCartGoods: 普通商品=" + mCartListAdapter.getSelectedGoodsIds(false) + "----组合商品" + mCartListAdapter.getSelectedGoodsIds(true));

        BaseOkHttpClient.newBuilder()
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("goods_id_list", mCartListAdapter.getSelectedGoodsIds(false))
                .addParam("preferential_id_list", mCartListAdapter.getSelectedGoodsIds(true))
                .url(NetUrlUtils.DELETE_CART_GOODS)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {

            @Override
            public void onSuccess(String result, String msg) {
                //还原UI样式，结束编辑
                finishEdit();
                StyledDialogUtils.getInstance().dismissLoading();
                getCartGoodsList();
            }

            @Override
            public void onError(int code, String msg) {
                toast(msg);
                StyledDialogUtils.getInstance().dismissLoading();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                toast(e.getMessage());
                StyledDialogUtils.getInstance().dismissLoading();
            }
        });
    }

    //修改购物车数量
    private void editCartNum(int position, CartGoodsListBean data, int num) {
        JSONArray normalGoodsListjson = new JSONArray();
        JSONArray composeGoodsListjson = new JSONArray();
        try {
            if (mCartGoodsList != null && mCartGoodsList.size() > 0) {

                for (CartGoodsListBean goodsListBean : mCartGoodsList) {
                    if (goodsListBean.getPreferential_id() == -1) {

                        JSONObject jo = new JSONObject();
                        jo.put("goods_id", goodsListBean.getGoods_id());
                        if (goodsListBean.isSelect()) {

                            jo.put("is_select", 1);

                        } else {
                            jo.put("is_select", 2);
                        }
                        jo.put("goods_number", goodsListBean.getGoods_number());
                        normalGoodsListjson.put(jo);
                    } else {

                        JSONObject jo = new JSONObject();
                        jo.put("preferential_id", goodsListBean.getPreferential_id());
                        if (goodsListBean.isSelect()) {
                            jo.put("is_select", 1);
                        } else {
                            jo.put("is_select", 2);
                        }
                        jo.put("preferential_number", goodsListBean.getGoods_number());
                        composeGoodsListjson.put(jo);
                    }


                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(Constants.WHK_TAG, "editCartNum: normalGoodsListjson= " + normalGoodsListjson + "----composeGoodsListjson=" + composeGoodsListjson);
        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .addParam("goods_list", normalGoodsListjson)
                .addParam("preferential_list", composeGoodsListjson)
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.UPDATE_GOODS_NUM)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {

            @Override
            public void onSuccess(String result, String msg) {
                Log.e(Constants.WHK_TAG, "onSuccess: 更新购物车数量----" + result);
                toast(msg);
                StyledDialogUtils.getInstance().dismissLoading();
                mCartListAdapter.updateCartNum(position, num);
            }

            @Override
            public void onError(int code, String msg) {
                toast(msg);
                getCartGoodsList();
                StyledDialogUtils.getInstance().dismissLoading();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                toast(e.getMessage());
                StyledDialogUtils.getInstance().dismissLoading();
            }
        });
    }


    //刷新页面
    public void refreshData(boolean selectAll) {
        isSelectAll = selectAll;
        if (isEdit) {
            finishEdit();
        }
        getCartGoodsList();
    }

    //标题栏右侧按钮被点击
    public void onRightBtnClicked() {
        //编辑和完成切换
        if (isEdit) {
            finishEdit();
        } else {
            startEdit();
        }
    }

    //开始编辑
    private void startEdit() {
        isEdit = true;
        tvDelete.setText(getResources().getString(R.string.shop_cart_complete));
        llytTotal.setVisibility(View.GONE);
        btnSubmit.setText(getResources().getString(R.string.shop_cart_delete));
    }

    //结束编辑
    private void finishEdit() {
        isEdit = false;
        tvDelete.setText(getResources().getString(R.string.shop_cart_delete));
        llytTotal.setVisibility(View.VISIBLE);
        btnSubmit.setText(getResources().getString(R.string.shop_cart_settlement));
    }

    private void toast(String msg) {
        ToastUtils.show(mContext, msg);
    }

    @OnClick({R.id.tv_location, R.id.tv_delete, R.id.tv_select_all, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_location://定位
                startActivity(new Intent(mContext, LocationActivity.class));
                break;
            case R.id.tv_delete://删除
                if (!LoginCheckUtils.checkUserIsLogin(mContext)) {
                    LoginCheckUtils.showYanZhengDialog(mContext, new MyDialogListener() {
                        @Override
                        public void onFirst() {
                            startActivity(new Intent(mContext, LoginActivity.class));
                        }

                        @Override
                        public void onSecond() {

                        }
                    });
                    return;
                }

                if (mCartListAdapter.getItemCount() <= 0) {
                    return;
                }
                if (!mCartListAdapter.checkSelected()) {
                    toast(getString(R.string.no_choose_goods));
                    return;
                }
                initDeleteDialog();
                break;
            case R.id.tv_select_all://全选
                isSelectAll = !isSelectAll;
                mCartListAdapter.selectAll(isSelectAll);
                break;
            case R.id.btn_submit://结算
                Log.e(Constants.WHK_TAG, "onViewClicked: isEdit =" + isEdit);

                if (mCartListAdapter.checkSelected()) {

                    if (mCartListAdapter.isHaveInvalidGoods()) {
                        toast(getString(R.string.have_invalid_goods));
                        return;
                    }
                    SubmitOrderGoodsFormatBean goodsFormatBean = new SubmitOrderGoodsFormatBean(mCartListAdapter.getSelectedGoodsList());
                    Intent intent = new Intent(mContext, SubmitOrderActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_GOODS_LIST, goodsFormatBean);
                    intent.putExtra("total_money",mTotalMoney);
                    startActivity(intent);

                } else {

                }

                break;
        }
    }

}
