package com.liemo.shareresource;

/**
 * Created by Administrator on 2016/7/1 0001.
 */
public class Url {

//    public static final String HOST = "https://m.liemoapp.com/BBC/"; //阿里云
    public static final String HOST = "http://192.168.0.111:8080/BBC/";//本地

    /**
     * 网络存放的logo
     */
    public static final String LOGO_URL_REMOTE = "http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-b/member.png";
    //-----------------------------------新版本接口 bbc.com.moteduan_lib2

    /**
     * 已开通的城市
     */
    public static final String openCity = HOST + "use/getCity_Of_Opened";

    /**
     * 更新当前定位地址
     */
    public static final String updateAddress = HOST + "mregister/getCurrent";
    /**
     * 点击退出登录调用
     * para  member_id
     */
    public static final String signOut = HOST + "mregister/signOut";
    /**
     * 发现消息列表
     */
    public static final String momentsMsgList = HOST + "dynamic/circlesDynamic";

    /**
     * 查看自己发过的动态列表
     */
    public static final String peekMomentList = HOST + "dynamic/viewOneSelf";

    /**
     * 查看别人的动态列表
     */
    public static final String peekOtherMomentList = HOST + "dynamic/viewMemberUserCircles";

    /**
     * 删除动态
     */
    public static final String deleteMoment = HOST + "dynamic/deleteMyCircles";

    /**
     * 动态详情接口
     */
    public static final String momentDetailas = HOST + "dynamic/getViewCircles";
    /**
     * 动态详情接口2
     * 多增加了两个字段
     */
    public static final String momentDetailas2 = HOST + "dynamic/getNewViewCircles";
    /**
     * 关注相关接口
     */
    public static final String getWatchList = HOST + "m_schedule/myConcern";
    /*提现页面展示的提现规则。*/
    public static final String getWithdrawTips = HOST + "use_utils/getRulesWithdrawal";

    /*发布技能时候获取价格范围*/
    public static final String getSkillMoneyRange = HOST + "use_utils/getSheduleMoney";
    /**
     * 协议
     */
    public static final String agreement = HOST + "h55/H5-user/html/agreement.html";

    /**
     * 查看自己发过的档期
     */
    public static final String peekSelfSchedule = HOST + "m_schedule/resultSchedule";
    /**
     * 签到
     */
    public static final String sign = HOST + "use/member_Signs";
    /**
     * 反馈
     */
    public static final String feedback = HOST + "user/feedBack";
    /**
     * 上传照片到相册
     */
    public static final String uploadAlbumsPhoto = HOST + "dynamic/circlesAblum";
    /**
     * 删除相册照片
     */
    public static final String deleteAlbumPhoto = HOST + "dynamic/deleteMyAlbum";

    /**
     * 查看相册，看谁传谁id..
     */
    public static final String peekAlbums = HOST + "dynamic/myAlbum";
    /**
     * 查看用户主页，看谁传谁id
     */
    public static final String peekUserInfoPage = HOST + "use/getUserHome_Modular";
    /**
     * 模特认证
     */
    public static final String modelAuthentication = HOST + "m_schedule/memberAuthentication";
    /**
     * 获取模特认证数据
     */
    public static final String getAuthenticationState = HOST + "m_schedule/checkAuthentication";

    /**
     * 获取模特认证的状态，如果code返回200则是认证通过
     */
    public static final String getModelAuthState = HOST + "m_schedule/getModel";

    /**
     * 同意用户报名
     */
    public static final String agreeUserApply = HOST + "m_traderorder/agreeAppointment";

    /**
     * 拒绝用户报名
     */
    public static final String declineUserAapply = HOST + "m_traderorder/removeAppointment";

    /**
     * 删除订单
     */
    public static final String deleteOrder = HOST + "m_traderorder/removeOrder";
    /**
     * 开始约会
     */
    public static final String startInviteToMulti = HOST + "m_schedule/startAppointments";
    public static final String startInvite = HOST + "m_schedule/startAppointment";

