package com.benben.megamart.utils;

import android.app.Activity;
import android.content.Intent;

import com.benben.megamart.config.Constants;
import com.benben.megamart.ui.CommonWebViewActivity;
import com.benben.megamart.ui.GoodsDetailsActivity;

/**
 * Created by: wanghk 2019-06-28.
 * Describe: 跳转参数工具类
 */
public class UrlUtils {

    /**
     * @param mContext
     * @param type     1 商品详情 2 网页url 3 富文本 html标签
     * @param title
     * @param param
     */
    // type
    public static void clickBannerUrl(Activity mContext, int type, String title, String param) {
        Intent intent = null;
        switch (type) {
            case 1:
                intent = new Intent(mContext, GoodsDetailsActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_GOODS_ID, param);
                break;
            case 2:
                intent = new Intent(mContext, CommonWebViewActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_TITLE, title);
                intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_URL, param);
                intent.putExtra(Constants.EXTRA_KEY_IS_URL, false);
                break;
            case 3:
                intent = new Intent(mContext, CommonWebViewActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_TITLE, title);
                intent.putExtra(Constants.EXTRA_KEY_WEB_VIEW_URL, param);
                intent.putExtra(Constants.EXTRA_KEY_IS_URL, true);
                break;
        }

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }


    }
}
