package bbc.com.moteduan_lib.renzheng;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.bean.Picture;
import bbc.com.moteduan_lib.tools.LoadBitmapUtils;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class PictureChose extends BaseActivity {

    private Adapter adapter;
    private android.widget.GridView mGridView;

    private android.widget.TextView finish;

    private static final int REQUEST_CODE = 110;

    private static final int PHOTO_REQUEST_CUT = 111;


    /**
     * 模特认证
     */
    public static final int AUTH_MODEL = 1006;


    public static final int Auth_other_result = 4000;

    /**
     * 其他认证，单选一张
     */
    public static final int AUTH_OTHER = 1007;

    /**
     * 当前所属类型
     */
    public static int current_type;

    private int model_oldNums;//模特认证 已经选中数量

    /**
     * 选中的pic
     */
    private java.util.List<Picture> checkPic;


    //    private java.util.List<com.bbc.lm.beans.Picture> takePhoto;
//    private static boolean isTakePhoto;
    private java.io.File file;

    /**
     * 适配器数据
     */
    private java.util.List<Picture> listPicData;
    private boolean isUseCarame = false;

    public static final int LOAD_PIC_OK = 1;
    private android.os.Handler handler = new android.os.Handler() {

        public void handleMessage(android.os.Message msg) {

            if (msg.what == LOAD_PIC_OK) {
                listPicData.clear();
                java.util.List<Picture> temp = (java.util.List<Picture>) msg.obj;
//                if(isTakePhoto){//拍照后刷新，最后一张是拍照所得
//                    isTakePhoto = false;
//                    int pos = temp.size()-1;
//                    com.bbc.lm.beans.Picture pic = temp.get(pos);
//                    com.bbc.lm.log.LogDebug.print("最后一张 = "+pic.getData());
//                    temp.remove(pos);
//                    temp.add(0,pic);
//                }
                listPicData.addAll(temp);
                Collections.reverse(listPicData);
                adapter.notifyDataSetChanged();
            }

            if (isUseCarame) {
                android.content.Intent intent = new android.content.Intent(PictureChose.this, Auth.class);
                intent.putExtra("pic", listPicData.get(0));
                intent.putExtra("auth_type", auth_type);
                intent.putExtra("from", "PictureChose");
                startActivity(intent);
                finish();
            }

        }

    };
    private String localTempImgFileName;
    private String localTempImgDir;
    private Bitmap srcbitmap;
    private String auth_type;
    private File f;
    private Bitmap image;
    private File dir;
    private int width;
    private int height;
    private int imgwidth;
    private int imgheight;
    private String imagePath;
    private int scaleX;
    private int scaleY;
    private int bitmapHeight;
    private int bitmapWidth;
    private Uri uritempFile;
    private ImageView back;


    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.idcard_chose_photo);

        initView();
        initData();
    }


    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }

    @Override
    public void initView() {


        WindowManager wm = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        listPicData = new java.util.ArrayList<>();
        checkPic = new java.util.ArrayList<>();
        adapter = new PictureChose.Adapter();

        finish = (android.widget.TextView) findViewById(R.id.finish);
        /*finish.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {

               android.content.Intent intent = new android.content.Intent();
                intent.putParcelableArrayListExtra("choosePic", (java.util.ArrayList<? extends android.os.Parcelable>) checkPic);
//                setResult(Auth_model.IMAGE_RESULT_CODE, intent);
                finish();

            }
        });*/


        mGridView = (android.widget.GridView) findViewById(R.id.gridView);
        mGridView.setFriction(android.view.ViewConfiguration.getScrollFriction() * 3);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                localTempImgDir = Environment.getExternalStorageDirectory() + "/aImage/";
                if (position == 0) {
                    isUseCarame = true;
                    android.content.Intent intent = new android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    //先验证手机是否有sdcard
                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) {
                        try {
                            dir = new File(Environment.getExternalStorageDirectory() + "/" + localTempImgDir);
                            if (!dir.exists()) dir.mkdirs();
                            localTempImgFileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                            //localTempImgDir和localTempImageFileName是自己定义的名字
                            f = new File(dir, localTempImgFileName);
                            Uri u = Uri.fromFile(f);

                            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                        } catch (ActivityNotFoundException e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(PictureChose.this, "没有找到储存目录", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(PictureChose.this, "没有储存卡", Toast.LENGTH_LONG).show();
                    }
                    //java.io.File
                 /*   file = new java.io.File(com.bbc.lm.menu.PictureChose.this.getExternalFilesDir(android.os.Environment.DIRECTORY_DCIM).getAbsoluteFile(), com.bbc.lm.toools.DateUtils.transformationDate(System.currentTimeMillis(), com.bbc.lm.toools.DateUtils.yyyyMMdd_HHmmss2)+".jpg");
                    com.bbc.lm.log.LogDebug.print(file.toURI().toString());

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, file.toURI().toString());*/
                    startActivityForResult(intent, REQUEST_CODE);

                } else {
                   Picture picture = listPicData.get(position - 1);


                    if (current_type == AUTH_OTHER || AUTH_MODEL == current_type) {
                        android.content.Intent intent = new android.content.Intent();
                        intent.putExtra("choosePic", listPicData.get(position - 1));
                        setResult(Auth_other_result, intent);
                        finish();
                    }

                  /*  else if(AUTH_MODEL == current_type){//模特认证 最多7张

                        if(!picture.isCheck() && checkPic.size() >= (7-model_oldNums)){
                            com.bbc.lm.toools.ToastUtils.getInstance(com.bbc.lm.menu.PictureChose.this).showText("最多可选"+(7-model_oldNums)+"张图片");
                            return;
                        }
                        if(picture.isCheck()){

                            picture.setCheck(false);
                            if (checkPic.contains(picture)) {
                                checkPic.remove(picture);
                            }
                        }else{
                            picture.setCheck(true);
                            if (!checkPic.contains(picture)) {
                                checkPic.add(picture);
                            }
                        }
                        adapter.notifyDataSetChanged();

                        com.bbc.lm.log.LogDebug.print(picture.getData());
                    }*/


                }

            }
        });

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void initData() {
        LoadBitmapUtils.AsynLoadImage(handler, getApplicationContext(), false, false, null, null);

        if (getIntent().getBooleanExtra("auth_model", false)) {

            current_type = AUTH_MODEL;
            model_oldNums = getIntent().getIntExtra("model_oldNums", 0);

        } else if (getIntent().getBooleanExtra("auth_other", false)) {
            current_type = AUTH_OTHER;

        }

        auth_type = getIntent().getStringExtra("auth_type");

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:// 当选择拍照时调用
                File file = new File(Environment.getExternalStorageDirectory()
                        + "/" + localTempImgDir + "/" + localTempImgFileName);
                imagePath = Environment.getExternalStorageDirectory()
                        + "/" + localTempImgDir + "/" + localTempImgFileName;
//                int angle = readPictureDegree(imagePath);//有些手机拍完照片之后会有旋转，此处是获取照片旋转的角度，具体方法内容在后面贴出
//                if (angle != 0)
//                    rotatePhoto(angle);//获取旋转角度之后，将图片旋转回来

                //u就是拍摄获得的原始图片的uri，剩下的你想干神马坏事请便……
                startPhotoZoom(Uri.fromFile(file));
                break;

            case PHOTO_REQUEST_CUT:// 返回的结果
                if (data != null) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
//                    android.graphics.Bitmap thumbnail = data.getParcelableExtra("data");
                    saveImageToGallery(PictureChose.this, bitmap);
                    initData();
                }
                break;
        }

      /*  if (data == null) {
            com.bbc.lm.log.LogDebug.print("onActivityResult 返回null");
            return;
        }*/
    /*    if (REQUEST_CODE == requestCode) {

            startPhotoZoom(Uri.fromFile(file));
            try {
                Uri u = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(getContentResolver(),f.getAbsolutePath(), null, null));
                //u就是拍摄获得的原始图片的uri，剩下的你想干神马坏事请便……
                srcbitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(u));
                image = getImage(Environment.getExternalStorageDirectory()
                      +"/"+localTempImgDir+"/"+localTempImgFileName);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//            isTakePhoto = true;
//            com.bbc.lm.toools.LoadBitmapUtils.AsynLoadImage(handler, getApplicationContext(), false, false,null,null);
//            saveImageToGallery(this,image );
            initData();
            Log.e("aaa", "onActivityResult: "+"=============" );
*/
          /*  if (dir.exists()){
                dir.delete();
            } else {
                Log.e("aaa", "onActivityResult: "+"------------------" );
            }
            if (file.exists()){
                file.delete();
            }*/
