package bbc.com.moteduan_lib.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.liemo.shareresource.Url;
import com.nineoldandroids.view.ViewHelper;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.ReleaseDate.ReleaseSkill;
import bbc.com.moteduan_lib.app.App;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.bean.DateEvery;
import bbc.com.moteduan_lib.bean.LoginBean;
import bbc.com.moteduan_lib.bean.NoticeEvery;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.dialog.DialogUtils;
import bbc.com.moteduan_lib.im.ConversationListActivity;
import bbc.com.moteduan_lib.leftmenu.ShareActivity;
import bbc.com.moteduan_lib.leftmenu.activity.MyOrder;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.maps.IMap;
import bbc.com.moteduan_lib.maps.LmLocation;
import bbc.com.moteduan_lib.maps.LmLocationListener;
import bbc.com.moteduan_lib.maps.LmMap;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;
import bbc.com.moteduan_lib.renzheng.Auth;
import bbc.com.moteduan_lib.tools.FastBlur;
import bbc.com.moteduan_lib.tools.ToastUtils;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import wei.toolkit.utils.ToastUtil;

public class HomeActivity extends BaseActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {
    private final String TAG = "HomeActivity";
    // 抽屉菜单对象

    private ActionBarDrawerToggle drawerbar;
    private ContentFragment testFragment;
    private RelativeLayout left_menu_layout, right_xiaoxi_layout;
    private ImageView sidebar;
    private ImageView msg;
    private RelativeLayout topbanner;
    private FrameLayout main_content_frame;
    private RelativeLayout main_content_frame_parent;
    private RelativeLayout main_left_drawer_layout;
    private RelativeLayout main_right_drawer_layout;
    private DrawerLayout main_drawer_layout;
    private LinearLayout money;
    private LinearLayout myorder;
    private LinearLayout sharetofriend;
    private LinearLayout setting;
    private ImageView date_title_iv;
    private TextView date_title_tv;
    private ImageView icon_iv;
    private TextView nickname_tv;
    private TextView time_start_end;
    private TextView location_tv;
    private TextView rewardmoney;
    private ImageView cover;

    private PullToRefreshListView data_listview;
    private PullToRefreshListView notice_listview;

    private GestureDetector mGestureDetector;
    private List<NoticeEvery> map_noticeitem;
    private List<DateEvery> map_dateitem;

    private int verticalMinDistance = 500;

    private int minVelocity = 60;

    private TextView date_tv_change;
    private TextView notice_tv_change;


    private ViewPager viewPager;
    private ArrayList<View> pageview;
    private int offset = 0;
    // 当前页编号
    private int currIndex = 0;
    // 滚动条宽度
    private int bmpW;
    //一倍滚动量
    private int one;

    private View view1;
    private View view2;
    private RadioButton textView18;
    private RadioButton textView20;
    private RadioButton textView23;
    private RadioButton textView24;
    private RadioButton textView25;
    private RadioButton textView26;
    private RadioButton textView28;
    private RadioButton textView29;
    private RadioButton textView30;
    private RadioButton textView31;
    private RadioButton textView32;
    private RadioButton textView33;
    private RadioButton textView34;

    private RadioGroup radioGroup;

    private RadioButton radioButton40;
    private RadioButton radioButton41;
    private RadioButton radioButton42;
    private RadioButton radioButton43;
    private RadioButton radioButton44;
    private RadioButton radioButton45;
    private RadioGroup radioGroup2;
    private String lat;
    private String lon;
    private View rootView;
    private ImageView home_btn;
    private android.support.design.widget.FloatingActionButton fab_btn;
    //  private android.support.design.widget.FloatingActionButton fab_btn;
    private TextView nickName;
    private int kk = 1;

    private ImageView leftmenuicondk;

    private int m_add_state;
    private int work_state;
    private String from;
    private FloatingActionButton actionButton;
    private ImageView mengban;
//    private DialogProgress dialogProgress;

    private int dateItem = 0;
    private int datepageNum = 1;
    private int noticeItem = 0;


    private DateitemAdapter dateadapter;
    private NoticeitemAdapter notiadapter;
    private Boolean isChange = false;
    private Boolean isFirst = true;

    private static final int REQUEST_CODE = 0;//请求码

    //配置需要取的权限
    static final String[] PERMISSION = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
//            Manifest.permission.CAPTURE_SECURE_VIDEO_OUTPUT
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION

    };
    private String userID;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private ToastUtils toast;
    private FrameLayout frameLayout;
    private RotateAnimation rotanimation;
    private long exitTime = 0;
    private TextView mUnreadMsgTv;
    private TextView mModlerz;

    // 缓存聊天用户
    private Map<String, UserInfo> mCacheChatId = new HashMap();


    private IMap mIMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ToastUtil.show(this,"66");
        LoginBean loginBean = SpDataCache.getSelfInfo(HomeActivity.this);
        String token = "";
        if(loginBean != null){
            token = loginBean.getData().getM_rong_token();
            userID = loginBean.getData().getM_head_photo();
        }
        connect(token);
        rootView = View.inflate(HomeActivity.this, R.layout.activity_home, null);


        String portraitUri = SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_head_photo();
        if (TextUtils.isEmpty(portraitUri)) {
            portraitUri = "";
        }

        // 给融云提供用户信息
