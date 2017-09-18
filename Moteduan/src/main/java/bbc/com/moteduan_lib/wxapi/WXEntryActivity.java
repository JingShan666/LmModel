package bbc.com.moteduan_lib.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.app.App;
import bbc.com.moteduan_lib.bean.WeiXinLoginGetTokenBean;
import bbc.com.moteduan_lib.bean.WeiXinLoginGetUserinfoBean;
import bbc.com.moteduan_lib.bean.utils.JsonUtil;
import bbc.com.moteduan_lib.constant.Constants;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.home.HomeActivity;
import bbc.com.moteduan_lib.home.SubmitCodeActivity;
import bbc.com.moteduan_lib.log.LogDebug;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.tools.ToastUtils;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

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
                LogDebug.err("获取code成功：" + code);
                if (code != null) {
//                    new AsynctaskToken().execute("https://api.weixin.qq.com/sns/oauth2/access_token?" + "appid=" +
//                            Constants.WEIXIN_APP_ID + "&secret=" + Constants.WEIXIN_APP_SECRET + "&grant_type=authorization_code" + "&code=" + code);


                    Map<String, Object> map = new HashMap();
                    map.put("appid", Constants.WEIXIN_APP_ID);
                    map.put("secret", Constants.WEIXIN_APP_SECRET);
                    map.put("grant_type", "authorization_code");
                    map.put("code", code);
                    acquireToken(map);
                }
                break;
            default:
                LogDebug.err("获取code失败");
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
                    login(map, bean);

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

    private void login(Map map, final WeiXinLoginGetUserinfoBean bean) {
        Xutils.post(Url.wxLogin, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.err("result==" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    String code = json.getString("code");
                    String msg = json.getString("tips");
                    if (!"200".equals(code)) {
                        ToastUtils.getInstance(WXEntryActivity.this).showText(msg);
                        finish();
                        return;
                    }
                    SpDataCache.saveAccount(WXEntryActivity.this, bean.getHeadimgurl(), bean.getNickname(), bean.getUnionid(), bean.getSex() + "");
                    SpDataCache.saveSelfInfo(WXEntryActivity.this, result);
                    SharedPreferences sp = getSharedPreferences(SubmitCodeActivity.InviteCode, MODE_PRIVATE);
                    String code1 = sp.getString("code", "");
                    if ("200".equals(code1)) {
                        startActivity(new Intent(WXEntryActivity.this, HomeActivity.class));
                    } else {
                        startActivity(new android.content.Intent(WXEntryActivity.this, SubmitCodeActivity.class));
                    }

                    finish();


                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                    ToastUtils.getInstance(WXEntryActivity.this).showText("网络忙，请重新登录");
                    finish();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogDebug.err("onError" + ex.toString());
                ToastUtils.getInstance(WXEntryActivity.this).showText("网络忙，请重新登录");
                finish();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogDebug.err("onCancelled" + cex.toString());
                ToastUtils.getInstance(WXEntryActivity.this).showText("取消登录");
                finish();
            }

            @Override
            public void onFinished() {

            }
        });
    }

    }
