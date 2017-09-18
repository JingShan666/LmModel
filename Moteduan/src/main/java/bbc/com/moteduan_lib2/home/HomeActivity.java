package bbc.com.moteduan_lib2.home;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.liemo.shareresource.Url;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.ReleaseDate.ReleaseSkill;
import bbc.com.moteduan_lib.app.App;
import bbc.com.moteduan_lib.base.BaseTranslucentActivity;
import bbc.com.moteduan_lib.bean.LoginBean;
import bbc.com.moteduan_lib.constant.Constants;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.dialog.DialogUtils;
import bbc.com.moteduan_lib.home.DetailActivity;
import bbc.com.moteduan_lib.leftmenu.UpdateManager;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.maps.IMap;
import bbc.com.moteduan_lib.maps.LmLocation;
import bbc.com.moteduan_lib.maps.LmLocationListener;
import bbc.com.moteduan_lib.maps.LmMap;
import bbc.com.moteduan_lib.maps.UploadGPS;
import bbc.com.moteduan_lib.network.Req;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;
import bbc.com.moteduan_lib.renzheng.Auth;
import bbc.com.moteduan_lib.tools.ToastUtils;
import bbc.com.moteduan_lib2.InvitePushListActivity;
import bbc.com.moteduan_lib2.login.LoginActivity;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import wei.moments.MomentsFragment;
import wei.toolkit.bean.EventBusMessages;
import wei.toolkit.utils.AppUtils;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.PermissionUtils;
import wei.toolkit.utils.ToastUtil;
import wei.toolkit.widget.VViewPager;

/**
 * Created by Administrator on 2017/5/18 0018.
 */