//
/*
            if (data != null) { //可能尚未指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                if (data.hasExtra("data")) {//返回有缩略图
                    Log.e("aaa", "onActivityResult: "+srcbitmap.toString());
                    android.graphics.Bitmap thumbnail = data.getParcelableExtra("data");
                    com.bbc.lm.log.LogDebug.print("photo = " + thumbnail.getWidth());
                    com.bbc.lm.log.LogDebug.print("photo = " + thumbnail.getHeight());
                    saveImageToGallery(this, srcbitmap);
                    initData();

//                    原图
//                    android.graphics.Bitmap bp = getBitmapFromUri(data.getData());
//                    com.bbc.lm.log.LogDebug.print("bp = " + bp.getWidth());
//                    com.bbc.lm.log.LogDebug.print("bp = " + bp.getHeight());
                }
            }
*/
                // else { //由于指定了目标uri，存储在目标uri，intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);   通过目标uri，找到图片

//                android.graphics.Bitmap bmp = getBitmapFromUri(android.net.Uri.fromFile(file));
                //                com.bbc.lm.log.LogDebug.print( bmp == null);
//                android.net.Uri uri = android.net.Uri.fromFile(file);
//                android.database.Cursor cursor = this.getContentResolver().query(uri, com.bbc.lm.toools.LoadBitmapUtils.projection, null,null, null);//"_data desc"
              /* if(data.getData() == null){
                   com.bbc.lm.log.LogDebug.print("onActivityResult data.getData()=null");
                   return;
               }*/
     /*           android.database.Cursor cursor = this.getContentResolver().query(data.getData(), com.bbc.lm.toools.LoadBitmapUtils.projection, null,null, null);//"_data desc"

                while (cursor != null && cursor.moveToNext()){

                    com.bbc.lm.beans.Picture pic = new com.bbc.lm.beans.Picture(
                            cursor.getString(cursor.getColumnIndex(com.bbc.lm.toools.LoadBitmapUtils.projection[0])),
                            cursor.getString(cursor.getColumnIndex(com.bbc.lm.toools.LoadBitmapUtils.projection[1])),
                            cursor.getString(cursor.getColumnIndex(com.bbc.lm.toools.LoadBitmapUtils.projection[2])),
                            cursor.getString(cursor.getColumnIndex(com.bbc.lm.toools.LoadBitmapUtils.projection[3])),
                            cursor.getString(cursor.getColumnIndex(com.bbc.lm.toools.LoadBitmapUtils.projection[4])),
                            cursor.getString(cursor.getColumnIndex(com.bbc.lm.toools.LoadBitmapUtils.projection[5])),
                            cursor.getString(cursor.getColumnIndex(com.bbc.lm.toools.LoadBitmapUtils.projection[6])),
                            cursor.getString(cursor.getColumnIndex(com.bbc.lm.toools.LoadBitmapUtils.projection[7])),
                            cursor.getString(cursor.getColumnIndex(com.bbc.lm.toools.LoadBitmapUtils.projection[8])),
                            cursor.getString(cursor.getColumnIndex(com.bbc.lm.toools.LoadBitmapUtils.projection[9])));
                    com.bbc.lm.log.LogDebug.print( pic.toString() );
                }
*/

