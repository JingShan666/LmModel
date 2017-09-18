package bbc.com.moteduan_lib.base;

import android.os.Bundle;

import bbc.com.moteduan_lib.tools.ToastUtils;


/**
 * Created by yx on 2016/3/29.
 */
public abstract class BaseTranslucentActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            //透明状态栏
//            getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//            getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
    }


}