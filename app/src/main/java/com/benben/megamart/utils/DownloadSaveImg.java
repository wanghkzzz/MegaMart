package com.benben.megamart.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.benben.megamart.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/18
 * Time: 16:07
 */
public class DownloadSaveImg {
    private static Context context;
    private static String filePath;
    private static Bitmap mBitmap;
    private static String mSaveMessage = "failure";
    private final static String TAG = "PictureActivity";
    private static ProgressDialog mSaveDialog = null;

    public static void donwloadImg(Context contexts, String filePaths) {
        context = contexts;
        filePath = filePaths;
        mSaveDialog = ProgressDialog.show(context, context.getResources().getString(R.string.save_photo), context.getResources().getString(R.string.photo_saving_please_wait), true);
        new Thread(saveFileRunnable).start();
    }

    private static Runnable saveFileRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (!TextUtils.isEmpty(filePath)) { //网络图片
                    // 对资源链接
                    URL url = new URL(filePath);
                    //打开输入流
                    InputStream inputStream = url.openStream();
                    //对网上资源进行下载转换位图图片
                    mBitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                }
                saveFile(mBitmap);
                mSaveMessage = context.getResources().getString(R.string.save_photo_success);
            } catch (IOException e) {
                mSaveMessage = context.getResources().getString(R.string.save_photo_fail);
                e.printStackTrace();
                Log.e("TAG", "getMessage=" + e.getMessage());
                Log.e("TAG", "getCause=" + e.getCause());

            } catch (Exception e) {
                e.printStackTrace();
            }
            messageHandler.sendMessage(messageHandler.obtainMessage());
        }
    };

    private static Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mSaveDialog.dismiss();
            Log.d(TAG, mSaveMessage);
            Toast.makeText(context, mSaveMessage, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 保存图片
     *
     * @param bm
     * @throws IOException
     */
    public static void saveFile(Bitmap bm) throws IOException {
        File dirFile = new File(Environment.getExternalStorageDirectory().getPath());
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        String fileName = UUID.randomUUID().toString() + ".jpg";
        File myCaptureFile = new File(Environment.getExternalStorageDirectory().getPath()
                + "/DCIM/Camera/" + fileName);
        if (!myCaptureFile.getParentFile().exists()) {
            myCaptureFile.getParentFile().mkdirs();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        //把图片保存后声明这个广播事件通知系统相册有新图片到来
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(myCaptureFile);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }
}