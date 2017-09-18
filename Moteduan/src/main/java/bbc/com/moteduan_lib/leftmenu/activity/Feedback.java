package bbc.com.moteduan_lib.leftmenu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import bbc.com.moteduan_lib.network.Xutils3.Xutils;


public class Feedback extends BaseActivity implements View.OnClickListener {

    private ImageButton back;
    private RelativeLayout titleLayout;
    private TextView tishi;
    private MultiAutoCompleteTextView multi_text;
    private TextView submit;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
    }
    @Override
    public void initView() {
        back = (ImageButton) findViewById(R.id.back);
        titleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
        tishi = (TextView) findViewById(R.id.tishi);
        multi_text = (MultiAutoCompleteTextView) findViewById(R.id.multi_text);
        back.setOnClickListener(this);
        submit = (TextView) findViewById(R.id.feedback_submit);
        submit.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            finish();

        } else if (i == R.id.feedback_submit) {
            request();

        }
    }

    private void request() {
        String content = multi_text.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            toast.showText("反馈内容不能为空");
            return;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("feedback",content);
        map.put("user_id", SpDataCache.getSelfInfo(Feedback.this).getData().getM_id());
        map.put("type",2);
        Xutils.post(Url.feedback, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String tips = jsonObject.getString("tips");
                    if ("200".equals(code)){
                        View view = LayoutInflater.from(Feedback.this).inflate(R.layout.activity_change_pwd_dialog, null);
                        TextView text = (TextView) view.findViewById(R.id.show);
                        text.setText("问题反馈成功！");
                        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                popupWindow.dismiss();
                                finish();
                            }
                        },1000);
                    }else{
                        toast.showText(tips);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogDebug.err("feedback"+ex.toString());
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
