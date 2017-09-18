package bbc.com.moteduan_lib.leftmenu.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jungly.gridpasswordview.GridPasswordView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.log.LogDebug;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.tools.CountDownTimerUtils;
import bbc.com.moteduan_lib.tools.TextUtils;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;


public class AccountSecurity extends BaseActivity implements View.OnClickListener {


    private ImageButton back;
    private RelativeLayout titleLayout;
    private TextView updatePwd;
    private TextView updateWithdrawalpwd;
    private EditText security_phone;
    private EditText security_code;
    private TextView security_getcode;
    private TextView security_next;
    private AlertDialog setpwddialog;
    private GridPasswordView psw;
    private GridPasswordView psw2;
    private String phone;
    private CountDownTimerUtils mCountDownTimerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(this).inflate(R.layout.activity_account_security, null);
        setContentView(view);


        initView();
        initData();
    }

    @Override
    public void initView() {

        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(this);
        security_phone = (EditText) findViewById(R.id.security_phone);
        security_code = (EditText) findViewById(R.id.security_code);
        security_getcode = (TextView) findViewById(R.id.security_getcode);
        security_getcode.setOnClickListener(this);
        security_next = (TextView) findViewById(R.id.security_next);
        security_next.setOnClickListener(this);
        mCountDownTimerUtils = new CountDownTimerUtils(security_getcode, 60000, 1000);
    }

    @Override
    public void initData() {
        String getphone =  SpDataCache.getSelfInfo(AccountSecurity.this).getData().getM_mobile();
        if (getphone != null && !"".equals(getphone)){
            security_phone.setText(getphone);
            security_phone.setClickable(false);

        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            finish();

        } else if (i == R.id.security_getcode) {
            huquCode();
            security_next.setBackgroundResource(R.drawable.bg_solid_radius90_mainred);

        } else if (i == R.id.security_next) {
            submit();

        }
    }

    private void huquCode() {
        String phone = security_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("mobile",phone);
        map.put("purpose","changemobile");
        Xutils.post(Url.sms, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    LogDebug.err(result);
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
                        security_code.setClickable(false);
                        mCountDownTimerUtils.start();
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

    private void submit() {
        // validate
        phone = security_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        String code = security_code.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("purpose","changemobile");
        map.put("smsCode",code);
        map.put("uid", SpDataCache.getSelfInfo(AccountSecurity.this).getData().getM_id());

        Xutils.post(Url.checkSms, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    LogDebug.err(result);
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(AccountSecurity.this);
                        View setpwd = LayoutInflater.from(AccountSecurity.this).inflate(R.layout.setpassword, null);
                        psw = (GridPasswordView) setpwd.findViewById(R.id.pswView);
                        psw2 = (GridPasswordView) setpwd.findViewById(R.id.pswView2);

                        ImageView cut2 = (ImageView) setpwd.findViewById(R.id.cut);
                        cut2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setpwddialog.dismiss();
                            }
                        });
                        TextView confirmiv = (TextView) setpwd.findViewById(R.id.confirmiv);
                        builder2.setView(setpwd);
                        setpwddialog = builder2.create();
                        setpwddialog.show();
                        confirmiv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resetPwd();
                            }
                        });

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

    private void resetPwd() {
        if (psw.getPassWord().equals("")||psw2.getPassWord().equals("")) {
            Toast.makeText(AccountSecurity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (psw.getPassWord().equals(psw2.getPassWord())){
            Map<String, Object> map = new HashMap<>();
            map.put("mobile",phone);
            map.put("pwd",psw.getPassWord());

            Xutils.post(Url.find_pwd, map, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        LogDebug.err(result);
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        if ("200".equals(code)) {
                            finish();
                            toast.showText("密码重置成功");
                        } else {
                            toast.showText(jsonObject.getString("tips"));
                        }
                        setpwddialog.dismiss();
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

        } else {
            toast.showText("两次输入的密码不一致");
            return;
        }
    }
}