//        RongIM.getInstance().setMessageAttachedUserInfo(true);
//        RongIM.getInstance().setCurrentUserInfo(new UserInfo(userID, App.getApp().getUserName(), Uri.parse(portraitUri)));

        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {
                UserInfo userInfo = mCacheChatId.get(s);
                if (userInfo != null) {
                    return userInfo;
                }
                if (s.equals(SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_head_photo())) {
                    userInfo = new UserInfo(s, SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_nick_name(), Uri.parse(SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_head_photo()));
                    mCacheChatId.put(s, userInfo);
                    return userInfo;
                }
                return findUserInfo(s);
            }
        }, false);


        initRongUnreadMessage();
        requestRegisterId();

        initView();
        try {
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initEvent();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        toast = ToastUtils.getInstance(HomeActivity.this);
        requestLocation();
    }

    private void requestLocation() {
        mIMap = LmMap.get();
        mIMap.setLocationListener(new LmLocationListener() {
            @Override
            public void onLocationChange(LmLocation location) {
                mIMap.stopLocation();
                mIMap.onDestroy();
                String city = location.getCity();
                String address = location.getCity() + location.getStreet() + location.getStreetNumber();
                LogDebug.log("imap", address);
                SpDataCache.saveAddress(getBaseContext(), city, address,location.getLatitude(),location.getLongitude());
            }
        });
        mIMap.startLocation();
    }


    private UserInfo findUserInfo(String id) {
        Map map = new HashMap();
        map.put("mid", id);
        Xutils.post(Url.getUserInfo, map, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogDebug.log(TAG, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (!TextUtils.equals("200", code)) {
                        return;
                    }
                    JSONObject object = jsonObject.getJSONObject("data");
                    String id = object.getString("mid");
                    String nickName = object.getString("nickName");
                    String headPhoto = object.getString("headPhoto");
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

//    private void registerIMExtensionModule() {
//        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
//        IExtensionModule defaultModule = null;
//        if (moduleList != null) {
//            for (IExtensionModule module : moduleList) {
//                if (module instanceof BBCDefaultExtensionModule) {
//                    defaultModule = module;
//                    break;
//                }
//            }
//            if (defaultModule != null) {
//                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
//            }
//            RongExtensionManager.getInstance().registerExtensionModule(new SupplyRongIMPlugin.Builder().create());
//        }
//    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 { #init(Context)} 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     *              callback 连接回调。
     * @return RongIM  客户端核心类的实例。
     */
    private void connect(String token) {
        if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {
            LogDebug.err("进入链接融云：" + token);
            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    LogDebug.err("token获取失败");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    LogDebug.err("融云链接成功" + userid);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogDebug.err("融云链接失败" + errorCode);
                }
            });
        }
    }



    /**
     * 向服务器传极光RegisterId
     */
    private void requestRegisterId() {
        String registrationID = JPushInterface.getRegistrationID(HomeActivity.this);
        LogDebug.log(TAG,"HomeActivity-registrationID:=" + registrationID);
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", userID);
        map.put("registration_id", registrationID);
        Xutils.post(Url.getregisId, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.err(result);
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
    }


    //进入权限设置页面
    private void startPermissionActivity() {
        PermissionActivity.startActivityForResult(this, REQUEST_CODE, PERMISSION);
    }


    //返回结果回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拒绝时，没有获取到主要权限，无法运行，关闭页面
        if (requestCode == REQUEST_CODE && resultCode == PermissionActivity.PERMISSION_DENIEG) {
            finish();
        }
    }

    public void initView() {
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.notice, null);
        view2 = inflater.inflate(R.layout.date, null);
        mModlerz = (TextView) view2.findViewById(R.id.modelrz);

        mUnreadMsgTv = (TextView) findViewById(R.id.activity_home_unread_tv);
        mengban = (ImageView) findViewById(R.id.mengban_home);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        data_listview = (PullToRefreshListView) view1.findViewById(R.id.notice_listview);
        textView18 = (RadioButton) view1.findViewById(R.id.textView18);
        textView20 = (RadioButton) view1.findViewById(R.id.textView20);
        textView23 = (RadioButton) view1.findViewById(R.id.textView23);
        textView24 = (RadioButton) view1.findViewById(R.id.textView24);
        textView25 = (RadioButton) view1.findViewById(R.id.textView25);
        textView26 = (RadioButton) view1.findViewById(R.id.textView26);
        textView28 = (RadioButton) view1.findViewById(R.id.textView28);
        textView29 = (RadioButton) view1.findViewById(R.id.textView29);
        textView30 = (RadioButton) view1.findViewById(R.id.tv30);
        textView31 = (RadioButton) view1.findViewById(R.id.textView31);
        textView32 = (RadioButton) view1.findViewById(R.id.textView32);
        textView33 = (RadioButton) view1.findViewById(R.id.textView33);
        textView34 = (RadioButton) view1.findViewById(R.id.textView34);
        radioGroup = (RadioGroup) view1.findViewById(R.id.notice_radiogroup);

        notice_listview = (PullToRefreshListView) view2.findViewById(R.id.date_listview);
        radioButton40 = (RadioButton) view2.findViewById(R.id.textView40);
        radioButton41 = (RadioButton) view2.findViewById(R.id.textView41);
        radioButton42 = (RadioButton) view2.findViewById(R.id.textView42);
        radioButton43 = (RadioButton) view2.findViewById(R.id.textView43);
        radioButton44 = (RadioButton) view2.findViewById(R.id.textView44);
        radioButton45 = (RadioButton) view2.findViewById(R.id.textView45);
        radioGroup2 = (RadioGroup) view2.findViewById(R.id.notice_radiogroup2);


        data_listview.setMode(PullToRefreshBase.Mode.BOTH);
        data_listview.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_to_load));
        data_listview.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
        data_listview.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.release_to_load));
        data_listview.getRefreshableView().setDivider(null);
        data_listview.getRefreshableView().setSelector(android.R.color.transparent);

        data_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isChange = false;
                datepageNum = 1;
                dateRequest(dateItem, datepageNum);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isChange = false;
                datepageNum++;
                dateRequest(dateItem, datepageNum);
            }
        });

        notice_listview.setMode(PullToRefreshBase.Mode.BOTH);
        notice_listview.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_to_load));
        notice_listview.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
        notice_listview.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.release_to_load));
        notice_listview.getRefreshableView().setDivider(null);
        notice_listview.getRefreshableView().setSelector(android.R.color.transparent);
        date_tv_change = (TextView) findViewById(R.id.date_tv_change);
        notice_tv_change = (TextView) findViewById(R.id.notice_tv_change);
        mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener) this);


        notice_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isChange = false;
                datepageNum = 1;
                noticeRequest(noticeItem, datepageNum);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isChange = false;
                datepageNum++;
                noticeRequest(noticeItem, datepageNum);
            }
        });
        cover = (ImageView) findViewById(R.id.cover);
        date_title_iv = (ImageView) findViewById(R.id.date_title_iv);
        date_title_tv = (TextView) findViewById(R.id.date_title_tv);
        icon_iv = (ImageView) findViewById(R.id.icon_iv);
        nickname_tv = (TextView) findViewById(R.id.nickname_tv);
        time_start_end = (TextView) findViewById(R.id.time_start_end);
        location_tv = (TextView) findViewById(R.id.location_tv);
        rewardmoney = (TextView) findViewById(R.id.rewardmoney);
        home_btn = (ImageView) findViewById(R.id.home_btn);


        fab_btn = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab);

        testFragment = new ContentFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction f_transaction = fragmentManager.beginTransaction();

        f_transaction.replace(R.id.main_content_frame_parent, testFragment);

        f_transaction.commitAllowingStateLoss();

        initLeftLayout();

        initRightLayout();

        sidebar = (ImageView) findViewById(R.id.sidebar);
        // chose = (ImageView) findViewById(R.id.chose);
        msg = (ImageView) findViewById(R.id.msg);
        topbanner = (RelativeLayout) findViewById(R.id.topbanner);

        main_content_frame = (FrameLayout) findViewById(R.id.main_content_frame);
        main_content_frame_parent = (RelativeLayout) findViewById(R.id.main_content_frame_parent);
        main_left_drawer_layout = (RelativeLayout) findViewById(R.id.main_left_drawer_layout);
        main_right_drawer_layout = (RelativeLayout) findViewById(R.id.main_right_drawer_layout);
        main_drawer_layout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        main_drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                Gravity.RIGHT);
        main_drawer_layout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mengban.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mengban.setVisibility(View.GONE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }


    private void LoginAgain() {
        SharedPreferences sharedPreferences = getSharedPreferences(SpDataCache.ACCOUNT, Context.MODE_PRIVATE);
        String phtotourl = sharedPreferences.getString("phtotourl", "");
        String name = sharedPreferences.getString("name", "");
        String account = sharedPreferences.getString("account", "");
        String sex1 = sharedPreferences.getString("sex", "");
//        String registerid = sharedPreferences.getString("registerid", "");
        Map<String, Object> map = new HashMap<>();
        map.put("m.m_head_photo", phtotourl);
        map.put("m.m_nick_name", name);
        map.put("m.m_wx_account", account);
        map.put("m.m_sex", sex1);
//        map.put("m_registration_id",registerid);
        Xutils.post(Url.wxLogin, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.err("result==" + result);
                SharedPreferences sharedPreferences = getSharedPreferences(SpDataCache.SELF_INFO, MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.clear();
                edit.commit();
                JSONObject json = null;
                try {
                    json = new JSONObject(result);
                    String code = json.getString("code");
                    String msg = json.getString("tips");
                    if ("200".equals(code)) {
                        SpDataCache.saveSelfInfo(HomeActivity.this, result);
                        Xutils.setCircularIcon(leftmenuicondk, SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_head_photo());
                        String userName = SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_nick_name();
                        if (nickName != null) {
                            nickName.setText(userName);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogDebug.err("onError" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogDebug.err("onCancelled" + cex.toString());
            }

            @Override
            public void onFinished() {
                modelState();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent i) {
        super.onNewIntent(i);
        Intent intent = i;
        String from = intent.getStringExtra("from");
        if (TextUtils.isEmpty(from)) {
            from = "";
        }
        if ("ReleaseSkill".equals(from)) {
            // 2 模特出车状态
//            requetWorkState(2, new Callback.CommonCallback<String>() {
//                @Override
//                public void onSuccess(String result) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        String code = jsonObject.getString("code");
//                        if ("200".equals(code)) {
//                            SpDataCache.saveModeAddState(1);
//                            modelState();
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onError(Throwable ex, boolean isOnCallback) {
//
//
//                }
//
//                @Override
//                public void onCancelled(CancelledException cex) {
//
//                }
//
//                @Override
//                public void onFinished() {
//
//                }
//            });
        } else if ("myReceiver".equals(from)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("通知");
            String alert = intent.getStringExtra("alert");
            builder.setMessage(alert);
            builder.setPositiveButton("查看详情", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent1 = new Intent(HomeActivity.this, MyOrder.class);
                    startActivity(intent1);
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            //需要把对话框的类型设为TYPE_SYSTEM_ALERT，否则对话框无法在广播接收器里弹出
//            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();
        }

    }

    // 模特状态
    private void modelState() {
        m_add_state = SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_add_state();
        work_state = SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_work_state();
        LogDebug.err(m_add_state + "-----------");
        if (m_add_state == 1) {
            if (fab_btn.getVisibility() != View.GONE) {
                fab_btn.setVisibility(View.GONE);
            }
            if (work_state == 2 && mModlerz != null) {
                mModlerz.setVisibility(View.VISIBLE);
            }
            startMenuAnimation();
        }

    }

    public void initData() {
        rotanimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        map_noticeitem = new ArrayList<>();
        map_dateitem = new ArrayList<>();
        dateadapter = new DateitemAdapter();
        data_listview.setAdapter(dateadapter);

        notiadapter = new NoticeitemAdapter(map_noticeitem);
        notice_listview.setAdapter(notiadapter);


        final Intent intent = getIntent();
        from = intent.getStringExtra("from");
        if (TextUtils.isEmpty(from)) {
            from = "";
        }
        if ("myReceiver".equals(from)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("通知");
            String alert = intent.getStringExtra("alert");
            builder.setMessage(alert);
            builder.setPositiveButton("查看详情", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent1 = new Intent(HomeActivity.this, MyOrder.class);
                    startActivity(intent1);
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            //需要把对话框的类型设为TYPE_SYSTEM_ALERT，否则对话框无法在广播接收器里弹出
//            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();
        }
        /*leftmenuicondk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,EditActivity.class));
            }
        });*/
//        noticeRequest(noticeItem,13);

        radioButton40.setTextColor(Color.parseColor("#ffffff"));
        radioButton40.setBackgroundResource(R.drawable.bg_radius12_color_themered);
        textView18.setTextColor(Color.parseColor("#ffffff"));
        textView18.setBackgroundResource(R.drawable.bg_radius12_color_themered);
//        textView20.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dateRequest(dateItem);
//            }
//        });
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                isChange = true;
                notice_listview.setVisibility(View.GONE);
                if (i == radioButton40.getId()) {
                    radioButton40.setTextColor(Color.parseColor("#ffffff"));
                    radioButton41.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton42.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton43.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton44.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton45.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton40.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    radioButton41.setBackgroundResource(R.drawable.bg_write);
                    radioButton42.setBackgroundResource(R.drawable.bg_write);
                    radioButton43.setBackgroundResource(R.drawable.bg_write);
                    radioButton44.setBackgroundResource(R.drawable.bg_write);
                    radioButton45.setBackgroundResource(R.drawable.bg_write);
                    noticeItem = 0;
                    noticeRequest(noticeItem, 1);
                } else if (i == radioButton41.getId()) {
                    radioButton40.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton41.setTextColor(Color.parseColor("#ffffff"));
                    radioButton42.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton43.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton44.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton45.setTextColor(Color.parseColor("#c8c8c8"));

                    radioButton40.setBackgroundResource(R.drawable.bg_write);
                    radioButton41.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    radioButton42.setBackgroundResource(R.drawable.bg_write);
                    radioButton43.setBackgroundResource(R.drawable.bg_write);
                    radioButton44.setBackgroundResource(R.drawable.bg_write);
                    radioButton45.setBackgroundResource(R.drawable.bg_write);
                    noticeItem = 14;
                    noticeRequest(noticeItem, 1);
                } else if (i == radioButton42.getId()) {
                    radioButton40.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton41.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton42.setTextColor(Color.parseColor("#ffffff"));
                    radioButton43.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton44.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton45.setTextColor(Color.parseColor("#c8c8c8"));

                    radioButton40.setBackgroundResource(R.drawable.bg_write);
                    radioButton41.setBackgroundResource(R.drawable.bg_write);
                    radioButton42.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    radioButton43.setBackgroundResource(R.drawable.bg_write);
                    radioButton44.setBackgroundResource(R.drawable.bg_write);
                    radioButton45.setBackgroundResource(R.drawable.bg_write);
                    noticeItem = 15;
                    noticeRequest(noticeItem, 1);
                } else if (i == radioButton43.getId()) {
                    radioButton40.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton41.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton42.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton43.setTextColor(Color.parseColor("#ffffff"));
                    radioButton44.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton45.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton40.setBackgroundResource(R.drawable.bg_write);
                    radioButton41.setBackgroundResource(R.drawable.bg_write);
                    radioButton42.setBackgroundResource(R.drawable.bg_write);
                    radioButton43.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    radioButton44.setBackgroundResource(R.drawable.bg_write);
                    radioButton45.setBackgroundResource(R.drawable.bg_write);
                    noticeItem = 16;
                    noticeRequest(noticeItem, 1);
                } else if (i == radioButton44.getId()) {
                    radioButton40.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton41.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton42.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton43.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton44.setTextColor(Color.parseColor("#ffffff"));
                    radioButton45.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton40.setBackgroundResource(R.drawable.bg_write);
                    radioButton41.setBackgroundResource(R.drawable.bg_write);
                    radioButton42.setBackgroundResource(R.drawable.bg_write);
                    radioButton43.setBackgroundResource(R.drawable.bg_write);
                    radioButton44.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    radioButton45.setBackgroundResource(R.drawable.bg_write);
                    noticeItem = 17;
                    noticeRequest(noticeItem, 1);
                } else if (i == radioButton45.getId()) {
                    radioButton40.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton41.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton42.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton43.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton44.setTextColor(Color.parseColor("#c8c8c8"));
                    radioButton45.setTextColor(Color.parseColor("#ffffff"));
                    radioButton40.setBackgroundResource(R.drawable.bg_write);
                    radioButton41.setBackgroundResource(R.drawable.bg_write);
                    radioButton42.setBackgroundResource(R.drawable.bg_write);
                    radioButton43.setBackgroundResource(R.drawable.bg_write);
                    radioButton44.setBackgroundResource(R.drawable.bg_write);
                    radioButton45.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    noticeItem = 18;
                    noticeRequest(noticeItem, 1);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                isChange = true;
                if (i == textView18.getId()) {
                    textView18.setTextColor(Color.parseColor("#ffffff"));
                    textView20.setTextColor(Color.parseColor("#c8c8c8"));
                    textView23.setTextColor(Color.parseColor("#c8c8c8"));
                    textView24.setTextColor(Color.parseColor("#c8c8c8"));
                    textView25.setTextColor(Color.parseColor("#c8c8c8"));
                    textView26.setTextColor(Color.parseColor("#c8c8c8"));
                    textView28.setTextColor(Color.parseColor("#c8c8c8"));
                    textView29.setTextColor(Color.parseColor("#c8c8c8"));
                    textView30.setTextColor(Color.parseColor("#c8c8c8"));
                    textView31.setTextColor(Color.parseColor("#c8c8c8"));
                    textView32.setTextColor(Color.parseColor("#c8c8c8"));
                    textView33.setTextColor(Color.parseColor("#c8c8c8"));
                    textView34.setTextColor(Color.parseColor("#c8c8c8"));

                    textView18.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    textView20.setBackgroundResource(R.drawable.bg_write);
                    textView23.setBackgroundResource(R.drawable.bg_write);
                    textView24.setBackgroundResource(R.drawable.bg_write);
                    textView25.setBackgroundResource(R.drawable.bg_write);
                    textView26.setBackgroundResource(R.drawable.bg_write);
                    textView28.setBackgroundResource(R.drawable.bg_write);
                    textView29.setBackgroundResource(R.drawable.bg_write);
                    textView30.setBackgroundResource(R.drawable.bg_write);
                    textView31.setBackgroundResource(R.drawable.bg_write);
                    textView32.setBackgroundResource(R.drawable.bg_write);
                    textView33.setBackgroundResource(R.drawable.bg_write);
                    textView34.setBackgroundResource(R.drawable.bg_write);
                    dateItem = 0;
                    dateRequest(dateItem, 1);
                    data_listview.setVisibility(View.GONE);

                } else if (i == textView20.getId()) {
                    textView18.setTextColor(Color.parseColor("#c8c8c8"));
                    textView20.setTextColor(Color.parseColor("#ffffff"));
                    textView23.setTextColor(Color.parseColor("#c8c8c8"));
                    textView24.setTextColor(Color.parseColor("#c8c8c8"));
                    textView25.setTextColor(Color.parseColor("#c8c8c8"));
                    textView26.setTextColor(Color.parseColor("#c8c8c8"));
                    textView28.setTextColor(Color.parseColor("#c8c8c8"));
                    textView29.setTextColor(Color.parseColor("#c8c8c8"));
                    textView30.setTextColor(Color.parseColor("#c8c8c8"));
                    textView31.setTextColor(Color.parseColor("#c8c8c8"));
                    textView32.setTextColor(Color.parseColor("#c8c8c8"));
                    textView33.setTextColor(Color.parseColor("#c8c8c8"));
                    textView34.setTextColor(Color.parseColor("#c8c8c8"));

                    textView18.setBackgroundResource(R.drawable.bg_write);
                    textView20.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    textView23.setBackgroundResource(R.drawable.bg_write);
                    textView24.setBackgroundResource(R.drawable.bg_write);
                    textView25.setBackgroundResource(R.drawable.bg_write);
                    textView26.setBackgroundResource(R.drawable.bg_write);
                    textView28.setBackgroundResource(R.drawable.bg_write);
                    textView29.setBackgroundResource(R.drawable.bg_write);
                    textView30.setBackgroundResource(R.drawable.bg_write);
                    textView31.setBackgroundResource(R.drawable.bg_write);
                    textView32.setBackgroundResource(R.drawable.bg_write);
                    textView33.setBackgroundResource(R.drawable.bg_write);
                    textView34.setBackgroundResource(R.drawable.bg_write);
                    dateItem = 1;
                    dateRequest(dateItem, 1);

                } else if (i == textView23.getId()) {
                    textView18.setTextColor(Color.parseColor("#c8c8c8"));
                    textView20.setTextColor(Color.parseColor("#c8c8c8"));
                    textView23.setTextColor(Color.parseColor("#ffffff"));
                    textView24.setTextColor(Color.parseColor("#c8c8c8"));
                    textView25.setTextColor(Color.parseColor("#c8c8c8"));
                    textView26.setTextColor(Color.parseColor("#c8c8c8"));
                    textView28.setTextColor(Color.parseColor("#c8c8c8"));
                    textView29.setTextColor(Color.parseColor("#c8c8c8"));
                    textView30.setTextColor(Color.parseColor("#c8c8c8"));
                    textView31.setTextColor(Color.parseColor("#c8c8c8"));
                    textView32.setTextColor(Color.parseColor("#c8c8c8"));
                    textView33.setTextColor(Color.parseColor("#c8c8c8"));
                    textView34.setTextColor(Color.parseColor("#c8c8c8"));
                    textView18.setBackgroundResource(R.drawable.bg_write);
                    textView23.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    textView20.setBackgroundResource(R.drawable.bg_write);
                    textView24.setBackgroundResource(R.drawable.bg_write);
                    textView25.setBackgroundResource(R.drawable.bg_write);
                    textView26.setBackgroundResource(R.drawable.bg_write);
                    textView28.setBackgroundResource(R.drawable.bg_write);
                    textView29.setBackgroundResource(R.drawable.bg_write);
                    textView30.setBackgroundResource(R.drawable.bg_write);
                    textView31.setBackgroundResource(R.drawable.bg_write);
                    textView32.setBackgroundResource(R.drawable.bg_write);
                    textView33.setBackgroundResource(R.drawable.bg_write);
                    textView34.setBackgroundResource(R.drawable.bg_write);
                    dateItem = 2;
                    dateRequest(dateItem, 1);
                } else if (i == textView24.getId()) {
                    textView18.setTextColor(Color.parseColor("#c8c8c8"));
                    textView20.setTextColor(Color.parseColor("#c8c8c8"));
                    textView23.setTextColor(Color.parseColor("#c8c8c8"));
                    textView24.setTextColor(Color.parseColor("#ffffff"));
                    textView25.setTextColor(Color.parseColor("#c8c8c8"));
                    textView26.setTextColor(Color.parseColor("#c8c8c8"));
                    textView28.setTextColor(Color.parseColor("#c8c8c8"));
                    textView29.setTextColor(Color.parseColor("#c8c8c8"));
                    textView30.setTextColor(Color.parseColor("#c8c8c8"));
                    textView31.setTextColor(Color.parseColor("#c8c8c8"));
                    textView32.setTextColor(Color.parseColor("#c8c8c8"));
                    textView33.setTextColor(Color.parseColor("#c8c8c8"));
                    textView34.setTextColor(Color.parseColor("#c8c8c8"));
                    textView18.setBackgroundResource(R.drawable.bg_write);
                    textView23.setBackgroundResource(R.drawable.bg_write);
                    textView20.setBackgroundResource(R.drawable.bg_write);
                    textView24.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    textView23.setBackgroundResource(R.drawable.bg_write);
                    textView25.setBackgroundResource(R.drawable.bg_write);
                    textView26.setBackgroundResource(R.drawable.bg_write);
                    textView28.setBackgroundResource(R.drawable.bg_write);
                    textView29.setBackgroundResource(R.drawable.bg_write);
                    textView30.setBackgroundResource(R.drawable.bg_write);
                    textView31.setBackgroundResource(R.drawable.bg_write);
                    textView32.setBackgroundResource(R.drawable.bg_write);
                    textView33.setBackgroundResource(R.drawable.bg_write);
                    textView34.setBackgroundResource(R.drawable.bg_write);
                    dateItem = 3;
                    dateRequest(dateItem, 1);
                } else if (i == textView25.getId()) {
                    textView18.setTextColor(Color.parseColor("#c8c8c8"));
                    textView20.setTextColor(Color.parseColor("#c8c8c8"));
                    textView23.setTextColor(Color.parseColor("#c8c8c8"));
                    textView24.setTextColor(Color.parseColor("#c8c8c8"));
                    textView25.setTextColor(Color.parseColor("#ffffff"));
                    textView26.setTextColor(Color.parseColor("#c8c8c8"));
                    textView28.setTextColor(Color.parseColor("#c8c8c8"));
                    textView29.setTextColor(Color.parseColor("#c8c8c8"));
                    textView30.setTextColor(Color.parseColor("#c8c8c8"));
                    textView31.setTextColor(Color.parseColor("#c8c8c8"));
                    textView32.setTextColor(Color.parseColor("#c8c8c8"));
                    textView33.setTextColor(Color.parseColor("#c8c8c8"));
                    textView34.setTextColor(Color.parseColor("#c8c8c8"));
                    textView18.setBackgroundResource(R.drawable.bg_write);
                    textView20.setBackgroundResource(R.drawable.bg_write);
                    textView23.setBackgroundResource(R.drawable.bg_write);
                    textView24.setBackgroundResource(R.drawable.bg_write);
                    textView25.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    textView26.setBackgroundResource(R.drawable.bg_write);
                    textView28.setBackgroundResource(R.drawable.bg_write);
                    textView29.setBackgroundResource(R.drawable.bg_write);
                    textView30.setBackgroundResource(R.drawable.bg_write);
                    textView31.setBackgroundResource(R.drawable.bg_write);
                    textView32.setBackgroundResource(R.drawable.bg_write);
                    textView33.setBackgroundResource(R.drawable.bg_write);
                    textView34.setBackgroundResource(R.drawable.bg_write);
                    dateItem = 4;
                    dateRequest(dateItem, 1);
                } else if (i == textView26.getId()) {

                    textView18.setTextColor(Color.parseColor("#c8c8c8"));
                    textView20.setTextColor(Color.parseColor("#c8c8c8"));
                    textView23.setTextColor(Color.parseColor("#c8c8c8"));
                    textView24.setTextColor(Color.parseColor("#c8c8c8"));
                    textView25.setTextColor(Color.parseColor("#c8c8c8"));
                    textView26.setTextColor(Color.parseColor("#ffffff"));
                    textView28.setTextColor(Color.parseColor("#c8c8c8"));
                    textView29.setTextColor(Color.parseColor("#c8c8c8"));
                    textView30.setTextColor(Color.parseColor("#c8c8c8"));
                    textView31.setTextColor(Color.parseColor("#c8c8c8"));
                    textView32.setTextColor(Color.parseColor("#c8c8c8"));
                    textView33.setTextColor(Color.parseColor("#c8c8c8"));
                    textView34.setTextColor(Color.parseColor("#c8c8c8"));
                    textView18.setBackgroundResource(R.drawable.bg_write);
                    textView20.setBackgroundResource(R.drawable.bg_write);
                    textView23.setBackgroundResource(R.drawable.bg_write);
                    textView24.setBackgroundResource(R.drawable.bg_write);
                    textView25.setBackgroundResource(R.drawable.bg_write);
                    textView26.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    textView28.setBackgroundResource(R.drawable.bg_write);
                    textView29.setBackgroundResource(R.drawable.bg_write);
                    textView30.setBackgroundResource(R.drawable.bg_write);
                    textView31.setBackgroundResource(R.drawable.bg_write);
                    textView32.setBackgroundResource(R.drawable.bg_write);
                    textView33.setBackgroundResource(R.drawable.bg_write);
                    textView34.setBackgroundResource(R.drawable.bg_write);
                    dateItem = 5;
                    dateRequest(dateItem, 1);
                } else if (i == textView28.getId()) {
                    textView18.setTextColor(Color.parseColor("#c8c8c8"));
                    textView20.setTextColor(Color.parseColor("#c8c8c8"));
                    textView23.setTextColor(Color.parseColor("#c8c8c8"));
                    textView24.setTextColor(Color.parseColor("#c8c8c8"));
                    textView25.setTextColor(Color.parseColor("#c8c8c8"));
                    textView26.setTextColor(Color.parseColor("#c8c8c8"));
                    textView28.setTextColor(Color.parseColor("#ffffff"));
                    textView29.setTextColor(Color.parseColor("#c8c8c8"));
                    textView30.setTextColor(Color.parseColor("#c8c8c8"));
                    textView31.setTextColor(Color.parseColor("#c8c8c8"));
                    textView32.setTextColor(Color.parseColor("#c8c8c8"));
                    textView33.setTextColor(Color.parseColor("#c8c8c8"));
                    textView34.setTextColor(Color.parseColor("#c8c8c8"));

                    textView18.setBackgroundResource(R.drawable.bg_write);
                    textView20.setBackgroundResource(R.drawable.bg_write);
                    textView23.setBackgroundResource(R.drawable.bg_write);
                    textView24.setBackgroundResource(R.drawable.bg_write);
                    textView25.setBackgroundResource(R.drawable.bg_write);
                    textView26.setBackgroundResource(R.drawable.bg_write);
                    textView28.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    textView29.setBackgroundResource(R.drawable.bg_write);
                    textView30.setBackgroundResource(R.drawable.bg_write);
                    textView31.setBackgroundResource(R.drawable.bg_write);
                    textView32.setBackgroundResource(R.drawable.bg_write);
                    textView33.setBackgroundResource(R.drawable.bg_write);
                    textView34.setBackgroundResource(R.drawable.bg_write);
                    dateItem = 6;
                    dateRequest(dateItem, 1);
                } else if (i == textView29.getId()) {

                    textView18.setTextColor(Color.parseColor("#c8c8c8"));
                    textView20.setTextColor(Color.parseColor("#c8c8c8"));
                    textView23.setTextColor(Color.parseColor("#c8c8c8"));
                    textView24.setTextColor(Color.parseColor("#c8c8c8"));
                    textView25.setTextColor(Color.parseColor("#c8c8c8"));
                    textView26.setTextColor(Color.parseColor("#c8c8c8"));
                    textView28.setTextColor(Color.parseColor("#c8c8c8"));
                    textView29.setTextColor(Color.parseColor("#ffffff"));
                    textView30.setTextColor(Color.parseColor("#c8c8c8"));
                    textView31.setTextColor(Color.parseColor("#c8c8c8"));
                    textView32.setTextColor(Color.parseColor("#c8c8c8"));
                    textView33.setTextColor(Color.parseColor("#c8c8c8"));
                    textView34.setTextColor(Color.parseColor("#c8c8c8"));

                    textView18.setBackgroundResource(R.drawable.bg_write);
                    textView20.setBackgroundResource(R.drawable.bg_write);
                    textView23.setBackgroundResource(R.drawable.bg_write);
                    textView24.setBackgroundResource(R.drawable.bg_write);
                    textView25.setBackgroundResource(R.drawable.bg_write);
                    textView26.setBackgroundResource(R.drawable.bg_write);
                    textView28.setBackgroundResource(R.drawable.bg_write);
                    textView29.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    textView30.setBackgroundResource(R.drawable.bg_write);
                    textView31.setBackgroundResource(R.drawable.bg_write);
                    textView32.setBackgroundResource(R.drawable.bg_write);
                    textView33.setBackgroundResource(R.drawable.bg_write);
                    textView34.setBackgroundResource(R.drawable.bg_write);
                    dateItem = 7;
                    dateRequest(dateItem, 1);
                } else if (i == textView30.getId()) {
                    textView18.setTextColor(Color.parseColor("#c8c8c8"));
                    textView20.setTextColor(Color.parseColor("#c8c8c8"));
                    textView23.setTextColor(Color.parseColor("#c8c8c8"));
                    textView24.setTextColor(Color.parseColor("#c8c8c8"));
                    textView25.setTextColor(Color.parseColor("#c8c8c8"));
                    textView26.setTextColor(Color.parseColor("#c8c8c8"));
                    textView28.setTextColor(Color.parseColor("#c8c8c8"));
                    textView29.setTextColor(Color.parseColor("#c8c8c8"));
                    textView30.setTextColor(Color.parseColor("#ffffff"));
                    textView31.setTextColor(Color.parseColor("#c8c8c8"));
                    textView32.setTextColor(Color.parseColor("#c8c8c8"));
                    textView33.setTextColor(Color.parseColor("#c8c8c8"));
                    textView34.setTextColor(Color.parseColor("#c8c8c8"));
                    textView18.setBackgroundResource(R.drawable.bg_write);
                    textView20.setBackgroundResource(R.drawable.bg_write);
                    textView23.setBackgroundResource(R.drawable.bg_write);
                    textView24.setBackgroundResource(R.drawable.bg_write);
                    textView25.setBackgroundResource(R.drawable.bg_write);
                    textView26.setBackgroundResource(R.drawable.bg_write);
                    textView28.setBackgroundResource(R.drawable.bg_write);
                    textView29.setBackgroundResource(R.drawable.bg_write);
                    textView30.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    textView31.setBackgroundResource(R.drawable.bg_write);
                    textView32.setBackgroundResource(R.drawable.bg_write);
                    textView33.setBackgroundResource(R.drawable.bg_write);
                    textView34.setBackgroundResource(R.drawable.bg_write);
                    dateItem = 8;
                    dateRequest(dateItem, 1);
                } else if (i == textView31.getId()) {
                    textView18.setTextColor(Color.parseColor("#c8c8c8"));
                    textView20.setTextColor(Color.parseColor("#c8c8c8"));
                    textView23.setTextColor(Color.parseColor("#c8c8c8"));
                    textView24.setTextColor(Color.parseColor("#c8c8c8"));
                    textView25.setTextColor(Color.parseColor("#c8c8c8"));
                    textView26.setTextColor(Color.parseColor("#c8c8c8"));
                    textView28.setTextColor(Color.parseColor("#c8c8c8"));
                    textView29.setTextColor(Color.parseColor("#c8c8c8"));
                    textView30.setTextColor(Color.parseColor("#c8c8c8"));
                    textView31.setTextColor(Color.parseColor("#ffffff"));
                    textView32.setTextColor(Color.parseColor("#c8c8c8"));
                    textView33.setTextColor(Color.parseColor("#c8c8c8"));
                    textView34.setTextColor(Color.parseColor("#c8c8c8"));
                    textView18.setBackgroundResource(R.drawable.bg_write);
                    textView20.setBackgroundResource(R.drawable.bg_write);
                    textView23.setBackgroundResource(R.drawable.bg_write);
                    textView24.setBackgroundResource(R.drawable.bg_write);
                    textView25.setBackgroundResource(R.drawable.bg_write);
                    textView26.setBackgroundResource(R.drawable.bg_write);
                    textView28.setBackgroundResource(R.drawable.bg_write);
                    textView29.setBackgroundResource(R.drawable.bg_write);
                    textView30.setBackgroundResource(R.drawable.bg_write);
                    textView31.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    textView32.setBackgroundResource(R.drawable.bg_write);
                    textView33.setBackgroundResource(R.drawable.bg_write);
                    textView34.setBackgroundResource(R.drawable.bg_write);
                    dateItem = 9;
                    dateRequest(dateItem, 1);
                } else if (i == textView32.getId()) {
                    textView18.setTextColor(Color.parseColor("#c8c8c8"));
                    textView20.setTextColor(Color.parseColor("#c8c8c8"));
                    textView23.setTextColor(Color.parseColor("#c8c8c8"));
                    textView24.setTextColor(Color.parseColor("#c8c8c8"));
                    textView25.setTextColor(Color.parseColor("#c8c8c8"));
                    textView26.setTextColor(Color.parseColor("#c8c8c8"));
                    textView28.setTextColor(Color.parseColor("#c8c8c8"));
                    textView29.setTextColor(Color.parseColor("#c8c8c8"));
                    textView30.setTextColor(Color.parseColor("#c8c8c8"));
                    textView31.setTextColor(Color.parseColor("#c8c8c8"));
                    textView32.setTextColor(Color.parseColor("#ffffff"));
                    textView33.setTextColor(Color.parseColor("#c8c8c8"));
                    textView34.setTextColor(Color.parseColor("#c8c8c8"));
                    textView18.setBackgroundResource(R.drawable.bg_write);
                    textView20.setBackgroundResource(R.drawable.bg_write);
                    textView23.setBackgroundResource(R.drawable.bg_write);
                    textView24.setBackgroundResource(R.drawable.bg_write);
                    textView25.setBackgroundResource(R.drawable.bg_write);
                    textView26.setBackgroundResource(R.drawable.bg_write);
                    textView28.setBackgroundResource(R.drawable.bg_write);
                    textView29.setBackgroundResource(R.drawable.bg_write);
                    textView30.setBackgroundResource(R.drawable.bg_write);
                    textView31.setBackgroundResource(R.drawable.bg_write);
                    textView32.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    textView33.setBackgroundResource(R.drawable.bg_write);
                    textView34.setBackgroundResource(R.drawable.bg_write);
                    dateItem = 10;
                    dateRequest(dateItem, 1);
                } else if (i == textView33.getId()) {
                    textView18.setTextColor(Color.parseColor("#c8c8c8"));
                    textView20.setTextColor(Color.parseColor("#c8c8c8"));
                    textView23.setTextColor(Color.parseColor("#c8c8c8"));
                    textView24.setTextColor(Color.parseColor("#c8c8c8"));
                    textView25.setTextColor(Color.parseColor("#c8c8c8"));
                    textView26.setTextColor(Color.parseColor("#c8c8c8"));
                    textView28.setTextColor(Color.parseColor("#c8c8c8"));
                    textView29.setTextColor(Color.parseColor("#c8c8c8"));
                    textView30.setTextColor(Color.parseColor("#c8c8c8"));
                    textView31.setTextColor(Color.parseColor("#c8c8c8"));
                    textView32.setTextColor(Color.parseColor("#c8c8c8"));
                    textView33.setTextColor(Color.parseColor("#ffffff"));
                    textView34.setTextColor(Color.parseColor("#c8c8c8"));
                    textView18.setBackgroundResource(R.drawable.bg_write);
                    textView20.setBackgroundResource(R.drawable.bg_write);
                    textView23.setBackgroundResource(R.drawable.bg_write);
                    textView24.setBackgroundResource(R.drawable.bg_write);
                    textView25.setBackgroundResource(R.drawable.bg_write);
                    textView26.setBackgroundResource(R.drawable.bg_write);
                    textView28.setBackgroundResource(R.drawable.bg_write);
                    textView29.setBackgroundResource(R.drawable.bg_write);
                    textView30.setBackgroundResource(R.drawable.bg_write);
                    textView31.setBackgroundResource(R.drawable.bg_write);
                    textView32.setBackgroundResource(R.drawable.bg_write);
                    textView33.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    textView34.setBackgroundResource(R.drawable.bg_write);
                    dateItem = 11;
                    dateRequest(dateItem, 1);
                } else if (i == textView34.getId()) {
                    textView18.setTextColor(Color.parseColor("#c8c8c8"));
                    textView20.setTextColor(Color.parseColor("#c8c8c8"));
                    textView23.setTextColor(Color.parseColor("#c8c8c8"));
                    textView24.setTextColor(Color.parseColor("#c8c8c8"));
                    textView25.setTextColor(Color.parseColor("#c8c8c8"));
                    textView26.setTextColor(Color.parseColor("#c8c8c8"));
                    textView28.setTextColor(Color.parseColor("#c8c8c8"));
                    textView29.setTextColor(Color.parseColor("#c8c8c8"));
                    textView30.setTextColor(Color.parseColor("#c8c8c8"));
                    textView31.setTextColor(Color.parseColor("#c8c8c8"));
                    textView32.setTextColor(Color.parseColor("#c8c8c8"));
                    textView33.setTextColor(Color.parseColor("#c8c8c8"));
                    textView34.setTextColor(Color.parseColor("#ffffff"));
                    textView18.setBackgroundResource(R.drawable.bg_write);
                    textView20.setBackgroundResource(R.drawable.bg_write);
                    textView23.setBackgroundResource(R.drawable.bg_write);
                    textView24.setBackgroundResource(R.drawable.bg_write);
                    textView25.setBackgroundResource(R.drawable.bg_write);
                    textView26.setBackgroundResource(R.drawable.bg_write);
                    textView28.setBackgroundResource(R.drawable.bg_write);
                    textView29.setBackgroundResource(R.drawable.bg_write);
                    textView30.setBackgroundResource(R.drawable.bg_write);
                    textView31.setBackgroundResource(R.drawable.bg_write);
                    textView32.setBackgroundResource(R.drawable.bg_write);
                    textView33.setBackgroundResource(R.drawable.bg_write);
                    textView34.setBackgroundResource(R.drawable.bg_radius12_color_themered);
                    dateItem = 12;
                    dateRequest(dateItem, 1);
                }
            }

        });


        pageview = new ArrayList<View>();
        //添加想要切换的界面
        pageview.add(view1);
        pageview.add(view2);
        //数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {

            @Override
            //获取当前窗体界面数
            public int getCount() {
                // TODO Auto-generated method stub
                return pageview.size();
            }

            @Override
            //判断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Object o = pageview.get(position);
                container.addView((View) o);
                return o;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

        };
        //绑定适配器
        viewPager.setAdapter(mPagerAdapter);
        //设置viewPager的初始界面为第一个界面
        viewPager.setCurrentItem(0);
        //添加切换界面的监听器
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        // 获取滚动条的宽度
        bmpW = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher).getWidth();
        //为了获取屏幕宽度，新建一个DisplayMetrics对象
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //将当前窗口的一些信息放在DisplayMetrics类中
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //得到屏幕的宽度
        int screenW = displayMetrics.widthPixels;
        //计算出滚动条初始的偏移量
        offset = (screenW / 2 - bmpW) / 2;
        //计算出切换一个界面时，滚动条的位移量
        one = offset * 2 + bmpW;
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);


        date_tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
                isChange = true;
                dateRequest(0, 1);

                ColorStateList white = getApplicationContext().getResources().getColorStateList(R.color.white);
                date_tv_change.setTextColor(white);
                ColorStateList gray = getApplicationContext().getResources().getColorStateList(R.color.gray);
                notice_tv_change.setTextColor(gray);

            }
        });
        notice_tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
                isChange = true;
                noticeRequest(noticeItem, 1);
                ColorStateList white = getApplicationContext().getResources().getColorStateList(R.color.white);
                notice_tv_change.setTextColor(white);
                ColorStateList gray = getApplicationContext().getResources().getColorStateList(R.color.gray);
                date_tv_change.setTextColor(gray);
            }
        });


