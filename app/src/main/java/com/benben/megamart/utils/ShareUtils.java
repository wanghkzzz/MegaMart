package com.benben.megamart.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.benben.megamart.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class ShareUtils {

    private static final String TAG = "ShareUtils";
    private Activity activity;

    public ShareUtils(Activity activity) {
        this.activity = activity;
    }

    public void share() {
        SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE
//                       , SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
                };
        new ShareAction(activity).setDisplayList(displaylist)
                .setShareboardclickCallback(shareBoardlistener).open();
    }

    private String shareUri;
    private String title;
    private String content;

    public ShareUtils(Activity activity, String shareUri, String title, String content) {
        this.activity = activity;
        this.shareUri = shareUri;
        this.title = title;
        this.content = content;
    }

    //分享面板的点击事件
    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {
        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {

            if (share_media == null) {

            } else {
                UMImage image = new UMImage(activity, R.mipmap.icon_app);//网络图片
                UMWeb web = new UMWeb(shareUri);
                web.setDescription(content);
                web.setThumb(image);
                web.setTitle(title);
                new ShareAction(activity).setPlatform(share_media).setCallback(umShareListener)
                        .withMedia(web)
                        .share();
            }
        }
    };

    //分享的回调
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.e(TAG, "onResult: platform="+platform.getName() );
            Toast.makeText(activity, activity.getResources().getString(R.string.share_success), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Log.e(TAG, "onError: "+t.getMessage() );

            Toast.makeText(activity, activity.getResources().getString(R.string.share_fail)+"error:"+t.getMessage(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(activity, activity.getResources().getString(R.string.share_fail), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(activity, activity.getResources().getString(R.string.share_cancel), Toast.LENGTH_SHORT).show();
        }
    };
}
