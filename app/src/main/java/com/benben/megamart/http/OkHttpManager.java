package com.benben.megamart.http;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.megamart.R;
import com.benben.megamart.ui.LoginActivity;
import com.benben.megamart.utils.LoginCheckUtils;
import com.hss01248.dialog.interfaces.MyDialogListener;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Description:OkHttpMange管理类
 *
 * @author zjn
 * Email：168455992@qq.com
 * @date 2019/1/11
 */
public class OkHttpManager {
    private static OkHttpManager mInstance;
    private OkHttpClient mClient;
    private Handler mHnadler;

    /**
     * 单例
     *
     * @return
     */
    public static synchronized OkHttpManager getInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpManager();
        }
        return mInstance;
    }

    /**
     * 构造函数
     */
    private OkHttpManager() {
        initOkHttp();
        mHnadler = new Handler(Looper.getMainLooper());
    }

    /**
     * 初始化OkHttpClient
     */
    private void initOkHttp() {
        mClient = new OkHttpClient().newBuilder()
                .readTimeout(30000, TimeUnit.SECONDS)
                .connectTimeout(30000, TimeUnit.SECONDS)
                .writeTimeout(30000, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 请求
     *
     * @param client
     * @param callBack
     */
    public void request(Activity activity, BaseOkHttpClient client, final BaseCallBack callBack) {
        if (callBack == null) {
            throw new NullPointerException(" callback is null");
        }
//        StyledDialogUtils.getInstance().loading(activity);
        mClient.newCall(client.buildRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                StyledDialogUtils.getInstance().dismissLoading(activity);
                sendonFailureMessage(callBack, call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                StyledDialogUtils.getInstance().dismissLoading(activity);
                if (response.isSuccessful()) {
                    String result = response.body().string().trim();
                    Log.e("TAG", "result=" + result);
                    try {
                        if (!StringUtils.isEmpty(result)) {
                            String code = JSONUtils.getNoteJson(result,"code");
                            String msg = JSONUtils.getNoteJson(result,"msg");
                            String data = JSONUtils.getNoteJson(result,"data");
                            if("1".equals(code)) {
                                sendonSuccessMessage(callBack, data,msg);
                            }else if("-201".equals(code) || "-202".equals(code) ||
                                    "-203".equals(code) || "-300".equals(code)){
                                //需要验证用户登录
                                LoginCheckUtils.showYanZhengDialog(activity, new MyDialogListener() {
                                    @Override
                                    public void onFirst() {
                                        LoginCheckUtils.clearUserInfo();
                                        Intent intent = new Intent(activity, LoginActivity.class);
                                        activity.startActivity(intent);
                                    }

                                    @Override
                                    public void onSecond() {
                                        LoginCheckUtils.clearUserInfo();
                                    }
                                });
                            }else{
                                sendonErrorMessage(callBack,Integer.parseInt(code),msg);
//                                sendonErrorMessage(callBack,response.code(),msg);
                            }
                        }
                    } catch (Exception e) {
                        sendonErrorMessage(callBack, response.code(), activity.getResources().getString(R.string.server_exception));
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                } else {
                    Log.e("TAG", "response.code=" + response.code());
                    sendonErrorMessage(callBack, response.code(), activity.getResources().getString(R.string.server_exception));
                }
            }
        });
    }

    /**
     * 成功信息
     *
     * @param callBack
     * @param result
     */
    private void sendonSuccessMessage(final BaseCallBack callBack, final Object result,final String msg) {
        mHnadler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(result,msg);
            }
        });
    }

    /**
     * 失败信息
     *
     * @param callBack
     * @param call
     * @param e
     */
    private void sendonFailureMessage(final BaseCallBack callBack, final Call call, final IOException e) {
        mHnadler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onFailure(call, e);
            }
        });
    }

    /**
     * 错误信息
     *
     * @param callBack
     * @param code
     */
    private void sendonErrorMessage(final BaseCallBack callBack, final int code, final String msg) {
        mHnadler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(code, msg);
            }
        });
    }
}
