package bbc.com.moteduan_lib.home;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.githang.statusbar.StatusBarCompat;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.app.App;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.dialog.DialogUtils;
import bbc.com.moteduan_lib.localvideo.LocalVideoActivity;
import bbc.com.moteduan_lib.activity.PictureChoseActivity2;
import bbc.com.moteduan_lib.log.LogDebug;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.renzheng.Auth;
import bbc.com.moteduan_lib.renzheng.ShipinRenzhengActivity;
import bbc.com.moteduan_lib.tools.PermissionHelper;
import bbc.com.moteduan_lib.tools.ToastUtils;


public class DetailActivity extends BaseActivity {
    private WebView webView;
    private String userID;

    public static final String url = Url.HOST + "h55/H5m/html/modalDetail.html";
//    public static final String url = "http://192.168.1.105:8080/h5/html/modalDetail.html";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private String me;
    private PermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        StatusBarCompat.setStatusBarColor(this, R.color.black_theme, false);
//        StatusBarUtils.compat(this,R.color.black_theme);
        mPermissionHelper = new PermissionHelper(this);
        initView();
        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPermissionHelper.onResume(); // 当界面一定通过权限才能继续，就要加上这行
        initData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void initView() {
        webView = (WebView) findViewById(R.id.info_wv);
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

        //设置支持js调用java
        webView.addJavascriptInterface(new AndroidAndJSInterface(), "Android");


    }

    @Override
    public void initData() {
        userID = SpDataCache.getSelfInfo(DetailActivity.this).getData().getM_head_photo();
        String from = getIntent().getStringExtra("from");
        if ("home".equals(from)) {
            final String userid = getIntent().getStringExtra("userid");
            me = 0 + "";
            webView.loadUrl(url);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:postStr(" + "'" + userid + "'" + "," + "'" + me + "'" + ")");
                }
            }, 1000);
        } else {
            me = 1 + "";
            webView.loadUrl(url);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:postStr(" + "'" + userID + "'" + "," + "'" + me + "'" + ")");
                }
            }, 1000);
        }


    }

    //    http://192.168.1.105:8080/h5/html/modalDetail.html
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            if (webView.getUrl().equals(url)) {
                super.onBackPressed();
            } else {
                webView.goBack();
            }
        } else {
            super.onBackPressed();
        }
    }

    class AndroidAndJSInterface {
        /**
         * 该方法将被js调用,结束当前页面
         */
        @JavascriptInterface
        public void doFinish() {
            finish();
        }


        @JavascriptInterface
        public void bainJizl() {
            LogDebug.err("bainJizl---");
            Intent intent1 = new Intent(DetailActivity.this, EditActivity.class);
            startActivity(intent1);
            finish();
        }


        @JavascriptInterface
        public void uploadVedio() {
            DialogUtils.videoUploadSelectDialog(DetailActivity.this, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            startActivity(new Intent(DetailActivity.this, LocalVideoActivity.class));
                            break;
                        case 1:
                            mPermissionHelper.checkPermisson(new PermissionHelper.OnPermissionListener() {
                                @Override
                                public void onAgreePermission() {
                                    toRecord();
                                }

                                @Override
                                public void onDeniedPermission() {
                                    ToastUtils.getInstance(App.getApp().getBaseContext()).showText("拒绝了调用摄像头的权限");
                                    return;
                                }

                            }, Manifest.permission.CAMERA);
                            break;
                    }
                }
            });
        }

        @JavascriptInterface
        public void modelRZ() {
            LogDebug.err("modelRZ---");
            Intent intent3 = new Intent(DetailActivity.this, Auth.class);
            intent3.putExtra("from", "Authentication");
            intent3.putExtra("type", Auth.MODEL_FLAG);
            startActivity(intent3);
        }

        @JavascriptInterface
        public void uploadImage() {
            Intent intent = new Intent(DetailActivity.this, PictureChoseActivity2.class);
            intent.putExtra("XXZ", true);
            startActivity(intent);
        }
    }

    private void toRecord() {
        Intent intent2 = new Intent(DetailActivity.this, ShipinRenzhengActivity.class);
        startActivity(intent2);

    }

}
