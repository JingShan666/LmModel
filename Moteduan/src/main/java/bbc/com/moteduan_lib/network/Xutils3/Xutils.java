package bbc.com.moteduan_lib.network.Xutils3;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.Map;

import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.tools.IO_Utils;
import wei.toolkit.utils.Loger;

/**
 * Created by yx on 2016/4/13.
 */
public class Xutils {

    static ImageOptions imageOptions = new ImageOptions.Builder()
            .setUseMemCache(true)
            .setAnimation(new AlphaAnimation(0.3f, 1f))
            .setAutoRotate(true)
            .setConfig(Bitmap.Config.RGB_565)
            .setSize(DensityUtil.dip2px(100), DensityUtil.dip2px(100))//图片大小0
            .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//            .setFailureDrawableId(com.bbc.lm.R.drawable.zanweitu)//加载失败后默认显示图片
            .build();

    static ImageOptions squareImgOps = new ImageOptions.Builder()
            .setUseMemCache(true)
            .setConfig(Bitmap.Config.RGB_565)
            .setAnimation(new AlphaAnimation(0.5f, 1f))
            .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//            .setFailureDrawableId(com.bbc.lm.R.drawable.zanweitu)//加载失败后默认显示图片
            .build();

    static ImageOptions circleImgOps = new ImageOptions.Builder()
            .setUseMemCache(true)
            .setCircular(true)
            .setConfig(Bitmap.Config.RGB_565)
            .setAnimation(new AlphaAnimation(0.5f, 1f))
            .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//            .setLoadingDrawableId(R.drawable.yujiazai_xiao)//加载中默认显示图片
//            .setFailureDrawableId(R.drawable.yujiazai_xiao)//加载失败后默认显示图片
            .build();

    static ImageOptions rect_circle = new ImageOptions.Builder()
            .setUseMemCache(true)
            .setConfig(Bitmap.Config.RGB_565)
            .setAnimation(new AlphaAnimation(0.5f, 1f))
            .setAutoRotate(true)
            .setCrop(true)
            .setSize(DensityUtil.dip2px(175), DensityUtil.dip2px(260))//图片大小
            .setRadius(DensityUtil.dip2px(18))//ImageView圆角半径
            .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
//            .setLoadingDrawableId(R.drawable.yujiazai)//加载中默认显示图片
//            .setFailureDrawableId(R.drawable.yujiazai)//加载失败后默认显示图片
            .build();

    private static IO_Utils ioUtils = null;

    /**
     * 加载图片(100 x 100)
     *
     * @param view
     * @param url
     */
    public static void setRemoteImg(ImageView view, String url) {
        x.image().bind(view, url, imageOptions);
    }

    /**
     * 加载图片(100 x 100)
     *
     * @param view
     * @param url
     */
    public static void setRemoteImg(ImageView view, String url, Callback.CommonCallback<Drawable> callback) {
        x.image().bind(view, url, imageOptions, callback);
    }

    /**
     * 加载圆形图片
     *
     * @param view
     * @param url
     */
    public static void setCircularIcon(ImageView view, String url) {
        x.image().bind(view, url, circleImgOps);
    }

    /**
     * 加载圆角矩形图片
     *
     * @param view
     * @param url
     */
    public static void setCircularRectIcon(ImageView view, String url) {
        x.image().bind(view, url, rect_circle);
    }

    /**
     * 加载图片(广场)
     *
     * @param view
     * @param url
     */
    public static void setRemoteSquareImg(ImageView view, String url) {
        x.image().bind(view, url, squareImgOps);
    }


    /**
     * 异步get请求下载
     *
     * @param url
     * @param filepath 文件保存在本地的路径和文件名   String filepath = "";
     * @param callback com.bbc.lm.toools.Xutils3.Xutils.DownLoadFile(url, videoPath, new org.xutils.common.Callback.CommonCallback<java.io.File >()
     * @param <T>
     * @return
     */
    public static <T> Callback.Cancelable DownLoadFile(String url, String filepath, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(filepath);
        params.setConnectTimeout(20 * 1000);//20秒
        params.setCancelFast(true);
//        params.setAutoRename(true);//默认以文件名，存到 外部cache目录（同名会在前加毫秒 以 共存）
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    /**
     * 发送异步get请求
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable get(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        LogDebug.print(url);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
//                params.addParameter(entry.getKey(), entry.getValue().toString());
                LogDebug.print(entry.getKey() + " = " + entry.getValue());
                params.addQueryStringParameter(entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString());
            }
        }
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    /**
     * 发送异步post请求
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable post(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
        Loger.log("post", "url=" + url);
        RequestParams params = new RequestParams(url);
        params.setReadTimeout(1000 * 30);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
                Loger.log("post", entry.getKey() + "=" + entry.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }


    /**
     * 使用表单，发送异步post请求
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable postByFrom(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        LogDebug.print(url);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue(), null);
                LogDebug.print(entry.getKey() + " = " + entry.getValue());
            }
        }
        params.setMultipart(true);
        Callback.Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }


    /**
     * 使用  HttpURLConnection 下载
     *
     * @param url
     * @param filePath xx/oo/mm
     * @param fileName test.mp4
     */
    public static void downLoad(final String url, final String filePath, final String fileName, final IO_Utils.SaveSuccessCallBackListener listener) {

        new Thread(new Runnable() {
            private java.net.HttpURLConnection conn;

            @Override
            public void run() {
                final java.io.File video = new java.io.File(filePath, fileName);
                try {
                    conn = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
                    conn.setConnectTimeout(20 * 1000);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("GET");
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        java.io.InputStream stream = conn.getInputStream();

                        if (ioUtils == null)
                            ioUtils = new IO_Utils();
                        ioUtils.saveFile(stream, video.getAbsolutePath(), listener);
                    } else {
                        LogDebug.print("请求失败，返回状态码 = " + conn.getResponseCode());
                    }

                } catch (java.net.MalformedURLException e) {
                    e.printStackTrace();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();

    }


}
