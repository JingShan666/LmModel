package bbc.com.moteduan_lib2.login;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mob.tools.utils.UIHandler;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.tauth.Tencent;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.app.App;
import bbc.com.moteduan_lib.base.BaseTranslucentActivity;
import bbc.com.moteduan_lib.bean.LoginBean;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.constant.Constants;
import bbc.com.moteduan_lib.log.LogDebug;

import com.liemo.shareresource.Url;

import bbc.com.moteduan_lib.tools.CountDownTimerUtils;
import bbc.com.moteduan_lib.tools.PhoneUrils;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.ShareSDKR;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.ToastUtil;


public class LoginActivity extends BaseTranslucentActivity implements View.OnClickListener, PlatformActionListener, Handler.Callback {
    public static final String TAG = "LoginActivity";
    private ImageView img_logo;
    private TextView phone_login;
    private TextView phone_login_line;
    private TextView fast_login;
    private TextView fast_login_line;
    private LinearLayout chose_login_way;
    private EditText user_account;
    private LinearLayout user_account_ll;
    private TextView user_account_line;
    private EditText user_pwd;
    private LinearLayout user_pwd_ll;
    private TextView user_pwd_line;
    private TextView forget_pwd;
    private EditText phoneNum;
    private LinearLayout phone_ll;
    private TextView phoneNum_line;
    private EditText checkCode;
    private LinearLayout checkCode_ll;
    private TextView checkCode_line;
    private TextView userAgreement;
    private LinearLayout userAgreement_ll;
    private RelativeLayout activity_login;
    private ImageView seeorno;
    private TextView login;
    private TextView register;
    private int loginType = 1; //1 手机号登录 2 手机号快速登录

    //----------------------新浪微博授权获取用户信息相关------------------------
    private static final int MSG_TOAST = 1;
    private static final int MSG_ACTION_CCALLBACK = 2;
    private static final int MSG_CANCEL_NOTIFY = 3;

    private ImageView weixin_login;
    private ImageView qq_login;
    private ImageView weibo_login;
    private TextView getcode;
    private String quickSmsCode;
    private String quickphonenum;
    private static Tencent mTencent;
    /**
     * IWXAPI 是第三方app和微信通信的openapi接口
     */
    private static boolean isServerSideLogin = false;
    private String openid = null;
    private QQAuth mQQAuth;

    private boolean isChecked = false;
    private ImageView back;
    private CountDownTimerUtils countDownTimerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    @Override
    public void initView() {
        back = (ImageView) findViewById(R.id.back);
        img_logo = (ImageView) findViewById(R.id.img_logo);
        img_logo.setOnClickListener(this);
        phone_login = (TextView) findViewById(R.id.phone_login);
        phone_login.setOnClickListener(this);
        phone_login_line = (TextView) findViewById(R.id.phone_login_line);
        phone_login_line.setOnClickListener(this);
        fast_login = (TextView) findViewById(R.id.fast_login);
        fast_login.setOnClickListener(this);
        fast_login_line = (TextView) findViewById(R.id.fast_login_line);
        fast_login_line.setOnClickListener(this);
        chose_login_way = (LinearLayout) findViewById(R.id.chose_login_way);
        chose_login_way.setOnClickListener(this);
        user_account = (EditText) findViewById(R.id.user_account);
        user_account.setOnClickListener(this);
        user_account_ll = (LinearLayout) findViewById(R.id.user_account_ll);
        user_account_ll.setOnClickListener(this);
        user_account_line = (TextView) findViewById(R.id.user_account_line);
        user_account_line.setOnClickListener(this);
        user_pwd = (EditText) findViewById(R.id.user_pwd);
        user_pwd.setOnClickListener(this);
        user_pwd_ll = (LinearLayout) findViewById(R.id.user_pwd_ll);
        user_pwd_ll.setOnClickListener(this);
        user_pwd_line = (TextView) findViewById(R.id.user_pwd_line);
        user_pwd_line.setOnClickListener(this);
        forget_pwd = (TextView) findViewById(R.id.forget_pwd);
        forget_pwd.setOnClickListener(this);
        phoneNum = (EditText) findViewById(R.id.phoneNum);
        phoneNum.setOnClickListener(this);
        phone_ll = (LinearLayout) findViewById(R.id.phone_ll);
        phone_ll.setOnClickListener(this);
        phoneNum_line = (TextView) findViewById(R.id.phoneNum_line);
        phoneNum_line.setOnClickListener(this);
        checkCode = (EditText) findViewById(R.id.checkCode);
        checkCode_ll = (LinearLayout) findViewById(R.id.checkCode_ll);
        checkCode_ll.setOnClickListener(this);
        checkCode_line = (TextView) findViewById(R.id.checkCode_line);
        checkCode_line.setOnClickListener(this);
        userAgreement = (TextView) findViewById(R.id.userAgreement);
        userAgreement.setOnClickListener(this);
        userAgreement_ll = (LinearLayout) findViewById(R.id.userAgreement_ll);
        userAgreement_ll.setOnClickListener(this);
        activity_login = (RelativeLayout) findViewById(R.id.activity_login);
        activity_login.setOnClickListener(this);
        seeorno = (ImageView) findViewById(R.id.seeOrNo);
        seeorno.setOnClickListener(this);
        login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(this);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
        weixin_login = (ImageView) findViewById(R.id.weChat_login);
        weixin_login.setOnClickListener(this);
        qq_login = (ImageView) findViewById(R.id.qq_login);
        qq_login.setOnClickListener(this);
        weibo_login = (ImageView) findViewById(R.id.weibo_login);
        weibo_login.setOnClickListener(this);
        getcode = (TextView) findViewById(R.id.getCode);
        getcode.setOnClickListener(this);
        back.setOnClickListener(this);
    }


