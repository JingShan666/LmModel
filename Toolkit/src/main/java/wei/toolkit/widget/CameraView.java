package wei.toolkit.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import wei.toolkit.R;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.PictureUtil;
import wei.toolkit.utils.ToastUtil;


/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class CameraView extends TextureView implements TextureView.SurfaceTextureListener, MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener, SensorEventListener {
    private final String TAG = "CameraView";
    private Camera camera;
    private SurfaceTexture surfaceTexture;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;
    private int width = DEFAULT_WIDTH;
    private int heigh = DEFAULT_HEIGHT;
    private int mCameraId;
    private RecordListener recordListener;
    private DropListener dropListener;
    // 拍照返回的数据
    private byte[] mPictureData;
    private String mVideoPath;
    /*是否正在录制视频*/
    private boolean isRecord;

    private int ORIENTATION_SCREEN = ORIENTATION_TOP;
    private static final int ORIENTATION_LEFT = 0;
    private static final int ORIENTATION_TOP = 1;
    private static final int ORIENTATION_RIGHT = 2;
    private static final int ORIENTATION_BOTTOM = 3;
    private int mCameraDropOrientation;
    private SensorManager sensorManager;
    private Sensor sensor;
    private int mPreviewVideoWidth;
    private int mPreviewVideoHeight;
    private Camera.Parameters parameters;

    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSurfaceTextureListener(this);
        setFocusable(true);
        setKeepScreenOn(true);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);

    }

    // 拍照,拍完照进行照片预览，并保存照片数据
    public void drop() {
        if (isRecord) {
            return;
        }
        if (camera != null) {
            camera.takePicture(new Camera.ShutterCallback() {
                @Override
                public void onShutter() {
                }
            }, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    mPictureData = null;
                    mPictureData = data;
                    mCameraDropOrientation = ORIENTATION_SCREEN;
                    camera.stopPreview();
                }
            });
        }
    }

    // 确认拍过的照片并从接口中返回保存照片的地址
    public void sure() {
        if (mPictureData != null) {
            if (dropListener != null) {
                Matrix matrix = new Matrix();
                switch (mCameraId) {
                    case 0:
                        switch (mCameraDropOrientation) {
                            case ORIENTATION_LEFT:
                                matrix.setRotate(0);
                                break;
                            case ORIENTATION_TOP:
                                matrix.setRotate(90);
                                break;
                            case ORIENTATION_RIGHT:
                                matrix.setRotate(180);
                                break;
                            case ORIENTATION_BOTTOM:
                                matrix.setRotate(270);
                                break;
                        }
                        break;
                    case 1:
                        switch (mCameraDropOrientation) {
                            case ORIENTATION_LEFT:
                                matrix.setRotate(0);
                                break;
                            case ORIENTATION_TOP:
                                matrix.setRotate(270);
                                break;
                            case ORIENTATION_RIGHT:
                                matrix.setRotate(180);
                                break;
                            case ORIENTATION_BOTTOM:
                                matrix.setRotate(90);
                                break;
                        }
                        break;
                }
                Bitmap bit = BitmapFactory.decodeByteArray(mPictureData, 0, mPictureData.length);
                Bitmap bitmap = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit.getHeight(), matrix, true);
                dropListener.pictureCompleted(PictureUtil.savePicture(bitmap));
                mPictureData = null;
            }
        } else if (!TextUtils.isEmpty(mVideoPath)) {
            if (recordListener != null) {
                recordListener.videoCompleted(mVideoPath);
                mVideoPath = null;
            }
        }
    }


    // 拍视频
    public void record() {
        if (isRecord) {
            return;
        }
        if (camera == null) {
            openCamera(mCameraId);
        }
        camera.unlock();
//        if (mediaRecorder == null) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
        mediaRecorder.setVideoSize(width, heigh);
        // 设置录制文件质量，格式，分辨率之类，这个全部包括了
//        mediarecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));  //7.43M  10frame
//        mediarecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_1080P));   //70.94M  10frame
//        mediarecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_CIF));  // 2.6M  5frame/10frame
//        mediarecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QCIF));  //0.76M   30frame  模糊
//        mediarecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA));  //2.1M
//        mediarecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_TIME_LAPSE_CIF));  //不支持
//        mediarecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));  //766KB  还行  比QUALITY_QCIF好
//        mediarecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_TIME_LAPSE_LOW));  //1M 质量类似LOW
//        mediarecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_TIME_LAPSE_480P));  //480p效果
//        }
        switch (mCameraId) {
            case 0:
                switch (ORIENTATION_SCREEN) {
                    case ORIENTATION_LEFT:
                        mediaRecorder.setOrientationHint(0);
                        break;
                    case ORIENTATION_TOP:
                        mediaRecorder.setOrientationHint(90);
                        break;
                    case ORIENTATION_RIGHT:
                        mediaRecorder.setOrientationHint(180);
                        break;
                    case ORIENTATION_BOTTOM:
                        mediaRecorder.setOrientationHint(270);
                        break;
                }
                break;
            case 1:
                switch (ORIENTATION_SCREEN) {
                    case ORIENTATION_LEFT:
                        mediaRecorder.setOrientationHint(0);
                        break;
                    case ORIENTATION_TOP:
                        mediaRecorder.setOrientationHint(270);
                        break;
                    case ORIENTATION_RIGHT:
                        mediaRecorder.setOrientationHint(180);
                        break;
                    case ORIENTATION_BOTTOM:
                        mediaRecorder.setOrientationHint(90);
                        break;
                }
                break;
        }
        File externalStorage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/lmVideo");
        if (!externalStorage.exists()) {
            externalStorage.mkdir();
        }
        mVideoPath = externalStorage.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp4";
        mediaRecorder.setOutputFile(mVideoPath);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecord = true;
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.show(getContext(), getContext().getString(R.string.v_permission_alert, "录音"), Toast.LENGTH_LONG);
        }

    }

    public void stopRecord() {
        if (!isRecord) {
            return;
        }
        isRecord = false;
        releaseMediaRecorder();
        releaseCamera();
        startPreviewVideo();

    }


    public void startPreviewVideo() {
        if (TextUtils.isEmpty(mVideoPath)) {
            return;
        }
        if (surfaceTexture == null) {
            return;
        }
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(mVideoPath);
                    mediaPlayer.setSurface(new Surface(surfaceTexture));
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setOnVideoSizeChangedListener(videoSizeChangedListener);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mediaPlayer.prepareAsync();

        } catch (RuntimeException e) {
            e.printStackTrace();
            ToastUtil.show(getContext(), getContext().getString(R.string.v_permission_alert, "录音"), Toast.LENGTH_LONG);
        }

    }

    public void releaseMediaRecorder() {
        try {
            if (mediaRecorder != null) {
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void releaseMediaPlay() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.setOnPreparedListener(null);
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void releaseCamera() {
        try {
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 恢复初始状态
    public void reset() {
        openCamera(mCameraId);
    }

    // 摄像头转换
    public void cameraSwitch() {
        openCamera(mCameraId == 0 ? 1 : 0);

    }

    // 初始化摄像头
    public void openCamera(int cameraId) {
        destory();
        mPreviewVideoHeight = 0;
        mPreviewVideoWidth = 0;
        requestLayout();
        int count = Camera.getNumberOfCameras();
        if (cameraId > 1 || cameraId < 0) {
            cameraId = 0;
        }
        mCameraId = count > 1 ? cameraId : 0;
        camera = Camera.open(mCameraId);
        parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
        parameters.setPictureFormat(ImageFormat.JPEG);
        List<Camera.Size> list = parameters.getSupportedPreviewSizes();
        Iterator<Camera.Size> iterator = list.iterator();
        while (iterator.hasNext()) {
            Camera.Size size = iterator.next();
            Loger.log(TAG, cameraId + " camera support width height = " + size.width + " " + size.height);
            if (size.width == 1280 && size.height == 720) {
                width = size.width;
                heigh = size.height;
                break;
            }
        }
        parameters.setPreviewSize(width, heigh);
        parameters.setPictureSize(width, heigh);
        try {
            camera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            camera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.setDisplayOrientation(90);
        camera.autoFocus(autoFocusCallback);
        camera.startPreview();
    }

    public void destory() {
        releaseMediaRecorder();
        releaseMediaPlay();
        releaseCamera();
        if (!TextUtils.isEmpty(mVideoPath)) {
            File file = new File(mVideoPath);
            if (file.exists()) {
                file.delete();
            }
            mVideoPath = null;
        }
        mPictureData = null;
        isRecord = false;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Loger.log(TAG, "onSurfaceTextureAvailable width height = " + width + " " + height);
        surfaceTexture = surface;
        try {
            openCamera(mCameraId);
        } catch (RuntimeException e) {
            e.printStackTrace();
            ToastUtil.show(getContext(), getContext().getString(R.string.v_permission_alert, "相机"), Toast.LENGTH_LONG);
            ((Activity) getContext()).finish();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Loger.log(TAG, "onSurfaceTextureSizeChanged width height = " + width + " " + height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        destory();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {

    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {

    }

    // 自动对焦监听
    Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                camera.cancelAutoFocus();
            }
        }
    };

    public void setDropListener(DropListener dropListener) {
        this.dropListener = dropListener;
    }

    public void setRecordListener(RecordListener recordListener) {
        this.recordListener = recordListener;
    }


    public static interface DropListener {
        public void pictureCompleted(String path);
    }

    public static interface RecordListener {
        public void videoCompleted(String path);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) {
            return;
        }
        int x = (int) event.values[0];
        int y = (int) event.values[1];
        int z = (int) event.values[2];

        if (y > 5) {
            ORIENTATION_SCREEN = ORIENTATION_TOP;
        } else if (y < -5) {
            ORIENTATION_SCREEN = ORIENTATION_BOTTOM;
        } else if (x > 5) {
            ORIENTATION_SCREEN = ORIENTATION_LEFT;
        } else if (x < -5) {
            ORIENTATION_SCREEN = ORIENTATION_RIGHT;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private MediaPlayer.OnVideoSizeChangedListener videoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            mPreviewVideoWidth = mp.getVideoWidth();
            mPreviewVideoHeight = mp.getVideoHeight();
            Loger.log(TAG, "onVideoSizeChanged: " + mPreviewVideoWidth + " " + mPreviewVideoHeight);
            if (mPreviewVideoWidth > 0 && mPreviewVideoHeight > 0) {
                requestLayout();
            }
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(mPreviewVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mPreviewVideoHeight, heightMeasureSpec);

        if (mPreviewVideoWidth > 0 && mPreviewVideoHeight > 0) {
            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize;
                height = heightSpecSize;

                // for compatibility, we adjust size based on aspect ratio
                if (mPreviewVideoWidth * height < width * mPreviewVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mPreviewVideoWidth / mPreviewVideoHeight;
                } else if (mPreviewVideoWidth * height > width * mPreviewVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mPreviewVideoHeight / mPreviewVideoWidth;
                }
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                // only the width is fixed, adjust the height to match aspect ratio if possible
                width = widthSpecSize;
                height = width * mPreviewVideoHeight / mPreviewVideoWidth;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    height = heightSpecSize;
                }
            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                // only the height is fixed, adjust the width to match aspect ratio if possible
                height = heightSpecSize;
                width = height * mPreviewVideoWidth / mPreviewVideoHeight;
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    width = widthSpecSize;
                }
            } else {
                // neither the width nor the height are fixed, try to use actual video size
                width = mPreviewVideoWidth;
                height = mPreviewVideoHeight;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize;
                    width = height * mPreviewVideoWidth / mPreviewVideoHeight;
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize;
                    height = width * mPreviewVideoHeight / mPreviewVideoWidth;
                }
            }
        }
        setMeasuredDimension(width, height);


    }
}
