package bbc.com.moteduan_lib2;

import android.content.Intent;
import android.os.Bundle;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib2.home.HomeActivity;

/**
 * Created by Administrator on 2017/5/25 0025.
 */

public class LaunchActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        startActivity(new Intent(this,HomeActivity.class));
        finish();

    }

    @Override
    public void initView() {
    }
    @Override
    public void initData() {

    }
}
