package wei.toolkit.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class SoftInputUtils {

    /**
     * 显示,隐藏软键盘
     * @param context
     */
    public static void switchSoftInput(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 强制关闭软键盘
     * @param context
     */
    public static void closeSoftInput(Context context,View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);

    }
}
