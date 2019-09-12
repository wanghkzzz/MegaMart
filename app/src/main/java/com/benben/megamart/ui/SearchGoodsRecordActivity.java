package com.benben.megamart.ui;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.ScreenUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.AFinalRecyclerViewAdapter;
import com.benben.megamart.adapter.SearchRecordListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.SearchKeyWordListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.StatusBarUtils;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-05-31.
 * Describe:热门搜索，搜索记录页面
 */
public class SearchGoodsRecordActivity extends BaseActivity {

    public static final String KEY_WORD = "key_word";
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.edt_goods_search)
    EditText edtGoodsSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.rlv_hot_search_list)
    RecyclerView rlvHotSearchList;
    @BindView(R.id.iv_del_history_search)
    ImageView ivDelHistorySearch;
    @BindView(R.id.rlv_history_search_list)
    RecyclerView rlvHistorySearchList;
    //热门搜索列表adapter
    private SearchRecordListAdapter mHotSearchAdapter;
    //历史搜索列表adapter
    private SearchRecordListAdapter mHistorySearchAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_goods;
    }

    @Override
    protected void initData() {
        rlvHotSearchList.setLayoutManager(new GridLayoutManager(mContext, 4));
        rlvHistorySearchList.setLayoutManager(new GridLayoutManager(mContext, 4));

        mHotSearchAdapter = new SearchRecordListAdapter(mContext);
        mHistorySearchAdapter = new SearchRecordListAdapter(mContext);
        rlvHotSearchList.setAdapter(mHotSearchAdapter);
        rlvHistorySearchList.setAdapter(mHistorySearchAdapter);

        ivDelHistorySearch.setVisibility(View.GONE);
        mHotSearchAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(View view, int position, String model) {
                edtGoodsSearch.setText(model);
                search();
            }

            @Override
            public void onItemLongClick(View view, int position, String model) {

            }
        });
        mHistorySearchAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(View view, int position, String model) {
                edtGoodsSearch.setText(model);
                search();
            }

            @Override
            public void onItemLongClick(View view, int position, String model) {

            }
        });


        edtGoodsSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });

        initSearchRecord();
    }

    private void initSearchRecord() {

        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.GET_SEARCH_KEYWORD)
                .json()
                .post().build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                Log.e(Constants.WHK_TAG, "onSuccess: 获取热门搜索和搜索记录----" + result);
                StyledDialogUtils.getInstance().dismissLoading();

                SearchKeyWordListBean searchKeyWordListBean = JSONUtils.jsonString2Bean(result, SearchKeyWordListBean.class);
                if (searchKeyWordListBean != null) {
                    if (searchKeyWordListBean.getHot_list() != null && searchKeyWordListBean.getHot_list().size() > 0) {
                        mHotSearchAdapter.refreshList(searchKeyWordListBean.getHot_list());
                    }else {
                        mHotSearchAdapter.clear();
                    }
                    if (searchKeyWordListBean.getUsed_list() != null && searchKeyWordListBean.getUsed_list().size() > 0) {
                        ivDelHistorySearch.setVisibility(View.VISIBLE);
                        mHistorySearchAdapter.clear();
                        mHistorySearchAdapter.appendToList(searchKeyWordListBean.getUsed_list());

                    } else {
                        mHistorySearchAdapter.clear();
                        ivDelHistorySearch.setVisibility(View.GONE);
                    }
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


    @OnClick({R.id.rl_back, R.id.iv_del_history_search, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                ScreenUtils.closeKeybord(edtGoodsSearch, mContext);
                finish();
                break;
            case R.id.tv_search:
                search();
                break;
            case R.id.iv_del_history_search:
                StyledDialog.buildIosAlert(getResources().getString(R.string.do_you_confirm_deletion), "", new MyDialogListener() {
                    @Override
                    public void onFirst() {

                        //删除历史搜索
                        deleteHistorySearch();
                        StyledDialog.dismiss();
                    }

                    @Override
                    public void onSecond() {
                        StyledDialog.dismiss();
                    }
                }).setBtnText(getResources().getString(R.string.shop_cart_positive), getResources().getString(R.string.shop_cart_cancel)).show();

                break;
        }
    }

    //搜索
    private void search() {
        Intent intent = new Intent(mContext, SearchGoodsListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_WORD, edtGoodsSearch.getText().toString());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenUtils.closeKeybord(edtGoodsSearch, mContext);
    }

    //删除历史搜索
    private void deleteHistorySearch() {
        // StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.DELETE_USED_KEYWORD)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 删除搜索记录----" + result);
                        StyledDialogUtils.getInstance().dismissLoading();
                        toast(msg);
                        initSearchRecord();
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


    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }
}
