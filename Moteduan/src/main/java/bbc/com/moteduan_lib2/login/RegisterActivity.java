package bbc.com.moteduan_lib2.login;

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
import bbc.com.moteduan_lib.log.LogDebug;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.tools.CountDownTimerUtils;
import bbc.com.moteduan_lib.tools.PhoneUrils;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;
import wei.toolkit.WebViewActivity;


public class RegisterActivity extends BaseTranslucentActivity implements View.OnClickListener {

    private ImageView huya_logo;
    private EditText phoneNum;
    private LinearLayout phone_ll;
    private TextView phoneNum_line;
    private EditText checkCode;
    private LinearLayout checkCode_ll;
    private TextView checkCode_line;
    private TextView userAgreement;
    private LinearLayout userAgreement_ll;
    private TextView turn_to_login;
    private RelativeLayout activity_register;
    private TextView toRegister;
    private ImageView back;
    private TextView getCheckCode;
    private CountDownTimerUtils countDownTimerUtils;
    private String phoneNumString;
    private EditText inputPwd;
    private ImageView seeOrNO;
    private Boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initData();
    }

    @Override
    public void initView() {
        back = (ImageView) findViewById(R.id.back);
        huya_logo = (ImageView) findViewById(R.id.huya_logo);
        phoneNum = (EditText) findViewById(R.id.phoneNum);
        phone_ll = (LinearLayout) findViewById(R.id.phone_ll);
        phoneNum_line = (TextView) findViewById(R.id.phoneNum_line);
        checkCode = (EditText) findViewById(R.id.checkCode);
        checkCode_ll = (LinearLayout) findViewById(R.id.checkCode_ll);
        checkCode_line = (TextView) findViewById(R.id.checkCode_line);
        userAgreement = (TextView) findViewById(R.id.userAgreement);
        userAgreement_ll = (LinearLayout) findViewById(R.id.userAgreement_ll);
        turn_to_login = (TextView) findViewById(R.id.turn_to_login);
        activity_register = (RelativeLayout) findViewById(R.id.activity_register);
        toRegister = (TextView) findViewById(R.id.to_register);
        getCheckCode = (TextView) findViewById(R.id.getCheckCode);
        inputPwd = (EditText) findViewById(R.id.inputPwd);
        toRegister.setOnClickListener(this);
        turn_to_login.setOnClickListener(this);
        back.setOnClickListener(this);
        getCheckCode.setOnClickListener(this);
        userAgreement.setOnClickListener(this);
        seeOrNO = (ImageView) findViewById(R.id.seeOrNo_register);
        seeOrNO.setOnClickListener(this);
    }

    @Override
    public void initData() {
        countDownTimerUtils = new CountDownTimerUtils(getCheckCode, 60000, 1000);
    }

    private void submit() {


        String checkCodeString = checkCode.getText().toString().trim();
        if (TextUtils.isEmpty(checkCodeString)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }

    @Override
    public void onClick(View v) {


        int i = v.getId();
        if (i == R.id.turn_to_login) {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        } else if (i == R.id.to_register) {
            registerAcount();

        } else if (i == R.id.back) {
            finish();

        } else if (i == R.id.getCheckCode) {
            requestPhoneCode(Constants.REGISTER);

        } else if (i == R.id.seeOrNo_register) {
            if (!isChecked) {
                // 显示密码
                isChecked = true;
                seeOrNO.setImageResource(R.drawable.icon_yanjing);
                inputPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                // 隐藏密码
                isChecked = false;
                seeOrNO.setImageResource(R.drawable.icon_biyan);
                inputPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            //设置光标位置
            inputPwd.setSelection(inputPwd.getText().length());
        } else if (i == R.id.userAgreement) {
            startActivity(new Intent(RegisterActivity.this, WebViewActivity.class)
                    .putExtra("url", Url.agreement)
                    .putExtra("title", "用户协议"));
        }
    }

    /**
     * 注册账号
     */
    private void registerAcount() {
        phoneNumString = phoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumString)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!PhoneUrils.isPhone(phoneNumString)) {
            Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        String codeString = checkCode.getText().toString().trim();
        if (TextUtils.isEmpty(codeString)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String pwdString = inputPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwdString)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwdString.length() < 6) {
            Toast.makeText(this, "密码长度要大于6个字符", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> registerHashMap = new HashMap<>();
        registerHashMap.put("mobile", phoneNumString);
        registerHashMap.put("code", codeString);
        registerHashMap.put("purpose", Constants.REGISTER);
        registerHashMap.put("pwd", pwdString);
        Xutils.post(Url.mregister, registerHashMap, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.log(RegisterActivity.class.getSimpleName(), "Register result=" + result);
                Gson gson = new Gson();
                LoginBean loginBean = gson.fromJson(result, LoginBean.class);
                String code = loginBean.getCode();
                String tips = loginBean.getTips();
                if ("200".equals(code)) {
                    startActivity(new Intent(RegisterActivity.this, CompleteSelfInfoActivity.class)
                            .putExtra("data", loginBean));
                    finish();
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
    private void requestPhoneCode(String purpose) {
        // validate
        phoneNumString = phoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumString)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!PhoneUrils.isPhone(phoneNumString)) {
            Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
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
