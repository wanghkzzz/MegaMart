package com.benben.megamart.ui.mine;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.widget.CircleImageView;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.PersonDataBean;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.RxBus;
import com.benben.megamart.utils.StatusBarUtils;
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
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

/**
 * 编辑个人资料
 */
public class PersonEditorActivity extends BaseActivity {
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_receiver_address)
    TextView tvReceiverAddress;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_phone)
    TextView tvPhone;

    private PersonDataBean.UserinfoBean mBean;

    private List<String> mSexList = new ArrayList<>();//性别的item

    private String mSex = "";//性别 1男 2女
    private String mName = "";//昵称
    private String mHeader = "";//头像

    //图片选择的参数
    private List<LocalMedia> mSelectMedia = new ArrayList<>();
    private boolean isShow = true;
    private boolean isPreviewVideo = true;
    private boolean isCompress = true;
    private boolean isCheckNumMode = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_editor;
    }

    @Override
    protected void initData() {

        initTitle( getString(R.string.person_data));
        rightTitle.setText( getString(R.string.person_save));
        rightTitle.setTextColor(getResources().getColor(R.color.white));

        RxBus.getInstance().toObservable(Integer.class)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                        if(integer == 2001 || integer == 2002){
                            getData(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        getData(true);
    }

    private void getData(boolean isRefreshAvatar) {
     //   StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.MINE_PERSON_DATA)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                mBean = JSONUtils.jsonString2Bean(result, PersonDataBean.class).getUserinfo().get(0);

                if (mBean != null) {
                    tvId.setText("" + mBean.getUser_id());
                    edtName.setText("" + mBean.getUser_name());
                    if ("1".equals(mBean.getUser_sex())) {
                        tvSex.setText(getResources().getString(R.string.man));
                    } else if ("0".equals(mBean.getUser_sex())) {
                        tvSex.setText(getResources().getString(R.string.woman));
                    } else {
                        tvSex.setText(getResources().getString(R.string.unknown));
                    }
                    tvEmail.setText("" + mBean.getUser_email());
                    tvPhone.setText("" + mBean.getUser_mobile());

                    mSex = mBean.getUser_sex();
                    mName = mBean.getUser_name();
                    //刷新头像
                    if(isRefreshAvatar){
                        ImageUtils.getPic(mBean.getUser_avatar(), ivHeader
                                , PersonEditorActivity.this, R.mipmap.mine_no_login_header);
                        mHeader = mBean.getUser_avatar();
                    }

                }
            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
            }
        });
    }


    @OnClick({R.id.right_title, R.id.iv_header, R.id.rl_header, R.id.tv_id, R.id.tv_sex, R.id.tv_receiver_address, R.id.tv_email, R.id.tv_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //保存
            case R.id.right_title:
                if (mSelectMedia.size() > 0) {
                    uploadImg();
                } else {
                    if (!"".equals(mHeader)) {
                        submit();
                    } else {
                        toast(getResources().getString(R.string.please_select_avatar));
                    }
                }
                break;
            //头像
            case R.id.iv_header:
            case R.id.rl_header:
                // 进入相册
                FunctionConfig config = new FunctionConfig();
                config.setType(1);
                config.setCompress(isCompress);
                config.setEnablePixelCompress(true);
                config.setEnableQualityCompress(true);
                config.setMaxSelectNum(3);
                config.setSelectMode(FunctionConfig.MODE_SINGLE);
                config.setShowCamera(isShow);
                config.setEnablePreview(true);
                config.setEnableCrop(false);
                config.setPreviewVideo(isPreviewVideo);
                config.setCheckNumMode(isCheckNumMode);
                config.setCompressQuality(100);
                config.setImageSpanCount(4);
                config.setCompressFlag(1);

                // 先初始化参数配置，在启动相册
                PictureConfig.init(config);
                PictureConfig.getPictureConfig().openPhoto(PersonEditorActivity.this, resultCallback);
                break;
            //Id
            case R.id.tv_id:
                break;
            //性别
            case R.id.tv_sex:
                mSexList.clear();
                mSexList.add(getResources().getString(R.string.man));
                mSexList.add(getResources().getString(R.string.woman));
                StyledDialog.init(PersonEditorActivity.this);
                StyledDialog.buildBottomItemDialog(mSexList, getResources().getString(R.string.shop_cart_cancel), new MyItemDialogListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        if (position == 0) {
                            mSex = "1";
                            tvSex.setText(getResources().getString(R.string.man));
                        } else {
                            mSex = "0";
                            tvSex.setText(getResources().getString(R.string.woman));
                        }
                    }
                }).show();
                break;
            //收货地址
            case R.id.tv_receiver_address:
                startActivity(new Intent(PersonEditorActivity.this, AddressManagerActivity.class));
                break;
            //邮箱
            case R.id.tv_email:
                if (mBean == null) {
                    Toast.makeText(mContext, getResources().getString(R.string.get_data_failure), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intentEmail = new Intent(PersonEditorActivity.this, BindingEmailActivity.class);
                intentEmail.putExtra("email", "" + mBean.getUser_email());
                startActivity(intentEmail);
                break;
            //手机号
            case R.id.tv_phone:
                if (mBean == null) {
                    Toast.makeText(mContext, getResources().getString(R.string.get_data_failure), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intentPhone = new Intent(PersonEditorActivity.this, BindingPhoneActivity.class);
                intentPhone.putExtra("phone", "" + mBean.getUser_mobile());
                startActivity(intentPhone);
                break;
        }
    }

    /**
     * 保存
     */
    private void submit() {
        mName = edtName.getText().toString();
        if (TextUtils.isEmpty(mName)) {
            toast(getResources().getString(R.string.please_enter_nickname));
            return;
        }
        if ("".equals(mSex)) {
            toast(getResources().getString(R.string.please_select_sex));
            return;
        }
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.MINE_UPDATE_PERSON_DATA)
                .addParam("user_name", "" + mName)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("user_avatar", "" + mHeader)
                .addParam("user_sex", "" + mSex)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(msg);
                RxBus.getInstance().post(true);
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
                toast(getResources().getString(R.string.server_exception));
            }
        });
    }

    /**
     * 获取图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            if (resultList.size() != 0) {
                mSelectMedia = resultList;
                Log.i("callBack_result", mSelectMedia.size() + "");
                if (mSelectMedia != null) {
                    ImageUtils.getPic(mSelectMedia.get(0).getCompressPath(), ivHeader
                            , PersonEditorActivity.this, R.mipmap.mine_no_login_header);
                }
            }
        }
    };

    /**
     * 图片上传
     */
    private void uploadImg() {
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.Builder builder = BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.IMAGE_UPLOAD);
        builder.addFile("images[]", "" + new File(mSelectMedia.get(0).getCompressPath()).getName()
                , new File(mSelectMedia.get(0).getCompressPath()));
        builder.post()
                .build()
                .enqueue(this, new BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        StyledDialogUtils.getInstance().dismissLoading();
                        toast(msg);

                        try {
                            JSONObject object = new JSONObject(result);

                            if (object.has("thumb_url") && !object.isNull("thumb_url")) {
                                JSONArray array = object.getJSONArray("thumb_url");
                                for (int i = 0; i < array.length(); i++) {
                                    mHeader = array.getString(i);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!"".equals(mHeader)) {
                            submit();
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

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }
}