//


        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MoneyActivity.class));
            }
        });
        myorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MyOrder.class);
                startActivity(intent);
            }
        });


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CSCustomServiceInfo.Builder builder = new CSCustomServiceInfo.Builder();
//                CSCustomServiceInfo info = builder
//                        .nickName(App.getApp().getUserName())
//                        .userId(App.getApp().getUserID())
//                        .city(App.getApp().getCity())
//                        .age(App.getApp().getM_age()+"")
//                        .mobileNo(App.getApp().getphone()).build();
//                RongIM.getInstance().startCustomerServiceChat(HomeActivity.this,"KEFU149163815211138","在线客服",info);


                startActivity(new Intent(HomeActivity.this, SettingActivity.class));

            }
        });
        sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLeftLayout();
            }
        });

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRightLayout();


            }
        });


        sharetofriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int m_patner = SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_patner();
                if (m_patner == 0) {
                    showShare();
                } else if (m_patner == 1) {
                    String m_invite_code = String.valueOf(SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_account());
                    LogDebug.err("m_invite_code" + m_invite_code);
                    Intent intent1 = new Intent(HomeActivity.this, ShareActivity.class);
                    intent1.putExtra("code", m_invite_code);
                    startActivity(intent1);
                }

            }
        });

        fab_btn.setOnClickListener(publishSkillListener);


        dateRequest(dateItem, datepageNum);
    }


    /**
     * 发布技能事件
     */
    View.OnClickListener publishSkillListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