    @Override
    public void initData() {
        countDownTimerUtils = new CountDownTimerUtils(getcode, 60000, 1000);
   /*     // Tencent类是SDK的主要实现类，通过此访问腾讯开放的OpenAPI。
        mQQAuth = QQAuth.createInstance(Constant.QQ_APP_ID,this.getApplicationContext());*/
        // 实例化
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String from = intent.getStringExtra("from");
        if ("WXEntryActivity".equals(from)) {
            finish();
        } else if ("BingPhoneOrResetPasswordActivity".equals(from)) {
            finish();
        } else if ("threeLogin".equals(from)) {
            finish();
        } else if ("SystemRecommentActivity".equals(from)) {
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.phone_login) {
            loginType = 1;
            user_account_ll.setVisibility(View.VISIBLE);
            user_account_line.setVisibility(View.VISIBLE);
            user_pwd_ll.setVisibility(View.VISIBLE);
            user_pwd_line.setVisibility(View.VISIBLE);
            seeorno.setVisibility(View.VISIBLE);
            forget_pwd.setVisibility(View.VISIBLE);

            phone_ll.setVisibility(View.INVISIBLE);
            checkCode.setVisibility(View.INVISIBLE);
            checkCode_line.setVisibility(View.INVISIBLE);
            checkCode_ll.setVisibility(View.INVISIBLE);
            fast_login.setTextColor(getResources().getColor(R.color.color_757575));
            phone_login.setTextColor(getResources().getColor(R.color.black_theme));
            fast_login_line.setVisibility(View.INVISIBLE);
            phone_login_line.setVisibility(View.VISIBLE);

        } else if (i == R.id.fast_login) {
            loginType = 2;
            user_account_ll.setVisibility(View.INVISIBLE);
            user_account_line.setVisibility(View.INVISIBLE);
            user_pwd_ll.setVisibility(View.INVISIBLE);
            user_pwd_line.setVisibility(View.INVISIBLE);
            seeorno.setVisibility(View.INVISIBLE);
            forget_pwd.setVisibility(View.INVISIBLE);

            phone_ll.setVisibility(View.VISIBLE);
            checkCode.setVisibility(View.VISIBLE);
            checkCode_line.setVisibility(View.VISIBLE);
            checkCode_ll.setVisibility(View.VISIBLE);

            fast_login.setTextColor(getResources().getColor(R.color.black_theme));
            phone_login.setTextColor(getResources().getColor(R.color.color_757575));
            fast_login_line.setVisibility(View.VISIBLE);
            phone_login_line.setVisibility(View.INVISIBLE);

        } else if (i == R.id.login) {
            if (loginType == 1) {
                phoneLogin();
            } else if (loginType == 2) {
                quickLogin();
            }
        } else if (i == R.id.register) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();

        } else if (i == R.id.weChat_login) {
            if (!App.wxapi.isWXAppInstalled()) {
                Toast.makeText(LoginActivity.this, "请安装微信客户端之后再进行登录", Toast.LENGTH_SHORT);
                return;
            }
            getCode();

        } else if (i == R.id.weibo_login) {

            thirdSinaLogin();


        } else if (i == R.id.qq_login) {
            Platform pf = ShareSDK.getPlatform(QQ.NAME);
            pf.SSOSetting(false);
            //设置监听
            pf.setPlatformActionListener(LoginActivity.this);
            //获取登陆用户的信息，如果没有授权，会先授权，然后获取用户信息
            pf.authorize();

        } else if (i == R.id.getCode) {
            requestPhoneCode();
        } else if (i == R.id.seeOrNo) {
            if (!isChecked) {
                // 显示密码
                isChecked = true;
                seeorno.setImageResource(R.drawable.icon_yanjing);
                user_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                // 隐藏密码
                isChecked = false;
                seeorno.setImageResource(R.drawable.icon_biyan);
                user_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            //设置光标位置
            user_pwd.setSelection(checkCode.getText().length());

        } else if (i == R.id.back) {
            finish();

        } else if (i == R.id.forget_pwd) {
            Intent intent = new Intent(LoginActivity.this, BingPhoneOrResetPasswordActivity.class);
            startActivity(intent);
        }
    }

