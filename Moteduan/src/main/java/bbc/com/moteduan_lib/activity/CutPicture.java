package bbc.com.moteduan_lib.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import com.edmodo.cropper.CropImageView;

import java.io.File;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.home.EditActivity;
import bbc.com.moteduan_lib.tools.BitMapUtils;
import bbc.com.moteduan_lib.tools.IO_Utils;
import bbc.com.moteduan_lib.tools.ScreenUtils;

/**
 * Created by Administrator on 2016/7/1 0001.
 */
public class CutPicture extends BaseActivity {

    private String picPath;
    private android.graphics.Bitmap bitmap;
    private CropImageView cropImageView;

    private String from;

    /**
     当前所属类型
     */
    public static int current_type ;


    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cut_pic);

        picPath = getIntent().getStringExtra("pic");
        from = getIntent().getStringExtra("type");
        current_type = getIntent().getIntExtra("type",-1);


        initView();
        initData();
    }

    @Override
    public void initView() {


        /*
               Attribute Name	Related Method	                Default Value	Description
        custom:imageResource	setImageResource(int resId)     	0	        set图像
        custom:guidelines	    setGuidelines(integer)	            1	        设置是否显示剪切窗口中的辅助九宫格线 0-2
        custom:fixAspectRatio	setFixedAspectRatio(boolean)	    false	    修正纵横比。如果这是关闭的，剪切窗口将复位和默认的图像的大小，但在每一方有10%个填充。如果这是打开的，剪切窗口将重置和扩大尽可能多的纵横比。
        custom:aspectRatioX	    See setAspectRatio()	            1	        设置纵横比的x值，其中纵横比等于X / Y。默认值为1 / 1。
        custom:aspectRatioY	    See setAspectRatio()	            1	        设置纵横比的Y值，其中纵横比等于X / Y。默认值为1 / 1。
                                setAspectRatio(int X, int Y)	    1, 1	    设置剪切框的 X和Y值的纵横比，其中的纵横比等于X / Y。默认为1 / 1
                                rotateImage(int degrees)	        0	        顺时针旋转图像
         */
        cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        cropImageView.setAspectRatio(1, 1);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setGuidelines(2);
        try {
            bitmap = BitMapUtils.decodeScaleBitmapFromFilePathWhen_over_screen_size(this,
                    picPath,
                    (ScreenUtils.getScreenWidth(this)),
                    (ScreenUtils.getScreenHeight(this)));
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }

        if(bitmap   != null)
            cropImageView.setImageBitmap(bitmap);

    }

    @Override
    public void initData() {

    }


    public void back(android.view.View view){

        finish();

    }

    public void ok(View view){
        Bitmap bmp = cropImageView.getCroppedImage();
        if(bmp != null){
            Intent it = new Intent();
            switch (current_type){
               /* case PictureChoseActivity2.INVITATION://发布邀约

                    it.setClass(this, com.bbc.lm.invitation.UpLoadPhoto.class);

                    break;*/
                case PictureChoseActivity2.LOGINSTEP2:// 旧版注册获取头像

                    break;
                case PictureChoseActivity2.REGISTER_THREE_INFM://新版注册获取头像

                    it.setClass(this, EditActivity.class);

                    break;
             /*   case PictureChoseActivity2.TouXiang://编辑获取头像
                    it.setClass(this, BianJiActivity.class);*/
            }

            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            it.putExtra("picPath",picPath);
//            it.putExtra("cropPicPath",new com.bbc.lm.toools.IO_Utils().saveBitmapCache(bmp,new java.io.File(getExternalCacheDir().getAbsoluteFile(),"liemoIcon.jpg")));

            File iconF = new File(getExternalCacheDir().getAbsoluteFile(), System.currentTimeMillis()+".jpg");
            if(iconF.exists()){
                iconF.delete();
            }
            it.putExtra("cropPicPath",new IO_Utils().saveBitmapCache(bmp,iconF));
            startActivity(it);
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap   != null){
            bitmap.recycle();
            bitmap = null;
        }
    }

}
