package bbc.com.moteduan_lib.base;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.githang.statusbar.StatusBarCompat;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.app.App;
import bbc.com.moteduan_lib.tools.ToastUtils;


/**
 * Created by Administrator on 2016/6/1 0001.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    public int screenW;
    public int screenH;
    public ToastUtils toast;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarCompat.setStatusBarColor(this, R.color.black_theme,false);
//        StatusBarCompat.setFitsSystemWindows(getWindow(),true);
        App.getApp().addActivity(this);
        toast = ToastUtils.getInstance(this);
        DisplayMetrics displayMetrics = new android.util.DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenW = displayMetrics.widthPixels;
        screenH = displayMetrics.heightPixels;

    }

    public void initView() {
    }

    public void initData() {
    }

    public void initEvents() {
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    public final <T> T getView(int resId) {
        return (T) findViewById(resId);
    }


    public final <T> T getView(android.view.View view, int resId) {
        return (T) view.findViewById(resId);
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    public void showLoadDataDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    public void endLoadDataDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


}
