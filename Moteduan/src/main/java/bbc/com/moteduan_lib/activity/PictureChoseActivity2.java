package bbc.com.moteduan_lib.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import org.xutils.common.Callback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.ReleaseDate.ReleaseSkill;
import bbc.com.moteduan_lib.app.App;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.adapter.PictureAdapter;
import bbc.com.moteduan_lib.bean.Picture;
import bbc.com.moteduan_lib.bean.PictureGroup;
import bbc.com.moteduan_lib.constant.Constants;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.home.DetailActivity;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.mywidget.DialogProgress;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib.oss.OSSManager;
import bbc.com.moteduan_lib.tools.LoadBitmapUtils;
import bbc.com.moteduan_lib.tools.PermissionHelper;
import bbc.com.moteduan_lib.tools.ToastUtils;
import bbc.com.moteduan_lib.network.Xutils3.Xutils;

public class PictureChoseActivity2 extends BaseActivity implements android.view.View.OnClickListener {

    /**
     * 聊天发送图片
     */
    public static final int Chat_ACTIVITY = 1000;

    /**
     * 发布图片说说
     */
    public static final int RELEASE_ACTIVITY = 1001;

    /**
     * 发布邀约
     */
    public static final int INVITATION = 1002;

    /**
     * 新版注册获取头像
     */
    public static final int REGISTER_THREE_INFM = 1003;

    /**
     * 旧版注册获取头像
     */
    public static final int LOGINSTEP2 = 1004;

    /**
     * 从视频录制界面而来，选中图片合成视频
     */
    public static final int VIDEO_RECORD_ACTIVITY = 1005;
    /**
     * 模特认证
     */
    public static final int AUTH_MODEL = 1006;

    /**
     * 从形象照上传而来
     */
    public static final int XXZ = 1007;
    /**
     * 从修改头像而来
     */
    public static final int TouXiang = 1008;
    /**
     * 当前所属类型
     */
    public static int current_type;

    private boolean isFromReleaseActivity = false;//发布图片说说
    private boolean isFromInvitation;//发布邀约过来选图片
    private boolean isFromLoginStep2 = false;//单选一张作为头像
    private boolean RegisterThree_Infm = false;//新版注册获取头像
    private boolean isFromVideoRecordActivity = false;//合成视频
//    private boolean isFrom

    private int oldNums;
    private int model_oldNums;//模特认证 已经选中数量
    private final int choseNums = 15; // 制作视频，最多可选图片数量

    private static final int REQUEST_CODE = 110;

    public static boolean isUpdate = false;

    private ImageButton back;
    private Button photo;
    private Button sendImg;
    private GridView mGridView;
    private PictureAdapter picAdapter;
    /**
     * 适配器数据
     */
    private List<Object> listPicData = new ArrayList<Object>();
    /**
     * 选中的pic
     */
    private List<Picture> checkPic;

    //	private ArrayList<String> picPath;//组内的图片地址
    /**
     * 所有的图片组数据
     */
    private List<PictureGroup> picGroup;//用来存放  组的数据
    /**
     * 每次点击组的内部数据，赋值给listPicGroup
     */
//    private java.util.List<com.bbc.lm.beans.Picture> listPic;//组内的图片
    public static final int upload_front_request = 1000;
    public static final int upload_behind_request = 2000;
    public static final int upload_hand_front_request = 3000;

    private int type = -1;


    public static final int IMAGE_REQUEST_CODE = 111;
    public static final int IMAGE_RESULT_CODE = 112;

    private ArrayList<Picture> picList;  //所选图片

    private GridView picGridView;


    private int grideH;//高像素值px


    private String url = "";

    private static final int progress_flag = 1111;
    private static final int error_flag = 2222;
    private static final int success_flag = 3333;
    private static final int auth_success_flag = 4444;
    private static final int auth_error_flag = 5555;
    private static final int auth_cancle_flag = 6666;


    private static final int LOAD_PIC_OK = 1;


    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            if (msg.what == LOAD_PIC_OK) {
                listPicData.clear();
                listPicData.addAll((List<PictureGroup>) msg.obj);
                Collections.reverse(listPicData);
                picAdapter.notifyDataSetChanged();
            }

