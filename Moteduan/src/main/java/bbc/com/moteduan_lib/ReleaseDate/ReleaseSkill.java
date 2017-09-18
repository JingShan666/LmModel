package bbc.com.moteduan_lib.ReleaseDate;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.gson.Gson;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.ReleaseDate.adapter.RangegvAdapter;
import bbc.com.moteduan_lib.ReleaseDate.bean.ReleaseBean;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.bean.SimpleDateBean;
import bbc.com.moteduan_lib.constant.Constants;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.dialog.SelectInviteTimeDialog;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.mywidget.DefSizeVideoView;
import bbc.com.moteduan_lib.mywidget.DialogProgress;
import bbc.com.moteduan_lib.mywidget.MyGridView;
import bbc.com.moteduan_lib.network.Req;

import com.liemo.shareresource.Url;

import bbc.com.moteduan_lib.network.Xutils3.Xutils;
import bbc.com.moteduan_lib.oss.OSSManager;
import bbc.com.moteduan_lib.tools.DateUtils;
import bbc.com.moteduan_lib2.InviteTypeActivity;
import bbc.com.moteduan_lib2.OpenCityActivity;
import bbc.com.moteduan_lib2.bean.InviteTypeBean;
import wei.toolkit.PictureListActivity;
import wei.toolkit.VideoListActivity;
import wei.toolkit.decoration.GridlayoutDecoration;
import wei.toolkit.utils.DialogUtils;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.PictureUtil;

public class ReleaseSkill extends BaseActivity {
    private static final String TAG = "ReleaseSkill";

    private static final int REQUEST_CODE_OPEN_CITY = 1;
    private static final int REQUEST_CODE_VIDEO = 2;
    public static final int IMAGE_REQUEST_CODE = 111;
    public static final int DATERANGE = 520;
    public static final int ADDRESS = 521;
    private static final int progress_flag = 1111;
    private static final int error_network_flag = 2222;
    private static final int success_flag = 3333;
    private static final int auth_success_flag = 4444;
    private static final int auth_error_flag = 5555;
    private static final int auth_cancle_flag = 6666;

    private ArrayList<String> picList;
    private ArrayList<String> videoList;
    private ImageButton back;
    private RelativeLayout titleLayout;
    private RelativeLayout theme;
    private TextView reward_money;
    private CrystalSeekbar rangeSeekbar1;
    private RelativeLayout reward;
    private RelativeLayout dateRange;
    private RelativeLayout release_time;
    private RelativeLayout release_address;
    private TextView remark_text;
    private RelativeLayout release_remark;
    private TextView personPhoto_text1;
    private TextView personPhoto_text2;
    private RecyclerView picRv;
    private RelativeLayout personPhoto;
    private PopupWindow popWnd;
    private TagFlowLayout themeflowlayout;


    private Adapter adapter;

    private SimpleDateFormat formatHH;

    private int todayHH;
    private boolean clearItemChose = false;

    private java.util.List<String> times_adapter;


    private List<String> dayStr;
    private List<java.util.Date> dayDate;

    private Date toadyDate;//今天日期

    private boolean isCurrentDateIndex = true;//是不是今天的日期选择页面，是：当前已经过去的时间不可选
    //当前选中日期
    private List<String> times_adapter_chose;
    private Date currentDateChose;

    //显示d，<></>
    private String show_d = "";

    private int show_t_start;
    private int show_t_end;
    private int d = 0;
    private SimpleDateFormat mm_dd;
    private StringBuffer mGprsStringBF = new StringBuffer();
    private StringBuffer sbf = new StringBuffer();

    private String[] mVals = new String[]
            {"喝咖啡聊人生", "陪你吃遍所有美食", "一起发发呆", "聊聊天", "吐槽身边的奇葩", "讨论某一话题",
                    "谈谈心聊聊近况", "不念过往 不畏将来"};
    private View rootView;
    private AutoCompleteTextView themeautotext;
    private Set<Integer> selectPos;
    private TextView theme_title;
    private View timecontentView;

    //选择时间
    private Button ok;
    private Button cancel;
    private ImageView img_shijianbuxuan;
    private TextView msg;
    private GridView timeGride;
    private ImageView to_right;
    private TextView d1;
    private ImageView toleft;
    private TextView range_unlimited;
    private TextView time_unlimited;
    private TextView address_unlimited;

    private Boolean ischecked = false;
    private EditText remark_et;


    private static Boolean seekbarIsMove = false;
    private OSSManager ossManager;
//    private DialogProgress dialogProgress;
    private DialogUtils.DataReqDialog dialogProgress;
    private Button release;