//            v.setEnabled(false);

//            LogDebug.err("" + App.getApp().getModel_state());
//            // TODO 判断模特状态 要改
//            int state = App.getApp().getM_video_state();
//            if (state == 0) {
//                // 视频未认证
//                DialogUtils.videoCertification(HomeActivity.this);
//            } else if (state == 1) {
//                // 视频认证中
//                ToastUtils.getInstance(getBaseContext()).showText("视频认证中...");
//            } else if (App.getApp().getModel_state() == 0) {
//                /*state 0 资料不完整状态*/
//                View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_tishi, null);
//                final PopupWindow popupWindow = new PopupWindow();
//                popupWindow.setContentView(view);
//                double screenHeight = ScreenUtils.getScreenHeight(HomeActivity.this);
//                double screenWidth = ScreenUtils.getScreenWidth(HomeActivity.this);
//                //高度设置为屏幕的0.3
//                int height = (int) (screenHeight * 0.2);
//                //宽度设置为屏幕的0.5
//                int width = (int) (screenWidth * 0.8);
//                popupWindow.setWidth(width);
//                popupWindow.setHeight(height);
//                popupWindow.setOutsideTouchable(true);
//                popupWindow.setFocusable(true);
//
//                popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = (float) 0.4; //0.0-1.0
//                getWindow().setAttributes(lp);
//
//                TextView wanshan_tv = (TextView) view.findViewById(R.id.wanshan_tv);
//                TextView quxiao = (TextView) view.findViewById(R.id.quxiao);
//                quxiao.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        popupWindow.dismiss();
//                        WindowManager.LayoutParams lp1 = getWindow().getAttributes();
//                        lp1.alpha = (float) 1; //0.0-1.0
//                        getWindow().setAttributes(lp1);
//                    }
//                });
//                wanshan_tv.setClickable(true);
//                wanshan_tv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent intent1 = new Intent(HomeActivity.this, EditActivity.class);
//                        startActivity(intent1);
//                        popupWindow.dismiss();
//                        WindowManager.LayoutParams lp1 = getWindow().getAttributes();
//                        lp1.alpha = (float) 1; //0.0-1.0
//                        getWindow().setAttributes(lp1);
//                    }
//                });
//            } else if (TextUtils.isEmpty(App.getApp().getphone())) {
//                // 未绑定手机号
//                DialogUtils.phoneBindingAlert(HomeActivity.this);
//            } else {
//                Intent intent = new Intent(HomeActivity.this, ReleaseSkill.class);
//                startActivity(intent);
//            }

