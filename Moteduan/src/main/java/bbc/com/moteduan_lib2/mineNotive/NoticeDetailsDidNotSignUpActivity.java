package bbc.com.moteduan_lib2.mineNotive;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liemo.shareresource.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.bean.LoginBean;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.dialog.DialogUtils;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.network.ImageLoad;
import bbc.com.moteduan_lib.network.Req;
import bbc.com.moteduan_lib.tools.ToastUtils;
import bbc.com.moteduan_lib2.AuthenticationActivity;
import bbc.com.moteduan_lib2.bean.InviteOrderDetailsBean;
import wei.toolkit.utils.DateUtils;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.ToastUtil;

/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class NoticeDetailsDidNotSignUpActivity extends BaseActivity {

    public static String TAG = "NoticeDetailsDidNotSignUpActivity";
    private ImageView back;
    private TextView title;
    private ImageView inviteTypeImg;
    private TextView orderPrice;
    private TextView orderPersonNumber;
    private TextView orderDate;
    private TextView orderTime;
    private TextView orderStature;
    private TextView orderAge;
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
    private TextView bwh;
    private TextView bt;
    private TextView decline;
    private TextView tips;
    private static String trader_order_id = "";
    private InviteOrderDetailsBean bean;
    private TextView cancelOrder;
    private TextView remark;
    private wei.toolkit.utils.DialogUtils.DataReqDialog dialogDataReq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);
        trader_order_id = getIntent().getStringExtra("orderId");
        if (TextUtils.isEmpty(trader_order_id)) {
            toast.showText("订单未找到");
            finish();
        }
        initView();
        initData();
        initEvents();
    }

    @Override
    public void initView() {
        dialogDataReq = new wei.toolkit.utils.DialogUtils.DataReqDialog(this);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        inviteTypeImg = (ImageView) findViewById(R.id.activity_notice_details_img);
        orderPrice = (TextView) findViewById(R.id.activity_notice_details_price);
        orderPersonNumber = (TextView) findViewById(R.id.activity_notice_details_person_number);
        orderDate = (TextView) findViewById(R.id.activity_notice_details_order_date);
        orderTime = (TextView) findViewById(R.id.activity_notice_details_order_time);
        orderStature = (TextView) findViewById(R.id.activity_notice_details_order_stature);
        orderAge = (TextView) findViewById(R.id.activity_notice_details_order_age);
        orderInviteSite = (TextView) findViewById(R.id.activity_notice_details_site);
        orderInviteAddress = (TextView) findViewById(R.id.activity_notice_details_address);
        portrait = (ImageView) findViewById(R.id.activity_notice_details_portrait);
        name = (TextView) findViewById(R.id.activity_notice_details_name);
        approveIcon = (ImageView) findViewById(R.id.activity_notice_details_approve);
        conversationLL = (LinearLayout) findViewById(R.id.activity_notice_details_conversation_ll);
        sendMsg = (ImageView) findViewById(R.id.activity_notice_details_msg);
        callPhone = (ImageView) findViewById(R.id.activity_notice_details_call);
        age = (TextView) findViewById(R.id.activity_notice_details_age);
        weight = (TextView) findViewById(R.id.activity_notice_details_weight);
        bwh = (TextView) findViewById(R.id.activity_notice_details_bwh);
        bt = (TextView) findViewById(R.id.activity_notice_details_bt);
        decline = (TextView) findViewById(R.id.activity_notice_details_decline);
        tips = (TextView) findViewById(R.id.activity_notice_details_tips);
        cancelOrder = (TextView) findViewById(R.id.sure);
        remark = (TextView) findViewById(R.id.activity_notice_details_remark);
        bt.setText("报名");
    }

    @Override
    public void initData() {
        Map<String, Object> map = new HashMap<>();
        map.put("trader_id", trader_order_id);
        LoginBean loginBean = SpDataCache.getSelfInfo(this);
        map.put("member_id", loginBean == null ? "" : loginBean.getData().getM_id());
        Req.post(Url.Invite.mineNoticeDetailsDidNotSignUp, map, new Req.ReqCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(String result) {
                Loger.log(TAG, result);
                bean = new Gson().fromJson(result, InviteOrderDetailsBean.class);
                if (!"200".equals(bean.getCode())) {
                    ToastUtil.show(NoticeDetailsDidNotSignUpActivity.this, bean.getTips());
                    return;
                }
                title.setText(bean.getTrader().getSmall_navigation());
                ImageLoad.bind(inviteTypeImg, bean.getTrader().getPhotos_typeb());
                int totalMoney = bean.getTrader().getTrader_money();
                int totalHours = bean.getTrader().getTrader_hours();
                int needPeople = bean.getTrader().getNeednum();
                int hoursPrice = bean.getTrader().getReward_price();
                orderPrice.setText(totalMoney + "币/单/人");
                orderPersonNumber.setText("需求：" + needPeople + "人");
                remark.setText(bean.getTrader().getRemarks());
                String startTime = bean.getTrader().getStart_time();
                String endTime = bean.getTrader().getEnd_time();
                if (!TextUtils.isEmpty(startTime) && startTime.contains(" ") && !TextUtils.isEmpty(endTime) && endTime.contains(" ")) {
                    String time = DateUtils.getCustomFormatTime(startTime, endTime);
                    if (time.contains(" ")) {
                        String[] times = time.split(" ");
                        orderDate.setText(times[0]);
                        orderTime.setText(times[1]);
                    } else {
                        orderDate.setText(startTime);
                        orderTime.setText(endTime);
                    }
                }

                orderStature.setText(bean.getTrader().getMin_tall() + "-" + bean.getTrader().getMax_tall() + "cm");
                orderAge.setText(bean.getTrader().getMin_age() + "-" + bean.getTrader().getMax_age() + "岁");

                String address = bean.getTrader().getAdress();
                if (!TextUtils.isEmpty(address)) {
                    if (address.contains("==")) {
                        String[] addressList = address.split("==");
                        if (addressList != null && addressList.length > 0) {
                            orderInviteAddress.setText(addressList[0]);
                            if (addressList.length > 1) {
                                orderInviteSite.setText(addressList[1]);
                            } else {
                                orderInviteSite.setText("");
                            }
                        }
                    } else {
                        orderInviteSite.setText("");
                        orderInviteAddress.setText(address);
                    }
                }
                ImageLoad.bind(portrait, bean.getTrader().getU_head_photo());
                name.setText(bean.getTrader().getU_nick_name());
                /*如果==1 显示认证图标*/
//                if (1 == bean.getTraderOrder().getM_type()) {
//                    approveIcon.setVisibility(View.VISIBLE);
//                }
                age.setText(bean.getTrader().getU_age() + "岁");
                weight.setText(bean.getTrader().getU_tall() + "cm / " + bean.getTrader().getU_weight() + "kg");
                if (bean.getTrader().getTrader_state() == 1) {
                    bt.setEnabled(true);
                }

                if(bean.getTrader().getK_num() == 1){
                    bt.setEnabled(false);
                    bt.setText("已报名");
                }

            }

            @Override
            public void failed(String msg) {
                ToastUtil.show(NoticeDetailsDidNotSignUpActivity.this, msg);
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

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoginBean loginBean = SpDataCache.getSelfInfo(NoticeDetailsDidNotSignUpActivity.this);
                if (null == loginBean || TextUtils.isEmpty(loginBean.getData().getM_id())) {
                    ToastUtil.show(NoticeDetailsDidNotSignUpActivity.this, "您还未登录，请先登录");
                    Intent intent = new Intent();
                    intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
                    NoticeDetailsDidNotSignUpActivity.this.startActivity(intent);
                    return;
                }
                new AlertDialog.Builder(NoticeDetailsDidNotSignUpActivity.this)
                        .setMessage("确定要报名吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                dialogDataReq.show();
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("member_id", loginBean.getData().getM_id());
                                map.put("trader_id", bean.getTrader().getTrader_id());
                                Req.post(Url.Invite.applyNotice, map, new Req.ReqCallback() {
                                    @Override
                                    public void success(String result) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(result);
                                            String code = jsonObject.getString("code");
                                            String tips = jsonObject.getString("tips");
                                            ToastUtils.getInstance(NoticeDetailsDidNotSignUpActivity.this).showText(tips);
                                            if (!"200".equals(code)) {
                                                return;
                                            }
                                            setResult(RESULT_OK);
                                            finish();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void failed(String msg) {
                                        ToastUtils.getInstance(NoticeDetailsDidNotSignUpActivity.this).showText(msg);
                                    }

                                    @Override
                                    public void completed() {
                                        dialogDataReq.dismiss();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

//                Map<String, Object> map = new HashMap<>();
//                map.put("member_id", SpDataCache.getSelfInfo(NoticeDetailsDidNotSignUpActivity.this).getData().getM_id());
//                Req.post(Url.getModelAuthState, map, new Req.ReqCallback() {
//
//                    @Override
//                    public void success(String result) {
//                        LogDebug.log(TAG, result);
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = new JSONObject(result);
//                            String code = jsonObject.getString("code");
//                            String tips = jsonObject.getString("tips");
//                            if (!"200".equals(code)) {
//                                startActivity(new Intent(NoticeDetailsDidNotSignUpActivity.this, AuthenticationActivity.class));
//                                ToastUtils.getInstance(NoticeDetailsDidNotSignUpActivity.this).showText(tips);
//                                return;
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            ToastUtils.getInstance(NoticeDetailsDidNotSignUpActivity.this).showText(e.getMessage());
//                        }
//                    }
//                    @Override
//                    public void failed(String msg) {
//                        ToastUtils.getInstance(NoticeDetailsDidNotSignUpActivity.this).showText(msg);
//                    }
//                    @Override
//                    public void completed() {
//                    }
//                });
            }
        });
    }


}
