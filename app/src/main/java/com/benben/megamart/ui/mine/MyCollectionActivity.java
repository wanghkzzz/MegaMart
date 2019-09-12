package com.benben.megamart.ui.mine;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.CollectionAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.MyCollectionBean;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.StatusBarUtils;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/14
 * Time: 14:58
 * <p>
 * 我的收藏
 */
public class MyCollectionActivity extends BaseActivity {
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.rv_collection)
    RecyclerView rvCollection;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;
    @BindView(R.id.tv_all_select)
    TextView tvAllSelect;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;

    private CollectionAdapter mAdapter;

    private MyCollectionBean mBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_collection;
    }

    @Override
    protected void initData() {
        initTitle("" + getString(R.string.collection_title));

        rightTitle.setText("" + getString(R.string.person_editor));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvCollection.setLayoutManager(gridLayoutManager);

        getData();
    }

    private void getData() {
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.MINE_MY_COLLECTION_LIST)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                mBean = JSONUtils.jsonString2Bean(result, MyCollectionBean.class);
                mAdapter = new CollectionAdapter(MyCollectionActivity.this, mBean.getGoods_list());
                rvCollection.setAdapter(mAdapter);
                llytNoData.setVisibility(View.GONE);
            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(msg);
                llytNoData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(getResources().getString(R.string.server_exception));
                llytNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick({R.id.tv_all_select, R.id.tv_delete, R.id.right_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_all_select:
                if (mBean == null) {
                    Toast.makeText(mContext, getResources().getString(R.string.no_data_yet), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (getResources().getString(R.string.shop_cart_select_all).equals(tvAllSelect.getText().toString())) {
                    tvAllSelect.setText("" + getString(R.string.collection_no_select));
                    for (int i = 0; i < mBean.getGoods_list().size(); i++) {
                        mBean.getGoods_list().get(i).setSelect(true);
                        Drawable drawable = getResources().getDrawable(R.mipmap.icon_select_theme);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
                        tvAllSelect.setCompoundDrawables(drawable, null, null, null);
                    }
                } else {
                    tvAllSelect.setText("" + getString(R.string.collection_all_select));
                    for (int i = 0; i < mBean.getGoods_list().size(); i++) {
                        mBean.getGoods_list().get(i).setSelect(false);
                        Drawable drawable = getResources().getDrawable(R.mipmap.icon_select_no);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
                        tvAllSelect.setCompoundDrawables(drawable, null, null, null);
                    }
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_delete:
                delete();
                break;
            case R.id.right_title:
                if (mAdapter != null) {
                    if (getString(R.string.person_editor).equals(rightTitle.getText().toString().trim())) {
                        mAdapter.setEditor(true);
                        rightTitle.setText("" + getString(R.string.shop_cart_complete));
                        rlBottom.setVisibility(View.VISIBLE);
                    } else {
                        rlBottom.setVisibility(View.GONE);
                        rightTitle.setText("" + getString(R.string.person_editor));
                        mAdapter.setEditor(false);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    toast(getResources().getString(R.string.no_collection_yet));
                }
                break;
        }
    }

    /**
     * 删除
     */
    private void delete() {
        JSONArray array = new JSONArray();
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < mBean.getGoods_list().size(); i++) {
            if (mBean.getGoods_list().get(i).isSelect()) {
                array.put(mBean.getGoods_list().get(i).getCollect_id());
            }
        }
        if (array.length() == 0) {
            toast(getResources().getString(R.string.please_select_item_to_delete));
            return;
        }
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.MINE_DELETE_COLLECTION)
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("delete_id_list", array)
                .addParam("is_empty", 0)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(msg);

                Iterator<MyCollectionBean.GoodsListBean> iterator = mBean.getGoods_list().iterator();
                while (iterator.hasNext()) {
                    MyCollectionBean.GoodsListBean a = iterator.next();
                    if (a.isSelect()) {
                        //取出已选择的品牌，移除相同的规格材料
                        iterator.remove();
                    }
                }
                if (mBean.getGoods_list().size() == 0) {
                    mBean = null;
                    mAdapter = null;
                    rlBottom.setVisibility(View.GONE);
                    rightTitle.setText("" + getString(R.string.person_editor));
                    llytNoData.setVisibility(View.VISIBLE);
                    rvCollection.setVisibility(View.GONE);
                }else{
                    mAdapter.notifyDataSetChanged();
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