//            if (TextUtils.isEmpty(App.getApp().getphone())) {
//                // 未绑定手机号
//                DialogUtils.phoneBindingAlert(HomeActivity.this);
//                return;
//            }

            Map map = new HashMap();
            map.put("member_id",SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_head_photo());
            Xutils.post(Url.getBeforeAddSkill, map, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        if (!"200".equals(code)) {
                            showDialogRz(code, jsonObject.getString("tips"));
                            return;
                        }
                        Intent intent = new Intent(HomeActivity.this, ReleaseSkill.class);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    ToastUtils.getInstance(HomeActivity.this).showText("网络忙");
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    ToastUtils.getInstance(HomeActivity.this).showText("请求被取消");
                }

                @Override
                public void onFinished() {
                    v.setEnabled(true);
                }
            });
        }
    };

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

    //高斯模糊
    private void applyBlur() {
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        /**
         * 获取当前窗口快照，相当于截屏
         */
        Bitmap bmp1 = view.getDrawingCache();
        int height = getOtherHeight();
        /**
         * 除去状态栏和标题栏
         */
        Bitmap bmp2 = Bitmap.createBitmap(bmp1, 0, height, bmp1.getWidth(), bmp1.getHeight() - height);
        blur(bmp2, view);
    }

    @SuppressLint("NewApi")
    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;//图片缩放比例；
        float radius = 20;//模糊程度

        Bitmap overlay = Bitmap.createBitmap(
                (int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);


        overlay = FastBlur.doBlur(overlay, (int) radius, true);
//        view.setBackground(new BitmapDrawable(getResources(), overlay));
//        releaseRl.setBackground(new BitmapDrawable(getResources(), overlay));
        /**
         * 打印高斯模糊处理时间，如果时间大约16ms，用户就能感到到卡顿，时间越长卡顿越明显，如果对模糊完图片要求不高，可是将scaleFactor设置大一些。
         */
        Log.e("jerome", "blur time:" + (System.currentTimeMillis() - startMs));
    }

    /**
     * 获取系统状态栏和软件标题栏，部分软件没有标题栏，看自己软件的配置；
     *
     * @return
     */
    private int getOtherHeight() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentTop - statusBarHeight;
        return statusBarHeight + titleBarHeight;
    }


    public void startDateAnimotion() {
        dateadapter.notifyDataSetChanged();
//        View childAt = data_listview.getChildAt(0);
//        deletePattern(childAt, position);
    }


    private void deletePattern(final View view, final int position) {

        LogDebug.err("------------11111111111111");
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                map_dateitem.remove(position);
                dateadapter.notifyDataSetChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        collapse(view, al);

    }

    private void collapse(final View view, Animation.AnimationListener al) {
        final int originHeight = view.getMeasuredHeight();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                LogDebug.err("------------22222");
                if (interpolatedTime == 1.0f) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = originHeight - (int) (originHeight * interpolatedTime);
                    view.requestLayout();
                }
                LogDebug.err("------------3333333");
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        if (al != null) {
            animation.setAnimationListener(al);
        }
        animation.setDuration(500);
        view.startAnimation(animation);
        LogDebug.err("------------44444");
    }

    private ImageView image_icon;
    private TextView textView1;
    private TextView textView2;

    private void startMenuAnimation() {
        if (actionButton == null) {
            image_icon = new ImageView(HomeActivity.this);
            image_icon.setImageResource(R.drawable.btn_jiedanzhong);

            image_icon.setScaleType(ImageView.ScaleType.FIT_XY);
            FloatingActionButton.LayoutParams layoutParams2 = new FloatingActionButton.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            actionButton = new FloatingActionButton.Builder(HomeActivity.this)
                    .setContentView(image_icon, layoutParams2)
                    .build();
            actionButton.setBackgroundResource(R.color.wubeijingse);


            /** 设置旋转动画 */

            rotanimation.setDuration(1000);//设置动画持续时间
            rotanimation.setRepeatCount(-1);//设置重复次数
            LinearInterpolator lir = new LinearInterpolator();
            rotanimation.setInterpolator(lir);
            image_icon.setAnimation(rotanimation);


            SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
            // repeat many times:
            textView1 = new TextView(HomeActivity.this);
            textView1.setText("模式");
            textView1.setTextSize(12);
            textView1.setTextColor(Color.WHITE);
            textView1.setGravity(Gravity.CENTER);
            SubActionButton button1 = itemBuilder.setContentView(textView1).build();
            button1.setBackgroundResource(R.drawable.btn_home_mode_item);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            button1.setLayoutParams(layoutParams);
            button1.setOnClickListener(publishSkillListener);

            textView2 = new TextView(HomeActivity.this);
            textView2.setText("停止");
            textView2.setTextSize(12);
            textView2.setTextColor(Color.WHITE);
            textView2.setGravity(Gravity.CENTER);
            final SubActionButton button2 = itemBuilder.setContentView(textView2).build();
            button2.setBackgroundResource(R.drawable.btn_home_item);
            button2.setLayoutParams(layoutParams);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    v.setEnabled(false);
                    if (SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_work_state() == 2) {
                        requetWorkState(0, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject object = new JSONObject(result);
                                    String code = object.getString("code");
                                    if ("200".equals(code)) {
                                        SpDataCache.saveModelWorkState(0);
                                        rotanimation.cancel();
                                        textView2.setText("开始");
                                    } else {
                                        String errmsg = object.getString("tips");
                                        ToastUtils.getInstance(HomeActivity.this).showText(errmsg);
                                    }
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
                                v.setEnabled(true);
                            }
                        });
                    } else {
                        requetWorkState(2, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject object = new JSONObject(result);
                                    String code = object.getString("code");
                                    if ("200".equals(code)) {
                                        SpDataCache.saveModelWorkState(2);
                                        rotanimation.start();
                                        textView2.setText("停止");
                                    } else {
                                        String errmsg = object.getString("tips");
                                        ToastUtils.getInstance(HomeActivity.this).showText(errmsg);
                                    }
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
                                v.setEnabled(true);
                            }
                        });
                    }
                }
            });

            FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                    .addSubActionView(button2)
                    .setStartAngle(190)
                    .addSubActionView(button1)
                    .setEndAngle(250)
                    .attachTo(actionButton)
                    .build();

        }

        LogDebug.err(work_state + "");
        if (work_state == 0) {
            if (rotanimation != null) {
                rotanimation.cancel();
            }
            textView2.setText("开始");
        } else if (work_state == 2) {
            textView2.setText("停止");
            if (rotanimation != null) {
                rotanimation.start();
            }
        }

    }

    private void requetWorkState(int tag) {
        requetWorkState(tag, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.err("状态改成功");
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
    }

    private void requetWorkState(int tag, Callback.CommonCallback callback) {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_head_photo());
        map.put("m_work_state", tag);
        LogDebug.err(tag + "---");
        Xutils.post(Url.workState, map, callback);
    }

    /**
     * 获取当前程序的版本号
     */
    private String getVersionName() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionName;
    }


    /**
     * 分享好友
     */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();

        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        oks.addHiddenPlatform(WechatFavorite.NAME);
