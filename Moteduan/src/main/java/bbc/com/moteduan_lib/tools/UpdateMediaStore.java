package bbc.com.moteduan_lib.tools;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.provider.MediaStore;

/**
 * Created by Administrator on 2016/6/4 0004.
 * 增加删除多媒体文件后同步安卓系统媒体库
 */
public class UpdateMediaStore {


    /**
     * 增加媒体文件后更新媒体库
     * @param mContext
     * @param filePath 文件全路径
     */
    public static void updateAdd(Context mContext, String filePath){
        if(!TextUtils.isEmpty(filePath))
            MediaScannerConnection.scanFile(mContext, new String[] {filePath}, null,null);
    }

    /**
     * 删除后，同步更新媒体库
     * @param mContext
     * @param filePath 文件全路径
     */
    public static void updateDelete(Context mContext,String filePath){
        mContext.getContentResolver().delete(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Video.Media.DATA+ " = '" + filePath + "'", null);
    }

}
