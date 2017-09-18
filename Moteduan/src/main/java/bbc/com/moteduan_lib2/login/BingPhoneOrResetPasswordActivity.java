package bbc.com.moteduan_lib2.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseTranslucentActivity;
import bbc.com.moteduan_lib.bean.LoginBean;
import bbc.com.moteduan_lib.constant.Constants;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.log.LogDebug;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;
import bbc.com.moteduan_lib.tools.CountDownTimerUtils;
import bbc.com.moteduan_lib.tools.PhoneUrils;
import wei.toolkit.WebViewActivity;


public class BingPhoneOrResetPasswordActivity extends BaseTranslucentActivity implements View.OnClickListener {

    private String phoneNumString;
    private ImageView back;
    private TextView head_title;
    private RelativeLayout actionstatus;
    private EditText phoneNum;
    private TextView getcode;
    private LinearLayout phone_ll;
    private TextView phoneNum_line;
    private EditText checkCode;
    private LinearLayout checkCode_ll;
    private TextView checkCode_line;
    private EditText inputPwd;
    private ImageView biyan;
    private RelativeLayout inputPwd_ll;
    private TextView inputPwd_line;
    private TextView userAgreement;
    private LinearLayout userAgreement_ll;
    private TextView completeBing;
    private RelativeLayout activity_bingphone;
    private String account;
    private String name;
    private String photo;
    private int sex;
    private String from = "";
    private Boolean isChecked = false;
    /**
     * intcidubf value 0 第三方注册 1 第三方绑定
     */
    private int intcoding = -1;
    private CountDownTimerUtils countDownTimerUtils;
    private TextView agreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingphone);
        initView();
        initData();
    }

    public void initView() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        head_title = (TextView) findViewById(R.id.head_title);
        head_title.setOnClickListener(this);
        actionstatus = (RelativeLayout) findViewById(R.id.actionstatus);
        actionstatus.setOnClickListener(this);
        phoneNum = (EditText) findViewById(R.id.phoneNum);
        phoneNum.setOnClickListener(this);
        getcode = (TextView) findViewById(R.id.getcode);
        getcode.setOnClickListener(this);
        phone_ll = (LinearLayout) findViewById(R.id.phone_ll);
        phone_ll.setOnClickListener(this);
        phoneNum_line = (TextView) findViewById(R.id.phoneNum_line);
        phoneNum_line.setOnClickListener(this);
        checkCode = (EditText) findViewById(R.id.checkCode);
        checkCode.setOnClickListener(this);
        checkCode_ll = (LinearLayout) findViewById(R.id.checkCode_ll);
        checkCode_ll.setOnClickListener(this);
        checkCode_line = (TextView) findViewById(R.id.checkCode_line);
        checkCode_line.setOnClickListener(this);
        inputPwd = (EditText) findViewById(R.id.inputPwd);
        inputPwd.setOnClickListener(this);
        biyan = (ImageView) findViewById(R.id.biyan);
        biyan.setOnClickListener(this);
        inputPwd_ll = (RelativeLayout) findViewById(R.id.inputPwd_ll);
        inputPwd_ll.setOnClickListener(this);
        inputPwd_line = (TextView) findViewById(R.id.inputPwd_line);
        inputPwd_line.setOnClickListener(this);
        userAgreement = (TextView) findViewById(R.id.userAgreement);
        userAgreement.setOnClickListener(this);
        userAgreement_ll = (LinearLayout) findViewById(R.id.userAgreement_ll);
        userAgreement_ll.setOnClickListener(this);
        completeBing = (TextView) findViewById(R.id.completeBing);
        completeBing.setOnClickListener(this);
        activity_bingphone = (RelativeLayout) findViewById(R.id.activity_bingphone);
        activity_bingphone.setOnClickListener(this);
        agreement = (TextView) findViewById(R.id.userAgreement);
        agreement.setOnClickListener(this);
    }

    @Override
    public void initData() {
        countDownTimerUtils = new CountDownTimerUtils(getcode, 60000, 1000);
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        if (TextUtils.isEmpty(from)) from = "";
        LogDebug.log("BingPhoneOrResetPasswordActivity","from = "+from);
        if ("weixin".equals(from)) {
            intcoding = intent.getIntExtra("intcoding", 0);
            account = intent.getStringExtra("account");
            name = intent.getStringExtra("m.m_nick_name");
            photo = intent.getStringExtra("m.m_head_photo");
            sex = intent.getIntExtra("m.m_sex", 2);
        } else if ("weibo".equals(from)) {
            intcoding = intent.getIntExtra("intcoding", 0);
            account = intent.getStringExtra("account");
            name = intent.getStringExtra("m.m_nick_name");
            photo = intent.getStringExtra("m.m_head_photo");
            sex = intent.getIntExtra("m.m_sex", 2);
        } else if ("qq".equals(from)) {
            intcoding = intent.getIntExtra("intcoding", 0);
            account = intent.getStringExtra("account");
            name = intent.getStringExtra("m.m_nick_name");
            photo = intent.getStringExtra("m.m_head_photo");
            sex = intent.getIntExtra("m.m_sex", 2);
        } else {
            head_title.setText("重设密码");
            inputPwd_ll.setVisibility(View.VISIBLE);
            inputPwd_line.setVisibility(View.VISIBLE);
            userAgreement_ll.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            finish();

        } else if (i == R.id.getcode) {
            // from为空=重置密码否则检查手机号是否被绑定过
            if (TextUtils.isEmpty(from)) {
                requestPhoneCode(Constants.resetpwd);
            } else {
                checkPhone();
            }
        } else if (i == R.id.completeBing) {
            phoneNumString = phoneNum.getText().toString().trim();
            if (TextUtils.isEmpty(phoneNumString)) {
                Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                return;
            } else if (!PhoneUrils.isPhone(phoneNumString)) {
                Toast.makeText(this, "请确认手机号码是否正确", Toast.LENGTH_SHORT).show();
                return;
            }
            if ("weixin".equals(from) || "weibo".equals(from) || "qq".equals(from)) {
                if (intcoding == 0) {
                    // 注册
                    thirdRegisterLogin();
                } else if (intcoding == 1) {
                    // 绑定手机号
                    bindToPhone();
                }
            } else {
                final String checkCodeString = checkCode.getText().toString().trim();
                if (TextUtils.isEmpty(checkCodeString)) {
                    Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String inputPwdString = inputPwd.getText().toString().trim();
                if (TextUtils.isEmpty(inputPwdString)) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //忘记密码 重设密码接口
                HashMap<String, Object> forgetpwdHashMap = new HashMap<>();
                forgetpwdHashMap.put("mobile", phoneNumString);
                forgetpwdHashMap.put("code", checkCodeString);
                forgetpwdHashMap.put("purpose", Constants.resetpwd);
                forgetpwdHashMap.put("pwd", inputPwdString);
                Xutils.post(Url.uforgetPwd, forgetpwdHashMap, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        LogDebug.err("uforgetPwd = result" + result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String code = jsonObject.getString("code");
                            String tips = jsonObject.getString("tips");
                            if ("200".equals(code)) {
                                toast.showText(tips);
                                finish();
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

        } else if (i == R.id.biyan) {
            if (!isChecked) {
                // 显示密码
                isChecked = true;
                biyan.setImageResource(R.drawable.icon_yanjing);
                inputPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                // 隐藏密码
                isChecked = false;
                biyan.setImageResource(R.drawable.icon_biyan);
                inputPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            //设置光标位置
            inputPwd.setSelection(checkCode.getText().length());
        } else if (i == R.id.userAgreement) {
            startActivity(new Intent(BingPhoneOrResetPasswordActivity.this, WebViewActivity.class)
                    .putExtra("url", Url.agreement)
                    .putExtra("title", "用户协议"));
        }
    }


    /**
     * 检查手机号是否绑被绑定过
     */
    private void checkPhone() {
        phoneNumString = phoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumString)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        } else if (!PhoneUrils.isPhone(phoneNumString)) {
            Toast.makeText(this, "请确认手机号码是否正确", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, Object> checkPhoneHashMap = new HashMap<>();
        checkPhoneHashMap.put("mobile", phoneNumString);
        Xutils.post(Url.exists_mobile, checkPhoneHashMap, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.err("exists_mobile result = " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String tips = jsonObject.getString("tips");
                    if ("200".equals(code)) {
                        // 手机号可用
                        requestPhoneCode("bangding");
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


    private void choseThreeOrPhone() {
        final View choseThreeOrPhoneView = View.inflate(BingPhoneOrResetPasswordActivity.this, R.layout.custom_dilog_layout, null);
        TextView dialogType = (TextView) choseThreeOrPhoneView.findViewById(R.id.dilog_type);
        TextView dilog_content = (TextView) choseThreeOrPhoneView.findViewById(R.id.dilog_content);
        TextView confirm = (TextView) choseThreeOrPhoneView.findViewById(R.id.confirm);
        confirm.setText("好的");
        TextView cancel = (TextView) choseThreeOrPhoneView.findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        cancel.setText("使用手机号登录");
        dilog_content.setText("该手机号已被绑定，换个手机号试试吧~");
        final Dialog removeModelBingdialog = new Dialog(this, R.style.MyDialogStyle);
        removeModelBingdialog.setContentView(choseThreeOrPhoneView);
        removeModelBingdialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeModelBingdialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeModelBingdialog.dismiss();
            }
        });
    }


    private void showRemoveBingDialog(String msg) {
        final View removeModelBingView = View.inflate(BingPhoneOrResetPasswordActivity.this, R.layout.custom_dilog_layout, null);
        TextView dialogType = (TextView) removeModelBingView.findViewById(R.id.dilog_type);
        TextView dilog_content = (TextView) removeModelBingView.findViewById(R.id.dilog_content);
        TextView confirm = (TextView) removeModelBingView.findViewById(R.id.confirm);
        TextView cancel = (TextView) removeModelBingView.findViewById(R.id.cancel);
        dilog_content.setText(msg);
        final Dialog removeModelBingdialog = new Dialog(this, R.style.MyDialogStyle);
        removeModelBingdialog.setContentView(removeModelBingView);
        removeModelBingdialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeModelBingdialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeModelBingdialog.dismiss();
                Intent intent = new Intent(BingPhoneOrResetPasswordActivity.this, RemoveModelAccountActivity.class);
                intent.putExtra("phone", phoneNumString);
                startActivity(intent);
            }
        });
    }


    /**
     * 第三方注册
     */
    private void thirdRegisterLogin() {
        String checkCodeString = checkCode.getText().toString().trim();
        if (TextUtils.isEmpty(checkCodeString)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, Object> registerLoginHashMap = new HashMap<>();
        registerLoginHashMap.put("mobile", phoneNumString);
        registerLoginHashMap.put("code", checkCodeString);
        if ("weixin".equals(from)) {
            registerLoginHashMap.put("m_wx_account", account);
        } else if ("weibo".equals(from)) {
            registerLoginHashMap.put("m_wb_account", account);
        } else if ("qq".equals(from)) {
            registerLoginHashMap.put("m_qq_account", account);
        }
        if (sex == 0) {
            sex = 2; //男
        } else if (sex == 1) {
            sex = 1; //女
        }

        LogDebug.err("account = " + account + "sex = " + sex);
        registerLoginHashMap.put("m.m_sex", sex);
        registerLoginHashMap.put("m.m_head_photo", photo);
        registerLoginHashMap.put("m.m_nick_name", name);
        registerLoginHashMap.put("purpose", "bangding");

        Xutils.post(Url.registerLogin, registerLoginHashMap, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.err("registerLogin result=" + result);
                Gson gson = new Gson();
                LoginBean loginBean = gson.fromJson(result, LoginBean.class);
                LoginBean.DataBean data = loginBean.getData();
                try {
                    String code = loginBean.getCode();
                    String tips = loginBean.getTips();
                    if ("200".equals(code)) {
                        String m_tag = data.getM_tag();
                        if (TextUtils.isEmpty(m_tag)) {
                            Intent intent = new Intent(BingPhoneOrResetPasswordActivity.this, CompleteSelfInfoActivity.class);
                            intent.putExtra("data", loginBean);
                            startActivity(intent);
                            finish();
                        } else {
                            //保存数据
                            SpDataCache.saveSelfInfo(BingPhoneOrResetPasswordActivity.this, result);
                            Intent intent = new Intent();
                            intent.setAction("com.lm.loginedNotificationReceiver");
                            sendBroadcast(intent);
                            finish();
                        }

                    } else {
                        toast.showText(tips);
                    }
                } catch (Exception e) {
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
     * 手机号和第三方绑定
     */
    private void bindToPhone() {
        String checkCodeString = checkCode.getText().toString().trim();
        if (TextUtils.isEmpty(checkCodeString)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, Object> bindToPhoneHashMap = new HashMap<>();
        bindToPhoneHashMap.put("mobile", phoneNumString);
        bindToPhoneHashMap.put("code", checkCodeString);
        if ("weixin".equals(from)) {
            bindToPhoneHashMap.put("m_wx_account", account);
        } else if ("weibo".equals(from)) {
            bindToPhoneHashMap.put("m_wb_account", account);
        } else if ("qq".equals(from)) {
            bindToPhoneHashMap.put("m_qq_account", account);
        }
        bindToPhoneHashMap.put("purpose", "bangding");
        Xutils.post(Url.bindToPhone, bindToPhoneHashMap, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.err("bindToThreeNumber result=" + result);
                Gson gson = new Gson();
                LoginBean loginBean = gson.fromJson(result, LoginBean.class);
                LoginBean.DataBean data = loginBean.getData();
                try {
                    String code = loginBean.getCode();
                    String tips = loginBean.getTips();
                    if ("200".equals(code)) {
                        String m_tag = data.getM_tag();
                        if (TextUtils.isEmpty(m_tag)) {
                            Intent intent = new Intent(BingPhoneOrResetPasswordActivity.this, CompleteSelfInfoActivity.class);
                            intent.putExtra("data", loginBean);
                            startActivity(intent);
                            finish();
                        } else {
                            //保存数据
                            SpDataCache.saveSelfInfo(BingPhoneOrResetPasswordActivity.this, result);
                            Intent intent = new Intent();
                            intent.setAction("com.lm.loginedNotificationReceiver");
                            sendBroadcast(intent);
                            finish();
                        }

                    } else {
                        toast.showText(tips);
                    }
                } catch (Exception e) {
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
     * 获取验证码
     */
    private void requestPhoneCode(String purpose) {
        phoneNumString = phoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumString)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        } else if (!PhoneUrils.isPhone(phoneNumString)) {
            Toast.makeText(this, "请确认手机号码是否正确", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, Object> getCodeHashMap = new HashMap<>();
        getCodeHashMap.put("mobile", phoneNumString);
        getCodeHashMap.put("purpose", purpose);
        Xutils.post(Url.ugetCode, getCodeHashMap, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.err("requestPhoneCode result=" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String tips = jsonObject.getString("tips");
                    if ("200".equals(code)) {
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

}
