package bbc.com.moteduan_lib2.mineInvite;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.dialog.DialogUtils;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.network.ImageLoad;
import bbc.com.moteduan_lib.network.Req;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.tools.ToastUtils;
import bbc.com.moteduan_lib2.bean.MineOrderDetailsBean;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import wei.toolkit.utils.DateUtils;
import wei.toolkit.utils.ToastUtil;

/**
 * Created by Administrator on 2017/6/8 0008.
 */

public class RealtimeOrderDetailsActivity extends BaseActivity {
    private ImageView back;
    private TextView title;
    private ImageView inviteTypeImg;
    private TextView orderPrice;
    private TextView orderPersonNumber;
    private TextView orderDate;
    private TextView orderInviteSite;
    private TextView orderInviteAddress;
    private ImageView portrait;
    private TextView name;
    private ImageView approveIcon;
    private LinearLayout conversationLL;
    private ImageView sendMsg;
    private ImageView callPhone;
    private TextView age;
    private TextView weight;
    private TextView bt;
    private TextView decline;
    private TextView tips;
    private static String trader_order_id = "";
    private MineOrderDetailsBean bean;
    private TextView cancelOrder;
    private CountDownTimer countDownTimer;
    private wei.toolkit.utils.DialogUtils.DataReqDialog dataReqDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_realtime_details);
        trader_order_id = getIntent().getStringExtra("orderId");
        dataReqDialog = new wei.toolkit.utils.DialogUtils.DataReqDialog(this);
        initView();
        initData();
        initEvents();
    }

    @Override
    public void initView() {
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        inviteTypeImg = (ImageView) findViewById(R.id.activity_order_appointment_details_img);
        orderPrice = (TextView) findViewById(R.id.activity_order_appointment_details_price);
        orderPersonNumber = (TextView) findViewById(R.id.activity_order_appointment_details_person_number);
        orderDate = (TextView) findViewById(R.id.activity_order_appointment_details_order_date);
        orderInviteSite = (TextView) findViewById(R.id.activity_order_appointment_details_site);
        orderInviteAddress = (TextView) findViewById(R.id.activity_order_appointment_details_address);
        portrait = (ImageView) findViewById(R.id.activity_order_appointment_details_portrait);
        name = (TextView) findViewById(R.id.activity_order_appointment_details_name);
        approveIcon = (ImageView) findViewById(R.id.activity_order_appointment_details_approve);
        conversationLL = (LinearLayout) findViewById(R.id.activity_order_appointment_details_conversation_ll);
        sendMsg = (ImageView) findViewById(R.id.activity_order_appointment_details_msg);
        callPhone = (ImageView) findViewById(R.id.activity_order_appointment_details_call);
        age = (TextView) findViewById(R.id.activity_order_appointment_details_age);
        weight = (TextView) findViewById(R.id.activity_order_appointment_details_weight);
        bt = (TextView) findViewById(R.id.activity_order_appointment_details_bt);
        decline = (TextView) findViewById(R.id.activity_order_appointment_details_decline);
        tips = (TextView) findViewById(R.id.activity_order_appointment_details_tips);
        cancelOrder = (TextView) findViewById(R.id.sure);
    }

    @Override
    public void initData() {

        Map<String, Object> map = new HashMap<>();
        map.put("traderorder_id", trader_order_id);
        Req.post(Url.realtimeInviteOrderDetails, map, new Req.ReqCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(String result) {
                bean = new Gson().fromJson(result, MineOrderDetailsBean.class);
                if (!"200".equals(bean.getCode())) {
                    ToastUtil.show(RealtimeOrderDetailsActivity.this, bean.getTips());
                    return;
                }
                title.setText(bean.getTraderOrder().getSmall_navigation());
                ImageLoad.bind(inviteTypeImg, bean.getTraderOrder().getPhotos_typeb());
                orderPrice.setText(bean.getTraderOrder().getTrader_money() + "币/单/人");

                String startTime = bean.getTraderOrder().getStart_time();
                final String endTime = bean.getTraderOrder().getEnd_time();
                if (!TextUtils.isEmpty(startTime) && startTime.contains(" ") && !TextUtils.isEmpty(endTime) && endTime.contains(" ")) {
                    String time = DateUtils.getCustomFormatTime(startTime, endTime);
                    orderDate.setText(time);
                }


                String address = bean.getTraderOrder().getAdress();
                if (!TextUtils.isEmpty(address)) {
                    if (address.contains("==")) {
                        String[] addressList = address.split("==");
                        orderInviteSite.setText(addressList[1]);
                        orderInviteAddress.setText(addressList[0]);
                    } else {
                        orderInviteSite.setText("");
                        orderInviteAddress.setText(address);
                    }
                }
                ImageLoad.bind(portrait, bean.getTraderOrder().getU_head_photo());
                name.setText(bean.getTraderOrder().getU_nick_name());
                /*如果==1 显示认证图标*/
                if (1 == bean.getTraderOrder().getM_type()) {
                    approveIcon.setVisibility(View.VISIBLE);
                }
                age.setText(bean.getTraderOrder().getU_age() + "岁");
                weight.setText(bean.getTraderOrder().getU_tall() + "cm / " + bean.getTraderOrder().getU_weight() + "kg");

                final int traderState = bean.getTraderOrder().getTrader_state();
                switch (traderState) {
                    case 1:
                        // 待处理
                        // 实时订单是直接到已成单的，没有待处理状态
                        break;
                    case 2:
                        // 已成单
                        conversationLL.setVisibility(View.VISIBLE);
                        bt.setEnabled(true);
                        bt.setText(String.format("开始%1$s", getResources().getString(R.string.deal_appellation)));
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtils.inputInviteCodeDialog(RealtimeOrderDetailsActivity.this, "", new Handler.Callback() {
                                    @Override
                                    public boolean handleMessage(Message msg) {
                                        if (null == msg) return false;
                                        String content = (String) msg.obj;
                                        if (TextUtils.isEmpty(content)) {
                                            toast.showText(String.format("%1$s码不能为空", getResources().getString(R.string.deal_appellation)));
                                            return false;
                                        }
                                        dataReqDialog.show();
                                        Map<String, Object> map = new HashMap<String, Object>();
                                        map.put("traderorder_id", trader_order_id);
                                        map.put("verification", content);
                                        Req.post(Url.startInvite, map, new Req.ReqCallback() {
                                            @Override
                                            public void success(String result) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(result);
                                                    String code = jsonObject.getString("code");
                                                    String tips = jsonObject.getString("tips");
                                                    toast.showText(tips);
                                                    if (!"200".equals(code)) {
                                                        return;
                                                    }
                                                    final String endTime = jsonObject.getString("time_end_of_verfication");
                                                    cancelOrder.setText("");
                                                    cancelOrder.setOnClickListener(null);
                                                    bt.setOnClickListener(null);
                                                    Req.post(Url.getSystemTime, null, new Req.ReqCallback() {
                                                        @Override
                                                        public void success(String result) {
                                                            JSONObject jsonObject = null;
                                                            try {
                                                                jsonObject = new JSONObject(result);
                                                                String code = jsonObject.getString("code");
                                                                String currentTime = jsonObject.getString("time");
                                                                if (!"200".equals(code) || TextUtils.isEmpty(currentTime)) {
                                                                    currentTime = getCurrentTime();
                                                                }
                                                                countDownTime(currentTime, endTime, bt);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void failed(String msg) {
                                                            countDownTime(getCurrentTime(), endTime, bt);
                                                        }

                                                        @Override
                                                        public void completed() {
                                                        }
                                                    });
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void failed(String msg) {
                                                toast.showText(getString(R.string.error_network_block));
                                            }

                                            @Override
                                            public void completed() {
                                                dataReqDialog.dismiss();

                                            }
                                        });
                                        return false;
                                    }
                                });
                            }
                        });
                        cancelOrder.setText(String.format("取消%1$s", getResources().getString(R.string.deal_appellation)));
                        cancelOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtils.showTextDialog(RealtimeOrderDetailsActivity.this, String.format("确定要取消%1$s吗？", getResources().getString(R.string.deal_appellation)), new Handler.Callback() {
                                    @Override
                                    public boolean handleMessage(Message msg) {
                                        if (null == msg) return false;
                                        if (msg.what == 1) {
                                            dataReqDialog.show();
                                            Map<String, Object> map = new HashMap<String, Object>();
                                            map.put("traderorder_id", trader_order_id);
                                            Req.post(Url.cancelInvite, map, new Req.ReqCallback() {
                                                @Override
                                                public void success(String result) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(result);
                                                        String code = jsonObject.getString("code");
                                                        String tips = jsonObject.getString("tips");
                                                        toast.showText(tips);
                                                        if (!"200".equals(code)) {
                                                            return;
                                                        }
                                                        finish();

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void failed(String msg) {
                                                    toast.showText(getString(R.string.error_network_block));
                                                }

                                                @Override
                                                public void completed() {
                                                    dataReqDialog.dismiss();


                                                }
                                            });
                                        }
                                        return false;
                                    }
                                });
                            }
                        });

                        break;
                    case 3:

                        // 进行中
                        final String datingendTime = bean.getTraderOrder().getTime_end_of_verfication();
                        if (TextUtils.isEmpty(datingendTime)) {
                            bt.setText("计时时间读取错误");
                            return;
                        }
                        findViewById(R.id.activity_order_appointment_details_conversation_ll).setVisibility(View.VISIBLE);
                        Req.post(Url.getSystemTime, null, new Req.ReqCallback() {
                            @Override
                            public void success(String result) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(result);
                                    String code = jsonObject.getString("code");
                                    String currentTime = jsonObject.getString("time");
                                    if (!"200".equals(code) || TextUtils.isEmpty(currentTime)) {
                                        currentTime = getCurrentTime();
                                    }
                                    countDownTime(currentTime, datingendTime, bt);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void failed(String msg) {
                                countDownTime(getCurrentTime(), datingendTime, bt);
                            }

                            @Override
                            public void completed() {

                            }
                        });
                        break;
                    case 4:
                        // 已结束
                        if (!TextUtils.isEmpty(startTime) && startTime.contains(" ") && !TextUtils.isEmpty(endTime) && endTime.contains(" ")) {
                            String[] startTimes = startTime.split(" ");
                            String[] endTimes = endTime.split(" ");
                            bt.setText(String.format("%1$s结束\n", getResources().getString(R.string.deal_appellation)) + startTimes[0] + " " + startTimes[1] + "-" + endTimes[1]);
                        } else {
                            bt.setText(String.format("%1$s结束", getResources().getString(R.string.deal_appellation)));
                        }
                        break;
                    case 5:
                        // 模特取消报名
                        bt.setText("您已取消报名");
                        break;
                    case 6:
                        // 模特取消约会
                        bt.setText(String.format("您已取消%1$s", getResources().getString(R.string.deal_appellation)));
                        break;
                    case 7:
                        // 用户取消约会
                        bt.setText(String.format("用户已取消%1$s", getResources().getString(R.string.deal_appellation)));
                        break;
                    case 8:
                        // 用户拒绝报名
                        bt.setText("用户拒绝报名");
                        break;
                    case 9:
                        // 模特拒绝预约
                        bt.setText(String.format("您已拒绝%1$s", getResources().getString(R.string.deal_appellation)));
                        break;
                    case 10:
                        bt.setText(String.format("%1$s失效",getResources().getString(R.string.deal_appellation)));
                        break;
                    case 11:
                        // 用户取消预约
                        bt.setText("用户已取消预约");
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void failed(String msg) {
                ToastUtil.show(RealtimeOrderDetailsActivity.this, msg);
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
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != bean) {
                    if (null != RongIM.getInstance()) {
                        if (TextUtils.isEmpty(bean.getTraderOrder().getU_id())) {
                            ToastUtils.getInstance(RealtimeOrderDetailsActivity.this).showText("聊天启动失败，未找到对方ID。");
                            return;
                        }
                        RongIM.getInstance().startConversation(RealtimeOrderDetailsActivity.this,
                                Conversation.ConversationType.PRIVATE, bean.getTraderOrder().getU_id(), bean.getTraderOrder().getU_nick_name());
                    }
                }
            }
        });

        callPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != bean) {
                    DialogUtils.callPhoneAlert(RealtimeOrderDetailsActivity.this, bean.getTraderOrder().getU_mobile());
                }
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    private void countDownTime(String surrectDate, String endDate, final TextView showView) {
        LogDebug.log(ApplyOrderDetailsActivity.class.getSimpleName(), "surrectDate=" + surrectDate + " endDate=" + endDate);
        if (null != countDownTimer) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        Calendar currect = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            currect.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(surrectDate));
            end.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LogDebug.log(ApplyOrderDetailsActivity.class.getSimpleName(), end.getTimeInMillis() + "-" + currect.getTimeInMillis());
        countDownTimer = new CountDownTimer(end.getTimeInMillis() - currect.getTimeInMillis(), 1000) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                LogDebug.log(ApplyOrderDetailsActivity.class.getSimpleName(), millisUntilFinished + "");
                int s = (int) (millisUntilFinished / 1000);
                int ss = s % 60;
                int mm = s / 60 % 60;
                int hh = s / 60 / 60;
                showView.setText(String.format("%02d:%02d:%02d", hh, mm, ss));
            }

            @Override
            public void onFinish() {

            }
        };

        countDownTimer.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != countDownTimer) {
            countDownTimer.cancel();
        }
    }
}
