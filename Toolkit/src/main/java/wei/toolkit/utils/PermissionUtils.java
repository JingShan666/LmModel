package wei.toolkit.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class PermissionUtils {
    private final static String TAG = "PermissionUtils";
    private static Map<String, Object> mPermisstionNameMap;

    public interface PermissionCallback {
        void onGranted();

        void onDenied(String[] deniedName);
    }

    public static void checkPermission(Context context, String[] perName, PermissionCallback permissionCallback) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (permissionCallback != null) {
                permissionCallback.onGranted();
            }
            return;
        }

        if (perName == null || perName.length < 1) {
            if (permissionCallback != null) {
                permissionCallback.onGranted();
            }
            return;
        }

        List<String> filter = new ArrayList<>();
        for (int i = 0; i < perName.length; i++) {
            int result = PermissionChecker.checkSelfPermission(context, perName[i]);
            Loger.log(TAG, perName[i] + " result = " + result + " SDK_INT >= 23");
            if (result != PermissionChecker.PERMISSION_GRANTED) {
                filter.add(perName[i]);
                Loger.log(TAG, perName[i]);
            }
        }
        if (filter.size() > 0) {
            String[] s = filter.toArray(new String[filter.size()]);
            if (permissionCallback != null) {
                permissionCallback.onDenied(s);
            }
        } else {
            if (permissionCallback != null) {
                permissionCallback.onGranted();
            }
        }
    }


    public static void showDeniedPermissionDialog(final Context context, String[] deniedPermission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        TextView textView = new TextView(context);
        textView.setPadding(16, 0, 16, 0);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < deniedPermission.length; i++) {
            stringBuilder.append(getPermissionName(deniedPermission[i]));
            if (i != deniedPermission.length - 1) {
                stringBuilder.append("\n");
            }
        }
        textView.setText(stringBuilder.toString());
        builder.setView(textView);
        builder.setTitle("您有以下权限未打开");
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                context.startActivity(getAppDetailSettingIntent(context));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return localIntent;
    }

    public static String getPermissionName(String permission) {
        if (mPermisstionNameMap == null) {
            mPermisstionNameMap = new HashMap<>();
            mPermisstionNameMap.put("android.permission.RECORD_AUDIO", "录音");
            mPermisstionNameMap.put("android.permission.CAMERA", "相机");
            mPermisstionNameMap.put("android.permission.ACCESS_COARSE_LOCATION", "网络定位");
        }
        return (String) mPermisstionNameMap.get(permission);
    }

}