    private int finalMoney;
    private MyGridView address_gv;
    private ArrayList<String> flowItems;
    private RangegvAdapter flowAdapter;
    private int maxMoneyValue = 200;
    private int minMoneyValue = 80;
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {

                case progress_flag:
                    break;
                case error_network_flag:
                    dialogProgress.dismiss();
                    toast.showText("网络不畅通，发布失败");
                    break;
                case success_flag:
                    LogDebug.err("照片上传阿里云成功...");
                    break;
                case auth_success_flag:
                    dialogProgress.dismiss();
                    toast.showText("发布成功");
                    finish();
                    break;
                case auth_error_flag:
                    dialogProgress.dismiss();
                    toast.showText("发布失败，请重新上传");
                    break;
                case auth_cancle_flag:
                    break;
            }

        }

    };
    private int position;
    private String fitem;
    private Map<String, Object> releasemap;


    private RecyclerView mIntiveDateRv;
    private List<SimpleDateBean> mIntiveDateList = new ArrayList<>();
    private InviteDateAdapter mIntiveDateAdapter;

    private SelectInviteTimeDialog.ItemBean[] mItemBeans;
    private StringBuilder mLngSb = new StringBuilder();
    private StringBuilder mLatSb = new StringBuilder();

    private List<InviteTypeBean> mInviteTypes = new ArrayList<>();
    private RecyclerView mInviteTypeRv;
    private RelativeLayout videoSelect;
    private DefSizeVideoView videoView;
    private ImageView videoIcon;
    private String mVideoPath;
    private String currentCity = "";
    private TextView currentCityTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(ReleaseSkill.this, R.layout.activity_release_skill, null);
        setContentView(rootView);
        ossManager = OSSManager.getInstance();
//        dialogProgress = new DialogProgress(rootView, this);
        dialogProgress = new DialogUtils.DataReqDialog(this);
        initView();
        initEvent();
        initData();
    }


    @Override
    public void initView() {

        flowItems = new ArrayList<>();
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        currentCityTv = (TextView) findViewById(R.id.activity_release_skill_current_city);
        titleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
        theme = (RelativeLayout) findViewById(R.id.theme);
        reward_money = (TextView) findViewById(R.id.reward_money);
        rangeSeekbar1 = (CrystalSeekbar) findViewById(R.id.rangeSeekbar1);
        reward = (RelativeLayout) findViewById(R.id.reward);
        dateRange = (RelativeLayout) findViewById(R.id.dateRange);
        release_time = (RelativeLayout) findViewById(R.id.release_time);
        release_address = (RelativeLayout) findViewById(R.id.release_address);
        remark_text = (TextView) findViewById(R.id.remark_text);
        release_remark = (RelativeLayout) findViewById(R.id.release_remark);
        personPhoto_text1 = (TextView) findViewById(R.id.personPhoto_text1);
        personPhoto_text2 = (TextView) findViewById(R.id.personPhoto_text2);
        picRv = (RecyclerView) findViewById(R.id.picRv);
        personPhoto = (RelativeLayout) findViewById(R.id.personPhoto);
        theme_title = (TextView) findViewById(R.id.theme_title);
        range_unlimited = (TextView) findViewById(R.id.range_unlimited);
        time_unlimited = (TextView) findViewById(R.id.time_unlimited);
        address_unlimited = (TextView) findViewById(R.id.address_unlimited);
        remark_et = (EditText) findViewById(R.id.remark_et);
        release = (Button) findViewById(R.id.release);
        address_gv = (MyGridView) findViewById(R.id.address_gridview);
        flowAdapter = new RangegvAdapter(flowItems, ReleaseSkill.this, ADDRESS);
        address_gv.setAdapter(flowAdapter);
        remark_et.setCursorVisible(false);
        picList = new ArrayList<>();
        videoList = new ArrayList<>();
        GridLayoutManager picRvManager = new GridLayoutManager(ReleaseSkill.this, 3);
        picRv.setLayoutManager(picRvManager);
        picRv.addItemDecoration(new GridlayoutDecoration());
        picRv.setAdapter(new PicRvAdapter());
        release_remark.setFocusable(false);
        release_address.setClickable(true);
        mIntiveDateRv = (RecyclerView) findViewById(R.id.release_skill_intive_date_rv);
        GridLayoutManager manager = new GridLayoutManager(ReleaseSkill.this, 3);
        mIntiveDateRv.setLayoutManager(manager);
        mIntiveDateRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            Drawable drawable = ContextCompat.getDrawable(ReleaseSkill.this, R.mipmap.arrow_half);
            RecyclerView.LayoutManager manager;
            int spanCount;

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//                super.onDrawOver(c, parent, state);
                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    if (i + 1 % spanCount == 0) {
                        continue;
                    }
                    View childView = parent.getChildAt(i);
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();
                    final int left = childView.getRight();
                    final int right = left + drawable.getIntrinsicWidth();
                    final int bottom = childView.getBottom();
                    drawable.setBounds(left, bottom / 2 - drawable.getIntrinsicHeight() / 2, right, bottom / 2 + drawable.getIntrinsicHeight());
                    drawable.draw(c);
                }
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
                if (manager == null) {
                    manager = parent.getLayoutManager();
                }
                if (manager instanceof GridLayoutManager) {
                    spanCount = ((GridLayoutManager) manager).getSpanCount();
                    int position = manager.getPosition(view);
                    if ((position + 1) % spanCount != 0) {
                        outRect.right = drawable.getIntrinsicWidth();
                    }
                }
            }
        });
        mIntiveDateAdapter = new InviteDateAdapter();
        mIntiveDateList.add(DateUtils.getOtherDayInfo(0));
        mIntiveDateList.add(DateUtils.getOtherDayInfo(1));
        mIntiveDateList.add(DateUtils.getOtherDayInfo(2));
        mIntiveDateRv.setAdapter(mIntiveDateAdapter);

        mInviteTypeRv = (RecyclerView) findViewById(R.id.activity_release_skill_invite_type_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mInviteTypeRv.setLayoutManager(gridLayoutManager);
        mInviteTypeRv.addItemDecoration(new GridlayoutDecoration());
        mInviteTypeRv.setAdapter(new InviteTypeAdapter());

        videoSelect = (RelativeLayout) findViewById(R.id.activity_release_skill_select_video);
        videoView = (DefSizeVideoView) findViewById(R.id.activity_release_skill_video);
        videoIcon = (ImageView) findViewById(R.id.activity_release_skill_select_video_icon);
    }


    private void initEvent() {
        videoSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ReleaseSkill.this, VideoListActivity.class), REQUEST_CODE_VIDEO);
            }
        });
        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishSkill();
            }
        });
