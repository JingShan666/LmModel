package bbc.com.moteduan_lib.tools;

/**
 * Created by yx on 2016/4/8.
 */

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import bbc.com.moteduan_lib.app.App;

/**
 * Created by MJJ on 2015/7/25.
 */
public class DeviceUtils {

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * (fontScale) + 0.5f);
    }

    /**
     * 获取两点之间的距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double distance(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    /**
     * 判断指定点是否在圆心之内
     *
     * @param x1     当前点x坐标
     * @param y1     当前点y坐标
     * @param x2     圆心x坐标
     * @param y2     圆心y坐标
     * @param radius 圆的半径
     * @return
     */
    public static boolean isInCircle(float x1, float y1, float x2, float y2, int radius) {
        return distance(x1, y1, x2, y2) < radius;
    }

    /**
     * 获取状态栏高度＋标题栏高度
     *
     * @param activity
     * @return
     */
    public static int getTopBarHeight(android.app.Activity activity) {
        return activity.getWindow().findViewById(android.view.Window.ID_ANDROID_CONTENT).getTop();
    }


    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备的密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 把dp转换为px
     */
    public static int dip2px(Context context, float px) {

        float scale = getScreenDensity(context);
        return (int) (px * scale + 0.5);
    }

    /**
     * 把px转换为dp
     */
    public static int px2dp(Context context, float px) {
        float scale = getScreenDensity(context);
        return (int) (px / scale + 0.5);
    }

    /**
     * 检查是否存在SDCard
     *
     * @return boolean
     */
    public static boolean hasSDcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\s*|\t|\r|\n");
            java.util.regex.Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 获取Android版本号
     */
    public static int getAndroidSDK() {
        // TODO Auto-generated method stub
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return version;

    }

    /**
     * 获取App版本号
     */
    public static String getVersion() {
        // 获取packagemanager的实例
        android.content.pm.PackageManager packageManager = App.getApp().getPackageManager();
        String version = "";
        try {
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            android.content.pm.PackageInfo packInfo = packageManager.getPackageInfo(App.getApp().getPackageName(), 0);
            version = packInfo.versionName;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 读取手机IMEI号
     *
     * @author Ben
     */
    public static String getIMEI(Context mContext) {
        if (mContext == null) {
            return "";
        }
        android.telephony.TelephonyManager telephonyMgr = (android.telephony.TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyMgr != null) {
            return telephonyMgr.getDeviceId();
        }
        return "";
    }

    /**
     * ROM版本、制造商、CPU型号、以及其他硬件信息
     *
     * @author Ben
     */
    public static String getDeviceInfoID() {
        // we make this look like a valid IMEI
        String m_szDevIDShort = "35"
                + android.os.Build.BOARD.length() % 10
                + android.os.Build.BRAND.length() % 10
                + android.os.Build.CPU_ABI.length() % 10
                + android.os.Build.DEVICE.length() % 10
                + android.os.Build.DISPLAY.length() % 10
                + android.os.Build.HOST.length() % 10
                + android.os.Build.ID.length() % 10
                + android.os.Build.MANUFACTURER.length() % 10
                + android.os.Build.MODEL.length() % 10
                + android.os.Build.PRODUCT.length() % 10
                + android.os.Build.TAGS.length() % 10
                + android.os.Build.TYPE.length() % 10
                + android.os.Build.USER.length() % 10;
        return m_szDevIDShort;
    }

    /**
     * AndroidID,这个ID会改变如果进行了出厂设置。并且，如果某个Andorid手机被Root过的话，这个ID也可以被任意改变。
     *
     * @author Ben
     */
    public static String getAndroidID(Context mContext) {
        return android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    }

    /**
     * Wifi MAC地址
     *
     * @author Ben
     */
    public static String getWifiMAC(Context mContext) {
        android.net.wifi.WifiManager wm = (android.net.wifi.WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (wm != null) {
            return wm.getConnectionInfo().getMacAddress();
        }
        return "";
    }

    /**
     * BT 地址
     *
     * @author Ben
     */
    public static String getBTMAC() {
        android.bluetooth.BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
        m_BluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if (m_BluetoothAdapter != null) {
            return m_BluetoothAdapter.getAddress();
        }
        return "";
    }

    /**
     * 通过md5加密，获取唯一标识
     *
     * @author Ben
     */
    public static String getOnlyID(Context mContext) {
        String m_szLongID = getIMEI(mContext)
                + getDeviceInfoID()
                + getAndroidID(mContext)
                + getWifiMAC(mContext)
                + getBTMAC();
        // compute md5
        java.security.MessageDigest mDigest = null;
        try {
            mDigest = java.security.MessageDigest.getInstance("MD5");
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        mDigest.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        // get md5 bytes
        byte p_md5Data[] = mDigest.digest();
        // create a hex string
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper
            // padding)
            if (b <= 0xF)
                m_szUniqueID += "0";
            // add number to string
            m_szUniqueID += Integer.toHexString(b);
        } // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase();
        return m_szUniqueID;
    }


}
