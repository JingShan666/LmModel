package bbc.com.moteduan_lib.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import java.io.FileNotFoundException;

/**
 * @author yx
 *         要想imageView.setImageMatrix()方法起作用，xml得配置android:scaleType="matrix"
 *         matrix.setRotate和matrix.postRotate的区别：
 *         post...：平移、旋转等效果可以叠加在一起；
 *         set...：前一种效果会消失，只有后来的操作，即它会重置Matrix
 */
public class BitMapUtils {

    public static float DISPLAY_WIDTH = 200;
    public static float DISPLAY_HEIGHT = 250;


    //	public static final float DISPLAY_WIDTH = 200;
//	public static final float DISPLAY_HEIGHT =200;
    public static float getDISPLAY_HEIGHT() {
        return DISPLAY_HEIGHT;
    }

    public static void setDISPLAY_HEIGHT(float dISPLAY_HEIGHT) {
        DISPLAY_HEIGHT = dISPLAY_HEIGHT;
    }

    /**
     * 从文件路径  获取  位图,为了防止内存溢出，默认缩放至200x250的位图
     */
    public static Bitmap decodeScaleBitmapFromFilePath(String pathName, Integer width, Integer hight) throws FileNotFoundException {

        if (!new java.io.File(pathName).exists()) {
            throw new FileNotFoundException();
        }
        BitmapFactory.Options op = new BitmapFactory.Options();

		/*
         * 如果inJustDecodeBounds(boolean flag)的参数是true，则不返回bitmap，
		 * 但是能获取bitmap的宽和高（op.outWidth和op.outWidth)
		 */
        op.inJustDecodeBounds = true;

//		因为设置op.inJustDecodeBounds = true_chat，此时获取的是尺寸信息(op中存放)
        BitmapFactory.decodeFile(pathName, op);

//		获取缩放比例(ceil返回大于参数的最小整数,向上取整)
        int wRatio;
        int hRatio;
        if (width == null || hight ==null) {
            wRatio = (int) Math.ceil(op.outWidth / DISPLAY_WIDTH);
            hRatio = (int) Math.ceil((op.outHeight / DISPLAY_HEIGHT));
        } else {
            wRatio = (int) Math.ceil(op.outWidth / (float) width);
            hRatio = (int) Math.ceil((op.outHeight / (float) hight));
        }

//		超出指定宽高，缩放
        if (wRatio > 1 || hRatio > 1) {
            if (wRatio > hRatio) {//如果宽度比高度更加超出指定的值，设置缩放为wRatio分之一
                op.inSampleSize = wRatio;
            } else {
                op.inSampleSize = hRatio;
            }
        }

//        com.bbc.lm.log.LogDebug.print(" 缩放值 = " + op.inSampleSize);
//        com.bbc.lm.log.LogDebug.print("屏幕的宽高 = " + width + " x " + hight);
//        com.bbc.lm.log.LogDebug.print("原宽高 = " + op.outWidth + " x " + op.outHeight);

//		为false  输出缩放的位图
        op.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(pathName, op);

//        bbc.com.lm.log.LogDebug.print("现宽高 = " + bmp.getWidth() + " x " + bmp.getHeight());

        return bmp;

    }


    /**
     * 当超过屏幕尺寸时才按照给定宽高缩放
     * @param pathName
     * @param width  为null 时超过屏幕尺寸  默认200x250
     * @param hight
     * @return
     * @throws FileNotFoundException
     */
    public static Bitmap decodeScaleBitmapFromFilePathWhen_over_screen_size(Context con, String pathName, Integer width, Integer hight) throws FileNotFoundException {

        Bitmap bmp;

        if (!new java.io.File(pathName).exists()) {
            throw new FileNotFoundException();
        }
        BitmapFactory.Options op = new BitmapFactory.Options();

		/*
         * 如果inJustDecodeBounds(boolean flag)的参数是true，则不返回bitmap，
		 * 但是能获取bitmap的宽和高（op.outWidth和op.outWidth)
		 */
        op.inJustDecodeBounds = true;

//		因为设置op.inJustDecodeBounds = true_chat，此时获取的是尺寸信息(op中存放)
        BitmapFactory.decodeFile(pathName, op);

//		获取缩放比例(ceil返回大于参数的最小整数,向上取整)
        int wRatio;
        int hRatio;

        if(op.outWidth > ScreenUtils.getScreenWidth(con) || op.outHeight > ScreenUtils.getScreenHeight(con)){

            if (width == null || hight ==null) {
                wRatio = (int) Math.ceil(op.outWidth / DISPLAY_WIDTH);
                hRatio = (int) Math.ceil((op.outHeight / DISPLAY_HEIGHT));
            } else {
                wRatio = (int) Math.ceil(op.outWidth / (float) width);
                hRatio = (int) Math.ceil((op.outHeight / (float) hight));
            }

//		超出指定宽高，缩放
            if (wRatio > 1 || hRatio > 1) {
                if (wRatio > hRatio) {//如果宽度比高度更加超出指定的值，设置缩放为wRatio分之一
                    op.inSampleSize = wRatio;
                } else {
                    op.inSampleSize = hRatio;
                }
            }

//            com.bbc.lm.log.LogDebug.print(" 缩放值 = " + op.inSampleSize);
//            com.bbc.lm.log.LogDebug.print("屏幕的宽高 = " + width + " x " + hight);
//            com.bbc.lm.log.LogDebug.print("原宽高 = " + op.outWidth + " x " + op.outHeight);

//		为false  输出缩放的位图
            op.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeFile(pathName, op);

//            com.bbc.lm.log.LogDebug.print("现宽高 = " + bmp.getWidth() + " x " + bmp.getHeight());
        }else{
            bmp = BitmapFactory.decodeFile(pathName);
//            com.bbc.lm.log.LogDebug.print("宽高 = " + bmp.getWidth() + " x " + bmp.getHeight());
        }

        return bmp;

    }