//        oks.addHiddenPlatform(QQ.NAME);
//        oks.addHiddenPlatform(QZone.NAME);
//        oks.addHiddenPlatform(SinaWeibo.NAME);
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("猎模：让你的颜值更有价值");

        String url = Url.HOST + "h55/H5m/html/downAppModal.html";
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setImageUrl("http://liemo-test.oss-cn-qingdao.aliyuncs.com/HomeImage/logo.png");
        oks.setText("一个高颜值模特打造的专属工作平台");
        oks.setExecuteUrl(url);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

        // 启动分享GUI
        oks.show(this);
    }


    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    /**
                     * TranslateAnimation的四个属性分别为
                     * float fromXDelta 动画开始的点离当前View X坐标上的差值
                     * float toXDelta 动画结束的点离当前View X坐标上的差值
                     * float fromYDelta 动画开始的点离当前View Y坐标上的差值
                     * float toYDelta 动画开始的点离当前View Y坐标上的差值
                     **/
                    animation = new TranslateAnimation(one, 0, 0, 0);
                    ColorStateList white = getApplicationContext().getResources().getColorStateList(R.color.white);
                    date_tv_change.setTextColor(white);
                    ColorStateList gray = getApplicationContext().getResources().getColorStateList(R.color.gray);
                    notice_tv_change.setTextColor(gray);
                    break;
                case 1:
                    animation = new TranslateAnimation(offset, one, 0, 0);
                    ColorStateList white1 = getApplicationContext().getResources().getColorStateList(R.color.white);
                    notice_tv_change.setTextColor(white1);
                    ColorStateList gray1 = getApplicationContext().getResources().getColorStateList(R.color.gray);
                    date_tv_change.setTextColor(gray1);
                    break;
            }
            //arg0为切换到的页的编码
            currIndex = arg0;
            // 将此属性设置为true可以使得图片停在动画结束时的位置
            animation.setFillAfter(true);
            //动画持续时间，单位为毫秒
            animation.setDuration(200);
            //滚动条开始动画
            // scrollbar.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }


    public void initLeftLayout() {

        main_drawer_layout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        //设置透明
        main_drawer_layout.setScrimColor(0x00000000);
        //左边菜单
        left_menu_layout = (RelativeLayout) findViewById(R.id.main_left_drawer_layout);
        View view2 = getLayoutInflater().inflate(R.layout.leftmenu_layout, null);
        leftmenuicondk = (ImageView) view2.findViewById(R.id.leftmenuicondk);
        leftmenuicondk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, DetailActivity.class));
            }
        });
        setting = (LinearLayout) view2.findViewById(R.id.setting);
        money = (LinearLayout) view2.findViewById(R.id.money);
        myorder = (LinearLayout) view2.findViewById(R.id.myorder);
        sharetofriend = (LinearLayout) view2.findViewById(R.id.sharetofriend);
        // headPhoto = (ImageView) view2.findViewById(R.id.imageView5);
        Xutils.setCircularIcon(leftmenuicondk, SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_head_photo());
        nickName = (TextView) view2.findViewById(R.id.name);
        String userName = SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_nick_name();
        nickName.setText(userName);


        TextView app_version = (TextView) view2.findViewById(R.id.app_version);
        try {
            String versionName = getVersionName();
            app_version.setText("v" + versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView xiugai = (ImageView) view2.findViewById(R.id.img_xiugai);
        xiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EditActivity.class);
                intent.putExtra("from", "home");
                startActivity(intent);
            }
        });
        left_menu_layout.addView(view2);
    }

    public void initRightLayout() {

        //右边菜单
        main_drawer_layout.openDrawer(Gravity.RIGHT);
        main_drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                Gravity.RIGHT);
        right_xiaoxi_layout = (RelativeLayout) findViewById(R.id.main_right_drawer_layout);

        View view = View.inflate(HomeActivity.this, R.layout.rightmenu_layout, null);
        frameLayout = (FrameLayout) view.findViewById(R.id.subconversationlist);
        right_xiaoxi_layout.addView(view);


    }

    private void initEvent() {

        drawerbar = new ActionBarDrawerToggle(this, main_drawer_layout, new Toolbar(this), R.string.open, R.string.close) {

            //菜单打开

            @Override

            public void onDrawerOpened(View drawerView) {


                super.onDrawerOpened(drawerView);

            }

            // 菜单关闭

            @Override

            public void onDrawerClosed(View drawerView) {


                super.onDrawerClosed(drawerView);

            }

        };

//        main_drawer_layout.setDrawerListener(drawerbar);
        main_drawer_layout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = main_drawer_layout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;

                if (drawerView.getTag().equals("LEFT")) {

                    ViewHelper.setTranslationX(mContent,
                            mMenu.getMeasuredWidth() * (1 - scale));


                } else {
                    ViewHelper.setTranslationX(mContent,
                            -mMenu.getMeasuredWidth() * slideOffset);
                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }
        });

    }
    //左边菜单开关事件

    public void openLeftLayout() {
        LogDebug.err("------------------");
        if (!main_drawer_layout.isDrawerOpen(left_menu_layout)) {
            mengban.setVisibility(View.VISIBLE);
            main_drawer_layout.openDrawer(left_menu_layout);
        } else {
            mengban.setVisibility(View.GONE);
            main_drawer_layout.closeDrawer(left_menu_layout);
        }
    }
    // 右边菜单开关事件

    public void openRightLayout() {

        if (main_drawer_layout.isDrawerOpen(right_xiaoxi_layout)) {
            main_drawer_layout.closeDrawer(right_xiaoxi_layout);
            mengban.setVisibility(View.GONE);
        } else {
            main_drawer_layout.openDrawer(right_xiaoxi_layout);
            if (RongIM.getInstance() != null) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                ConversationListActivity conversationListActivity = new ConversationListActivity();
                fragmentTransaction.replace(R.id.messagelist, conversationListActivity);
                fragmentTransaction.commit();
                LogDebug.err("openrightlayout");
            }
            mengban.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (main_drawer_layout.isDrawerOpen(android.view.Gravity.LEFT)) {
                main_drawer_layout.closeDrawer(android.view.Gravity.LEFT);
                return true;
            }

            if (main_drawer_layout.isDrawerOpen(Gravity.RIGHT)) {
                main_drawer_layout.closeDrawer(Gravity.RIGHT);
                return true;
            }

            if ((System.currentTimeMillis() - exitTime) >= 1000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                App.getApp().exit();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        LoginAgain();
    }

    /**
     * 约会筛选开始
     */
    long dateTimeStamp;

    public void dateRequest(final int item, final int pageNumber) {

        if (isChange) {
            map_dateitem.clear();
            dateadapter.notifyDataSetChanged();
            LogDebug.err("ischange");
        }
        lat = SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_gps_lat();
        lon = SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_gps_long();
        LogDebug.err("item-" + lat + "-" + lon);
        Map<String, Object> map = new HashMap<>();
        map.put("pageNumber", pageNumber);
        map.put("pageSize", 5);
        map.put("invite_type", 1);
        map.put("userid", SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_head_photo());
        map.put("gps", lon + "," + lat);
        map.put("invite_item", item);
        LogDebug.err("item-" + item);

        if (pageNumber < 2) {
            dateTimeStamp = System.currentTimeMillis();
        }
        map.put("timeStamp", dateTimeStamp);

        Xutils.post(Url.chose, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (pageNumber < 2) {
                    map_dateitem.clear();
                }
                try {
                    Log.e("aaa", "约会：" + result);
                    if (isFirst) {
                        isFirst = false;
                    }

                    JSONObject jsonObject = new JSONObject(result);
                    String codenum = jsonObject.getString("code");
                    int code = Integer.parseInt(codenum);
                    if (code == 200) {
                        JSONObject data = (JSONObject) jsonObject.get("data");
                        JSONArray list = (JSONArray) data.get("list");
                        if (list == null) {
                            LogDebug.err("空数据呀大哥。。。。。");
                            return;
                        }
                        if (!(list.length() > 0)) {
                            toast.showText("没有更多了");
                            if (datepageNum == 1) {
                                return;
                            } else {
                                datepageNum--;
                            }
                            return;
                        }
                        data_listview.setVisibility(View.VISIBLE);
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject jsonObject_every = list.getJSONObject(i);

                            String u_nick_name = jsonObject_every.getString("u_nick_name");
                            String end_time = jsonObject_every.getString("end_time");
                            String invite_address = jsonObject_every.getString("shoparea");
                            String start_time = jsonObject_every.getString("start_time");
                            String invite_cover = jsonObject_every.getString("invite_cover");
                            String reward_price = jsonObject_every.getString("invite_money");
                            String u_head_photo = jsonObject_every.getString("u_head_photo");
                            String invite_item = jsonObject_every.getString("invite_item");
                            String u_id = jsonObject_every.getString("u_id");
                            String invite_id = jsonObject_every.getString("invite_id");
                            String u_age = jsonObject_every.getString("u_age");
                            int itemType = Integer.parseInt(invite_item);

                            String dateDay = jsonObject_every.getString("time");

                            DateEvery dateEvery = new DateEvery();
                            String time = start_time + "-" + end_time;
                            if (!TextUtils.isEmpty(dateDay)) {
                                int first = dateDay.indexOf("-");
                                if (first != -1 && first + 1 < dateDay.length()) {
                                    dateDay = dateDay.substring(first + 1, dateDay.length());
                                }
                                time = dateDay + " " + time;
                            }
                            Log.e("time123", time);
                            dateEvery.setDatetime(time);
                            dateEvery.setAge(u_age);
                            dateEvery.setDatecover(invite_cover);
                            dateEvery.setDatelocation(invite_address);
                            dateEvery.setDatemoney(reward_price);
                            dateEvery.setDatenickname(u_nick_name);

                            dateEvery.setDateicon(u_head_photo);
                            dateEvery.setUid(u_id);
                            dateEvery.setInviteId(invite_id);

                            LogDebug.err("invite_title: " + itemType);

                            if (itemType == 1) {
                                dateEvery.setDatetype_iv(R.drawable.icon_date_1);
                                dateEvery.setDateType_img(R.drawable.img_date_1);
                            } else if (itemType == 2) {
                                dateEvery.setDatetype_iv(R.drawable.icon_date_2);
                                dateEvery.setDateType_img(R.drawable.img_date_2);
                            } else if (itemType == 3) {
                                dateEvery.setDatetype_iv(R.drawable.icon_date_3);
                                dateEvery.setDateType_img(R.drawable.img_date_3);
                            } else if (itemType == 4) {
                                dateEvery.setDatetype_iv(R.drawable.icon_date_4);
                                dateEvery.setDateType_img(R.drawable.img_date_4);
                            } else if (itemType == 5) {
                                dateEvery.setDatetype_iv(R.drawable.icon_date_5);
                                dateEvery.setDateType_img(R.drawable.img_date_5);
                            } else if (itemType == 6) {
                                dateEvery.setDatetype_iv(R.drawable.icon_date_6);
                                dateEvery.setDateType_img(R.drawable.img_date_6);
                            } else if (itemType == 7) {
                                dateEvery.setDatetype_iv(R.drawable.icon_date_7);
                                dateEvery.setDateType_img(R.drawable.img_date_7);
                            } else if (itemType == 8) {
                                dateEvery.setDatetype_iv(R.drawable.icon_date_9);
                                dateEvery.setDateType_img(R.drawable.img_date_9);
                            } else if (itemType == 9) {
                                dateEvery.setDatetype_iv(R.drawable.icon_date_8);
                                dateEvery.setDateType_img(R.drawable.img_date_8);
                            } else if (itemType == 10) {
                                dateEvery.setDatetype_iv(R.drawable.icon_date_10);
                                dateEvery.setDateType_img(R.drawable.img_date_10);
                            } else if (itemType == 11) {
                                dateEvery.setDatetype_iv(R.drawable.icon_date_11);
                                dateEvery.setDateType_img(R.drawable.img_date_11);
                            } else if (itemType == 12) {
                                dateEvery.setDatetype_iv(R.drawable.icon_date_12);
                                dateEvery.setDateType_img(R.drawable.img_date_12);
                            }

                            map_dateitem.add(dateEvery);
                        }

                        dateadapter.notifyDataSetChanged();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogDebug.err("Throwable ex--出错" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                data_listview.onRefreshComplete();
            }
        });
    }

    public void dateRefresh() {
        dateadapter.notifyDataSetChanged();
        toast.showText("报名成功");
    }

    public void notiRefresh() {
        notiadapter.notifyDataSetChanged();
        toast.showText("报名成功");
    }

    /**
     * 通告筛选开始
     */
    long noticeTimeStamp;

    public void noticeRequest(final int noitem, final int pageNum) {
        if (isChange) {
            map_noticeitem.clear();
            notiadapter.notifyDataSetChanged();
            LogDebug.err("ischange");
        }
        String lat = SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_gps_lat();
        String lon = SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_gps_long();
        final Map<String, Object> map = new HashMap<>();
        map.put("pageNumber", pageNum);
        map.put("pageSize", 5);
        map.put("invite_type", 2);
        map.put("userid", SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_head_photo());
        map.put("gps", lon + "," + lat);
        map.put("invite_item", noitem);

        if (pageNum < 2) {
            noticeTimeStamp = System.currentTimeMillis();
        }
        map.put("timeStamp", noticeTimeStamp);

        Xutils.post(Url.chose, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                if (pageNum < 2) {
                    map_noticeitem.clear();
                }

                try {
                    LogDebug.err("通告 result" + noitem + "--" + result);
                    notice_listview.setVisibility(View.VISIBLE);
                    JSONObject jsonObject = new JSONObject(result);
                    String codenum = jsonObject.getString("code");
                    int code = Integer.parseInt(codenum);
                    if (code == 200) {
                        JSONObject data = (JSONObject) jsonObject.get("data");
                        JSONArray list = (JSONArray) data.get("list");

                        if (!(list.length() > 0)) {
                            toast.showText("没有更多了");
                            if (datepageNum == 1) {
                                return;
                            } else {
                                datepageNum--;
                            }
                            return;
                        }

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject jsonObject_every = list.getJSONObject(i);

                            String invite_title = jsonObject_every.getString("invite_title");
                            String reward_price = jsonObject_every.getString("invite_money");
                            String u_nick_name = jsonObject_every.getString("u_nick_name");
                            String send_time = jsonObject_every.getString("send_time");
                            String end_time = jsonObject_every.getString("end_time");
                            String invite_address = jsonObject_every.getString("shoparea");
                            String start_time = jsonObject_every.getString("start_time");
                            String invite_cover = jsonObject_every.getString("invite_cover");
                            String u_head_photo = jsonObject_every.getString("u_head_photo");
                            String invite_item = jsonObject_every.getString("invite_item");
//                            jsonObject_every.getString("")
                            String u_id = jsonObject_every.getString("u_id");
                            String invite_id = jsonObject_every.getString("invite_id");

                            int notitem = Integer.parseInt(invite_item);
                            LogDebug.err("notitem----" + noitem);

                            String dateDay = jsonObject_every.getString("time");

                            NoticeEvery noticeItem = new NoticeEvery();
                            noticeItem.setNoticelocation(invite_address);
                            noticeItem.setNoticecover(invite_cover);
                            noticeItem.setNoticemoney(reward_price);
                            String noticetime = start_time + "-" + end_time;
                            if (!TextUtils.isEmpty(dateDay)) {
                                int first = dateDay.indexOf("-");
                                if (first != -1 && first + 1 < dateDay.length()) {
                                    dateDay = dateDay.substring(first + 1, dateDay.length());
                                }
                                noticetime = dateDay + " " + noticetime;
                            }
                            noticeItem.setNoticetime(noticetime);
                            noticeItem.setNoticeicon(u_head_photo);
                            noticeItem.setNoticename(u_nick_name);
                            noticeItem.setInviteId(invite_id);
                            noticeItem.setUid(u_id);


                            if (notitem == 14) {
                                noticeItem.setNoticetype_iv(R.drawable.icon_ann_14);
                                noticeItem.setNotice_img(R.drawable.img_ann_14);
                            } else if (notitem == 15) {
                                noticeItem.setNoticetype_iv(R.drawable.icon_ann_15);
                                noticeItem.setNotice_img(R.drawable.img_ann_15);
                            } else if (notitem == 16) {
                                noticeItem.setNoticetype_iv(R.drawable.icon_ann_16);
                                noticeItem.setNotice_img(R.drawable.img_ann_16);
                            } else if (notitem == 17) {
                                noticeItem.setNoticetype_iv(R.drawable.icon_ann_17);
                                noticeItem.setNotice_img(R.drawable.img_ann_17);
                            } else if (notitem == 18) {
                                noticeItem.setNoticetype_iv(R.drawable.icon_ann_18);
                                noticeItem.setNotice_img(R.drawable.img_ann_18);
                            }
                            map_noticeitem.add(noticeItem);

                        }

                        notiadapter.notifyDataSetChanged();


                    }
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
                notice_listview.onRefreshComplete();
            }
        });

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {


        } else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {


        }

        return false;
    }

    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    // 约会
    private class DateitemAdapter extends BaseAdapter {


        private LayoutInflater layoutInflater;
        private Boolean isCircluar = true;


        public DateitemAdapter() {

            this.layoutInflater = LayoutInflater.from(HomeActivity.this);


        }

        @Override
        public int getCount() {
            return map_dateitem.size();
        }

        @Override
        public DateEvery getItem(int position) {
            return map_dateitem.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final DateEvery object = map_dateitem.get(position);
            final ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.dateitem, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.datename.setText(object.getDatenickname());
//         x.image().bind(holder.datecover,object.getDatecover());
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setCircular(isCircluar)
                    .setCrop(true)
                    .build();
            x.image().bind(holder.dateicon, object.getDateicon(), imageOptions);
            holder.dateicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                    intent.putExtra("from", "home");
                    intent.putExtra("userid", object.getUid());
                    startActivity(intent);
                }
            });
            holder.datesex.setText(object.getAge());
            holder.datetime.setText(object.getDatetime());
            holder.datelocation.setText(object.getDatelocation());
            holder.datemoney.setText(object.getDatemoney());
            holder.datecover.setImageResource(object.getDateType_img());
            holder.dateapply.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View v) {

                    String inviteId = object.getInviteId();
                    String uid = object.getUid();
                    showDialog(inviteId, uid, holder.dateapply, 1, position);
                }
            });
            return convertView;
        }


        protected class ViewHolder {


            private RelativeLayout relativeLayout14;
            private ImageView datecover;
            private ImageView datetypeIv;
            private TextView datetypeTv;
            private RelativeLayout relativeLayout18;
            private ImageView dateicon;
            private TextView datename;
            private TextView datesex;
            private LinearLayout timelayout;
            private TextView datetime;
            private LinearLayout locationlayout;
            private TextView datelocation;
            private TextView datemoney;
            private TextView dateapply;
            private final ImageView type_photo;

            public ViewHolder(View view) {
                relativeLayout14 = (RelativeLayout) view.findViewById(R.id.relativeLayout14);
                datecover = (ImageView) view.findViewById(R.id.datecover);
                relativeLayout18 = (RelativeLayout) view.findViewById(R.id.relativeLayout18);
                dateicon = (ImageView) view.findViewById(R.id.dateicon);
                datename = (TextView) view.findViewById(R.id.datename);
                datesex = (TextView) view.findViewById(R.id.datesex);
                timelayout = (LinearLayout) view.findViewById(R.id.timelayout);
                datetime = (TextView) view.findViewById(R.id.datetime);
                locationlayout = (LinearLayout) view.findViewById(R.id.locationlayout);
                datelocation = (TextView) view.findViewById(R.id.datelocation);
                datemoney = (TextView) view.findViewById(R.id.datemoney);
                dateapply = (TextView) view.findViewById(R.id.dateapply);
                type_photo = (ImageView) view.findViewById(R.id.type_photo);
            }
        }
    }

    // 通告
    public class NoticeitemAdapter extends BaseAdapter {

        private List<NoticeEvery> objects = new ArrayList<NoticeEvery>();

        private LayoutInflater layoutInflater;

        private Boolean isCircluar = true;
        private int curPosition;
        private View viewItem;
        private final TranslateAnimation animation;

        public NoticeitemAdapter(List<NoticeEvery> objects) {
            this.layoutInflater = LayoutInflater.from(HomeActivity.this);
            this.objects = objects;
            animation = new TranslateAnimation(0, 150, 0, 0);
            animation.setDuration(1000);//设置动画持续时间
            animation.setRepeatMode(Animation.REVERSE);//设置反方向执行
        }

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public NoticeEvery getItem(int position) {
            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            curPosition = position;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.noticeitem, null);
                convertView.setTag(new NoticeitemAdapter.ViewHolder(convertView));
            }
            initializeViews((NoticeEvery) getItem(position), (ViewHolder) convertView.getTag());
            viewItem = parent.getChildAt(position);
