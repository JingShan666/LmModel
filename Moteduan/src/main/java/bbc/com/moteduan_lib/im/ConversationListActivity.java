package bbc.com.moteduan_lib.im;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.log.LogDebug;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;


public class ConversationListActivity extends Fragment {
    private View v;


    public ConversationListActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.conversationlist,null);
        //这段话不能少，静态集成，否则会话列表不会显示内容
        ConversationListFragment fragment = (ConversationListFragment) getChildFragmentManager()
                .findFragmentById(R.id.conversationlist);
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();

        fragment.setUri(uri);
        LogDebug.err("ConversationListActivity-------------");
        return v;
    }


}
