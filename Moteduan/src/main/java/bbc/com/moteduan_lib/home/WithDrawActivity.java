package bbc.com.moteduan_lib.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jungly.gridpasswordview.GridPasswordView;
import com.liemo.shareresource.Url;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.MD5;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.bean.LoginBean;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.dialog.DialogUtils;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.network.Req;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;
import bbc.com.moteduan_lib.tools.ToastUtils;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.ToastUtil;


public class WithDrawActivity extends BaseActivity {

    private EditText totalmoney;
    private RelativeLayout lineview;
    private TextView textView;
    private TextView textView2;
    private TextView textView4;
    private TextView moneyfinal;
    private TextView textView7;
    private TextView textView8;
    private LinearLayout linearLayout2;
    private TextView textView9;
    private EditText holdername_et;
    private RelativeLayout relativeLayout3;
    private TextView textView10;
    private EditText cardnumber_et;
    private RelativeLayout relativeLayout4;
    private TextView textView11;
    private EditText bankname_et;
    private RelativeLayout relativeLayout5;
    private TextView withdraw_rules;
    private TextView rules1;
    private TextView rules2;
    private RelativeLayout relativeLayout10;
    private TextView textView5;
    private RelativeLayout withdraw_sub_layout;
    private RelativeLayout relativeLayout1;

    private TextView total_withdraw;

    private AlertDialog gotosetpwddialog;
    private AlertDialog setpwddialog;
    private AlertDialog withdrawdialog;
    private AlertDialog withdrawsuccessdialog;

    private GridPasswordView psw;
    private GridPasswordView psw2;
    private GridPasswordView psw3;
    private String lastfour;
    private double canWithDraw;
    private String format;
    private String totalm;
    private TextView moneyk;
    private TextView card;
    private String holdername;
    private String cardnumber;
    private String bankname;
    private LoginBean loginBean;
    private String balance;
    private wei.toolkit.utils.DialogUtils.DataReqDialog dataReqDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw);
        dataReqDialog = new wei.toolkit.utils.DialogUtils.DataReqDialog(this);
        loginBean = SpDataCache.getSelfInfo(WithDrawActivity.this);
        if (null == loginBean) {
            toast.showText("您还未登录");
            finish();
        }
        balance = getIntent().getStringExtra("balance");
        if (TextUtils.isEmpty(balance)) balance = "0.00";

        initView();
        initData();
    }

    public void initView() {

        total_withdraw = (TextView) findViewById(R.id.total_withdraw);
        totalmoney = (EditText) findViewById(R.id.totalmoney);
        lineview = (RelativeLayout) findViewById(R.id.view);
        textView = (TextView) findViewById(R.id.textView);
        // textView2 = (TextView) findViewById(R.id.textView2);
        textView4 = (TextView) findViewById(R.id.textView4);
        moneyfinal = (TextView) findViewById(R.id.moneyfinal);
        textView7 = (TextView) findViewById(R.id.textView7);
        // textView8 = (TextView) findViewById(R.id.textView8);
        //linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
        textView9 = (TextView) findViewById(R.id.textView9);
        holdername_et = (EditText) findViewById(R.id.holdername_et);
        relativeLayout3 = (RelativeLayout) findViewById(R.id.relativeLayout3);
        textView10 = (TextView) findViewById(R.id.textView10);
        cardnumber_et = (EditText) findViewById(R.id.cardnumber_et);
        relativeLayout4 = (RelativeLayout) findViewById(R.id.relativeLayout4);
        textView11 = (TextView) findViewById(R.id.textView11);
        bankname_et = (EditText) findViewById(R.id.bankname_et);
        relativeLayout5 = (RelativeLayout) findViewById(R.id.relativeLayout5);
        withdraw_rules = (TextView) findViewById(R.id.withdraw_rules);
        rules1 = (TextView) findViewById(R.id.rules1);
        rules2 = (TextView) findViewById(R.id.rules2);
        relativeLayout10 = (RelativeLayout) findViewById(R.id.relativeLayout10);
        textView5 = (TextView) findViewById(R.id.textView5);
        withdraw_sub_layout = (RelativeLayout) findViewById(R.id.withdraw_sub_layout);
        //relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        Req.post(Url.getWithdrawTips, null, new Req.ReqCallback() {
            @Override
            public void success(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (!"200".equals(code)) {
                        return;
                    }
                    String rules = jsonObject.getString("rules");
                    withdraw_rules.setText("提现规则:\n" + rules);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {

            }

            @Override
            public void completed() {

            }
        });

        double m_balance = Double.parseDouble(balance);
        double v = m_balance - 100;
        if (v < 0) {
            canWithDraw = 0;
        } else {
            canWithDraw = v * 0.8;
        }

        DecimalFormat df = new DecimalFormat("#.##");
        format = df.format(canWithDraw);
        moneyfinal.setText(format);
        total_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalmoney.setText(format);
            }
        });
        totalmoney.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        totalmoney.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(".") && dest.toString().length() == 0) {
                    return "0.";
                }
                if (dest.toString().contains(".")) {
                    int index = dest.toString().indexOf(".");
                    int mlength = dest.toString().substring(index).length();
                    if (mlength == 3) {
                        return "";
                    }
                }
                return null;
            }
        }});
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View gotosetpwd = LayoutInflater.from(this).inflate(R.layout.gotosetpwd, null);
        ImageView cut = (ImageView) gotosetpwd.findViewById(R.id.cut);
        cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotosetpwddialog.dismiss();
            }
        });
        TextView gotosetpwd_tv = (TextView) gotosetpwd.findViewById(R.id.gotosetpwd_tv);
        builder.setView(gotosetpwd);
        gotosetpwddialog = builder.create();

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        View setpwd = LayoutInflater.from(this).inflate(R.layout.setpassword, null);
        psw = (GridPasswordView) setpwd.findViewById(R.id.pswView);
        psw2 = (GridPasswordView) setpwd.findViewById(R.id.pswView2);

        ImageView cut2 = (ImageView) setpwd.findViewById(R.id.cut);
        cut2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setpwddialog.dismiss();
            }
        });
        TextView confirmiv = (TextView) setpwd.findViewById(R.id.confirmiv);
        builder2.setView(setpwd);
        setpwddialog = builder2.create();
