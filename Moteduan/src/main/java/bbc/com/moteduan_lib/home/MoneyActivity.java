package bbc.com.moteduan_lib.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.log.LogDebug;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;


public class MoneyActivity extends BaseActivity {


    private ImageView back;
    private TextView name;
    private RelativeLayout topbanner;
    private ImageView money_icon_iv;
    private TextView nickname;
    private TextView withdraw;
    private TextView mobi;
    private RelativeLayout relativeLayout2;
    private ImageView imageView7;
    private TextView textView3;
    private TextView total;
    private TextView textView5;
    private RelativeLayout withdraw_sub_layout;
    private RelativeLayout to_withdraw_record_layout;
    private Boolean isCircluar = true;
    private TextView mobi_tv;
    private ImageView back_money;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        initView();
        initData();
    }

    @Override
    public void initView() {
        back_money = (ImageView) findViewById(R.id.back_money);
        mobi_tv = (TextView) findViewById(R.id.mobi_tv);
        to_withdraw_record_layout = (RelativeLayout) findViewById(R.id.to_withdraw_record_layout);
        back = (ImageView) findViewById(R.id.back);
        name = (TextView) findViewById(R.id.name);
        topbanner = (RelativeLayout) findViewById(R.id.topbanner);
        money_icon_iv = (ImageView) findViewById(R.id.money_icon_iv);
        nickname = (TextView) findViewById(R.id.nickname);
        withdraw = (TextView) findViewById(R.id.withdraw);
        mobi = (TextView) findViewById(R.id.mobi);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
        imageView7 = (ImageView) findViewById(R.id.imageView7);
        textView3 = (TextView) findViewById(R.id.textView3);
        total = (TextView) findViewById(R.id.total);
        textView5 = (TextView) findViewById(R.id.textView5);
        withdraw_sub_layout = (RelativeLayout) findViewById(R.id.withdraw_sub_layout);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getBalance();
    }

    private void getBalance() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", SpDataCache.getSelfInfo(MoneyActivity.this).getData().getM_head_photo());
        Xutils.post(Url.getBlance, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.err(result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String blance = jsonObject.getString("blance");
                    total.setText(blance + "");
                    mobi_tv.setText(blance + "");
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

    @Override
    public void initData() {
        x.image().bind(back_money, SpDataCache.getSelfInfo(MoneyActivity.this).getData().getM_head_photo());
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setCircular(isCircluar)
                .setCrop(true)
                .build();
        x.image().bind(money_icon_iv, SpDataCache.getSelfInfo(MoneyActivity.this).getData().getM_head_photo(), imageOptions);

        nickname.setText(SpDataCache.getSelfInfo(MoneyActivity.this).getData().getM_nick_name());

        to_withdraw_record_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MoneyActivity.this, WithdrawRecordActivity.class));
            }
        });
        withdraw_sub_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoneyActivity.this, WithDrawActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Deprecated
    private void LoginAgain() {
        SharedPreferences sharedPreferences = getSharedPreferences(SpDataCache.ACCOUNT, Context.MODE_PRIVATE);
        String phtotourl = sharedPreferences.getString("phtotourl", "");
        String name = sharedPreferences.getString("name", "");
        String account = sharedPreferences.getString("account", "");
        String sex1 = sharedPreferences.getString("sex", "");
        int se = Integer.parseInt(sex1);
        Map<String, Object> map = new HashMap<>();
        map.put("m.m_head_photo", phtotourl);
        map.put("m.m_nick_name", name);
        map.put("m.m_wx_account", account);
        map.put("m.m_sex", se);

        Xutils.post(Url.wxLogin, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.err("result==" + result);
                SharedPreferences sharedPreferences = getSharedPreferences(SpDataCache.SELF_INFO, MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.clear();
                edit.commit();
                org.json.JSONObject json = null;
                try {
                    json = new org.json.JSONObject(result);
                    String code = json.getString("code");
                    String msg = json.getString("tips");
                    if ("200".equals(code)) {
                        SpDataCache.saveSelfInfo(MoneyActivity.this, result);

                    }
                } catch (org.json.JSONException e) {
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

}
