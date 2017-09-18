package bbc.com.moteduan_lib.constant;

import android.os.Environment;

/**
 * Created by zhang on 2016/11/16.
 */
public class Constants {
    /**
     * SD卡文件保存位置
     */

    public final static String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/moyue/";

    /**
     * 微信appid
     */
    public static String WEIXIN_APP_ID = "wx188b371e488252ac";
    public static String WEIXIN_APP_SECRET = "4ad82496bf06eb1ae1e7e6ef63eddc68";
    /**
     * qq
     */
    public static final String QQ_APP_ID = "1105916914";

    /**
     * 登录注册
     */
    public static final String BANG_DING = "bangding";
    public static final String REGISTER = "register";
    public static final String resetpwd = "resetpwd";

    /**
     * 发布动态  图片类型
     */
    public static final int IMAGE_TYPE = 1;

    public static class PhoneConstants {
        public static final String CONTACT_US_PHONE = "4008238386";
    }


    /**
     * 登录成功
     */
    public static final String ACTION_LOGIN_SUCCESS = "bbc.com.moteduan.login";
    /**
     * 退出登录成功
     */
    public static final String ACTION_LOGOUT_SUCCESS = "bbc.com.moteduan.logout";

}
