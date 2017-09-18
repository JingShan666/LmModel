package wei.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liemo.shareresource.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wei.moments.base.BaseActivity;
import wei.moments.base.BaseRvAdapter;
import wei.moments.bean.LoginBean;
import wei.moments.bean.MomentListItemBean;
import wei.moments.database.SPUtils;
import wei.moments.decoration.GridlayoutDecoration;
import wei.moments.holder.PictureHoder;
import wei.moments.network.ReqCallback;
import wei.moments.network.ReqUrl;
import wei.toolkit.WatchPictureActivity;
import wei.toolkit.utils.ImageLoad;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.ToastUtil;
import wei.toolkit.widget.CircleImageBorderView;

/**
 * Created by Administrator on 2017/5/4 0004.
 */
@Deprecated
public class MomentDetailsPictureActivity extends BaseActivity {
    public static final String TAG = "MomentDetailsPictureActivity";
    private TabLayout mTabLayout;
    private ViewPager mBaseViewPager;
    private List<Fragment> mFragments;
    private String[] titles = {"评论", "送花"};
    private MomentListItemBean mMomentListItemBean;

    private ImageView mBack;
    private TextView mTitle;
    private TextView mContent;
    private TextView mPraise;
    private TextView mFlower;
    private CircleImageBorderView mCircleImageView;
    private TextView mName;
    private TextView mAge;
    private ImageView mWatch;
    private RecyclerView mRv;
    private ImageView mPictureSingle;
    private MomentListItemBean.PhotoList photoList;
    /*如果getIntent能获取到值证明是数据源中的位置,关闭activity时 setResult 再传回去。*/
    private int dataPosition = -1;
    private ImageView roleFlag;
    private String contentId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lm_activity_moment_details_picture);

        mMomentListItemBean = getIntent().getParcelableExtra("data");
        contentId = getIntent().getStringExtra("contentId");
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
                    }else{
                        if(!TextUtils.isEmpty(contentId)){
                            ToastUtil.show(MomentDetailsPictureActivity.this,jsonObject.getString("tips"));
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
                if(!TextUtils.isEmpty(contentId)){
                    ToastUtil.show(MomentDetailsPictureActivity.this,getResources().getString(R.string.error_network_block));
                    finish();
                }
                initViews();
            }
        });

    }


    private void initViews() {
        mBack = (ImageView) findViewById(R.id.bar_action_back);
        mTitle = (TextView) findViewById(R.id.bar_action_title);
        mContent = (TextView) findViewById(R.id.activity_moment_details_picture_conetnt);
        mPraise = (TextView) findViewById(R.id.activity_moment_details_picture_praise);
        mFlower = (TextView) findViewById(R.id.activity_moment_details_picture_flower);
        mCircleImageView = (CircleImageBorderView) findViewById(R.id.activity_moment_details_picture_portrait);
        mName = (TextView) findViewById(R.id.activity_moment_details_picture_name);
        mAge = (TextView) findViewById(R.id.activity_moment_details_picture_age);
        mWatch = (ImageView) findViewById(R.id.activity_moment_details_picture_watch);
        mPictureSingle = (ImageView) findViewById(R.id.activity_moment_details_picture_single);
        roleFlag = (ImageView) findViewById(R.id.activity_moment_details_picture_role_flag);
        mRv = (RecyclerView) findViewById(R.id.activity_moment_details_picture_rv);
        GridLayoutManager manager = new GridLayoutManager(MomentDetailsPictureActivity.this, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mRv.addItemDecoration(new GridlayoutDecoration());
        initData();
        initEvents();
    }

    private void initData() {
        if (!TextUtils.isEmpty(mMomentListItemBean.getPhotos())) {
            photoList = new Gson().fromJson(mMomentListItemBean.getPhotos(), MomentListItemBean.PhotoList.class);
            if (photoList.getList() != null) {
                if (photoList.getList().size() == 1) {
                    mPictureSingle.setVisibility(View.VISIBLE);
                    mRv.setVisibility(View.GONE);
                    ImageLoad.bind(mPictureSingle, photoList.getList().get(0).getUrl(), 600, 600);
                } else {
                    mPictureSingle.setVisibility(View.GONE);
                    mRv.setVisibility(View.VISIBLE);
                    mRv.setAdapter(new PictureAdapter(mRv, photoList.getList()));
                }
            }
        }

        mFragments = new ArrayList<>();
        mFragments.add(new CommentFragment(mMomentListItemBean));
        mFragments.add(new SendFlowerFragment(mMomentListItemBean.getContent_id()));
        mTabLayout = (TabLayout) findViewById(R.id.activity_moment_details_picture_tablayout);
        mBaseViewPager = (ViewPager) findViewById(R.id.activity_moment_details_picture_viewpager);
        mTabLayout.setupWithViewPager(mBaseViewPager);
        mBaseViewPager.setAdapter(new VpAdapter(getSupportFragmentManager()));

        ImageLoad.bind(mCircleImageView, mMomentListItemBean.getHead_photo());
        mName.setText(mMomentListItemBean.getNames());
        mTitle.setText(mMomentListItemBean.getNames());
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
            roleFlag.setVisibility(View.VISIBLE);
        } else {
            roleFlag.setVisibility(View.GONE);
            mWatch.setVisibility(View.VISIBLE);
            mAge.setBackgroundResource(R.mipmap.lm_bg_age_man);
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

    @Override
    public void onBackPressed() {
        setDataCallback();
        super.onBackPressed();
    }

    private void initEvents() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        mPictureSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MomentDetailsPictureActivity.this, WatchPictureActivity.class);
                List<String> list = new ArrayList<String>();
                list.add(photoList.getList().get(0).getUrl());
                intent.putStringArrayListExtra("data", (ArrayList<String>) list);
                startActivity(intent);
            }
        });

        mPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginBean loginBean = SPUtils.getSelfInfo(MomentDetailsPictureActivity.this);
                if (null == loginBean) {
                    ToastUtil.show(MomentDetailsPictureActivity.this, "您还未登录，请先登录");
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
                            ToastUtil.show(MomentDetailsPictureActivity.this, jsonObject.getString("tips"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(String msg) {
                        ToastUtil.show(MomentDetailsPictureActivity.this, msg);
                    }
                });
            }
        });


        mFlower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LoginBean loginBean = SPUtils.getSelfInfo(MomentDetailsPictureActivity.this);
