package bbc.com.moteduan_lib2.home;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.app.App;
import bbc.com.moteduan_lib.im.CustomConversationListFragment;
import bbc.com.moteduan_lib.log.LogDebug;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2017/5/18 0018.
 * 消息
 */

public class ChatListFragment extends Fragment {
    private boolean initialized;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!initialized) {
                if (RongIM.getInstance() != null) {
                    initialized = RongIM.getInstance().getCurrentConnectionStatus() == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED;
                }
                initViews();
            } else {
                if (RongIM.getInstance() != null) {
                    if (RongIM.getInstance().getCurrentConnectionStatus() == RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED) {
                        initViews();
                    }
                }

            }
        }
    }

    private void initViews() {
        CustomConversationListFragment fragment = new CustomConversationListFragment();
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();
        fragment.setUri(uri);
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_chat_list_frame, fragment).commit();
    }
}
