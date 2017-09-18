package bbc.com.moteduan_lib.home;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.log.LogDebug;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.tools.CountDownTimerUtils;
import bbc.com.moteduan_lib.tools.ToastUtils;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;

public class PhoneBingActivity extends BaseActivity {

    private ImageView back;
    private TextView name;
    private RelativeLayout topbanner;
    private EditText phonenumber;
    private RelativeLayout relativeLayout24;
    private TextView securitycode_tv;
    private EditText securitycode_et;
    private RelativeLayout relativeLayout26;
    private TextView binding_iv;
    private String purpose;
    private String pNum;
    private String smsCode;
    private CountDownTimerUtils mCountDownTimerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_bing);
        initView();
        initData();
    }

    public void initView() {
        back = (ImageView) findViewById(R.id.back);
        name = (TextView) findViewById(R.id.name);
        topbanner = (RelativeLayout) findViewById(R.id.topbanner);
        phonenumber = (EditText) findViewById(R.id.phonenumber);
        relativeLayout24 = (RelativeLayout) findViewById(R.id.relativeLayout24);
        securitycode_tv = (TextView) findViewById(R.id.securitycode_tv);
        securitycode_et = (EditText) findViewById(R.id.securitycode_et);
        relativeLayout26 = (RelativeLayout) findViewById(R.id.relativeLayout26);
        binding_iv = (TextView) findViewById(R.id.binding_iv);

        mCountDownTimerUtils = new CountDownTimerUtils(securitycode_tv, 60000, 1000);
    }

    @Override
    public void initData() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        securitycode_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pNum = phonenumber.getText().toString().trim();
                if (pNum == null || "".equals(pNum)) {
                    ToastUtils.getInstance(PhoneBingActivity.this).showText("手机号不能为空");
                    return;
                } else {
                    if (isPhone(pNum)) {
                        requestCheckCode();
                    } else {
                        Toast.makeText(PhoneBingActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            }
        });

        binding_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pNum = phonenumber.getText().toString().trim();
                if (pNum == null || "".equals(pNum)) {
                    ToastUtils.getInstance(PhoneBingActivity.this).showText("手机号不能为空");
                    return;
                } else {
                    if (isPhone(pNum)) {
                        smsCode = securitycode_et.getText().toString().trim();
                        if (smsCode == null || "".equals(smsCode)) {
                            Toast.makeText(PhoneBingActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            checkSms();

                        }
                    } else {
                        Toast.makeText(PhoneBingActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
    }


    private void checkSms() {
        String getphone =  SpDataCache.getSelfInfo(PhoneBingActivity.this).getData().getM_mobile();
        if (getphone == null || "".equals(getphone)) {
            purpose = "register";
        } else {
            purpose = "resetpwd";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", pNum);
        map.put("purpose", purpose);
        map.put("uid", SpDataCache.getSelfInfo(PhoneBingActivity.this).getData().getM_head_photo());
        map.put("smsCode", smsCode);
            Xutils.post(Url.checkSMS, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.err("checkSMS:" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
                        SpDataCache.saveBindingPhone(PhoneBingActivity.this, pNum);
                        toast.showText("手机绑定成功");
                        finish();
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
     * 获取验证码
     */
    private void requestCheckCode() {
        String getphone =  SpDataCache.getSelfInfo(PhoneBingActivity.this).getData().getM_mobile();
        if (getphone == null || "".equals(getphone)) {
            purpose = "register";
        } else {
            purpose = "resetpwd";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", pNum);
        map.put("purpose", purpose);
        map.put("uid", SpDataCache.getSelfInfo(PhoneBingActivity.this).getData().getM_head_photo());
        Xutils.post(Url.sendSMS, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.err("result" + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String tips = jsonObject.getString("tips");
                    if ("200".equals(code)) {
//                        securitycode_tv.setClickable(false);
                        mCountDownTimerUtils.start();
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


    public static boolean isPhone(String inputText) {
        Pattern p = Pattern.compile("^((14[0-9])|(13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }
}