//        viewItem.setAnimation(animation);
            return convertView;
        }

        private void initializeViews(final NoticeEvery object, final ViewHolder holder) {
            holder.noticename.setText(object.getNoticename());
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setCircular(isCircluar)
                    .setCrop(true)
                    .build();
//         x.image().bind(holder.noticecover,object.getNoticecover());

            x.image().bind(holder.noticeicon, object.getNoticeicon(), imageOptions);
            holder.noticeicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                    intent.putExtra("from", "home");
                    intent.putExtra("userid", object.getUid());
                    startActivity(intent);
                }
            });
            holder.noticecover.setImageResource(object.getNotice_img());
            holder.noticetime.setText(object.getNoticetime());
            holder.noticelocation.setText(object.getNoticelocation());
            holder.noticeprice.setText(object.getNoticemoney());
            holder.noticeapply.setTag(curPosition);
            holder.noticeapply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Log.e("aaa", "报名");

                    Map<String, Object> map = new HashMap<>();
                    String inviteId = object.getInviteId();
                    String uid = object.getUid();
                    showDialog(inviteId, uid, holder.noticeapply, 2);
                }
            });
            holder.li.setImageResource(object.getNoticetype_iv());
        }


        protected class ViewHolder {
            private ImageView li;
            private RelativeLayout relativeLayout14;
            private RelativeLayout relativeLayout18;
            private LinearLayout timelayout;
            private LinearLayout locationlayout;
            private TextView noticetypeTv;
            private ImageView noticeicon;
            private TextView noticename;
            private TextView sex;
            private TextView noticetime;

            private TextView noticelocation;
            private TextView noticeprice;
            private TextView noticeapply;
            private ImageView noticecover;
            private ImageView noticetypeIv;
            private final ImageView notice_photo;


            public ViewHolder(View view) {
                relativeLayout14 = (RelativeLayout) view.findViewById(R.id.relativeLayout14);
                li = (ImageView) view.findViewById(R.id.li);
                relativeLayout18 = (RelativeLayout) view.findViewById(R.id.relativeLayout18);
                timelayout = (LinearLayout) view.findViewById(R.id.timelayout);
                locationlayout = (LinearLayout) view.findViewById(R.id.locationlayout);

                noticecover = (ImageView) view.findViewById(R.id.noticecover);
                // noticetypeIv = (ImageView) view.findViewById(R.id.noticetype_iv);
                //  noticetypeTv = (TextView) view.findViewById(R.id.noticetype_tv);
                noticeicon = (ImageView) view.findViewById(R.id.noticeicon);
                noticename = (TextView) view.findViewById(R.id.noticename);
                sex = (TextView) view.findViewById(R.id.sex);
                noticetime = (TextView) view.findViewById(R.id.noticetime);
                noticelocation = (TextView) view.findViewById(R.id.noticelocation);
                noticeprice = (TextView) view.findViewById(R.id.noticeprice);
                noticeapply = (TextView) view.findViewById(R.id.noticeapply);
                notice_photo = (ImageView) view.findViewById(R.id.notice_photo);
            }
        }
    }

    /**
     * 报名显示dialog
     */
    public void showDialog(final String inviteId, final String uId, final TextView dateapplay, final int type, final int deletePosition) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("确定要报名么");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
                if (TextUtils.isEmpty( SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_mobile())) {
                    DialogUtils.phoneBindingAlert(HomeActivity.this);
                    return;
                }
                // TODO Auto-generated method stub
                final Map<String, Object> map = new HashMap<>();
                map.put("inviteid", inviteId);
                map.put("invite_uid", uId);
                map.put("enroll_uid", SpDataCache.getSelfInfo(HomeActivity.this).getData().getM_head_photo());
                dateapplay.setClickable(false);
                Xutils.post(Url.apply_invite, map, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        LogDebug.err("报名：" + result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String code = jsonObject.getString("code");
                            if (!"200".equals(code)) {
                                showDialogRz(code, jsonObject.getString("tips"));
                                return;
                            }
                            if (deletePosition > -1) {
                                map_dateitem.remove(deletePosition);
                            }
                            if (type == 1) {
                                dateRefresh();
                            } else if (type == 2) {
                                notiRefresh();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        ToastUtils.getInstance(HomeActivity.this).showText("网络忙");
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        ToastUtils.getInstance(HomeActivity.this).showText("请求被取消");
                    }

                    @Override
                    public void onFinished() {
                        dateapplay.setClickable(true);
                    }
                });

            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                arg0.dismiss();
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void showDialog(final String inviteId, final String uId, final TextView dateapplay, final int type) {
        showDialog(inviteId, uId, dateapplay, type, -1);
    }

    /**
     * 获取融云未读消息的个数
     */
    private void initRongUnreadMessage() {
        final Conversation.ConversationType[] conversationTypes = {Conversation.ConversationType.PRIVATE, Conversation.ConversationType.DISCUSSION,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE};

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


}
