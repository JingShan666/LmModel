package bbc.com.moteduan_lib.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;

import com.tencent.mm.sdk.modelmsg.SendAuth;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.app.App;
import bbc.com.moteduan_lib.base.BaseTranslucentActivity;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.home.EditActivity;
import bbc.com.moteduan_lib.home.HomeActivity;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.tools.PermissionHelper;
import bbc.com.moteduan_lib.tools.ToastUtils;
import cn.sharesdk.framework.ShareSDK;
public class LoginFirstActivity extends BaseTranslucentActivity implements View.OnClickListener {

    private ImageButton phoneregister;
    private ImageButton phonelogin;

    private ImageButton weixin_login;
    private PermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPermissionHelper = new PermissionHelper(this);
        mPermissionHelper.checkPermisson(new PermissionHelper.OnPermissionListener() {
            @Override
            public void onAgreePermission() {
                // do something
                directLogin();
            }
            @Override
            public void onDeniedPermission() {
                ToastUtils.getInstance(App.getApp().getBaseContext()).showText("拒绝了定位权限");
                finish();
            }
        }, Manifest.permission.ACCESS_COARSE_LOCATION);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mPermissionHelper.onResume(); // 当界面一定通过权限才能继续，就要加上这行
        if (weixin_login != null) {
            weixin_login.setEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void directLogin() {
        int account = SpDataCache.getSelfInfo(LoginFirstActivity.this).getData().getM_account();
        LogDebug.err("m_account" + account);
        if (account != 0) {
            startActivity(new Intent(LoginFirstActivity.this, HomeActivity.class));
            finish();
        }else{
            setContentView(R.layout.activity_login_first);
            initView();
        }
    }

    @Override
    public void initView() {
        /*myVideoView = (FullScreenVideoView) findViewById(R.id.myVideoView);
        uri = "android.resource://" + getPackageName() + "/" + R.raw.splashvideo;
        myVideoView.setVideoURI(Uri.parse(uri));
        myVideoView.start();
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });

        myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                myVideoView.setVideoURI(Uri.parse(uri));
                myVideoView.start();

            }
        });*/

        phoneregister = (ImageButton) findViewById(R.id.phoneregister);
        phoneregister.setOnClickListener(this);
        phonelogin = (ImageButton) findViewById(R.id.phonelogin);
        phonelogin.setOnClickListener(this);

        weixin_login = (ImageButton) findViewById(R.id.weixinlogin);

        weixin_login.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
//                startActivity(new android.content.Intent(LoginFirst.this, RegisterThree_Infm.class));
                if (!App.wxapi.isWXAppInstalled()) {
                    toast.showText("请安装微信客户端之后再进行登录");
                } else {
                    getCode();
                }

            }
        });


    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.phoneregister) {
            startActivity(new Intent(LoginFirstActivity.this, EditActivity.class));

        }
    }



    //获取微信访问getCode
    public void getCode() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        App.wxapi.sendReq(req);
        weixin_login.setEnabled(false);
        LogDebug.err("微信登录请求成功");
    }

    /**
     * -------------------------微信第三方登录结束--------------------
     */


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
