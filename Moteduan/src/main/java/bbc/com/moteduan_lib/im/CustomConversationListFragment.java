package bbc.com.moteduan_lib.im;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.app.App;
import bbc.com.moteduan_lib.log.LogDebug;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.fragment.IHistoryDataResultCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import wei.toolkit.utils.PictureUtil;

/**
 * Created by Administrator on 2017/6/15 0015.
 */

public class CustomConversationListFragment extends ConversationListFragment {
    public static final String TAG = "CustomConversationListFragment";


    @Override
    public void getConversationList(final Conversation.ConversationType[] conversationTypes, final IHistoryDataResultCallback<List<Conversation>> callback) {
        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (callback != null) {
                    if (conversations == null) conversations = new ArrayList<Conversation>();
                    Conversation service = null;
                    Conversation system = null;
                    Conversation orderPush = null;
                    for (Conversation conversation : conversations) {
                        LogDebug.log(TAG, conversation.getTargetId());
                        if (null == service && conversation.getTargetId().equals(App.serviceId)) {
                            service = conversation;
                        }
                        if (null == system && conversation.getTargetId().equals(App.systemId)) {
                            system = conversation;
                        }
                        if (null == orderPush && conversation.getTargetId().equals(App.orderPushId)) {
                            orderPush = conversation;
                        }
                    }

                    if (null == service) {
                        service = new Conversation();
                        service.setTargetId(App.serviceId);
                        service.setConversationType(Conversation.ConversationType.CUSTOMER_SERVICE);
                        service.setNotificationStatus(Conversation.ConversationNotificationStatus.NOTIFY);
                        conversations.add(service);
                    }

                    if (null == system) {
                        system = new Conversation();
                        system.setTargetId(App.systemId);
                        system.setConversationType(Conversation.ConversationType.SYSTEM);
                        system.setNotificationStatus(Conversation.ConversationNotificationStatus.NOTIFY);
                        conversations.add(system);
                    }

                    if (null == orderPush) {
                        orderPush = new Conversation();
                        orderPush.setTargetId(App.orderPushId);
                        orderPush.setConversationType(Conversation.ConversationType.SYSTEM);
                        orderPush.setNotificationStatus(Conversation.ConversationNotificationStatus.NOTIFY);
                        conversations.add(orderPush);
                    }
                    callback.onResult(conversations);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                if (callback != null) {
                    callback.onError();
                }
            }
        }, conversationTypes);
    }
}
