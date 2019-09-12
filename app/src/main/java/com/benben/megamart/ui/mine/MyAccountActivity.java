package com.benben.megamart.ui.mine;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.MyAccountAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.AccountListBean;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.StatusBarUtils;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的账本
 */
public class MyAccountActivity extends BaseActivity {
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.lv_account)
    RecyclerView lvAccount;
    @BindView(R.id.tv_total_consume)
    TextView tvTotalConsume;
    @BindView(R.id.llyt_no_data)
    LinearLayout llytNoData;
    @BindView(R.id.stf_layout)
    SmartRefreshLayout stfLayout;

    private int mYear;//年份
    private int mMonth;//月份
    private int mPageSize = 15;
    private int mPageStart = 1;

    private MyAccountAdapter adapter;//账户明细适配器

    private AccountListBean mBean;
    //当前年份
    private int mCurrentYear;
    //当前月份
    private int mCurrentMonth;
    private Calendar mCurrentCalendar;
    private boolean isRefresh = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_account;
    }

    @Override
    protected void initData() {
        initTitle(getString(R.string.wallet_record));
        mCurrentCalendar = Calendar.getInstance();
        mYear = mCurrentCalendar.get(Calendar.YEAR);
        mMonth = mCurrentCalendar.get(Calendar.MONTH) + 1;// Java月份从0开始算  

        mCurrentYear = mYear;
        mCurrentMonth = mMonth;

        tvMonth.setText(String.valueOf(mMonth));
        tvYear.setText(String.valueOf(mYear));

        lvAccount.setLayoutManager(new LinearLayoutManager(mContext));

        adapter = new MyAccountAdapter(MyAccountActivity.this);
        lvAccount.setAdapter(adapter);

        initRefreshLayout();
        getData();
    }

    //初始化下来刷新
    private void initRefreshLayout() {
        stfLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                mPageStart = 1;
                getData();
            }
        });
        stfLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                mPageStart++;
                getData();
            }
        });
    }

    private void getData() {


        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.MINE_MY_ACCOUNT_LIST)
                .addParam("user_id", "" + MegaMartApplication.mPreferenceProvider.getUId())
                .addParam("bal_month", "" + mMonth)
                .addParam("bal_year", "" + mYear)
                .addParam("page_size", "" + mPageSize)
                .addParam("page_start", "" + mPageStart)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                mBean = JSONUtils.jsonString2Bean(result, AccountListBean.class);
                if (mBean == null || mBean.getUser_balance() == null || mBean.getUser_balance().size() <= 0) {
                    lvAccount.setVisibility(View.GONE);
                    llytNoData.setVisibility(View.VISIBLE);
                    return;
                }

                if (isRefresh) {
                    adapter.refreshList(mBean.getUser_balance());
                    stfLayout.finishRefresh(true);
                } else {
                    adapter.appendToList(mBean.getUser_balance());
                    stfLayout.finishLoadMore(true);
                }
                lvAccount.setVisibility(View.VISIBLE);
                llytNoData.setVisibility(View.GONE);
                tvTotalConsume.setText("" + getString(R.string.account_total_consume) + mBean.getBal_total());
            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();

                stfLayout.finishRefresh(true);
                stfLayout.finishLoadMore(true);
                if (isRefresh) {
                    lvAccount.setVisibility(View.GONE);
                    llytNoData.setVisibility(View.VISIBLE);
                    tvTotalConsume.setText("" + getString(R.string.account_total_consume) + "0");
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
                toast(getResources().getString(R.string.server_exception));
                stfLayout.finishRefresh(false);
                stfLayout.finishLoadMore(false);
                lvAccount.setVisibility(View.GONE);
                llytNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick({R.id.tv_year, R.id.tv_month})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //选择年
            case R.id.tv_year:
                ShowSelectYearDialog();
                break;
            //选择月
            case R.id.tv_month:
                showSelectMonthDialog();
                break;
        }
    }

    //选择月份
    private void showSelectMonthDialog() {
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Calendar instance = Calendar.getInstance();
                instance.setTime(date);
                mMonth = (instance.get(Calendar.MONTH) + 1);
                tvMonth.setText(String.valueOf(mMonth));
                isRefresh = true;
                mPageStart = 1;
                getData();
            }
        }).setDate(mCurrentCalendar).setType(new boolean[]{false, true, false, false, false, false}).build();
        pvTime.show();

    }

    //选择年份
    private void ShowSelectYearDialog() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        startDate.set(mCurrentYear - 1, 0, 1);
        endDate.set(mCurrentYear + 1, 0, 1);
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Calendar instance = Calendar.getInstance();
                instance.setTime(date);
                mYear = instance.get(Calendar.YEAR);
                tvYear.setText(String.valueOf(mYear));
                isRefresh = true;
                mPageStart = 1;
                getData();
            }
        }).setRangDate(startDate, endDate).setDate(mCurrentCalendar).setType(new boolean[]{true, false, false, false, false, false}).build();
        pvTime.show();
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtils.setStatusBarColor(this, R.color.color_EC5413);
        StatusBarUtils.setAndroidNativeLightStatusBar(this, false);
    }


}
