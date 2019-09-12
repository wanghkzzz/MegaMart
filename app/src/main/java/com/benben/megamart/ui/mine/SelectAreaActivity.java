package com.benben.megamart.ui.mine;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.R;
import com.benben.megamart.adapter.SelectAreaAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.SelectAreaBean;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.KeyBoardUtils;
import com.benben.megamart.utils.StatusBarUtils;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/17
 * Time: 9:32
 * 选择区或者州
 */
public class SelectAreaActivity extends BaseActivity {
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.lv_area)
    ListView lvArea;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_post_code)
    TextView tvPostCode;

    private SelectAreaAdapter mAdapter;

    private String mSearch = "";//搜索的地址或者邮编

    private int mPType = 1;//1州 2区

    private SelectAreaBean mBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_area;
    }

    @Override
    protected void initData() {

        mPType = getIntent().getIntExtra("type", 1);

        if (mPType == 1) {
            initTitle("" + getString(R.string.select_state));
            tvTitle.setText(R.string.select_now_state);
            tvPostCode.setVisibility(View.GONE);

        } else {
            initTitle("" + getString(R.string.select_area));
            tvTitle.setText(R.string.select_now_area);
            tvPostCode.setVisibility(View.VISIBLE);
        }
        getData();

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mSearch = v.getText().toString().trim();
                    getData();
                    KeyBoardUtils.hideKeyboard(edtSearch);
                    return true;
                }
                return false;
            }
        });
    }

    private void getData() {
        StyledDialogUtils.getInstance().loading(this);
        BaseOkHttpClient.newBuilder().url(NetUrlUtils.ADDRESS_AREA)
                .addParam("search_keyword", "" + mSearch)
                .addParam("area_level", "" + mPType)
                .post()
                .json()
                .build().enqueue(this, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                mBean = JSONUtils.jsonString2Bean(result, SelectAreaBean.class);
                mAdapter = new SelectAreaAdapter(SelectAreaActivity.this, mBean.getArea_info(), mPType);
                lvArea.setAdapter(mAdapter);
                lvArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        intent.putExtra("bean", mBean.getArea_info().get(position));
                        intent.putExtra("type", mPType);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                StyledDialogUtils.getInstance().dismissLoading();
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                StyledDialogUtils.getInstance().dismissLoading();
                Toast.makeText(mContext, getResources().getString(R.string.server_exception), Toast.LENGTH_SHORT).show();
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
