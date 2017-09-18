package bbc.com.moteduan_lib.network.Xutils3;

/**
 * 网络相关工具类
 * Created by QingHua on 2014/12/3.
 */
public class NetUtils {
    private NetUtils() {
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(android.content.Context context) {

        android.net.ConnectivityManager connectivity = (android.net.ConnectivityManager) context
                .getSystemService(android.content.Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {
            android.net.NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (android.net.NetworkInfo.State.CONNECTED == info.getState()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(android.content.Context context) {
        android.net.ConnectivityManager cm = (android.net.ConnectivityManager) context
                .getSystemService(android.content.Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == android.net.ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(android.app.Activity activity) {
        android.content.Intent intent = new android.content.Intent("/");
        android.content.ComponentName cm = new android.content.ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }
}
