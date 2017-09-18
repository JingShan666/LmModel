package wei.moments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.liemo.shareresource.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import wei.moments.base.BaseRecycleView;
import wei.moments.base.BaseRvAdapter;
import wei.moments.bean.LoginBean;
import wei.moments.bean.MomentListContentBean;
import wei.moments.bean.MomentListItemBean;
import wei.moments.database.SPUtils;
import wei.moments.decoration.LinelayoutDecoration;
import wei.moments.holder.MomentPictureHolder;
import wei.moments.holder.MomentVideoHolder;
import wei.moments.holder.PictureHoder;
import wei.moments.network.ReqCallback;
import wei.moments.network.ReqUrl;
;
import wei.toolkit.WatchPictureActivity;
import wei.toolkit.helper.EmptyDataViewHolder;
import wei.toolkit.utils.DateUtils;
import wei.toolkit.utils.ImageLoad;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.PictureUtil;
import wei.toolkit.utils.SnackbarUtil;
import wei.toolkit.utils.TextTools;
import wei.toolkit.utils.ToastUtil;


/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class OtherMomentsActivity extends BaseActivity {
    private static final String TAG = "OtherMomentsActivity";
    private ImageView back;
    private TextView title;

    private BaseRecycleView recycleView;
    private SpringView mSpringView;
    private long mTimeStamp;
    private int mCurrentPage = 1;
    private int mCurrentPageSize = 10;

    private List<MomentListItemBean> mItemBeans = new ArrayList<>();
    private MomentsAdapter mMomentsAdapter;
    private MomentVideoHolder mMomentVideoHolder;
    private static String userId = "";
    private static int userType;
    private static String userName = "";
    private static final int REQUEST_CODE_DETAILS = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lm_activity_momoents);
        userId = getIntent().getStringExtra("userId");
        if (TextUtils.isEmpty(userId)) {
            ToastUtil.show(OtherMomentsActivity.this, "数据开小差了哦，暂时无法查看动态。");
            finish();
            return;
        }
        userName = getIntent().getStringExtra("userName");
        if (TextUtils.isEmpty(userName)) userName = "我";
        userType = getIntent().getIntExtra("userType", 1);
        back = (ImageView) findViewById(R.id.bar_action_back);
        title = (TextView) findViewById(R.id.bar_action_title);
        title.setText(userName + "的动态");
        mSpringView = (SpringView) findViewById(R.id.fragment_moments_springview);
        mSpringView.setHeader(new DefaultHeader(OtherMomentsActivity.this));
        mSpringView.setFooter(new DefaultFooter(OtherMomentsActivity.this));
        mSpringView.setListener(onFreshListener);
        recycleView = (BaseRecycleView) findViewById(R.id.fragment_moments_rv);
        final LinearLayoutManager manager = new LinearLayoutManager(OtherMomentsActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(manager);
        recycleView.addItemDecoration(new LinelayoutDecoration());
        recycleView.addOnScrollListener(onScrollListener);
        mMomentsAdapter = new MomentsAdapter();
        initEvents();
        initData(1);
    }

    private SpringView.OnFreshListener onFreshListener = new SpringView.OnFreshListener() {
        @Override
        public void onRefresh() {
            initData(1);
        }

        @Override
        public void onLoadmore() {
            initData(2);
        }
    };

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mMomentVideoHolder != null) {
                Rect rect = new Rect();
                mMomentVideoHolder.itemView.getHitRect(rect);
                if ((rect.bottom - recyclerView.getTop()) < 1 || (recyclerView.getBottom() - rect.top) < 50) {
                    itemVideoReset();
                }
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        itemVideoReset();
    }

    // 1下拉 2上拉
    private void initData(final int type) {
        LoginBean loginBean = SPUtils.getSelfInfo(OtherMomentsActivity.this);
        Map<String, Object> map = new HashMap<>();
        String aid = "";
        String atype = "3";
        if (null != loginBean) {
            aid = loginBean.getData().getM_id();
            atype = "2";
        }
        map.put("use_id", aid);
        map.put("use_type", atype);
        map.put("b_use_id", userId);
        map.put("b_use_type", userType);
        if (1 == type) {
            map.put("timeStamp", mTimeStamp < 1 ? System.currentTimeMillis() : mTimeStamp);
            map.put("pageNumber", mCurrentPage = 1);
        } else if (2 == type) {
            map.put("timeStamp", mTimeStamp);
            map.put("pageNumber", ++mCurrentPage);
        }
        map.put("pageSize", mCurrentPageSize);
        ReqUrl.post(Url.peekOtherMomentList, map, new ReqCallback.Callback1<String>() {


            @Override
            public void success(String result) {
                itemVideoReset();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Loger.log(TAG, jsonObject.toString());
                    String code = jsonObject.getString("code");
                    long timeStamp = 0;
                    if (jsonObject.has("timeStamp")) {
                        timeStamp = jsonObject.getLong("timeStamp");
                    }
                    if (!TextUtils.equals("200", code)) {
                        String tips = jsonObject.getString("tips");
                        if ("201".equals(code)) {
                            if (1 == type) {
                                if (mItemBeans.size() > 0) {
                                    mItemBeans.clear();
                                }
                                recycleView.setAdapter(mMomentsAdapter);
                                return;
                            } else if (type == 2) {
                                tips = getResources().getString(R.string.sr_no_more_data);
                            }
                        }
                        SnackbarUtil.show(recycleView, tips);
                        return;
                    }
                    MomentListContentBean bean = new Gson().fromJson(jsonObject.getString("contents"), MomentListContentBean.class);
                    if (1 == type) {
                        mItemBeans.clear();
                        mItemBeans.addAll(bean.getList());
                        mTimeStamp = timeStamp > 0 ? timeStamp : System.currentTimeMillis();
                        recycleView.setAdapter(mMomentsAdapter);
                    } else {
                        mItemBeans.addAll(bean.getList());
                        mMomentsAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {
                ToastUtil.show(OtherMomentsActivity.this, "获取数据失败" + msg);
            }

            @Override
            public void completed() {
                mSpringView.onFinishFreshAndLoad();
            }
        });
    }

    private void initEvents() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void itemVideoReset() {
        if (mMomentVideoHolder != null) {
            mMomentVideoHolder.mVideoView.destroy();
            mMomentVideoHolder.mThumbnail.setVisibility(View.VISIBLE);
            mMomentVideoHolder.mPlay.setVisibility(View.VISIBLE);
            mMomentVideoHolder.mProgressBar.setVisibility(View.INVISIBLE);
            mMomentVideoHolder = null;
        }
    }

    private class MomentsAdapter extends BaseRvAdapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == -1) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.helper_empty_data_view, parent, false);
                return new EmptyDataViewHolder(view);
            } else if (viewType == 1) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lm_item_moment_picture, parent, false);
                return new MomentPictureHolder(view);
            } else if (viewType == 2) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lm_item_moment_video, parent, false);
                return new MomentVideoHolder(view);
            }

            return /*返回个Holder，防止空指针*/new RecyclerView.ViewHolder(new View(parent.getContext())) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof EmptyDataViewHolder) {
                holder.itemView.setVisibility(View.VISIBLE);
            } else if (holder instanceof MomentPictureHolder) {
                final MomentListItemBean itemBean = mItemBeans.get(position);
                // 照片和纯文本布局
                final MomentPictureHolder h = (MomentPictureHolder) holder;
                ImageLoad.bind(h.mPortrait, itemBean.getHead_photo());
                h.mName.setText(itemBean.getNames());
                h.mAge.setText(itemBean.getUse_age());
                h.mContent.setText(TextTools.Url.replaceUrlToText(itemBean.getContent()));
                h.mPraise.setText("赞(" + itemBean.getZan_num() + ")");
                h.mFlower.setText("花(" + itemBean.getGift_num() + ")");
                h.mComment.setText("评论(" + itemBean.getPin_num() + ")");
                if (!TextUtils.isEmpty(itemBean.getSend_addres())) {
                    h.sendAddress.setVisibility(View.VISIBLE);
                    h.sendAddress.setText(itemBean.getSend_addres());
                } else {
                    h.sendAddress.setVisibility(View.GONE);
                }
                String sendtime = "";
                if (!TextUtils.isEmpty(itemBean.getSend_time())) {
                    long timeDiff = DateUtils.getTimeDiff(itemBean.getSend_time());
                    if (timeDiff > 0) {
                        int minute = (int) (timeDiff / 1000 / 60);
                        try {
                            if (minute < 60) {
                                sendtime = minute + "分钟前";
                            } else if (minute < 24 * 60) {
                                sendtime = minute / 60 + "小时前";
                            } else if (minute < 7 * 24 * 60) {
                                sendtime = minute / (24 * 60) + "天前";
                            } else {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(itemBean.getSend_time()));
                                if (DateUtils.isToyear(itemBean.getSend_time())) {
                                    sendtime = calendar.get(Calendar.MONTH) + 1 + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                                } else {
                                    sendtime = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + 1 + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                h.sendTime.setText(sendtime);
                if (!TextUtils.isEmpty(itemBean.getPhotos())) {
                    final MomentListItemBean.PhotoList photoList = new Gson().fromJson(itemBean.getPhotos(), MomentListItemBean.PhotoList.class);
                    if (photoList.getList() != null) {
                        if (photoList.getList().size() == 1) {
                            h.mPictureSingle.setVisibility(View.VISIBLE);
                            h.mPicRv.setVisibility(View.GONE);
                            ImageLoad.bind(h.mPictureSingle, photoList.getList().get(0).getUrl(), 600, 600);
                        } else if (photoList.getList().size() > 1) {
                            h.mPictureSingle.setVisibility(View.GONE);
                            h.mPicRv.setVisibility(View.VISIBLE);
                            h.mPicRv.setAdapter(new PictureAdapter(h.mPicRv, photoList.getList()));
                        } else if (photoList.getList().size() < 1) {
                            h.mPictureSingle.setVisibility(View.GONE);
                            h.mPicRv.setVisibility(View.GONE);
                        }
                        h.mPictureSingle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(OtherMomentsActivity.this, WatchPictureActivity.class);
                                List<String> list = new ArrayList<String>();
                                list.add(photoList.getList().get(0).getUrl());
                                intent.putStringArrayListExtra("data", (ArrayList<String>) list);
                                startActivity(intent);
                            }
                        });
                    } else {
                        h.mPictureSingle.setVisibility(View.GONE);
                        h.mPicRv.setVisibility(View.GONE);
                    }
                } else {
                    h.mPictureSingle.setVisibility(View.GONE);
                    h.mPicRv.setVisibility(View.GONE);
                }

                if ("1".equals(itemBean.getZan_Y())) {
                    h.mPraise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.lm_icon_praise_on, 0, 0, 0);
                } else {
                    h.mPraise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.lm_icon_praise_off, 0, 0, 0);
                }

                if ("2".equals(itemBean.getContent_sex())) {
                    h.mAge.setBackgroundResource(R.mipmap.lm_bg_age);
                } else {
                    h.mAge.setBackgroundResource(R.mipmap.lm_bg_age_man);
                }