    /**
     * //使用静态方法获取视频的缩略图
     *
     * @param path
     * @return
     */
    public static Bitmap getThuml(Context con, String path) {

        //調用ThumbnailUtils類的靜態方法createVideoThumbnail獲取視頻的截圖；
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, android.provider.MediaStore.Video.Thumbnails.MICRO_KIND);

//        com.bbc.lm.log.LogDebug.print("=======" + bitmap.getWidth());
//        com.bbc.lm.log.LogDebug.print(bitmap.getHeight());
        //調用ThumbnailUtils類的靜態方法extractThumbnail將原圖片（即上方截取的圖片）轉化為指定大小；
        //最後一個參數的具體含義我也不太清楚，因為是閉源的；
        Bitmap bitmap2 = ThumbnailUtils.extractThumbnail(bitmap, DimensionConvert.dip2px(con, 100), DimensionConvert.dip2px(con, 100),
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        bitmap.recycle();
        bitmap = null;
        return bitmap2;

//        android.graphics.Bitmap thumbnail = android.media.ThumbnailUtils.createVideoThumbnail(path, android.provider.MediaStore.Video.Thumbnails.MINI_KIND);
//        return thumbnail;
    }


//    public static android.graphics.Bitmap getVideoThumbnail(android.content.ContentResolver cr, String fileName) {
//        android.graphics.Bitmap bitmap = null;
//        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
//        options.inDither = false;
//        options.inPreferredConfig = android.graphics.Bitmap.Config.ARGB_8888;
//        //select condition.
//        String whereClause = android.provider.MediaStore.Video.Media.DATA + "='" + fileName + "'";
//        //colection of results.
//        android.database.Cursor cursor = cr.query(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                new String[]{android.provider.MediaStore.Video.Media._ID}, whereClause,
//                null, null);
//        if (cursor == null || cursor.getCount() == 0) {
//            return null;
//        }
//        cursor.moveToFirst();
//        //image id in image table.
//        String videoId = cursor.getString(cursor
//                .getColumnIndex(android.provider.MediaStore.Video.Media._ID));
//        if (videoId == null) {
//            return null;
//        }
//        cursor.close();
//        long videoIdLong = Long.parseLong(videoId);
//        //via imageid get the bimap type thumbnail in thumbnail table.
//        bitmap = android.provider.MediaStore.Video.Thumbnails.getThumbnail(cr, videoIdLong,
//                android.provider.MediaStore.Images.Thumbnails.MICRO_KIND, options);
//        return bitmap;
//    }

    /**
     * 以原始尺寸取图片
     *
     * @param pathName
     * @return
     */
    public Bitmap decodeBitmapFromFilePath2(String pathName) {
        /*
         * Bitmap bmp = BitmapFactory.decodeStream(
		 * 	getContentResolver().openInputStream(imageFileUri), null,  bmpFactoryOptions);
		 */
        return BitmapFactory.decodeFile(pathName);
    }

