package com.benben.megamart.ui;

import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.ScreenUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.LazyBaseFragments;
import com.benben.megamart.R;
import com.benben.megamart.adapter.HomeTabViewPagerAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.GoodsOriginAndCompanyBean;
import com.benben.megamart.bean.HomeTopTabEntityBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.frag.SearchGoodsListFragment;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.KeyBoardUtils;
import com.benben.megamart.utils.RxBus;
import com.benben.megamart.utils.StatusBarUtils;
import com.benben.megamart.widget.ScreenPopupWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-06-01.
 * Describe:搜索商品列表activity
 */
public class SearchGoodsListActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.edt_goods_search)
    EditText edtGoodsSearch;
    @BindView(R.id.xTablayout)
    XTabLayout xTablayout;
    @BindView(R.id.llyt_synthetical)
    LinearLayout llytSynthetical;
    @BindView(R.id.tv_sales_volume)
    TextView tvSalesVolume;
    @BindView(R.id.llyt_price)
    LinearLayout llytPrice;
    @BindView(R.id.llyt_screen)
    LinearLayout llytScreen;
    @BindView(R.id.vp_goods_list)
    ViewPager vpGoodsList;
    @BindView(R.id.iv_price_sort)
    ImageView ivPriceSort;
    @BindView(R.id.tv_synthetical)
    TextView tvSynthetical;
    @BindView(R.id.tv_price_condition)
    TextView tvPriceCondition;
    @BindView(R.id.tv_screen)
    TextView tvScreen;


    //价格排序
    private boolean isMaxPrice = false;
    //tab数据list
    private List<HomeTopTabEntityBean> mTabEntities = new ArrayList<>();
    //商品产地和所属公司bean
    private GoodsOriginAndCompanyBean mOriginAndCompanyBean;
    //搜索关键字
    private String mKeyWord;
    //分类id
    private int mCateId = 0;
    private int mCateParentId = 0;
    //rxbus 传值工具类
    private RxBus mRxBus;
    private ScreenPopupWindow mScreenPopupWindow;
    private List<LazyBaseFragments> mFragmentList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_good_list;
    }

    @Override
    protected void initData() {
        mCateId = getIntent().getIntExtra(Constants.EXTRA_KEY_CATE_ID, 0);
        mCateParentId = getIntent().getIntExtra(Constants.EXTRA_KEY_CATE_PARENT_ID, 0);
        mKeyWord = getIntent().getStringExtra(SearchGoodsRecordActivity.KEY_WORD);
        if (!StringUtils.isEmpty(mKeyWord)) {
            edtGoodsSearch.setText(mKeyWord);
        }
        mRxBus = RxBus.getInstance();
        mOriginAndCompanyBean = new GoodsOriginAndCompanyBean("", "");

        initTabEntity();
        //初始化筛选对话框
        initScreenPopupWindow();

        edtGoodsSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mKeyWord = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //软键盘点击搜索
        edtGoodsSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SearchGoodsListFragment lazyBaseFragments = (SearchGoodsListFragment) mFragmentList.get(vpGoodsList.getCurrentItem());
                    lazyBaseFragments.getGoodsList(false,mKeyWord);
                    KeyBoardUtils.hideKeyboard(edtGoodsSearch);
                    return true;
                }
                return false;
            }
        });
    }


    //初始化tab数据
    private void initTabEntity() {
        // StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .addParam("cate_id", mCateParentId)
                .url(NetUrlUtils.GET_NAVI_GOODS_LIST)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {


            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();

                String noteJson = JSONUtils.getNoteJson(result, "cate_list");
                mTabEntities = JSONUtils.jsonString2Beans(noteJson, HomeTopTabEntityBean.class);
                //增加全部商品的tab
                HomeTopTabEntityBean hotBlockTabEntity = new HomeTopTabEntityBean();
                hotBlockTabEntity.setCate_id(mCateParentId);
                hotBlockTabEntity.setCate_name(getString(R.string.all_goods));
                mTabEntities.add(0, hotBlockTabEntity);

                for (int i = 0; i < mTabEntities.size(); i++) {
                    xTablayout.addTab(xTablayout.newTab().setText(mTabEntities.get(i).getCate_name()));
                }

                initFragment();
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

    private void initFragment() {
        //添加fragment
        mFragmentList = new ArrayList<>();
        for (int i = 0; i < mTabEntities.size(); i++) {
            mFragmentList.add(new SearchGoodsListFragment());

        }


        vpGoodsList.setAdapter(new HomeTabViewPagerAdapter(getSupportFragmentManager(), mTabEntities, mFragmentList));
        xTablayout.setupWithViewPager(vpGoodsList);
        for (int i = 0; i < mTabEntities.size(); i++) {
            if (mCateId == mTabEntities.get(i).getCate_id()) {
                vpGoodsList.setCurrentItem(i);
                return;
            }
        }


    }

    @OnClick({R.id.rl_back, R.id.llyt_synthetical, R.id.tv_sales_volume, R.id.llyt_price, R.id.llyt_screen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.llyt_synthetical://综合
                tvSalesVolume.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvPriceCondition.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvSynthetical.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                ivPriceSort.setImageResource(R.mipmap.icon_pre_select);
                mRxBus.post(0);
                break;
            case R.id.tv_sales_volume://销量
                mRxBus.post(1);
                tvSalesVolume.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvPriceCondition.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvSynthetical.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                ivPriceSort.setImageResource(R.mipmap.icon_pre_select);
                break;
            case R.id.llyt_price://价格

                tvSalesVolume.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvPriceCondition.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvSynthetical.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                if (isMaxPrice) {
                    mRxBus.post(2);
                    isMaxPrice = false;
                    ivPriceSort.setImageResource(R.mipmap.icon_xialas);
                } else {
                    mRxBus.post(3);
                    isMaxPrice = true;
                    ivPriceSort.setImageResource(R.mipmap.icon_shanglas);
                }

                break;
            case R.id.llyt_screen://筛选
                if (mScreenPopupWindow != null) {
                    ScreenUtils.closeKeybord(edtGoodsSearch, mContext);
                    mScreenPopupWindow.showAtLocation(getWindow().getDecorView().getRootView(), Gravity.RIGHT, 1000, 0);
                    mScreenPopupWindow.backgroundAlpha(0.5f);
                }
                break;
        }
    }


    //筛选对话框
    private void initScreenPopupWindow() {

        mScreenPopupWindow = new ScreenPopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, mContext);
        mScreenPopupWindow.setOnPositiveClickListener((v, condition) -> {
            mOriginAndCompanyBean = condition;
            //  toast(mOriginAndCompanyBean.getGoods_origin_id() + mOriginAndCompanyBean.getGoods_company_id());
            mRxBus.post(mOriginAndCompanyBean);
        });
        //popwindow消失的监听 屏幕亮度调回正常
        mScreenPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mScreenPopupWindow.backgroundAlpha(1f);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ScreenUtils.isShowKeyboard(mContext, edtGoodsSearch)) {
            ScreenUtils.closeKeybord(edtGoodsSearch, mContext);
        }
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }

}
