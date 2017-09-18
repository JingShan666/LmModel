package bbc.com.moteduan_lib.renzheng;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.app.App;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.activity.PictureChoseActivity2;
import wei.toolkit.widget.CircleImageBorderView;
import bbc.com.moteduan_lib.tools.FileUtils;
import bbc.com.moteduan_lib.tools.MySensorManager;
import bbc.com.moteduan_lib.tools.PermissionHelper;
import bbc.com.moteduan_lib.tools.StringUtils;
import bbc.com.moteduan_lib.tools.ToastUtils;
import bbc.com.moteduan_lib.tools.UpdateMediaStore;

/**
 * 类描述： 视频录制
 */
public class VideoRecordActivity extends BaseActivity implements View.OnClickListener, MySensorManager.OnScreenChangeListener {
    /**
     * 视频时长，单位s
     */
    private int video_length = 10;

    private VideoDataCollect videoCollect;
    private android.view.SurfaceView surfaceView;
    //相册
    private CircleImageBorderView albumImageView;
    // 取消，确认，开始停止， 切换模式 按钮
    private android.widget.ImageButton cancelBtn, saveBtn, takeBtn, modelBtn;
    //相册按钮（圆形）
    private CircleImageBorderView ablumView;
    // 返回，闪光灯，前置摄像头 按钮
    private android.widget.ImageButton returnBtn, flashBtn, frontBtn;
    // 进度
    private android.widget.ProgressBar progressBar;
    // 闪光灯的模式
    private int mFlashMode = 0;
    // 是否是前置摄像头
    private boolean isFrontCamera = false;
    // 是否拍照模式
    private boolean isCameraModel = false;
    // 文件本地路径
    private String mLocalVideoPath;
    private String mLocalImagePath;//拍照保存的目录地址
    // 感应器管理
    private MySensorManager sensorManager;
    private int screen;// 屏幕方向


