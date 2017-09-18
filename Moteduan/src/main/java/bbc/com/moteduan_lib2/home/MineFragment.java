package bbc.com.moteduan_lib2.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liemo.shareresource.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.bean.LoginBean;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.home.EditActivity;
import bbc.com.moteduan_lib.home.SettingActivity;
import bbc.com.moteduan_lib.leftmenu.ShareActivity;
import bbc.com.moteduan_lib.leftmenu.activity.Feedback;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.network.ImageLoad;
import bbc.com.moteduan_lib.network.Req;
import bbc.com.moteduan_lib2.AuthenticationActivity;
import bbc.com.moteduan_lib2.MineScheduleActivity;
import bbc.com.moteduan_lib2.WatchActivity;
import bbc.com.moteduan_lib2.album.AlbumsActivity;
import bbc.com.moteduan_lib2.base.BaseFragment;
import bbc.com.moteduan_lib2.bean.PersonageBean;
import bbc.com.moteduan_lib2.login.LoginActivity;
import bbc.com.moteduan_lib2.mineInvite.MineOrderActivity;
import bbc.com.moteduan_lib2.mineNotive.MineNoticeActivity;
import bbc.com.moteduan_lib2.wallet.MineWalletActivity;
import cn.sharesdk.onekeyshare.OnekeyShare;
import wei.moments.MomentsActivity;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.ToastUtil;

/**
 * Created by Administrator on 2017/5/18 0018.
 * 我的
 */

public class
MineFragment extends BaseFragment {
    public static final String TAG = "MineFragment";
    private ImageView mBlurIv;
    private ImageView mEditInfoIv;
    private ImageView mPortraitIv;
    private TextView mNameTv;
    private TextView mPartNumberTv;


    private TextView mApproveCardTv;
    private TextView mApproveVideoTv;
    private TextView mFollowMeTv;
    private TextView mCareTv;
    private LinearLayout mFollowMeLL, mCareLL;
    private LoginBean mLoginBean;
    private ImageView signIv;
    private PersonageBean mPersonageBean;
    private RelativeLayout mSettingRl;
    private RelativeLayout mineOrder;
    private RelativeLayout mineAuth;
    private RelativeLayout mineWallet;
    private RelativeLayout mineAlbums;
    private RelativeLayout feedBack;
    private RelativeLayout mineMoments;
    private RelativeLayout mineSchedule;
    private NestedScrollView nestedScrollView;
    private RelativeLayout mineShare;
    private RelativeLayout mNotice;

    //认证dialog
    private AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        mBlurIv = (ImageView) view.findViewById(R.id.fragment_mine_img_blur);
        mPortraitIv = (ImageView) view.findViewById(R.id.fragment_mine_portrait);
        mNameTv = (TextView) view.findViewById(R.id.fragment_mine_name);
        mPartNumberTv = (TextView) view.findViewById(R.id.fragment_mine_part_number);
        mApproveCardTv = (TextView) view.findViewById(R.id.fragment_mine_approve_card);
        mApproveVideoTv = (TextView) view.findViewById(R.id.fragment_mine_approve_video);
        mFollowMeTv = (TextView) view.findViewById(R.id.fragment_mine_follow_me);
        mCareTv = (TextView) view.findViewById(R.id.fragment_mine_care);
        mFollowMeLL = (LinearLayout) view.findViewById(R.id.fragment_mine_follow_me_ll);
        mCareLL = (LinearLayout) view.findViewById(R.id.fragment_mine_care_ll);
        //个人信息编辑
        mEditInfoIv = (ImageView) view.findViewById(R.id.fragment_mine_edit_info);

        //设置
        mSettingRl = (RelativeLayout) view.findViewById(R.id.fragment_mine_setting);
        //我的订单
        mineOrder = (RelativeLayout) view.findViewById(R.id.fragment_mine_order);
        //我的认证
        mineAuth = (RelativeLayout) view.findViewById(R.id.fragment_mine_auth_rl);
        //我的钱包
        mineWallet = (RelativeLayout) view.findViewById(R.id.fragment_mine_wallet);
        //我的相册
        mineAlbums = (RelativeLayout) view.findViewById(R.id.fragment_mine_albums);
        feedBack = (RelativeLayout) view.findViewById(R.id.fragment_mine_feedback);
        //我的动态
        mineMoments = (RelativeLayout) view.findViewById(R.id.fragment_mine_moments);
        //签到
        signIv = (ImageView) view.findViewById(R.id.fragment_mine_sign);
        //我的档期
        mineSchedule = (RelativeLayout) view.findViewById(R.id.fragment_mine_schedule);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.fragment_mine_scrollview);
        //我的分享
        mineShare = (RelativeLayout) view.findViewById(R.id.fragment_mine_share);
        //我的通告
        mNotice = (RelativeLayout) view.findViewById(R.id.fragment_mine_notice);
        initEvents();
        return view;
    }

    private void initEvents() {
        mEditInfoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), EditActivity.class));
            }
        });
        mSettingRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
        mineOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastUtil.show(getContext(),"网络连接不可用，请稍后重试");
                startActivity(new Intent(getActivity(), MineOrderActivity.class));
            }
        });
        mineAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AuthenticationActivity.class));
            }
        });

        mineWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MineWalletActivity.class));
            }
        });

        mineAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AlbumsActivity.class)
                        .putExtra("userId", SpDataCache.getSelfInfo(getContext()).getData().getM_id())
                        .putExtra("userType", 2)
                        .putExtra("delete", true)
                        .putExtra("upload", true));
            }
        });

        feedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Feedback.class);
                startActivity(intent);
            }
        });

        mineMoments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MomentsActivity.class)
                        .putExtra("userId", SpDataCache.getSelfInfo(getContext()).getData().getM_id())
                        .putExtra("userType", 2)
                        .putExtra("delete", true)
                );
            }
        });

        signIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("member_id", SpDataCache.getSelfInfo(getContext()).getData().getM_id());
                Req.post(Url.sign, map, new Req.ReqCallback() {
                    @Override
                    public void success(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String code = jsonObject.getString("code");
                            String tips = jsonObject.getString("tips");
//                            ToastUtil.show(getContext(), tips);
//                            Log.e("MineFragment:签到success",tips);

                            //判断用户是否已经认证，确定认证跳转认证界面，否则取消dialog
                            if ("202".equals(code)){
                                //未认证
                                View view = LayoutInflater.from(getContext()).inflate(R.layout.renzheng_dialog, null);
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setView(view);

                                dialog = builder.create();
                                dialog.show();

                                TextView content= (TextView) view.findViewById(R.id.renzheng_content);
                                content.setText(tips);
                                //立即认证
                                Button yesRenzheng = (Button) view.findViewById(R.id.yesrenzheng);
                                yesRenzheng.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //跳转认证界面
                                        startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                                        dialog.dismiss();

                                    }
                                });
                                //稍后认证
                                Button noRenzheng = (Button) view.findViewById(R.id.norenzheng);
                                noRenzheng.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });



                            }


                            if (!"200".equals(code)) {
                                return;
                            }
                            signIv.setImageResource(R.mipmap.icon_qiandao_on);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(String msg) {
                        ToastUtil.show(getContext(), msg);
                        Log.e("MineFragment:签到失败信息",msg);
                    }

                    @Override
                    public void completed() {
                    }
                });
            }
        });

        mineSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MineScheduleActivity.class));
            }
        });
        mineShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int m_patner = SpDataCache.getSelfInfo(getContext()).getData().getM_patner();
