package wei.toolkit.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;

import java.io.IOException;

import wei.toolkit.utils.Loger;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class VideoPlayView extends TextureView implements TextureView.SurfaceTextureListener {
    private final String TAG = "VideoPlayView";
    private MediaPlayer mediaPlayer;
    private SurfaceTexture surfaceTexture;
    private String videoPath;
    private int currentPosition;
    private int mVideoWidth;
    private int mVideoHeight;

    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnPreparedListener mOnPreparedListener;

    public VideoPlayView(Context context) {
        super(context);
        init(context);
    }

    public VideoPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public VideoPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        setSurfaceTextureListener(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setSurface(new Surface(surfaceTexture));
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(preparedListener);
            mediaPlayer.setOnCompletionListener(completionListener);
            mediaPlayer.setOnBufferingUpdateListener(bufferingUpdateListener);
            mediaPlayer.setOnErrorListener(errorListener);
            mediaPlayer.setOnInfoListener(infoListener);
//            mediaPlayer.setOnTimedMetaDataAvailableListener(timedMetaDataAvailableListener);
            mediaPlayer.setOnSeekCompleteListener(seekCompleteListener);
            mediaPlayer.setOnTimedTextListener(timedTextListener);
            mediaPlayer.setOnVideoSizeChangedListener(videoSizeChangedListener);
        }
    }


    public VideoPlayView setPath(String path) {
        this.videoPath = path;
        return this;
    }

    public void prepare() {
        initMediaPlayer();
        if (mediaPlayer != null) {
            try {
                mediaPlayer.setDataSource(videoPath);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void prepareAsync() {
        initMediaPlayer();
        if (mediaPlayer != null) {
            try {
                mediaPlayer.setDataSource(videoPath);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            currentPosition = 0;
        }

    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            currentPosition = mediaPlayer.getCurrentPosition();
        }
    }

    public void reset() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            currentPosition = 0;
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            currentPosition = 0;
        }
    }

    public boolean isPlaying() {
        if (mediaPlayer == null) {
            return false;
        }
        return mediaPlayer.isPlaying();
    }

    public int getCurrentPosition() {
        if (mediaPlayer == null) {
            return -1;
        }
        return currentPosition = mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    public void destroy() {
        pause();
        stop();
        reset();
        release();
        mediaPlayer = null;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Loger.log(TAG, "onSurfaceTextureAvailable");
        this.surfaceTexture = surface;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Loger.log(TAG, "onSurfaceTextureSizeChanged");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Loger.log(TAG, "onSurfaceTextureDestroyed");
        destroy();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        Loger.log(TAG, "onSurfaceTextureUpdated");
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        this.mOnCompletionListener = onCompletionListener;
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
        this.mOnPreparedListener = onPreparedListener;
    }

    private MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mp);
            }
        }
    };

    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if (mOnCompletionListener != null) {
                mOnCompletionListener.onCompletion(mp);
            }
        }
    };

    private MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {

        }
    };

    private MediaPlayer.OnTimedTextListener timedTextListener = new MediaPlayer.OnTimedTextListener() {
        @Override
        public void onTimedText(MediaPlayer mp, TimedText text) {

        }
    };

    private MediaPlayer.OnSeekCompleteListener seekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {

        }
    };

    // 23版本以下的SDK都没有这个类，6.0以下android系统手机运行会报错
//    private MediaPlayer.OnTimedMetaDataAvailableListener timedMetaDataAvailableListener = new MediaPlayer.OnTimedMetaDataAvailableListener() {
//        @Override
//        public void onTimedMetaDataAvailable(MediaPlayer mp, TimedMetaData data) {
//
//        }
//    };

    private MediaPlayer.OnInfoListener infoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return false;
        }
    };

    private MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    };

    private MediaPlayer.OnVideoSizeChangedListener videoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            Loger.log(TAG,"onVideoSizeChanged: "+mVideoWidth+" "+mVideoHeight);
            if (mVideoWidth > 0 && mVideoHeight > 0) {
               requestLayout();
            }
        }
    };


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);

        if (mVideoWidth > 0 && mVideoHeight > 0) {
            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize;
                height = heightSpecSize;

                // for compatibility, we adjust size based on aspect ratio
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                // only the width is fixed, adjust the height to match aspect ratio if possible
                width = widthSpecSize;
                height = width * mVideoHeight / mVideoWidth;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    height = heightSpecSize;
                }
            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                // only the height is fixed, adjust the width to match aspect ratio if possible
                height = heightSpecSize;
                width = height * mVideoWidth / mVideoHeight;
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    width = widthSpecSize;
                }
            } else {
                // neither the width nor the height are fixed, try to use actual video size
                width = mVideoWidth;
                height = mVideoHeight;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize;
                    width = height * mVideoWidth / mVideoHeight;
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize;
                    height = width * mVideoHeight / mVideoWidth;
                }
            }
        }

        setMeasuredDimension(width, height);
    }
}