//        release_time.setOnClickListener(releaseTimeListener);

        remark_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remark_et.setCursorVisible(true);
            }
        });
        //期望悬赏
        rangeSeekbar1.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                reward_money.setText(value + "币/小时");
                seekbarIsMove = true;
//                String nValue = new String(value + "");
//                finalMoney = Integer.parseInt(nValue);
            }
        });

        //约会范围
        dateRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ReleaseSkill.this, InviteTypeActivity.class)
                        .putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) mInviteTypes)
                        .putExtra("selectNumber", 3), DATERANGE);
            }
        });


        release_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReleaseSkill.this, AddressLocation.class);
                intent.putExtra("city", TextUtils.isEmpty(currentCity) ? SpDataCache.getCity() : currentCity);
                startActivityForResult(intent, ADDRESS);
            }
        });

        //主题
        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View contentView = View.inflate(ReleaseSkill.this, R.layout.releasetheme_window, null);
                popWnd = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //流式标签
                final LayoutInflater mInflater = LayoutInflater.from(ReleaseSkill.this);
                themeflowlayout = (TagFlowLayout) contentView.findViewById(R.id.theme_flowlayout);
                themeautotext = (AutoCompleteTextView) contentView.findViewById(R.id.theme_autoText);
                Button close = (Button) contentView.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popWnd.dismiss();
                    }
                });

                Button confirm = (Button) contentView.findViewById(R.id.confirm);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String themeTitle = themeautotext.getText().toString().trim();
                        theme_title.setText(themeTitle);
                        popWnd.dismiss();

                    }
                });
                themeflowlayout.setAdapter(new TagAdapter<String>(mVals) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        TextView tv = (TextView) mInflater.inflate(R.layout.themetagbg, themeflowlayout, false);
                        tv.setText(s);
                        return tv;
                    }
                });

                themeflowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        //ToDO 有问题 再改改
                        themeautotext.setText(mVals[position]);
                        for (Integer item : selectPos) {
                            View childAt = parent.getChildAt(item);
                            LogDebug.err(item + "--" + childAt);
                            childAt.setBackgroundResource(R.drawable.bg_gray);
                        }
                        parent.getChildAt(position).setBackgroundResource(R.drawable.bg_solid_radius90_mainred);
                        return true;
                    }
                });

                //点击标签时的回调
                themeflowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
                    @Override
                    public void onSelected(Set<Integer> selectPosSet) {

                        ReleaseSkill.this.setTitle("choose:" + selectPosSet.toString());
                        LogDebug.err("choose:" + selectPosSet.toString());
                        selectPos = selectPosSet;
                    }
                });
                popWnd.setOutsideTouchable(false);
                popWnd.showAtLocation(rootView, Gravity.CENTER, 0, 0);
            }
        });


        currentCityTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ReleaseSkill.this, OpenCityActivity.class), REQUEST_CODE_OPEN_CITY);
            }
        });
    }

    /**
     * 选择档期时间事件
     */
    private View.OnClickListener releaseTimeListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            releaseTime();

        }
    };

    private void releaseTime() {
        timecontentView = View.inflate(ReleaseSkill.this, R.layout.activity_data_time_chose, null);
        popWnd = new PopupWindow(timecontentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ok = (Button) timecontentView.findViewById(R.id.ok);
        cancel = (Button) timecontentView.findViewById(R.id.cancel);
        toleft = (ImageView) timecontentView.findViewById(R.id.toleft);
        to_right = (ImageView) timecontentView.findViewById(R.id.to_right);
        timeGride = (GridView) timecontentView.findViewById(R.id.timeGride);
        d1 = (TextView) timecontentView.findViewById(R.id.d1);
        msg = (TextView) timecontentView.findViewById(R.id.msg);
        img_shijianbuxuan = (ImageView) timecontentView.findViewById(R.id.img_shijianbuxuan);
        if (ischecked) {
            img_shijianbuxuan.setImageResource(R.drawable.icon_shijianbuxian_pre);
        } else {
            img_shijianbuxuan.setImageResource(R.drawable.icon_shijianbuxian);
        }

        img_shijianbuxuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ischecked) {
                    img_shijianbuxuan.setImageResource(R.drawable.icon_shijianbuxian_pre);
                    ischecked = true;
                } else {
                    img_shijianbuxuan.setImageResource(R.drawable.icon_shijianbuxian);
                    ischecked = false;
                }
            }
        });

        fabushijian();
        popWnd.setOutsideTouchable(true);
        popWnd.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = (float) 0.4; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 选择发布的时间
     */
    private void fabushijian() {
        dayStr = new ArrayList<>();
        dayDate = new ArrayList<>();
        times_adapter = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            if (i == 0) {
                times_adapter.add("09:00");
            } else {
                times_adapter.add((9 + i) + ":00");
            }
        }
        adapter = new ReleaseSkill.Adapter();
        timeGride.setAdapter(adapter);

        ok.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if (!ischecked) {
                    if (show_t_start == 0 && show_t_end == 0) {
                        toast.showText("请选择档期");
                        return;
                    }
                }
                String date = DateUtils.transformationDate(currentDateChose, DateUtils.yyyy_MM_dd);
                String msgdate = show_t_start + ":00-" + show_t_end + ":00";
                LogDebug.err(msgdate);
                time_unlimited.setText(msgdate);
                if (ischecked) {
                    time_unlimited.setText("时间不限");
                }
                popWnd.dismiss();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = (float) 1; //0.0-1.0
                getWindow().setAttributes(lp);

            }
        });

        cancel.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                popWnd.dismiss();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = (float) 1; //0.0-1.0
                getWindow().setAttributes(lp);
            }
        });


        times_adapter_chose = new java.util.ArrayList<>();

        //取时间
        toadyDate = new java.util.Date();

        formatHH = DateUtils.getSimpleDateFormat(DateUtils.HH);
        todayHH = Integer.valueOf(formatHH.format(toadyDate));
        currentDateChose = toadyDate;

        show_d = DateUtils.transformationDate(currentDateChose.getTime(), DateUtils.MM_dd2);
        mm_dd = DateUtils.getSimpleDateFormat(DateUtils.yyyy_MM_dd);
        java.util.Date date1 = DateUtils.getDateForDate(toadyDate, d);
        dayDate.add(date1);
        String dateStr = mm_dd.format(date1);
        dayStr.add(dateStr);