    private void phoneLogin() {
        String phonenumString = user_account.getText().toString().trim();
        if (!PhoneUrils.isPhone(phonenumString)) {
            toast.showText("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(phonenumString)) {
            toast.showText("请输入手机号");
            return;
        }
        String usePwd = user_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(usePwd)) {
            toast.showText("请输入密码");
            return;
        }
        HashMap<String, Object> phoneLoginMap = new HashMap<>();
        phoneLoginMap.put("mobile", phonenumString);
        phoneLoginMap.put("pwd", usePwd);
        Xutils.post(Url.phonelogin, phoneLoginMap, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.log(LoginActivity.class.getSimpleName(), "phoneLogin_Of_Use result=" + result);
                Gson gson = new Gson();
                LoginBean loginBean = gson.fromJson(result, LoginBean.class);
                String code = loginBean.getCode();
                String tips = loginBean.getTips();
                if ("200".equals(code)) {
                    LoginBean.DataBean data = loginBean.getData();
                    String m_tag = data.getM_tag();
                    if (TextUtils.isEmpty(m_tag)) {
                        //没有完善过资料
                        startActivity(new Intent(LoginActivity.this, CompleteSelfInfoActivity.class)
                                .putExtra("data", loginBean));
                        finish();
                    } else {
                        SpDataCache.saveSelfInfo(LoginActivity.this, result);
                        Intent intent = new Intent();
                        intent.setAction(Constants.ACTION_LOGIN_SUCCESS);
                        sendBroadcast(intent);
                        //完善过资料
                        finish();
                    }
                } else {
                    toast.showText(tips);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 快速登录
     */
    private void quickLogin() {
        quickphonenum = phoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(quickphonenum)) {
            toast.showText("手机号不能为空");
            return;
        }
        quickSmsCode = checkCode.getText().toString().trim();
        if (TextUtils.isEmpty(quickSmsCode)) {
            toast.showText("验证码不能为空");
            return;
        }
        HashMap<String, Object> getCodeHashMap = new HashMap<>();
        getCodeHashMap.put("mobile", quickphonenum);
        getCodeHashMap.put("purpose", "kdenglu");
        getCodeHashMap.put("smsCode", quickSmsCode);
        Xutils.post(Url.quick_Logon_Of_Member, getCodeHashMap, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.log("LoginActivity", "quick_Logon_Of_Use result=" + result);
                Gson gson = new Gson();
                LoginBean loginBean = gson.fromJson(result, LoginBean.class);
                String code = loginBean.getCode();
                String tips = loginBean.getTips();
                if ("200".equals(code)) {
                    LoginBean.DataBean data = loginBean.getData();
                    String m_tag = data.getM_tag();
                    if (TextUtils.isEmpty(m_tag)) {
                        //没有完善过资料
                        startActivity(new Intent(LoginActivity.this, CompleteSelfInfoActivity.class)
                                .putExtra("data", loginBean));
                        finish();
                    } else {
                        SpDataCache.saveSelfInfo(LoginActivity.this, result);
                        Intent intent = new Intent();
                        intent.setAction(Constants.ACTION_LOGIN_SUCCESS);
                        sendBroadcast(intent);
                        //完善过资料
                        finish();
                    }
                } else {
                    toast.showText(tips);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    /**
     * 获取验证码
     */
    private void requestPhoneCode() {
        quickphonenum = phoneNum.getText().toString().trim();
        if (!PhoneUrils.isPhone(quickphonenum)) {
            toast.showText("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(quickphonenum)) {
            toast.showText("请输入手机号");
            return;
        }
        HashMap<String, Object> getCodeHashMap = new HashMap<>();
        getCodeHashMap.put("mobile", quickphonenum);
        getCodeHashMap.put("purpose", "kdenglu");
        Xutils.post(Url.ugetCode, getCodeHashMap, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("LoginActivity", "result=" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String tips = jsonObject.getString("tips");
                    if ("200".equals(code)) {
//                        quickSmsCode = jsonObject.getString("data");
                        countDownTimerUtils.start();
                    } else {
                        toast.showText(tips);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    /**
     * //-----------------------------------------------------新浪微博授权相关-----------------------
     * <p>
     * /**
     * 新浪微博授权、获取用户信息页面
     */
    private void thirdSinaLogin() {
        //初始化新浪平台
        Platform pf = ShareSDK.getPlatform(SinaWeibo.NAME);
        pf.SSOSetting(false);
        //设置监听
        pf.setPlatformActionListener(LoginActivity.this);
        //获取登陆用户的信息，如果没有授权，会先授权，然后获取用户信息
        pf.authorize();
    }

    /**
     * 新浪微博授权成功回调页面
     */
    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        /** res是返回的数据，例如showUser(null),返回用户信息，对其解析就行
         *   http://sharesdk.cn/androidDoc/cn/sharesdk/framework/PlatformActionListener.html
         *   1、不懂如何解析hashMap的，可以上网搜索一下
         *   2、可以参考官网例子中的GetInforPage这个类解析用户信息
         *   3、相关的key-value,可以看看对应的开放平台的api
         *     如新浪的：http://open.weibo.com/wiki/2/users/show
         *     腾讯微博：http://wiki.open.t.qq.com/index.php/API%E6%96%87%E6%A1%A3/%E5%B8%90%E6%88%B7%E6%8E%A5%E5%8F%A3/%E8%8E%B7%E5%8F%96%E5%BD%93%E5%89%8D%E7%99%BB%E5%BD%95%E7%94%A8%E6%88%B7%E7%9A%84%E4%B8%AA%E4%BA%BA%E8%B5%84%E6%96%99
         *
         */
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    /**
     * 取消授权
     */
    @Override
    public void onCancel(Platform platform, int action) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    /**
     * 授权失败
     */
    @Override
    public void onError(Platform platform, int action, Throwable t) {
        t.printStackTrace();
        t.getMessage();
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_TOAST: {
                String text = String.valueOf(msg.obj);
                Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_ACTION_CCALLBACK: {
                switch (msg.arg1) {
                    case 1: {
                        // 成功, successful notification
                        //授权成功后,获取用户信息，要自己解析，看看oncomplete里面的注释
                        //ShareSDK只保存以下这几个通用值
                        final Platform pf = (Platform) msg.obj;
                        Log.e("sharesdk use_id", pf.getDb().getUserId()); //获取用户id
                        Log.e("sharesdk use_name", pf.getDb().getUserName());//获取用户名称
                        Log.e("sharesdk use_icon", pf.getDb().getUserIcon());//获取用户头像
                        LogDebug.log(TAG, "授权成功"
                                + "\n用户id:"
                                + pf.getDb().getUserId()
                                + "\n" + "获取用户名称"
                                + pf.getDb().getUserName()
                                + "\n" + "获取用户头像"
                                + pf.getDb().getUserIcon()
                                + "\n platformName = " + pf.getName());
                        //mPf.author()这个方法每一次都会调用授权，出现授权界面
                        //如果要删除授权信息，重新授权
                        //mPf.getDb().removeAccount();
                        //调用后，用户就得重新授权，否则下一次就不用授权
                        /**
                         * 根据用户信息登录
                         */
                        Map<String, Object> map = new HashMap<>();
                        final String unionid = pf.getDb().getUserId();
                        if ("SinaWeibo".equals(pf.getName())) {
                            map.put("m_wb_account", unionid);
                        } else if ("QQ".equals(pf.getName())) {
                            map.put("m_qq_account", unionid);
                        }
                        Xutils.post(Url.phone_Of_Verify, map, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                LogDebug.log(TAG, "phone_Of_Verifyresult==" + result);
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String code = jsonObject.getString("code");
                                    String tips = jsonObject.getString("tips");

                                    if ("200".equals(code)) {
                                        // 调用后台第三方注册接口,成功后直接返回用户信息
                                        Intent intent = new Intent(LoginActivity.this, BingPhoneOrResetPasswordActivity.class);
                                        if ("SinaWeibo".equals(pf.getName())) {
                                            intent.putExtra("from", "weibo");
                                        } else if ("QQ".equals(pf.getName())) {
                                            intent.putExtra("from", "qq");
                                        }
                                        intent.putExtra("account", pf.getDb().getUserId());
                                        intent.putExtra("m.m_nick_name", pf.getDb().getUserName());
                                        intent.putExtra("m.m_head_photo", pf.getDb().getUserIcon());
                                        intent.putExtra("m.m_sex", 2);
                                        intent.putExtra("intcoding", 0);
                                        startActivity(intent);
                                        finish();
                                    } else if ("201".equals(code)) {
                                        // 去绑定手机号
                                        Intent intent = new Intent(LoginActivity.this, BingPhoneOrResetPasswordActivity.class);
                                        if ("SinaWeibo".equals(pf.getName())) {
                                            intent.putExtra("from", "weibo");
                                        } else if ("QQ".equals(pf.getName())) {
                                            intent.putExtra("from", "qq");
                                        }
                                        intent.putExtra("account", pf.getDb().getUserId());
                                        intent.putExtra("m.m_nick_name", pf.getDb().getUserName());
                                        intent.putExtra("m.m_head_photo", pf.getDb().getUserIcon());
                                        intent.putExtra("m.m_sex", 2);
                                        intent.putExtra("intcoding", 1);
                                        startActivity(intent);
                                        finish();
                                    } else if ("202".equals(code)) {
                                        // 成功登录，返回用户数据
                                        Gson gson = new Gson();
                                        LoginBean loginBean = gson.fromJson(result, LoginBean.class);
                                        LoginBean.DataBean data = loginBean.getData();
                                        String m_tag = data.getM_tag();
                                        if (TextUtils.isEmpty(m_tag)) {
                                            //没有完善过资料
                                            startActivity(new Intent(LoginActivity.this, CompleteSelfInfoActivity.class)
                                                    .putExtra("data", loginBean));
                                            finish();
                                        } else {
                                            SpDataCache.saveSelfInfo(LoginActivity.this, result);
                                            Intent intent = new Intent();
                                            intent.setAction(Constants.ACTION_LOGIN_SUCCESS);
                                            sendBroadcast(intent);
                                            //完善过资料
                                            finish();
                                        }
                                    } else {
                                        ToastUtil.show(LoginActivity.this, tips);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                LogDebug.err("onError" + ex.toString());
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {
                                LogDebug.err("onCancelled" + cex.toString());
                            }

                            @Override
                            public void onFinished() {
                            }
                        });
                    }
                    break;
                    case 2: {
                        ToastUtil.show(LoginActivity.this, "登录失败");
                        LogDebug.err("登录失败");
                    }
                    break;
                    case 3: {
                        // 取消, cancel notification
                        ToastUtil.show(LoginActivity.this, "取消授权");
                        LogDebug.err("取消授权");
                    }
                    break;
                }
            }
            break;
            case MSG_CANCEL_NOTIFY: {
                NotificationManager nm = (NotificationManager) msg.obj;
                if (nm != null) {
                    nm.cancel(msg.arg1);
                }
            }
            break;
        }
        return false;
    }
  /*  // 在状态栏提示分享操作,the notification on the status bar
    private void showNotification(long cancelTime, String tab) {
        try {
            Context app = getApplicationContext();
            NotificationManager nm = (NotificationManager) app.getSystemService(Context.NOTIFICATION_SERVICE);
            final int id = Integer.MAX_VALUE / 13 + 1;
            nm.cancel(id);
            long when = System.currentTimeMillis();
            Notification notification = new Notification(R.mipmap.ic_launcher, tab, when);
            PendingIntent pi = PendingIntent.getActivity(app, 0, new Intent(), 0);
            notification.setLatestEventInfo(app, "sharesdk test", tab, pi);
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            nm.notify(id, notification);
            if (cancelTime > 0) {
                Message msg = new Message();
                msg.what = MSG_CANCEL_NOTIFY;
                msg.obj = nm;
                msg.arg1 = id;
                UIHandler.sendMessageDelayed(msg, cancelTime, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    /**
     * -------------------------微信第三方登录----------------------
     */
    //获取微信访问getCode
    private void getCode() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        App.wxapi.sendReq(req);
        finish();
        Loger.log(TAG, "微信登录请求成功");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * -------------------------微信第三方登录结束--------------------
     */


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ShareSDK.stopSDK(this);
    }


}
