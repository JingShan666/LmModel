package bbc.com.moteduan_lib.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import bbc.com.moteduan_lib.R;


public class ProtocolActivity extends AppCompatActivity implements View.OnClickListener {

    private Button yiyuedu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);
        initView();
    }

    private void initView() {
        yiyuedu = (Button) findViewById(R.id.yiyuedu);

        yiyuedu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.yiyuedu) {
            finish();

        }
    }
}