//        d1.setText(dateStr + " " + DateUtils.getWeekOfDate(date1));
        to_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d = d + 1;
                updategv();
                toleft.setImageResource(R.drawable.icon_zuo);
                to_right.setImageResource(R.drawable.icon_you_pre);
            }

        });

        toleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (d > 0) {
                    d = d - 1;
                    updategv();
                    toleft.setImageResource(R.drawable.icon_zuo_pre);
                    to_right.setImageResource(R.drawable.icon_you);
                }
            }
        });


    }

    private void updategv() {

        Date date1 = DateUtils.getDateForDate(toadyDate, d);
        dayDate.add(date1);
        String dateStr = mm_dd.format(date1);
        dayStr.add(dateStr);
//        d1.setText(dateStr + " " + DateUtils.getWeekOfDate(date1));
        java.util.Date currentDateChose1 = null;
        currentDateChose1 = dayDate.get(d);
        show_d = DateUtils.transformationDate(currentDateChose1.getTime(), DateUtils.MM_dd2);
        if (currentDateChose1 != null && currentDateChose1.getTime() != ReleaseSkill.this.currentDateChose.getTime()) {
            ReleaseSkill.this.currentDateChose = currentDateChose1;
            times_adapter_chose.clear();
            clearItemChose = true;
            adapter.notifyDataSetChanged();
        }

        if (toadyDate.getTime() != currentDateChose.getTime()) {
            isCurrentDateIndex = false;
            msg.setText("请选择服务时间");
        } else {
            isCurrentDateIndex = true;
        }
    }


    public void choosePic() {
        Intent it = new Intent(ReleaseSkill.this, PictureListActivity.class);
        it.putExtra("count", 5 - picList.size());
        startActivityForResult(it, IMAGE_REQUEST_CODE);
    }

    @Override
    public void initData() {
        currentCityTv.setText(SpDataCache.getCity());
        rangeSeekbar1.setMaxValue(maxMoneyValue);
        rangeSeekbar1.setMinValue(minMoneyValue);
        rangeSeekbar1.setMinStartValue(maxMoneyValue).apply();
        reward_money.setText(maxMoneyValue + "币/小时");

        Req.post(Url.getSkillMoneyRange, null, new Req.ReqCallback() {
            @Override
            public void success(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
                        if (jsonObject.has("highest")) {
                            int highest = jsonObject.getInt("highest");
                            if (highest > 0) {
                                maxMoneyValue = highest;
                            }
                        }
                        if (jsonObject.has("minimun")) {
                            int minimun = jsonObject.getInt("minimun");
                            if (minimun > 0) {
                                minMoneyValue = minimun;
                            }
                        }
                        rangeSeekbar1.setMaxValue(maxMoneyValue);
                        rangeSeekbar1.setMinValue(minMoneyValue);
                        rangeSeekbar1.setMinStartValue(maxMoneyValue).apply();
                        reward_money.setText(maxMoneyValue + "币/小时");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {

            }

            @Override
            public void completed() {

            }
        });
    }

    /**
     * 请求发布内容进行编辑
     */
    private void requestSkillData() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SpDataCache.getSelfInfo(ReleaseSkill.this).getData().getM_head_photo());
        Xutils.post(Url.editSkill, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogDebug.err("editSkillResult:" + result);
                Gson gson = new Gson();
                ReleaseBean releaseBean = gson.fromJson(result, ReleaseBean.class);
                List<ReleaseBean.DataBean.ScopeBean> scope = releaseBean.getData().getScope();
                if (scope.size() > 0) {
                    range_unlimited.setVisibility(View.GONE);
                }
                for (ReleaseBean.DataBean.ScopeBean scopeBean : scope) {
                    String content = scopeBean.getContent();
                    LogDebug.err(content);
                    int parseInt = Integer.parseInt(content);
//                    gvitems.add(DateRange.mVals[parseInt - 1]);
//                    range_gv.setVisibility(View.VISIBLE);
                }
//                rangegvAdapter.notifyDataSetChanged();
                ReleaseBean.DataBean.SkillBean skill = releaseBean.getData().getSkill();
                String startTime = skill.getStartTime();
                String stopTime = skill.getStopTime();
                String reltime = startTime + "-" + stopTime;
                if (startTime == null && stopTime == null) {
                    time_unlimited.setText("时间不限");
                } else {
                    time_unlimited.setText(reltime);
                }
                int reward = skill.getReward();
                reward_money.setText(reward + "币/小时");
                rangeSeekbar1.setMinStartValue(reward).apply();
                LogDebug.err("reward:" + reward + " ");
                List<ReleaseBean.DataBean.BusinessBean> business = releaseBean.getData().getBusiness();
                if (business.size() > 0) {
                    address_unlimited.setVisibility(View.GONE);
                }

                for (int i = 0; i < business.size(); i++) {
                    ReleaseBean.DataBean.BusinessBean businessBean = business.get(i);
                    flowItems.add(businessBean.getBShoparea());
                    mLatSb.append(businessBean.getBGpsLat());
                    mLatSb.append(",");
                    mLngSb.append(businessBean.getBGpsLng());
                    mLngSb.append(",");
                    address_gv.setVisibility(View.VISIBLE);
                }
                if (mLatSb.length() > 0 && mLngSb.length() > 0) {
                    mLatSb.deleteCharAt(mLatSb.length() - 1);
                    mLngSb.deleteCharAt(mLngSb.length() - 1);
                }
                flowAdapter.notifyDataSetChanged();

                List<ReleaseBean.DataBean.PhotosBean> photos = releaseBean.getData().getPhotos();
                for (ReleaseBean.DataBean.PhotosBean photo : photos) {
                    String url = photo.getPhotoUrl();
                    if (TextUtils.isEmpty(url) || !url.startsWith("http://") && !url.startsWith("https://")) {
                        continue;
                    }
                    picList.add(url);
                }
                if (picList.size() > 0) {
                    picRv.getAdapter().notifyDataSetChanged();
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
    }


    private void publishSkill() {
        if (mItemBeans == null || mItemBeans.length < 1) {
            toast.showText("请选择档期");
            return;
        }
        releasemap = new HashMap<>();
        if (mInviteTypes.size() > 0) {
            StringBuffer contents = new StringBuffer();
            for (int i = 0; i < mInviteTypes.size(); i++) {
                if (i < mInviteTypes.size() - 1) {
                    contents.append(mInviteTypes.get(i).getId() + ",");
                } else {
                    contents.append(mInviteTypes.get(i).getId());
                }
            }
            releasemap.put("content", contents);
        } else {
            toast.showText("请选择内容范围");
            return;
        }
        if (flowItems.size() > 0) {
            StringBuffer flowContent = new StringBuffer();
            for (int i = 0; i < flowItems.size(); i++) {
                if (i < flowItems.size() - 1) {
                    flowContent.append(flowItems.get(i) + ",");
                } else {
                    flowContent.append(flowItems.get(i));
                }
            }
            releasemap.put("b_shoparea", flowContent.toString());
        } else {
            toast.showText("请选择地点场所");
            return;
        }

        if (TextUtils.isEmpty(mVideoPath)) {
            toast.showText("发布需要上传个人视频");
            return;
        }
        if (picList.size() < 5) {
            toast.showText("发布需要上传五张照片");
            return;
        }

        StringBuilder startTime = new StringBuilder();
        StringBuilder endTime = new StringBuilder();
        for (int i = 0; i < mItemBeans.length; i++) {
            SelectInviteTimeDialog.ItemBean bean = mItemBeans[i];
            if (bean == null) {
                continue;
            }
            switch (i) {
                case 0:
                    startTime.append(DateUtils.transformationDate(new Date(), "yyyy-MM-dd"));
                    startTime.append(" ");
                    startTime.append(bean.getHour());
                    startTime.append(":00:00");
                    startTime.append(",");
                    break;
                case 1:
                    endTime.append(DateUtils.transformationDate(new Date(), "yyyy-MM-dd"));
                    endTime.append(" ");
                    endTime.append(bean.getHour());
                    endTime.append(":00:00");
                    endTime.append(",");
                    break;
                case 2:
                    startTime.append(DateUtils.transformationDate(DateUtils.getDateForDate(new Date(), 1), "yyyy-MM-dd"));
                    startTime.append(" ");
                    startTime.append(bean.getHour());
                    startTime.append(":00:00");
                    startTime.append(",");
                    break;
                case 3:
                    endTime.append(DateUtils.transformationDate(DateUtils.getDateForDate(new Date(), 1), "yyyy-MM-dd"));
                    endTime.append(" ");
                    endTime.append(bean.getHour());
                    endTime.append(":00:00");
                    endTime.append(",");
                    break;
                case 4:
                    startTime.append(DateUtils.transformationDate(DateUtils.getDateForDate(new Date(), 2), "yyyy-MM-dd"));
                    startTime.append(" ");
                    startTime.append(bean.getHour());
                    startTime.append(":00:00");
                    break;
                case 5:
                    endTime.append(DateUtils.transformationDate(DateUtils.getDateForDate(new Date(), 2), "yyyy-MM-dd"));
                    endTime.append(" ");
                    endTime.append(bean.getHour());
                    endTime.append(":00:00");
                    break;
            }
        }
        if (startTime.length() < 1 || endTime.length() < 1) {
            toast.showText("请选择档期");
            return;
        }
        releasemap.put("startTime", startTime.toString());
        releasemap.put("endTime", endTime.toString());
        updateLoadPhoto(picList);
    }

    /**
     * 上传照片
     *
     * @param pics
     */
    private void updateLoadPhoto(List<String> pics) {
        dialogProgress.show();
        new Thread(new UpLoadThread(pics)).start();
    }


    class UpLoadThread implements Runnable {
        private List<String> pics;

        public UpLoadThread(List<String> pics) {
            this.pics = pics;
        }

        @Override
        public void run() {
            sbf.setLength(0);
            JSONArray jsonArray = new JSONArray();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            try {
                for (int i = 0; i < pics.size(); i++) {
                    String url = PictureUtil.pictureCompress(pics.get(i));
                    BitmapFactory.decodeFile(url, options);
                    int w = options.outWidth;
                    int h = options.outHeight;
                    PutObjectResult putResult = ossManager.synchUpLoad(url, Constants.IMAGE_TYPE, new OSSProgressCallback() {
                        @Override
                        public void onProgress(Object o, long currentSize, long totalSize) {
                            // 暂时不需要显示进度，先隐藏
//                            int progress = Math.round((float) currentSize / totalSize * 100L);
//                            Message msg = new Message();
//                            msg.what = progress_flag;
//                            msg.obj = progress;
//                            handler.sendMessage(msg);
                        }
                    });
                    url = putResult.getETag();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("width", w);
                    jsonObject.put("height", h);
                    jsonObject.put("url", url);
                    jsonArray.put(jsonObject);
                }
                sbf.append(new JSONObject().put("list", jsonArray).toString());

                String thumbnailUrl = PictureUtil.savePicture(PictureUtil.getLocalVideoThumbnail(mVideoPath));
                PutObjectResult thumbnailResult = ossManager.synchUpLoad(thumbnailUrl, wei.moments.oss.OSSManager.IMAGE_TYPE, null);
                String remoteThumbnailUrl = thumbnailResult.getETag();
                PutObjectResult videoResult = ossManager.synchUpLoad(mVideoPath, wei.moments.oss.OSSManager.VIDEO_TYPE, null);
                String remoteVideoUrl = videoResult.getETag();
                JSONObject jsonObject = new JSONObject();
                JSONArray jArray = new JSONArray();
                jArray.put(new JSONObject().put("thumbnail", remoteThumbnailUrl).put("video", remoteVideoUrl));
                jsonObject.put("list", jArray);
                releasemap.put("videoUrl", jsonObject.toString());
            } catch (ClientException | ServiceException e) {
                e.printStackTrace();
                Message msg = handler.obtainMessage();
                msg.what = error_network_flag;
                handler.sendMessage(msg);
                return;
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            Message msg = handler.obtainMessage();
            msg.what = success_flag;
            handler.sendMessage(msg);
            LogDebug.err(sbf.toString());

            modelRelease();

        }
    }

    private void modelRelease() {
        releasemap.put("member_Id", SpDataCache.getSelfInfo(ReleaseSkill.this).getData().getM_id());
        if (seekbarIsMove) {
            String money = reward_money.getText().toString();
            String moneyNum = money.substring(0, money.length() - 4);
            releasemap.put("price", moneyNum);
        } else {
            releasemap.put("price", maxMoneyValue + "");
        }

        releasemap.put("b_gps_lng", mLngSb.toString());
        releasemap.put("b_gps_lat", mLatSb.toString());
        releasemap.put("current_city", TextUtils.isEmpty(currentCity) ? SpDataCache.getCity() : currentCity);

        if (sbf.length() > 0) {
            releasemap.put("photosUrl", sbf.toString());
        } else {
            toast.showText("照片数量未解析成功,请重新选取");
            return;
        }

        LogDebug.err("商圈大小：" + flowItems.size());
        String url = Url.publishInvite;
        Xutils.post(url, releasemap, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Loger.log(TAG, "addSkillresult" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String tips = jsonObject.getString("tips");
                    if (!"200".equals(code)) {
                        toast.showText(tips);
                        dialogProgress.dismiss();
                        return;
                    }
                    Message msg = handler.obtainMessage();
                    msg.what = auth_success_flag;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Loger.log(TAG, ex.toString());
                Message msg = handler.obtainMessage();
                msg.what = auth_error_flag;
                handler.sendMessage(msg);
            }

            @Override
            public void onCancelled(org.xutils.common.Callback.CancelledException cex) {
                Message msg = handler.obtainMessage();
                msg.what = auth_cancle_flag;
                handler.sendMessage(msg);
            }

            @Override
            public void onFinished() {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_VIDEO) {
            if (data == null) {
                return;
            }
            String videoPath = data.getStringExtra("video");
            if (TextUtils.isEmpty(videoPath)) {
                return;
            }
            this.mVideoPath = videoPath;
            videoIcon.setVisibility(View.INVISIBLE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setVolume(0f, 0f);
                    mp.start();
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });

            videoView.setVideoPath(mVideoPath);
            videoView.start();
        } else if (requestCode == IMAGE_REQUEST_CODE && data != null) {

            List<String> picPaths = data.getStringArrayListExtra("data");
            if (null == picPaths || picPaths.size() < 1) {
                return;
            }
            picList.addAll(picPaths);
            picRv.getAdapter().notifyDataSetChanged();

        } else if (requestCode == DATERANGE && data != null) {
            if (data == null) return;
            List<InviteTypeBean> list = data.getParcelableArrayListExtra("data");
            if (list == null) return;
            mInviteTypes.clear();
            mInviteTypes.addAll(list);
            mInviteTypeRv.getAdapter().notifyDataSetChanged();
        } else if (requestCode == ADDRESS && data != null) {
            ArrayList<String> items = data.getStringArrayListExtra("items");
            flowItems.clear();
            flowItems.addAll(items);
            if (items.size() > 0) {
                address_unlimited.setVisibility(View.GONE);
                address_gv.setVisibility(View.VISIBLE);
            } else {
                address_unlimited.setVisibility(View.VISIBLE);
                address_gv.setVisibility(View.GONE);
            }
            flowAdapter.notifyDataSetChanged();

            ArrayList<String> lllist = data.getStringArrayListExtra("lllist");
            if (lllist != null && lllist.size() > 0) {
                mLngSb.setLength(0);
                mLatSb.setLength(0);
                for (int i = 0; i < lllist.size(); i++) {
                    String[] latLng = lllist.get(i).split(",");
                    if (latLng.length > 1) {
                        mLatSb.append(latLng[0]);
                        mLatSb.append(",");
                        mLngSb.append(latLng[1]);
                        mLngSb.append(",");
                    }

                }
                if (mLatSb.length() > 0 && mLngSb.length() > 0) {
                    mLatSb.deleteCharAt(mLatSb.length() - 1);
                    mLngSb.deleteCharAt(mLngSb.length() - 1);
                }
            }

        } else if (requestCode == REQUEST_CODE_OPEN_CITY) {
            if (data == null) return;
            currentCity = data.getStringExtra("city");
            currentCityTv.setText(currentCity);
        }
    }


    //发布时间
    class Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            return times_adapter == null ? 0 : times_adapter.size();
        }

        @Override
        public Object getItem(int i) {
            return times_adapter.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            final String itemDate = times_adapter.get(i);
            view = LayoutInflater.from(ReleaseSkill.this).inflate(R.layout.time_grideview, null);

            final CheckBox checkBox = ((CheckBox) view);
            checkBox.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(android.widget.CompoundButton compoundButton, boolean b) {
                    if (b) {
                        times_adapter_chose.add(itemDate);
                        if (verifyAdjacent()) {
                            msg.setText("请选择服务时间");
                        } else
                            show();
                    } else {
                        if (!verifyAdjacent2(itemDate)) {
                            times_adapter_chose.remove(itemDate);
                            show();
                        } else {
                            b = true;
                            checkBox.setChecked(true);
//                            checkBox.setTextColor(DataTimeChoseActivity.this.getResources().getColor(com.bbc.lm.R.color.black));
                            toast.showText("所选日期必须是连续的");
                        }
                    }
//                    checkBox.setTextColor(b ? ReleaseSkill.this.getResources().getColor(R.color.black) : ReleaseSkill.this.getResources().getColor(R.color.theme_white));

                }
            });

            if (isCurrentDateIndex && todayHH >= Integer.valueOf(itemDate.substring(0, 2))) {
                checkBox.setEnabled(false);
                checkBox.setBackgroundResource(R.drawable.release_time_itembg);
            } else {
                checkBox.setEnabled(true);
//                checkBox.setBackgroundResource(R.color.theme_white);
            }

            if (clearItemChose) {
                checkBox.setChecked(false);
                clearItemChose = false;
            }
            checkBox.setText(itemDate);

            return view;
        }
    }

    private void show() {

        msg.setText(show_d + show_t_start + ":00-" + show_t_end + ":00"
                + ("(" + (show_t_end - show_t_start) + "小时)"));
    }

    /**
     * @return true 当取消选中时，验证是否是已经选中的时间，且非首尾位置
     */
    private boolean verifyAdjacent2(String time) {
        java.util.List<Integer> chose = new java.util.ArrayList<>();
        for (String date : times_adapter_chose) {//09:00
            chose.add(Integer.valueOf(date.substring(0, 2)));
        }

        Integer[] integers = new Integer[]{};
        java.util.Arrays.sort(integers = chose.toArray(integers));

        show_t_start = (integers[0]);
        show_t_end = (integers[integers.length - 1] + 1);

        int count = integers.length;
        if (count == 1) {
            return false;
        } else {
            Integer integer = Integer.valueOf(time.substring(0, 2));
            if ((integers[0] == integer) || (integer == integers[integers.length - 1])) {
                return false;
            } else {
                for (int i : integers) {
                    if (i == integer) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * 当选中时，验证是否是相邻的时间，若不是 清空状态重新选
     */
    private boolean verifyAdjacent() {

        java.util.List<Integer> chose = new java.util.ArrayList<>();
        for (String date : times_adapter_chose) {//09:00
            chose.add(Integer.valueOf(date.substring(0, 2)));
            LogDebug.print("选中 时间: " + date);
        }

        Integer[] integers = new Integer[]{};
        java.util.Arrays.sort(integers = chose.toArray(integers));

        show_t_start = (integers[0]);
        show_t_end = (integers[integers.length - 1] + 1);

        int count = integers.length;
        if (count == 1) {
            return false;
        }
        if (count == 2) {
            if (integers[0] + 1 != integers[1]) {
                times_adapter_chose.clear();
                clearItemChose = true;
                adapter.notifyDataSetChanged();
                msg.setText("请选择服务时间");
                return true;
            } else {

                return false;
            }
        } else {
            for (int i = 0; i < count - 1; i++) {

                if (integers[i] + 1 != integers[i + 1]) {
                    times_adapter_chose.clear();
                    clearItemChose = true;
                    adapter.notifyDataSetChanged();
                    msg.setText("请选择服务时间");
                    return true;
                }
            }

            return false;
        }

    }


    /**
     * 档期选择布局
     */
    private class InviteDateAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_date
                    , null);
            InviteDateHolder holder = new InviteDateHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            InviteDateHolder h = (InviteDateHolder) holder;
            final SimpleDateBean bean = mIntiveDateList.get(position);
            if (bean.isSelected()) {
                h.mDayTv.setTextColor(ContextCompat.getColor(ReleaseSkill.this, R.color.theme_red));
            } else {
                h.mDayTv.setTextColor(ContextCompat.getColor(ReleaseSkill.this, R.color.text_gray));
            }

            h.mMonthTv.setText(bean.getMonth() + "月");
            h.mDayTv.setText(bean.getDay() + "日");
            h.mWeekTv.setText(DateUtils.weekDaysName[bean.getWeek()]);

            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (SimpleDateBean b : mIntiveDateList) {
                        if (bean == b) {
                            bean.setSelected(true);
                        } else {
                            b.setSelected(false);
                        }
                    }
                    notifyDataSetChanged();
                    SelectInviteTimeDialog dialog = new SelectInviteTimeDialog();
                    dialog.setOnSelectedListener(new SelectInviteTimeDialog.OnSelectedListener() {
                        @Override
                        public void result(SelectInviteTimeDialog.ItemBean[] itemBeens, DialogFragment dialogFragment) {
                            dialogFragment.dismiss();
                            ReleaseSkill.this.mItemBeans = itemBeens;
                        }
                    });
                    dialog.show(getSupportFragmentManager(), position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mIntiveDateList.size();
        }

        private class InviteDateHolder extends RecyclerView.ViewHolder {
            public TextView mMonthTv;
            public TextView mDayTv;
            public TextView mWeekTv;


            public InviteDateHolder(View itemView) {
                super(itemView);
                mMonthTv = (TextView) itemView.findViewById(R.id.item_invite_date_month);
                mDayTv = (TextView) itemView.findViewById(R.id.item_invite_date_day);
                mWeekTv = (TextView) itemView.findViewById(R.id.item_invite_date_week);

            }
        }
    }

    private class InviteTypeAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_type_stroke, parent, false);
            return new InviteTypeHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            InviteTypeHolder h = (InviteTypeHolder) holder;
            InviteTypeBean bean = mInviteTypes.get(position);
            h.label.setText(bean.getLabel());
        }

        @Override
        public int getItemCount() {
            return mInviteTypes.size();
        }
    }

    private class InviteTypeHolder extends RecyclerView.ViewHolder {
        public TextView label;

        public InviteTypeHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.item_invite_type_stroke_label);
        }

    }


    private class PicRvAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_item, null);
            return new PicRvHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder h, final int position) {
            final PicRvHolder holder = (PicRvHolder) h;
            holder.delete.setVisibility(android.view.View.GONE);
            if (position == picList.size()) {
                holder.img.setImageResource(R.drawable.add_pic);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {
                        choosePic();
                    }
                });
            } else {
                holder.delete.setVisibility(android.view.View.GONE);
                String url = picList.get(position);
                x.image().bind(holder.img, url);

                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                holder.img.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(android.view.View v) {
                        if (position != picList.size()) {
                            holder.delete.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        picList.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return picList == null ? 0 : picList.size() + 1;
        }
    }

    private class PicRvHolder extends RecyclerView.ViewHolder {
        public RelativeLayout itemLayout;
        public ImageView img;
        public ImageView delete;

        public PicRvHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.p1);
            itemLayout = (RelativeLayout) itemView.findViewById(R.id.itemLayout);
            delete = (ImageView) itemView.findViewById(R.id.delete);
        }
    }


}