//            }

    }

   /* public int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    public void rotatePhoto(int angle) {

        WindowManager wm = getWindowManager();
        int windowWidth = wm.getDefaultDisplay().getWidth();
        int windowHeight = wm.getDefaultDisplay().getHeight();

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, opts);
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmapHeight = opts.outHeight;
        bitmapWidth = opts.outWidth;
        if (bitmapHeight > windowHeight || bitmapWidth > windowWidth) {
            scaleX = bitmapWidth / windowWidth;
            scaleY = bitmapHeight / windowHeight;
            if (scaleX > scaleY)
                //按照水平方向的比例缩放
                opts.inSampleSize = scaleX;
            else //按照竖直方向的比例缩放
                opts.inSampleSize = scaleY;
        } else {//图片比手机屏幕小 不缩放
            opts.inSampleSize = 1;
        }
        opts.inJustDecodeBounds = false;
        Matrix m = new Matrix();
        m.postRotate(angle);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(imagePath, opts);
            if (bitmap == null)
                return;
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);// 重新生成图片
//            saveImageToGallery(PictureChose.this, bitmap);//将新生成的图片保存的指定位置，以供后面裁剪时调用，具体代码就不贴出了
        } catch (OutOfMemoryError e) {
            System.gc();
        } finally {
            if (bitmap != null)
                bitmap.recycle();
        }
    }
*/
    //拍照后对照片进行裁剪
    private void startPhotoZoom(Uri uri) {
       WindowManager wm = getWindowManager();
        int windowWidth = wm.getDefaultDisplay().getWidth();
        int windowHeight = wm.getDefaultDisplay().getHeight();

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, opts);
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmapHeight = opts.outHeight;
        bitmapWidth = opts.outWidth;
        if (bitmapHeight > windowHeight || bitmapWidth > windowWidth) {
            scaleX = bitmapWidth / windowWidth;
            scaleY = bitmapHeight / windowHeight;
            if (scaleX > scaleY)
                //按照水平方向的比例缩放
                opts.inSampleSize = scaleX;
            else //按照竖直方向的比例缩放
                opts.inSampleSize = scaleY;
        } else {//图片比手机屏幕小 不缩放
            opts.inSampleSize = 1;
        }
//        opts.inJustDecodeBounds = false;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", bitmapHeight/bitmapWidth);
        intent.putExtra("aspectY", bitmapWidth/bitmapHeight);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", bitmapWidth);
        intent.putExtra("outputY", bitmapHeight);
