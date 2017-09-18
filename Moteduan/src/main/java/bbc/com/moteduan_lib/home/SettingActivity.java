package bbc.com.moteduan_lib.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.constant.Constants;
import bbc.com.moteduan_lib.dialog.DialogUtils;
import bbc.com.moteduan_lib.leftmenu.UpdateManager;
import bbc.com.moteduan_lib.leftmenu.activity.AboutUs;
import bbc.com.moteduan_lib.leftmenu.activity.AccountSecurity;
import bbc.com.moteduan_lib.leftmenu.activity.Feedback;
import bbc.com.moteduan_lib.leftmenu.holder.SetMenuHolder;
import bbc.com.moteduan_lib.log.LogDebug;

import com.liemo.shareresource.Url;

import bbc.com.moteduan_lib.tools.DataCleanManager;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;
import wei.toolkit.utils.AppUtils;

public class SettingActivity extends BaseActivity {
    private static final String TAG = "SettingActivity";
    private SetMenuHolder viewHolder;
    private ImageButton back;
    private RelativeLayout titleLayout;
    private TextView l22;
    private RelativeLayout l2;
    private TextView l44;
    private RelativeLayout l4;
    private TextView l55;
    private RelativeLayout l5;
    private TextView l66;
    private RelativeLayout l6;
    private TextView l67;
    private RelativeLayout l61;
    private Button cancellation;

    private TextView mContactUsTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initData();
        initEvent();
    }


    @Override
    public void initView() {
        mContactUsTv = getView(R.id.activity_setting_contact_us);
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
        l22 = (TextView) findViewById(R.id.l22);
        l2 = (RelativeLayout) findViewById(R.id.l2);
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it2 = new Intent(SettingActivity.this, AccountSecurity.class);
                startActivity(it2);
            }
        });

        //TODO 检查更新
        l44 = (TextView) findViewById(R.id.l44);
        l4 = (RelativeLayout) findViewById(R.id.l4);
        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificationUpdate();
            }
        });

        l55 = (TextView) findViewById(R.id.l55);
        l5 = (RelativeLayout) findViewById(R.id.l5);
        //反馈
        l5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, Feedback.class);
                startActivity(intent);
            }
        });
        l66 = (TextView) findViewById(R.id.l66);
        l6 = (RelativeLayout) findViewById(R.id.l6);
        l6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClearCache();
            }
        });
        l67 = (TextView) findViewById(R.id.l67);
        l61 = (RelativeLayout) findViewById(R.id.l61);
        l61.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, AboutUs.class);
                startActivity(intent);
            }
        });
        cancellation = (Button) findViewById(R.id.cancellation);

    }

    private void initEvent() {
        mContactUsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.callPhoneAlert(v.getContext(), Constants.PhoneConstants.CONTACT_US_PHONE);
            }
        });
    }

    /**
     * 版本更新
     */
    private void verificationUpdate() {
        Map<String, Object> map = new HashMap<>();
        Xutils.post(Url.update, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.log(TAG, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String tips = jsonObject.getString("tips");
                    if (!"200".equals(code)) {
                        toast.showText(tips);
                        return;
                    }
                    int versionCode = jsonObject.getInt("version_code");
                    int currentVersionCode = AppUtils.getVersionCode(SettingActivity.this);
                    if (versionCode > currentVersionCode) {
                        String url = jsonObject.getString("url");
                        if (TextUtils.isEmpty(url)) return;
                        String version = jsonObject.getString("version");
                        UpdateManager updateManager = new UpdateManager(SettingActivity.this);
                        updateManager.showNoticeDialog(version, "优化性能,修复已知BUG", url, false);
                    }else{
                        toast.showText("当前已是最新版本!");
                    }
                } catch (Exception e) {
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

    /**
     * 获取当前程序的版本号
     */
    private String getVersionName() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionName;
    }

    /**
     * 清除缓存
     */
    AlertDialog dialog1;

    private void openClearCache() {
        View view = LayoutInflater.from(this).inflate(R.layout.cancellation_window, null);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        title.setText("清除缓存，需要等待几分钟~");
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(view);
        dialog1 = builder.create();
        dialog1.show();
        Button exitAccount = (Button) view.findViewById(R.id.exitAccount);
        exitAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 清除缓存
                try {
                    String totalCacheSize = DataCleanManager.getTotalCacheSize(SettingActivity.this);
                    LogDebug.err(totalCacheSize);
                    toast.showText("已清除" + totalCacheSize + "~");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DataCleanManager.clearAllCache(SettingActivity.this);
                dialog1.dismiss();
            }
        });

        Button close = (Button) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
    }

    @Override
    public void initData() {


    }

    /**
     * 退出账户
     */
    AlertDialog dialog;

    public void openCancellationWindow(View v) {

        View view = LayoutInflater.from(this).inflate(R.layout.cancellation_window, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(view);

        dialog = builder.create();
        dialog.show();
        LogDebug.print("openCancellationWindow ");
        Button exitAccount = (Button) view.findViewById(R.id.exitAccount);
        exitAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                Glide.get(SettingActivity.this).clearMemory();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Glide.get(SettingActivity.this).clearDiskCache();
//                    }
//                }).start();
//                SpDataCache.clean();
//                App.getApp().exit();
//                if (RongIM.getInstance() != null) {
//                    RongIM.getInstance().logout();
//                }
//
//                /*将alias设置成空，这样退出账号后就不会收到推送消息了*/
//                Set<String> set = new HashSet<>();
//                set.add("");
//                JPushInterface.setTags(App.getApp(), set, null);
////                startActivity(new Intent(SettingActivity.this, bbc.com.moteduan_lib2.home.HomeActivity.class));
//                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                Intent intent = new Intent(Constants.ACTION_LOGOUT_SUCCESS);
                sendBroadcast(intent);
                finish();
            }
        });

        Button close = (Button) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void back(View view) {

        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


}
