package bbc.com.moteduan_lib.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.amap.api.services.geocoder.GeocodeSearch;
import com.mob.MobSDK;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xutils.x;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import bbc.com.moteduan_lib.constant.Constants;
import bbc.com.moteduan_lib.tools.ProgressUtils;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.ShareSDKR;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.location.AMapLocationActivity;


/**
 * Created by yx on 2016/3/29.
 */
public class App extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {
    private static App app;
    public static IWXAPI wxapi;
    public static boolean isAppForeground;
    private static List<Activity> mList = new LinkedList<>();
    public static final String ID = "f7afcf2fd8224aa28344d4365aa438b3";
    public static final String serviceId = "KEFU149163815211138";
    public static final String systemId = "7855eca77b1a4ec7a0e4d9054c347e6a";
    /*这个id是自定义的并没有在后台注册*/;
    public static final String orderPushId = "lm_order_push_id";
    /*21031543178d4d8294bd73f41912f6e0 融id*/
    public static final String rongId = "21031543178d4d8294bd73f41912f6e0";

    @Override
    public void onCreate() {
        super.onCreate();
//        LogDebug.setIsDebug(false);
        app = this;
        wxapi = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APP_ID, true);
        // 将应用的appid注册到微信
        wxapi.registerApp(Constants.WEIXIN_APP_ID);

        //初始化sdk   RegistrationId 1507bfd3f7ca1f3f6a4
        JPushInterface.setDebugMode(false);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);
        //建议添加tag标签，发送消息的之后就可以指定tag标签来发送了
        Set<String> set = new HashSet<>();
        set.add("members");//名字任意，可多添加几个,能区别就好了
        JPushInterface.setTags(this, set, null);//设置标签

        /**初始化融云
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) || "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            RongIM.init(this);
        }

        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {//com.bbc.lm
            //Xutils初始化
            x.Ext.init(this);
            x.Ext.setDebug(false); // 是否输出debug日志
            //更新服务端定位信息
        }
        registerActivityLifecycleCallbacks(this);
        MobSDK.init(this,"17be30b351d60","12ed37ca7236521c89c470e5417ffc39");

    }


    public static App getApp() {
        return app;
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
            mList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit(String... filterClassname) {
        if (filterClassname == null || filterClassname.length < 1) {
            exit();
        } else {
            for (int i = 0; i < mList.size(); i++) {
                Activity activity = mList.get(i);
                if (activity == null) continue;
                String aname = activity.getClass().getSimpleName();
                for (String s : filterClassname) {
                    if (aname.equals(s)) {
                        activity = null;
                        break;
                    }
                }
                if (activity != null) {
                    activity.finish();
                }

            }
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(final Activity activity) {
        isAppForeground = true;
        if (activity.getClass().getSimpleName().equals(AMapLocationActivity.class.getSimpleName())) {
            Class c = activity.getClass();
            Field[] fields = c.getDeclaredFields();

            Field field = null;
            try {
                field = c.getDeclaredField("mGeocodeSearch");
                field.setAccessible(true);
                GeocodeSearch geocodeSearch = (GeocodeSearch) field.get(activity);
                if (geocodeSearch != null) {
                    geocodeSearch.setOnGeocodeSearchListener(null);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        isAppForeground = ProgressUtils.isAppForeground(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }


}









