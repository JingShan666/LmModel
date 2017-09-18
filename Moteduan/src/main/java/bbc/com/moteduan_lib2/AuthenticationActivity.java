package bbc.com.moteduan_lib2;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.network.ImageLoad;
import bbc.com.moteduan_lib.network.Req;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.oss.OSSManager;
import bbc.com.moteduan_lib2.bean.AuthenticationBean;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wei.toolkit.PictureListActivity;
import wei.toolkit.VideoListActivity;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.PictureUtil;

/**
 * Created by Administrator on 2017/6/8 0008.
 * 我的认证页面
 */

public class AuthenticationActivity extends BaseActivity {
    private static final String TAG = "AuthenticationActivity";
    private ImageView back;
    private TextView title;

    private ImageView lifeImg;
    private ImageView lifeImgState;
    private TextView lifeImgUp;
    private TextView lifeImgTips;

    private ImageView artImg;
    private ImageView artImgState;
    private TextView artImgUp;
    private TextView artImgTips;

    private ImageView cardImg;
    private ImageView cardImgState;
    private TextView cardImgUp;
    private TextView cardImgTips;


    private VideoView videoView;
    private ImageView videoState;
    private TextView videoUp;
    private TextView videoTips;
    private ImageView firstFrame;
    private ImageView videoPlay;


