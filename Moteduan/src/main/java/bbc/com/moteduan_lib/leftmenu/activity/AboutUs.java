package bbc.com.moteduan_lib.leftmenu.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import bbc.com.moteduan_lib.R;

public class AboutUs extends AppCompatActivity implements View.OnClickListener {

    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initView();
    }

    private void initView() {
        back = (ImageButton) findViewById(R.id.back);

        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            finish();

        }
    }
}
