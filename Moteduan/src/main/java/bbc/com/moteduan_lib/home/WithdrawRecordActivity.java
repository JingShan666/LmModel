package bbc.com.moteduan_lib.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.adapter.RechargeRecordAdapter;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.log.LogDebug;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;

public class WithdrawRecordActivity extends BaseActivity {

    private ImageView back;
    private TextView name;
    private RelativeLayout topbanner;
    private ListView listView;
    private ImageView imageView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_record);
        initView();
        initData();
    }

    public void initView() {
        back = (ImageView) findViewById(R.id.back);
        name = (TextView) findViewById(R.id.name);
        topbanner = (RelativeLayout) findViewById(R.id.topbanner);
        listView = (ListView) findViewById(R.id.listView);
        imageView6 = (ImageView) findViewById(R.id.imageView6);
    }

    @Override
    public void initData() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Map<String ,Object>map =new HashMap<>();
        map.put("mid", SpDataCache.getSelfInfo(WithdrawRecordActivity.this).getData().getM_head_photo());
        Xutils.post(Url.withdrawrecorde, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {

                    LogDebug.err("result"+result);
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)){
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data.length()>0){
                            imageView6.setVisibility(View.GONE);
                            RechargeRecordAdapter rechargeRecordAdapter = new RechargeRecordAdapter(data);
                            listView.setAdapter(rechargeRecordAdapter);
                        }
                    }






                    /*Gson gson = new Gson();
                    RecordBean recordBean = gson.fromJson(result, RecordBean.class);


                    String code = recordBean.getCode();
                    LogDebug.err(code+"code");
                    LogDebug.err("333333333333");
                    if ("200".equals(code)){
                        List<RecordBean.DataBean> data = recordBean.getData();
                        LogDebug.err("11111111111111111111111");
                        if (data.size()>0){
                            LogDebug.err("222232---------------");
                            imageView6.setVisibility(View.GONE);
                            RechargeRecordAdapter rechargeRecordAdapter = new RechargeRecordAdapter(data);
                            listView.setAdapter(rechargeRecordAdapter);
                        }
                    }*/
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
}
