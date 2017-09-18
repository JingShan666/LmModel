package wei.moments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liemo.shareresource.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wei.moments.base.BaseActivity;
import wei.moments.bean.LoginBean;
import wei.moments.bean.MomentListItemBean;
import wei.moments.database.SPUtils;
import wei.moments.network.ReqCallback;
import wei.moments.network.ReqUrl;
import wei.toolkit.utils.ImageLoad;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.PictureUtil;
import wei.toolkit.utils.ToastUtil;
import wei.toolkit.widget.CircleImageBorderView;
import wei.toolkit.widget.VideoPlayView;

/**
 * Created by Administrator on 2017/4/17 0017.
 */
@Deprecated
public class MomentDetailsVideoActivity extends BaseActivity {
    public static final String TAG = "MomentDetailsPictureActivity";
    private TabLayout mTabLayout;
    private ViewPager mBaseViewPager;
    private List<Fragment> mFragments;
    private String[] titles = {"评论", "送花"};
    private MomentListItemBean mMomentListItemBean;

    private ImageView mBack;
    private VideoPlayView mVideoPlayView;
    private ImageView mThumbnail;
    private ImageView mPlayIcon;
    private RelativeLayout mPlay;
    private ProgressBar mProgressBar;
    private TextView mContent;
    private TextView mPraise;
    private TextView mFlower;
    private CircleImageBorderView mCircleImageView;
    private TextView mName;
    private TextView mAge;
    private ImageView mWatch;
    private String thumbnail = "";
    private String videoUrl = "";
    /*如果getIntent能获取到值证明是数据源中的位置,关闭activity时 setResult 再传回去。*/
    private int dataPosition = -1;

