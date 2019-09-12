package com.benben.megamart.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.commoncore.widget.TopProgressWebView;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MainActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.GoodsDetailsVideoPictureAdapter;
import com.benben.megamart.adapter.GuessYouLikeListAdapter;
import com.benben.megamart.adapter.OtherSpecListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.AttrOtherListBean;
import com.benben.megamart.bean.GoodsCartNumberBean;
import com.benben.megamart.bean.GoodsDetailsInfoBean;
import com.benben.megamart.bean.GoodsDetailsVideoPictureBean;
import com.benben.megamart.bean.GuessYouLikeGoodsListBean;
import com.benben.megamart.bean.NotesBuyInfoBean;
import com.benben.megamart.bean.TabEntityBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.LoginCheckUtils;
import com.benben.megamart.utils.RecyclerViewPageChangeListenerHelper;
import com.benben.megamart.utils.RxBus;
import com.benben.megamart.utils.StatusBarUtils;
import com.benben.megamart.widget.GoodsDetailsNumberView;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.hyphenate.helper.HyphenateHelper;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-06-03.
 * Describe:商品详情页面
 */
public class GoodsDetailsActivity extends BaseActivity implements ViewTreeObserver.OnGlobalLayoutListener {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_collection_count)
    TextView tvCollectionCount;
    @BindView(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @BindView(R.id.tv_sell_count)
    TextView tvSellCount;
    @BindView(R.id.tv_purchase_instructions)
    TextView tvPurchaseInstructions;
    @BindView(R.id.rlv_other_specifications)
    RecyclerView rlvOtherSpecifications;
    @BindView(R.id.wv_goods_details)
    WebView wvGoodsDetails;
    @BindView(R.id.rlv_guess_you_like)
    RecyclerView rlvGuessYouLike;
    @BindView(R.id.tab_goods_details)
    CommonTabLayout tabGoodsDetails;
    @BindView(R.id.number_view)
    GoodsDetailsNumberView numberView;
    @BindView(R.id.rlyt_goods_info)
    RelativeLayout rlytGoodsInfo;
    @BindView(R.id.llyt_bottom)
    LinearLayout llytBottom;
    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;
    @BindView(R.id.rlv_goods_videopic)
    RecyclerView rlvGoodsVideopic;
    @BindView(R.id.tv_select_video)
    TextView tvSelectVideo;
    @BindView(R.id.tv_select_picture)
    TextView tvSelectPicture;
    @BindView(R.id.tv_current_picture_position)
    TextView tvCurrentPicturePosition;
    @BindView(R.id.llyt_navigation)
    LinearLayout llytNavigation;

    //tab数据
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    //底部导航栏的高度
    private int mNavigationBarHeight = 0;
    //猜你喜欢列表adapter
    private GuessYouLikeListAdapter mGuessYouLikeListAdapter;
    //其他规格列表adapter
    private OtherSpecListAdapter mOtherSpecListAdapter;
    //是否选中视频
    private boolean isSelectVideo = true;
    //是否选中照片
    private boolean isSelectPicture = false;
    //视频和图片列表的adapter
    private GoodsDetailsVideoPictureAdapter mVideoPictureAdapter;
    //视频和图片列表的list
    private ArrayList<GoodsDetailsVideoPictureBean> mVideoPictureInfoList;
    //videoPlayer播放辅助类
    private GSYVideoHelper mSmallVideoHelper;
    //videoPlayer构建
    private GSYVideoHelper.GSYVideoHelperBuilder mSmallVideoHelperBuilder;
    //商品id
    private String mGoodsId;
    //用户id
    private String mUserId;
    //收藏状态
    boolean isCollection = false;
    //商品详情bean
    private GoodsDetailsInfoBean mGoodsDetailsInfoBean;
    //购买须知的HTML文本
    private String mNotesBuyInfoContent = "";
    private boolean isHaveVideo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_details;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initData() {
        mGoodsId = getIntent().getStringExtra(Constants.EXTRA_KEY_GOODS_ID);
        mUserId = MegaMartApplication.mPreferenceProvider.getUId();
        // ImageUtils.getPic("https://gd4.alicdn.com/imgextra/i4/0/O1CN01mSIva125BYFDWwl6G_!!0-item_pic.jpg", ivGoodsImg, mContext);

        initTabData();
        initWebView();

        rlvGuessYouLike.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mGuessYouLikeListAdapter = new GuessYouLikeListAdapter(mContext);
        rlvGuessYouLike.setAdapter(mGuessYouLikeListAdapter);

        rlvOtherSpecifications.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mOtherSpecListAdapter = new OtherSpecListAdapter(mContext);
        rlvOtherSpecifications.setAdapter(mOtherSpecListAdapter);

        //初始化视频播放
        initVideoPlayer();
        LinearLayoutManager mVideoPictureLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rlvGoodsVideopic.setLayoutManager(mVideoPictureLinearLayoutManager);
        mVideoPictureAdapter = new GoodsDetailsVideoPictureAdapter(mContext, mSmallVideoHelper, mSmallVideoHelperBuilder);
        rlvGoodsVideopic.setAdapter(mVideoPictureAdapter);
        //自动修复item位置
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rlvGoodsVideopic);
        rlvGoodsVideopic.addOnScrollListener(new RecyclerViewPageChangeListenerHelper(pagerSnapHelper, new RecyclerViewPageChangeListenerHelper.OnPageChangeListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mVideoPictureInfoList != null && mVideoPictureInfoList.size() > 0) {

                    if (isHaveVideo) {
                        if (position != (mVideoPictureInfoList.size() - 1)) { //滑到图片

                            isSelectVideo = false;
                            isSelectPicture = true;
                            tvSelectVideo.setBackgroundResource(R.drawable.shape_goods_details_video_pic_btn_unselected_bg);
                            tvSelectPicture.setBackgroundResource(R.drawable.shape_goods_details_video_pic_btn_selected_bg);
                            tvCurrentPicturePosition.setText((position + 1) + "/" + (mVideoPictureInfoList.size() - 1));
                            tvCurrentPicturePosition.setVisibility(View.VISIBLE);
                        } else { //滑到视频
                            isSelectVideo = true;
                            isSelectPicture = false;

                            tvSelectVideo.setBackgroundResource(R.drawable.shape_goods_details_video_pic_btn_selected_bg);
                            tvSelectPicture.setBackgroundResource(R.drawable.shape_goods_details_video_pic_btn_unselected_bg);
                            tvCurrentPicturePosition.setVisibility(View.GONE);
                        }
                    } else {
                        tvCurrentPicturePosition.setVisibility(View.VISIBLE);
                        tvCurrentPicturePosition.setText((position + 1) + "/" + (mVideoPictureInfoList.size()));

                    }
                }
            }
        }));

        //加减控件监听

        numberView.setOnNumberChangeListener(new GoodsDetailsNumberView.OnNumberChangeListener() {
            @Override
            public void onChange(boolean isAdd) {
                if (checkLogin()) return;
                if (isAdd) {
                    addOrDeleteCart(1);
                } else {
                    addOrDeleteCart(2);
                }
            }
        });

        //跳转对应的页面
        tabGoodsDetails.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                jumpOtherPage(position);
            }

            @Override
            public void onTabReselect(int position) {
                jumpOtherPage(position);
            }
        });

        //不占用底部导航栏
        //setMarginNavigationBar(mNavigationBarHeight);

        //获取商品详情信息
        getGoodsDetailInfo();
        //获取猜你喜欢商品列表
        getGuessYouLikeList();
        //获取其他规格
        getAttrOtherList();
        //获取购物车指定商品数量
        getCartGoodsNumber();
        //获取购买须知的html文本
        getNotesBuyInfo(false);
    }

    //获取购物车指定商品数量
    private void getCartGoodsNumber() {

        BaseOkHttpClient.newBuilder()
                .addParam("user_id", mUserId)
                .addParam("goods_id", mGoodsId)
                .url(NetUrlUtils.GET_CART_GOODS_NUMBER)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取购物车指定商品数量----" + result);
                        StyledDialogUtils.getInstance().dismissLoading();
                        GoodsCartNumberBean goodsCartNumberBean = JSONUtils.jsonString2Bean(result, GoodsCartNumberBean.class);
                        numberView.setNum(goodsCartNumberBean.getNumber());

                    }

                    @Override
                    public void onError(int code, String msg) {
                        // ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        // ToastUtils.show(mContext, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                    }
                });
    }

    //获取商品详情信息
    private void getGoodsDetailInfo() {
        // StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_GOODS_INFO)
                .addParam("user_id", mUserId)
                .addParam("goods_id", mGoodsId)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取商品详情信息----" + result);

                        StyledDialogUtils.getInstance().dismissLoading();
                        String noteJson = JSONUtils.getNoteJson(result, "goods_info");
                        mGoodsDetailsInfoBean = JSONUtils.jsonString2Bean(noteJson, GoodsDetailsInfoBean.class);
                        tvGoodsName.setText(mGoodsDetailsInfoBean.getGoods_name());
                        tvGoodsPrice.setText(getResources().getString(R.string.goods_price, mGoodsDetailsInfoBean.getGoods_price()));
                        tvSellCount.setText(getResources().getString(R.string.sell_count, mGoodsDetailsInfoBean.getSale_count()));
                        tvCollectionCount.setText(getResources().getString(R.string.collection_count, mGoodsDetailsInfoBean.getCollection_count()));
                        StringBuilder sb = new StringBuilder();
                        sb.append(TopProgressWebView.getHtmlData(mGoodsDetailsInfoBean.getGoods_content()));
                        wvGoodsDetails.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
                        //1 收藏 0 未收藏
                        isCollection = mGoodsDetailsInfoBean.getIs_collection() != 0;
                        changeCollectionStatus(isCollection);
                        initGoodsVideoAndPicture();

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

    //改变收藏状态
    private void changeCollectionStatus(boolean isCollection) {
        if (isCollection) {
            tvCollectionCount.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.icon_shoucang_pre), null, null, null);
        } else {
            tvCollectionCount.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.icon_shoucang), null, null, null);
        }
    }

    //初始化视频播放
    private void initVideoPlayer() {
        //创建小窗口帮助类
        mSmallVideoHelper = new GSYVideoHelper(this);
        //配置
        mSmallVideoHelperBuilder = new GSYVideoHelper.GSYVideoHelperBuilder();
        mSmallVideoHelperBuilder
                .setHideStatusBar(true)
                .setNeedLockFull(true)
                .setCacheWithPlay(true)
                .setShowFullAnimation(false)
                .setRotateViewAuto(false)
                .setLockLand(true);


        mSmallVideoHelper.setGsyVideoOptionBuilder(mSmallVideoHelperBuilder);
    }

    //获取商品视频和图片
    private void initGoodsVideoAndPicture() {
        //如果没有视频资源 隐藏导航栏 只显示图片
        mVideoPictureInfoList = new ArrayList<>();
        for (int i = 0; i < mGoodsDetailsInfoBean.getGoods_img().size(); i++) {
            GoodsDetailsVideoPictureBean goodsDetailsVideoPictureBean = new GoodsDetailsVideoPictureBean();
            goodsDetailsVideoPictureBean.setImage_url(mGoodsDetailsInfoBean.getGoods_img().get(i));
            mVideoPictureInfoList.add(goodsDetailsVideoPictureBean);
        }

        if (StringUtils.isEmpty(mGoodsDetailsInfoBean.getGoods_video())) {
            llytNavigation.setVisibility(View.GONE);
            isHaveVideo = false;
        } else {
            llytNavigation.setVisibility(View.VISIBLE);
            isHaveVideo = true;
            GoodsDetailsVideoPictureBean goodsDetailsVideoBean = new GoodsDetailsVideoPictureBean();
            goodsDetailsVideoBean.setVideo_url(mGoodsDetailsInfoBean.getGoods_video());
            mVideoPictureInfoList.add(goodsDetailsVideoBean);
        }
        if (isHaveVideo) {
            tvCurrentPicturePosition.setText(1 + "/" + (mVideoPictureInfoList.size() - 1));
        } else {
            tvCurrentPicturePosition.setText(1 + "/" + (mVideoPictureInfoList.size()));
        }
        mVideoPictureAdapter.appendToList(mVideoPictureInfoList);
    }

    //初始化webview
    private void initWebView() {

        // String imageUrl = "<h1>重回汉唐萦妆汉服女原创中国风春装日常对襟齐胸襦裙套装非 古装</h1><h1>重回汉唐萦妆汉服女原创中国风春装日常对襟齐胸襦裙套装非 古装</h1><h1>重回汉唐萦妆汉服女原创中国风春装日常对襟齐胸襦裙套装非 古装</h1><p><img src=\"http://live.zzqcnz.com/uploads/images/20190412/6616f700bdf40c9428984a9707f03c36.jpg\" alt=\"O1CN016Hwb3o1vZgxjJufTw_!!2835046187\" style=\"max-width:100%;\"></p><p><br></p>";

        //初始化WebView 加载商品详情图片
        WebSettings settings = wvGoodsDetails.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);
        settings.setSupportMultipleWindows(true);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setDisplayZoomControls(false);
        wvGoodsDetails.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

    }

    //获取其他规格
    private void getAttrOtherList() {

        BaseOkHttpClient.newBuilder()
                .addParam("goods_id", mGoodsId)
                .url(NetUrlUtils.GET_ATTR_OTHER_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取其他规格----" + result);

                        StyledDialogUtils.getInstance().dismissLoading();
                        String noteJson = JSONUtils.getNoteJson(result, "goods_list");
                        List<AttrOtherListBean> attrOtherListBeans = JSONUtils.jsonString2Beans(noteJson, AttrOtherListBean.class);
                        mOtherSpecListAdapter.appendToList(attrOtherListBeans);
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
                        List<GuessYouLikeGoodsListBean> guessYouLikeGoodsListBeans = JSONUtils.jsonString2Beans(noteJson, GuessYouLikeGoodsListBean.class);
                        mGuessYouLikeListAdapter.appendToList(guessYouLikeGoodsListBeans);
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

    //计算虚拟按键的高度 setmargin
    private void setMarginNavigationBar(int height) {
        RelativeLayout.LayoutParams bottomlayoutParams = (RelativeLayout.LayoutParams) llytBottom.getLayoutParams();
        RelativeLayout.LayoutParams scrollViewLayoutParams = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
        bottomlayoutParams.setMargins(0, 0, 0, height);
        scrollViewLayoutParams.setMargins(0, 0, 0, height);
        llytBottom.setLayoutParams(bottomlayoutParams);
        scrollView.setLayoutParams(scrollViewLayoutParams);
    }

    //跳转到首页 购物车  客服页面
    private void jumpOtherPage(int position) {
        switch (position) {
            case 0://首页
                startActivity(new Intent(mContext, MainActivity.class));
                RxBus.getInstance().post(0);
                finish();
                break;
            case 1://联系客服
                if (checkLogin()) return;
                HyphenateHelper.callServiceIM(mContext, MegaMartApplication.mPreferenceProvider.getHuanXinName(), MegaMartApplication.mPreferenceProvider.getHuanXinPwd(), MegaMartApplication.mPreferenceProvider.getPhoto());
                break;
            case 2://购物车

                startActivity(new Intent(mContext, MainActivity.class));
                RxBus.getInstance().post(2);
                finish();
                break;
        }
    }


    @OnClick({R.id.rl_back, R.id.tv_select_video, R.id.tv_select_picture, R.id.tv_collection_count, R.id.tv_purchase_instructions})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_purchase_instructions: //购买须知
                if (StringUtils.isEmpty(mNotesBuyInfoContent)) {
                    getNotesBuyInfo(true);
                } else {
                    jumpNotesBuyInfo();
                }
                break;
            case R.id.tv_collection_count://收藏
                if (checkLogin()) return;
                if (isCollection) {
                    updateUsersCollect(2);
                } else {
                    updateUsersCollect(1);
                }
                break;
            case R.id.tv_select_video://切换到视频
                if (isSelectVideo) {
                    return;
                } else {
                    isSelectVideo = true;
                    isSelectPicture = false;
                    tvSelectVideo.setBackgroundResource(R.drawable.shape_goods_details_video_pic_btn_selected_bg);
                    tvSelectPicture.setBackgroundResource(R.drawable.shape_goods_details_video_pic_btn_unselected_bg);
                    rlvGoodsVideopic.scrollToPosition(mVideoPictureInfoList.size() - 1);
                    tvCurrentPicturePosition.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_select_picture://切换到图片
                if (isSelectPicture) {
                    return;
                } else {
                    isSelectVideo = false;
                    isSelectPicture = true;
                    tvSelectVideo.setBackgroundResource(R.drawable.shape_goods_details_video_pic_btn_unselected_bg);
                    tvSelectPicture.setBackgroundResource(R.drawable.shape_goods_details_video_pic_btn_selected_bg);

                    rlvGoodsVideopic.scrollToPosition(0);
                    if (mVideoPictureInfoList != null && mVideoPictureInfoList.size() > 0) {
                        tvCurrentPicturePosition.setText("1/" + (mVideoPictureInfoList.size() - 1));
                    }
                    tvCurrentPicturePosition.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    //跳转购买须知
    private void jumpNotesBuyInfo() {
        Intent intent = new Intent(mContext, CommonWebViewActivity.class);
        intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_URL, mNotesBuyInfoContent);
        intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_TITLE, mContext.getResources().getString(R.string.purchase_instructions));
        intent.putExtra(Constants.EXTRA_KEY_IS_URL, false);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //获取购买须知
    private void getNotesBuyInfo(boolean isJumpNotesBuyInfo) {

        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.GET_NOTES_BUY_INFO)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                NotesBuyInfoBean notesBuyInfoBean = JSONUtils.jsonString2Bean(result, NotesBuyInfoBean.class);
                mNotesBuyInfoContent = notesBuyInfoBean.getContent();
                if (isJumpNotesBuyInfo) {
                    jumpNotesBuyInfo();
                }
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

    /**
     * 添加/取消收藏
     *
     * @param collect_type 操作状态 1:添加收藏 2:取消收藏
     */
    private void updateUsersCollect(int collect_type) {
        StyledDialogUtils.getInstance().loading(mContext);

        BaseOkHttpClient.newBuilder()
                .addParam("user_id", mUserId)
                .addParam("goods_id", mGoodsId)
                .addParam("collect_type", collect_type)
                .url(NetUrlUtils.UPDATE_USERS_COLLECT)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        StyledDialogUtils.getInstance().dismissLoading();
                        toast(msg);
                        getGoodsDetailInfo();
                        isCollection = collect_type == 1;
                        changeCollectionStatus(isCollection);

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


    //初始化底部tab数据
    private void initTabData() {
        mTabEntities.add(new TabEntityBean(getResources().getString(R.string.main_home), R.mipmap.icon_shouye, R.mipmap.icon_shouye));
        mTabEntities.add(new TabEntityBean(getResources().getString(R.string.custom_services), R.mipmap.icon_kefu, R.mipmap.icon_kefu));
        mTabEntities.add(new TabEntityBean(getResources().getString(R.string.main_shop_cart), R.mipmap.tab_gouwuche, R.mipmap.tab_gouwuche));
        tabGoodsDetails.setTabData(mTabEntities);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(mContext,R.color.black);
//        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
//        mNavigationBarHeight = StatusBarUtils.getNavigationBarHeight(mContext);
    }


    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
        getWindow().getDecorView().getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().getRootView().getViewTreeObserver().addOnGlobalLayoutListener(this);
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }


    /**
     * 添加/删除购物车商品
     *
     * @param activity        上下文
     * @param goods_id        商品id     没有则传0
     * @param user_id         用户id
     * @param preferential_id 组合商品id  没有则传0
     * @param operate_type    操作类型 1 添加 2 删除
     */
    public static void addCart(Activity activity, int goods_id, String user_id, int preferential_id, int operate_type) {


        StyledDialogUtils.getInstance().loading(activity);
        BaseOkHttpClient.newBuilder()
                .addParam("user_id", user_id)
                .addParam("goods_id", goods_id)
                .addParam("preferential_id", preferential_id)
                .addParam("operate_type", operate_type)
                .url(NetUrlUtils.ADD_CART_GOODS)
                .json()
                .post().build().enqueue(activity, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 添加/删除购物车商品----" + result);

                        StyledDialogUtils.getInstance().dismissLoading();
                        ToastUtils.show(activity, msg);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        ToastUtils.show(activity, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.show(activity, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                    }
                });
    }

    /**
     * 添加/删除购物车商品
     *
     * @param operate_type 操作类型 1 添加 2 删除
     */
    public void addOrDeleteCart(int operate_type) {

        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .addParam("user_id", mUserId)
                .addParam("goods_id", mGoodsId)
                .addParam("preferential_id", 0)
                .addParam("operate_type", operate_type)
                .url(NetUrlUtils.ADD_CART_GOODS)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 商品详情页 添加/删除购物车商品----" + result);

                        StyledDialogUtils.getInstance().dismissLoading();
                        ToastUtils.show(mContext, msg);
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


    public boolean checkLogin() {
        if (!LoginCheckUtils.checkUserIsLogin(mContext)) {
            LoginCheckUtils.showYanZhengDialog(mContext, new MyDialogListener() {
                @Override
                public void onFirst() {
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }

                @Override
                public void onSecond() {

                }
            });
            return true;

        }
        return false;

    }


    @Override
    public void onGlobalLayout() {
        if (StatusBarUtils.isNavigationBarShow(this)) {
//    虚拟按键显示的时候的处理
            //setMarginNavigationBar(mNavigationBarHeight);
        } else {
//            虚拟按键隐藏时的处理
           // setMarginNavigationBar(0);
        }
    }
}