    private static final int REQUEST_CODE_LIFE = 1;
    private static final int REQUEST_CODE_ART = 2;
    private static final int REQUEST_CODE_MODEL_CARD = 3;
    private static final int REQUEST_CODE_VIDEO = 4;
    private int tempCodeFlag = -1;
    private String localLifeUrl = "";
    private String localArtUrl = "";
    private String localModelCardUrl = "";
    private String localVideoUrl = "";
    private String localFirstFrameUrl = "";
    private OSSManager ossManager;
    private AuthenticationBean authBean;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ossManager = OSSManager.getInstance();
        initView();
        initData();
        initEvents();
    }

    @Override
    public void initView() {

        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);

        lifeImg = (ImageView) findViewById(R.id.activity_authentication_life_img);
        lifeImgState = (ImageView) findViewById(R.id.activity_authentication_life_state);
        lifeImgUp = (TextView) findViewById(R.id.activity_authentication_life_up);
        lifeImgTips = (TextView) findViewById(R.id.activity_authentication_life_tips);

        artImg = (ImageView) findViewById(R.id.activity_authentication_art_img);
        artImgState = (ImageView) findViewById(R.id.activity_authentication_art_state);
        artImgUp = (TextView) findViewById(R.id.activity_authentication_art_up);
        artImgTips = (TextView) findViewById(R.id.activity_authentication_art_tips);

        cardImg = (ImageView) findViewById(R.id.activity_authentication_card_img);
        cardImgState = (ImageView) findViewById(R.id.activity_authentication_card_state);
        cardImgUp = (TextView) findViewById(R.id.activity_authentication_card_up);
        cardImgTips = (TextView) findViewById(R.id.activity_authentication_card_tips);

        videoView = (VideoView) findViewById(R.id.activity_authentication_video);
        videoState = (ImageView) findViewById(R.id.activity_authentication_video_state);
        videoUp = (TextView) findViewById(R.id.activity_authentication_video_up);
        videoTips = (TextView) findViewById(R.id.activity_authentication_video_tips);
        firstFrame = (ImageView) findViewById(R.id.activity_authentication_video_firstframe);
        videoPlay = (ImageView) findViewById(R.id.activity_authentication_video_play);
        progressBar = (ProgressBar) findViewById(R.id.activity_authentication_video_load_prog);
    }

    @Override
    public void initData() {
        title.setText("我的认证");
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", SpDataCache.getSelfInfo(AuthenticationActivity.this).getData().getM_id());
        Req.post(Url.getAuthenticationState, map, new Req.ReqCallback() {
            @Override
            public void success(String result) {
                LogDebug.log(TAG, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String tips = jsonObject.getString("tips");
                    if (!"200".equals(code)) {
                        if ("201".equals(code)) {
                            return;
                        }
                        toast.showText(tips);
                        return;
                    }
                    authBean = new Gson().fromJson(result, AuthenticationBean.class);
                    if (!TextUtils.isEmpty(authBean.getAuthen().getAuth_model_live())) {
                        JSONObject json = new JSONObject(authBean.getAuthen().getAuth_model_live());
                        JSONArray jsonArray = json.getJSONArray("list");
                        if (jsonArray == null || jsonArray.length() < 1) {
                            return;
                        }
                        JSONObject object = jsonArray.getJSONObject(0);
                        ImageLoad.bind(lifeImg, object.getString("url"));
                        int state = authBean.getAuthen().getAuth_model_live_state();
                        switch (state) {
                            case 1:
                                lifeImgState.setImageResource(R.drawable.icon_verifying);
                                break;
                            case 2:
                                lifeImgState.setImageResource(R.drawable.icon_verify_ok);
                                break;
                            case 3:
                                lifeImgState.setImageResource(R.drawable.icon_verify_no);
                                break;
                            default:
                                break;
                        }
                    }

                    if (!TextUtils.isEmpty(authBean.getAuthen().getAuth_model_art())) {
                        JSONObject json = new JSONObject(authBean.getAuthen().getAuth_model_art());
                        JSONArray jsonArray = json.getJSONArray("list");
                        if (jsonArray == null || jsonArray.length() < 1) {
                            return;
                        }
                        JSONObject object = jsonArray.getJSONObject(0);
                        ImageLoad.bind(artImg, object.getString("url"));
                        int state = authBean.getAuthen().getAuth_model_art_state();
                        switch (state) {
                            case 1:
                                artImgState.setImageResource(R.drawable.icon_verifying);
                                break;
                            case 2:
                                artImgState.setImageResource(R.drawable.icon_verify_ok);
                                break;
                            case 3:
                                artImgState.setImageResource(R.drawable.icon_verify_no);
                                break;
                            default:
                                break;
                        }
                    }

                    if (!TextUtils.isEmpty(authBean.getAuthen().getAuth_model_card())) {
                        JSONObject json = new JSONObject(authBean.getAuthen().getAuth_model_card());
                        JSONArray jsonArray = json.getJSONArray("list");
                        if (jsonArray == null || jsonArray.length() < 1) {
                            return;
                        }
                        JSONObject object = jsonArray.getJSONObject(0);
                        ImageLoad.bind(cardImg, object.getString("url"));
                        int state = authBean.getAuthen().getAuth_model_card_state();
                        switch (state) {
                            case 1:
                                cardImgState.setImageResource(R.drawable.icon_verifying);
                                break;
                            case 2:
                                cardImgState.setImageResource(R.drawable.icon_verify_ok);
                                break;
                            case 3:
                                cardImgState.setImageResource(R.drawable.icon_verify_no);
                                break;
                            default:
                                break;
                        }
                    }

                    if (!TextUtils.isEmpty(authBean.getAuthen().getAuth_video())) {
                        JSONObject json = new JSONObject(authBean.getAuthen().getAuth_video());
                        JSONArray jsonArray = json.getJSONArray("list");
                        if (jsonArray == null || jsonArray.length() < 1) {
                            return;
                        }
                        JSONObject object = jsonArray.getJSONObject(0);
                        ImageLoad.bind(firstFrame, object.getString("thumbnail"));
                        videoPlay.setVisibility(View.VISIBLE);
                        int state = authBean.getAuthen().getAuth_video_state();
                        switch (state) {
                            case 1:
                                videoState.setImageResource(R.drawable.icon_verifying);
                                break;
                            case 2:
                                videoState.setImageResource(R.drawable.icon_verify_ok);
                                break;
                            case 3:
                                videoState.setImageResource(R.drawable.icon_verify_no);
                                break;
                            default:
                                break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {
                toast.showText(msg);
                toast.showText("网络连接不可用，请稍后重试");

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
        lifeImgUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AuthenticationActivity.this, PictureListActivity.class).putExtra("count", 1), REQUEST_CODE_LIFE);
            }
        });

        artImgUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AuthenticationActivity.this, PictureListActivity.class).putExtra("count", 1), REQUEST_CODE_ART);
            }
        });

        cardImgUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AuthenticationActivity.this, PictureListActivity.class).putExtra("count", 1), REQUEST_CODE_MODEL_CARD);
            }
        });

        videoUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AuthenticationActivity.this, VideoListActivity.class), REQUEST_CODE_VIDEO);

            }
        });


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                firstFrame.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
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

        videoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != authBean) {
                    if (!TextUtils.isEmpty(authBean.getAuthen().getAuth_video())) {
                        JSONObject json = null;
                        try {
                            json = new JSONObject(authBean.getAuthen().getAuth_video());
                            JSONArray jsonArray = json.getJSONArray("list");
                            if (jsonArray == null || jsonArray.length() < 1) {
                                toast.showText("未找到视频地址");
                                return;
                            }
                            JSONObject object = jsonArray.getJSONObject(0);
                            if (videoView.isPlaying()) {
                                videoView.pause();
                                videoView.stopPlayback();
                            }
                            videoView.setVideoPath(object.getString("video"));

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    videoView.start();
                                }
                            }).start();
                            progressBar.setVisibility(View.VISIBLE);
                            videoPlay.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_LIFE) {
            if (data == null) return;
            List<String> list = data.getStringArrayListExtra("data");
            if (list == null) return;
            if (list.size() < 1) return;
            localLifeUrl = list.get(0);
            ImageLoad.bind(lifeImg, localLifeUrl);
            showLoadDataDialog();
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> ee) throws Exception {
                    try {
                        PutObjectResult putObjectResult = ossManager.synchUpLoad(localLifeUrl, OSSManager.IMAGE_TYPE, null);
                        String path = putObjectResult.getETag();
                        if (TextUtils.isEmpty(path)) {
                            ee.onError(new Throwable("生活照上传失败，请重新上传。"));
                            return;
                        }
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(localLifeUrl, options);
                        int w = options.outWidth;
                        int h = options.outHeight;
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("width", w);
                        jsonObject.put("height", h);
                        jsonObject.put("url", path);
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(jsonObject);
                        tempCodeFlag = REQUEST_CODE_LIFE;
                        reqAuth(SpDataCache.getSelfInfo(AuthenticationActivity.this).getData().getM_id(), "", "", "", new JSONObject().put("list", jsonArray).toString(), "");
                    } catch (ClientException e) {
                        e.printStackTrace();
                        ee.onError(new Throwable("网络不畅通,上传失败！"));
                    } catch (ServiceException e) {
                        e.printStackTrace();
                        ee.onError(new Throwable("网络不畅通,上传失败！"));
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(String value) {
                        }

                        @Override
                        public void onError(Throwable e) {
                            endLoadDataDialog();
                            toast.showText(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                        }
                    });


        } else if (requestCode == REQUEST_CODE_ART) {
            if (data == null) return;
            List<String> list = data.getStringArrayListExtra("data");
            if (list == null) return;
            if (list.size() < 1) return;
            localArtUrl = list.get(0);
            ImageLoad.bind(artImg, localArtUrl);
            showLoadDataDialog();
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> ee) throws Exception {
                    try {
                        PutObjectResult putObjectResult = ossManager.synchUpLoad(localArtUrl, OSSManager.IMAGE_TYPE, null);
                        String path = putObjectResult.getETag();
                        if (TextUtils.isEmpty(path)) {
                            ee.onError(new Throwable("艺术照上传失败，请重新上传。"));
                            return;
                        }
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(localArtUrl, options);
                        int w = options.outWidth;
                        int h = options.outHeight;
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("width", w);
                        jsonObject.put("height", h);
                        jsonObject.put("url", path);
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(jsonObject);
                        tempCodeFlag = REQUEST_CODE_ART;
                        reqAuth(SpDataCache.getSelfInfo(AuthenticationActivity.this).getData().getM_id(), "", "", new JSONObject().put("list", jsonArray).toString(), "", "");
                    } catch (ClientException e) {
                        e.printStackTrace();
                        ee.onError(new Throwable("网络不畅通,上传失败！"));
                    } catch (ServiceException e) {
                        e.printStackTrace();
                        ee.onError(new Throwable("网络不畅通,上传失败！"));
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(String value) {
                        }

                        @Override
                        public void onError(Throwable e) {
                            endLoadDataDialog();
                            toast.showText(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                        }
                    });

        } else if (requestCode == REQUEST_CODE_MODEL_CARD) {
            if (data == null) return;
            List<String> list = data.getStringArrayListExtra("data");
            if (list == null) return;
            if (list.size() < 1) return;
            localModelCardUrl = list.get(0);
            ImageLoad.bind(cardImg, localModelCardUrl);
            showLoadDataDialog();
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> ee) throws Exception {
                    try {
                        PutObjectResult putObjectResult = ossManager.synchUpLoad(localModelCardUrl, OSSManager.IMAGE_TYPE, null);
                        String path = putObjectResult.getETag();
                        if (TextUtils.isEmpty(path)) {
                            ee.onError(new Throwable("模卡照片上传失败，请重新上传。"));
                            return;
                        }
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(localModelCardUrl, options);
                        int w = options.outWidth;
                        int h = options.outHeight;
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("width", w);
                        jsonObject.put("height", h);
                        jsonObject.put("url", path);
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(jsonObject);
                        tempCodeFlag = REQUEST_CODE_MODEL_CARD;
                        reqAuth(SpDataCache.getSelfInfo(AuthenticationActivity.this).getData().getM_id(), "", "", "", "", new JSONObject().put("list", jsonArray).toString());
                    } catch (ClientException e) {
                        e.printStackTrace();
                        ee.onError(new Throwable("网络不畅通,上传失败！"));
                    } catch (ServiceException e) {
                        e.printStackTrace();
                        ee.onError(new Throwable("网络不畅通,上传失败！"));
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String value) {

                        }

                        @Override
                        public void onError(Throwable e) {
                            endLoadDataDialog();
                            toast.showText(e.getMessage());
                            Loger.log(TAG, e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else if (requestCode == REQUEST_CODE_VIDEO) {
            if (data == null) return;
            String path = data.getStringExtra("video");
            if (TextUtils.isEmpty(path)) return;
            localVideoUrl = path;
            if (videoView.isPlaying()) {
                videoView.pause();
                videoView.stopPlayback();
            }
            videoView.setVideoPath(localVideoUrl);
            videoView.start();

            showLoadDataDialog();

            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> ee) throws Exception {
                    localFirstFrameUrl = PictureUtil.savePicture(PictureUtil.getLocalVideoThumbnail(localVideoUrl));

                    try {
                        PutObjectResult firstFrameResult = ossManager.synchUpLoad(localFirstFrameUrl, OSSManager.IMAGE_TYPE, null);
                        String firstFrame = firstFrameResult.getETag();
                        PutObjectResult videoResult = ossManager.synchUpLoad(localVideoUrl, OSSManager.VIDEO_TYPE, null);
                        String videoPath = videoResult.getETag();
                        if (TextUtils.isEmpty(videoPath)) {
                            ee.onError(new Throwable("视频上传失败，请重新上传。"));
                            return;
                        }

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(localFirstFrameUrl, options);
                        int w = options.outWidth;
                        int h = options.outHeight;
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("width", w);
                        jsonObject.put("height", h);
                        jsonObject.put("video", videoPath);
                        jsonObject.put("thumbnail", firstFrame);
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(jsonObject);
                        tempCodeFlag = REQUEST_CODE_VIDEO;
                        reqAuth(SpDataCache.getSelfInfo(AuthenticationActivity.this).getData().getM_id(), new JSONObject().put("list", jsonArray).toString(), "", "", "", "");
                    } catch (ClientException e) {
                        e.printStackTrace();
                        ee.onError(new Throwable("网络不畅通,上传失败！"));
                    } catch (ServiceException e) {
                        e.printStackTrace();
                        ee.onError(new Throwable("网络不畅通,上传失败！"));
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(String value) {
                        }

                        @Override
                        public void onError(Throwable e) {
                            endLoadDataDialog();
                            toast.showText(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void reqAuth(String member_id, final String auth_video, String auth_video_first, final String auth_model_art, final String auth_model_live, final String auth_model_card) {
        LogDebug.log(AuthenticationActivity.class.getSimpleName(), member_id);
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", member_id);
        map.put("auth_video", auth_video);
        map.put("auth_video_first", auth_video_first);
        map.put("auth_model_art", auth_model_art);
        map.put("auth_model_live", auth_model_live);
        map.put("auth_model_card", auth_model_card);
        Req.post(Url.modelAuthentication, map, new Req.ReqCallback() {
            @Override
            public void success(String result) {
                endLoadDataDialog();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    String tips = jsonObject.getString("tips");
                    toast.showText(tips);
                    if (!"200".equals(code)) {
                        return;
                    }
                    if (tempCodeFlag == REQUEST_CODE_LIFE) {
                        lifeImgState.setImageResource(R.drawable.icon_verifying);
                    } else if (tempCodeFlag == REQUEST_CODE_ART) {
                        artImgState.setImageResource(R.drawable.icon_verifying);
                    } else if (tempCodeFlag == REQUEST_CODE_MODEL_CARD) {
                        cardImgState.setImageResource(R.drawable.icon_verifying);
                    } else if (tempCodeFlag == REQUEST_CODE_VIDEO) {
                        videoState.setImageResource(R.drawable.icon_verifying);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {
                endLoadDataDialog();
                toast.showText("网络不畅通,请求失败");
                Loger.log(TAG, msg);
            }

            @Override
            public void completed() {

            }
        });
    }
}
