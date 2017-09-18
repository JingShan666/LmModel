package wei.toolkit.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

public class ToastUtil {
    private static Toast toast;

    public static void show(Context context, String msg) {
        show(context, msg, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String msg, int duration) {
        if (context == null) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, msg, duration);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
