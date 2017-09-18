package bbc.com.moteduan_lib.constant;

import android.content.Context;

import java.io.File;

import bbc.com.moteduan_lib.tools.FileUtils;


/**
 * Created by Administrator on 2016/5/16.
 */
public class FilePathContent {


    //视频下载存储路径名称
    private static final String dirName = "video_square/";

    //获取发送图片消息时，ui显示缩略图的缓存路径
    private static final String sendImage_thumbPic_PathName = "sendImage_thumbPic";

    //录音 缓存路径
    private static final String sendVoice_thumbPic_PathName = "sendVoice_thumbPic";


    /**
     * 得到视频下载存储路径
     *  当内置sd卡不可用，返回内置缓存目录
     * @return
     */
    public static String getVideoDownLoadPath(Context context){

        File f = FileUtils.getExternalCache(context,dirName);
        if( f != null ){//&& f.exists()
            //内置sd卡 存储目录
            return f.getAbsolutePath();
        }else{
            //手机内存
            return FileUtils.getCustomerCacheDir(context,dirName).getAbsolutePath();
        }

    }


    /**
     * 获取发送图片消息时，ui显示缩略图的缓存路径
     * @param context
     * @return
     */
    public static File getSengImageThumbnailPath(Context context, String name){

        File f = new File(FileUtils.getExternalCache(context,sendImage_thumbPic_PathName),name);
        if(f == null){
            f = new File(FileUtils.getCustomerCacheDir(context,sendImage_thumbPic_PathName),name);
        }
        return f;
    }

    /**
     * 获取语音 缓存路径
     * @param context
     * @return
     */
    public static File getVoiceThumbnailPath(Context context, String name){

        File f = new File(FileUtils.getExternalCache(context,sendVoice_thumbPic_PathName),name);
        if(f == null){
            f = new File(FileUtils.getCustomerCacheDir(context,sendVoice_thumbPic_PathName),name);
        }
        return f;
    }


    /**
     * 得到保持图片路径
     *   /storage/emulated/0/Android/data/im.bbc.com.rongyunim/files/DCIM/takePicture
     */
    public static String getSaveTakePhotoPath(Context context){

//        java.io.File file = new java.io.File(context.getExternalFilesDir(android.os.Environment.DIRECTORY_DCIM).getAbsolutePath()+"/takePicture");

        File file = new File(android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DCIM).getAbsolutePath(),"/takePicture");

        if(!file.exists()){
            file.mkdirs();
        }
        return file.getAbsolutePath();

    }

}




























