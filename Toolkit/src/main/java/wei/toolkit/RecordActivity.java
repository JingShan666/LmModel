package wei.toolkit;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import wei.toolkit.utils.Loger;
import wei.toolkit.widget.CameraView;
import wei.toolkit.widget.CircleProgressImage;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class RecordActivity extends AppCompatActivity implements CameraView.DropListener, CircleProgressImage.ProgressListener, CameraView.RecordListener {
    private CameraView cameraView;
    private ImageView cameraSwitch;
    private CircleProgressImage dropBt;
    private ImageView mCameraReset;
    private ImageView mCameraSure;
    private ImageView mBack;
    private boolean isRecord;
    private boolean isLongPress;
    private long mCurrentMillis;
    private TextView mCameraOptTips;
    private TranslateAnimation mTranslateAnimationLeft, mTranslateAnimationRight;

    private int mActionFlag = ACTION_FLAG_PICTURE_VIDEO;
    // 可以拍照和录视频
    public static final int ACTION_FLAG_PICTURE_VIDEO = 0;
    // 只可以拍照
    public static final int ACTION_FLAG_PICTURE = 1;
    // 只可以录视频
    public static final int ACTION_FLAG_VIDEO = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initViews();
        initEvents();

    }

    private void initViews() {
        Intent intent = getIntent();
        if (intent != null) {
            mActionFlag = intent.getIntExtra("action_flag", ACTION_FLAG_PICTURE_VIDEO);
        }
        cameraView = (CameraView) findViewById(R.id.cameraview);
        cameraView.setDropListener(this);
        cameraView.setRecordListener(this);

        mCameraOptTips = (TextView) findViewById(R.id.activity_record_opt_tips);
        if (mActionFlag == ACTION_FLAG_PICTURE_VIDEO) {
            mCameraOptTips.setText("轻触拍照,长安摄像");
        } else if (mActionFlag == ACTION_FLAG_PICTURE) {
            mCameraOptTips.setText("轻触拍照");
        } else if (mActionFlag == ACTION_FLAG_VIDEO) {
            mCameraOptTips.setText("长安摄像");
        }
        cameraSwitch = (ImageView) findViewById(R.id.camera_switch);
        cameraSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.cameraSwitch();
            }
        });


        dropBt = (CircleProgressImage) findViewById(R.id.activity_record_drop_bt);
        dropBt.setProgressListener(this);
        dropBt.setMaxProgress(11 * 1000);

        mCameraReset = (ImageView) findViewById(R.id.camera_reset);
        mCameraSure = (ImageView) findViewById(R.id.activity_record_sure);
        mBack = (ImageView) findViewById(R.id.activity_record_back);
    }

    private void initEvents() {
        dropBt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (mActionFlag == ACTION_FLAG_PICTURE) {
                    return false;
                }

                setRecord(true);
                setLongPress(true);
                dropBt.setScaleX(1.3f);
                dropBt.setScaleY(1.3f);
                mCurrentMillis = System.currentTimeMillis();
                cameraView.record();
                dropBt.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isRecord()) {
                            long temp = System.currentTimeMillis();
                            dropBt.setCurrentProgress(temp - mCurrentMillis);
                            mCurrentMillis = temp;
                            dropBt.postDelayed(this, 0);
                        }
                    }
                }, 0);

                return false;
            }
        });
        dropBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mActionFlag == ACTION_FLAG_VIDEO) {
                    return;
                }

                cameraView.drop();
                uiReset(false);
            }
        });

        dropBt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (isLongPress) {
                        setLongPress(false);
                        if (isRecord()) {
                            setRecord(false);
                            dropBt.reset();
                            uiReset(false);
                            cameraView.stopRecord();
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        mCameraReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.reset();
                uiReset(true);
            }
        });
        mCameraSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.sure();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    ;

    public boolean isRecord() {
        return isRecord;
    }

    public void setRecord(boolean record) {
        isRecord = record;
    }

    public boolean isLongPress() {
        return isLongPress;
    }

    public void setLongPress(boolean longPress) {
        isLongPress = longPress;
    }

    // 切换布局显示
    private void uiReset(boolean swit) {
        if (swit) {
            mBack.setVisibility(View.VISIBLE);
            dropBt.setVisibility(View.VISIBLE);
            mCameraOptTips.setVisibility(View.VISIBLE);
            cameraSwitch.setVisibility(View.VISIBLE);
            mCameraSure.setVisibility(View.INVISIBLE);
            mCameraReset.setVisibility(View.INVISIBLE);
        } else {
            mBack.setVisibility(View.INVISIBLE);
            dropBt.setVisibility(View.INVISIBLE);
            mCameraOptTips.setVisibility(View.INVISIBLE);
            cameraSwitch.setVisibility(View.INVISIBLE);
            if (mTranslateAnimationLeft == null) {
                mTranslateAnimationLeft = new TranslateAnimation(dropBt.getX() - mCameraReset.getX(), 0, 0, 0);
                mTranslateAnimationLeft.setDuration(300);
                mTranslateAnimationRight = new TranslateAnimation(-dropBt.getX() - -mCameraReset.getX(), 0, 0, 0);
                mTranslateAnimationRight.setDuration(300);
            }

            mCameraSure.startAnimation(mTranslateAnimationRight);
            mCameraReset.startAnimation(mTranslateAnimationLeft);
            mCameraSure.setVisibility(View.VISIBLE);
            mCameraReset.setVisibility(View.VISIBLE);

        }

    }


    @Override
    public void progress(float currentProgress, float maxProgress) {
        if (currentProgress >= maxProgress) {
            setRecord(false);
            dropBt.reset();
            uiReset(false);
            cameraView.stopRecord();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.destory();
    }

    @Override
    public void pictureCompleted(String path) {
        Intent intent = new Intent();
        intent.putExtra("picture", path);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void videoCompleted(String path) {
        Intent intent = new Intent();
        intent.putExtra("video", path);
        setResult(RESULT_OK, intent);
        finish();
    }
}