    /**
     * 该方法使用native本地方法 效率高，参数需要文件路径名，或者直接提供输入流
     *
     * @param pathName
     * @return
     */
    public Bitmap decodeFileDescriptor(String pathName) {

        java.io.FileInputStream is = null;
        try {
            is = new java.io.FileInputStream(pathName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
//			AssetFileDescriptor afd = getActivity().getContentResolver().openAssetFileDescriptor(thumbUri, "r");

            Bitmap bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, null);
            return bmp;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * View转化为Bitmap:  不能再onCreate中执行！
     * 当前窗口   View view = getWindow().getDecorView();
     */
    public Bitmap viewCutByWindow(android.app.Activity ui) {

        android.view.View view = ui.getWindow().getDecorView();
        return deal(view);
    }

    /**
     * 点击按钮获取当前屏幕截图
     */
    public Bitmap viewCutByWidget(android.view.View v) {

        android.view.View view = v.getRootView();
        return deal(view);

    }

    private Bitmap deal(android.view.View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
//		view.destroyDrawingCache();

        if (bitmap != null) {

            android.graphics.Rect frame = new android.graphics.Rect();
            view.getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            int width = view.getWidth();
            int height = view.getHeight();

//			System.out.println(statusBarHeight);
//			System.out.println(width);
//			System.out.println(height);

            // 去掉标题栏:以(0,statusBarHeight)起点，截取宽高width，height - statusBarHeight的图
            Bitmap b = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width,
                    height - statusBarHeight);

            System.out.println("bitmap got!");
            try {
                java.io.FileOutputStream out = new java.io.FileOutputStream(new java.io.File(android.os.Environment.getExternalStorageDirectory(), "cut2.png"));
//				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                b.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return b;
//			return bitmap;
        } else {
            System.out.println("bitmap is NULL!");
        }
        return null;

    }

    /**
     * 将bitmap保存到指定路径
     *
     * @param bitmap
     * @param savePath = Environment.getExternalStorageDirectory()+"/tu.png"
     */
    public static boolean bitmapConversionPng(Bitmap bitmap, String savePath) throws FileNotFoundException {

        return bitmap.compress(Bitmap.CompressFormat.PNG, 100, new java.io.FileOutputStream(savePath));

    }

    /**
     * 截取图片
     * 图片裁剪，可用这个方法：
     * Bitmap source：要从中截图的原始位图
     * int x:  起始x坐标
     * int y：起始y坐标
     * int width：  要截的图的宽度
     * int height：要截的图的高度
     *
     * @param bitmap
     * @param startX
     * @param startY
     * @param needW
     * @param needH
     * @return // 去掉标题栏:以(0,50)起点，截取宽高720，1230的图
     * //		Bitmap b = Bitmap.createBitmap(bitmap, 0, 50, 720,1230);
     */
    public Bitmap cutBitmap(Bitmap bitmap, int startX, int startY, int needW, int needH) {

        Bitmap b = Bitmap.createBitmap(bitmap, startX, startY, needW, needH);
        return b;
    }

    /**
     * 用createBitmap剪切
     * 用Matrix缩放到自定的宽高
     *
     * @param bitmap 操作的 位图
     * @param ridX   以左上为坐标原点，不需要的，也就是剪切起点x坐标
     * @param ridY
     * @param needW  以（ridX，ridY）为起点开始截取想要的宽高
     * @param needH
     * @return
     */
    public Bitmap cutScaleBitmap(Bitmap bitmap, int ridX, int ridY, int needW, int needH) {

        int oldW = bitmap.getWidth();
        int oldH = bitmap.getHeight();
        float scaleW = needW / oldW;
        float scaleH = needH / oldH;

        android.graphics.Matrix matrix = new android.graphics.Matrix();
//		缩放宽高 scaleW分之一和scaleH分之一
        matrix.postScale(scaleW, scaleH);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

//		当进行的不只是平移变换时，filter参数为true可以进行滤波处理，有助于改善新图像质量;flase时，计算机不做过滤处理。
        return Bitmap.createBitmap(bitmap, ridX, ridY, needW, needH, matrix, true);
    }


    /**
     * 获得圆角图片
     *
     * @param bitmap
     * @param roundPx
     * @return
     */

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(output);
        final android.graphics.Paint paint = new android.graphics.Paint();
        final android.graphics.Rect rect = new android.graphics.Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final android.graphics.RectF rectF = new android.graphics.RectF(rect);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }


    /**
     * 获得带倒影的图片
     *
     * @param bitmap
     * @return
     */

    public static Bitmap getReflectionImageWithOrigin(Bitmap bitmap) {

        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();


        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Bitmap.Config.ARGB_8888);

        android.graphics.Canvas canvas = new android.graphics.Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        android.graphics.Paint defaultPaint = new android.graphics.Paint();

        canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        android.graphics.Paint paint = new android.graphics.Paint();

        android.graphics.LinearGradient shader = new android.graphics.LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, android.graphics.Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;

    }

    /**
     * Bitmap---->Drawable
     */
    public android.graphics.drawable.Drawable BitmapToDrawable(Bitmap bmp) {

        @SuppressWarnings("deprecation")
        android.graphics.drawable.Drawable drawable = new android.graphics.drawable.BitmapDrawable(bmp);
        return drawable;

    }

    /**
     * drawable --> Bitmap
     *
     * @param drawable
     * @return
     */
    public Bitmap drawableToBitmapSimple(android.graphics.drawable.Drawable drawable) // drawable 转换成bitmap
    {
        android.graphics.drawable.BitmapDrawable bd = (android.graphics.drawable.BitmapDrawable) drawable;
        Bitmap bm = bd.getBitmap();

        return bm;
    }

    /**
     * drawable --> Bitmap
     *
     * @param drawable
     * @return
     */
    public Bitmap drawableToBitmap(android.graphics.drawable.Drawable drawable) // drawable 转换成bitmap
    {
        int width = drawable.getIntrinsicWidth();// 取drawable的长宽
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != android.graphics.PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;// 取drawable的颜色格式

        Bitmap bitmap = Bitmap.createBitmap(width, height, config);// 建立对应bitmap
        android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);// 建立对应bitmap的画布

        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);// 把drawable内容画到画布中
        return bitmap;
    }

}

