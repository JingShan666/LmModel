package bbc.com.moteduan_lib.renzheng;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.bean.VideoInfo;
import bbc.com.moteduan_lib.constant.Constants;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.home.DetailActivity;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.mywidget.DialogProgress;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.oss.OSSManager;
import bbc.com.moteduan_lib.tools.LoadVideoUtils;
import bbc.com.moteduan_lib.tools.ToastUtils;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;

public class ShipinRenzhengActivity extends BaseActivity implements View.OnClickListener, OSSProgressCallback<PutObjectRequest> {

    private ImageButton back;
    private TextView list;
    private TextView shipin_text;
    private ImageView bg_shipin;
    private ImageView paisheshipin;
    public static final int REQUEST_CODE = 520;
    private List<VideoInfo> videoList;
    private TextView renzheng_send;
    private OSSManager oss;
    private DialogProgress dialogProgress;

    private static final int progress_flag = 1111;
    private static final int error_network_flag = 2222;
    private static final int success_flag = 3333;
    private static final int auth_success_flag = 4444;
    private static final int auth_error_flag = 5555;
    private static final int auth_cancle_flag = 6666;
    public static final int VEDIO_FLAG = 5;
    private TextView upload;
    private VideoInfo videoInfo;
    private HashMap<String, Object> map;
    private View rootView;
    private VideoView video;


    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    videoInfo = videoList.get(0);
                    playVideo(videoInfo);
                   /* Bitmap bmp = videoInfo.getBmp();
                    bg_shipin.setImageBitmap(bmp);
                    renzheng_send.setVisibility(View.VISIBLE);
                    shipin_text.setText("点击视频可进行预览哦~");
                    paisheshipin.setImageResource(R.drawable.chongxinpaishe);
                    android.content.Intent it = new android.content.Intent(ShipinRenzhengActivity.this, VideoPlay.class);
                    it.putExtra("video", videoInfo);
                    startActivity(it);*/
                    break;
                case progress_flag:
                    int progress = (int) msg.obj;
//                    dialogProgress.setProgress(progress);
//                    dialogProgress.setProTxt(progress + "%");
                    break;

                case error_network_flag:
                    dialogProgress.dismiss();
                    toast.showText("网络不畅通，上传失败");
                    break;
                case success_flag:
//                    toast.showText("上传成功，正在提交验证信息...");
                    break;

                case auth_success_flag:
                    toast.showText("上传成功！");
                    dialogProgress.dismiss();

