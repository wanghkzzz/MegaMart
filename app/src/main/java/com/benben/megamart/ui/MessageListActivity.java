package com.benben.megamart.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.commoncore.utils.DateUtils;
import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.StyledDialogUtils;
import com.benben.megamart.BaseActivity;
import com.benben.megamart.MegaMartApplication;
import com.benben.megamart.R;
import com.benben.megamart.adapter.MessageListAdapter;
import com.benben.megamart.api.NetUrlUtils;
import com.benben.megamart.bean.MessageListBean;
import com.benben.megamart.config.Constants;
import com.benben.megamart.http.BaseCallBack;
import com.benben.megamart.http.BaseOkHttpClient;
import com.benben.megamart.utils.StatusBarUtils;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.Conversation;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.chat.Message;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Create by wanghk on 2019-05-30.
 * Describe:消息列表页面
 */
public class MessageListActivity extends BaseActivity implements OnRefreshListener {
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
    //消息列表adapter
    private MessageListAdapter mMessageListAdapter;
    //消息列表
    private List<MessageListBean> mMessageInfoList = new ArrayList<>();
    private String TAG = getClass().getName();

    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_common_list;
    }

    @Override
    protected void initData() {

        centerTitle.setText(getString(R.string.message));

        stfLayout.setEnableLoadMore(false);
        stfLayout.setOnRefreshListener(this);
        rlvMessageList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mMessageListAdapter = new MessageListAdapter(mContext);
        rlvMessageList.setAdapter(mMessageListAdapter);


        llytNoData.setVisibility(View.GONE);
        stfLayout.setVisibility(View.VISIBLE);
        //初始化列表
        MessageListBean articleBean = new MessageListBean();
        articleBean.setArticle_title(getResources().getString(R.string.message_notice));
        articleBean.setUnreadMessageCount(0);
        articleBean.setArticle_img(String.valueOf(R.mipmap.icon_gonggao));
        articleBean.setArticle_description("");
        articleBean.setArticle_time("");
        mMessageInfoList.add(articleBean);
        MessageListBean customServiceBean = new MessageListBean();
        customServiceBean.setArticle_title(getResources().getString(R.string.platform_customer));
        customServiceBean.setUnreadMessageCount(0);
        customServiceBean.setArticle_img(String.valueOf(R.mipmap.default_custom_service));
        customServiceBean.setArticle_description("");
        customServiceBean.setArticle_time("");
        mMessageInfoList.add(customServiceBean);
        mMessageListAdapter.appendToList(mMessageInfoList);



    }

    @Override
    protected void onResume() {
        super.onResume();
        //初始化消息公告和平台客服
        getMessageList();
    }

    //初始化消息公告和平台客服
    private void getMessageList() {

        if(ChatClient.getInstance() != null &&ChatClient.getInstance().chatManager() != null){
            Hashtable<String, Conversation> allConversations = ChatClient.getInstance().chatManager().getAllConversations();
            if(allConversations != null){
                for (String key : allConversations.keySet()) {
                    //获取用户信息
                    getContactInfo(allConversations.get(key));
                }
            }
        }




        StyledDialogUtils.getInstance().loading(mContext);
        BaseOkHttpClient.newBuilder()
                .addParam("user_id", MegaMartApplication.mPreferenceProvider.getUId())
                .url(NetUrlUtils.GET_ALL_LIST)
                .json()
                .post().build().enqueue(mContext, new
                BaseCallBack<String>() {
                    @Override
                    public void onSuccess(String result, String msg) {
                        Log.e(Constants.WHK_TAG, "onSuccess: 获取消息列表----" + result);
                        StyledDialogUtils.getInstance().dismissLoading();
                        String noteJson = JSONUtils.getNoteJson(result, "article_info");
                        MessageListBean messageListBean = JSONUtils.jsonString2Bean(noteJson, MessageListBean.class);
                        mMessageInfoList.get(0).setArticle_time(DateUtils.stampToDate(messageListBean.getArticle_time()));
                        mMessageInfoList.get(0).setArticle_description(messageListBean.getArticle_description());

                        MessageListBean articleBean = new MessageListBean();
                        articleBean.setArticle_title(getResources().getString(R.string.message_notice));
                        articleBean.setUnreadMessageCount(0);
                        articleBean.setArticle_img(String.valueOf(R.mipmap.icon_gonggao));
                        articleBean.setArticle_description(messageListBean.getArticle_description());
                        articleBean.setArticle_time(DateUtils.stampToDate(messageListBean.getArticle_time()));
                        mMessageInfoList.set(0,articleBean);
                        mMessageListAdapter.notifyItemChanged(0);
                        //llytNoData.setVisibility(View.GONE);
                        stfLayout.finishRefresh(true);

                    }

                    @Override
                    public void onError(int code, String msg) {
                        // ToastUtils.show(mContext, msg);
                        StyledDialogUtils.getInstance().dismissLoading();
                        stfLayout.finishRefresh(true);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        // ToastUtils.show(mContext, e.getMessage());
                        StyledDialogUtils.getInstance().dismissLoading();
                        stfLayout.finishRefresh(true);
                    }
                });

    }

    //获取用户昵称和头像
    private void getContactInfo(Conversation conversation) {
        //未读消息数量
        int unreadMsgCount = conversation.unreadMessagesCount();
        //信息bean
        Message imMessage = conversation.latestMessage();
        if (imMessage == null) {
            return;
        }
        //最新一条消息
        String lastMsg = "";
        //该消息的发送时间
        String lastMsgTime = mSdf.format(new Date(imMessage.messageTime()));
        String eid = imMessage.getUserName();


        EMMessageBody body = imMessage.body();
        if (body instanceof EMTextMessageBody) {
            lastMsg = ((EMTextMessageBody) body).getMessage();
        } else if (body instanceof EMImageMessageBody) {
            lastMsg = getString(R.string.message_type_image);
        } else if (body instanceof EMVideoMessageBody) {
            lastMsg = getString(R.string.message_type_video);
        } else if (body instanceof EMVoiceMessageBody) {
            lastMsg = getString(R.string.message_type_audio);
        } else if (body instanceof EMFileMessageBody) {
            lastMsg = getString(R.string.message_type_file);
        } else if (body instanceof EMLocationMessageBody) {
            lastMsg = getString(R.string.message_type_location);
        } else {
            lastMsg = getString(R.string.message_type_unknown);
        }
        Log.e(Constants.WHK_TAG, "getContactInfo: lastMsg=" +lastMsg);


        MessageListBean customServiceBean = new MessageListBean();
        customServiceBean.setArticle_title(getResources().getString(R.string.platform_customer));
        customServiceBean.setUnreadMessageCount(unreadMsgCount);
        customServiceBean.setArticle_img(String.valueOf(R.mipmap.default_custom_service));
        customServiceBean.setArticle_description(lastMsg);
        customServiceBean.setArticle_time(lastMsgTime);
        mMessageInfoList.remove(1);
        mMessageInfoList.add(customServiceBean);
        mMessageListAdapter.refreshList(mMessageInfoList);
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
        getMessageList();
    }


}
