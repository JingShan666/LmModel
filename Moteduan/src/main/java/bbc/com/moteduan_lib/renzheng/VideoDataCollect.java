package bbc.com.moteduan_lib.renzheng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.ToneGenerator;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.tools.MySensorManager;
import bbc.com.moteduan_lib.tools.UpdateMediaStore;

/**
 * 视频数据采集
 */
@android.annotation.SuppressLint("NewApi")
public class VideoDataCollect implements SurfaceHolder.Callback {

    // 在2.3的Camera.CameraInfo类中
    // CAMERA_FACING_BACK常量的值为0，CAMERA_FACING_FRONT为1
    private int Camera_Id = 0;
    private static final int CAMERA_FACING_BACK = 0;
    private static final int CAMERA_FACING_FRONT = 1;
    // 闪光灯
    public static final int FLASH_MODE_AUTO = 0;//自动
    public static final int FLASH_MODE_ON = 1;//拍照时打开
    public static final int FLASH_MODE_OFF = 2;//关闭
    public static final int FLASH_MODE_TORCH = 3;//常亮
    // 拍照成功与失败
    public static final int MSG_CODE_PHOTO_SUCCESS = 131;
    public static final int MSG_CODE_PHOTO_FAILURE = 132;

    private Context mContext;
    private SurfaceView surfaceView;
    private SurfaceHolder mHolder;
    private MediaRecorder mediaRecorder;
    private Camera mCamera;
    private boolean record = false;
    private boolean isShowFrame;// 对焦

    private String localVideoPath = "";//BBC/video/yyyyMMdd_HHmmss.mp4
    /**
     * 拍摄视频宽 640
     */
    private int width = 960;
    /**
     * 拍摄视频高 480
     */
    private int height = 720;
    /**
     * 拍摄后的视频宽
     */
    private int videoWidth = 960;
    /**
     * 拍摄后的视频高
     */
    private int videoHeight = 720;
    /**
     * 屏幕宽
     */
    private int screenWidth = 960;
    /**
     * 屏幕高
     */
    private int screenHeight = 720;

    private int EncodingBitRate = 3 * 1024 * 1024;

    public String print() {
        return "VideoDataCollect{" +
                "width=" + width +
                ", height=" + height +
                ", videoWidth=" + videoWidth +
                ", videoHeight=" + videoHeight +
                ", screenWidth=" + screenWidth +
                ", screenHeight=" + screenHeight +
                '}';
    }

    public VideoDataCollect(SurfaceView surfaceView) {
        mContext = surfaceView.getContext();
        this.surfaceView = surfaceView;
        this.surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        init();
    }

    // SurfaceView 初始化
    private void init() {
        surfaceView.setBackgroundColor(Color.TRANSPARENT);
        surfaceView.getHolder().addCallback(this);
        surfaceView.setFocusable(true);
        surfaceView.setFocusableInTouchMode(true);
        surfaceView.setClickable(true);
        //		surfaceView.setOnClickListener(new View.OnClickListener() {
        //			@Override
        //			public void onClick(View v) {
        //				if (mCamera != null)
        //					mCamera.autoFocus(null);//对焦
        //			}
        //		});
    }

    /**
     * 获取视频宽
     */
    public int getVideoWith() {
        return videoWidth;
    }

    /**
     * 获取视频高
     */
    public int getVideoHeight() {
        return videoHeight;
    }

    public void setWidth(int width, int height) {
        float proportion = (float) width / height;
        if (proportion <= (float) (3 / 4)) {
            this.screenHeight = width;
            this.screenWidth = 4 * width / 3;
        } else {
            this.screenWidth = height;
            this.screenHeight = 3 * height / 4;
        }

    }

    public String getLocalVideoPath() {
        return localVideoPath;
    }

    public void setLocalVideoPath(String localVideoPath) {
        this.localVideoPath = localVideoPath;
    }

    public boolean isStart() {
        return record;
    }

