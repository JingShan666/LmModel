package wei.toolkit;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import io.reactivex.Observable;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wei.toolkit.bean.VideoBean;
import wei.toolkit.decoration.GridlayoutDecoration;
import wei.toolkit.utils.CPUUtils;
import wei.toolkit.utils.PictureUtil;
import wei.toolkit.utils.ToastUtil;


/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class VideoListActivity extends AppCompatActivity {

    private ImageView mBack;
    private TextView mTitle;
    private RecyclerView mRv;
    private List<VideoBean> mVideos;
    private RvAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_videos);
        mVideos = new ArrayList<>();

        initView();
        initData();
        initEvent();
        loadVideo();
    }

    public void initView() {

        mBack = (ImageView) findViewById(R.id.activity_local_videos_back);
        mTitle = (TextView) findViewById(R.id.activity_local_videos_label);
        mRv = (RecyclerView) findViewById(R.id.activity_local_videos_rv);
        GridLayoutManager manager = new GridLayoutManager(VideoListActivity.this, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mRv.addItemDecoration(new GridlayoutDecoration());
    }

    public void initData() {
        mTitle.setText("本地视频");
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
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media._ID},
                null,
                null,
                MediaStore.Video.Media.DATE_TAKEN + " desc");

        while (c.moveToNext()) {
            long size = c.getLong(2);
            long duration = c.getLong(3);
            if (size < 1 || duration < 1) {
                continue;
            }
            String fileName = c.getString(0);
            if(TextUtils.isEmpty(fileName)){
                continue;
            }
            String filePath = c.getString(1);
            if(TextUtils.isEmpty(filePath)){
                continue;
            }
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
                    if (bean.getDuration() / 1000 > 10) {
                        ToastUtil.show(VideoListActivity.this, "只支持上传10秒以内视频");
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("video", bean.getFilePath());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            Glide.with(VideoListActivity.this)
                    .load(Uri.fromFile(new File(bean.getFilePath())))
                    .into(h.mImageView);

            h.mSize.setVisibility(View.VISIBLE);
            int m = (int) (bean.getDuration() / 1000);
            int ss = m % 60;
            int mm = m / 60;
            h.mSize.setText(String.format("%d:%02d", mm, ss));
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
            int scale = mRv.getWidth();
            mRl.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, scale / 3));
        }
    }


}