    Handler mHandler = new android.os.Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            WaitingDialog.cancelDialog();
            switch (msg.what) {
                case VideoDataCollect.MSG_CODE_PHOTO_SUCCESS:
                    UpdateMediaStore.updateAdd(VideoRecordActivity.this, mLocalImagePath);
                    toast.showText("保存照片成功！");
                    break;
                case VideoDataCollect.MSG_CODE_PHOTO_FAILURE:
                    toast.showText("保存照片失败！");
                    break;
                default:
                    break;
            }
        }

        ;
    };
    private PermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mPermissionHelper = new PermissionHelper(this);
        mPermissionHelper.checkPermisson(new PermissionHelper.OnPermissionListener() {
            @Override
            public void onAgreePermission() {
                mLocalVideoPath = FileUtils.getInstance().genLocalVideoPath(VideoRecordActivity.this);
                if (StringUtils.isEmpty(mLocalVideoPath)) {
                    finish();
                    return;
                }
                // 拍摄视频时长
                String length = getIntent().getStringExtra("TimeLength");
                try {
                    if (StringUtils.isNotEmpty(length) && Integer.valueOf(length) > 0) {
                        video_length = Integer.valueOf(length);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                initView();
                sensorManager = new MySensorManager(VideoRecordActivity.this);
                sensorManager.setOnScreenChangeListener(VideoRecordActivity.this);
            }

            @Override
            public void onDeniedPermission() {
                ToastUtils.getInstance(App.getApp().getBaseContext()).showText("拒绝了调用摄像头的权限");
                return;
            }
        }, Manifest.permission.RECORD_AUDIO);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPermissionHelper.onResume(); // 当界面一定通过权限才能继续，就要加上这行
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onScreenChanged(int s) {
        this.screen = s;
    }

    /**
     * 设置按钮的旋转动画
     *
     * @param anim
     */
    private void setAnim(android.view.animation.Animation anim) {
        if (videoCollect.isStart()) {
            return;
        }
        if (anim == null) {
            frontBtn.clearAnimation();
            flashBtn.clearAnimation();
            modelBtn.clearAnimation();
        } else {
            frontBtn.startAnimation(anim);
            flashBtn.startAnimation(anim);
            modelBtn.startAnimation(anim);
        }
    }


    public void initView() {
        setContentView(R.layout.activity_video_record);
        surfaceView = (android.view.SurfaceView) findViewById(R.id.surfaceView);
        videoCollect = new VideoDataCollect(surfaceView);
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        videoCollect.setWidth(width, height);
        videoCollect.setLocalVideoPath(mLocalVideoPath);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        albumImageView = (CircleImageBorderView) findViewById(R.id.album);
        returnBtn = (ImageButton) findViewById(R.id.top_cancel);
        flashBtn = (ImageButton) findViewById(R.id.flash);
        if (!videoCollect.isSupportedFlashMode()) {
            flashBtn.setVisibility(android.view.View.INVISIBLE);
        }
        frontBtn = (ImageButton) findViewById(R.id.front);
        int cameraNo = videoCollect.getNumberOfCameras();
        if (cameraNo <= 1) {
            frontBtn.setVisibility(android.view.View.INVISIBLE);
        }

        cancelBtn = (ImageButton) findViewById(R.id.cancel);
        saveBtn = (ImageButton) findViewById(R.id.save);
        takeBtn = (ImageButton) findViewById(R.id.take);
        modelBtn = (ImageButton) findViewById(R.id.camera_model);

        ablumView = (CircleImageBorderView) findViewById(R.id.album);
        // 设置触摸聚焦
        surfaceView.setOnTouchListener(new android.view.View.OnTouchListener() {
            @Override
            public boolean onTouch(android.view.View v, android.view.MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && !isFrontCamera) {// 前置摄像头取消触摸自动聚焦功能
                    View view = findViewById(R.id.RelativeLayout1);
                    videoCollect.autoFocus(view, event);
                }
                return true;
            }
        });

    }

    public void takevideo(View view) {
        if (videoCollect != null) {
            if (videoCollect.isStart()) {
                stopVideo();
            } else {
                if (isCameraModel) {
                    takePhoto();// 拍照
                } else {
                    startVideo();// 录制视频
                }
            }
        }
    }


    @Override
    public void initData() {

        if (videoCollect.getNumberOfCameras() > 1) {

            isFrontCamera = !isFrontCamera;
            if (isFrontCamera) {
                flashBtn.setVisibility(android.view.View.GONE);
            } else {
                flashBtn.setVisibility(android.view.View.VISIBLE);
            }
            videoCollect.switchCamera(surfaceView, isFrontCamera);
        }
    }

    @Override
    public void onClick(android.view.View v) {
        int i = v.getId();
        if (i == R.id.camera_model) {
            isCameraModel = !isCameraModel;
            setCameraModel(isCameraModel);
            if (videoCollect != null) {
                if (videoCollect.isStart()) {
                    stopVideo();
                } else {
                    if (isCameraModel) {
                        takePhoto();// 拍照
                    } else {
                        startVideo();// 录制视频
                    }
                }
            }

//		case com.bbc.lm.R.id.take:
//			if (videoCollect != null) {
//				if (videoCollect.isStart()) {
//					stopVideo();
//				} else {
//					if (isCameraModel) {
//						takePhoto();// 拍照
//					} else {
//						startVideo();// 录制视频
//					}
//				}}
//
//			break;
        } else if (i == R.id.cancel) {// 取消保存
            albumImageView.setVisibility(View.VISIBLE);
            returnBtn.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            saveBtn.setVisibility(View.GONE);
            modelBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.GONE);

            deleteLocalPath();


        } else if (i == R.id.top_cancel) {// 返回
            setResult(RESULT_CANCELED);
            finish();

        } else if (i == R.id.save) {
            Intent intent = new Intent();
            intent.putExtra("videoPath", mLocalVideoPath);
            setResult(RESULT_OK, intent);
            finish();

        } else if (i == R.id.flash) {
            if (videoCollect != null) {
                if (isCameraModel) {//照相机
                    mFlashMode = (mFlashMode + 1) % 3;
                    switch (mFlashMode) {
                        case VideoDataCollect.FLASH_MODE_AUTO:
                            videoCollect.setFlashCode(VideoDataCollect.FLASH_MODE_AUTO);
                            flashBtn.setImageResource(R.drawable.btn_flash_auto);
                            break;
                        case VideoDataCollect.FLASH_MODE_ON:
                            videoCollect.setFlashCode(VideoDataCollect.FLASH_MODE_ON);
                            flashBtn.setImageResource(R.drawable.btn_flash_on);
                            break;
                        case VideoDataCollect.FLASH_MODE_OFF:
                            videoCollect.setFlashCode(VideoDataCollect.FLASH_MODE_OFF);
                            flashBtn.setImageResource(R.drawable.btn_flash_off);
                            break;
                    }
                } else {//摄像机
                    if (mFlashMode == VideoDataCollect.FLASH_MODE_OFF) {
                        mFlashMode = VideoDataCollect.FLASH_MODE_TORCH;
                        videoCollect.setFlashCode(VideoDataCollect.FLASH_MODE_OFF);
                        flashBtn.setImageResource(R.drawable.btn_flash_off);
                    } else {
                        mFlashMode = VideoDataCollect.FLASH_MODE_OFF;
                        videoCollect.setFlashCode(VideoDataCollect.FLASH_MODE_TORCH);
                        flashBtn.setImageResource(R.drawable.btn_flash_torch);
                    }
                }
            }

        } else if (i == R.id.front) {
            if (videoCollect != null && !videoCollect.isStart()) {
                isFrontCamera = !isFrontCamera;
                if (isFrontCamera) {
                    flashBtn.setVisibility(View.GONE);
                } else {
                    flashBtn.setVisibility(View.VISIBLE);
                }
                videoCollect.switchCamera(surfaceView, isFrontCamera);
            }

        } else if (i == R.id.album) {
            Intent addIntent = new Intent(
                    VideoRecordActivity.this,
                    PictureChoseActivity2.class);
            addIntent.putExtra("videoRecord", true);
            startActivity(addIntent);
            finish();

        } else {
        }
    }

    private void deleteLocalPath(){
        //手动删除
        File video = new File(mLocalVideoPath);
        if (video.exists()) {
            video.delete();
            UpdateMediaStore.updateDelete(this, mLocalVideoPath);
        }
    }
    /**
     * 到下一页面 上传视频
     */
    private void goNext() {
//		android.content.Intent mIntent = new android.content.Intent(this, UploadHairShowActivity.class);
//		HairShowData data = new HairShowData(mLocalVideoPath, mLocalImagePath, videoCollect.getVideoWith(), videoCollect.getVideoHeight(), progress * time / 1000, isCameraModel);
//		mIntent.putExtra("HairShowData", data);
//		startActivityForResult(mIntent, REQUEST_CODE_UPLOAD_HAIRSHOW);
    }


    private static final int REQUEST_CODE_UPLOAD_HAIRSHOW = 2009;// 上传

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == REQUEST_CODE_UPLOAD_HAIRSHOW) {
//			if (resultCode == RESULT_OK) {
//				setResult(RESULT_OK);
//			} else {
//				setResult(RESULT_CANCELED);
//			}
//			finish();
//		}
    }

    /**
     * 倒计时
     */
    private int progress;//进度
    Runnable runnable;
    private int max;// 进度条最大值
    private int time = 50;// 多久更新一次进度ms

    private void initTimer(boolean start) {
        if (start) {
            progress = 0;
            max = video_length * 1000 / time;
            progressBar.setMax(max);
            runnable = new Runnable() {
                public void run() {
                    progress++;
                    progressBar.setProgress(progress);
                    if (progress > max) {
                        stopVideo();
                        return;
                    }
                    mHandler.postDelayed(this, time);
                }
            };
            mHandler.postAtFrontOfQueue(runnable);
        } else {
            mHandler.removeCallbacks(runnable);
        }

    }

    /**
     * 开始录制视频
     */
    private void startVideo() {
//
//		mLocalVideoPath = com.bbc.lm.toools.FileUtils.getInstance().genLocalVideoPath(this);
//		videoCollect.setLocalVideoPath(mLocalVideoPath);

        albumImageView.setVisibility(android.view.View.GONE);
        modelBtn.setVisibility(android.view.View.GONE);
        returnBtn.setVisibility(android.view.View.INVISIBLE);
        cancelBtn.setVisibility(android.view.View.INVISIBLE);
        saveBtn.setVisibility(android.view.View.INVISIBLE);
        takeBtn.setImageResource(R.drawable.btn_stop_red_selector);
        videoCollect.start(screen);
        initTimer(true);
    }

    /**
     * 停止录制视频
     */
    private void stopVideo() {
        if (progress * time < 2000) {
            Toast.makeText(this, "视频不能短于两秒！", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        cancelBtn.setVisibility(android.view.View.VISIBLE);
        saveBtn.setVisibility(android.view.View.VISIBLE);
        takeBtn.setImageResource(R.drawable.btn_shutter_video_selector);
        videoCollect.stop();
        initTimer(false);

    }

    /**
     * 拍照
     */
    private void takePhoto() {
        WaitingDialog.show(this, "", false);
        // 照片名
        mLocalVideoPath = FileUtils.getInstance().genLocalVideoPath(this);
        mLocalImagePath = mLocalVideoPath.substring(0, mLocalVideoPath.lastIndexOf(".")) + ".png";
        videoCollect.takePhoto(mHandler, mLocalImagePath, screen);
    }

    /**
     * 设置相机，拍照模式，摄像模式
     *
     * @param isCamera
     */
    private void setCameraModel(boolean isCamera) {
        if (isCamera) {// 照相机
            toast.showText("照相机");
            takeBtn.setImageResource(R.drawable.btn_camera_selector);
            modelBtn.setImageResource(R.drawable.ic_switch_video);
            videoCollect.setFlashCode(VideoDataCollect.FLASH_MODE_AUTO);
            flashBtn.setImageResource(R.drawable.btn_flash_auto);
            progressBar.setVisibility(android.view.View.GONE);
        } else {// 摄像机
            toast.showText("摄像机");
            takeBtn.setImageResource(R.drawable.btn_shutter_video_selector);
            modelBtn.setImageResource(R.drawable.ic_switch_camera);
            videoCollect.setFlashCode(VideoDataCollect.FLASH_MODE_OFF);
            flashBtn.setImageResource(R.drawable.btn_flash_off);
            mFlashMode = VideoDataCollect.FLASH_MODE_OFF;
            progressBar.setVisibility(android.view.View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        videoCollect.stop();
        initTimer(false);
        deleteLocalPath();
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}