//                if (m_patner == 0) {
//                    showShare();
//                } else if (m_patner == 1) {
                String m_invite_code = String.valueOf(SpDataCache.getSelfInfo(getContext()).getData().getM_account());
                if (TextUtils.isEmpty(m_invite_code)) {
                    showShare();
                } else {
                    Loger.log(TAG, "m_invite_code" + m_invite_code);
                    Intent intent1 = new Intent(getContext(), ShareActivity.class);
                    intent1.putExtra("code", m_invite_code);
                    startActivity(intent1);
                }
//                }
            }
        });

        mFollowMeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginBean loginBean = SpDataCache.getSelfInfo(getActivity());
                if (null == loginBean) {
                    ToastUtil.show(getActivity(), "您还未登录，请先登录");
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                startActivity(new Intent(getActivity(), WatchActivity.class)
                        .putExtra("memberId", loginBean.getData().getM_id())
                        .putExtra("reqType", "2")
                        .putExtra("title", "关注我的"));
            }
        });

        mCareLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginBean loginBean = SpDataCache.getSelfInfo(getActivity());
                if (null == loginBean) {
                    ToastUtil.show(getActivity(), "您还未登录，请先登录");
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                startActivity(new Intent(getActivity(), WatchActivity.class)
                        .putExtra("memberId", loginBean.getData().getM_id())
                        .putExtra("reqType", "1")
                        .putExtra("title", "我关注的"));
            }
        });

        mNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MineNoticeActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            initDatas();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            initDatas();
        }
    }

    private void initDatas() {
        mLoginBean = SpDataCache.getSelfInfo(getContext());
        if (null == mLoginBean) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", mLoginBean.getData().getM_id());
        Req.post(Url.minePage, map, new Req.ReqCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void success(String result) {
                LogDebug.log(TAG, result);
                mPersonageBean = new Gson().fromJson(result, PersonageBean.class);
                if (!"200".equals(mPersonageBean.getCode())) {
                    ToastUtil.show(getContext(), mPersonageBean.getTips());
                    return;
                }
                if (mPersonageBean.getSign() == 0) {
                    signIv.setImageResource(R.mipmap.icon_qiandao_off);
                } else {
                    signIv.setImageResource(R.mipmap.icon_qiandao_on);
                }
                ImageLoad.bind(mPortraitIv, mPersonageBean.getMember().getM_head_photo());
                ImageLoad.bind(mBlurIv, mPersonageBean.getMember().getM_head_photo(), 3);
                mNameTv.setText(mPersonageBean.getMember().getM_nick_name());
                String stringBuilder = String.valueOf(mPersonageBean.getMember().getM_age())
                        + "岁 "
                        + String.valueOf(mPersonageBean.getMember().getM_tall())
                        + "cm / "
                        + String.valueOf(mPersonageBean.getMember().getM_weight())
                        + "kg "
                        + String.valueOf(mPersonageBean.getMember().getM_bust())
                        + "/"
                        + String.valueOf(mPersonageBean.getMember().getM_waist())
                        + "/"
                        + String.valueOf(mPersonageBean.getMember().getM_hips());
                mPartNumberTv.setText(stringBuilder);

                if (mPersonageBean.getMember().getM_model_state() == 2) {
                    mApproveCardTv.setText("模卡已认证");
                } else {
                    mApproveCardTv.setText("模卡未认证");
                }
                if (mPersonageBean.getMember().getM_video_state() == 2) {
                    mApproveVideoTv.setText("视频已认证");
                } else {
                    mApproveVideoTv.setText("视频未认证");
                }

                mFollowMeTv.setText(mPersonageBean.getMember().getB_num() + "");
                mCareTv.setText(mPersonageBean.getMember().getNum() + "");

            }

            @Override
            public void failed(String msg) {
                Loger.log(TAG, msg);
            }

            @Override
            public void completed() {

            }
        });
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
        oks.setImageUrl(Url.LOGO_URL_REMOTE);
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
        oks.show(getContext());
    }
}