//                h.mWatch.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        LoginBean loginBean = SPUtils.getSelfInfo(getActivity());
//                        if (null == loginBean) {
//                            ToastUtil.show(getActivity(), "您还未登录，请先登录");
//                            Intent intent = new Intent();
//                            intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
//                            getActivity().startActivity(intent);
//                            return;
//                        }
//                        Map map = new HashMap();
//                        map.put("use_id", loginBean.getData().getM_id());
//                        map.put("use_type", ContRes.getSelfType());
//                        map.put("b_use_id", itemBean.getUse_id());
//                        map.put("b_use_type", itemBean.getContent_sex());
//                        ReqUrl.post(Url.watchMoments, map, new ReqCallback.Callback<String>() {
//                            @Override
//                            public void success(String backPamam) {
//                                try {
//                                    JSONObject jsonObject = new JSONObject(backPamam);
//                                    String code = jsonObject.getString("code");
//                                    if (TextUtils.equals("200", code)) {
//                                        itemBean.setGuanz_Y("1");
//                                        h.mWatch.setImageResource(R.mipmap.lm_bg_watch_on);
//                                    } else if (TextUtils.equals("202", code)) {
//                                        itemBean.setGuanz_Y("0");
//                                        h.mWatch.setImageResource(R.mipmap.lm_bg_watch_off);
//                                    }
//                                    ToastUtil.show(getContext(), jsonObject.getString("tips"));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void failed(String msg) {
//
//                            }
//                        });
//                    }
//                });
                h.mFlower.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        LoginBean loginBean = SPUtils.getSelfInfo(OtherMomentsActivity.this);