//        setpwddialog.setContentView(setpwd);

        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        View withdraw = LayoutInflater.from(this).inflate(R.layout.withdraw, null);
        moneyk = (TextView) withdraw.findViewById(R.id.moneyk);
        card = (TextView) withdraw.findViewById(R.id.card);
//        if (cardnumber!=null){
//            lastfour = cardnumber.substring(cardnumber.length()-4, cardnumber.length()-1);
//        }
//         moneyk.setText(totalm);
//        card.setText(bankname+"("+lastfour+")");

        psw3 = (GridPasswordView) withdraw.findViewById(R.id.pswView3);
        psw3.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {

            }

            @Override
            public void onInputFinish(String psw) {
                String s = MD5.md5(psw);
                if (!s.equals(SpDataCache.getSelfInfo(WithDrawActivity.this).getData().getM_out_pw())) {
                    toast.showText("输入的密码不正确，请重试");
                    return;
                }
                if (psw.length() == 6) {
                    Loger.log("可以提现了");
                    Map<String, Object> map = new HashMap<>();
                    map.put("member_id", loginBean.getData().getM_id());
                    map.put("money", totalm);
                    map.put("card_number", cardnumber);
                    map.put("name", holdername);
                    map.put("bank", bankname);

                    Xutils.post(Url.withdraw, map, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String code = (String) jsonObject.get("code");
                                if (code.equals("200")) {
                                    ToastUtils.getInstance(WithDrawActivity.this).showText("提现成功");
                                    LoginAgain();
                                    withdrawdialog.dismiss();
                                    finish();
                                } else {
                                    ToastUtils.getInstance(WithDrawActivity.this).showText("提现失败");
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
        });


        ImageView cut3 = (ImageView) withdraw.findViewById(R.id.cut);
        cut3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                withdrawdialog.dismiss();
            }
        });
        TextView forgetpwd = (TextView) withdraw.findViewById(R.id.forgetpwd);
        builder3.setView(withdraw);
        withdrawdialog = builder3.create();


        AlertDialog.Builder builder4 = new android.app.AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.withdraw_apply_layout, null);

        builder4.setView(inflate);
        withdrawsuccessdialog = builder4.create();

        /*提现*/
        withdraw_sub_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holdername = holdername_et.getText().toString().trim();
                bankname = bankname_et.getText().toString().trim();
                cardnumber = cardnumber_et.getText().toString().trim();
                totalm = totalmoney.getText().toString().trim();
                if (TextUtils.isEmpty(totalm) || "0".equals(totalm)) {
                    toast.showText("请确定要提现金额");
                    return;
                }
                if (TextUtils.isEmpty(holdername)) {
                    toast.showText("请输入持卡人姓名");
                    return;
                }
                if (TextUtils.isEmpty(cardnumber)) {
                    toast.showText("请输入卡号");
                    return;
                }
                if (!(cardnumber.length() > 15 && cardnumber.length() < 21)) {
                    Toast.makeText(WithDrawActivity.this, "您输入的银行卡号不正确，请核对后输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(bankname)) {
                    toast.showText("请输入开户行");
                    return;
                }

                double tm = Double.parseDouble(totalm);
                String moneyf = moneyfinal.getText().toString().trim();
                double tf = Double.parseDouble(moneyf);
                if (tm > tf) {
                    ToastUtils.getInstance(WithDrawActivity.this).showText("余额不足，请确认余额");
                    return;
                }

                dataReqDialog.show();
                HashMap<String, Object> getCodeHashMap = new HashMap<>();
                getCodeHashMap.put("mobile", loginBean.getData().getM_mobile());
                getCodeHashMap.put("purpose", "bangding");
                Xutils.post(Url.ugetCode, getCodeHashMap, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Loger.log("requestPhoneCode result=" + result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String code = jsonObject.getString("code");
                            String tips = jsonObject.getString("tips");
                            if (!"200".equals(code)) {
                                toast.showText(tips);
                            } else {
                                DialogUtils.inputInviteCodeDialog(WithDrawActivity.this, "请输入您收到的验证码", new Handler.Callback() {
                                    @Override
                                    public boolean handleMessage(Message msg) {
                                        if (msg == null) return false;
                                        String verificationCode = (String) msg.obj;
                                        if (TextUtils.isEmpty(verificationCode)) {
                                            toast.showText("验证码不能为空");
                                            return false;
                                        }

                                        dataReqDialog.show();
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("member_id", loginBean.getData().getM_id());
                                        map.put("money", totalm);
                                        map.put("card_number", cardnumber);
                                        map.put("name", holdername);
                                        map.put("bank", bankname);
                                        map.put("mobile", loginBean.getData().getM_mobile());
                                        map.put("code", verificationCode);
                                        map.put("purpose", "bangding");

                                        Xutils.post(Url.withdraw, map, new Callback.CommonCallback<String>() {
                                            @Override
                                            public void onSuccess(String result) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(result);
                                                    String code = jsonObject.getString("code");
                                                    String tips = jsonObject.getString("tips");
                                                    if (code.equals("200")) {
                                                        ToastUtils.getInstance(WithDrawActivity.this).showText(tips);
                                                        startActivity(new Intent(WithDrawActivity.this, bbc.com.moteduan_lib2.home.HomeActivity.class));
                                                    } else {
                                                        ToastUtils.getInstance(WithDrawActivity.this).showText(tips);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onError(Throwable ex, boolean isOnCallback) {
                                                ToastUtil.show(WithDrawActivity.this,getResources().getString(R.string.error_network_block));
                                            }

                                            @Override
                                            public void onCancelled(CancelledException cex) {

                                            }

                                            @Override
                                            public void onFinished() {
                                               dataReqDialog.dismiss();
                                            }
                                        });

                                        return false;
                                    }
                                });


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
                        dataReqDialog.dismiss();
                    }
                });
            }
        });


        gotosetpwd_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotosetpwddialog.dismiss();
                setpwddialog.show();
                setpwddialog.getWindow().setGravity(Gravity.CENTER);
                setpwddialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        });

        confirmiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (psw.getPassWord().equals("") || psw2.getPassWord().equals("")) {
                    Toast.makeText(WithDrawActivity.this, "请输入正确密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!psw.getPassWord().equals(psw2.getPassWord())) {
                    ToastUtils.getInstance(WithDrawActivity.this).showText("请确认两次输入的密码一致");
                    return;
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("mid", SpDataCache.getSelfInfo(WithDrawActivity.this).getData().getM_head_photo());
                    map.put("outPass", psw.getPassWord());
                    LogDebug.err(psw.getPassWord());
                    Xutils.post(Url.setpwd, map, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String code = (String) jsonObject.get("code");
                                if (code.equals("200")) {
                                    setpwddialog.dismiss();
                                    ToastUtils.getInstance(WithDrawActivity.this).showText("密码设置成功");
                                    LoginAgain();
                                } else {
                                    ToastUtils.getInstance(WithDrawActivity.this).showText("密码设置失败");
                                    return;
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
                cardnumber = cardnumber_et.getText().toString().trim();
                bankname = bankname_et.getText().toString().trim();
                lastfour = cardnumber.substring(cardnumber.length() - 4, cardnumber.length());

                withdrawdialog.show();
                moneyk.setText(totalm);
                card.setText(bankname + "(" + lastfour + ")");
                withdrawdialog.getWindow().setGravity(Gravity.CENTER);
                withdrawdialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        });
    }

    private void LoginAgain() {
        SharedPreferences sharedPreferences = getSharedPreferences(SpDataCache.ACCOUNT, Context.MODE_PRIVATE);
        String phtotourl = sharedPreferences.getString("phtotourl", "");
        String name = sharedPreferences.getString("name", "");
        String account = sharedPreferences.getString("account", "");
        String sex1 = sharedPreferences.getString("sex", "");
//        String registerid = sharedPreferences.getString("registerid", "");
        Map<String, Object> map = new HashMap<>();
        map.put("m.m_head_photo", phtotourl);
        map.put("m.m_nick_name", name);
        map.put("m.m_wx_account", account);
        map.put("m.m_sex", sex1);
//        map.put("m_registration_id",registerid);
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
                        SpDataCache.saveSelfInfo(WithDrawActivity.this, result);

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
