package bbc.com.moteduan_lib.im;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bbc.com.moteduan_lib.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubConversationListFragment extends FragmentActivity {
    private TextView mTitle;
    private RelativeLayout mBack;
    /**
     * 聚合类型
     */
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sub_conversation_list);

//        mTitle = (TextView) findViewById(R.id.txt1);
//        mBack = (RelativeLayout) findViewById(R.id.back);



//        getActionBarTitle();
    }

    /**
     * 通过 intent 中的数据，得到当前的 targetId 和 type
     *//*
    private void getActionBarTitle() {

        Intent intent = getIntent();

        type = intent.getData().getQueryParameter("type");

        if (type.equals("group")) {
            mTitle.setText("聚合群组");
        } else if (type.equals("private")) {
            mTitle.setText("聚合单聊");
        } else if (type.equals("discussion")) {
            mTitle.setText("聚合讨论组");
        } else if (type.equals("system")) {
            mTitle.setText("聚合系统会话");
        } else {
            mTitle.setText("聚合");
        }

    }*/
}