    /**
     * 停止录制
     */
    public void stop() {
        setFlashCode(FLASH_MODE_OFF);
        if (record) {
            freeRecordResource();
            UpdateMediaStore.updateAdd(mContext, localVideoPath);
            if (mCamera != null) {
                //				mCamera.stopPreview();
            }
            record = false;
        }
    }


    /**
     * 释放 Camera 实例
     */
    private void freeCameraResource() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 释放 MediaRecorder 实例
     */
    private void freeRecordResource() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     * 开始录制
     *
     * @param screen 屏幕方向
     */
    public void start(int screen) {
        if (record) {
            return;
        }
        mediaRecorder = new MediaRecorder();
        mediaRecorder.reset();
        // 设置录制视频源为Camera(相机)
        if (mCamera != null) {
            mCamera.unlock();
            mCamera.stopPreview();
        }
        try {
            mediaRecorder.setCamera(mCamera);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 设置在录制过程中产生的输出文件格式
            // 设置录制的音频编码
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);// 设置音频编码器可用于录制.如果是录音一定要设置,不然没有音频
//            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // 设置录制的视频编码h263 h264
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264); // 设置视频编码器可用于录制.如果这个方法不叫,输出文件将不包含视频

            // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
            mediaRecorder.setVideoSize(640, 480);
            // 设置视频编码比特率
            mediaRecorder.setVideoEncodingBitRate(EncodingBitRate);

            //保存地址
            File videoFile = new File(localVideoPath);
            localVideoPath = videoFile.getAbsolutePath();
            mediaRecorder.setOutputFile(localVideoPath);

            Log.e("aaa", "start: " + "录制视频宽高 = " + width + "    " + height);

            // CamcorderProfile profile =
            // CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
            // Androd上，实际上测出来的效果：H264编码的时候，采用的就是可变帧率，设置帧率没用。
            // mediaRecorder.setVideoFrameRate(20);

            //设置输出视频旋转角度
            if (screen == MySensorManager.SCREEN_LEFT) {// 左横屏
                videoHeight = height;
                videoWidth = width;
                mediaRecorder.setOrientationHint(0);
            } else if (screen == MySensorManager.SCREEN_RIGHT) {// 右横屏
                videoHeight = height;
                videoWidth = width;
                mediaRecorder.setOrientationHint(180);
            } else {// 正反竖屏
                videoHeight = width;
                videoWidth = height;
                if (Camera_Id == CAMERA_FACING_FRONT) {
                    mediaRecorder.setOrientationHint(270);
                } else {
                    mediaRecorder.setOrientationHint(90);
                }
            }
            // 设置预览大小
            surfaceView.setLayoutParams(new LinearLayout.LayoutParams(screenHeight, screenWidth));
            mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                record = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            freeRecordResource();
            freeCameraResource();
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        mHolder = holder;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;
        openCamera(false);
        if (mCamera != null) {
            mCamera.startPreview();// 开启预览
//            mCamera.autoFocus(null);
        } else {
            Log.e("aaa", "surfaceCreated: " + "摄像头未获取");
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        holder.removeCallback(this);
        // 在这里需要释放Camera资源
        freeCameraResource();
    }

