package wei.toolkit;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017/6/12 0012.
 */

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

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
