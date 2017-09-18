package bbc.com.moteduan_lib.tools;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/8 0008.
 */
public class TextUtils {

    public static boolean isEmpty(@Nullable CharSequence str) {
        if (str == null || str.length() == 0 || "null".equals(str))
            return true;
        else
            return false;
    }


    /**
     设置字体
     @param con
     @param tv
     */
    public void setTextType(Context con, TextView tv){
//        程序中放入TTF字体文件，在程序中使用Typeface来设置字体：第一步，在assets目录下新建fonts目录，把TTF字体文件放到这里。第二步，程序中调用：
        AssetManager mgr = con.getAssets();//得到AssetManager
        Typeface tf= Typeface.createFromAsset(mgr, "fonts/mini.TTF");//根据路径得到Typeface
        tv.setTypeface(tf);//设置字体
    }


    /**
     * 改变文本中指定范围风格
     * @param textView
     * @param start
     * @param end
     * @param style
     */
    public static void spanTextStyle(TextView textView,int start,int end,int style){
        spanTextStyle(textView,start,end,style, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void spanTextStyle(TextView textView,int start,int end,int style,int flag){
        String text = textView.getText().toString().trim();
        if(TextUtils.isEmpty(text)){
            return;
        }
        SpannableString spannableString = SpannableString.valueOf(text);
        spannableString.setSpan(new TextAppearanceSpan(textView.getContext(),style),
                start,end,flag);

        textView.setText(spannableString,TextView.BufferType.SPANNABLE);
    }

}
