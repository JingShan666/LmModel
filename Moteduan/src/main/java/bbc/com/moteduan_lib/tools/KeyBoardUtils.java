package bbc.com.moteduan_lib.tools;

/**
 * Created by zhangming on 2016/9/22 0022.
 */


import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

//打开或关闭软键盘
public class KeyBoardUtils
{
    /**
     * 打卡软键盘
     *
     * @param mContext
     * @param mEditText
     */
    public static void openKeybord(View mEditText, Context mContext)
    {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText
     * @param mContext
     */
    public static void closeKeybord(View mEditText, Context mContext)
    {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
