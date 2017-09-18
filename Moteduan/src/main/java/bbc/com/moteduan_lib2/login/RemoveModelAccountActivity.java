package bbc.com.moteduan_lib2.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseTranslucentActivity;
import bbc.com.moteduan_lib.constant.Constants;
import bbc.com.moteduan_lib.log.LogDebug;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.tools.CountDownTimerUtils;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;


public class RemoveModelAccountActivity extends BaseTranslucentActivity {

    private ImageView back;
    private TextView head_title;
    private RelativeLayout actionstatus;
    private TextView phoneNum;
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
    private String phoneNumString;
    private CountDownTimerUtils countDownTimerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_model_account);
        initView();
        initData();
    }

    public void initView() {
        back = (ImageView) findViewById(R.id.back);
        head_title = (TextView) findViewById(R.id.head_title);
        actionstatus = (RelativeLayout) findViewById(R.id.actionstatus);
        phoneNum = (TextView) findViewById(R.id.phoneNum);
        getcode = (TextView) findViewById(R.id.getcode);
        phone_ll = (LinearLayout) findViewById(R.id.phone_ll);
        phoneNum_line = (TextView) findViewById(R.id.phoneNum_line);
        checkCode = (EditText) findViewById(R.id.checkCode);
        checkCode_ll = (LinearLayout) findViewById(R.id.checkCode_ll);
        checkCode_line = (TextView) findViewById(R.id.checkCode_line);
        inputPwd = (EditText) findViewById(R.id.inputPwd);
        biyan = (ImageView) findViewById(R.id.biyan);
        inputPwd_ll = (RelativeLayout) findViewById(R.id.inputPwd_ll);
        inputPwd_line = (TextView) findViewById(R.id.inputPwd_line);
        userAgreement = (TextView) findViewById(R.id.userAgreement);
        userAgreement_ll = (LinearLayout) findViewById(R.id.userAgreement_ll);
        completeBing = (TextView) findViewById(R.id.completeBing);
        activity_bingphone = (RelativeLayout) findViewById(R.id.activity_bingphone);
        getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPhoneCode(Constants.BANG_DING);
            }
        });

        completeBing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeModelBing();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        phoneNumString = intent.getStringExtra("phone");
        phoneNum.setText(phoneNumString);
        countDownTimerUtils = new CountDownTimerUtils(getcode, 60000, 1000);
    }

    /**
     * 获取验证码
     */
    private void requestPhoneCode(String purpose) {
        HashMap<String, Object> getCodeHashMap = new HashMap<>();
        getCodeHashMap.put("mobile", phoneNumString);
        getCodeHashMap.put("purpose", purpose);
        countDownTimerUtils.start();
        Xutils.post(Url.ugetCode, getCodeHashMap, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.err("requestPhoneCode result=" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String tips = jsonObject.getString("tips");
                    if ("200".equals(code)) {

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
     * 移除模特端绑定
     */
    private void removeModelBing() {
        String checkCodeString = checkCode.getText().toString().trim();
        if (TextUtils.isEmpty(checkCodeString)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, Object> removeModelBingHashMap = new HashMap<>();
        removeModelBingHashMap.put("mobile",phoneNumString);
        removeModelBingHashMap.put("code",checkCodeString);
        removeModelBingHashMap.put("purpose", Constants.BANG_DING);
        Xutils.post(Url.removeMobileOfUser, removeModelBingHashMap, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.err("removeMobileOfMember result= "+result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String tips = jsonObject.getString("tips");
                    if ("200".equals(code)){
                        toast.showText("解绑成功~");
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
}
