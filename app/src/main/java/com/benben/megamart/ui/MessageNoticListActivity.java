package com.benben.megamart.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.MessageNoticListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.MessageNoticListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.StatusBarUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by: wanghk 2019-05-31.
 * Describe:消息公告列表页面
 */
public class MessageNoticListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.rlv_list)
    RecyclerView rlvMessageList;
    @BindView(R.id.stf_layout)
    SmartRefreshLayout stfLayout;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;
    private MessageNoticListAdapter mMessageNoticListAdapter;

    private int mPage = 1;
    private boolean isRefresh;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_common_list;
    }

    @Override
    protected void initData() {

        centerTitle.setText(getString(R.string.message_notice));

        stfLayout.setOnRefreshListener(this);
        stfLayout.setOnLoadMoreListener(this);
        rlvMessageList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mMessageNoticListAdapter = new MessageNoticListAdapter(mContext);
        rlvMessageList.setAdapter(mMessageNoticListAdapter);


        //获取消息公告列表
        getMessageNoticList();

    }

    //获取消息公告列表
    private void getMessageNoticList() {

        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("page_start", mPage)
                .addParam("page_size", 15)
                .url(NetUrlUtils.GET_ARTICLE_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取消息公告列表----" + result);
                        StyledDialogUtils.getInstance().dismissLoading();
                        String noteJson = JSONUtils.getNoteJson(result, "article_list");
                        List<MessageNoticListBean> messageNoticList = JSONUtils.jsonString2Beans(noteJson, MessageNoticListBean.class);
                        if (isRefresh) {
                            mMessageNoticListAdapter.refreshList(messageNoticList);
                            stfLayout.finishRefresh(true);
                        } else {
                            mMessageNoticListAdapter.appendToList(messageNoticList);
                            stfLayout.finishLoadMore(true);
                        }

                        if (mMessageNoticListAdapter.getItemCount() <= 0) {
                            llytNoData.setVisibility(View.VISIBLE);
                            stfLayout.setVisibility(View.GONE);
                        } else {
                            llytNoData.setVisibility(View.GONE);
                            stfLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                        if (isRefresh) {
                            stfLayout.finishRefresh(false);
                        } else {
                            stfLayout.finishLoadMore(false);
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.show(mContext, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                        if (isRefresh) {
                            stfLayout.finishRefresh(false);
                        } else {
                            stfLayout.finishLoadMore(false);
                        }
                    }
                });

    }


    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }


    @OnClick(R.id.rl_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
        }
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPage = 1;
        isRefresh = true;
        getMessageNoticList();
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPage++;
        isRefresh = false;
        getMessageNoticList();
    }
}
