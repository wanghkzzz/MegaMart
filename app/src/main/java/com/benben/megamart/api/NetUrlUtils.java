package com.benben.megamart.api;

/**
 * 功能:APP接口类
 */
public class NetUrlUtils {
    //该项目接口命名，根据后台接口的真实地址，全参数命名

    //    public static String BASEURL = "http://live.zzqcnz.com/api/v1/";
    public static String BASEURL = "http://megamart.brd-techi.com/index.php/api/v1/";

    //首页活动列表-获取活动
    public static String GET_LIVE_ACTIVE_LIST = BASEURL + "5cb83af2dc193";

    /**
     * 登录注册忘记密码  这个模块接口以login开头
     */
    //密码登录
    public static String LOGIN_PASSWORD_LOGIN = BASEURL + "login/login";
    //手机号注册
    public static String LOGIN_PHONE_REGISTER = BASEURL + "login/phoneRegister";
    //邮箱注册
    public static String LOGIN_EMAIL_REGISTER = BASEURL + "login/emailRegister";
    //忘记密码
    public static String LOGIN_FORGET_PASSWORD = BASEURL + "login/resetPasswordPer";
    //微信登录
    public static String WX_LOGIN=BASEURL+"login/third";
    //微信登录绑定手机号
    public static String WX_LOGIN_GIND_PHONE=BASEURL+"login/thirdBinding";
    /**
     * 公共接口
     */
    //发送验证码
    public static String SEND_MESSAGE = BASEURL + "communal/checkSmsCode";
    //发送邮箱验证码
    public static String EMAIL_SMS_CODE = BASEURL + "communal/emailSmsCode";
    //验证邮箱验证码
    public static String CHECK_EMS = BASEURL + "communal/checkEms";
    //图片上传
    public static String IMAGE_UPLOAD = BASEURL + "communal/imagesUpload";
    //按邮编获取地址
    public static String GET_AREA_INFO_FOR_ZIP = BASEURL + "communal/getAreaInfoForZip";
    //发送邮箱验证码
    public static String SEND_EMAIL_CODE = BASEURL + "communal/emailEmsCode";
    //验证短信验证码
    public static String CHECK_PHONE_CODE = BASEURL + "communal/checkSms";
    //按邮编/地区名称获取地区信息
    public static String GET_AREA_INFO = BASEURL + "communal/getAreaInfo";
    //首页设置地址
    public static String SET_ADDRESS = BASEURL + "communal/setAddress";
    //首页获取历史地址
    public static String GET_OLD_ADDRESS = BASEURL + "communal/getOldAddress";

    /**
     * 信息配置
     */
    //获取首次开启app轮播广告
    public static String GET_FIRST_ADVERT_LIST = BASEURL + "config/getFirstAdvertList";
    //获取开启app时图片广告
    public static String GET_ADVERT_LIST = BASEURL + "config/getAdvertList";
    //获取商品购买须知
    public static String GET_NOTES_BUY_INFO = BASEURL + "config/getNotesBuyInfo";
    //获取联系我们
    public static String GET_CONTACT_INFO = BASEURL + "config/getContactInfo";
    //获取关于我们
    public static String GET_MGA_INFO = BASEURL + "config/getMegaInfo";
    //首页-获取轮播
    public static String GET_BANNER_LIST = BASEURL + "config/getBannerList";
    //首页-获取顶部导航菜单
    public static String GET_INDEX_TOP_NAVIGATION = BASEURL + "config/getIndexTopNavigation";
    //首页-获取banner下二级导航
    public static String GET_INDEX_SECOND_NAVIGATION = BASEURL + "config/getIndexSecondNavigation";
    //首页-获取版块详细信息
    public static String GET_INDEX_BLOCK_LIST = BASEURL + "config/getIndexBlockList";
    //首页-版块二级页
    public static String GET_BLOCK_GOODS_LIST = BASEURL + "config/getBlockGoodsList";
    //首页-获取搜索栏关键字
    public static String GET_SEARCH_KEYWORD = BASEURL + "config/getSearchKeyword";
    //首页-删除搜索栏历史记录
    public static String DELETE_USED_KEYWORD = BASEURL + "config/deleteUsedKeyword";
    //首页-满免运费公告
    public static String GET_DISCOUNT_INFO = BASEURL + "config/getDiscountInfo";
    //获取版本号
    public static String GET_VERSION_CODE = BASEURL + "config/getVersionCode";
    //获取用户注册协议
    public static String GET_REGISTER_AGREEMENT = BASEURL + "config/getRegisterAgreement";

