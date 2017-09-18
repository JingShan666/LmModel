package bbc.com.moteduan_lib.tools;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class VideoUtils {

    /**
     * @param videoPath
     * @return video缩略图
     */
    public static Bitmap getThumbnail(String videoPath) {
        if (TextUtils.isEmpty(videoPath)) {
            return null;
        }
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(videoPath);
        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(-1);
        return bitmap;
    }
}