    /**
     * 我的报名，取消约会
     */
    public static final String cancelInviteMineApply = HOST + "m_schedule/trader_Of_Orders";
    /**
     * 预约、事实、取消约会
     */
    public static final String cancelInvite = HOST + "m_schedule/cancelAppointment";
    /**
     * 取消订单报名
     */
    public static final String cancelApply = HOST + "m_schedule/cancelRegistration";
    /**
     * 获取后台的系统时间
     * 接口返回时间字段  time
     */
    public static final String getSystemTime = HOST + "time/getSystemTime";
    /**
     * 预约订单详情
     */
    public static final String appointmentInviteOrderDetails = HOST + "m_traderorder/getIntoTraderOrderAppointment";
    /**
     * 实时订单详情
     */
    public static final String realtimeInviteOrderDetails = HOST + "m_traderorder/getIntoTraderOrder";
    /**
     * 报名订单详情
     */
    public static final String applyInviteOrderDetails = HOST + "m_traderorder/getIntoMySignUp";
    /**
     * 订单列表进入详情
     */
    public static final String inviteOrderDetails = HOST + "m_schedule/enterTrader";
    /**
     * 预约订单列表
     */
    public static final String appointmentOrderList = HOST + "m_traderorder/traderOrderAppointment";
    /**
     * 实时订单列表
     */
    public static final String realtimeOrderList = HOST + "m_traderorder/traderOrder";
    /**
     * 我的报名列表
     */
    public static final String mineApplyList = HOST + "m_traderorder/mySignUp";
    /**
     * 报名用户发的邀约
     */
    public static final String applyInviteOrder = HOST + "m_schedule/startRegistration";
    /**
     * 获取模特选择过的邀约类型
     */
    public static final String obtainInviteType = HOST + "mregister/getData";
    /**
     * 我的页面数据
     */
    public static final String minePage = HOST + "use/getHomePage";

    /**
     * 模特发布档期
     */
    public static final String publishInvite = HOST + "m_schedule/addSkill";

    /**
     * 用户订单列表导航栏标签
     */
    public static final String inviteNavigationTab = HOST + "u_trader/getTypes";
    /**
     * 模特可选职业数据获取
     */
    public static final String professionList = HOST + "m_schedule/get_Type_Appointment";
    /**
     * 用户邀约列表
     */
    public static final String userInviteList = HOST + "m_schedule/serchShedule";
    /**
     * 刚注册完善资料
     */
    public static final String editData = HOST + "mregister/editMemberMess";
    /**
     * 个人中心编辑资料
     */
    public static final String personageEditData = HOST + "mregister/editData";
    /**
     * 检查这个第三方是否绑定过手机号，根据返回的code进行下步操作
     */
    public static final String phone_Of_Verify = HOST + "newregister/phone_Of_Verify";
    /**
     * 移除在用户端绑定的手机号
     */
    @Deprecated
    public static final String removeMobileOfUser = HOST + "mregister/removeMobileOfUser";
    /**
     * 绑定完手机号登录
     */
    public static final String registerLogin = HOST + "newregister/registerLogin";
    /**
     * 手机号绑定到第三方
     */
    public static final String bindToPhone = HOST + "newregister/bindToPhoneNumber";
    /**
     * 忘记密码
     */
    public static final String uforgetPwd = HOST + "mlogin/mforgetPwd";
    /**
     * 手机号是否存在和绑定过
     */
    public static final String exists_mobile = HOST + "newregister/exists_mobile";
    /**
     * 手机号注册
     */
    public static final String mregister = HOST + "mlogin/mregister";
    /**
     * 快速登录
     */
    public static final String quick_Logon_Of_Member = HOST + "sms/quick_Logon_Of_Member";
    /**
     * 获取验证码
     */
    public static final String ugetCode = HOST + "sms/mgetCode";
    /**
     * 手机号普通登录
     */
    public static final String phonelogin = HOST + "mlogin/login";
    // 发送评论
    public static final String sendComment = HOST + "dynamic/releaseComment";
    // 评论列表
    public static final String commentList = HOST + "dynamic/circlesComment";
    // 送花列表
    public static final String sendFlowerList = HOST + "dynamic/circlesGift";
    // 动态获取数据
    public static final String getMomentsList = HOST + "dynamic/viewCircles";
    // 发布动态
    public static final String pulishMoments = HOST + "dynamic/releaseCircle";
    // 关注
    public static final String watchMoments = HOST + "u_trader/preservationR";
    // 点赞
    public static final String clickPraise = HOST + "dynamic/giveZan";