//                        if (null == loginBean) {
//                            ToastUtil.show(OtherMomentsActivity.this, "您还未登录，请先登录");
//                            Intent intent = new Intent();
//                            intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
//                            OtherMomentsActivity.this.startActivity(intent);
//                            return;
//                        }
//                        Map<String, Object> map = new HashMap<String, Object>();
//                        map.put("content_id", itemBean.getContent_id());
//                        map.put("use_id", loginBean.getData().getM_id());
//                        map.put("use_type", ContRes.getSelfType());
//                        map.put("b_use_type", itemBean.getContent_sex());
//                        map.put("b_use_id", itemBean.getUse_id());
//                        ReqUrl.post(Url.sendFlower, map, new ReqCallback.Callback<String>() {
//                            @Override
//                            public void success(String backPamam) {
//                                try {
//                                    JSONObject jsonObject = new JSONObject(backPamam);
//                                    String code = jsonObject.getString("code");
//                                    if (!TextUtils.equals("200", code)) {
//                                        ToastUtil.show(OtherMomentsActivity.this, jsonObject.getString("tips"));
//                                        return;
//                                    }
//                                    int i = Integer.parseInt(itemBean.getGift_num());
//                                    ++i;
//                                    itemBean.setGift_num(String.valueOf(i));
//                                    h.mFlower.setText("送花(" + itemBean.getGift_num() + ")");
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void failed(String msg) {
//                                ToastUtil.show(OtherMomentsActivity.this, msg);
//                            }
//                        });
                    }
                });
                h.mPraise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginBean loginBean = SPUtils.getSelfInfo(OtherMomentsActivity.this);
                        if (null == loginBean) {
                            ToastUtil.show(OtherMomentsActivity.this, "您还未登录，请先登录");
                            Intent intent = new Intent();
                            intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
                            OtherMomentsActivity.this.startActivity(intent);
                            return;
                        }
                        Map map = new HashMap();
                        map.put("content_id", itemBean.getContent_id());
                        map.put("use_id", loginBean.getData().getM_id());
                        map.put("use_type", ContRes.getSelfType());
                        map.put("b_use_type", itemBean.getContent_sex());
                        map.put("b_use_id", itemBean.getUse_id());
                        ReqUrl.post(Url.clickPraise, map, new ReqCallback.Callback<String>() {
                            @Override
                            public void success(String backPamam) {
                                try {
                                    JSONObject jsonObject = new JSONObject(backPamam);
                                    String code = jsonObject.getString("code");
                                    if (TextUtils.equals("200", code)) {
                                        itemBean.setZan_Y("1");
                                        int i = Integer.parseInt(itemBean.getZan_num());
                                        ++i;
                                        itemBean.setZan_num(String.valueOf(i));
                                    } else if (TextUtils.equals("202", code)) {
                                        itemBean.setZan_Y("0");
                                        int i = Integer.parseInt(itemBean.getZan_num());
                                        if (i > 0) {
                                            --i;
                                            itemBean.setZan_num(String.valueOf(i));
                                        }
                                    }
                                    h.mPraise.setText("赞(" + itemBean.getZan_num() + ")");
                                    if ("1".equals(itemBean.getZan_Y())) {
                                        h.mPraise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.lm_icon_praise_on, 0, 0, 0);
                                    } else {
                                        h.mPraise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.lm_icon_praise_off, 0, 0, 0);
                                    }
                                    ToastUtil.show(OtherMomentsActivity.this, jsonObject.getString("tips"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failed(String msg) {
                                ToastUtil.show(OtherMomentsActivity.this, msg);
                            }
                        });


                    }
                });

                h.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(OtherMomentsActivity.this, MomentDetailsActivity.class)
                                .putExtra("data", itemBean)
                                .putExtra("dataPosition", position), REQUEST_CODE_DETAILS);
                    }
                });


            } else if (holder instanceof MomentVideoHolder) {
                final MomentListItemBean itemBean = mItemBeans.get(position);
                // 视频布局----------------------------------------------------
                final MomentVideoHolder h = (MomentVideoHolder) holder;
                ImageLoad.bind(h.mPortrait, itemBean.getHead_photo());
                h.mName.setText(itemBean.getNames());
                h.mAge.setText(itemBean.getUse_age());
                h.mContent.setText(TextTools.Url.replaceUrlToText(itemBean.getContent()));
                h.mPraise.setText("赞(" + itemBean.getZan_num() + ")");
                h.mFlower.setText("花(" + itemBean.getGift_num() + ")");
                h.mComment.setText("评论(" + itemBean.getPin_num() + ")");
                if (!TextUtils.isEmpty(itemBean.getSend_addres())) {
                    h.sendAddress.setVisibility(View.VISIBLE);
                    h.sendAddress.setText(itemBean.getSend_addres());
                } else {
                    h.sendAddress.setVisibility(View.GONE);
                }
                String sendtime = "";
                if (!TextUtils.isEmpty(itemBean.getSend_time())) {
                    long timeDiff = DateUtils.getTimeDiff(itemBean.getSend_time());
                    if (timeDiff > 0) {
                        int minute = (int) (timeDiff / 1000 / 60);
                        try {
                            if (minute < 60) {
                                sendtime = minute + "分钟前";
                            } else if (minute < 24 * 60) {
                                sendtime = minute / 60 + "小时前";
                            } else if (minute < 7 * 24 * 60) {
                                sendtime = minute / (24 * 60) + "天前";
                            } else {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(itemBean.getSend_time()));
                                if (DateUtils.isToyear(itemBean.getSend_time())) {
                                    sendtime = calendar.get(Calendar.MONTH) + 1 + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                                } else {
                                    sendtime = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + 1 + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                h.sendTime.setText(sendtime);
                String thumbnail = "";
                String videoUrl = "";
                try {
                    JSONObject jsonObject = new JSONObject(itemBean.getVideo_path());
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
                    ImageLoad.bind(h.mThumbnail, thumbnail);
                } else {
                    if (!TextUtils.isEmpty(videoUrl)) {
                        final String finalVideoUrl = videoUrl;
                        Observable.create(new ObservableOnSubscribe<Bitmap>() {
                            @Override
                            public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                                e.onNext(PictureUtil.getNetworkVideoThumbnail(finalVideoUrl));
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Bitmap>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                    }

                                    @Override
                                    public void onNext(Bitmap value) {
                                        h.mThumbnail.setImageBitmap(value);
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

                if ("2".equals(itemBean.getContent_sex())) {
                    h.mAge.setBackgroundResource(R.mipmap.lm_bg_age);
                } else {
                    h.mAge.setBackgroundResource(R.mipmap.lm_bg_age_man);
                }

                if ("1".equals(itemBean.getZan_Y())) {
                    h.mPraise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.lm_icon_praise_on, 0, 0, 0);
                } else {
                    h.mPraise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.lm_icon_praise_off, 0, 0, 0);
                }

                // 播放视频
                final String finalVideoUrl1 = videoUrl;
                h.mControlContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(finalVideoUrl1)) {
                            ToastUtil.show(OtherMomentsActivity.this, "视频地址未找到,播放失败");
                            return;
                        }

                        if (h.mVideoView.isPlaying()) {
                            itemVideoReset();
                        } else {
                            itemVideoReset();
                            h.mProgressBar.setVisibility(View.VISIBLE);
                            h.mPlay.setVisibility(View.INVISIBLE);
                            h.mVideoView.setPath(finalVideoUrl1);
                            h.mVideoView.prepareAsync();
                            mMomentVideoHolder = h;
                        }
                    }
                });
                h.mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        itemVideoReset();
                    }
                });
                h.mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        h.mProgressBar.setVisibility(View.INVISIBLE);
                        h.mThumbnail.setVisibility(View.INVISIBLE);
                        mp.start();
                    }
                });
