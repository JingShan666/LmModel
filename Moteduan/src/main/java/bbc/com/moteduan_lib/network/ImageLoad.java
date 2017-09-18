package bbc.com.moteduan_lib.network;

import android.widget.ImageView;


/**
 * Created by Administrator on 2017/5/18 0018.
 */

public class ImageLoad {

    public static void bind(ImageView imageView, Object url) {
        wei.toolkit.utils.ImageLoad.bind(imageView, url);
    }

    public static void bind(ImageView imageView, Object url, int value) {
        wei.toolkit.utils.ImageLoad.bind(imageView, url, value);
    }

    public static void bindRounded(ImageView imageView, Object url, int radius) {
        wei.toolkit.utils.ImageLoad.bindRounded(imageView, url, radius);
    }
}