    /**
     * 判断是否支持闪光灯
     */
    public boolean isSupportedFlashMode() {
        try {
            if (mCamera == null) {
                mCamera = Camera.open();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (mCamera == null) {
                mContext.startActivity(new Intent(mContext, DialogActivity.class));
                ((Activity) mContext).finish();
                return false;
            }
        }
        android.hardware.Camera.Parameters parameters = mCamera.getParameters();
        java.util.List<String> modes = parameters.getSupportedFlashModes();

        if (modes != null && modes.size() != 0) {
            boolean torchSupported = modes.contains(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
            boolean onSupported = modes.contains(android.hardware.Camera.Parameters.FLASH_MODE_ON);
            boolean offSupported = modes.contains(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
            boolean autoSupported = modes.contains(android.hardware.Camera.Parameters.FLASH_MODE_AUTO);
            return torchSupported && onSupported && offSupported && autoSupported;
        }
        return false;
    }

    /**
     * 连续对焦
     */
    public void setFocus_mode_continuous() {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            //前置摄像头设置该属性报错
            List<String> supportFocus = parameters.getSupportedFocusModes();
            if (supportFocus.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)){
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
            }
            if (supportFocus.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)){
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);//1连续对焦
            }


                mCamera.setParameters(parameters);
        }


    }

    /**
     * 返回摄像头个数
     */
    public int getNumberOfCameras() {
        try {
            return Camera.getNumberOfCameras();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 打开摄像头
     */
    public Camera open(int i) {
        try {
            Method method = Camera.class.getMethod("open", int.class);
            if (method != null) {
                Object object = method.invoke(mCamera, i);
                if (object != null) {
                    return (Camera) object;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 多镜头切换
     */
    public void switchCamera(SurfaceView surfaceView, boolean isFrontCamera) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        Camera_Id = isFrontCamera ? CAMERA_FACING_FRONT : CAMERA_FACING_BACK;
        openCamera(isFrontCamera);
        if (mCamera != null) {
            mCamera.startPreview();
        } else {

        }

    }

    /**
     * 打开Camera
     */
    private void openCamera(boolean isFrontCamera) {
        int PreviewWidth = 0;
        int PreviewHeight = 0;
        try {
            if (mCamera == null) {
                mCamera = open(Camera_Id);
            }
            Camera.Parameters param = mCamera.getParameters();
            // 在2.2以上可以使用
            mCamera.setDisplayOrientation(90);

            // 设置完成需要再次调用setParameter方法才能生效
//            mCamera.setParameters(param);
            // 选择合适的预览尺寸
            List<Camera.Size> sizeList = param.getSupportedPreviewSizes();

            // 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
            if (sizeList.size() > 1) {
                Iterator<Camera.Size> itor = sizeList.iterator();
                while (itor.hasNext()) {
                    Camera.Size cur = itor.next();
                    if (cur.width >= PreviewWidth
                            && cur.height >= PreviewHeight) {
                        PreviewWidth = cur.width;
                        PreviewHeight = cur.height;
                        break;
                    }
                }
            }
            // 设置预览大小
//			param.setPreviewSize(PreviewWidth, PreviewHeight); //获得摄像区域的大小
//			param.setPreviewFrameRate(3);//每秒3帧  每秒从摄像头里面获得3个画面
            param.setPictureFormat(PixelFormat.JPEG);//设置照片输出的格式
            param.set("jpeg-quality", 85);//设置照片质量
            surfaceView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(screenHeight, screenWidth));
            // 设置拍照的照片大小
//			param.setPictureSize(640, 480);
//			param.setPictureSize(width, height);
            param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            mCamera.setParameters(param);
            mCamera.setPreviewDisplay(mHolder);
        } catch (Exception e) {
            e.printStackTrace();
            if (mCamera == null) {
                mContext.startActivity(new android.content.Intent(mContext, DialogActivity.class));
                ((android.app.Activity) mContext).finish();
                return;
            }
            mCamera.lock();
            mCamera.release();
            mCamera = null;
            return;
        }

    }

    /**
     * 设置闪光灯模式
     */
    public void setFlashCode(int flashMode) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters mParameters = mCamera.getParameters();
        switch (flashMode) {
            case FLASH_MODE_AUTO:
                mParameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_AUTO);
                break;
            case FLASH_MODE_ON:
                mParameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_ON);
                break;
            case FLASH_MODE_OFF:
                mParameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
                break;
            case FLASH_MODE_TORCH:
                mParameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
                break;
        }
        mCamera.setParameters(mParameters);
    }

    /**
     * 点击对焦
     */
    public void autoFocus(View v, MotionEvent event) {
        if (isShowFrame) {
            return;
        }
        mCamera.autoFocus(new android.hardware.Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, android.hardware.Camera camera) {
                if (success) {
                    camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
                }
            }
        });

        RelativeLayout layout = (RelativeLayout) v;
        // 聚焦框
        final ImageView imageView = new ImageView(mContext);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.focus_box);
        imageView.setImageBitmap(bitmap);
        // 聚焦框大小
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
        params.leftMargin = (int) (event.getX() - bitmap.getWidth());/// 2
        params.topMargin = (int) (event.getY() - bitmap.getHeight() / 2);//
        // 添加聚焦框
        layout.addView(imageView, params);
        imageView.setVisibility(View.VISIBLE);//没有图标，先隐藏
        ScaleAnimation animation = new ScaleAnimation(1, 0.5f, 1, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(300);
        animation.setFillAfter(true);
        animation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {
            }

            @Override
            public void onAnimationEnd(final android.view.animation.Animation animation) {
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(400);
                            ((android.app.Activity) (mContext)).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageResource(R.drawable.focus_box_green);
                                }
                            });
                            Thread.sleep(200);
                            ((android.app.Activity) (mContext)).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.clearAnimation();
                                    imageView.setVisibility(android.view.View.GONE);
                                    isShowFrame = false;
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    ;
                }.start();
            }
        });
        imageView.startAnimation(animation);
        isShowFrame = true;
    }

    /**
     * 拍照
     *
     * @param path   文件保存路径（***.png）
     * @param screen 屏幕旋转方向
     */
    public void takePhoto(final Handler mHandler, final String path, final int screen) {

        mCamera.takePicture(shutterCallback, null, new android.hardware.Camera.PictureCallback() {// 拍照回调

            @Override
            public void onPictureTaken(byte[] data, android.hardware.Camera camera) {

                java.io.FileOutputStream fos = null;
                try {
                    java.io.File file = new java.io.File(path);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    // 拍照完成后旋转会导致内存溢出
                    Bitmap bMapRotate;
                    // 设置输出照片旋转角度
                    Matrix matrix = new Matrix();
                    matrix.reset();
                    if (screen == MySensorManager.SCREEN_LEFT) {// 左横屏
                        videoHeight = height;
                        videoWidth = width;
                        matrix.postRotate(0);
                    } else if (screen == MySensorManager.SCREEN_RIGHT) {// 右横屏
                        videoHeight = height;
                        videoWidth = width;
                        matrix.postRotate(180);
                    } else {// 正反竖屏，
                        videoHeight = width;
                        videoWidth = height;
//						videoHeight = height;
//						videoWidth = width;
                        if (Camera_Id == CAMERA_FACING_FRONT) {
                            matrix.postRotate(270);
                        } else {
                            matrix.postRotate(90);
                        }
                    }
                    bMapRotate = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    bitmap.recycle();
                    bitmap = bMapRotate;
                    // 保存图片到文件
                    fos = new FileOutputStream(file);
                    boolean compress = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    if (compress) {
                        // 保存成功
                        mHandler.sendEmptyMessage(MSG_CODE_PHOTO_SUCCESS);
                    } else {
                        mHandler.sendEmptyMessage(MSG_CODE_PHOTO_FAILURE);
                    }
                    mCamera.startPreview();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(MSG_CODE_PHOTO_FAILURE);
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                            mHandler.sendEmptyMessage(MSG_CODE_PHOTO_FAILURE);
                        }
                    }
                }
            }
        });

    }

    // 快门按下的时候onShutter()被回调
    private Camera.ShutterCallback shutterCallback = new android.hardware.Camera.ShutterCallback() {
        public void onShutter() {
            // 发出提示用户的声音
            ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_MUSIC, android.media.ToneGenerator.MAX_VOLUME);
            tone.startTone(ToneGenerator.TONE_PROP_BEEP2);
        }
    };
}
