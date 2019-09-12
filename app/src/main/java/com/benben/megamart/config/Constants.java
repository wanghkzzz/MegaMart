package com.benben.megamart.config;

/**
 * 功能:配置的APPid
 */
public class Constants {
    public static final String APPID = "80175391";
    public static final String WX_APP_ID = "wxb44daab170bd4feb";
//    public static final String WX_APP_ID = "wx232f23c5c84ced3e";
    public static final String WX_SECRET = "edd54ad6df8b0d7135117bc7ba602267";
//    public static final String WX_SECRET = "56cfb41859c96ad0df5765392a2c4ea8";
    public static final String APP_SECRET = "RUYcTVLIqPmtrbbgcwMxOAzBZpocJSai";
    //
    public static final String STRIPE_SECRET_KEY = "sk_test_mLUpYc0aUhJkmn9Z7d7yoFeG00pJy44Rw8";
    //正式的publishkey
    public static final String STRIPE_PUBLISHABLE_KEY = "pk_test_NtjlNVW6KzHBcIQcxRnnKbOt008lqwgt7w";
    //测试publishkey
    public static final String TEST_STRIPE_PUBLISHABLE_KEY = "pk_test_ojhuCPfrWzNqbanI8fQ9q8wy00PucAjDdy";

    //图片后缀
    public static final String BITMAP_SUFFIX = ".zrjm";
    public static final int TO_DIARY_REQUEST = 100;

    //wanghk add
    public static final String WHK_TAG = "wanghk_log";

    //商品id
    public static final String EXTRA_KEY_GOODS_ID = "goods_id";
    //分类id
    public static final String EXTRA_KEY_CATE_ID = "cate_id";
    //分类父级id
    public static final String EXTRA_KEY_CATE_PARENT_ID = "cate_parent_id";
    //首页模块
    public static final String EXTRA_KEY_BLOCK_TYPE = "search_field";
    //商品列表
    public static final String EXTRA_KEY_GOODS_LIST = "goods_list";
    //商品总价格
    public static final String EXTRA_KEY_GOODS_TOTAL_MONEY = "goods_total_money";
    //支付方式
    public static final String EXTRA_KEY_PAYMENT_WAY = "payment_way";
    //订单编号
    public static final String EXTRA_KEY_ORDER_ID = "orderId";
    //支付金额
    public static final String EXTRA_KEY_PAYMENT_MONEY = "payment_money";
    //webview标题
    public static final String EXTRA_KEY_WEB_VIEW_TITLE = "webview_title";
    //是否是url 还是富文本
    public static final String EXTRA_KEY_IS_URL = "is_url";
    //Url
    public static final String EXTRA_KEY_WEB_VIEW_URL = "webview_url";
    //常用地址
    public static final String SP_KEY_USED_ADDRESS = "used_address";
    //是否刷新
    public static final String EXTRA_KEY_IS_REFRESH = "is_refresh";
    //用户id
    public static final String EXTRA_KEY_USER_ID = "uid";
    //我的邀请二维码
    public static final String EXTRA_KEY_MY_QR_CODE = "my_qr_code";
    //分类商品页面的标题
    public static final String EXTRA_KEY_CATE_TITLE = "cate_title";
    //request code
    public static final int ADD_DIARY_REQUEST_CODE = 100;
    public static final int ADD_COMMENT_REQUEST_CODE = 101;
    public static final int ADDRESS_REQUEST_CODE = 102;
    public static final int COUPON_REQUEST_CODE = 103;
    public static final int CONFIRM_ORDER_REQUEST_CODE = 104;
    public static final int ORDER_DETAILS_REQUEST_CODE = 105;
    //result code
    public static final int RESULT_CODE_OK = 200;
    /*socket 连接常量定义*/
    //广播服务
    public static final String EVENT_BROADCASTINGLISTENER = "broadcastingListen";
    //心跳包
    public static final String EVENT_HEARTBEAT = "heartbeat";
    //握手包
    public static final String EVENT_HANDSHAKE = "conn";
    //自己发送的消息事件
    public static final String EVENT_BROAD_CAST = "broadcast";
    /*END*/
}
