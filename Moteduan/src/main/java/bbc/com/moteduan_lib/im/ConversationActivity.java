package bbc.com.moteduan_lib.im;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import bbc.com.moteduan_lib.R;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

public class ConversationActivity extends FragmentActivity {

    private TextView mTitle;
    private ImageView mBack;
    private String mTargetId;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        initActionBar();
        Intent intent = getIntent();
        mTargetId = intent.getData().getQueryParameter("targetId");
        title = getIntent().getData().getQueryParameter("title");
        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
        enterFragment(mConversationType, mTargetId);
        setActionBarTitle(title);
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType
     * @param mTargetId
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {
        ConversationFragment fragment = new ConversationFragment();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();
        fragment.setUri(uri);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
    }

    /**
     * 设置 actionbar 事件
     */
    private void initActionBar() {
        mTitle = (TextView) findViewById(R.id.title);
        mBack = (ImageView) findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 设置 actionbar title
     */
    private void setActionBarTitle(String targetid) {
        mTitle.setText(targetid);
    }


}