//                if (null == loginBean) {
//                    ToastUtil.show(MomentDetailsPictureActivity.this, "您还未登录，请先登录");
//                    Intent intent = new Intent();
//                    intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
//                   startActivity(intent);
//                    return;
//                }
//                Map map = new HashMap();
//                map.put("content_id", mMomentListItemBean.getContent_id());
//                map.put("use_id", loginBean.getData().getM_id());
//                map.put("use_type", ContRes.getSelfType());
//                map.put("b_use_type",mMomentListItemBean.getContent_sex());
//                map.put("b_use_id",mMomentListItemBean.getUse_id());
//                ReqUrl.post(Url.sendFlower, map, new ReqCallback.Callback<String>() {
//                    @Override
//                    public void success(String backPamam) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(backPamam);
//                            String code = jsonObject.getString("code");
//                            if (!TextUtils.equals("200", code)) {
//                                ToastUtil.show(MomentDetailsPictureActivity.this, jsonObject.getString("tips"));
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
//
//                    @Override
//                    public void failed(String msg) {
//                        ToastUtil.show(MomentDetailsPictureActivity.this, msg);
//                    }
//                });
            }
        });
        mWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginBean loginBean = SPUtils.getSelfInfo(MomentDetailsPictureActivity.this);
                if (null == loginBean) {
                    ToastUtil.show(MomentDetailsPictureActivity.this, "您还未登录，请先登录");
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
                            ToastUtil.show(MomentDetailsPictureActivity.this, jsonObject.getString("tips"));
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
            return new PictureHoder(view,recyclerView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            PictureHoder h = (PictureHoder) holder;
            ImageLoad.bind(h.imageView, picList.get(position).getUrl());
            h.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MomentDetailsPictureActivity.this, WatchPictureActivity.class);
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