            switch (msg.what) {

                case progress_flag:

                    int progress = (int) msg.obj;

                    break;
                case error_flag:
                    LogDebug.err("上传阿里云失败");
                    break;
                case success_flag:
                    LogDebug.err("上传阿里云成功");
                    break;
                case auth_success_flag:
                    dialogProgress.dismiss();
                    LogDebug.err("照片添加成功");
                    toast.showText("照片添加成功");
                    Intent intent = new Intent(PictureChoseActivity2.this, DetailActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case auth_error_flag:
                    dialogProgress.dismiss();
                    LogDebug.err("照片添加失败");
                    toast.showText("照片添加失败");
                    finish();
                    break;
                case auth_cancle_flag:
                    dialogProgress.dismiss();
                    toast.showText("已取消上传");
                    break;
            }
        }

    };
    private Intent intent;
    private DialogProgress dialogProgress;
    private OSSManager ossManager;
    private View rootView;
    private PermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic);
        rootView = View.inflate(PictureChoseActivity2.this, R.layout.pic, null);
        ossManager = OSSManager.getInstance();
        dialogProgress = new DialogProgress(rootView, this);

        mPermissionHelper = new PermissionHelper(this);
        mPermissionHelper.checkPermisson(new PermissionHelper.OnPermissionListener() {
            @Override
            public void onAgreePermission() {
                // do something
                initView();
                initData();

                intent = getIntent();
                oldNums = intent.getIntExtra("oidNums", 0);

                //从广场图片说说 过来
                if ((isFromReleaseActivity = intent.getBooleanExtra("release", false)) || intent.getBooleanExtra("squre", false)) {
                    current_type = RELEASE_ACTIVITY;
//            sendImg.setVisibility(android.view.View.GONE);
                    sendImg.setText("确定");

                } else if ((isFromLoginStep2 = intent.getBooleanExtra("login", false))) {//登陆注册

                    current_type = LOGINSTEP2;
                    sendImg.setVisibility(View.GONE);

                } else if ((RegisterThree_Infm = intent.getBooleanExtra("RegisterThree_Infm", false))) {//改版后的注册

                    current_type = REGISTER_THREE_INFM;
                    sendImg.setVisibility(View.GONE);

                } else if ((isFromInvitation = intent.getBooleanExtra("ReleaseInvitation_chosePhoto", false))) {//发布邀约

                    current_type = INVITATION;
                    sendImg.setVisibility(View.GONE);
                } else if (intent.getBooleanExtra("Auth_model", false)) {

                    current_type = AUTH_MODEL;
                    sendImg.setVisibility(View.GONE);
                    oldNums = intent.getIntExtra("model_oldNums", 0);

                } else if (intent.getBooleanExtra("XXZ", false)) {     //形象照
                    current_type = XXZ;
                    sendImg.setText("添加");
                } else if (intent.getBooleanExtra("xiuGaiTouXiang", false)) {   //头像
                    current_type = TouXiang;
                    sendImg.setText("完成");
                }

                //从视频录制界面过来  挑选照片合成视频，隐藏发送和拍照按钮。
                else if ((isFromVideoRecordActivity = intent.getBooleanExtra("videoRecord", false))) {
                    current_type = VIDEO_RECORD_ACTIVITY;
//            sendImg.setVisibility(android.view.View.GONE);
                    sendImg.setText("合成");
                } else {
                    current_type = Chat_ACTIVITY;
                }
            }

            @Override
            public void onDeniedPermission() {
                ToastUtils.getInstance(App.getApp().getBaseContext()).showText("拒绝了访问本地权限");
                finish();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);


    }


    @Override
    protected void onResume() {
        super.onResume();
        mPermissionHelper.onResume(); // 当界面一定通过权限才能继续，就要加上这行
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void initData() {

//		注册媒体图片库变动
//		registerContentObserver(new PictureObserver(handler,this));

//        long i = System.currentTimeMillis();
        LoadBitmapUtils.AsynLoadImage(handler, getApplicationContext(), false, false, null, null);
    }

    public void initView() {

        checkPic = new ArrayList<Picture>();
        picGroup = new ArrayList<PictureGroup>();

//        picPath = new java.util.ArrayList<String>();
//        listPic = new java.util.ArrayList<com.bbc.lm.beans.Picture>();

        back = (ImageButton) this.findViewById(R.id.back);
        back.setOnClickListener(this);

//        photo = (android.widget.Button) this.findViewById(com.bbc.lm.R.id.photo);
//        photo.setOnClickListener(this);

        sendImg = (Button) this.findViewById(R.id.sendImg);
        sendImg.setOnClickListener(this);
        if (isFromReleaseActivity) sendImg.setVisibility(android.view.View.GONE);

        picAdapter = new PictureAdapter(PictureChoseActivity2.this, listPicData);

        mGridView = (GridView) this.findViewById(R.id.gridView);
        mGridView.setAdapter(picAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view,
                                    int position, long id) {

                Picture picture = (Picture) parent.getItemAtPosition(position);
                if (position == 0) {
                    //启动系统相机
                    try {
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        PictureChoseActivity2.this.startActivityForResult(intent, REQUEST_CODE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    switch (current_type) {
                        case INVITATION://发布邀约

                            Intent it = new android.content.Intent();
                            it.setClass(PictureChoseActivity2.this, CutPicture.class);
                            it.putExtra("pic", picture.getData());
                            it.putExtra("type", INVITATION);
                            startActivity(it);

                            break;
                        case REGISTER_THREE_INFM://新版注册获取头像

                            Intent it3 = new Intent();
                            it3.setClass(PictureChoseActivity2.this, CutPicture.class);
                            it3.putExtra("pic", picture.getData());
                            it3.putExtra("type", REGISTER_THREE_INFM);
                            startActivity(it3);

                            break;

                        case LOGINSTEP2://旧版注册获取头像

                            android.content.Intent it2 = new android.content.Intent();
                            it2.setClass(PictureChoseActivity2.this, CutPicture.class);
                            it2.putExtra("pic", picture.getData());
                            it2.putExtra("type", LOGINSTEP2);
                            startActivity(it2);

                            break;

                        case RELEASE_ACTIVITY://发布图片说说

                            if (!picture.isCheck() && checkPic.size() == (6 - oldNums)) {
                                ToastUtils.getInstance(PictureChoseActivity2.this).showText("最多可选" + (6 - oldNums) + "张图片");
                                return;
                            }

                            picAdapter.setCheck(picture);
                            if (checkPic.contains(picture)) {
                                checkPic.remove(picture);
                            } else {
                                checkPic.add(picture);
                            }

                            break;
                        case AUTH_MODEL://模特认证 最多7张

                            if (!picture.isCheck() && checkPic.size() == (7 - oldNums)) {
                                ToastUtils.getInstance(PictureChoseActivity2.this).showText("最多可选" + (7 - oldNums) + "张图片");
                                return;
                            }

                            picAdapter.setCheck(picture);
                            if (checkPic.contains(picture)) {
                                checkPic.remove(picture);
                            } else {
                                checkPic.add(picture);
                            }

                            break;
                        case VIDEO_RECORD_ACTIVITY://从视频录制界面而来，选中图片合成视频

                            if (!picture.isCheck() && isFromVideoRecordActivity && checkPic.size() == choseNums) {
                                ToastUtils.getInstance(PictureChoseActivity2.this).showText("最多可选" + choseNums + "张图片");
                                return;
                            }

                            picAdapter.setCheck(picture);
                            if (checkPic.contains(picture)) {
                                checkPic.remove(picture);
                            } else {
                                checkPic.add(picture);
                            }

                        case Chat_ACTIVITY:

                            picAdapter.setCheck(picture);
                            if (checkPic.contains(picture)) {
                                checkPic.remove(picture);
                            } else {
                                checkPic.add(picture);
                            }

                            break;

                        case XXZ:                   //从形象照过来，选中任意数量图片上传

                            if (!picture.isCheck()) {
//                                ToastUtils.getInstance(PictureChoseActivity2.this).showText("可上传任意多张形象照");
                            }
                            picAdapter.setCheck(picture);
                            if (checkPic.contains(picture)) {
                                checkPic.remove(picture);
                            } else {
                                checkPic.add(picture);
                            }
                            break;

                        case TouXiang:   //从头像上传而来，只能选一张上传
                            if (!picture.isCheck() && checkPic.size() == (1 - oldNums)) {
                                ToastUtils.getInstance(PictureChoseActivity2.this).showText("只能选" + (1 - oldNums) + "张图片");
                                return;
                            }

                            picAdapter.setCheck(picture);

                            if (checkPic.contains(picture)) {
                                checkPic.remove(picture);
                            } else {
                                checkPic.add(picture);
                            }

                            android.content.Intent it4 = new android.content.Intent();
                            it4.setClass(PictureChoseActivity2.this, CutPicture.class);
                            it4.putExtra("pic", picture.getData());
                            it4.putExtra("type", TouXiang);
                            startActivity(it4);
                            break;
                    }
                }
//                if(RegisterThree_Infm || isFromLoginStep2 || isFromInvitation){//单选一张后剪切
//
//                    android.content.Intent it= new android.content.Intent();
//                    it.setClass(PictureChoseActivity2.this, com.bbc.lm.login.CutPicture.class);
//                    it.putExtra("pic",picture.getData());
//                    it.putExtra("type",isFromInvitation?"isFromInvitation":"isFromLoginStep2");
//                    startActivity(it);
//
////                    finish();
//                }else{
//
//                    if(!picture.isCheck() && isFromReleaseActivity && checkPic.size() == (6-oldNums)){
//                        com.bbc.lm.toools.ToastUtils.getInstance(PictureChoseActivity2.this).showText("最多可选"+(6-oldNums)+"张图片");
//                        return;
//                    }
//
//                    if(!picture.isCheck() && isFromVideoRecordActivity && checkPic.size() == choseNums){
//                        com.bbc.lm.toools.ToastUtils.getInstance(PictureChoseActivity2.this).showText("最多可选"+ choseNums +"张图片");
//                        return;
//                    }
//
//                    picAdapter.setCheck(picture);
//                    if (checkPic.contains(picture)) {
//                        checkPic.remove(picture);
//                    } else {
//                        checkPic.add(picture);
//                    }
//                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            back();


        } else if (i == R.id.sendImg) {
            if (current_type == AUTH_MODEL) {

//                    android.content.Intent intent = new android.content.Intent();
//                    intent.putParcelableArrayListExtra("pic", (java.util.ArrayList<? extends android.os.Parcelable>) checkPic);
//                    setResult(com.bbc.lm.menu.Auth_model.IMAGE_RESULT_CODE, intent);
//                    finish();

            } else if (current_type == RELEASE_ACTIVITY) {

                back();

            } else if (current_type == XXZ) {
                //上传照片
                updateLoadPhoto(checkPic);
            }

        }
    }


    private void updateLoadPhoto(List<Picture> pics) {

        dialogProgress.show();
        dialogProgress.setJiazai_text("正在上传...");
        new Thread(new UpLoadThread(pics)).start();


    }


    class UpLoadThread implements Runnable {
        private List<Picture> pics;

        public UpLoadThread(List<Picture> pics) {
            this.pics = pics;
        }

        @Override
        public void run() {
            StringBuffer sbf = new StringBuffer();
            try {
                for (int i = 0; i < pics.size(); i++) {
                    String pathurlxxz = pics.get(i).getData();
                    Log.e("aaa", "run: " + pathurlxxz);
                    /*Message msg = new Message();
                    msg.what = i;
                    handler.sendMessage(msg);*/
                   PutObjectResult putResult = ossManager.synchUpLoad(pathurlxxz, Constants.IMAGE_TYPE, new OSSProgressCallback() {
                        @Override
                        public void onProgress(Object o, long currentSize, long totalSize) {
                            Message message = new Message();
                            message.what = progress_flag;
                            int progress = Math.round((float) currentSize / totalSize * 100L);
                            message.obj = progress;
                            handler.sendMessage(message);
                        }
                    });
                    String url = putResult.getETag();
                    sbf.append(i == 0 ? url : "," + url);
                    LogDebug.err(url);
                }

            } catch (ClientException | ServiceException e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.what = error_flag;
                handler.sendMessage(msg);
                return;
            }

            Message msg = new Message();
            msg.what = success_flag;
            handler.sendMessage(msg);
            map.put("path", sbf.toString());

            auth_ID();

        }
    }


    private Map<String, Object> map = new HashMap<>();

    private void auth_ID() {

        map.put("m_id", SpDataCache.getSelfInfo(PictureChoseActivity2.this).getData().getM_head_photo());
//        map.put("realName", "");
//        map.put("personCardnum", "");


        Xutils.post(Url.shangChuanXingXiangZhao, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                LogDebug.print(result);
                Message msg = handler.obtainMessage();
                msg.what = auth_success_flag;
                handler.sendMessage(msg);


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogDebug.print(ex.toString());
                Message msg = handler.obtainMessage();
                msg.what = auth_error_flag;
                handler.sendMessage(msg);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

                android.os.Message msg = handler.obtainMessage();
                msg.what = auth_cancle_flag;
                handler.sendMessage(msg);
            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void back() {
        Intent intent = new android.content.Intent();
        intent.putParcelableArrayListExtra("choosePic", (ArrayList<? extends Parcelable>) checkPic);
        setResult(ReleaseSkill.IMAGE_REQUEST_CODE, intent);
        /*if (isFromReleaseActivity) {
            android.content.Intent intent = new android.content.Intent();
            intent.putParcelableArrayListExtra("choosePic", (java.util.ArrayList<? extends android.os.Parcelable>) checkPic);
            setResult(com.bbc.lm.square.activity.ReleaseActivity.IMAGE_REQUEST_CODE, intent);
        } else if (getIntent().getBooleanExtra("squre",false)){
            android.content.Intent intent = new android.content.Intent(PictureChoseActivity2.this,ReleaseActivity.class);
            intent.putParcelableArrayListExtra("choosePic", (java.util.ArrayList<? extends android.os.Parcelable>) checkPic);
            intent.putExtra("type","image");
            PictureChoseActivity2.this.startActivity(intent);
        }*/
        finish();
    }

    /**
     * 发送图片消息
     */
    public void sendImg() {
       /* android.os.Parcelable[] picArray = new android.os.Parcelable[checkPic.size()];
        LogDebug.print("开启服务发送图片:" + checkPic.size());
        for (int i = 0; i < checkPic.size(); i++) {
            picArray[i] = checkPic.get(i);

            LogDebug.print(checkPic.get(i).getDisplay_name());

            //开启服务发送图片
            android.content.Intent it = new android.content.Intent(ActionContent.UPLOAD_SERVICE);//"com.lm.im.SENDIMG.intentservice"
            it.setPackage(getPackageName());//Android 5.0要求显示启动
            it.putExtra("imageFileThumbID", checkPic.get(i).get_id());
            it.putExtra("imageFileSource", checkPic.get(i).getData());
            it.putExtra("targetId", intent.getStringExtra("targetId"));
            startService(it);

        }
        intent.putExtra("pics", picArray);
        setResult(101, intent);*/
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_CODE == requestCode) {

            LoadBitmapUtils.AsynLoadImage(handler, getApplicationContext(), false, false, null, null);

            if (data != null) { //可能尚未指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                if (data.hasExtra("data")) {//返回有缩略图

                    Bitmap thumbnail = data.getParcelableExtra("data");
                    LogDebug.print("photo = " + thumbnail.getWidth());
                    LogDebug.print("photo = " + thumbnail.getHeight());
                    Log.e("aaa", "onActivityResult: " + thumbnail.getWidth());
                    saveImageToGallery(this, thumbnail);
                    initData();
                }
            } else { //由于指定了目标uri，存储在目标uri，intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);   通过目标uri，找到图片


            }

        }

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


    private Bitmap getBitmapFromUri(android.net.Uri uri) {
        try {
            // 读取uri所在的图片
           Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
//            android.util.Log.e("[Android]", e.getMessage());
//            android.util.Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

//	private void registerContentObserver(PictureObserver picObserver) {

//		Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//		getContentResolver().registerContentObserver(imageUri, false, picObserver);
//		logDebug.info(MainActivity.this, "registered!---------------------------");
//	}


    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            back();
        }
        return true;
    }
}