public class HomeActivity extends BaseTranslucentActivity {
    private static String TAG = "HomeActivity";
    private VViewPager mVp;
    private List<Fragment> mFragments;
    private Tab mTab;
    private RelativeLayout mInvite;
    private RelativeLayout mDiscover;
    private RelativeLayout mMessage;
    private RelativeLayout mMine;
    private TextView mInviteText;
    private TextView mDiscoverText;
    private TextView mMessageText;
    private TextView mMineText;
    private ImageView mPublish;
    private long mCurrentTime;
    private IMap mIMap;
    private LoginOrLogoutNotificationReceiver loginOrLogoutNotificationReceiver;
    // 缓存聊天用户
    private Map<String, UserInfo> mCacheChatId = new HashMap();
    private TextView mUnreadMsgTv;
    private static final int REQUEST_CODE_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_lib2);
        initView();
        initRongConnect();
        initEvents();
        loginOrLogoutNotificationReceiver = new LoginOrLogoutNotificationReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_LOGIN_SUCCESS);
        intentFilter.addAction(Constants.ACTION_LOGOUT_SUCCESS);
        registerReceiver(loginOrLogoutNotificationReceiver, intentFilter);

        RongIM.setConnectionStatusListener(new MyConnectionStatusListener());
        RongIM.setConversationListBehaviorListener(conversationListBehaviorListener);
        RongIM.setConversationBehaviorListener(conversationBehaviorListener);
        initRongUnreadMessage();
        /*给融云提供用户信息*/
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {
                UserInfo userInfo = mCacheChatId.get(s);
                if (null != userInfo) {
                    return userInfo;
                }
                if (s.equals(SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_id())) {
                    userInfo = new UserInfo(s, SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_nick_name(), Uri.parse(SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_head_photo()));
                    mCacheChatId.put(s, userInfo);
                    return userInfo;
                }
                return findUserInfo(s);
            }
        }, false);


        verificationUpdate();
    }

    @Override
    public void initView() {
        mPublish = (ImageView) findViewById(R.id.activity_home_lib2_publish);
        mInvite = (RelativeLayout) findViewById(R.id.activity_home_lib2_invite);
        mDiscover = (RelativeLayout) findViewById(R.id.activity_home_lib2_discover);
        mMessage = (RelativeLayout) findViewById(R.id.activity_home_lib2_message);
        mMine = (RelativeLayout) findViewById(R.id.activity_home_lib2_mine);
        mInviteText = (TextView) findViewById(R.id.activity_home_lib2_invite_text);
        mDiscoverText = (TextView) findViewById(R.id.activity_home_lib2_discover_text);
        mMessageText = (TextView) findViewById(R.id.activity_home_lib2_message_text);
        mMineText = (TextView) findViewById(R.id.activity_home_lib2_mine_text);
        mUnreadMsgTv = (TextView) findViewById(R.id.activity_home_lib2_unread_tv);

        mFragments = new ArrayList<>();
        mFragments.add(new InvitePageFragment());
        mFragments.add(new MomentsFragment());
        mFragments.add(new ChatListFragment());
        mFragments.add(new MineFragment());
        mVp = (VViewPager) findViewById(R.id.activity_home_lib2_vp);
        mVp.setOffscreenPageLimit(mFragments.size());
        mVp.setAdapter(new VpAdapter(getSupportFragmentManager()));
        tabSelectedposition(0);

        PermissionUtils.checkPermission(HomeActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionUtils.PermissionCallback() {
            @Override
            public void onGranted() {
                requestLocation();
            }

            @Override
            public void onDenied(String[] deniedName) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(deniedName, REQUEST_CODE_PERMISSION);
                }
            }
        });
        upGps();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(HomeActivity.this,
                            "您拒绝了" + PermissionUtils.getPermissionName(permissions[i]) + "权限,导致功能无法正常使用", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            requestLocation();
        }
    }

    public void initEvents() {
        mInvite.setOnClickListener(inviteClick);
        mDiscover.setOnClickListener(discoverClick);
        mMessage.setOnClickListener(messageClick);
        mMine.setOnClickListener(mineClick);
        mPublish.setOnClickListener(publishClick);
    }

    @Override
    public void initData() {
    }


    private void requestLocation() {
        mIMap = LmMap.get();
        mIMap.setLocationListener(new LmLocationListener() {
            @Override
            public void onLocationChange(final LmLocation location) {
                mIMap.stopLocation();
                final String city = location.getCity();
                final String address = location.getCity() + location.getStreet() + location.getStreetNumber();
                SpDataCache.saveGpsAddress(HomeActivity.this, city, address, location.getLatitude(), location.getLongitude());
                upGps();
                Loger.log(TAG, address);
                if (!TextUtils.equals(city, SpDataCache.getCity())) {
                    DialogUtils.commonAlert(HomeActivity.this, "", "当前定位到 " + city + " 是否切换?", "切换", "", "取消", new Handler.Callback() {
                        @Override
                        public boolean handleMessage(android.os.Message msg) {
                            if (msg == null) return false;
                            if (msg.what == 0) {
                                SpDataCache.saveAddress(HomeActivity.this, city, address, location.getLatitude(), location.getLongitude());
                                /*to InviteFragment.java*/
                                EventBus.getDefault().post(new EventBusMessages.GpsChangeNotification(city, location.getLatitude(), location.getLongitude()));
                            }
                            return false;
                        }
                    });
                } else {
                    SpDataCache.saveAddress(HomeActivity.this, city, address, location.getLatitude(), location.getLongitude());
                }

            }
        });
        mIMap.startLocation();
    }

    private void upGps() {
        LoginBean loginBean = SpDataCache.getSelfInfo(App.getApp());
        if (loginBean != null) {
            UploadGPS.request(loginBean.getData().getM_id(), SpDataCache.getGpsLatitude(), SpDataCache.getGpsLongitude(), SpDataCache.getGpsCity(), null);
        }
    }

    private void tabSelectedposition(int position) {
        switch (position) {
            case 0:
                if (!mInviteText.isSelected()) {
                    recoverTab();
                    mInviteText.setSelected(true);
                    mTab = new Tab();
                    mTab.tab = mInviteText;
                    mVp.setCurrentItem(position, false);
                }
                break;
            case 1:
                if (!mDiscoverText.isSelected()) {
                    recoverTab();
                    mDiscoverText.setSelected(true);
                    mTab = new Tab();
                    mTab.tab = mDiscoverText;
                    mVp.setCurrentItem(position, false);
                }
                break;
            case 2:
                if (!mMessageText.isSelected()) {
                    recoverTab();
                    mMessageText.setSelected(true);
                    mTab = new Tab();
                    mTab.tab = mMessageText;
                    mVp.setCurrentItem(position, false);
                }
                break;
            case 3:
                if (!mMineText.isSelected()) {
                    recoverTab();
                    mMineText.setSelected(true);
                    mTab = new Tab();
                    mTab.tab = mMineText;
                    mVp.setCurrentItem(position, false);
                }
                break;
            default:
                break;
        }
    }

        private void recoverTab() {
        if (mTab != null) {
            mTab.tab.setSelected(false);
            mTab = null;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tabSelectedposition(0);
    }

    /*登录、退出登录*/
    public static class LoginOrLogoutNotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent == null) return;
            Loger.log(TAG, "LoginOrLogoutNotificationReceiver");
            if (intent.getAction() == Constants.ACTION_LOGIN_SUCCESS) {
                Loger.log(TAG, "onReceive Login");
                /*登录*/
                if (JPushInterface.isPushStopped(App.getApp())) {
                    JPushInterface.resumePush(App.getApp());
                }
                requestRegisterId();
                initRongConnect();
            } else if (intent.getAction() == Constants.ACTION_LOGOUT_SUCCESS) {
                Loger.log(TAG, "onReceive Logout");
                /*退出登录*/
                Map<String, Object> map = new HashMap<>();
                map.put("member_id", SpDataCache.getSelfInfo(App.getApp()).getData().getM_id());
                Req.post(Url.signOut, map, new Req.ReqCallback() {
                    @Override
                    public void success(String result) {
                        Loger.log(TAG, result);
                    }

                    @Override
                    public void failed(String msg) {
                    }

                    @Override
                    public void completed() {
                    }
                });
                Glide.get(context).clearMemory();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
                SpDataCache.clean();
                App.getApp().exit(HomeActivity.class.getSimpleName());
                if (RongIM.getInstance() != null) {
//                    RongIM.getInstance().clearConversations(
//                            new RongIMClient.ResultCallback() {
//                                @Override
//                                public void onSuccess(Object o) {}
//                                @Override
//                                public void onError(RongIMClient.ErrorCode errorCode) {}
//                                                            }
//                            , Conversation.ConversationType.DISCUSSION
//                            , Conversation.ConversationType.PRIVATE
//                            , Conversation.ConversationType.CHATROOM
//                            , Conversation.ConversationType.GROUP
//                            , Conversation.ConversationType.APP_PUBLIC_SERVICE
//                            , Conversation.ConversationType.PUBLIC_SERVICE
//                            , Conversation.ConversationType.PUSH_SERVICE
//                    );
                    RongIM.getInstance().logout();
                }

                if (!JPushInterface.isPushStopped(App.getApp())) {
                    JPushInterface.stopPush(App.getApp());
                }

                context.startActivity(new Intent(context, HomeActivity.class));
                context.startActivity(new Intent(context, LoginActivity.class));
            }

        }
    }

    /**
     * 初始化融云连接
     */
    private static void initRongConnect() {
        LoginBean loginBean = SpDataCache.getSelfInfo(App.getApp());
        if (null != loginBean) {
            connect(loginBean.getData().getM_rong_token());
        }
    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 { #init(Context)} 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     *              callback 连接回调。
     * @return RongIM  客户端核心类的实例。
     */
    private static void connect(String token) {
        if (App.getApp().getApplicationInfo().packageName.equals(App.getCurProcessName(App.getApp()))) {
            Loger.log(TAG, "进入链接融云：" + token);
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    Loger.log(TAG, "token获取失败");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    Loger.log(TAG, "融云链接成功" + userid);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Loger.log(TAG, "融云链接失败" + errorCode);
                }
            });
        }
    }

    private UserInfo findUserInfo(final String id) {
        Map map = new HashMap();
        map.put("user_id", id);
        Xutils.post(Url.getUserInfo, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.log(TAG, id + " " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (!TextUtils.equals("200", code)) {
                        return;
                    }
                    JSONObject object = jsonObject.getJSONObject("user");
                    String id = object.getString("u_id");
                    String nickName = object.getString("u_nick_name");
                    String headPhoto = object.getString("u_head_photo");
                    UserInfo info = new UserInfo(id, nickName, Uri.parse(headPhoto));
                    mCacheChatId.put(id, info);
                    RongIM.getInstance().refreshUserInfoCache(info);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

        return null;
    }

    private View.OnClickListener inviteClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tabSelectedposition(0);
        }
    };
    private View.OnClickListener discoverClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tabSelectedposition(1);
        }
    };
    private View.OnClickListener messageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LoginBean loginBean = SpDataCache.getSelfInfo(HomeActivity.this);
            if (null == loginBean) {
                ToastUtil.show(HomeActivity.this, "您还未登录，请先登录");
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                return;
            }
            tabSelectedposition(2);
        }
    };
    private View.OnClickListener mineClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LoginBean loginBean = SpDataCache.getSelfInfo(HomeActivity.this);
            if (null == loginBean) {
                ToastUtil.show(HomeActivity.this, "您还未登录，请先登录");
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                return;
            }
            tabSelectedposition(3);
        }
    };


    private View.OnClickListener publishClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            LoginBean loginBean = SpDataCache.getSelfInfo(HomeActivity.this);
            if (null == loginBean) {
                ToastUtil.show(HomeActivity.this, "您还未登录，请先登录");
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("member_id", SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_id());
            Req.post(Url.getModelAuthState, map, new Req.ReqCallback() {
                @Override
                public void success(String result) {
                    LogDebug.log(TAG, result);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String tips = jsonObject.getString("tips");
                        if (!"200".equals(code)) {
//                            startActivity(new Intent(HomeActivity.this, AuthenticationActivity.class));
                            toast.showText(tips);
                            return;
                        }
                        Intent intent = new Intent(HomeActivity.this, ReleaseSkill.class);
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        toast.showText(e.getMessage());
                        //toast.showText("网络不可用，请检查您的网络！");
                    }
                }

                @Override
                public void failed(String msg) {
                    //toast.showText(msg+"99");
                    toast.showText("网络连接不可用，请稍后重试");
                }

                @Override
                public void completed() {
                }
            });

        }
    };

    private class VpAdapter extends FragmentPagerAdapter {

        public VpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    private class Tab {
        public TextView tab;
    }

    public void showDialogRz(String errcode, String errmsg) {
        View view = LayoutInflater.from(this).inflate(R.layout.cancellation_window, null);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        Button exitAccount = (Button) view.findViewById(R.id.exitAccount);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(view);
        final AlertDialog dialog = builder.create();
        if ("203".equals(errcode)) {
            title.setText(errmsg);
            exitAccount.setText("去认证");
            exitAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent3 = new Intent(HomeActivity.this, Auth.class);
                    intent3.putExtra("from", "Authentication");
                    intent3.putExtra("type", Auth.MODEL_FLAG);
                    startActivity(intent3);
                    dialog.dismiss();
                }
            });

        } else if ("205".equals(errcode)) {
            title.setText(errmsg);
            exitAccount.setText("重新认证");
            exitAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent3 = new Intent(HomeActivity.this, Auth.class);
                    intent3.putExtra("from", "Authentication");
                    intent3.putExtra("type", Auth.MODEL_FLAG);
                    startActivity(intent3);
                    dialog.dismiss();
                }
            });
        } else if ("206".equals(errcode)) {
            title.setText(errmsg);
            exitAccount.setText("去认证");
            exitAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent3 = new Intent(HomeActivity.this, DetailActivity.class);
                    startActivity(intent3);
                    dialog.dismiss();

                }
            });
        } else if ("208".equals(errcode)) {
            title.setText(errmsg);
            exitAccount.setText("重新认证");
            exitAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //认证模特
                    Intent intent3 = new Intent(HomeActivity.this, DetailActivity.class);
                    startActivity(intent3);
                    dialog.dismiss();

                }
            });
        } else if ("209".equals(errcode)) {
            DialogUtils.phoneBindingAlert(HomeActivity.this);
            return;
        } else {
            ToastUtils.getInstance(HomeActivity.this).showText(errmsg);
            return;
        }

        dialog.show();
        Button close = (Button) view.findViewById(R.id.close);
        close.setText("取消");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        if ((System.currentTimeMillis() - mCurrentTime) > 2000) {
//            mCurrentTime = System.currentTimeMillis();
//            toast.showText("在按一次退出程序");
//        } else {
//            super.onBackPressed();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogDebug.log(TAG, "onDestroy");
        if (null != loginOrLogoutNotificationReceiver) {
            unregisterReceiver(loginOrLogoutNotificationReceiver);
        }
    }

    /*这个链接状态回调是在子线程中执行的。*/
    private class MyConnectionStatusListener implements RongIMClient.ConnectionStatusListener {
        @Override
        public void onChanged(ConnectionStatus connectionStatus) {
            LogDebug.log(TAG, connectionStatus.getMessage());
            switch (connectionStatus) {

                case CONNECTED://连接成功。

                    break;
                case DISCONNECTED://断开连接。

                    break;
                case CONNECTING://连接中。

                    break;
                case NETWORK_UNAVAILABLE://网络不可用。

                    break;
                case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                    Intent intent = new Intent(Constants.ACTION_LOGOUT_SUCCESS);
                    sendBroadcast(intent);
                    Toast.makeText(HomeActivity.this, "此账户已在别处登录,您已经被踢下线.", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    }

    RongIM.ConversationListBehaviorListener conversationListBehaviorListener = new RongIM.ConversationListBehaviorListener() {
        @Override
        public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
            return true;
        }

        @Override
        public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
            return true;
        }

        @Override
        public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
            return false;
        }

        @Override
        public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
            if (uiConversation.getConversationTargetId().equals(App.orderPushId)) {
                RongIM.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.SYSTEM, App.orderPushId, null);
                startActivity(new Intent(HomeActivity.this, InvitePushListActivity.class));
                return true;
            }
            return false;
        }
    };

    RongIM.ConversationBehaviorListener conversationBehaviorListener = new RongIM.ConversationBehaviorListener() {
        @Override
        public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
            return false;
        }

        @Override
        public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
            return false;
        }

        @Override
        public boolean onMessageClick(Context context, View view, Message message) {

            return false;
        }

        @Override
        public boolean onMessageLinkClick(Context context, String s) {
            return false;
        }

        @Override
        public boolean onMessageLongClick(Context context, View view, Message message) {
            return false;
        }
    };

    /**
     * 获取融云未读消息的个数
     */
    private void initRongUnreadMessage() {
        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.DISCUSSION,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE,
                Conversation.ConversationType.APP_PUBLIC_SERVICE,
                Conversation.ConversationType.CUSTOMER_SERVICE};
        RongIM.getInstance().addUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int count) {
                if (count < 1) {
                    if (mUnreadMsgTv.getVisibility() != View.GONE) {
                        mUnreadMsgTv.setVisibility(View.GONE);
                    }
                } else if (count > 0 && count < 100) {
                    if (mUnreadMsgTv.getVisibility() != View.VISIBLE) {
                        mUnreadMsgTv.setVisibility(View.VISIBLE);
                    }
                    mUnreadMsgTv.setText(count + "");
                } else {
                    if (mUnreadMsgTv.getVisibility() != View.VISIBLE) {
                        mUnreadMsgTv.setVisibility(View.VISIBLE);
                    }
                    mUnreadMsgTv.setText("...");
                }
            }
        }, conversationTypes);
    }


    /**
     * 版本更新
     */
    private void verificationUpdate() {
        Map<String, Object> map = new HashMap<>();
        Xutils.post(Url.update, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.log(TAG, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (!"200".equals(code)) {
                        return;
                    }
                    int versionCode = jsonObject.getInt("version_code");
                    int currentVersionCode = AppUtils.getVersionCode(HomeActivity.this);
                    if (versionCode > currentVersionCode) {
                        String url = jsonObject.getString("url");
                        if (TextUtils.isEmpty(url)) return;
                        String version = jsonObject.getString("version");
                        UpdateManager updateManager = new UpdateManager(HomeActivity.this);
                        updateManager.showNoticeDialog(version, "优化性能,修复已知BUG", url, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogDebug.err(ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 向服务器传极光RegisterId
     */
    private static void requestRegisterId() {
        LoginBean loginBean = SpDataCache.getSelfInfo(App.getApp());
        if (null == loginBean) return;
        String registrationID = JPushInterface.getRegistrationID(App.getApp());
        LogDebug.log(TAG, "HomeActivity-registrationID:=" + registrationID);
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", loginBean.getData().getM_id());
        map.put("registration_id", registrationID);
        map.put("current_city", SpDataCache.getGpsCity());
        map.put("gps_lat", SpDataCache.getGpsLatitude());
        map.put("gps_long", SpDataCache.getGpsLongitude());
        Xutils.post(Url.getregisId, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Loger.log(TAG, result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    String code = jsonObject.getString("code");
//                    if(!"200".equals(code)){
//                        requestRegisterId();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                requestRegisterId();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}