//                h.mWatch.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        LoginBean loginBean = SPUtils.getSelfInfo(getActivity());
//                        if (null == loginBean) {
//                            ToastUtil.show(getActivity(), "您还未登录，请先登录");
//                            Intent intent = new Intent();
//                            intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
//                            getActivity().startActivity(intent);
//                            return;
//                        }
//                        Map map = new HashMap();
//                        map.put("use_id", loginBean.getData().getM_id());
//                        map.put("use_type", ContRes.getSelfType());
//                        map.put("b_use_id", itemBean.getUse_id());
//                        map.put("b_use_type", itemBean.getContent_sex());
//                        ReqUrl.post(Url.watchMoments, map, new ReqCallback.Callback<String>() {
//                            @Override
//                            public void success(String backPamam) {
//                                try {
//                                    JSONObject jsonObject = new JSONObject(backPamam);
//                                    String code = jsonObject.getString("code");
//                                    if (TextUtils.equals("200", code)) {
//                                        itemBean.setGuanz_Y("1");
//                                        h.mWatch.setImageResource(R.mipmap.lm_bg_watch_on);
//                                    } else if (TextUtils.equals("202", code)) {
//                                        itemBean.setGuanz_Y("0");
//                                        h.mWatch.setImageResource(R.mipmap.lm_bg_watch_off);
//                                    }
//                                    ToastUtil.show(getContext(), jsonObject.getString("tips"));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void failed(String msg) {
//
//                            }
//                        });
//                    }
//                });
                h.mFlower.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        LoginBean loginBean = SPUtils.getSelfInfo(OtherMomentsActivity.this);
