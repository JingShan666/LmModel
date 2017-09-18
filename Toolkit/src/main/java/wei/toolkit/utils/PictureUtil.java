package wei.toolkit.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class PictureUtil {
    private static final String TAG = "PictureUtil";

    public static String pictureCompress(String oldPath) {
        byte[] bytes = pictureCompress(getThumbnail(oldPath, 1920f));
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(oldPath);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return oldPath;
    }

    public static byte[] pictureCompress(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        Loger.log(TAG, baos.size() / 1024 + " 压缩前");
        while (baos.size() / 1024 > 300 && quality > 0) {
            quality -= 10;
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            Loger.log(TAG, baos.size() / 1024 + " 压缩中");
        }
        Loger.log(TAG, baos.size() / 1024 + " 压缩后");
        return baos.toByteArray();
    }

    public static String savePicture(byte[] bytes) {
        return savePicture(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
    }

    public static String savePicture(Bitmap bitmap) {
        byte[] data = pictureCompress(bitmap);
        String path = getPicSaveAddress().getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            path = "";
        } catch (IOException e) {
            e.printStackTrace();
            path = "";
        }
        return path;
    }


    public static Bitmap getNetworkVideoThumbnail(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filePath, new HashMap<String, String>());
        Bitmap bitmap = getThumbnail(retriever.getFrameAtTime(-1));
        retriever.release();
        return bitmap;
    }

    public static Bitmap getLocalVideoThumbnail(String filePath) {
        return ThumbnailUtils.createVideoThumbnail(filePath, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
    }


    public static Bitmap getThumbnail(String filePath, float size) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int w = options.outWidth;
        int h = options.outHeight;
        int max = Math.max(w, h);
        int scale = 1;
        if (max > size) {
            scale = Math.round(max / size);
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap getThumbnail(String filePath) {
        return getThumbnail(filePath, 512f);
    }

    public static Bitmap getThumbnail(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return getThumbnail(baos.toByteArray(), 512f);
    }

    public static Bitmap getThumbanil(byte[] bytes) {
        return getThumbnail(bytes, 521f);
    }

    public static Bitmap getThumbnail(byte[] bytes, float size) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        int w = options.outWidth;
        int h = options.outHeight;
        int max = Math.max(w, h);
        int scale = 1;
        if (max > size) {
            scale = Math.round(max / size);
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }


    /**
     * @param context
     * @param resId
     * @return 资源图片的uri
     */
    public static Uri getResourceUri(Context context, int resId) {
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + context.getResources().getResourcePackageName(resId) + "/"
                + context.getResources().getResourceTypeName(resId) + "/"
                + context.getResources().getResourceEntryName(resId));
        return uri;
    }


    /**
     * 获取保存图片的地址
     *
     * @return
     */
    public static File getPicSaveAddress() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Loger.log(TAG, "外部储存卡不存在");
            return null;
        }
        File externalStorage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/lm");
        if (!externalStorage.exists()) {
            externalStorage.mkdir();
        }
        return externalStorage;
    }

    /**
     * 启动相机
     *
     * @param context
     */
    public static void startSystemCamera(Context context, Uri uri, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        ActivityCompat.startActivityForResult(((Activity) context), intent, requestCode, null);
    }

    /**
     * 剪切图片
     *
     * @param context
     * @param uri
     */
    public static void startSystemPhotoZoom(Context context, Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 320);
//        intent.putExtra("outputY", 320);
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", true);
//        intent.putExtra("output", uri);
        ActivityCompat.startActivityForResult(((Activity) context), intent, requestCode, null);
    }

    /**
     * 调用系统相册
     *
     * @param context
     * @param requestCode
     */
    public static void startSystemAlbum(Context context, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        ActivityCompat.startActivityForResult(((Activity) context), intent, requestCode, null);
    }

}
