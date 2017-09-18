package bbc.com.moteduan.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.app.App;
import bbc.com.moteduan_lib.bean.LoginBean;
import bbc.com.moteduan_lib.bean.WeiXinLoginGetTokenBean;
import bbc.com.moteduan_lib.bean.WeiXinLoginGetUserinfoBean;
import bbc.com.moteduan_lib.bean.utils.JsonUtil;
import bbc.com.moteduan_lib.constant.Constants;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.network.Req;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.tools.ToastUtils;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;
import bbc.com.moteduan_lib2.login.BingPhoneOrResetPasswordActivity;
import bbc.com.moteduan_lib2.login.CompleteSelfInfoActivity;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.ToastUtil;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static String TAG = "WXEntryActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        App.wxapi.handleIntent(getIntent(), this);

    }

    @Override
    public void onReq(BaseReq req) {
    }

    /**
     * 微信登录第一步：获取code
     */
    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == 2/*分享后Type为2*/) {
            finish();
            return;
        }
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) resp).code;
                Loger.log(TAG,"获取code成功：" + code);
                if (code != null) {
                    Map<String, Object> map = new HashMap();
                    map.put("appid", Constants.WEIXIN_APP_ID);
                    map.put("secret", Constants.WEIXIN_APP_SECRET);
                    map.put("grant_type", "authorization_code");
                    map.put("code", code);
                    acquireToken(map);
                }
                break;
            default:
                Loger.log(TAG,"获取code失败");
                finish();
                break;
        }

    }

    /**
     * 请求token
     *
     * @param map
     */
    private void acquireToken(Map map) {
        Xutils.get("https://api.weixin.qq.com/sns/oauth2/access_token?", map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    WeiXinLoginGetTokenBean bean = JsonUtil.toObjectByJson(result, WeiXinLoginGetTokenBean.class);
                    acquireInfo(bean.getAccess_token(), bean.getOpenid());
                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtils.getInstance(WXEntryActivity.this).showText("网络忙，请重新登录");
                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.getInstance(WXEntryActivity.this).showText("网络忙，请重新登录");
                finish();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                ToastUtils.getInstance(WXEntryActivity.this).showText("取消登录");
                finish();
            }

            @Override
            public void onFinished() {
            }
        });

    }

    /**
     * 请求微信用户信息
     *
     * @param token
     * @param openId
     */
    private void acquireInfo(String token, String openId) {
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", token);
        map.put("openid", openId);
        Xutils.get("https://api.weixin.qq.com/sns/userinfo?", map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    WeiXinLoginGetUserinfoBean bean = JsonUtil.toObjectByJson(result, WeiXinLoginGetUserinfoBean.class);
                    Map<String, Object> map = new HashMap<>();
                    map.put("m.m_head_photo", bean.getHeadimgurl());
                    map.put("m.m_nick_name", bean.getNickname());
                    String unionid = bean.getUnionid();
                    LogDebug.err("unionid" + unionid);
                    map.put("m.m_wx_account", unionid);
//            map.put("m_registration_id",App.getApp().getRegistration());
                    int sex = bean.getSex();
                    String se = sex + "";
                    map.put("m.m_sex", se);
                    phoneIsBind(bean);

                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtils.getInstance(WXEntryActivity.this).showText("网络忙，请重新登录");
                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.getInstance(WXEntryActivity.this).showText("网络忙，请重新登录");
                finish();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                ToastUtils.getInstance(WXEntryActivity.this).showText("取消登录");
                finish();
            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void phoneIsBind(final WeiXinLoginGetUserinfoBean bean){
        Map<String,Object> map = new HashMap<>();
        map.put("m_wx_account",bean.getUnionid());
        Req.post(Url.phone_Of_Verify, map, new Req.ReqCallback() {
            @Override
            public void success(String result) {
                Loger.log(TAG,"weixinLogin result=" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String tips = jsonObject.getString("tips");
                    if("200".equals(code)){
                     // 调用后台第三方注册接口,成功后直接返回用户信息
                        Intent intent = new Intent(WXEntryActivity.this, BingPhoneOrResetPasswordActivity.class);
                        intent.putExtra("from", "weixin");
                        intent.putExtra("account", bean.getUnionid());
                        intent.putExtra("m.m_nick_name", bean.getNickname());
                        intent.putExtra("m.m_head_photo", bean.getHeadimgurl());
                        intent.putExtra("m.m_sex", 2);
                        intent.putExtra("intcoding", 0);
                        startActivity(intent);
                        finish();
                    }else if("201".equals(code)){
                    // 去绑定手机号
                        Intent intent = new Intent(WXEntryActivity.this, BingPhoneOrResetPasswordActivity.class);
                        intent.putExtra("from", "weixin");
                        intent.putExtra("account", bean.getUnionid());
                        intent.putExtra("m.m_nick_name", bean.getNickname());
                        intent.putExtra("m.m_head_photo", bean.getHeadimgurl());
                        intent.putExtra("m.m_sex", 2);
                        intent.putExtra("intcoding", 1);
                        startActivity(intent);
                        finish();
                    }else if("202".equals(code)){
                    // 成功登录，返回用户数据
                        Gson gson = new Gson();
                        LoginBean loginBean = gson.fromJson(result, LoginBean.class);
                        LoginBean.DataBean data = loginBean.getData();
                        try {
                                String m_tag = data.getM_tag();
                                if (TextUtils.isEmpty(m_tag)) {
                                    Intent intent = new Intent(WXEntryActivity.this, CompleteSelfInfoActivity.class);
                                    intent.putExtra("data", loginBean);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    //保存数据
                                    SpDataCache.saveSelfInfo(WXEntryActivity.this,result);
                                    Intent intent = new Intent();
                                    intent.setAction(Constants.ACTION_LOGIN_SUCCESS);
                                    sendBroadcast(intent);
                                    finish();
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        ToastUtil.show(WXEntryActivity.this,tips);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {
                Toast.makeText(WXEntryActivity.this, msg, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void completed() {
            }
        });
    }


    }
