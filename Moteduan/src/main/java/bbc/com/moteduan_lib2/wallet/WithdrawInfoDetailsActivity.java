package bbc.com.moteduan_lib2.wallet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liemo.shareresource.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.constant.Constants;
import bbc.com.moteduan_lib.dialog.DialogUtils;
import bbc.com.moteduan_lib.network.Req;
import wei.toolkit.utils.Loger;
import wei.toolkit.widget.CircleImageBorderView;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class WithdrawInfoDetailsActivity extends BaseActivity {
    private static final String TAG ="WithdrawInfoDetailsActivity";
    private ImageView back;
    private TextView title;
    private TextView moneyTv;
    private TextView tipsTv;
    private TextView timeTv;
    private TextView stateTv;
    private String detailsId = "";
    private CircleImageBorderView circleImageBorderView;
    private TextView numberTv;
    private TextView name;
    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_info_details);
        detailsId = getIntent().getStringExtra("detailsId");
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
        moneyTv = (TextView) findViewById(R.id.activity_withdraw_info_details_money);
        tipsTv = (TextView) findViewById(R.id.activity_withdraw_info_details_tips);
        timeTv = (TextView) findViewById(R.id.activity_withdraw_info_details_time);
        stateTv = (TextView) findViewById(R.id.activity_withdraw_info_details_state);
        circleImageBorderView = (CircleImageBorderView) findViewById(R.id.activity_withdraw_info_details_portrait);
        numberTv = (TextView) findViewById(R.id.activity_withdraw_info_details_number);
        name = (TextView) findViewById(R.id.activity_withdraw_info_details_name);
    }

    @Override
    public void initData() {
        title.setText("明细");

        Map<String, Object> map = new HashMap<>();
        map.put("order_no", detailsId);
        Req.post(Url.Wallet.withdrawDetails, map, new Req.ReqCallback() {
            @SuppressLint("SetTextI18n")
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
                    String user_name = trader.getString("user_name");
                    String time = trader.getString("time");
                    String bank_name = trader.getString("bank_name");
                    String bank_card = trader.getString("bank_card");
                    double model_money = trader.getDouble("model_money");
                    int do_state = trader.getInt("do_state");
                    String withdraw_number = trader.getString("withdraw_number");
                    String explains = trader.getString("explains");

                    String bank_card_last = "";
                    if(!TextUtils.isEmpty(bank_card)&&bank_card.length() > 4){
                        bank_card_last = bank_card.substring(bank_card.length() - 4,bank_card.length());
                    }
                    tipsTv.setText(explains + "-" + bank_name + "尾号"+bank_card_last);
                    moneyTv.setText("-"+decimalFormat.format(model_money));
                    numberTv.setText(withdraw_number);
                    timeTv.setText(time);
                    name.setText(user_name);
                    if(do_state == 0){
                        stateTv.setText("未到账");
                    }else if(do_state == 1){
                        stateTv.setText("已到账");
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
        findViewById(R.id.activity_withdraw_info_details_doubt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.callPhoneAlert(v.getContext(), Constants.PhoneConstants.CONTACT_US_PHONE);
            }
        });
    }
}
