package bbc.com.moteduan_lib2.wallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.constant.Constants;
import bbc.com.moteduan_lib.dialog.DialogUtils;
import bbc.com.moteduan_lib.network.Req;
import com.liemo.shareresource.Url;

import wei.toolkit.utils.Loger;
import wei.toolkit.widget.CircleImageBorderView;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class MoneyOperationDetailsActivity extends BaseActivity {
    private static final String TAG ="WithdrawInfoDetailsActivity";
    private ImageView back;
    private TextView title;
    private TextView moneyTv;
    private TextView tipsTv;
    private TextView originTv;
    private TextView originTips;
    private TextView timeTv;
    private TextView addressTv;
    private TextView stateTv;
    private String detailsId = "";
    private int dataType = -1;
    private CircleImageBorderView circleImageBorderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_operation_details);
        detailsId = getIntent().getStringExtra("detailsId");
        dataType = getIntent().getIntExtra("dataType",-1);
        if (TextUtils.isEmpty(detailsId)) {
            toast.showText("未找到该消费记录");
            finish();
        }
        initView();
        initData();
        initEvents();
    }

    @Override
    public void initView() {
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        moneyTv = (TextView) findViewById(R.id.activity_money_operation_detailas_money);
        tipsTv = (TextView) findViewById(R.id.activity_money_operation_detailas_tips);
        originTv = (TextView) findViewById(R.id.activity_money_operation_detailas_origin);
        originTips = (TextView) findViewById(R.id.activity_money_operation_detailas_origin_tips);
        timeTv = (TextView) findViewById(R.id.activity_money_operation_detailas_time);
        addressTv = (TextView) findViewById(R.id.activity_money_operation_detailas_address);
        stateTv = (TextView) findViewById(R.id.activity_money_operation_detailas_state);
        circleImageBorderView = (CircleImageBorderView) findViewById(R.id.activity_money_operation_detailas_portrait);

    }

    @Override
    public void initData() {
        title.setText("明细");

        Map<String, Object> map = new HashMap<>();
        map.put("order_no", detailsId);
        String url = "";
        if(dataType == 5 ){
            url = Url.Wallet.walletConsumeDetails;
        }else if(dataType == 1){
            url = Url.Wallet.consumeDetails;
        }
        Req.post(url, map, new Req.ReqCallback() {
            @Override
            public void success(String result) {
                Loger.log(TAG,result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String tips = jsonObject.getString("tips");
                    if (!"200".equals(code)) {
                        toast.showText(tips);
                        return;
                    }
                    if (!jsonObject.has("traderorder")) {
                        return;
                    }
                    JSONObject trader = jsonObject.getJSONObject("traderorder");
                    String type = trader.getString("order_type");
                    String money = trader.getString("order_money");
                    String trader_state = trader.getString("trader_state");
                    String address = trader.getString("adress");
                    String start_time = trader.getString("start_time");
                    String end_time = trader.getString("end_time");
                    String order_type_state = trader.getString("order_type_state");

                    tipsTv.setText(order_type_state);
                    if (type.equals("0")) {
                        // 扣费
                        moneyTv.setText("- " + money);
                        originTips.setText("扣费来源");
                        circleImageBorderView.setImageResource(R.drawable.icon_money_subtract);
                    } else if (type.equals("1")) {
                        // 收入
                        moneyTv.setText("+ " + money);
                        originTips.setText("收入来源");
                        circleImageBorderView.setImageResource(R.drawable.icon_money_plus);
                    }

                    String[] startTimes = null;
                    String[] endTimes = null;
                    if (!TextUtils.isEmpty(start_time) && start_time.contains(" ")) {
                        startTimes = start_time.split(" ");
                    }
                    if (!TextUtils.isEmpty(end_time) && end_time.contains(" ")) {
                        endTimes = end_time.split(" ");
                    }

                    if (startTimes != null && endTimes != null) {
                        timeTv.setText(startTimes[0] + " " + startTimes[1] + "-" + endTimes[1]);
                    } else {
                        if (startTimes != null) {
                            timeTv.setText(startTimes[0] + " " + startTimes[1]);
                        } else if (endTimes != null) {
                            timeTv.setText(endTimes[0] + " " + endTimes[1]);
                        }
                    }
                    addressTv.setText(address);
                    if (trader_state.equals("1")) {// 待处理
                        stateTv.setText("待处理");
                    } else if (trader_state.equals("2")) {// 已成单
                        stateTv.setText("已成单");
                    } else if (trader_state.equals("3")) {// 进行中
                        stateTv.setText("进行中");
                    } else if (trader_state.equals("4")) {// 已结束
                        stateTv.setText("已结束");
                    } else if (trader_state.equals("5")) {// 模特取消报名
                        stateTv.setText("模特取消报名");
                    } else if (trader_state.equals("6")) {// 模特取消约会
                        stateTv.setText("模特取消约会");
                    } else if (trader_state.equals("7")) {// 用户取消约会
                        stateTv.setText("用户取消约会");
                    } else if (trader_state.equals("8")) {// 用户拒绝报名
                        stateTv.setText("用户拒绝报名");
                    } else if (trader_state.equals("9")) {// 模特拒绝预约
                        stateTv.setText("模特拒绝预约");
                    } else if (trader_state.equals("10")) {
                        stateTv.setText("订单失效");
                    } else if (trader_state.equals("11")) {// 用户取消预约
                        stateTv.setText("用户取消预约");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {
                toast.showText(msg);
            }

            @Override
            public void completed() {

            }
        });
    }

    @Override
    public void initEvents() {
        super.initEvents();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        findViewById(R.id.activity_money_operation_detailas_doubt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.callPhoneAlert(v.getContext(), Constants.PhoneConstants.CONTACT_US_PHONE);
            }
        });
    }
}
