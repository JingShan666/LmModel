package bbc.com.moteduan_lib.leftmenu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.liemo.shareresource.Url;

public class ShareActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton mBack;
    private ImageView mSharefriend;
    private RelativeLayout mTitleLayout;
    private WebView webView;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private String ucode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initView();
        initData();
    }

    @Override
    public void initView() {
        mBack = (ImageButton) findViewById(R.id.back);
        mSharefriend = (ImageView) findViewById(R.id.sharefriend);
        mTitleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
        webView = (WebView) findViewById(R.id.shareWebview);
        mBack.setOnClickListener(this);
        mSharefriend.setOnClickListener(this);

    }

    @Override
    public void initData() {
        String ShareUrl_H5 = Url.HOST+"h55/H5m/html/shareApp.html";
        Intent intent = getIntent();
        ucode = intent.getStringExtra("code");
//        ucode = intent.getExtras().getString("code");
//        ucode = getIntent().getStringExtra("m_invite_code");
        Log.e("ucode",ucode+"");
        WebSettings webSettings = webView.getSettings();
        //设置支持javaScript脚步语言
        webSettings.setJavaScriptEnabled(true);
        //支持双击-前提是页面要支持才显示
        webSettings.setUseWideViewPort(true);
        //支持缩放按钮-前提是页面要支持才显示
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);

        //设置客户端-不跳转到默认浏览器中
        webView.setWebViewClient(new WebViewClient());
        //加载网络资源

        webView.loadUrl(ShareUrl_H5);
//        webView.loadUrl("javascript:javaCallJs("+"'"+numebr+"'"+")");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:postCode(" + "'" + ucode + "'" + ")");
            }
        }, 800);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            finish();

        } else if (i == R.id.sharefriend) {
            showShare();

        }
    }

    /**
     * 分享好友
     */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        oks.addHiddenPlatform(WechatFavorite.NAME);
//        oks.addHiddenPlatform(QQ.NAME);
//        oks.addHiddenPlatform(SinaWeibo.NAME);
//        oks.addHiddenPlatform(QZone.NAME);


        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("猎模：让你的颜值更有价值");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        String shareurl = Url.HOST+"h55/H5-user/html/shareApp.html?code=" + ucode;
        oks.setTitleUrl(shareurl);
        // text是分享文本，所有平台都需要这个字段
        oks.setImageUrl(Url.LOGO_URL_REMOTE);
        oks.setText("一个高颜值模特打造的专属工作平台");
        oks.setExecuteUrl(shareurl);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(shareurl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(shareurl);

        // 启动分享GUI
        oks.show(this);
    }

}
