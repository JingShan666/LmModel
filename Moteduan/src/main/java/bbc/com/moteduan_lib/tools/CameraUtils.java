package bbc.com.moteduan_lib.tools;

/**
 * Created by Administrator on 2016/5/21.
 * 21 版本以上使用 Camera2
 * 兼容21版本以下 还是需要用Camera
 */
public class CameraUtils {

    public static final boolean CAMERA_FACING_BACK = true;
    public static final boolean CAMERA_FACING_FRONT = false;

    /**
     * 检查是否有相机
     */
    public static boolean checkCameraHardware(android.content.Context context) {
        if (context.getPackageManager().hasSystemFeature(android.content.pm.PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    /**
     * 摄像头数目
     * （如果等于2，有两个摄像头：0后置  , 1前置）
     */
    public static int getCameraNums() {
        return android.hardware.Camera.getNumberOfCameras();
    }

    /**
     * 以安全的方式获取默认打开的相机
     */
    public static android.hardware.Camera getCameraInstance() {
        android.hardware.Camera c = null;
        try {
            c = android.hardware.Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }

        return c; // returns null if camera is unavailable
    }

    /**
     * 得到摄像头id
     * @param context
     * @param backOrFront = true 获取后置  ，  false 获取前置
     * @return
     */
    public static int getCameraId(android.content.Context context, boolean backOrFront) {
        int defaultId = -1;

        // Find the total number of cameras available
        int mNumberOfCameras = android.hardware.Camera.getNumberOfCameras();

            if (mNumberOfCameras > 0) {
                defaultId = 0;
            } else {
                // 没有摄像头
                android.widget.Toast.makeText(context.getApplicationContext(), "该设备没有摄像头",
                        android.widget.Toast.LENGTH_LONG).show();
                return defaultId;
            }

        // Find the ID of the default camera
        android.hardware.Camera.CameraInfo cameraInfo = new android.hardware.Camera.CameraInfo();
            for (int i = 0; i < mNumberOfCameras; i++) {
                android.hardware.Camera.getCameraInfo(i, cameraInfo);
                if( backOrFront ) { //后置摄像头
                    if (cameraInfo.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK) {
                        defaultId = i;
                    }
                }else{//前置摄像头
                    if (cameraInfo.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        defaultId = i;
                    }
                }
            }

        return defaultId;
    }



    private static void printList(java.util.List<android.hardware.Camera.Size> list){

        for (android.hardware.Camera.Size s:list){
//            com.bbc.lm.log.LogDebug.print("支持的 宽 = "+s.width +"   高 = "+ s.height );
        }

    }

    //相机参数的初始化设置
    public static void initCamera(android.content.Context context, android.hardware.Camera camera,boolean backOrFront,android.view.SurfaceView sfView,int width, int height)
    {
        android.hardware.Camera.Parameters parameters = camera.getParameters();
        parameters.setPictureFormat(android.graphics.PixelFormat.JPEG);
        //parameters.setPictureSize(surfaceView.getWidth(), surfaceView.getHeight());  // 部分定制手机，无法正常识别该方法。
//        parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);//设置闪光模式。
        if(backOrFront)
        parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);

        /*

        I: 预览支持
I: 支持的 宽 = 960   高 = 720
I: 支持的 宽 = 1280  高 = 720
I: 支持的 宽 = 704   高 = 704
I: 支持的 宽 = 352   高 = 288
I: 支持的 宽 = 640   高 = 480
I: 支持的 宽 = 320   高 = 320
I: 支持的 宽 = 320   高 = 240
        I: 图片支持
I: 支持的 宽 = 1280   高 = 960
I: 支持的 宽 = 1392   高 = 1392
I: 支持的 宽 = 640    高 = 480
I: 支持的 宽 = 1280   高 = 720
I: 支持的 宽 = 720    高 = 480
I: 支持的 宽 = 320    高 = 240

         */
//        com.bbc.lm.log.LogDebug.print("预览支持");
        printList(parameters.getSupportedPreviewSizes());
//        com.bbc.lm.log.LogDebug.print("图片支持");
        printList(parameters.getSupportedPictureSizes());


        /*test*/
        parameters.setPictureSize(480,640);
        parameters.setPreviewSize(480,640);
        sfView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(480,640));




        // surface 短边比长边  , 这时候得到的宽高比就是我们所能接受的最小比例
//        final float ratio = (float) width / (float)height;
//        final float ratio = (float) ScreenUtils.getScreenWidth(context) / (float)ScreenUtils.getScreenHeight(context);
//
//        com.bbc.lm.log.LogDebug.print(width +"  " + height +"  "+ ratio );
//
//        android.hardware.Camera.Parameters parameters = camera.getParameters();
//        android.hardware.Camera.Size mBestPictureSize;
//        android.hardware.Camera.Size mBestPreviewSize;
//
//        // 设置pictureSize
//        java.util.List<android.hardware.Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
//        mBestPictureSize =findBestPictureSize(pictureSizes, parameters.getPictureSize(), ratio);
//        parameters.setPictureSize(mBestPictureSize.width, mBestPictureSize.height);
//
//
//        // 设置previewSize
//        java.util.List<android.hardware.Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
//        mBestPreviewSize = findBestPreviewSize(previewSizes, parameters.getPreviewSize(), mBestPictureSize, ratio);
//        parameters.setPreviewSize(mBestPreviewSize.width, mBestPreviewSize.height);
//
//        //设置surface宽高
//        android.view.ViewGroup.LayoutParams params = sfView.getLayoutParams();
//        params.height = sfView.getWidth() * mBestPreviewSize.width / mBestPreviewSize.height;
//        sfView.setLayoutParams(params);
//
//        com.bbc.lm.log.LogDebug.print("获取适配的宽高 = "+mBestPictureSize.width+"______"+ mBestPictureSize.height);




        try {
            camera.setParameters(parameters);
        } catch (RuntimeException e) {
//            如法适配该机型
        }

        camera.startPreview();
        camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
    }

    /**
     * 找到短边比长边大于于所接受的最小比例的最大尺寸
     * 注意：旋转了相机的，所以计算的时候，对surfaceView的比例是宽除以高，而对Camera.Size则是高除以宽。
     * @param sizes       支持的尺寸列表
     * @param defaultSize 默认大小
     * @param minRatio    相机图片短边比长边所接受的最小比例
     * @return 返回计算之后的尺寸
     */
    private static android.hardware.Camera.Size findBestPictureSize(java.util.List<android.hardware.Camera.Size> sizes, android.hardware.Camera.Size defaultSize, float minRatio) {
        final int MIN_PIXELS = 320 * 480;

        sortSizes(sizes);

//        com.bbc.lm.log.LogDebug.print( "默认的 = "+ defaultSize.height +"  "+defaultSize.width );

        java.util.Iterator<android.hardware.Camera.Size> it = sizes.iterator();
        while (it.hasNext()) {
            android.hardware.Camera.Size size = it.next();
            //移除不满足比例的尺寸
            if ((float) size.height / size.width <= minRatio) {
                it.remove();
                continue;
            }
            //移除太小的尺寸
            if (size.width * size.height < MIN_PIXELS) {
                it.remove();
            }
        }

        // 返回符合条件中最大尺寸的一个
        if (!sizes.isEmpty()) {
            return sizes.get(0);
        }
        // 没得选，默认吧
        return defaultSize;
    }


    /**
     * @param sizes
     * @param defaultSize
     * @param pictureSize 图片的大小
     * @param minRatio preview短边比长边所接受的最小比例
     * @return
     */
    private static android.hardware.Camera.Size findBestPreviewSize(java.util.List<android.hardware.Camera.Size> sizes, android.hardware.Camera.Size defaultSize,
                                                             android.hardware.Camera.Size pictureSize, float minRatio) {
        final int pictureWidth = pictureSize.width;
        final int pictureHeight = pictureSize.height;
        boolean isBestSize = (pictureHeight / (float)pictureWidth) > minRatio;
        sortSizes(sizes);

        java.util.Iterator<android.hardware.Camera.Size> it = sizes.iterator();
        while (it.hasNext()) {
            android.hardware.Camera.Size size = it.next();
            if ((float) size.height / size.width <= minRatio) {
                it.remove();
                continue;
            }

            // 找到同样的比例，直接返回
            if (isBestSize && size.width * pictureHeight == size.height * pictureWidth) {
                return size;
            }
        }

        // 未找到同样的比例的，返回尺寸最大的
        if (!sizes.isEmpty()) {
            return sizes.get(0);
        }

        // 没得选，默认吧
        return defaultSize;
    }

    private static void sortSizes(java.util.List<android.hardware.Camera.Size> sizes) {
        java.util.Collections.sort(sizes, new java.util.Comparator<android.hardware.Camera.Size>() {
            @Override
            public int compare(android.hardware.Camera.Size a, android.hardware.Camera.Size b) {
                return b.height * b.width - a.height * a.width;
            }
        });
    }



    //相机参数的初始化设置
    public static void initCameraBeiFen(android.content.Context context, android.hardware.Camera camera,boolean backOrFront,android.view.SurfaceView sfView,int width, int height)
    {
        android.hardware.Camera.Parameters parameters = camera.getParameters();
        parameters.setPictureFormat(android.graphics.PixelFormat.JPEG);
        //parameters.setPictureSize(surfaceView.getWidth(), surfaceView.getHeight());  // 部分定制手机，无法正常识别该方法。
//        parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);//设置闪光模式。
        if(backOrFront)
        parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦

        camera.setDisplayOrientation(90);
        camera.setParameters(parameters);

//        设置预览宽高
        java.util.List<android.hardware.Camera.Size> previewSizes = camera.getParameters().getSupportedPreviewSizes();
        double aspectTolerance = Double.MAX_VALUE;
        int preWidth = 0, preHeight = 0;

        double scale = ((double) width) / height;
        for (int i = 0, len = previewSizes.size(); i < len; i++) {
            android.hardware.Camera.Size s = previewSizes.get(i);

//            com.bbc.lm.log.LogDebug.print( s.width +  "  <宽高> " + s.height );
            /*
             960  <宽高> 720
            1280  <宽高> 720
             704  <宽高> 704
             352  <宽高> 288
             640  <宽高> 480
             320  <宽高> 320
             320  <宽高> 240
             */

            double sizeScale = ((double) s.height) / s.width;
            if (Math.abs(scale - sizeScale) < aspectTolerance) {
                aspectTolerance = Math.abs(scale - sizeScale);
                preWidth = s.height; //  - ScreenUtils.getScreenHeight(context)
                preHeight = s.width;
            }
        }
        if (preWidth != 0) {
            sfView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(preWidth, preHeight));
            parameters.setPreviewSize(preWidth, preHeight);
            // mSurface.setLayoutParams(new LayoutParams(720, 1280));
            android.hardware.Camera.Size s = parameters.getPreviewSize();
//            com.bbc.lm.log.LogDebug.print( s.width + " " + s.height);
        }

        camera.startPreview();
        camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
    }




}