                    break;
                case auth_error_flag:
                    dialogProgress.dismiss();
                    toast.showText("上传失败，请重新上传");
                    break;
                case auth_cancle_flag:
                    dialogProgress.dismiss();
                    toast.showText("上传已经取消");
                    break;
            }


        }
    };

    private void playVideo(VideoInfo videoInfo) {
        MediaController controller = new MediaController(this);
        LogDebug.err("playVideo:" + videoInfo.getData());
        video.setVideoPath(videoInfo.getData());
        video.setMediaController(controller);
        video.setOnPreparedListener(new android.media.MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(android.media.MediaPlayer mp) {
                mp.start();
            }
        });
    }


    /*@Override
    protected void onPause() {
        super.onPause();

        if(video.isPlaying()){
            video.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        video.start();
    }*/


    //上传到oss
    private void upLoadVedio(String path) {
        dialogProgress.show();
        new Thread(new UpLoadThread(path)).start();


    }

    class UpLoadThread implements Runnable {

        private String vedioPath;
        private String url;

        public UpLoadThread(String vedioPath) {
            this.vedioPath = vedioPath;
        }

        @Override
        public void run() {

            StringBuffer sbf = new StringBuffer();
            try {

                PutObjectResult putResult = oss.synchUpLoad(vedioPath, Constants.IMAGE_TYPE, new OSSProgressCallback() {
                    @Override
                    public void onProgress(Object o, long currentSize, long totalSize) {
                        Message msg = new Message();
                        msg.what = progress_flag;
                        int progress = Math.round((float) currentSize / totalSize * 100L);
                        msg.obj = progress;
                        myHandler.sendMessage(msg);
                    }
                });

                url = putResult.getETag();


            } catch (ClientException | ServiceException e) {
                e.printStackTrace();

                android.os.Message msg = new Message();
                msg.what = error_network_flag;
                myHandler.sendMessage(msg);
                return;

            }

            Message msg = new Message();
            msg.what = success_flag;
            myHandler.sendMessage(msg);
            map.put("video_path", url);
            shangchuan();
        }
    }


    //上传自己服务器
    private void shangchuan() {
        map.put("userid", SpDataCache.getSelfInfo(ShipinRenzhengActivity.this).getData().getM_head_photo());
//        map.put("realName", "");
//        map.put("personCardnum", "");


        Xutils.post(Url.video_rz, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                android.os.Message msg = new Message();
                msg.what = auth_success_flag;
                myHandler.sendMessage(msg);

                Intent intent = new Intent(ShipinRenzhengActivity.this, DetailActivity.class);
                intent.putExtra("shenhezhuangtai", VEDIO_FLAG);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Message msg = new Message();
                msg.what = auth_error_flag;
                myHandler.sendMessage(msg);
            }

            @Override
            public void onCancelled(org.xutils.common.Callback.CancelledException cex) {

                android.os.Message msg = new Message();
                msg.what = auth_cancle_flag;
                myHandler.sendMessage(msg);
            }

            @Override
            public void onFinished() {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipin_renzheng);
        rootView = View.inflate(ShipinRenzhengActivity.this, R.layout.activity_shipin_renzheng, null);
        initView();
        VideoInfo info = getIntent().getParcelableExtra("videoInfo");
        if (info == null) {
            Intent itent = new Intent(ShipinRenzhengActivity.this, VideoRecordActivity.class);
            startActivityForResult(itent, REQUEST_CODE);
        } else {
            this.videoInfo = info;
            paisheshipin.setVisibility(View.GONE);
            playVideo(videoInfo);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void initView() {
        map = new HashMap<>();
        oss = OSSManager.getInstance();
        dialogProgress = new DialogProgress(rootView, ShipinRenzhengActivity.this);
        dialogProgress.getJiazai_text().setText("正在上传...");
        videoList = new ArrayList<>();
        back = (ImageButton) findViewById(R.id.back);
        list = (TextView) findViewById(R.id.list);
        shipin_text = (TextView) findViewById(R.id.shipin_text);
        bg_shipin = (ImageView) findViewById(R.id.bg_shipin);
        paisheshipin = (ImageView) findViewById(R.id.paisheshipin);
        video = (VideoView) findViewById(R.id.video);
//        paisheshipin.setImageResource(R.drawable.paisheshipin);


        back.setOnClickListener(this);
        bg_shipin.setOnClickListener(this);
        paisheshipin.setOnClickListener(this);

        renzheng_send = (TextView) findViewById(R.id.renzheng_send);
        renzheng_send.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            finish();

        } else if (i == R.id.paisheshipin) {
            Intent itent = new Intent(ShipinRenzhengActivity.this, VideoRecordActivity.class);
            startActivityForResult(itent, REQUEST_CODE);

        } else if (i == R.id.bg_shipin) {
            if (videoInfo != null) {
                Intent it = new Intent(ShipinRenzhengActivity.this, VideoPlay.class);
                it.putExtra("video", videoInfo);
                startActivity(it);
                return;
            }


            if (videoInfo == null) {
                ToastUtils.getInstance(ShipinRenzhengActivity.this).showText("未发现视频");
                return;
            }
            upLoadVedio(videoInfo.getData());

        } else if (i == R.id.renzheng_send) {
            if (videoInfo == null) {
                ToastUtils.getInstance(ShipinRenzhengActivity.this).showText("未发现视频");
                return;
            }
            upLoadVedio(videoInfo.getData());

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            if (videoInfo == null) {
                finish();
            }
        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String path = data.getStringExtra("videoPath");
                if (!TextUtils.isEmpty(path)) {
                    VideoInfo info = new VideoInfo();
                    info.setData(path);
                    videoList.clear();
                    videoList.add(info);
                    myHandler.sendEmptyMessage(0);
                }
            }
        }

    }

    @Override
    public void onProgress(PutObjectRequest putObjectRequest, long l, long l1) {

    }


    class MyAsychTask extends AsyncTask<Integer, Integer, List<VideoInfo>> {

        @Override
        protected List<VideoInfo> doInBackground(Integer... params) {
            return LoadVideoUtils.getVideo(ShipinRenzhengActivity.this);
        }

        @Override
        protected void onPostExecute(List<VideoInfo> videoInfos) {
            super.onPostExecute(videoInfos);

            videoList.clear();
            videoList.addAll(videoInfos);
            myHandler.sendEmptyMessage(0);
        }
    }


}
