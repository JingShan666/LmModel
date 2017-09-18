package bbc.com.moteduan_lib.tools;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import java.util.List;

import bbc.com.moteduan_lib.bean.VideoInfo;
import bbc.com.moteduan_lib.log.LogDebug;

/**
 * Created by Administrator on 2016/5/31 0031.
 */
public class LoadVideoUtils {

    static String progress[] = {

            MediaStore.Video.Media.DISPLAY_NAME,//视频的名字
            MediaStore.Video.Media.SIZE,//大小
            MediaStore.Video.Media.DURATION,//长度
            MediaStore.Video.Media.DATA,//播放地址
            MediaStore.Video.Media._ID,//视频id
    };

    public static List<VideoInfo> getVideo(android.content.Context context) {

        List<VideoInfo> temp = new java.util.ArrayList<VideoInfo>();

        //获取数据提供者,this是上下文
       ContentResolver cr = context.getContentResolver();

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //有sd卡的情况
            Cursor cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, progress, null, null, MediaStore.Video.Media.DATE_TAKEN + " desc");
            while (cursor.moveToNext()) {
                // 到视频文件的信息
                String name = cursor.getString(0);//得到视频的名字
                long size = cursor.getLong(1);//得到视频的大小
                long durantion = cursor.getLong(2);//得到视频的时间长度
                String data = cursor.getString(3);//得到视频的路径，可以转化为uri进行视频播放
                long id = cursor.getLong(4);

                //使用静态方法获取视频的缩略图
//                android.graphics.Bitmap thumbnail = android.media.ThumbnailUtils.createVideoThumbnail(data, android.provider.MediaStore.Video.Thumbnails.MINI_KIND);
                VideoInfo videoInfo = new VideoInfo();
                //创建视频信息对象
                videoInfo.setId(id);
                videoInfo.setVideoName(name);
                videoInfo.setData(data);
                videoInfo.setDuration(durantion);
                videoInfo.setSize(size);

                videoInfo.setBmp(getThum(cr,id));
//                videoInfo.setThumImage(thumbnail);
                LogDebug.err("EXTERNAL_CONTENT_URI:"+data);

                temp.add(videoInfo);
            }
        }

        //不论是否有sd卡都要查询手机内存
       /* android.database.Cursor cursor = cr.query(android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI, progress, null, null, null);
        while (cursor.moveToNext()) {
            // 到视频文件的信息
            String name = cursor.getString(0);//得到视频的名字
            long size = cursor.getLong(1);//得到视频的大小
            long durantion = cursor.getLong(2);//得到视频的时间长度
            String data = cursor.getString(3);//得到视频的路径，可以转化为uri进行视频播放
            long id = cursor.getLong(4);

            //使用静态方法获取视频的缩略图
//            android.graphics.Bitmap thumbnail = android.media.ThumbnailUtils.createVideoThumbnail(data, android.provider.MediaStore.Video.Thumbnails.MINI_KIND);
            VideoInfo videoInfo = new VideoInfo();
            //创建视频信息对象
            videoInfo.setId(id);
            videoInfo.setData(data);
            videoInfo.setVideoName(name);
            videoInfo.setDuration(durantion);
            videoInfo.setSize(size);
            videoInfo.setBmp(getThum(cr,id));
            LogDebug.err("INTERNAL_CONTENT_URI:"+data);
            temp.add(videoInfo);
        }*/

        return temp;
    }

    /**
     * 获取视频缩略图
     * @param cr
     * @param id 视频媒体库中的id
     * @return
     */
    public static android.graphics.Bitmap getThum(ContentResolver cr,long id) {
        /*
         * 可以访问android.provider.MediaStore.android.provider.MediaStore.Video.Thumbnails查询图片缩略图
         * Thumbnails下的getThumbnail方法可以获得图片缩略图，其中第三个参数类型还可以选择MINI_KIND，但MICRO_KIND更小
          */
        android.graphics.Bitmap thumbnail = MediaStore.Video.Thumbnails.getThumbnail(cr, id, MediaStore.Video.Thumbnails.MICRO_KIND, null);
       return thumbnail;

    }

    /**
     * 使用静态方法获取视频的缩略图
     * @param data 视频完整的路劲
     * @return
     */
    public static android.graphics.Bitmap getThum(String data) {
        android.graphics.Bitmap thumbnail = android.media.ThumbnailUtils.createVideoThumbnail(data, MediaStore.Video.Thumbnails.MINI_KIND);
       return thumbnail;

    }


}
