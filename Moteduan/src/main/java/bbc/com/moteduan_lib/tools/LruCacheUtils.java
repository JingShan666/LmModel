package bbc.com.moteduan_lib.tools;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Administrator on 2016/5/12.
 */
public class LruCacheUtils {

    private LruCache<String, Bitmap> mMemoryCache;
    private int bs = 0;

    /**
     * 使用bs分之一作为缓存大小
     * @param bs
     */
    public LruCacheUtils(int bs) {
        super();
    }

    public LruCacheUtils() {
        // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
        // LruCache通过构造函数传入缓存值，以KB为单位。
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//        com.bbc.lm.log.LogDebug.print("maxMemory =            =============="+maxMemory);
        // 使用最大可用内存值的1/8作为缓存的大小。
        int cacheSize;
        if(bs == 0){
            cacheSize = maxMemory / 8;
        }else{
            cacheSize = maxMemory / bs;
        }

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {

        return mMemoryCache.get(key);
    }


}