    /**
     * 商品
     */

    //获取商品列表
    public static String GET_GOODS_LIST = BASEURL + "goods/getGoodsList";
    //获取商品详情
    public static String GET_GOODS_INFO = BASEURL + "goods/getGoodsInfo";
    //猜你喜欢
    public static String GET_GUESS_GOODS = BASEURL + "goods/getGuessGoods";
    //商品详情页-获取其他规格
    public static String GET_ATTR_OTHER_LIST = BASEURL + "goods/getAttrOtherList";
    //添加/取消收藏
    public static String UPDATE_USERS_COLLECT = BASEURL + "goods/updateUsersCollect";
    //商品列表页-获取公司产地列表
    public static String GET_GOODS_PLACE_COMPANY_LIST = BASEURL + "goods/getGoodsPlaceCompanyList";

    /**
     * 分类
     */

    //获取分类列表
    public static String GET_CATE_LIST = BASEURL + "category/getCateList";
    //商品列表页-分类导航
    public static String GET_NAVI_GOODS_LIST = BASEURL + "category/getNaviGoodsList";

    /**
     * 购物车
     */

    //获取购物车商品列表
    public static String GET_CART_LIST = BASEURL + "cart/getCartList";
    //删除购物车商品
    public static String DELETE_CART_GOODS = BASEURL + "cart/deleteCartGoods";
    //添加/减少指定商品购物车数量
    public static String ADD_CART_GOODS = BASEURL + "cart/addCartGoods";
    //修改/选中商品数量计算总金额
    public static String UPDATE_GOODS_NUM = BASEURL + "cart/updateGoodsNum";
    //获取购物车指定商品数量
    public static String GET_CART_GOODS_NUMBER = BASEURL + "cart/getCartGoodsNumber";


    /**
     * 优惠券
     */
    //获取优惠券列表-我的优惠券
    public static String GET_COUPON_USER_LIST = BASEURL + "coupon/getCouponUserList";
    //获取优惠券列表-订单页
    public static String GET_COUPON_ORDER_LIST = BASEURL + "coupon/getCouponOrderList";
    //获取优惠券列表-领取优惠券页
    public static String GET_COUPON_ACTIVITY_LIST = BASEURL + "coupon/getCouponActivityList";
    //领取优惠券
    public static String GET_COUPON = BASEURL + "coupon/getCoupon";

    /**
     * 优惠活动
     */
    //组合优惠列表
    public static String GET_COMPOSE_LIST = BASEURL + "activity/getComposeList";
    //特价优惠列表
    public static String GET_SPECIAL_LIST = BASEURL + "activity/getSpecialList";
    //折扣优惠列表
    public static String GET_DISCOUNT_LIST = BASEURL + "activity/getDiscountList";

    /**
     * 消息
     */
    //获取消息列表
    public static String GET_ALL_LIST = BASEURL + "message/getAllList";
    //获取公告消息列表
    public static String GET_ARTICLE_LIST = BASEURL + "message/getArticleList";
    //获取公告消息详情
    public static String GET_ARTICLE_INFO = BASEURL + "message/getArticleInfo";
    //获取客服消息
    public static String GET_SERVICE_INFO = BASEURL + "message/getServiceInfo";
    //发送消息给客服
    public static String GADD_SERVICE_INFO = BASEURL + "message/addServiceInfo";
    //添加公告点击量
    public static String UPDATE_ARTICLE_CLICK_VOLUME = BASEURL + "message/updateArticleClickVolume";

    /**
     * 个人中心
     */
    //个人中心首页
    public static String MINE_USER_INFO = BASEURL + "user_info/myIndex";
    //个人资料
    public static String MINE_PERSON_DATA = BASEURL + "user_info/myInfo";
    //修改个人资料
    public static String MINE_UPDATE_PERSON_DATA = BASEURL + "user_info/updateInfo";
    //获取用户余额
    public static String MINE_USER_BALANCE = BASEURL + "user_info/getUserBalance";
    //获取流水账单
    public static String MINE_MY_ACCOUNT_LIST = BASEURL + "user_info/getUserbill";
    //我的邀请码
    public static String MINE_MY_INVITATION_CODE = BASEURL + "user_info/getInvitationInfo";
    //我邀请的好友列表
    public static String MINE_MY_INVITATION_FRIEND_LIST = BASEURL + "user_info/getInvitationList";
    //填写被邀请的邀请码
    public static String MINE_MY_INVITED = BASEURL + "user_info/addUserInvitationInfo";
    //修改密码
    public static String MINE_UPDATE_PWD = BASEURL + "user_info/updatePassword";