    public static class Moments {
        // 发送评论
        public static final String sendComment = HOST + "dynamic/releaseComment";
        // 评论列表
        public static final String commentList = HOST + "dynamic/circlesComment";
        // 送花列表
        public static final String sendFlowerList = HOST + "dynamic/circlesGift";
        // 动态获取数据
        public static final String getMomentsList = HOST + "dynamic/viewCircles";
        // 发布动态
        public static final String pulishMoments = HOST + "dynamic/releaseCircle";
        // 关注
        public static final String watchMoments = HOST + "u_trader/preservationR";
        // 点赞
        public static final String clickPraise = HOST + "dynamic/giveZan";
        // 送花
        public static final String sendFlower = HOST + "dynamic/giveGift";
        // 动态页面H5分享
        public static final String momentsDetailsShare = HOST + "dynamic/index.html?contentId=";
    }

    public static class Home {
        public static final String homePage = HOST + "m_schedule_modular/getHomePage";
    }

    public static class Invite {
        /**
         * 娱乐列表
         */
        public static final String play = HOST + "m_schedule_modular/serchShedule_One_Modular";
        /**
         * 商业列表
         */
        public static final String business = HOST + "m_schedule_modular/serchShedule_Three_Modular";
        /**
         * 通告列表
         */
        public static final String notice = HOST + "m_schedule_modular/serchEntertainment_Two_Modular";

        /**
         * 报名通告订单
         */
        public static final String applyNotice = HOST + "m_schedule_modular/startRegistration_Modular";

        /**
         * 我的通告列表
         */
        public static final String mineNoticeList = HOST + "m_traders_orders/getBusinessAffairs";

        /**
         * 查看通告详情
         */
        public static final String mineNoticeDetails = HOST + "m_traders_orders/enterBusinessAffairs";
        /**
         * 查看未报名的通告详情
         */
        public static final String mineNoticeDetailsDidNotSignUp = HOST + "m_schedule_modular/enterTrader_Modular";

        /**
         * 删除通告
         */
        public static final String deleteNotice = HOST + "m_traders_orders/deleteBusinessAffairs";

        /**
         * 取消报名通告
         */
        public static final String cancelNotice = HOST + "m_schedule_modular/cancelRegistration_Modular";
        /**
         * 取消报名通告订单
         */
        public static final String cancelNoticeOrder = HOST + "m_schedule_modular/trader_Of_Orders_Modular";

        /**
         * 开始通告
         */
        public static final String startNotice = HOST + "m_schedule_modular/startAppointments_Modular";
    }

    public static class NavigationLabel {
        /**
         * 娱乐和商业标签
         */
        public static final String playAndBusiness = HOST + "m_schedule_modular/getTypeOfNavigation";

        /**
         * 全部
         */
        public static final String all = HOST + "m_schedule_modular/getTypeOfNavigation_All_Modular";

        /**
         * 娱乐标签
         */
        public static final String play = HOST + "m_schedule_modular/getTypeOf_OneNavigation";

        /**
         * 商业标签
         */
        public static final String business = HOST + "m_schedule_modular/getTypeOf_Three_Navigation";

        /**
         * 通告标签
         */
        public static final String notice = HOST + "m_schedule_modular/getTypeOfNavigation_Two_Modular";
    }


    public static class Wallet {
        /**
         * 钱包信息包含消费信息列表
         */
        public static final String walletConsumeList = HOST + "m_traders_orders/myConsumptionMemberRecord";

        /**
         * 钱包信息包含提现信息列表
         */
        public static final String walletWithdrawList = HOST + "m_traders_orders/WhithMemberRecord";

        /**
         * 提现详情
         */
        public static final String withdrawDetails = HOST + "m_traders_orders/getWhith";
        /**
         * 钱包（除提现）订单详情   type = 5 时调这个，type在拉取账单明细列表时会返回
         */
        public static final String walletConsumeDetails = HOST + "m_traders_orders/myMemberAppointment";

