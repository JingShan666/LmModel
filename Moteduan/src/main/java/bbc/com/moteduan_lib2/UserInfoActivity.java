package bbc.com.moteduan_lib2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.daimajia.swipe.SwipeLayout;
import com.google.gson.Gson;
import com.liemo.shareresource.Const;
import com.liemo.shareresource.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.bean.LoginBean;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.network.ImageLoad;
import bbc.com.moteduan_lib.network.Req;
import bbc.com.moteduan_lib.tools.ToastUtils;
import bbc.com.moteduan_lib2.album.AlbumsActivity;
import bbc.com.moteduan_lib2.bean.UserInfoPageBean;
import bbc.com.moteduan_lib2.mineInvite.InviteDetailsDidNotSignUpActivity;
import bbc.com.moteduan_lib2.mineNotive.NoticeDetailsDidNotSignUpActivity;
import wei.moments.ContRes;
import wei.moments.OtherMomentsActivity;
import wei.moments.network.ReqCallback;
import wei.moments.network.ReqUrl;
import wei.toolkit.utils.DateUtils;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.ToastUtil;
import wei.toolkit.widget.CircleImageBorderView;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class UserInfoActivity extends BaseActivity {
    public static final String TAG = "UserInfoActivity";
    private ImageView back;
    private TextView totalFollowMe;
    private RecyclerView inviteRecyclerView;
    private TextView watch;
    private static String userId;
    private static String memberId;
    private UserInfoPageBean userInfoPageBean;
    private DelegateAdapter delegateAdapter;
    private VirtualLayoutManager virtualLayoutManager;
    private static int REQUEST_CODE_DETAILS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        userId = getIntent().getStringExtra("userId");
        if (TextUtils.isEmpty(userId)) {
            toast.showText("用户Id未找到");
            finish();
        }
        LoginBean loginBean = SpDataCache.getSelfInfo(UserInfoActivity.this);
        if (null == loginBean) {
            memberId = "";
        } else {
            memberId = loginBean.getData().getM_id();
            if (TextUtils.isEmpty(memberId)) memberId = "";
        }

        initView();
        initData();
        initEvents();
    }

    @Override
    public void initView() {
        back = (ImageView) findViewById(R.id.activity_user_info_back);
        watch = (TextView) findViewById(R.id.activity_user_info_invite_watch);

        inviteRecyclerView = (RecyclerView) findViewById(R.id.activity_user_info_invite_rv);
        inviteRecyclerView.setNestedScrollingEnabled(false);
        virtualLayoutManager = new VirtualLayoutManager(this);
        inviteRecyclerView.setLayoutManager(virtualLayoutManager);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        inviteRecyclerView.setAdapter(delegateAdapter);
    }

    @Override
    public void initData() {

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("member_id", memberId);
        Req.post(Url.peekUserInfoPage, map, new Req.ReqCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(String result) {
                Loger.log(TAG, result);
                userInfoPageBean = new Gson().fromJson(result, UserInfoPageBean.class);
                if (!"200".equals(userInfoPageBean.getCode())) {
                    toast.showText(userInfoPageBean.getTips());
                    return;
                }

                delegateAdapter.addAdapter(new RvPersonalAdapter(userInfoPageBean.getMap().getUser()));

                List<UserInfoPageBean.MapBean.AblumsBean> list = userInfoPageBean.getMap().getAblums();
                if (null != list) {
                    if (list.size() > 0) {
                        delegateAdapter.addAdapter(new RvAlbumsAdapter(userInfoPageBean.getMap().getAblums()));
                    }
                }
                if (null != userInfoPageBean.getMap().getContent()) {
                    delegateAdapter.addAdapter(new RvMomentsAdapter(userInfoPageBean.getMap().getContent()));
                }
                if (null != userInfoPageBean.getMap().getOne() && userInfoPageBean.getMap().getOne().size() > 0) {
                    delegateAdapter.addAdapter(new RvAdapter(1, userInfoPageBean.getMap().getOne()));
                }
                if (null != userInfoPageBean.getMap().getTwo() && userInfoPageBean.getMap().getTwo().size() > 0) {
                    delegateAdapter.addAdapter(new RvAdapter(2, userInfoPageBean.getMap().getTwo()));
                }
                if (null != userInfoPageBean.getMap().getThree() && userInfoPageBean.getMap().getThree().size() > 0) {
                    delegateAdapter.addAdapter(new RvAdapter(3, userInfoPageBean.getMap().getThree()));
                }
                inviteRecyclerView.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void failed(String msg) {
                ToastUtil.show(UserInfoActivity.this, getString(R.string.error_network_block));
                Loger.log(TAG, msg);
            }

            @Override
            public void completed() {
            }
        });

    }

    @Override
    public void initEvents() {
        super.initEvents();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginBean loginBean = SpDataCache.getSelfInfo(UserInfoActivity.this);
                if (null == loginBean) {
                    ToastUtil.show(UserInfoActivity.this, "您还未登录，请先登录");
                    Intent intent = new Intent();
                    intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
                    startActivity(intent);
                    return;
                }

                Map<String, Object> map = new HashMap();
                map.put("use_id", loginBean.getData().getM_id());
                map.put("use_type", ContRes.getSelfType());
                map.put("b_use_id", userInfoPageBean.getMap().getUser().getU_id());
                map.put("b_use_type", String.valueOf(userInfoPageBean.getMap().getUser().getU_sex()));
                ReqUrl.post(Url.watchMoments, map, new ReqCallback.Callback<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void success(String backPamam) {
                        try {
                            JSONObject jsonObject = new JSONObject(backPamam);
                            String code = jsonObject.getString("code");
                            if (TextUtils.equals("200", code)) {
                                int num = userInfoPageBean.getMap().getUser().getB_num();
                                totalFollowMe.setText((++num) + "");
                                watch.setText("已关注");
                                userInfoPageBean.getMap().getUser().setB_num(num);
                            } else if (TextUtils.equals("202", code)) {
                                int num = userInfoPageBean.getMap().getUser().getB_num();
                                totalFollowMe.setText((--num) + "");
                                watch.setText("关注");
                                userInfoPageBean.getMap().getUser().setB_num(num);
                            }
                            ToastUtil.show(UserInfoActivity.this, jsonObject.getString("tips"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(String msg) {
                        ToastUtil.show(UserInfoActivity.this, msg);
                    }
                });
            }
        });
    }


    // 资料
    private class RvPersonalAdapter extends DelegateAdapter.Adapter {
        private UserInfoPageBean.MapBean.UserBean userBean;

        public RvPersonalAdapter(UserInfoPageBean.MapBean.UserBean userBean) {
            this.userBean = userBean;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return new LinearLayoutHelper();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_userinfo_personal, parent, false);
            return new RvPersonalHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RvPersonalHolder h = (RvPersonalHolder) holder;
            ImageLoad.bind(h.portrait, userBean.getU_head_photo());
            ImageLoad.bind(h.blurImg, userBean.getU_head_photo(), 3);
            h.name.setText(userBean.getU_nick_name());
            h.partNumber.setText(userBean.getU_age() + "岁 "
                    + userBean.getU_tall() + "cm/" + userBean.getU_weight() + "kg");
            h.followMe.setText(userBean.getB_num() + "");
            h.myCare.setText(userBean.getNum() + "");
            watch.setVisibility(View.VISIBLE);
            totalFollowMe = h.followMe;
            if (userInfoPageBean.getMap().getAttention_type() == 1) {
                watch.setText("已关注");
            } else {
                watch.setText("关注");
            }
            if (userBean.getU_card_state() == 1) {
                h.approveCard.setVisibility(View.VISIBLE);
            }
            if (userBean.getU_video_state() == 1) {
                h.approveVideo.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

    private class RvPersonalHolder extends RecyclerView.ViewHolder {
        public CircleImageBorderView portrait;
        public ImageView blurImg;
        public TextView name;
        public TextView partNumber;
        public TextView approveVideo;
        public TextView approveCard;
        public TextView followMe;
        public TextView myCare;

        public RvPersonalHolder(View itemView) {
            super(itemView);
            portrait = (CircleImageBorderView) itemView.findViewById(R.id.item_userinfo_personal_portrait);
            blurImg = (ImageView) itemView.findViewById(R.id.item_userinfo_personal_img_blur);
            name = (TextView) itemView.findViewById(R.id.item_userinfo_personal_name);
            partNumber = (TextView) itemView.findViewById(R.id.item_userinfo_personal_part_number);
            approveVideo = (TextView) itemView.findViewById(R.id.item_userinfo_personal_approve_video);
            approveCard = (TextView) itemView.findViewById(R.id.item_userinfo_personal_approve_card);
            followMe = (TextView) itemView.findViewById(R.id.item_userinfo_personal_follow_me);
            myCare = (TextView) itemView.findViewById(R.id.item_userinfo_personal_care);
        }
    }


    // 相册
    private class RvAlbumsAdapter extends DelegateAdapter.Adapter {
        List<UserInfoPageBean.MapBean.AblumsBean> list;

        public RvAlbumsAdapter(List<UserInfoPageBean.MapBean.AblumsBean> list) {
            this.list = list;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return new LinearLayoutHelper();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_userinfo_albums, parent, false);
            return new RvAlbumsHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RvAlbumsHolder h = (RvAlbumsHolder) holder;
            if (list.size() > 0) {
                h.albumsLL.setVisibility(View.VISIBLE);
                String url1 = list.get(0).getPhotos_url();
                ImageLoad.bind(h.albumsImg1, url1);
                if (list.size() > 1) {
                    String url2 = list.get(1).getPhotos_url();
                    ImageLoad.bind(h.albumsImg2, url2);
                }
                if (list.size() > 2) {
                    String url3 = list.get(2).getPhotos_url();
                    ImageLoad.bind(h.albumsImg3, url3);
                }
            }

            h.albumsLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (null != userInfoPageBean) {
                        startActivity(new Intent(UserInfoActivity.this, AlbumsActivity.class)
                                .putExtra("userId", userInfoPageBean.getMap().getUser().getU_id())
                                .putExtra("userType", 1)
                                .putExtra("userName", userInfoPageBean.getMap().getUser().getU_nick_name()));
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

    private class RvAlbumsHolder extends RecyclerView.ViewHolder {
        public LinearLayout albumsLL;
        public ImageView albumsImg1;
        public ImageView albumsImg2;
        public ImageView albumsImg3;

        public RvAlbumsHolder(View itemView) {
            super(itemView);
            albumsLL = (LinearLayout) itemView.findViewById(R.id.item_userinfo_albums_ll);
            albumsImg1 = (ImageView) itemView.findViewById(R.id.item_userinfo_albums_img1);
            albumsImg2 = (ImageView) itemView.findViewById(R.id.item_userinfo_albums_img2);
            albumsImg3 = (ImageView) itemView.findViewById(R.id.item_userinfo_albums_img3);

        }
    }

    // 动态
    private class RvMomentsAdapter extends DelegateAdapter.Adapter {
        private UserInfoPageBean.MapBean.ContentBean contentBean;

        public RvMomentsAdapter(UserInfoPageBean.MapBean.ContentBean contentBean) {
            this.contentBean = contentBean;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return new LinearLayoutHelper();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_userinfo_moments, parent, false);
            return new RvMomentsHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RvMomentsHolder h = (RvMomentsHolder) holder;
            ImageLoad.bind(h.momentsPortrait, contentBean.getHead_photo());
            h.momentsContent.setText(contentBean.getContent());
            h.momentsLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != userInfoPageBean) {
                        startActivity(new Intent(UserInfoActivity.this, OtherMomentsActivity.class)
                                .putExtra("userId", userInfoPageBean.getMap().getUser().getU_id())
                                .putExtra("userType", 1)
                                .putExtra("userName", userInfoPageBean.getMap().getUser().getU_nick_name()));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

    private class RvMomentsHolder extends RecyclerView.ViewHolder {
        public LinearLayout momentsLL;
        public ImageView momentsPortrait;
        public TextView momentsContent;

        public RvMomentsHolder(View itemView) {
            super(itemView);
            momentsLL = (LinearLayout) itemView.findViewById(R.id.item_userinfo_moments_ll);
            momentsPortrait = (ImageView) itemView.findViewById(R.id.item_userinfo_moments_portrait);
            momentsContent = (TextView) itemView.findViewById(R.id.item_userinfo_moments_content);
        }
    }


    // 订单
    private class RvAdapter extends DelegateAdapter.Adapter<RecyclerView.ViewHolder> {
        private int dataType;
        private List<UserInfoPageBean.MapBean.OneBean> list;

        public RvAdapter(int dataType, List<UserInfoPageBean.MapBean.OneBean> list) {
            this.dataType = dataType;
            this.list = list;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return new LinearLayoutHelper();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_invite_list_userinfo, parent, false);
                return new RvTitleHolder(view);
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_order_user_info, parent, false);
            return new RvHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            if (holder instanceof RvTitleHolder) {
                RvTitleHolder h = (RvTitleHolder) holder;
                switch (dataType) {
                    case 1: {
                        h.title.setText("娱乐订单");
                    }
                    break;
                    case 2: {
                        h.title.setText("通告订单");
                    }
                    break;
                    case 3: {
                        h.title.setText("商务订单");
                    }
                    break;
                    default: {
                    }
                    break;
                }
            } else if (holder instanceof RvHolder) {
                final RvHolder h = (RvHolder) holder;
                final UserInfoPageBean.MapBean.OneBean bean = list.get(position - 1);

                ImageLoad.bind(h.img, bean.getPhotos_type());
                ImageLoad.bind(h.typeIcon, bean.getPhotos_urlc());
                h.typeName.setText(bean.getSmall_navigation());
                h.price.setText(bean.getReward_price() + "");
                if (!TextUtils.isEmpty(bean.getStart_time()) && !TextUtils.isEmpty(bean.getEnd_time())) {
                    h.time.setText(DateUtils.getCustomFormatTime(bean.getStart_time(), bean.getEnd_time()));
                }
                h.address.setText(bean.getAdress());

                h.apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final LoginBean loginBean = SpDataCache.getSelfInfo(UserInfoActivity.this);
                        if (null == loginBean || TextUtils.isEmpty(loginBean.getData().getM_id())) {
                            ToastUtil.show(UserInfoActivity.this, "您还未登录，请先登录");
                            Intent intent = new Intent();
                            intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
                            UserInfoActivity.this.startActivity(intent);
                            return;
                        }

                        new AlertDialog.Builder(UserInfoActivity.this)
                                .setMessage("确定要报名吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Map<String, Object> map = new HashMap<String, Object>();
                                        map.put("member_id", loginBean.getData().getM_id());
                                        map.put("trader_id", bean.getTrader_id());
                                        Req.post(dataType == Const.Companion.getINVITE_TYPE_NOVICE() ? Url.Invite.applyNotice : Url.applyInviteOrder, map, new Req.ReqCallback() {
                                            @Override
                                            public void success(String result) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(result);
                                                    String code = jsonObject.getString("code");
                                                    String tips = jsonObject.getString("tips");
                                                    ToastUtils.getInstance(UserInfoActivity.this).showText(tips);
                                                    if (!"200".equals(code)) {
                                                        return;
                                                    }
                                                    list.remove(position - 1);
                                                    notifyDataSetChanged();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void failed(String msg) {
                                                Loger.log(TAG, msg);
                                                ToastUtil.show(UserInfoActivity.this, getString(R.string.error_network_block));
                                            }

                                            @Override
                                            public void completed() {
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("member_id", SpDataCache.getSelfInfo(UserInfoActivity.this).getData().getM_id());
//                    Req.post(Url.getModelAuthState, map, new Req.ReqCallback() {
//                        @Override
//                        public void success(String result) {
//                            LogDebug.log(TAG, result);
//                            JSONObject jsonObject = null;
//                            try {
//                                jsonObject = new JSONObject(result);
//                                String code = jsonObject.getString("code");
//                                String tips = jsonObject.getString("tips");
//                                if (!"200".equals(code)) {
//                                    startActivity(new Intent(UserInfoActivity.this, AuthenticationActivity.class));
//                                    ToastUtils.getInstance(UserInfoActivity.this).showText(tips);
//                                    return;
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                ToastUtils.getInstance(UserInfoActivity.this).showText(e.getMessage());
//                            }
//                        }
//
//                        @Override
//                        public void failed(String msg) {
//                            ToastUtils.getInstance(UserInfoActivity.this).showText(msg);
//                        }
//
//                        @Override
//                        public void completed() {
//                        }
//                    });
                    }
                });

                h.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dataType == Const.Companion.getINVITE_TYPE_NOVICE()) {
                            startActivity(new Intent(UserInfoActivity.this, NoticeDetailsDidNotSignUpActivity.class)
                                    .putExtra("orderId", bean.getTrader_id()));

                        } else {
                            startActivity(new Intent(UserInfoActivity.this, InviteDetailsDidNotSignUpActivity.class)
                                    .putExtra("orderId", bean.getTrader_id()));
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) return 0;
            return 1;
        }
    }

    private class RvHolder extends RecyclerView.ViewHolder {
        public ImageView state;
        public ImageView img;
        public ImageView typeIcon;
        public TextView typeName;
        public TextView price;
        public TextView time;
        public TextView address;
        public TextView delete;
        public SwipeLayout swipeLayout;
        public TextView apply;

        public RvHolder(View itemView) {
            super(itemView);
            state = (ImageView) itemView.findViewById(R.id.item_mine_order_state);
            img = (ImageView) itemView.findViewById(R.id.item_mine_order_img);
            typeIcon = (ImageView) itemView.findViewById(R.id.item_mine_order_type_icon);
            typeName = (TextView) itemView.findViewById(R.id.item_mine_order_type_name);
            price = (TextView) itemView.findViewById(R.id.item_mine_order_price);
            time = (TextView) itemView.findViewById(R.id.item_mine_order_time);
            address = (TextView) itemView.findViewById(R.id.item_mine_order_address);
            delete = (TextView) itemView.findViewById(R.id.item_mine_order_delete);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            swipeLayout.setSwipeEnabled(false);
            apply = (TextView) itemView.findViewById(R.id.item_mine_order_apply);
        }
    }

    private class RvTitleHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public RvTitleHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_title_invite_list_userinfo_title);
        }
    }


}