    //关于我们
    public static String ABOUT=BASEURL+"config/getMegaInfo";
    //联系我们
    public static String CONNECTION_MINE=BASEURL+"config/getContactInfo";
    //获取版本信息
    public static String GET_VERSION=BASEURL+"communal/getVersionUpdate";

    //绑定邮箱
    public static String MINE_BIND_EMAIL = BASEURL + "user_info/addEmail";
    //绑定手机号
    public static String MINE_BIND_PHONE = BASEURL + "user_info/addPhone";
    //我的收藏
    public static String MINE_MY_COLLECTION_LIST = BASEURL + "user_info/collectList";
    //删除我的收藏
    public static String MINE_DELETE_COLLECTION = BASEURL + "user_info/deleteCollect";

    //添加收货地址
    public static String MINE_ADD_ADDRESS = BASEURL + "user_info/addAddress";
    //地址列表
    public static String ADDRESS_MANAGER_LIST = BASEURL + "user_info/getAddressList";
    //删除地址
    public static String DELETE_ADDRESS = BASEURL + "user_info/deleteAddress";
    //修改地址
    public static String UPDATE_ADDRESS = BASEURL + "user_info/updateAddress";
    //修改默认地址
    public static String UPDATE_DEFAULT_ADDRESS = BASEURL + "user_info/updateDefaultAddress";
    //地区的数据
    public static String ADDRESS_AREA=BASEURL+"communal/getAreaInfo";
    //按邮编/地区名称获取地区信息
    public static String AARESS_AREA_DETAIL=BASEURL+"communal/getFirstAreaInfo";
    //意见反馈
    public static String FEEDBACK=BASEURL+"user_info/addFeedback";

    /**
     * 用户
     */
    //获取未读消息数量
    public static String GET_UNREAD_NUMBER = BASEURL + "user_info/getUnreadNumber";

    /**
     * 订单管理
     */
    //订单列表
    public static String ORDER_LIST = BASEURL + "order/getUserOrderList";
    //订单详情
    public static String ORDER_DETAIL = BASEURL + "order/getUserOrderInfo";
    //申请售后
    public static String APPLY_SALE = BASEURL + "order/addAfterSale";
    //取消申请售后
    public static String CANCEL_APPLY_SALE = BASEURL + "order/deleteAfterSale";
    //删除订单
    public static String DELETE_ORDER = BASEURL + "order/deleteOrder";


    /**
     * 我的优惠券
     */
    //我的优惠券列表
    public static String DISCOUNT_LIST=BASEURL+"coupon/getCouponUserList";
    //可领取的优惠券列表
    public static String RECEIVER_DISCOUNT_LIST=BASEURL+"coupon/getCouponActivityList";
    //领取优惠券
    public static String RECEIVER_DISCOUNT=BASEURL+"coupon/getCoupon";

    /**
     * 订单
     */
    //订单页-默认地址获取
    public static String GET_DEFAULT_ADDRESS = BASEURL + "user_info/getDefaultAddress";
    //订单页-获取订单信息列表（订单页/修改地址/修改优惠券）
    public static String GET_ORDER_LIST = BASEURL + "order/getOrderList";
    //订单页-获取送货时间
    public static String GET_DELIVERY_TIME_LIST = BASEURL + "order/getDeliveryTimeList";
    //订单页-提交订单生成订单，判断支付方式是否需要第三方支付
    public static String ADD_ORDER = BASEURL + "order/addOrder";
    //支付后查询订单状态
    public static String GET_PAY_AFTER_ORDER_TYPE = BASEURL + "order/getPayAfterOrderType";

    /**
     * 支付
     */
    //获取支付方式
    public static String GET_ORDER_PAY_LIST = BASEURL + "pay/getOrderPayList";
    //支付结果返回处理
    public static String CALL_BACK = BASEURL + "pay/callback";
    //提交充值订单
    public static String ADD_RECHARGE_ORDER = BASEURL + "pay/addRechargeOrder";
    //提交支付
    public static String ADD_PAYMENT = BASEURL + "pay/addPayment";
    //查询充值结果
    public static String GET_PAYMENT_RESULT = BASEURL + "pay/getPaymentResult";
}