        /**
         * 钱包（除提现）订单详情
         * type = 1 时调这个，type在拉取账单明细列表时会返回
         */
        public static final String consumeDetails = HOST + "order/myMemberAppointment";
    }

    /**
     * 发布技能前调用接口返回200进入发布技能页面
     */
    public static final String getBeforeAddSkill = HOST + "minvite/beforeAddSkill";

    /**
     * 点击提现按钮
     */
    public static final String clickbutton = HOST + "order/clickButton";


    //---------------------------------------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------老版本接口-------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------------------------------
    /**
     * 获取用户信息
     */
    public static final String getUserInfo = HOST + "user/getNickPhotom";
    /**
     * 获取模币余额
     */

    public static final String getBlance = HOST + "order/getBlance";

    /**
     * 提交邀请码
     */
    public static final String getInviteCode = HOST + "mregister/getInviteCode";

    /**
     * uregister/getregisId
     */
    public static final String getregisId = HOST + "mregister/getregisId";

    /**
     * 报名
     */
//    public static final String apply_invite = HOST + "minvite/apply_invite";
    public static final String apply_invite = HOST + "minvite/apply_invitenew";

    /**
     * 校验短信验证码
     */
    public static final String checkSMS = HOST + "sms/check";


    /**
     * 发送短信验证码
     */
    public static final String sendSMS = HOST + "sms/bound";

    /**
     * 重置密码
     */
    public static final String find_pwd = HOST + "user/find_pwd";

    /**
     * 视频认证
     */
    public static final String video_rz = HOST + "rz/video_rz";
    /**
     * 出车状态
     */
    public static final String workState = HOST + "minvite/workState";
    /**
     * 编辑发布
     */
    public static final String editSkill = HOST + "minvite/editSkillOne";

    /**
     * 发布技能
     */
    public static final String addSkill = HOST + "minvite/addSkillOne";

    /**
     * 城市区
     */
    public static final String hatArea = HOST + "m_schedule/hatArea";

    /**
     * 商圈
     */
    public static final String shopArea = HOST + "m_schedule/shopArea";
    /**
     * 微信登录
     */
    public static final String wxLogin = HOST + "mregister/wxLogin";

    /**
     * 版本更新
     */
    public static final String update = HOST + "user/updateMember";

    /**
     * 修改密码
     */
    public static final String changePwd = HOST + "user/change_pwd";

    /**
     * 手机注册
     */
    public static final String phoneRegister = HOST + "mregister";

    /**
     * 请求短信验证码
     */
    public static final String sms = HOST + "sms/mgetCode";

    /**
     * 校验短信验证码
     */
    public static final String checkSms = HOST + "sms/check";

    /**
     * 手机号/应用账号 登录接口
     */
    public static final String login = HOST + "mlogin/login";
    /**
     * 找回密码
     */

    public static final String findpwd = HOST + "user/find_pwd";
    /**
     * 更新我的位置信息
     */
    public static final String uploadGPS = HOST + "home/uploadGPS";
    /**
     * 身份认证
     */
    public static final String real_name = HOST + "rz/real_name";
    /**
     * 车辆认证
     */
    public static final String car_rz = HOST + "rz/car_rz";
    /**
     * 模特认证
     */
    public static final String model_rz = HOST + "rz/model_rz";
    /**
     * 商家认证
     */
    public static final String merchant_rz = HOST + "rz/merchant_rz";
    /**
     * 上传形象照
     */
    public static final String shangChuanXingXiangZhao = HOST + "user/personPhoto";
    /**
     * 筛选模约信息
     */
    public static final String chose = HOST + "minvite/searchInvite";
    /**
     * 修改用户信息
     */
    public static final String edit = HOST + "user/editUserInfo";

    /**
     * 提现
     */
    public static final String withdraw = HOST + "order/withdrawals";

    /**
     * 设置提现密码
     */
    public static final String setpwd = HOST + "order/editOutPass";
    /**
     * 提现记录
     */
    public static final String withdrawrecorde = HOST + "order/withdraws";




}
