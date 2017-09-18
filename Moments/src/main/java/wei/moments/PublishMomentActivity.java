package wei.moments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.liemo.shareresource.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
import wei.moments.base.BaseRecycleView;
import wei.moments.base.BaseRvAdapter;
import wei.moments.bean.LoginBean;
import wei.moments.database.SPUtils;
import wei.moments.decoration.GridlayoutDecoration;
import wei.moments.dialog.DialogListener;
import wei.moments.dialog.DialogUtils;
import wei.moments.holder.PictureHoder;
import wei.moments.holder.VideoHolder;
import wei.moments.network.ReqCallback;
import wei.moments.network.ReqUrl;
import wei.moments.oss.OSSManager;
import wei.toolkit.PictureListActivity;
import wei.toolkit.RecordActivity;
import wei.toolkit.VideoListActivity;
import wei.toolkit.WatchPictureActivity;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.PermissionUtils;
import wei.toolkit.utils.PictureUtil;
import wei.toolkit.utils.ToastUtil;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public class PublishMomentActivity extends BaseActivity {
    private final String TAG = "PublishMomentActivity";
    private BaseRecycleView baseRecycleView;
    private List<String> mPics;
    private String mVideoPath;
    private TextView mSure;
    private ImageView mBack;
    private EditText mContent;
    private RelativeLayout mOpenStateRl;
    private TextView mOpenStateText;
    private TextView mPoiTv;
    private static final int REQUEST_CODE_PICTURE_VIDEO = 0;
    private static final int REQUEST_CODE_PHOTO = 1;
    private static final int REQUEST_CODE_VIDEO = 2;
    private static final int MAX_PICTURE_NUMBER = 9;
    private static final int REQUEST_CODE_WATCH_PICTURE = 10;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private int headerItemCount = 1;
    private OSSManager ossManager;
    private static final int REQUEST_CODE_POI = 100;
    private int mOpenStateInt = 1; // 默认1代表公开, 2 相互关注的人 3 仅用户
    private String mSendAddress = "";
    private String[] pers = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };
    private wei.toolkit.utils.DialogUtils.DataReqDialog dialogDataReq;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lm_activity_publish_moment);
        dialogDataReq = new wei.toolkit.utils.DialogUtils.DataReqDialog(this);
        ossManager = OSSManager.getInstance(this);
        mPics = new ArrayList<>();
        mSure = (TextView) findViewById(R.id.bar_action_sure);
        mSure.setVisibility(View.VISIBLE);
        mSure.setText("发布");
        mBack = (ImageView) findViewById(R.id.bar_action_back);
        mContent = (EditText) findViewById(R.id.activity_publish_moments_content);
        mOpenStateRl = (RelativeLayout) findViewById(R.id.activity_publish_moments_open_state_rl);
        mOpenStateText = (TextView) findViewById(R.id.activity_publish_moments_open_state_text);
        mPoiTv = (TextView) findViewById(R.id.activity_publish_moments_poi);
        baseRecycleView = (BaseRecycleView) findViewById(R.id.activity_publish_moments_rv);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        baseRecycleView.setLayoutManager(manager);
        baseRecycleView.addItemDecoration(new GridlayoutDecoration());
        baseRecycleView.setAdapter(new RvAdapter());

        mBack.setOnClickListener(back);
        mSure.setOnClickListener(sureListener);
        mPoiTv.setOnClickListener(poiListener);
        mOpenStateRl.setOnClickListener(openStateListener);

    }

    private View.OnClickListener back = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    // 所在位置

    private View.OnClickListener poiListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivityForResult(new Intent(PublishMomentActivity.this, PoiActivity.class), REQUEST_CODE_POI);
        }
    };


    // 弹出私密状态dialog

    private View.OnClickListener openStateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogUtils.showOpenStateDialog(PublishMomentActivity.this, mOpenStateInt, new DialogListener.ChooseItemListener() {
                @Override
                public void onSelected(String text, int i) {
                    if (!TextUtils.isEmpty(text)) {
                        mOpenStateText.setText(text);
                        mOpenStateInt = i;
                    }
                }
            });
        }
    };

    // 发布动态
    private View.OnClickListener sureListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final LoginBean loginBean = SPUtils.getSelfInfo(PublishMomentActivity.this);
            if (loginBean == null) {
                ToastUtil.show(PublishMomentActivity.this, "您还未登录,请先登录");
                return;
            }
            final String content = mContent.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                ToastUtil.show(PublishMomentActivity.this, "发布内容不能为空");
                return;
            }
            dialogDataReq.show();
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(final ObservableEmitter<String> e) throws Exception {
                    Map<String, Object> map = new HashMap<>();
                    if (!TextUtils.isEmpty(mVideoPath)) {
                        String thumbnailUrl = PictureUtil.savePicture(PictureUtil.getLocalVideoThumbnail(mVideoPath));
                        PutObjectResult thumbnailResult = ossManager.synchUpLoad(thumbnailUrl, OSSManager.IMAGE_TYPE, new OSSProgressCallback() {
                            @Override
                            public void onProgress(Object o, long l, long l1) {
                                Loger.log(TAG, "缩略图" + l + " " + l1);
                            }
                        });
                        String thumbnailResultUrl = thumbnailResult.getETag();
                        PutObjectResult result = ossManager.synchUpLoad(mVideoPath, OSSManager.VIDEO_TYPE, new OSSProgressCallback() {
                            @Override
                            public void onProgress(Object o, long l, long l1) {
                                Loger.log(TAG, "视频" + l + " " + l1);
                            }
                        });
                        String videoResult = result.getETag();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(thumbnailUrl, options);
                        int w = options.outWidth;
                        int h = options.outHeight;
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("video", videoResult);
                        jsonObject.put("thumbnail", thumbnailResultUrl);
                        jsonObject.put("width", w);
                        jsonObject.put("height", h);
                        map.put("video_path", new JSONObject().put("list", new JSONArray().put(jsonObject)).toString());
                        map.put("content_type", "2");
                        map.put("photos", new JSONObject().put("list", new JSONArray()).toString());
                    } else if (mPics.size() > 0) {
                        JSONArray jsonArray = new JSONArray();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        for (String path : mPics) {
                            path = PictureUtil.pictureCompress(path);
                            BitmapFactory.decodeFile(path, options);
                            int w = options.outWidth;
                            int h = options.outHeight;
                            PutObjectResult result = ossManager.synchUpLoad(path, OSSManager.IMAGE_TYPE, new OSSProgressCallback() {
                                @Override
                                public void onProgress(Object o, long l, long l1) {
                                    Loger.log(TAG, "图片" + l + " " + l1);
                                }
                            });
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("width", w);
                            jsonObject.put("height", h);
                            jsonObject.put("url", result.getETag());
                            jsonArray.put(jsonObject);
                        }
                        map.put("content_type", "1");
                        map.put("photos", new JSONObject().put("list", jsonArray).toString());
                        map.put("video_path", new JSONObject().put("list", new JSONArray()).toString());
                    }

                    if (!map.containsKey("content_type")) {
                        map.put("content_type", "3");
                        map.put("photos", new JSONObject().put("list", new JSONArray()).toString());
                        map.put("video_path", "");
                    }
                    map.put("member_id", loginBean.getData().getM_id());
                    map.put("content", content);
                    map.put("send_addres", mSendAddress);
                    map.put("secret_type", mOpenStateInt + "");
                    ReqUrl.post(Url.pulishMoments, map, new ReqCallback.Callback<String>() {
                        @Override
                        public void success(String result) {
                            e.onNext(result);
                        }

                        @Override
                        public void failed(String msg) {
                            e.onError(new Throwable(msg));
                        }
                    });
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(String value) {
                            dialogDataReq.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(value);
                                String code = jsonObject.getString("code");
                                if (!TextUtils.equals("200", code)) {
                                    ToastUtil.show(PublishMomentActivity.this, jsonObject.getString("tips"));
                                    return;
                                }
                                ToastUtil.show(PublishMomentActivity.this, "发布成功");
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                ToastUtil.show(PublishMomentActivity.this, getResources().getString(R.string.error_network_block));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialogDataReq.dismiss();
                            ToastUtil.show(PublishMomentActivity.this,getResources().getString(R.string.error_network_block));
                        }

                        @Override
                        public void onComplete() {
                        }
                    });

        }
    };

    private class RvAdapter extends BaseRvAdapter {

        public RvAdapter() {
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lm_item_picture, parent, false);
                return new PictureHoder(view, baseRecycleView);
            } else if (viewType == 1) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lm_item_video, parent, false);
                return new VideoHolder(view, baseRecycleView);
            } else if (viewType == 2) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lm_item_picture, parent, false);
                return new AddHolder(view, baseRecycleView);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof AddHolder) {
                AddHolder h = (AddHolder) holder;
                h.imageView.setImageResource(R.mipmap.lm_add);
                h.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                h.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(PublishMomentActivity.this)
                                .setItems(new String[]{
                                        "拍照或录像", "本地相册", "本地视频"
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                PermissionUtils.checkPermission(PublishMomentActivity.this, pers, new PermissionUtils.PermissionCallback() {
                                                    @Override
                                                    public void onGranted() {
                                                        if (mPics.size() > 0) {
                                                            startActivityForResult(new Intent(PublishMomentActivity.this, RecordActivity.class).putExtra("action_flag", RecordActivity.ACTION_FLAG_PICTURE)
                                                                    , REQUEST_CODE_PICTURE_VIDEO);
                                                        } else {
                                                            startActivityForResult(new Intent(PublishMomentActivity.this, RecordActivity.class), REQUEST_CODE_PICTURE_VIDEO);
                                                        }
                                                    }

                                                    @Override
                                                    public void onDenied(String[] deniedName) {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                            requestPermissions(deniedName, REQUEST_CODE_PERMISSION);
                                                        } else {
                                                            Toast.makeText(PublishMomentActivity.this,
                                                                    "您拒绝了" + PermissionUtils.getPermissionName(deniedName[0]) + "权限,导致功能无法正常使用", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                                break;
                                            case 1:
                                                startActivityForResult(new Intent(PublishMomentActivity.this, PictureListActivity.class).putExtra("count", MAX_PICTURE_NUMBER - mPics.size()), REQUEST_CODE_PHOTO);
                                                break;
                                            case 2:
                                                if (mPics.size() > 0) {
                                                    ToastUtil.show(PublishMomentActivity.this, "照片和视频不能一起上传哦");
                                                    return;
                                                }
                                                startActivityForResult(new Intent(PublishMomentActivity.this, VideoListActivity.class), REQUEST_CODE_VIDEO);
                                                break;
                                        }
                                    }
                                }).show();
                    }
                });
            } else if (holder instanceof PictureHoder) {
                PictureHoder h = (PictureHoder) holder;
                h.imageView.setImageBitmap(PictureUtil.getThumbnail(mPics.get(position - headerItemCount)));
                h.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PublishMomentActivity.this, WatchPictureActivity.class);
                        intent.putStringArrayListExtra("data", (ArrayList<String>) mPics);
                        intent.putExtra("position", position - headerItemCount);
                        intent.putExtra("delete", true);
                        startActivityForResult(intent, REQUEST_CODE_WATCH_PICTURE);

                    }
                });
            } else if (holder instanceof VideoHolder) {
                VideoHolder h = (VideoHolder) holder;
                h.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setVolume(0f, 0f);
                        mp.start();
                    }
                });
                h.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.start();
                    }
                });

                h.videoView.setVideoPath(mVideoPath);
                h.videoView.start();

            }


        }

        @Override
        public int getItemCount() {

            return mPics.size() + headerItemCount;
        }

        @Override
        public int getItemViewType(int position) {

            if (!TextUtils.isEmpty(mVideoPath)) {
                return 1;
            }
            if (position == 0 && headerItemCount == 1) {
                return 2;
            }
            return 0;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(PublishMomentActivity.this,
                            "您拒绝了" + PermissionUtils.getPermissionName(permissions[i]) + "权限,导致功能无法正常使用", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if (mPics.size() > 0) {
                startActivityForResult(new Intent(PublishMomentActivity.this, RecordActivity.class).putExtra("action_flag", RecordActivity.ACTION_FLAG_PICTURE)
                        , REQUEST_CODE_PICTURE_VIDEO);
                return;
            }
            startActivityForResult(new Intent(PublishMomentActivity.this, RecordActivity.class), REQUEST_CODE_PICTURE_VIDEO);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_PICTURE_VIDEO) {
            if (data == null) {
                return;
            }
            String path = data.getStringExtra("picture");
            if (TextUtils.isEmpty(path)) {
                path = data.getStringExtra("video");
                if (TextUtils.isEmpty(path)) {
                    return;
                }
                this.mVideoPath = path;
            } else {
                mPics.add(path);
            }
            Uri localUri = Uri.fromFile(new File(path));
            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
            sendBroadcast(localIntent);
        } else if (requestCode == REQUEST_CODE_PHOTO) {
            if (data == null) {
                return;
            }

            List<String> picPaths = data.getStringArrayListExtra("data");
            if (picPaths == null || picPaths.size() < 1) {
                return;
            }
            mPics.addAll(picPaths);

        } else if (requestCode == REQUEST_CODE_VIDEO) {
            if (data == null) {
                return;
            }
            String videoPath = data.getStringExtra("video");
            if (TextUtils.isEmpty(videoPath)) {
                return;
            }
            this.mVideoPath = videoPath;
        } else if (requestCode == REQUEST_CODE_POI) {

            if (data != null) {
                String sendAddress = data.getStringExtra("data");
                if (TextUtils.isEmpty(sendAddress)) {
                    mPoiTv.setText("地点定位");
                    mSendAddress = "";
                } else {
                    mPoiTv.setText(sendAddress);
                    mSendAddress = sendAddress;
                }
            }

            return;
        } else if (requestCode == REQUEST_CODE_WATCH_PICTURE) {
            if (data == null) {
                return;
            }

            List<String> picPaths = data.getStringArrayListExtra("data");
            if (picPaths == null) {
                return;
            }
            mPics.clear();
            mPics.addAll(picPaths);
        }

        headerItemCount = mPics.size() == MAX_PICTURE_NUMBER ? 0 : 1;
        baseRecycleView.getAdapter().notifyDataSetChanged();

    }

    private class AddHolder extends PictureHoder {

        public AddHolder(View itemView) {
            super(itemView);
        }

        public AddHolder(View view, RecyclerView recyclerView) {
            super(view, recyclerView);
        }


    }


}