    private ImageView roteFlag;
    private String contentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lm_activity_moment_details_video);

        contentId = getIntent().getStringExtra("contentId");
        mMomentListItemBean = getIntent().getParcelableExtra("data");
        dataPosition = getIntent().getIntExtra("dataPosition", -1);
        String userId = "";
        String useType = "3";
        String content_id = "";
        if (!TextUtils.isEmpty(contentId)) {
            content_id = contentId;
        } else if (mMomentListItemBean != null) {
            content_id = mMomentListItemBean.getContent_id();
        } else {
            Toast.makeText(this, "数据读取失败", Toast.LENGTH_SHORT).show();
            finish();
        }

        LoginBean loginBean = SPUtils.getSelfInfo(this);
        if (null != loginBean) {
            userId = loginBean.getData().getM_id();
            useType = SPUtils.ROLE_TYPE_DEF;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("use_id", userId);
        map.put("use_type", useType);
        map.put("content_id", content_id);
        ReqUrl.post(Url.momentDetailas, map, new ReqCallback.Callback<String>() {
            @Override
            public void success(String result) {
                Loger.log(TAG, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if ("200".equals(code)) {
                        mMomentListItemBean = new Gson().fromJson(jsonObject.getString("contents"), MomentListItemBean.class);
                    } else {
                        if (!TextUtils.isEmpty(contentId)) {
                            ToastUtil.show(MomentDetailsVideoActivity.this, jsonObject.getString("tips"));
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initViews();

            }

            @Override
            public void failed(String msg) {
                Loger.log(TAG, msg);
                if (!TextUtils.isEmpty(contentId)) {
                    ToastUtil.show(MomentDetailsVideoActivity.this, getResources().getString(R.string.error_network_block));
                    finish();
                }
                initViews();
            }
        });
    }


    public void initViews() {

        mBack = (ImageView) findViewById(R.id.activity_moment_details_video_back);
        mVideoPlayView = (VideoPlayView) findViewById(R.id.activity_moment_details_video);
        mThumbnail = (ImageView) findViewById(R.id.activity_moment_details_video_thumbnail);
        mPlayIcon = (ImageView) findViewById(R.id.activity_moment_details_video_play_icon);
        mPlay = (RelativeLayout) findViewById(R.id.activity_moment_details_video_play);
        mContent = (TextView) findViewById(R.id.activity_moment_details_video_conetnt);
        mPraise = (TextView) findViewById(R.id.activity_moment_details_video_praise);
        mFlower = (TextView) findViewById(R.id.activity_moment_details_video_flower);
        mCircleImageView = (CircleImageBorderView) findViewById(R.id.activity_moment_details_video_portrait);
        mName = (TextView) findViewById(R.id.activity_moment_details_video_name);
        mAge = (TextView) findViewById(R.id.activity_moment_details_video_age);
        mWatch = (ImageView) findViewById(R.id.activity_moment_details_video_watch);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_moment_details_video_progress);
        roteFlag = (ImageView) findViewById(R.id.activity_moment_details_video_role_flag);
        mFragments = new ArrayList<>();
        mFragments.add(new CommentFragment(mMomentListItemBean));
        mFragments.add(new SendFlowerFragment(mMomentListItemBean.getContent_id()));
        mTabLayout = (TabLayout) findViewById(R.id.activity_moment_details_video_tablayout);
        mBaseViewPager = (ViewPager) findViewById(R.id.activity_moment_details_video_viewpager);
        mTabLayout.setupWithViewPager(mBaseViewPager);
        mBaseViewPager.setAdapter(new VpAdapter(getSupportFragmentManager()));

        initDatas();
        initEvents();
    }

    private void initDatas() {
        ImageLoad.bind(mCircleImageView, mMomentListItemBean.getHead_photo());

        try {
            JSONObject jsonObject = new JSONObject(mMomentListItemBean.getVideo_path());
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            if (jsonArray.length() > 0) {
                JSONObject object = jsonArray.getJSONObject(0);
                thumbnail = object.getString("thumbnail");
                videoUrl = object.getString("video");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(thumbnail)) {
            ImageLoad.bind(mThumbnail, thumbnail);
        } else {
            if (!TextUtils.isEmpty(videoUrl)) {
                Observable.create(new ObservableOnSubscribe<Bitmap>() {
                    @Override
                    public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                        e.onNext(PictureUtil.getNetworkVideoThumbnail(videoUrl));
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Bitmap>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(Bitmap value) {
                                mThumbnail.setImageBitmap(value);
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
            }
        }
        mName.setText(mMomentListItemBean.getNames());
        mAge.setText(mMomentListItemBean.getUse_age());
        mContent.setText(mMomentListItemBean.getContent());
        mPraise.setText("赞(" + mMomentListItemBean.getZan_num() + ")");
        mFlower.setText("花(" + mMomentListItemBean.getGift_num() + ")");

        if ("1".equals(mMomentListItemBean.getZan_Y())) {
            mPraise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.lm_icon_praise_on, 0, 0, 0);
        } else {
            mPraise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.lm_icon_praise_off, 0, 0, 0);
        }

        if ("2".equals(mMomentListItemBean.getContent_sex())) {
            mWatch.setVisibility(View.GONE);
            roteFlag.setVisibility(View.VISIBLE);
        } else {
            roteFlag.setVisibility(View.GONE);
            mAge.setBackgroundResource(R.mipmap.lm_bg_age_man);
            mWatch.setVisibility(View.VISIBLE);
            if ("1".equals(mMomentListItemBean.getGuanz_Y())) {
                mWatch.setImageResource(R.mipmap.lm_bg_watch_on);
            } else {
                mWatch.setImageResource(R.mipmap.lm_bg_watch_off);
            }
        }


    }

    private void setDataCallback() {
        if (dataPosition > -1) {
            if (null != mMomentListItemBean) {
                Intent intent = new Intent();
                intent.putExtra("data", mMomentListItemBean);
                intent.putExtra("dataPosition", dataPosition);
                setResult(RESULT_OK, intent);
            }
        }
    }

    private void videoReset() {
        if (mVideoPlayView != null) {
            mVideoPlayView.destroy();
            mThumbnail.setVisibility(View.VISIBLE);
            mPlayIcon.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        setDataCallback();
        super.onBackPressed();
    }

    private void initEvents() {

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoReset();
                setDataCallback();
                finish();
            }
        });

        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mMomentListItemBean.getContent_sex()) && mMomentListItemBean.getContent_sex().equals("1")) {
                    startActivity(new Intent("bbc.com.moteduan_lib2.UserInfoActivity").putExtra("userId", mMomentListItemBean.getUse_id()));
                }
            }
        });

        // 播放视频
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(videoUrl)) {
                    ToastUtil.show(MomentDetailsVideoActivity.this, "视频地址未找到,播放失败");
                    return;
                }
                if (mVideoPlayView.isPlaying()) {
                    videoReset();
                } else {
                    videoReset();
                    mProgressBar.setVisibility(View.VISIBLE);
                    mPlayIcon.setVisibility(View.INVISIBLE);
                    mVideoPlayView.setPath(videoUrl);
                    mVideoPlayView.prepareAsync();
                }
            }
        });

        mVideoPlayView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoReset();
            }
        });
        mVideoPlayView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mThumbnail.setVisibility(View.INVISIBLE);
                mp.start();
            }
        });

        mPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginBean loginBean = SPUtils.getSelfInfo(MomentDetailsVideoActivity.this);
                if (null == loginBean) {
                    ToastUtil.show(MomentDetailsVideoActivity.this, "您还未登录，请先登录");
                    Intent intent = new Intent();
                    intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
                    startActivity(intent);
                    return;
                }
                Map map = new HashMap();
                map.put("content_id", mMomentListItemBean.getContent_id());
                map.put("use_id", loginBean.getData().getM_id());
                map.put("use_type", ContRes.getSelfType());
                map.put("b_use_type", mMomentListItemBean.getContent_sex());
                map.put("b_use_id", mMomentListItemBean.getUse_id());
                ReqUrl.post(Url.clickPraise, map, new ReqCallback.Callback<String>() {
                    @Override
                    public void success(String backPamam) {
                        try {
                            JSONObject jsonObject = new JSONObject(backPamam);
                            String code = jsonObject.getString("code");
                            if (TextUtils.equals("200", code)) {
                                mMomentListItemBean.setZan_Y("1");
                                int i = Integer.parseInt(mMomentListItemBean.getZan_num());
                                ++i;
                                mMomentListItemBean.setZan_num(String.valueOf(i));
                            } else if (TextUtils.equals("202", code)) {
                                mMomentListItemBean.setZan_Y("0");
                                int i = Integer.parseInt(mMomentListItemBean.getZan_num());
                                if (i > 0) {
                                    --i;
                                    mMomentListItemBean.setZan_num(String.valueOf(i));
                                }
                            }
                            mPraise.setText("赞(" + mMomentListItemBean.getZan_num() + ")");
                            if ("1".equals(mMomentListItemBean.getZan_Y())) {
                                mPraise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.lm_icon_praise_on, 0, 0, 0);
                            } else {
                                mPraise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.lm_icon_praise_off, 0, 0, 0);
                            }
                            ToastUtil.show(MomentDetailsVideoActivity.this, jsonObject.getString("tips"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(String msg) {
                        ToastUtil.show(MomentDetailsVideoActivity.this, msg);
                    }
                });
            }
        });


        mFlower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LoginBean loginBean = SPUtils.getSelfInfo(MomentDetailsVideoActivity.this);
