package bbc.com.moteduan_lib.localvideo;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.bean.VideoBean;
import bbc.com.moteduan_lib.bean.VideoInfo;
import bbc.com.moteduan_lib.renzheng.ShipinRenzhengActivity;
import bbc.com.moteduan_lib.tools.ScreenUtil;
import bbc.com.moteduan_lib.tools.VideoUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class LocalVideoActivity extends BaseActivity {

    private ImageView mBack;
    private TextView mTitle;
    private RecyclerView mRv;
    private List<VideoBean> mVideos;
    private LruCache<String, VideoBean> mLruCaches;
    private RvAdapter mAdapter;
    private int[] mScreenWhs;
    private DecimalFormat mDecimalFormat = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_video);
        mVideos = new ArrayList<>();
        mLruCaches = new LruCache<String, VideoBean>((int) (Runtime.getRuntime().maxMemory() / 1024 / 8)) {
            @Override
            protected int sizeOf(String key, VideoBean value) {

                return value.getThumbnail().getRowBytes() * value.getThumbnail().getHeight();
            }
        };
        initView();
        initData();
        initEvent();
        loadVideo();
    }

    @Override
    public void initView() {

        mBack = getView(R.id.back);
        mTitle = getView(R.id.title);
        mRv = getView(R.id.activity_local_video_rv);
        GridLayoutManager manager = new GridLayoutManager(LocalVideoActivity.this, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
    }

    @Override
    public void initData() {
        mTitle.setText("本地视频");
        mScreenWhs = ScreenUtil.getScreenSize(LocalVideoActivity.this);
    }

    private void initEvent() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void loadVideo() {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = getContentResolver();
//        Cursor c = cr.query(uri, null, MediaStore.Video.Media.MIME_TYPE + " in(?)",new String[]{"*"},
//                MediaStore.Video.Media.DATE_TAKEN + " desc");
        Cursor c = cr.query(uri, new String[]{
                        android.provider.MediaStore.Video.Media.DISPLAY_NAME,
                        android.provider.MediaStore.Video.Media.DATA,
                        android.provider.MediaStore.Video.Media.SIZE,
                        android.provider.MediaStore.Video.Media.DURATION,
                        android.provider.MediaStore.Video.Media._ID},
                null,
                null,
                MediaStore.Video.Media.DATE_TAKEN + " desc");

        while (c.moveToNext()) {
            long  size = c.getLong(2);
            long  duration = c.getLong(3);
            if(size < 1 || duration < 1){
                continue;
            }
            String fileName = c.getString(0);
            String filePath = c.getString(1);
            VideoBean bean = new VideoBean();
            bean.setFileName(fileName);
            bean.setFilePath(filePath);
            int len = fileName.length();
            int inx = fileName.lastIndexOf(".");
            bean.setSuffix(fileName.substring(inx, len));
            bean.setSize(size);
            bean.setDuration(duration);
            mVideos.add(bean);
        }
        c.close();
        mRv.setAdapter(mAdapter = new RvAdapter());

    }


    private class RvAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_local_video_thumbnail, null);
            RvHolder holder = new RvHolder(view);
            holder.setIsRecyclable(false);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final VideoBean bean = mVideos.get(position);
            final RvHolder h = (RvHolder) holder;
            h.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoInfo info = new VideoInfo();
                    info.setData(bean.getFilePath());
                    info.setDuration(bean.getDuration());
                    info.setSize(bean.getSize());
                    info.setVideoName(bean.getFileName());
                    Intent intent = new Intent(LocalVideoActivity.this, ShipinRenzhengActivity.class);
                    intent.putExtra("videoInfo", info);
                    startActivity(intent);
                }
            });
            Observable.create(new ObservableOnSubscribe<VideoBean>() {
                @Override
                public void subscribe(ObservableEmitter<VideoBean> e) throws Exception {
                    VideoBean videoBean = mLruCaches.get(bean.getFilePath());
                    if (videoBean == null) {
//                bitmap = BitmapCompressUtils.compressBitmap(bean.getFilePath());
                        videoBean = new VideoBean();
                        videoBean.setFilePath(bean.getFilePath());
                        videoBean.setDuration(bean.getDuration());
                        videoBean.setFileName(bean.getFileName());
                        videoBean.setSize(bean.getSize());
                        videoBean.setSuffix(bean.getSuffix());
                        videoBean.setThumbnail(VideoUtils.getThumbnail(bean.getFilePath()));
//                        videoBean.setThumbnail(BitmapCompressUtils.compressBitmap(VideoUtils.getThumbnail(bean.getFilePath())));
                        mLruCaches.put(bean.getFilePath(), videoBean);
                    }
                    e.onNext(videoBean);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<VideoBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(VideoBean value) {
                            h.mImageView.setImageBitmap(value.getThumbnail());
                            h.mSize.setVisibility(View.VISIBLE);
                            h.mSize.setText(mDecimalFormat.format(value.getSize() / 1024f / 1024f) + "M");
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

        @Override
        public int getItemCount() {
            return mVideos.size();
        }
    }

    private class RvHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mSize;
        private RelativeLayout mRl;

        public RvHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.item_local_video_thumbnail_image);
            mSize = (TextView) itemView.findViewById(R.id.item_local_video_thumbnail_text_size);
            mRl = (RelativeLayout) itemView.findViewById(R.id.item_local_video_thumbnail_rl);
            int min = Math.min(mScreenWhs[0], mScreenWhs[1]);
            int w = (int) (min * 0.33), h = (int) (min * 0.33);
            mRl.setLayoutParams(new LinearLayout.LayoutParams(w, h));
        }
    }


}
