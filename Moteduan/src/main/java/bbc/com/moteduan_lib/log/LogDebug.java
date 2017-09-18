package bbc.com.moteduan_lib.log;

import android.app.Activity;
import android.util.Log;

import wei.toolkit.utils.Loger;

/**
 * Created by yx on 2016/3/29.
 */
public class LogDebug {

    private static boolean isDebug = true;
    private static final String TAG = "LM_LogDebug";

    private LogDebug() {
    }

    public static boolean getIsDebug() {
        return isDebug;
    }

    public static void setIsDebug(boolean debug) {
        isDebug = debug;
        Loger.setIsDebug(debug);
    }

    public static void print(String msg) {
        if (isDebug)
            System.out.println(msg);
    }

    public static void print(boolean msg) {
        if (isDebug)
            System.out.println(msg);
    }

    public static void print(Integer... msg) {
        if (isDebug) {
            for (Integer i : msg)
                System.out.println(i);
        }
    }

    public static void print(String... msg) {
        if (isDebug) {
            for (String i : msg)
                System.out.println(i);
        }
    }

    public static void print(float msg) {
        if (isDebug) {
//            for(float i:msg)
//            System.out.println(i);
            System.out.println(msg);
        }
    }

    public static void print(double... msg) {
        if (isDebug) {
            for (double i : msg)
                System.out.println(i);
        }
    }

    public void info(String activity, String msg) {
        if (isDebug)
            Log.i(activity, msg);
    }

    public void info(Activity activity, String msg) {
        if (isDebug)
            Log.i(activity.getClass().getSimpleName(), msg);
    }

    public static void err(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }


    public static void log(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

}