//                if (null == loginBean) {
//                    ToastUtil.show(MomentDetailsVideoActivity.this, "您还未登录，请先登录");
//                    Intent intent = new Intent();
//                    intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
//                    startActivity(intent);
//                    return;
//                }
//                Map map = new HashMap();
//                map.put("content_id", mMomentListItemBean.getContent_id());
//                map.put("use_id", loginBean.getData().getM_id());
//                map.put("use_type", ContRes.getSelfType());
//                map.put("b_use_type", mMomentListItemBean.getContent_sex());
//                map.put("b_use_id", mMomentListItemBean.getUse_id());
//                ReqUrl.post(Url.sendFlower, map, new ReqCallback.Callback<String>() {
//                    @Override
//                    public void success(String backPamam) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(backPamam);
//                            String code = jsonObject.getString("code");
//                            if (!TextUtils.equals("200", code)) {
//                                ToastUtil.show(MomentDetailsVideoActivity.this, jsonObject.getString("tips"));
//                                return;
//                            }
//                            int i = Integer.parseInt(mMomentListItemBean.getGift_num());
//                            ++i;
//                            mMomentListItemBean.setGift_num(String.valueOf(i));
//                            mFlower.setText("送花(" + mMomentListItemBean.getGift_num() + ")");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void failed(String msg) {
//                        ToastUtil.show(MomentDetailsVideoActivity.this, msg);
//                    }
//                });
            }
        });

        mWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginBean loginBean = SPUtils.getSelfInfo(MomentDetailsVideoActivity.this);
                if (null == loginBean) {
                    ToastUtil.show(MomentDetailsVideoActivity.this, "您还未登录，请先登录");
                    Intent intent = new Intent();
                    intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
                    startActivity(intent);
                    return;
                }
                Map map = new HashMap();
                map.put("use_id", loginBean.getData().getM_id());
                map.put("use_type", ContRes.getSelfType());
                map.put("b_use_id", mMomentListItemBean.getUse_id());
                map.put("b_use_type", mMomentListItemBean.getContent_sex());
                ReqUrl.post(Url.watchMoments, map, new ReqCallback.Callback<String>() {
                    @Override
                    public void success(String backPamam) {
                        try {
                            JSONObject jsonObject = new JSONObject(backPamam);
                            String code = jsonObject.getString("code");
                            if (TextUtils.equals("200", code)) {
                                mMomentListItemBean.setGuanz_Y("1");
                                mWatch.setImageResource(R.mipmap.lm_bg_watch_on);
                            } else if (TextUtils.equals("202", code)) {
                                mMomentListItemBean.setGuanz_Y("0");
                                mWatch.setImageResource(R.mipmap.lm_bg_watch_off);
                            }
                            ToastUtil.show(MomentDetailsVideoActivity.this, jsonObject.getString("tips"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(String msg) {

                    }
                });
            }
        });
    }


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

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
