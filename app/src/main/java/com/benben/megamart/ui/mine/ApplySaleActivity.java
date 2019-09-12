package com.benben.megamart.ui.mine;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.GridImageAdapter;
import com.benben.megamart.adapter.OrderGoodsListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.GoodsListBean;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.FullyGridLayoutManager;
import com.benben.megamart.utils.RxBus;
import com.benben.megamart.utils.StatusBarUtils;
import com.benben.megamart.widget.CustomListView;
import com.benben.megamart.widget.CustomRecyclerView;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/14
 * Time: 11:30
 */
public class ApplySaleActivity extends BaseActivity {


    @BindView(R.id.tv_replace)
    TextView tvReplace;
    @BindView(R.id.edt_feedback)
    EditText edtFeedback;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.rv_photo)
    CustomRecyclerView rvPhoto;
    @BindView(R.id.lv_goods)
    CustomListView lvGoods;

    //图片选择的参数
    private List<LocalMedia> mSelectMedia = new ArrayList<>();
    private boolean isShow = true;
    private boolean isPreviewVideo = true;
    private boolean isCompress = true;
    private boolean isCheckNumMode = false;

    private List<String> mShowList = new ArrayList<>();//显示退换货

    private int mAfterType = 1;//类型:1=换货,2=退货,3=其他

    private String mOrderId = "";//订单id

    private GridImageAdapter mAdapter;//选择图片的适配器

    private File[] mFiles;//需要上传的问题件

    private JSONArray mArray = new JSONArray();//上传到服务器图片地址

    private List<GoodsListBean> mBean;//商品的展示
    private OrderGoodsListAdapter mGoodsAdapter;//商品的适配器

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_sale;
    }

    @Override
    protected void initData() {
        mOrderId = getIntent().getStringExtra("orderId");
        mBean = (List<GoodsListBean>) getIntent().getSerializableExtra("bean");
        initTitle("" + getString(R.string.order_apply_callback));
        FullyGridLayoutManager fullyGridLayoutManager = new FullyGridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        rvPhoto.setLayoutManager(fullyGridLayoutManager);
        rvPhoto.canScrollVertically(0);
        mAdapter = new GridImageAdapter(this, onAddPicClickListener);
        rvPhoto.setAdapter(mAdapter);
        if (mBean != null) {
            mGoodsAdapter = new OrderGoodsListAdapter(this, mBean);
            lvGoods.setAdapter(mGoodsAdapter);
        }
    }

    @OnClick({R.id.tv_replace, R.id.btn_submit,R.id.rl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                RxBus.getInstance().post(201);
                super.onBackPressed();
                break;
            case R.id.btn_submit:
                if (mSelectMedia.size() > 0) {
                    uploadPhoto();
                } else {
                    apply();
                }
                break;
            case R.id.tv_replace:
                mShowList.clear();
                mShowList.add(getResources().getString(R.string.order_replace));
                mShowList.add(getResources().getString(R.string.return_goods));
                mShowList.add(getResources().getString(R.string.other));
                StyledDialog.init(ApplySaleActivity.this);
                StyledDialog.buildBottomItemDialog(mShowList, getResources().getString(R.string.shop_cart_cancel), new MyItemDialogListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        tvReplace.setText("" + mShowList.get(position));
                        mAfterType = position + 1;
                    }
                }).show();
                break;
        }
    }

    /**
     * 上传图片
     */
    private void uploadPhoto() {
        String content = edtFeedback.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            toast(getResources().getString(R.string.please_enter_reason));
            return;
        }
        mFiles = new File[mSelectMedia.size()];
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.Builder builder = BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.IMAGE_UPLOAD);
        for (int i = 0; i < mSelectMedia.size(); i++) {
            mFiles[i] = new File(mSelectMedia.get(i).getCompressPath());
            builder.addFile("images[]", "" + mFiles[i].getName(), mFiles[i]);
        }
        builder.post()
                .build()
                .enqueue(this, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        StyledDialogUtils.getInstance().dismissLoading();
                        try {
                            JSONObject object = new JSONObject(result);
                            if (object.has("thumb_url") && !object.isNull("thumb_url")) {
                                JSONArray array = object.getJSONArray("thumb_url");
                                for (int i = 0; i < array.length(); i++) {
                                    mArray.put(array.getString(i));
                                }
                            }
                            apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        StyledDialogUtils.getInstance().dismissLoading();
                        toast(msg);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        StyledDialogUtils.getInstance().dismissLoading();
                        toast(getResources().getString(R.string.server_exception));
                    }
                });
    }

    //申请售后
    private void apply() {
        String content = edtFeedback.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            toast(getResources().getString(R.string.please_enter_reason));
            return;
        }
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.APPLY_SALE)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("order_id", "" + mOrderId)
                .addParam("after_content", "" + content)
                .addParam("after_type", "" + mAfterType)
                .addParam("after_imgs", mArray)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(msg);
                RxBus.getInstance().post(201);
                finish();
            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
            }
        });
    }

    /**
     * 删除图片回调接口
     */
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    // 进入相册
                    FunctionConfig config = new FunctionConfig();
                    config.setType(1);
                    config.setCompress(isCompress);
                    config.setEnablePixelCompress(true);
                    config.setEnableQualityCompress(true);
                    config.setMaxSelectNum(9);
                    config.setSelectMode(FunctionConfig.MODE_MULTIPLE);
                    config.setShowCamera(isShow);
                    config.setEnablePreview(true);
                    config.setEnableCrop(false);
                    config.setPreviewVideo(isPreviewVideo);
                    config.setCheckNumMode(isCheckNumMode);
                    config.setCompressQuality(100);
                    config.setImageSpanCount(4);
                    config.setSelectMedia(mSelectMedia);
                    config.setCompressFlag(1);
                    // 先初始化参数配置，在启动相册
                    PictureConfig.init(config);
                    PictureConfig.getPictureConfig().openPhoto(ApplySaleActivity.this, resultCallback);
                    break;
                case 1:
                    // 删除图片
                    mSelectMedia.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    break;
            }
        }
    };

    /**
     * 获取图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            if (resultList.size() != 0) {
                mSelectMedia = resultList;
                if (mSelectMedia != null) {
                    mAdapter.setList(mSelectMedia);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }

    @Override
    public void onBackPressed() {
        RxBus.getInstance().post(201);
        super.onBackPressed();
    }
}
