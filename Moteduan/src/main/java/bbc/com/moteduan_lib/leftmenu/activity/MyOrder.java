package bbc.com.moteduan_lib.leftmenu.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.githang.statusbar.StatusBarCompat;

import bbc.com.moteduan_lib.R;

import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.log.LogDebug;
import com.liemo.shareresource.Url;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class MyOrder extends AppCompatActivity implements View.OnClickListener {

    private ImageButton back;
    private RelativeLayout titleLayout;
    private WebView webView;
    private String webUrl = Url.HOST + "h55/H5m/html/myOrder.html";

    private String userId;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        webUrl = "http://192.168.1.132:8080/BBC/h55/H5m/html/myOrder.html";
        setContentView(R.layout.activity_my_order);
        StatusBarCompat.setStatusBarColor(this, R.color.black_theme, false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
    }

    private void initView() {
        back = (ImageButton) findViewById(R.id.back);
        titleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
        webView = (WebView) findViewById(R.id.myorder_webview);


//        webView = new WebView(this);
        WebSettings webSettings = webView.getSettings();
        //设置支持javaScript脚步语言
        webSettings.setJavaScriptEnabled(true);

        //支持双击-前提是页面要支持才显示
//        webSettings.setUseWideViewPort(true);

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


        //设置支持js调用java
        webView.addJavascriptInterface(new AndroidAndJSInterface(), "Android");
        //TODO 此处要改
//        userId ="2fbdd6b2860645cf9cad3d6e50447d5d";
        userId = SpDataCache.getSelfInfo(MyOrder.this).getData().getM_head_photo();


        Intent intent = getIntent();
        String from = intent.getStringExtra("from");
        if ("myReceiver".equals(from)) {
            final String inviteid = intent.getStringExtra("inviteid");
            String url = intent.getStringExtra("url");
            LogDebug.err("myorder_url = " + url + ";    ==inviteid=" + inviteid);
            webView.loadUrl(url);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogDebug.err("Runnable--myorder---------");
                    webView.loadUrl("javascript:postMsg(" + "'" + inviteid + "'" + ",'" + userId + "')");
                }
            }, 800);

        } else {
            webView.loadUrl(webUrl);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:postStr(" + "'" + userId + "'" + ")");
                }
            }, 800);
        }


        back.setOnClickListener(this);
    }


    class AndroidAndJSInterface {
        /**
         * 该方法将被js调用,结束当前页面
         */
        @JavascriptInterface
        public void doFinish() {
            finish();
        }

        /**
         * 拨打电话
         *
         * @param phone
         */
        @JavascriptInterface
        public void call(String phone) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            if (ActivityCompat.checkSelfPermission(MyOrder.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            MyOrder.this.startActivity(intent);
        }

        @JavascriptInterface
        public void chat(String chatId, String name) {
//                启动会话界面
            if (RongIM.getInstance() != null) {
                RongIM.getInstance().startPrivateChat(MyOrder.this, chatId, name);
                LogDebug.err("chat==" + "chatid:" + chatId + "name:" + name);
            }

        }

        /**
         * 移除对应的会话
         *
         * @param chatId
         */
        @JavascriptInterface
        public void removeChat(String chatId) {
            RongIM rongIM = RongIM.getInstance();
            if (rongIM != null) {
                rongIM.removeConversation(Conversation.ConversationType.PRIVATE, chatId, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            finish();

        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            if (webView.getUrl().equals(webUrl)) {
                super.onBackPressed();
            } else {
                webView.goBack();
                webView.loadUrl(webUrl);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:postStr(" + "'" + userId + "'" + ")");
                    }
                }, 500);
            }
        } else {
            super.onBackPressed();
        }
    }

}
