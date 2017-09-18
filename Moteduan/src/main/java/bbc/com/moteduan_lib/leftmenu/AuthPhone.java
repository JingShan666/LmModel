package bbc.com.moteduan_lib.leftmenu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;

import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.leftmenu.activity.AccountSecurity;
import bbc.com.moteduan_lib.log.LogDebug;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.tools.TextUtils;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;
/**
        修改密码页面
 */


public class AuthPhone extends BaseActivity implements View.OnClickListener {

    private String purpose;////register：注册  resetpwd：修改密码  changemobile：更换手机号
    private Object code;
    private ViewHolder viewHolder;
    private ImageButton back;
    private RelativeLayout titleLayout;
    private EditText yuanpwd;
    private EditText newPwd;
    private EditText newPwd2;
    private Button next;

   Handler handler = new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
       }
   };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_phone);

        View inflate = View.inflate(getApplicationContext(), R.layout.activity_auth_phone, null);
        viewHolder = new ViewHolder(inflate);
        initView();
        initData();
    }


    @Override
    public void initView() {

        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(this);
        titleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
        titleLayout.setOnClickListener(this);
        yuanpwd = (EditText) findViewById(R.id.yuanpwd);
        yuanpwd.setOnClickListener(this);
        newPwd = (EditText) findViewById(R.id.newPwd);
        newPwd.setOnClickListener(this);
        newPwd2 = (EditText) findViewById(R.id.newPwd2);
        newPwd2.setOnClickListener(this);
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(this);
    }

    @Override
    public void initData() {
        purpose = getIntent().getStringExtra("purpose");

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            finish();

        } else if (i == R.id.next) {
            submit();

        }
    }

    private void submit() {

        String yuanpwdString = yuanpwd.getText().toString().trim();
        if (TextUtils.isEmpty(yuanpwdString)) {
            Toast.makeText(this, "请输入原密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String newPwdString = newPwd.getText().toString().trim();
        if (TextUtils.isEmpty(newPwdString)) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String newPwd2String = newPwd2.getText().toString().trim();
        if (TextUtils.isEmpty(newPwd2String)) {
            Toast.makeText(this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPwdString.equals(newPwd2String)){
            Toast.makeText(this, "两次输入的新密码不一样", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something

        Map<String, Object> map = new HashMap<>();
        map.put("mobile", SpDataCache.getSelfInfo(AuthPhone.this).getData().getM_mobile());
        map.put("oldpwd",yuanpwdString);
        map.put("newpwd",newPwd2String);
        map.put("purpose",purpose);
        Xutils.post(Url.changePwd, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String tips = jsonObject.getString("tips");
                    String code = jsonObject.getString("code");
                    toast.showText(tips);
                    if ("200".equals(code)){
                        View view = LayoutInflater.from(AuthPhone.this).inflate(R.layout.activity_change_pwd_dialog, null);
                        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(AuthPhone.this, AccountSecurity.class);
                                startActivity(intent);
                                popupWindow.dismiss();
                                finish();
                            }
                        },3000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                LogDebug.err("修改密码："+result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogDebug.err(ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }


    public static class ViewHolder {
        public View rootView;
        public ImageButton back;
        public RelativeLayout titleLayout;
        public EditText yuanpwd;
        public EditText newPwd;
        public EditText newPwd2;
        public Button next;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.back = (ImageButton) rootView.findViewById(R.id.back);
            this.titleLayout = (RelativeLayout) rootView.findViewById(R.id.titleLayout);
            this.yuanpwd = (EditText) rootView.findViewById(R.id.yuanpwd);
            this.newPwd = (EditText) rootView.findViewById(R.id.newPwd);
            this.newPwd2 = (EditText) rootView.findViewById(R.id.newPwd2);
            this.next = (Button) rootView.findViewById(R.id.next);
        }

    }
}

