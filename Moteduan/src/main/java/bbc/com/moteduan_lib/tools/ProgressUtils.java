package bbc.com.moteduan_lib.tools;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public class ProgressUtils {

    public static boolean isAppForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = manager.getRunningAppProcesses();
        String packName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo info : list) {
            if (packName.equals(info.processName)) {
                boolean result = info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
                return result;
            }
        }
        return false;
    }

}
