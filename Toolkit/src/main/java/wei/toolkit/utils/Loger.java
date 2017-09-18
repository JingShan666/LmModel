package wei.toolkit.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class Loger {
    private static boolean isDebug = true;

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setIsDebug(boolean isDebug) {
        Loger.isDebug = isDebug;
    }

    public static void log(String content) {
        log("Loger", content);
    }

    public static void log(String tag, String content) {
        if (isDebug) {
            Log.e(tag, content);
        }
    }
}
