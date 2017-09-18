package bbc.com.moteduan_lib.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.common.Callback;

import java.util.HashMap;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.log.LogDebug;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.tools.TextUtils;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;

public class SubmitCodeActivity extends AppCompatActivity {

    private TextView mSubmitCodeText;
    private EditText mCode;
    private TextView mCodeConfirm;
    public static final String InviteCode = "invitecode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_code);
        initView();
    }

    private void initView() {
        mSubmitCodeText = (TextView) findViewById(R.id.submit_code_text);
        mCode = (EditText) findViewById(R.id.code);
        mCodeConfirm = (TextView) findViewById(R.id.code_confirm);
        TextView skip = (TextView) findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubmitCodeActivity.this,HomeActivity.class));
                SharedPreferences sharedPreferences = getSharedPreferences(InviteCode, MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("code", "200");
                edit.commit();
                finish();
            }
        });
        mCodeConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        // validate
        String codeString = mCode.getText().toString().trim();
        if (TextUtils.isEmpty(codeString)) {
            Toast.makeText(this, "邀请码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        HashMap<String, Object> map = new HashMap<>();
        map.put("mid", SpDataCache.getSelfInfo(SubmitCodeActivity.this).getData().getM_head_photo());
        map.put("invite_code",codeString);
        Xutils.post(Url.getInviteCode, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.err("getInviteCode:"+result);
                startActivity(new Intent(SubmitCodeActivity.this,HomeActivity.class));
                SharedPreferences sharedPreferences = getSharedPreferences(InviteCode, MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("code", "200");
                edit.commit();
                finish();
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