//        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        //uritempFile为Uri类变量，实例化uritempFile
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("scale", true)//黑边
                .putExtra("scaleUpIfNeeded", true)//黑边
                .putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //根据路径获取用户选择的图片
    public static Bitmap getImage(String imgPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;//直接设置它的压缩率，表示1/2
        Bitmap b = null;
        try {
            b = BitmapFactory.decodeFile(imgPath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }


    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
       /* // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File())));*/
    }


    private android.graphics.Bitmap getBitmapFromUri(android.net.Uri uri) {
        try {
            // 读取uri所在的图片
            android.graphics.Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            //            android.util.Log.e("[Android]", e.getMessage());
            //            android.util.Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }


    class Adapter extends android.widget.BaseAdapter {


        private android.view.animation.Animation animation;

        public Adapter() {
            animation = android.view.animation.AnimationUtils.loadAnimation(PictureChose.this, R.anim.gf_flip_horizontal_in);
        }

        @Override
        public int getCount() {
            return (listPicData == null || listPicData.isEmpty()) ? 1 : listPicData.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) return null;
            return listPicData.get(position - 1);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {

            final ViewHolder holder;
            if (convertView == null) {
                holder = new PictureChose.ViewHolder();
                convertView = android.view.LayoutInflater.from(parent.getContext()).inflate(R.layout.chose_item, null);
//                holder.cover = (android.widget.ImageView) convertView.findViewById(com.bbc.lm.R.id.cover);
                holder.img = (android.widget.ImageView) convertView.findViewById(R.id.img);
                holder.check = (android.widget.CheckBox) convertView.findViewById(R.id.check);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == 0) {//拍照

                holder.check.setVisibility(android.view.View.GONE);
                holder.img.setImageResource(R.drawable.add_pic);


            } else {

                final Picture data = listPicData.get(position - 1);

                holder.check.setVisibility(android.view.View.VISIBLE);

                holder.check.setChecked(data.isCheck());
//                holder.check.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(android.widget.CompoundButton compoundButton, boolean b) {
//
//                        com.bbc.lm.log.LogDebug.print(b+""+data.getDisplay_name() );
//
//                        data.setCheck(b);
//                        if(b){
//                            if(!checkPic.contains(data))
//                                checkPic.add(data);
//                        }else{
//                            if(checkPic.contains(data))
//                                checkPic.remove(data);
//                        }
//
//                        notifyDataSetChanged();
//                    }
//                });

//                holder.img.setImageResource(R.drawable.zanweitu);
//                if(holder.img.getAnimation() != null && !holder.img.getAnimation().hasEnded()){
//                    holder.img.getAnimation().cancel();
//                }
//                holder.img.startAnimation(animation);

                //                holder.cover.setImageResource(com.bbc.lm.R.drawable.zanweitu);
                //                if(holder.cover.getAnimation() != null && !holder.cover.getAnimation().hasEnded()){
                //                    holder.cover.getAnimation().cancel();
                //                }
                //                holder.cover.startAnimation(animation);

                Xutils.setRemoteImg(holder.img, data.getData(), new org.xutils.common.Callback.CommonCallback<android.graphics.drawable.Drawable>() {
                    @Override
                    public void onSuccess(android.graphics.drawable.Drawable result) {

//                        if(holder.cover.getAnimation() != null && !holder.cover.getAnimation().hasEnded()){
//                            holder.cover.getAnimation().cancel();
//                        }
//                        holder.cover.setVisibility(android.view.View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
//                        holder.cover.setVisibility(android.view.View.VISIBLE);
//                        holder.img.setImageResource(R.drawable.zanweitu);
                    }

                    @Override
                    public void onCancelled(org.xutils.common.Callback.CancelledException cex) {
//                        holder.cover.setVisibility(android.view.View.VISIBLE);
                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }

            return convertView;
        }


    }

    android.widget.AbsListView.LayoutParams layoutParams;

    class ViewHolder {

        android.widget.ImageView img;
        android.widget.CheckBox check;
//        android.widget.ImageView cover;

        public ViewHolder() {
//            img = new android.widget.ImageView(PictureChose.this);
//            if(layoutParams == null)
//                layoutParams = new android.widget.AbsListView.LayoutParams(com.bbc.lm.toools.DimensionConvert.dip2px(PictureChose.this,80f),com.bbc.lm.toools.DimensionConvert.dip2px(PictureChose.this,80f));
//            img.setLayoutParams(layoutParams);
//            img.setPadding(0, com.bbc.lm.toools.DimensionConvert.dip2px(PictureChose.this,6f),0,com.bbc.lm.toools.DimensionConvert.dip2px(PictureChose.this,6f));
        }

//        public android.view.View getView(){
//            return img;
//        }

    }


}