//                        if (null == loginBean) {
//                            ToastUtil.show(OtherMomentsActivity.this, "您还未登录，请先登录");
//                            Intent intent = new Intent();
//                            intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
//                            OtherMomentsActivity.this.startActivity(intent);
//                            return;
//                        }
//                        Map map = new HashMap();
//                        map.put("content_id", itemBean.getContent_id());
//                        map.put("use_id", loginBean.getData().getM_id());
//                        map.put("use_type", ContRes.getSelfType());
//                        map.put("b_use_type", itemBean.getContent_sex());
//                        map.put("b_use_id", itemBean.getUse_id());
//                        ReqUrl.post(Url.sendFlower, map, new ReqCallback.Callback<String>() {
//
//                            @Override
//                            public void success(String backPamam) {
//                                try {
//                                    JSONObject jsonObject = new JSONObject(backPamam);
//                                    String code = jsonObject.getString("code");
//                                    if (!TextUtils.equals("200", code)) {
//                                        ToastUtil.show(OtherMomentsActivity.this, jsonObject.getString("tips"));
//                                        return;
//                                    }
//                                    int i = Integer.parseInt(itemBean.getGift_num());
//                                    ++i;
//                                    itemBean.setGift_num(String.valueOf(i));
//                                    h.mFlower.setText("送花(" + itemBean.getGift_num() + ")");
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void failed(String msg) {
//                                ToastUtil.show(OtherMomentsActivity.this, msg);
//                            }
//                        });
                    }
                });
                h.mPraise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginBean loginBean = SPUtils.getSelfInfo(OtherMomentsActivity.this);
                        if (null == loginBean) {
                            ToastUtil.show(OtherMomentsActivity.this, "您还未登录，请先登录");
                            Intent intent = new Intent();
                            intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
                            OtherMomentsActivity.this.startActivity(intent);
                            return;
                        }
                        Map map = new HashMap();
                        map.put("content_id", itemBean.getContent_id());
                        map.put("use_id", loginBean.getData().getM_id());
                        map.put("use_type", ContRes.getSelfType());
                        map.put("b_use_type", itemBean.getContent_sex());
                        map.put("b_use_id", itemBean.getUse_id());
                        ReqUrl.post(Url.clickPraise, map, new ReqCallback.Callback<String>() {
                            @Override
                            public void success(String backPamam) {
                                try {
                                    JSONObject jsonObject = new JSONObject(backPamam);
                                    String code = jsonObject.getString("code");
                                    if (TextUtils.equals("200", code)) {
                                        itemBean.setZan_Y("1");
                                        int i = Integer.parseInt(itemBean.getZan_num());
                                        ++i;
                                        itemBean.setZan_num(String.valueOf(i));
                                    } else if (TextUtils.equals("202", code)) {
                                        itemBean.setZan_Y("0");
                                        int i = Integer.parseInt(itemBean.getZan_num());
                                        if (i > 0) {
                                            --i;
                                            itemBean.setZan_num(String.valueOf(i));
                                        }

                                    }
                                    h.mPraise.setText("赞(" + itemBean.getZan_num() + ")");
                                    if ("1".equals(itemBean.getZan_Y())) {
                                        h.mPraise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.lm_icon_praise_on, 0, 0, 0);
                                    } else {
                                        h.mPraise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.lm_icon_praise_off, 0, 0, 0);
                                    }
                                    ToastUtil.show(OtherMomentsActivity.this, jsonObject.getString("tips"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failed(String msg) {
                                ToastUtil.show(OtherMomentsActivity.this, msg);
                            }
                        });
                    }
                });
                h.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(OtherMomentsActivity.this, MomentDetailsActivity.class)
                                .putExtra("data", itemBean)
                                .putExtra("dataPosition", position), REQUEST_CODE_DETAILS);
                    }
                });


            }
        }

        @Override
        public int getItemCount() {
            return isEmptyData() ? 1 : mItemBeans.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (isEmptyData()) return -1;
            MomentListItemBean itemBean = mItemBeans.get(position);
            if (TextUtils.equals("1", itemBean.getContent_type()) || TextUtils.equals("3", itemBean.getContent_type())) {
                return 1;
            } else if (TextUtils.equals("2", itemBean.getContent_type())) {
                return 2;
            }
            return -2;
        }

        boolean isEmptyData() {
            return mItemBeans.size() < 1;
        }

    }


    private class PictureAdapter extends BaseRvAdapter {
        private RecyclerView recyclerView;
        private List<MomentListItemBean.PhotoListBean> picList;

        public PictureAdapter(RecyclerView recyclerView, List<MomentListItemBean.PhotoListBean> picList) {
            this.recyclerView = recyclerView;
            this.picList = picList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lm_item_picture, parent, false);
            return new PictureHoder(view, recyclerView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            PictureHoder h = (PictureHoder) holder;
            ImageLoad.bind(h.imageView, picList.get(position).getUrl());
            h.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OtherMomentsActivity.this, WatchPictureActivity.class);
                    List<String> list = new ArrayList<String>();
                    for (MomentListItemBean.PhotoListBean bean : picList) {
                        list.add(bean.getUrl());
                    }
                    intent.putStringArrayListExtra("data", (ArrayList<String>) list);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return picList.size();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode) return;
        if (requestCode == REQUEST_CODE_DETAILS) {
            if (data == null) return;
            MomentListItemBean bean = data.getParcelableExtra("data");
            if (bean == null) return;
            int dataPosition = data.getIntExtra("dataPosition", -1);
            if (dataPosition < 0) return;
            if (mItemBeans == null) return;
            if (mMomentsAdapter == null) return;
            mItemBeans.set(dataPosition, bean);
            mMomentsAdapter.notifyDataSetChanged();
        }

    }
}
