package bbc.com.moteduan_lib.renzheng;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.bean.VideoInfo;

/**
 * Created by Administrator on 2016/6/1 0001.
 */
public class VideoPlay extends android.support.v7.app.AppCompatActivity {

//    private android.media.MediaPlayer player;
    private android.widget.VideoView videoView;
    private android.widget.MediaController controller;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,  android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.play_video);
        initView();
        initData();
    }

    public void initView() {
        final VideoInfo video = getIntent().getParcelableExtra("video");

//        player = new android.media.MediaPlayer();
//        try {
//            player.setDataSource(video.getData() );
//        } catch (java.io.IOException e) {
//            e.printStackTrace();
//        }

        videoView = (android.widget.VideoView) findViewById(R.id.video);
        controller = new android.widget.MediaController(this);
        videoView.setVideoPath(video.getData());
        videoView.setMediaController(controller);
        videoView.setOnPreparedListener(new android.media.MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(android.media.MediaPlayer mp) {
                mp.start();
            }
        });
//        videoView.start();

//        final android.view.ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
//        com.bbc.lm.log.LogDebug.print( layoutParams.width, layoutParams.height );

//        player.prepareAsync();
//        player.setOnPreparedListener(new android.media.MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(android.media.MediaPlayer mp) {
//                com.bbc.lm.log.LogDebug.print(player.getVideoWidth() , player.getVideoWidth());
//
//                layoutParams.width =  player.getVideoWidth();
//                layoutParams.height = player.getVideoWidth();
//                player.release();
//
//                videoView.setLayoutParams( layoutParams );
//                //controller.show(0);
//
//                videoView.setVideoPath(video.getData());
//                videoView.setMediaController(controller);
//                videoView.start();
//            }
//        });

    }

    public void initData() {

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(videoView.isPlaying()){
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        videoView.start();
    }
}
