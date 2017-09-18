package wei.moments.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import wei.toolkit.utils.Loger;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showLoadDataDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(BaseActivity.this);
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
