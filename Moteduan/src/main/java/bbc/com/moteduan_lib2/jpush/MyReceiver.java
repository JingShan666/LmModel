package bbc.com.moteduan_lib2.jpush;

/**
 * Created by zhang on 2016/12/26.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import bbc.com.moteduan_lib.app.App;
import bbc.com.moteduan_lib.bean.LoginBean;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib2.mineInvite.InviteDetailsDidNotSignUpActivity;
import bbc.com.moteduan_lib2.bean.InvitePushListBean;
import bbc.com.moteduan_lib2.mineInvite.ApplyOrderDetailsActivity;
import bbc.com.moteduan_lib2.mineInvite.AppointmentOrderDetailsActivity;
import bbc.com.moteduan_lib2.mineInvite.RealtimeOrderDetailsActivity;
import bbc.com.moteduan_lib2.mineNotive.NoticeDetailsActivity;
import bbc.com.moteduan_lib2.mineNotive.NoticeDetailsDidNotSignUpActivity;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;
import wei.toolkit.bean.EventBusMessages;

/**
 * 自定义接收器
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    // notificationType = DD_1:我的报名；DD_2:实时；DD_3:预约；BMX_1:用户发的报名详情
    private static String notificationType = "";
    private static String orderId = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        LoginBean loginBean = SpDataCache.getSelfInfo(context);
        if (loginBean == null) {
            Log.e(TAG, "用户未登录...");
            return;
        }
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            // 自定义消息
//            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
//            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String msg = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            boolean b = App.isAppForeground;
//            Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: \n title: " + title + "\n" + " content: " + content + "\n" + " msg: " + msg + "\n App是否处于前台: " + b);
//            String msgType = "";
//            try {
//                JSONObject jsonObject = new JSONObject(msg);
//                msgType = jsonObject.getString("type");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            if ("1".equals(msgType)) {
//                if (b) {
//                    return;
//                }
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//                builder.setContentTitle(title);
//                builder.setContentText(content);
//                builder.setAutoCancel(true);
//                builder.setSmallIcon(R.mipmap.appicon);
//                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.appicon));
//                Intent[] i = {new Intent(context, HomeActivity.class)};
//                PendingIntent pi = PendingIntent.getActivities(context, 1, i, 0);
//                builder.setContentIntent(pi);
//                builder.setDefaults(NotificationCompat.DEFAULT_ALL);
//                Notification notification = builder.build();
//                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                manager.notify(0, notification);
//            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            // 通知
            String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.e(TAG, "通知 " + json);
            if (!TextUtils.isEmpty(json)) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (!jsonObject.has("type")) return;
                    notificationType = jsonObject.getString("type");
                    if (TextUtils.isEmpty(notificationType)) return;
                    if ("DT_1".equals(notificationType)) {
                        // 发现点赞、送花、评论消息通知
                        EventBus.getDefault().post(new EventBusMessages.MomentsMsgNotification(json));
                    } else {
                        orderId = jsonObject.getString("id");
                        String msgContent = jsonObject.getString("alert");
                        String receiveTime = jsonObject.getString("time");
                        InvitePushListBean.ListBean model = new InvitePushListBean.ListBean();
                        model.setId(orderId);
                        model.setType(notificationType);
                        model.setAlert(msgContent);
                        model.setTime(receiveTime);
                        InvitePushListBean invitePushListBean = SpDataCache.getInvitePushList();
                        if (null == invitePushListBean) {
                            invitePushListBean = new InvitePushListBean();
                        }
                        invitePushListBean.getList().add(model);
                        SpDataCache.saveInvitePushListBean(invitePushListBean);
                        TextMessage textMessage = TextMessage.obtain(msgContent);
                        RongIM.getInstance().insertMessage(Conversation.ConversationType.SYSTEM, App.orderPushId, App.orderPushId, textMessage, null);
//                    Message message = Message.obtain(App.orderPushId, Conversation.ConversationType.PRIVATE,textMessage);
//                    RongIM.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
//                        @Override
//                        public void onAttached(Message message) {}
//                        @Override
//                        public void onSuccess(Message message) {}
//                        @Override
//                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {}
//                    });
                        // 在订单结束或取消的时候 删除与这个用户的聊天
//                    if (!TextUtils.isEmpty(orderId)) {
//                        RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, orderId, null);
//                    }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            // 打开通知
            Log.e(TAG, "打开通知 notificationType= " + notificationType + " context of packageName  = " + context.getPackageName());

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if ("DD_1".equals(notificationType)) {
                    // 我的报名详情页面
                    context.startActivity(new Intent(context, bbc.com.moteduan_lib2.home.HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    context.startActivity(new Intent(context, ApplyOrderDetailsActivity.class).putExtra("orderId", orderId).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else if ("DD_2".equals(notificationType)) {
                    // 实时详情页面
                    context.startActivity(new Intent(context, bbc.com.moteduan_lib2.home.HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    context.startActivity(new Intent(context, RealtimeOrderDetailsActivity.class).putExtra("orderId", orderId).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else if ("DD_3".equals(notificationType)) {
                    // 预约详情页面
                    context.startActivity(new Intent(context, bbc.com.moteduan_lib2.home.HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    context.startActivity(new Intent(context, AppointmentOrderDetailsActivity.class).putExtra("orderId", orderId).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else if ("DD_4".equals(notificationType)) {
                    // 通告详情页面
                    context.startActivity(new Intent(context, bbc.com.moteduan_lib2.home.HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    context.startActivity(new Intent(context, NoticeDetailsActivity.class).putExtra("orderId", orderId).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else if ("BMX_1".equals(notificationType)) {
                    //用户发单后的提醒，跳转用户发的订单页面
                    context.startActivity(new Intent(context, bbc.com.moteduan_lib2.home.HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    context.startActivity(new Intent(context, InviteDetailsDidNotSignUpActivity.class).putExtra("orderId", orderId).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else if ("BMX_2".equals(notificationType)) {
                    // 用户发单后的提醒，跳转用户发的通告页面
                    context.startActivity(new Intent(context, bbc.com.moteduan_lib2.home.HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    context.startActivity(new Intent(context, NoticeDetailsDidNotSignUpActivity.class).putExtra("orderId", orderId).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    notificationType = "";
                }
            } else {
                if ("DD_1".equals(notificationType)) {
                    // 我的报名详情页面
                    context.startActivity(new Intent(context, bbc.com.moteduan_lib2.home.HomeActivity.class));
                    context.startActivity(new Intent(context, ApplyOrderDetailsActivity.class).putExtra("orderId", orderId));
                } else if ("DD_2".equals(notificationType)) {
                    // 实时详情页面
                    context.startActivity(new Intent(context, bbc.com.moteduan_lib2.home.HomeActivity.class));
                    context.startActivity(new Intent(context, RealtimeOrderDetailsActivity.class).putExtra("orderId", orderId));
                } else if ("DD_3".equals(notificationType)) {
                    // 预约详情页面
                    context.startActivity(new Intent(context, bbc.com.moteduan_lib2.home.HomeActivity.class));
                    context.startActivity(new Intent(context, AppointmentOrderDetailsActivity.class).putExtra("orderId", orderId));
                } else if ("DD_4".equals(notificationType)) {
                    //通告详情页面
                    context.startActivity(new Intent(context, bbc.com.moteduan_lib2.home.HomeActivity.class));
                    context.startActivity(new Intent(context, NoticeDetailsActivity.class).putExtra("orderId", orderId));
                } else if ("BMX_1".equals(notificationType)) {
                    // 用户发单后的提醒，跳转用户发的订单页面
                    context.startActivity(new Intent(context, bbc.com.moteduan_lib2.home.HomeActivity.class));
                    context.startActivity(new Intent(context, InviteDetailsDidNotSignUpActivity.class).putExtra("orderId", orderId));
                } else if ("BMX_2".equals(notificationType)) {
                    //用户发单后的提醒， 跳转用户发的通告页面
                    context.startActivity(new Intent(context, bbc.com.moteduan_lib2.home.HomeActivity.class));
                    context.startActivity(new Intent(context, NoticeDetailsDidNotSignUpActivity.class).putExtra("orderId", orderId));
                } else {
                    notificationType = "";
                }
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            // 网络状态改变
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.e(TAG, "[MyReceiver] 链接改变" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.e(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }


}

