package bbc.com.moteduan_lib.tools;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by yx on 2016/4/12.
 */
public class ToastUtils {

    private static ToastUtils mToastUtils;
    private static Context context;
    private static Toast mToast;
    private ToastUtils(){ }

    public static ToastUtils getInstance(Context context){
        ToastUtils.context = context;
        if(mToastUtils == null){
            mToastUtils = new ToastUtils();
        }
        return mToastUtils;
    }
    public void showText(String msg){

        if(mToast == null){
            mToast = Toast.makeText(context,msg, Toast.LENGTH_SHORT);
        }else{
            mToast.setText(msg);
        }

        mToast.show();
    }

}
