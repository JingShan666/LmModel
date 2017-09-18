package wei.toolkit.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import wei.toolkit.R;


/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class ImageLoad {
    public static final String TAG = "ImageLoad";

    public static void bind(ImageView imageView, Object url) {
        try {
            getGlide(imageView.getContext(), url)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bind(ImageView imageView, Object url, int width, int height) {
        try {
            if (width < 1 || height < 1) {
                bind(imageView, url);
            } else {
                getGlide(imageView.getContext(), url)
                        .override(width, height)
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bindOssThumbnail(ImageView imageView, Object url, int width, int height, int thumbnailWidth, int thumbnailHeight) {
        try {
            Context context = imageView.getContext();
            Loger.log(TAG, "thumbnailWidth thumbnailHeight = " + " " + thumbnailWidth + " " + thumbnailHeight);
            int max = Math.max(thumbnailWidth, thumbnailHeight);
            if (max > 600) {
                float scale = max / 600f;
                int w = Math.round(thumbnailWidth / scale);
                int h = Math.round(thumbnailHeight / scale);
                String thumbnailSuffix = String.format(context.getResources().getString(R.string.oss_image_thumbnail), w, h);
                Loger.log(TAG, "thumbnailSuffix = " + thumbnailSuffix + " " + w + " " + h + " " + scale);
                getGlide(context, url + thumbnailSuffix)
                        .override(width, height)
                        .into(imageView);
            } else {
                bind(imageView, url, width, height);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bindOssThumbnail(ImageView imageView, Object url, int thumbnailWidth, int thumbnailHeight) {
        try {
            Context context = imageView.getContext();
            Loger.log(TAG, "thumbnailWidth thumbnailHeight = " + " " + thumbnailWidth + " " + thumbnailHeight);
            int max = Math.max(thumbnailWidth, thumbnailHeight);
            if (max > 600f) {
                float scale = max / 600f;
                int w = Math.round(thumbnailWidth / scale);
                int h = Math.round(thumbnailHeight / scale);
                String thumbnailSuffix = String.format(context.getResources().getString(R.string.oss_image_thumbnail), w, h);
                Loger.log(TAG, "thumbnailSuffix = " + thumbnailSuffix + " " + w + " " + h + " " + scale);
                getGlide(context, url + thumbnailSuffix)
                        .into(imageView);
            } else {
                bind(imageView, url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bind(ImageView imageView, Object url, int blurValue) {
        try {
            if (Build.VERSION.SDK_INT <= 17) {
                bind(imageView, url);
            } else {
                getGlide(imageView.getContext(), url)
                        .bitmapTransform(new BlurTransformation(imageView.getContext(), blurValue))
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bind(ImageView imageView, Object url, float thumbnailScale) {
        try {
            getGlide(imageView.getContext(), url)
                    .thumbnail(thumbnailScale)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bindRounded(ImageView imageView, Object url, int radius) {
        try {
            getGlide(imageView.getContext(), url)
                    .bitmapTransform(new RoundedCornersTransformation(imageView.getContext(), radius, 0))
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static DrawableRequestBuilder getGlide(Context context, Object url) {
        return Glide.with(context)
                .load(url)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Object, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Object model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Loger.log(TAG, "onException model = " + model + " isFirstResource = " + isFirstResource);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Object model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Loger.log(TAG, "onResourceReady model = " + model + " isFromMemoryCache = " + isFromMemoryCache + " isFirstResource = " + isFirstResource);
                        return false;
                    }
                });
    }
}
